package com.ridge.socket;

import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

/**
 * Default Stomp session handler.
 * 
 * @author Sam Butler
 * @since March 30, 2022
 */
public class SocketSessionHandler implements StompSessionHandler {

    @Override
    public void afterConnected(StompSession session, StompHeaders headers) {
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return null;
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
            Throwable exception) {
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
    }
}
