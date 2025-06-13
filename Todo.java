import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class Todo {
    private static final String DATA_FILE = "tasks.json";

    static class Task {
        String text;
        boolean done;

        Task(String text, boolean done) {
            this.text = text;
            this.done = done;
        }
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        Path p = Paths.get(DATA_FILE);
        if (!Files.exists(p)) {
            return tasks;
        }
        try {
            String content = new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
            Pattern pattern = Pattern.compile("\\{\\s*\"text\"\\s*:\\s*\"(.*?)\"\\s*,\\s*\"done\"\\s*:\\s*(true|false)\\s*\\}");
            Matcher m = pattern.matcher(content);
            while (m.find()) {
                String text = m.group(1);
                boolean done = Boolean.parseBoolean(m.group(2));
                tasks.add(new Task(text, done));
            }
        } catch (IOException e) {
            System.err.println("Failed to load tasks: " + e.getMessage());
        }
        return tasks;
    }

    private static void saveTasks(List<Task> tasks) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            sb.append("  {\"text\":\"").append(escape(t.text)).append("\",\"done\":").append(t.done);
            sb.append("}");
            if (i < tasks.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]\n");
        try {
            Files.write(Paths.get(DATA_FILE), sb.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Failed to save tasks: " + e.getMessage());
        }
    }

    private static void listTasks() {
        List<Task> tasks = loadTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks.");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                Task t = tasks.get(i);
                String status = t.done ? "\u2713" : " ";
                System.out.printf("%d. [%s] %s%n", i + 1, status, t.text);
            }
        }
    }

    private static void addTask(String text) {
        List<Task> tasks = loadTasks();
        tasks.add(new Task(text, false));
        saveTasks(tasks);
        System.out.println("Task added.");
    }

    private static void completeTask(int index) {
        List<Task> tasks = loadTasks();
        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).done = true;
            saveTasks(tasks);
            System.out.println("Task completed.");
        } else {
            System.out.println("Invalid task number.");
        }
    }

    private static void deleteTask(int index) {
        List<Task> tasks = loadTasks();
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
            saveTasks(tasks);
            System.out.println("Task deleted.");
        } else {
            System.out.println("Invalid task number.");
        }
    }

    private static void usage() {
        System.out.println("Usage: java Todo [list|add TEXT|done NUMBER|delete NUMBER]");
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            usage();
            return;
        }
        String cmd = args[0];
        switch (cmd) {
            case "list":
                listTasks();
                break;
            case "add":
                if (args.length < 2) {
                    System.out.println("Provide task text.");
                } else {
                    String text = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                    addTask(text);
                }
                break;
            case "done":
                if (args.length < 2) {
                    System.out.println("Provide task number.");
                } else {
                    try {
                        int index = Integer.parseInt(args[1]) - 1;
                        completeTask(index);
                    } catch (NumberFormatException e) {
                        System.out.println("Provide a number.");
                    }
                }
                break;
            case "delete":
                if (args.length < 2) {
                    System.out.println("Provide task number.");
                } else {
                    try {
                        int index = Integer.parseInt(args[1]) - 1;
                        deleteTask(index);
                    } catch (NumberFormatException e) {
                        System.out.println("Provide a number.");
                    }
                }
                break;
            default:
                usage();
        }
    }
}
