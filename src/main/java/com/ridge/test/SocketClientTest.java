package com.ridge.test;

import com.ridge.socket.WebSocketClient;
import com.ridge.test.domain.CurrentSystemSettings;

public class SocketClientTest {
    private static final String BASE_URL = "ws://localhost:8080/api/websocket";

    public static void run() throws Exception {
        // normalSocketConnectionExample();
        asyncSocketConnectionExample();

        while (true) {
            // Loop as long as session is alive.
        }
    }

    /**
     * Simple example of showing the async process of connecting to a client and
     * then listening on a socket connection and printing out the data when the
     * websocket is triggered.
     */
    public static void asyncSocketConnectionExample() {
        WebSocketClient socketClient = new WebSocketClient();
        socketClient.connectAsync(BASE_URL).subscribe(res -> addListeners(socketClient));
        System.out.println("After Socket Client Connect");
    }

    /**
     * Simple synchronous example of the blocking functionality of the
     * {@link WebSocketClient#connect()}. This will not continue through the code
     * until a connection has been made.
     */
    public static void normalSocketConnectionExample() {
        WebSocketClient socketClient = new WebSocketClient();
        socketClient.connect(BASE_URL);
        addListeners(socketClient);

        socketClient.onDisconnect().subscribe(res -> {
            socketClient.reconnect();
            addListeners(socketClient);
        });
        System.out.println("After Socket Client Connect");
    }

    /**
     * Helper method to add listeners to trigger on for embedded system updates.
     * 
     * @param socketClient The client for the socket.
     */
    private static void addListeners(WebSocketClient socketClient) {
        socketClient.listen("/topic/update-system", CurrentSystemSettings.class)
                .subscribe(res -> System.out.println("Lights On: " + res.getId()));
    }
}
