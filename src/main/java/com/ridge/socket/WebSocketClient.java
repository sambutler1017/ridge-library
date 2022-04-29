package com.ridge.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import rx.subjects.BehaviorSubject;

/**
 * Create a websocket instance from a given session handler and base url.
 * Provided methods will then be able to connect to the socket url and integrate
 * the passed in session handler.
 * 
 * @author Sam Butler
 * @since March 30, 2022
 */
public class WebSocketClient {

    private final int RECONNECT_DELAY = 5000;

    private final Logger LOGGER = LoggerFactory.getLogger(WebSocketClient.class);

    private WebSocketStompClient stompClient;

    private StompSession session;

    private StompSessionHandler handler;

    private String url;

    private boolean isAsync = false;

    private boolean isForceDisconnect = false;

    private BehaviorSubject<StompSession> CONNECT_SUBJECT;

    private BehaviorSubject<Void> DISCONNECT_SUBJECT;

    /**
     * Default empty constructor to initializing the object.
     */
    public WebSocketClient() {
        this.CONNECT_SUBJECT = BehaviorSubject.create();
        this.DISCONNECT_SUBJECT = BehaviorSubject.create();
        this.url = "";
        initClient();
    }

    /**
     * Constructor that takes the {@link StompSessionHandler} to use when the
     * connection is established.
     * 
     * @param handler The handler to use for socket connection.
     */
    public <T extends StompSessionHandler> WebSocketClient(T handler) {
        this.CONNECT_SUBJECT = BehaviorSubject.create();
        this.DISCONNECT_SUBJECT = BehaviorSubject.create();
        this.url = "";
        this.handler = handler;
        initClient();
    }

    /**
     * Constructor that takes in a url to send the socket call too.
     * 
     * @param url The url to be used.
     */
    public WebSocketClient(String url) {
        this.CONNECT_SUBJECT = BehaviorSubject.create();
        this.DISCONNECT_SUBJECT = BehaviorSubject.create();
        this.url = url;
        initClient();
    }

    /**
     * Constructor that takes in a url to send the socket call too. Also takes the
     * {@link StompSessionHandler} to use when the connection is established.
     * 
     * @param url     The url to be used.
     * @param handler The handler to use for socket connection.
     */
    public <T extends StompSessionHandler> WebSocketClient(String url, T handler) {
        this.CONNECT_SUBJECT = BehaviorSubject.create();
        this.DISCONNECT_SUBJECT = BehaviorSubject.create();
        this.url = url;
        this.handler = handler;
        initClient();
    }

    /**
     * Async socket connection that will be run on a seperate thread. Once the
     * connection is established it will emit the subject with the new
     * {@link StompSession} of it connected and trigger the change. This will
     * handle socket reconnections if the server is unreachable.
     * <blockquote>
     * 
     * <pre>
     * WebSocketClient client = new WebSocketClient();
     * BehaviorSubject<Boolean> connectionSubject = client.connectAsync("/topic");
     * connectionSubject.subscribe(res -> {
     * });
     * </pre>
     * 
     * </blockquote>
     * 
     * @param url The url to connect the socket too.
     * @return {@link BehaviorSubject} of the connection status
     */
    public BehaviorSubject<StompSession> connectAsync(String url) {
        this.url = url;
        return connectAsync();
    }

    /**
     * Async socket connection that will be run on a seperate thread. Once the
     * connection is established it will emit the subject with the new
     * {@link StompSession} of it connected and trigger the change. This will
     * handle socket reconnections if the server is unreachable.
     * <blockquote>
     * 
     * <pre>
     * WebSocketClient client = new WebSocketClient();
     * MyStompSessionHandler handler = new MyStompSessionHandler();
     * BehaviorSubject<Boolean> connectionSubject = client.connectAsync("/topic", handler);
     * connectionSubject.subscribe(res -> {
     * });
     * </pre>
     * 
     * </blockquote>
     * 
     * @param <T>     The type of the custom stomp session handler.
     * @param url     The url to connect the socket too.
     * @param handler The handler to be used on the socket request.
     * @return {@link BehaviorSubject} of the connection status
     */
    public <T extends StompSessionHandler> BehaviorSubject<StompSession> connectAsync(T handler) {
        this.handler = handler;
        return connectAsync();
    }

