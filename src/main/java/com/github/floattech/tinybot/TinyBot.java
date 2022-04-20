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
            Thread.sleep(5000);
            logger.warning("开始重连......");
            CompletableFuture<WebSocket> future = HttpClient.newHttpClient().newWebSocketBuilder()
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
            logger.info("连接成功");
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            webSocket.request(1);
            logger.info(data.toString());
            return null;
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            webSocket.abort();
            logger.info(error.getLocalizedMessage());
            try {
                new TinyBot(uri);
            } catch (Exception e) {
                logger.info(e.toString());
                System.exit(0);
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {
        try {
            new TinyBot(uri);
        } catch (Exception e) {
            logger.info(e.toString());
            System.exit(0);
        }
        while (true) {
        }
    }
}