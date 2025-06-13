package com.example.todo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;


import javax.annotation.PostConstruct;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TaskService {
    private static final String DATA_FILE = "tasks.json";
    private final ObjectMapper mapper = new ObjectMapper();
    private final List<Task> tasks = new ArrayList<>();
    private final AtomicInteger counter = new AtomicInteger();

    @PostConstruct
    private void init() {
        loadTasks();
    }

    public synchronized List<Task> list() {
        return new ArrayList<>(tasks);
    }

    public synchronized Task add(String text) {
        Task t = new Task(counter.incrementAndGet(), text, false);
        tasks.add(t);
        saveTasks();
        return t;
    }

    public synchronized Task complete(int id) {
        for (Task t : tasks) {
            if (t.getId() == id) {
                t.setDone(true);
                saveTasks();
                return t;
            }
        }
        return null;
    }

    public synchronized boolean delete(int id) {
        boolean removed = tasks.removeIf(t -> t.getId() == id);
        if (removed) {
            saveTasks();
        }
        return removed;
    }

    private void loadTasks() {
        try {
            File f = new File(DATA_FILE);
            if (f.exists()) {
                List<Task> loaded = mapper.readValue(f, new TypeReference<List<Task>>() {});
                tasks.clear();
                tasks.addAll(loaded);
                int maxId = tasks.stream().mapToInt(Task::getId).max().orElse(0);
                counter.set(maxId);
            }
        } catch (Exception e) {
            System.err.println("Failed to load tasks: " + e.getMessage());
        }
    }

    private void saveTasks() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), tasks);
        } catch (Exception e) {
            System.err.println("Failed to save tasks: " + e.getMessage());
        }
    }
}