    /**
     * Async socket connection that will be run on a seperate thread. Once the
     * connection is established it will emit the subject with the new
     * {@link StompSession} of it connected and trigger the change. This will
     * handle socket reconnections if the server is unreachable.
     * <blockquote>
     * 
     * <pre>
     * WebSocketClient client = new WebSocketClient();
     * MyStompSessionHandler handler = new MyStompSessionHandler();
     * BehaviorSubject<Boolean> connectionSubject = client.connectAsync("/topic", handler);
     * connectionSubject.subscribe(res -> {
     * });
     * </pre>
     * 
     * </blockquote>
     * 
     * @param <T>     The type of the custom stomp session handler.
     * @param url     The url to connect the socket too.
     * @param handler The handler to be used on the socket request.
     * @return {@link BehaviorSubject} of the connection status
     */
    public <T extends StompSessionHandler> BehaviorSubject<StompSession> connectAsync(String url, T handler) {
        this.url = url;
        this.handler = handler;
        return connectAsync();
    }

    /**
     * Async socket connection that will be run on a seperate thread. Once the
     * connection is established it will emit the subject with the new
     * {@link StompSession} of it connected and trigger the change. This will
     * handle socket reconnections if the server is unreachable.
     * <blockquote>
     * 
     * <pre>
     * WebSocketClient client = new WebSocketClient();
     * BehaviorSubject<Boolean> connectionSubject = client.connectAsync();
     * connectionSubject.subscribe(res -> {
     * });
     * </pre>
     * 
     * </blockquote>
     * 
     * @return {@link BehaviorSubject} of the connection status
     */
    public BehaviorSubject<StompSession> connectAsync() {
        this.isAsync = true;
        new Thread(() -> {
            connect();
            this.CONNECT_SUBJECT.onNext(this.session);
            this.onDisconnect().filter(res -> !this.isForceDisconnect).switchMap(res -> this.reconnect()).subscribe();
        }).start();
        return this.CONNECT_SUBJECT;
    }

    /**
     * Establish a connection for the given url. The synchronous connect will not
     * handle reconnections to the server if it disconnects.
     * 
     * @param url The url to connect the socket too.
     * @throws InterruptedException
     */
    public void connect(String url) {
        this.url = url;
        connect();
    }

    /**
     * Establish a connection with the websocket server. This connect method assumes
     * that the base socket url is already set. This will keep retrying to reconnect
     * until the connection can be established. The synchronous connect will not
     * handle reconnections to the server if it disconnects.
     * 
     * @param handler The handler to be used on the socket request.
     * @throws InterruptedException If the connection could not be established.
     */
    public <T extends StompSessionHandler> void connect(T handler) {
        this.handler = handler;
        connect();
    }

    /**
     * Establish a connection for the given url and handler that wants to be used
     * for the request. The synchronous connect will not handle reconnections to the
     * server if it disconnects.
     * 
     * @param url     The url to connect the socket too.
     * @param handler The handler to be used on the socket request.
     * @throws InterruptedException
     */
    public <T extends StompSessionHandler> void connect(String url, T handler) {
        this.url = url;
        this.handler = handler;
        connect();
    }

