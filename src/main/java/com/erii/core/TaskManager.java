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

    /**
     * The Priority enum represents the priority levels of a task.
     */
    public enum Priority {
        SS, S, A, B, C, D
    }

    /**
     * The Task class represents a generic task.
     */
    public abstract class Task {
        protected String name;
        protected String description;
        protected Priority priority;

        /**
         * Constructs a Task object with the specified name, description, and priority.
         *
         * @param name        the name of the task
         * @param description the description of the task
         * @param priority    the priority of the task
         */
        public Task(final String name, final String description, final Priority priority) {
            this.name = name;
            this.description = description;
            this.priority = priority;
        }

        /**
         * Returns the name of the task.
         *
         * @return the name of the task
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the description of the task.
         *
         * @return the description of the task
         */
        public String getDescription() {
            return description;
        }

        /**
         * Returns the priority of the task.
         *
         * @return the priority of the task
         */
        public Priority getPriority() {
            return priority;
        }

        /**
         * Sets the priority of the task.
         *
         * @param priority the priority to be set
         */
        public void setPriority(final Priority priority) {
            this.priority = priority;
        }

        /**
         * Returns the status icon of the task.
         *
         * @return the status icon of the task
         */
        public abstract String getStatusIcon();

        /**
         * Returns a string representation of the task.
         *
         * @return a string representation of the task
         */
        @Override
        public abstract String toString();
    }

    /**
     * The Todo class represents a todo task.
     */
    public class Todo extends Task {
        protected boolean isDone;

        /**
         * Constructs a Todo object with the specified name, description, and priority.
         *
         * @param name        the name of the todo task
         * @param description the description of the todo task
         * @param priority    the priority of the todo task
         */
        public Todo(final String name, final String description, final Priority priority) {
            super(name, description, priority);
            isDone = false;
        }

        /**
         * Sets the done status of the todo task.
         *
         * @param done the done status to be set
         */
        public void setDone(final boolean done) {
            isDone = done;
        }

        /**
         * Returns the done status of the todo task.
         *
         * @return the done status of the todo task
         */
        public boolean isDone() {
            return isDone;
        }

        /**
         * Returns the status icon of the todo task.
         *
         * @return the status icon of the todo task
         */
        @Override
        public String getStatusIcon() {
            return (isDone ? "[X]" : "[ ]");
        }

        /**
         * Returns a string representation of the todo task.
         *
         * @return a string representation of the todo task
         */
        @Override
        public String toString() {
            return "[T]" + getStatusIcon() + " " + description + " <" + priority + "> ";
        }
    }

    /**
     * The Deadline class represents a deadline task.
     */
    public class Deadline extends Todo {
        protected LocalDateTime by;

        /**
         * Constructs a Deadline object with the specified name, description, deadline,
         * and priority.
         *
         * @param name        the name of the deadline task
         * @param description the description of the deadline task
         * @param by          the deadline of the deadline task
         * @param priority    the priority of the deadline task
         */
        public Deadline(final String name, final String description, final LocalDateTime by, final Priority priority) {
            super(name, description, priority);
            this.by = by;
        }

        /**
         * Returns the deadline of the deadline task.
         *
         * @return the deadline of the deadline task
         */
        public LocalDateTime getBy() {
            return this.by;
        }

        /**
         * Sets the deadline of the deadline task.
         *
         * @param by the deadline to be set
         */
        public void setBy(final LocalDateTime by) {
            this.by = by;
        }

        /**
         * Returns a string representation of the deadline task.
         *
         * @return a string representation of the deadline task
         */
        @Override
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
            String formattedDate = by.format(formatter);
            return "[D]" + super.getStatusIcon() + " " + description + " <" + priority + "> " + " (by: " + formattedDate
                    + ")";
        }
    }

    /**
     * The Event class represents an event task.
     */
    public class Event extends Todo {
        protected boolean isDone;
        protected LocalDate start;
        protected LocalDate end;

        /**
         * Constructs an Event object with the specified name, description, start date,
         * end date, and priority.
         *
         * @param name        the name of the event task
         * @param description the description of the event task
         * @param start       the start date of the event task
         * @param end         the end date of the event task
         * @param priority    the priority of the event task
         */
        public Event(final String name, final String description, final LocalDate start, final LocalDate end,
                final Priority priority) {
            super(name, description, priority);
            this.start = start;
            this.end = end;
        }

        /**
         * Sets the done status of the event task.
         *
         * @param done the done status to be set
         */
        public void setDone(final boolean done) {
            isDone = done;
        }

        /**
         * Returns the start date of the event task.
         *
         * @return the start date of the event task
         */
        public LocalDate getStart() {
            return this.start;
        }

        /**
         * Returns the end date of the event task.
         *
         * @return the end date of the event task
         */
        public LocalDate getEnd() {
            return this.end;
        }

        /**
         * Returns the status icon of the event task.
         *
         * @return the status icon of the event task
         */
        @Override
        public String getStatusIcon() {
            return isDone ? "[X]" : "[ ]";
        }

        /**
         * Returns a string representation of the event task.
         *
         * @return a string representation of the event task
         */
        @Override
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
            return "[E]" + getStatusIcon() + " " + description + " <" + priority + "> " + " (from: "
                    + start.format(formatter) + " to: " + end.format(formatter) + ")";
        }
    }

    private List<Task> tasks = new ArrayList<>();

    /**
     * Returns the size of the task list.
     *
     * @return the size of the task list
     */
    public int listSize() {
        return tasks.size();
    }

    /**
     * Adds a task to the task list.
     *
     * @param task the task to be added
     */
    public void addTask(final Task task) {
        tasks.add(task);
        System.out.println("\nGot it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("\nNow you have " + tasks.size() + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

    /**
     * Loads a task from Text to the task list.
     *
     * @param task the task to be loaded
     */
    public void loadTask(final Task task) {
        tasks.add(task);
    }

    /**
     * Lists all tasks in the task list.
     */
    public void listTasks() {
        System.out.println("\nHere are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        System.out.println("____________________________________________________________");
    }

    /**
     * Sorts the task list by priority.
     */
    public void sortListByPriority() {
        tasks.sort((Task t1, Task t2) -> t1.getPriority().compareTo(t2.getPriority()));
        System.out.println("\nTasks sorted by priority.");
    }

    /**
     * Sorts the task list by type.
     */
    public void sortListByType() {
        tasks.sort((Task t1, Task t2) -> t1.getName().compareTo(t2.getName()));
        System.out.println("\nTasks sorted by type.");
    }

    /**
     * Marks a task as done.
     *
     * @param taskIndex the index of the task to be marked as done
     */
    public void markTaskAsDone(final int taskIndex) {
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
     * Deletes a task from the task list.
     *
     * @param taskIndex the index of the task to be deleted
     */
    public void deleteTask(final int taskIndex) {
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
     * Returns a copy of all tasks in the task list.
     *
     * @return a copy of all tasks in the task list
     */
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * Lists deadline tasks on a specific datetime.
     *
     * @param datetime the datetime to filter deadline tasks
     */
    public void listTasksOn(final LocalDateTime datetime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        System.out.println("\nDeadline Tasks on " + datetime.format(formatter) + ":");

        boolean found = false;
        for (Task task : tasks) {
            if (task instanceof Deadline) {
                LocalDateTime taskDatetime = ((Deadline) task).getBy();
                if (taskDatetime.isEqual(datetime)) {
                    System.out.println(task);
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No deadline tasks found for this date and time.");
        }
    }

    /**
     * Lists event tasks occurring on a specific date.
     *
     * @param date the date to filter event tasks
     */
    public void listTasksOn(final LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        System.out.println("\nEvent Tasks on " + date.format(formatter) + ":");

        boolean found = false;
        for (Task task : tasks) {
            if (task instanceof Event) {
                LocalDate startDate = ((Event) task).getStart();
                LocalDate endDate = ((Event) task).getEnd();
                if ((date.isEqual(startDate) || date.isAfter(startDate))
                        && (date.isEqual(endDate) || date.isBefore(endDate))) {
                    System.out.println(task);
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No event tasks found for this date.");
        }
    }

    /**
     * Searches tasks by keyword and prints the matching tasks.
     *
     * @param keyword the keyword to search for
     */
    public void findTasks(final String keyword) {
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
     * Validates and converts a priority string to a Priority enum.
     *
     * @param priorityString the priority string to be validated and converted
     * @return the corresponding Priority enum value
     * @throws IllegalArgumentException if the priority string is invalid
     */
    public static Priority validateAndConvertPriority(final String priorityString) throws IllegalArgumentException {
        try {
            return Priority.valueOf(priorityString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }
}