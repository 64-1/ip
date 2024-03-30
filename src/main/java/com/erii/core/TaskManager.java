package com.erii.core;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



/**
 * The TaskManager class represents a task management system that allows users to manage tasks.
 * It provides functionality to add tasks, list tasks, mark tasks as done, delete tasks, and search for tasks.
 * Tasks can be of different types, such as Todo, Deadline, and Event, each with its own properties and behaviors.
 */
public class TaskManager {

    // Define the Priority enum
    public enum Priority {
        SS, S, A, B, C, D
    }

    // Define the abstract Task class
    public abstract class Task {
        protected String name;
        protected String description;
        protected Priority priority;

        // Constructor for Task class
        public Task(String name, String description, Priority priority) {
            this.name = name;
            this.description = description;
            this.priority = priority;
        }

        // Getter for name
        public String getName() {
            return name;
        }

        // Getter for description
        public String getDescription() {
            return description;
        }

        // Getter for priority
        public Priority getPriority() {
            return priority;
        }

        // Setter for priority
        public void setPriority(Priority priority) {
            this.priority = priority;
        }

        // Abstract method to get status icon
        public abstract String getStatusIcon();

        // Abstract method to convert Task to string
        @Override
        public abstract String toString();
    }

    // Define the Todo class, which extends Task
    public class Todo extends Task {
        protected boolean isDone;

        // Constructor for Todo class
        public Todo(String name, String description, Priority priority) {
            super(name, description, priority);
            isDone = false;
        }

        // Setter for isDone
        public void setDone(boolean done) {
            isDone = done;
        }

        // Getter for isDone
        public boolean isDone() {
            return isDone;
        }

        // Get the status icon for Todo
        @Override
        public String getStatusIcon() {
            return (isDone ? "[X]" : "[ ]");
        }

        // Convert Todo to string
        @Override
        public String toString() {
            return "[T]" + getStatusIcon() + " " + description + " <" + priority + "> ";
        }
    }

    // Define the Deadline class, which extends Todo
    public class Deadline extends Todo {
        protected LocalDateTime by;

        // Constructor for Deadline class
        public Deadline(String name, String description, LocalDateTime by, Priority priority) {
            super(name, description, priority);
            this.by = by;
        }

        // Getter for by
        public LocalDateTime getBy() {
            return this.by;
        }

        public void setBy(LocalDateTime by) {
            this.by = by;
        }

        // Convert Deadline to string
        @Override
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
            String formattedDate = by.format(formatter);
            return "[D]" + super.getStatusIcon() + " " + description + " <" + priority + "> " + " (by: " + formattedDate + ")";
        }
    }

    // Define the Event class, which extends Todo
    public class Event extends Todo {
        protected boolean isDone;
        protected LocalDate start;
        protected LocalDate end;

        // Constructor for Event class
        public Event(String name, String description, LocalDate start, LocalDate end, Priority priority) {
            super(name, description, priority);
            this.start = start;
            this.end = end;
        }

        // Setter for isDone
        public void setDone(boolean done) {
            isDone = done;
        }

        // Getter for start
        public LocalDate getStart() {
            return this.start;
        }

        // Getter for end
        public LocalDate getEnd() {
            return this.end;
        }

        // Get the status icon for Event
        @Override
        public String getStatusIcon() {
            return isDone ? "[X]" : "[ ]";
        }

        /* 
         * Convert Event to string
        */
        @Override
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
            return "[E]" + getStatusIcon() + " " + description + " <" + priority + "> " + " (from: " + start.format(formatter) + " to: " + end.format(formatter) + ")";
        }
    }

    private List<Task> tasks = new ArrayList<>();

    /**
     * Get the size of the task list
     */
    public int listSize() {
        return tasks.size();
    }

    /**
     * Add a task to the task list
     */
    public void addTask(Task task) {
        tasks.add(task);
        System.out.println("\nGot it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("\nNow you have " + tasks.size() + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

    /**
     * Load a task from Text to the task list
     */
    public void loadTask(Task task) {
        tasks.add(task);
    }

    /**
     * List all tasks in the task list
     */
    public void listTasks() {
        System.out.println("\nHere are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        System.out.println("____________________________________________________________");
    }

    /**
     * Sort the task list by priority
     */
    public void sortListByPriority() {
        tasks.sort((Task t1, Task t2) -> t1.getPriority().compareTo(t2.getPriority()));
        System.out.println("\nTasks sorted by priority.");
    }

    /**
     * Sort the task list by type
     */
    public void sortListByType() {
        tasks.sort((Task t1, Task t2) -> t1.getName().compareTo(t2.getName()));
        System.out.println("\nTasks sorted by type.");
    }

    /**
     * Mark a task as done
     */
    public void markTaskAsDone(int taskIndex) {
        if (taskIndex >= 0 && taskIndex < tasks.size()) {
            Task task = tasks.get(taskIndex);
            if (task instanceof Todo) {
                ((Todo) task).setDone(true);
            } else if (task instanceof Event) {
                ((Event) task).setDone(true);
            } else {
                System.out.println("\nThis task type cannot be marked as done.");
                return;
            }
            System.out.println("----------------------------------");
            System.out.println("\nTask completed");
            System.out.println(task);
            System.out.println("--------------------------------------");
        } else {
            System.out.println("\nInvalid task number.");
        }
    }

    /**
     * Delete a task from the task list
     */
    public void deleteTask(int taskIndex) {
        if (taskIndex >= 0 && taskIndex < tasks.size()) {
            Task task = tasks.remove(taskIndex);
            System.out.println("\nNoted. I've removed this task:");
            System.out.println("  " + task);
            System.out.println("\nNow you have " + tasks.size() + " tasks in the list.");
            System.out.println("____________________________________________________________");
        } else {
            System.out.println("Invalid task number.");
        }
    }

    /**
     * Get a copy of all tasks in the task list
     */
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * List tasks on a specific date
     */
    public void listTasksOn(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
        System.out.println("\nTasks on " + date.format(formatter) + ":");
        for (Task task : tasks) {
            if (task instanceof Deadline) {
                if (((Deadline) task).getBy().isEqual(date)) {
                    System.out.println(task);
                }
            }
            // Include similar logic for Event if it also uses LocalDateTime
            if (task instanceof Event) {
                if (((Event) task).getStart().isEqual(date.toLocalDate()) && ((Event) task).getEnd().isEqual(date.toLocalDate())) {
                    System.out.println(task);
                }
            }
        }
    }

    /**
     * Method to search tasks by keyword
     */
    public void findTasks(String keyword) {
        System.out.println("____________________________________________________________");
        System.out.println("\nHere are the matching tasks in your list:");

        int matchCount = 0;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println((i + 1) + "." + task);
                matchCount++;
            }
        }

        if (matchCount == 0) {
            System.out.println("\nNo matching tasks found.");
        }

        System.out.println("____________________________________________________________");
    }


    /**
     * Validate and convert a priority string to Priority enum
     */
    public static Priority validateAndConvertPriority(String priorityString) throws IllegalArgumentException {
        try {
            return Priority.valueOf(priorityString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("\nInvalid priority. Please enter a valid priority value (SS, S, A, B, C, D).");
        }
    }
}
