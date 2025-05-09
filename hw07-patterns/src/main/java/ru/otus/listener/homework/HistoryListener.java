package ru.otus.listener.homework;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import ru.otus.listener.Listener;
import ru.otus.model.Message;

public class HistoryListener implements Listener, HistoryReader {

    final HashMap<Long, Deque<Message>> history = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        history.computeIfAbsent(msg.getId(), k -> new ArrayDeque<>()).add(msg.clone());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        Deque<Message> deque = history.getOrDefault(id, new ArrayDeque<>());
        return deque.isEmpty() ? Optional.empty() : Optional.of(deque.peek());
    }
}
