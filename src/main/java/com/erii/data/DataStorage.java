package com.erii.data;

import com.erii.core.TaskManager;
import com.erii.core.TaskManager.Task;
import com.erii.user.UserDetails;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The DataStorage class is responsible for saving and loading data from files.
 * It provides methods to save and load tasks and user details.
 */
public class DataStorage {
    private static final String TASKS_FILE = "./data/tasks.txt";
    private static final String USER_DETAILS_FILE = "./data/userDetails.txt";

    /**
     * Save tasks to a file
     *
     * @param tasks The list of tasks to be saved
     */
    public void saveTasks(List<Task> tasks) {
        try (PrintWriter writer = new PrintWriter(new File(TASKS_FILE))) {
            for (Task task : tasks) {
                writer.println(taskToFileString(task));
            }
        } catch (IOException e) {
            System.out.println("An error occurred while saving tasks: " + e.getMessage());
        }
    }

    /**
     * Load tasks from a file
     *
     * @param taskManager The task manager instance
     * @return The list of loaded tasks
     */
    public List<Task> loadTasks(TaskManager taskManager) {
        List<Task> tasks = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(TASKS_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) continue;
                Task task = fileStringToTask(line, taskManager);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Tasks file not found. Starting with an empty task list.");
        }
        return tasks;
    }

    /**
     * Save user details to a file
     *
     * @param userDetails The user details to be saved
     */
    public void saveUserDetails(UserDetails userDetails) {
        File file = new File(USER_DETAILS_FILE);
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println(userDetails.getUserName());
            writer.println(userDetails.getUserBirthday());
            writer.println(userDetails.getUserGender());
        } catch (IOException e) {
            System.out.println("An error occurred while saving user details: " + e.getMessage());
        }
    }

    /**
     * Load user details from a file
     *
     * @return The loaded user details
     */
    public UserDetails loadUserDetails() {
        UserDetails userDetails = new UserDetails();
        try (Scanner scanner = new Scanner(new File(USER_DETAILS_FILE))) {
            if (scanner.hasNextLine()) {
                userDetails.setUserName(scanner.nextLine());
            } else {
                System.out.println("User name not found in file.");
            }
            if (scanner.hasNextLine()) {
                userDetails.setUserBirthday(scanner.nextLine());
            } else {
                System.out.println("User birthday not found in file.");
            }
            if (scanner.hasNextLine()) {
                userDetails.setUserGender(scanner.nextLine());
            } else {
                System.out.println("User gender not found in file.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("User details file not found. Starting with default user details.");
        }
        return userDetails;
    }

    /**
     * Convert a Task object to a string representation
     *
     * @param task The Task object
     * @return The string representation of the task
     */
    private String taskToFileString(TaskManager.Task task) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
        String taskType = task instanceof TaskManager.Todo ? "T" :
                task instanceof TaskManager.Deadline ? "D" : "E";
        String status = task instanceof TaskManager.Todo && ((TaskManager.Todo) task).isDone() ? "1" : "0";
        String priority = task.getPriority().name();
        String description = task.getDescription();
        String dateInfo = "";
    
        if (task instanceof TaskManager.Deadline) {
            LocalDateTime by = ((TaskManager.Deadline) task).getBy();
            dateInfo = "|" + by.format(dateTimeFormatter);
        } else if (task instanceof TaskManager.Event) {
            LocalDate start = ((TaskManager.Event) task).getStart();
            LocalDate end = ((TaskManager.Event) task).getEnd();
            dateInfo = "|" + start.format(dateFormatter) + "|" + end.format(dateFormatter);
        }
    
        return String.join("|", taskType, status, priority, description) + dateInfo;
    }
    
    /**
     * Convert a string representation of a task to a Task object
     *
     * @param line        The string representation of the task
     * @param taskManager The task manager instance
     * @return The Task object
     */
    private TaskManager.Task fileStringToTask(String line, TaskManager taskManager) {
        String[] parts = line.split("\\|");
        String taskType = parts[0];
        boolean isDone = parts[1].equals("1");
        TaskManager.Priority priority = TaskManager.Priority.valueOf(parts[2]);
        String description = parts[3];

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        switch (taskType) {
            case "T":
                TaskManager.Todo todo = taskManager.new Todo("Todo", description, priority);
                todo.setDone(isDone);
                return todo;
            case "D":
                try {
                    LocalDateTime byDate = LocalDateTime.parse(parts[4], dateTimeFormatter);
                    TaskManager.Deadline deadline = taskManager.new Deadline("Deadline", description, byDate, priority);
                    deadline.setDone(isDone);
                    return deadline;
                } catch (DateTimeParseException e) {
                    System.out.println("Error parsing the deadline date: " + e.getMessage());
                    return null;
                }
            case "E":
                try {
                    LocalDate startDate = LocalDate.parse(parts[4], dateFormatter);
                    LocalDate endDate = LocalDate.parse(parts[5], dateFormatter);
                    TaskManager.Event event = taskManager.new Event("Event", description, startDate, endDate, priority);
                    event.setDone(isDone);
                    return event;
                } catch (DateTimeParseException e) {
                    System.out.println("Error parsing event dates: " + parts[4] + " to " + parts[5]);
                    return null;
                }
            default:
                return null;
        }
    }
}
