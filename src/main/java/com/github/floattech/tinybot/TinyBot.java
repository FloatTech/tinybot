package com.github.floattech.tinybot;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class TinyBot {

    private static final URI uri = URI.create("ws://127.0.0.1:6700/ws");

    private static final Logger logger = Logger.getLogger("TinyBot");
    private static final Echo echo = new Echo(50);

    public TinyBot(URI uri) throws InterruptedException {
        while (true) {
            Thread.sleep(5000);
            logger.warning("开始连接......");
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
            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String, Object>>() {
            }.getType();
            HashMap<String, Object> context = gson.fromJson(data.toString(), type);
            if (context.get("post_type") == "meta_event") {
                logger.warning("心跳事件 -> " + context.toString());
            } else if (context.get("echo") != "") {
                logger.info("调用返回 -> " + context.toString());
                Integer number = (Integer) context.get("echo");
                Queue<HashMap<String, Object>> queue = echo.queue(number);
                queue.add(context);
            } else {
                logger.info("收到事件 -> " + context.toString());
                Plugin plugin = new Plugin(webSocket, echo, context);
                if (plugin.match()) {
                    plugin.handle();
                }
            }
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