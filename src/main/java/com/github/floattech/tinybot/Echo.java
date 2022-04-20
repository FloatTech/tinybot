package com.github.floattech.tinybot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Echo {
    private Integer number = 0;

    private HashMap<Integer, BlockingQueue<HashMap<String, Object>>> list = new HashMap<>();
    private Integer size;

    public Echo(Integer size) {
        this.size = size;
    }

    public Integer listen() {
        if (list.size() > size * 2) {
            for (Integer key : list.keySet()) {
                if (key > number - size) {
                    continue;
                }
                list.remove(key);
            }
        }
        number++;
        BlockingQueue<HashMap<String, Object>> queue = new ArrayBlockingQueue<>(1);
        list.put(number, queue);
        return number;
    }

    public BlockingQueue<HashMap<String, Object>> queue(Integer number) {
        return list.get(number);
    }
}
