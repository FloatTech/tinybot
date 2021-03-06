package com.github.floattech.tinybot;

import com.google.gson.Gson;

import java.net.http.WebSocket;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Plugin {
    private WebSocket webSocket;
    private HashMap<String, Object> context;

    private String superUser;
    private Echo echo;

    public Plugin(WebSocket webSocket, Echo echo, HashMap<String, Object> context) {
        this.webSocket = webSocket;
        this.echo = echo;
        this.context = context;
    }

    public Boolean match() {
        return true;
    }

    public Boolean handle() {
        return true;
    }

    public Boolean onMessage() {
        return this.context.get("post_type") == "message";
    }

    public Boolean onFullMatch(String keyword) {
        return onMessage() && this.context.get("message") == keyword;
    }

    public Boolean onRegexpMatch(String pattern) {
        return onMessage() && Pattern.matches(pattern, this.context.get("message").toString());
    }

    public Boolean onlyToMe() {
        // Todo 增加其他事件
        return this.context.get("post_type") == "notice" && this.context.get("notice_type") == "group_admin"
                && this.context.get("user_id") == this.context.get("self_id");
    }

    public Boolean isSuperUser() {
        // Todo 增加其他事件
        return this.context.get("user_id") == this.superUser;
    }

    public Boolean isAdminUser() {
        HashMap<String, Object> sender = (HashMap<String, Object>) this.context.get("sender");
        return isSuperUser() || sender.get("role") == "owner" || sender.get("role") == "admin";
    }

    public HashMap<String, Object> callAPI(String action, HashMap<String, Object> params) {
        Gson gson = new Gson();
        HashMap<String, Object> req = new HashMap<>();
        req.put("action", action);
        req.put("params", params);
        req.put("echo", 0);
        Integer number = echo.listen();
        this.webSocket.sendText(gson.toJson(req), true);
        try {
            return echo.queue(number).poll(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return null;
        }
    }

    public Boolean sendMsg(Message[] messages) {
        if (this.context.get("group_id") != "") {
            Integer groupID = (Integer) this.context.get("group_id");
            return sendGroupMsg(groupID, messages);
        } else {
            Integer userID = (Integer) this.context.get("user_id");
            return sendGroupMsg(userID, messages);
        }
    }

    public Boolean sendGroupMsg(Integer groupID, Message[] messages) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("group_id", groupID);
        params.put("message", messages);
        return callAPI("send_group_msg", params) == null;
    }

    public Boolean sendPrivateMsg(Integer userID, Message[] messages) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("user_id", userID);
        params.put("message", messages);
        return callAPI("send_private_msg", params) == null;
    }
}