    /**
     * Basic connect for creating a connection with the socket. This assumes that
     * the url is already set and the the desired handler is set. If not handler is
     * set it will use the default stomp session handler for the connection. The
     * synchronous connect will not handle reconnections to the server if it
     * disconnects.
     * 
     * @throws InterruptedException If the connection could not be established.
     */
    public void connect() {
        this.isForceDisconnect = false;
        this.handler = this.handler == null ? new SocketSessionHandler() : this.handler;

        do {
            LOGGER.info("Connecting to Socket url '{}'...", this.url);
            try {
                ListenableFuture<StompSession> futureClient = stompClient.connect(this.url, this.handler);
                this.session = futureClient.get();
                break;
            } catch (Exception e) {
                LOGGER.info("Could not establish connection. Reconnecting in {} seconds...", RECONNECT_DELAY / 1000);
            }

            try {
                Thread.sleep(RECONNECT_DELAY);
            } catch (InterruptedException e) {
                LOGGER.info("Could not trigger thread to sleep.");
            }
        } while (this.session == null || !this.session.isConnected());

        LOGGER.info("Connection established with session id: '{}'", session.getSessionId());
    }

    /**
     * Reconnect to the connection. This will only get run if the current session is
     * not connected. If the socket client is using an async request it will perform
     * a {@link #connectAsync()} call, otherwise it will do a blocking
     * {@link #connect()} call.
     */
    public BehaviorSubject<StompSession> reconnect() {
        LOGGER.warn("Attempting reconnect...");

        if (this.session != null && !this.session.isConnected()) {
            if (this.isAsync) {
                return this.connectAsync();
            } else {
                this.connect();
                this.onDisconnect();
                return BehaviorSubject.create(this.session);
            }
        }
        return null;
    }

    /**
     * Listen to a socket topic and get the returned behavior subject of the
     * listner. This will take in the topic URL to listen too and the class type of
     * the data being returned in the payload of the listener.
     * 
     * <blockquote>
     * 
     * <pre>
     * WebSocketClient client = new WebSocketClient();
     * client.connect("/api/websocket");
     * BehaviorSubject<CustomClass> listenerSubject = client.listen("/topic", CustomClass.class);
     * listenerSubject.subscribe(res -> {
     * });
     * </pre>
     * 
     * </blockquote>
     * 
     * @param <T>   The class type of the payload.
     * @param url   The URL to subscribe the socket too.
     * @param clazz The class type of the data.
     * @return {@link BehaviorSubject} of the listner.
     */
    public <T> BehaviorSubject<T> listen(String url, Class<T> clazz) {
        BehaviorSubject<T> subject = BehaviorSubject.create();
        this.session.subscribe(url, new SocketFrameHandler<T>(clazz, subject));
        LOGGER.info("Listening to '{}'...", url);
        return subject;
    }

    /**
     * Method that watches when the session connection is disconnected. Once it is
     * it will submit the disconnect as a behavior subject.
     * 
     * @return {@link BehaviorSubject} of the data.
     */
    public BehaviorSubject<Void> onDisconnect() {
        if (this.isAsync) {
            this.DISCONNECT_SUBJECT = BehaviorSubject.create();
        }

        new Thread(() -> {
            while (this.session.isConnected()) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    LOGGER.info("Could not trigger thread to sleep.");
                }
            }
            LOGGER.warn("Socket disconnected from session id '{}'", this.session.getSessionId());
            this.DISCONNECT_SUBJECT.onNext(null);
        }).start();

        return this.DISCONNECT_SUBJECT;
    }

    /**
     * Close the current session.
     */
    public void disconnect() {
        LOGGER.info("Disconnecting websocket...");
        this.isForceDisconnect = true;
        this.session.disconnect();
        this.DISCONNECT_SUBJECT.onNext(null);
    }

    /**
     * Get the currently active session.
     * 
     * @return The current session.
     */
    public StompSession getSession() {
        return this.session;
    }

    /**
     * Initializes the stomp client for the service.
     */
    private void initClient() {
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.setDefaultHeartbeat(new long[] { 20000, 20000 });
        stompClient.setTaskScheduler(taskScheduler());
    }

    /**
     * Creates a default task scheduler for setting a heartbeat with the server
     * 
     * @return {@link ThreadPoolTaskScheduler}
     */
    private TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler ts = new ThreadPoolTaskScheduler();
        ts.setPoolSize(3);
        ts.afterPropertiesSet();
        return ts;
    }
}
