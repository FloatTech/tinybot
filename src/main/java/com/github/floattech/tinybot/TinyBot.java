package com.github.floattech.tinybot;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class TinyBot {

    private static final URI uri = URI.create("ws://127.0.0.1:6700/ws");

    private static final Logger logger = Logger.getLogger("TinyBot");

    public TinyBot(URI uri) throws InterruptedException {
        while (true) {
            logger.warning("开始重连......");
            Thread.sleep(5000);
            var future = HttpClient.newHttpClient().newWebSocketBuilder()
                    .buildAsync(uri, new WebSocketListener());
            try {
                future.join();
            } catch (Exception e) {
                logger.warning(e.toString());
                continue;
            }
            break;
        }
    }

    private class WebSocketListener implements WebSocket.Listener {

        @Override
        public void onOpen(WebSocket webSocket) {
            System.out.println("websocket opened.");
            webSocket.request(1);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            webSocket.request(1);
            System.out.println(data.toString());
            return null;
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            System.out.println("ws closed with status(" + statusCode + "). cause:" + reason);
            webSocket.sendClose(statusCode, reason);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            try {
                new TinyBot(uri);
            } catch (Exception ignore) {
            }
            return null;
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            System.out.println("error: " + error.getLocalizedMessage());
            webSocket.abort();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            try {
                new TinyBot(uri);
            } catch (Exception ignore) {
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {
        try {
            new TinyBot(uri);
        } catch (Exception ignore) {
        }
        while (true) {
        }
    }
}