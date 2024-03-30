package com.erii.ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import com.erii.user.UserDetails;
import com.erii.core.TaskManager;
import com.erii.data.DataStorage;

/**
 * The ControlPanel class represents the user interface control panel for managing tasks.
 * It provides options for listing tasks, adding tasks, marking tasks as done, deleting tasks,
 * and searching for tasks. It interacts with the TaskManager, DataStorage, and UserDetails classes.
 */
public class ControlPanel {
    private TaskManager taskManager;
    private DataStorage storage;
    private UserDetails userDetails;

    /**
     * Constructs a ControlPanel object with the specified task manager, data storage, and user details.
     *
     * @param taskManager  the task manager to be used
     * @param storage      the data storage to be used
     * @param userDetails the user details to be used
     */
    public ControlPanel(TaskManager taskManager, DataStorage storage, UserDetails userDetails) {
        this.taskManager = taskManager;
        this.storage = storage;
        this.userDetails = userDetails;
    }

    /**
     * Starts the control panel and handles user input.
     */
    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            menu();

            while (scanner.hasNextLine()) {
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        listTasks();
                        break;
                    case "2":
                        System.out.println("\nPlease enter the task description and priority (e.g., slain a dragon /S):");
                        String inputAddTask = scanner.nextLine().trim();
                        addTodoTask(inputAddTask);
                        break;
                    case "3":
                        System.out.println("\nPlease enter the deadline task description, deadline date and priority (e.g., submit report /by 2021-09-30 18:30 /SS):");
                        String inputAddDeadline = scanner.nextLine().trim();
                        addDeadlineTask(inputAddDeadline);
                        break;
                    case "4":
                        System.out.println("\nPlease enter the event description, start date, end date and priority (e.g., project meeting /from 2021-09-30 /to 2021-10-01 /S):");
                        String inputAddEvent = scanner.nextLine().trim();
                        addEventTask(inputAddEvent);
                        break;
                    case "5":
                        System.out.println("\nPlease enter the task number to mark as done:");
                        String inputMark = scanner.nextLine().trim();
                        markTaskAsDone(inputMark);
                        break;
                    case "6":
                        System.out.println("\nChoose the task you want to delete: ");
                        String inputDelete = scanner.nextLine().trim();
                        deleteTask(inputDelete);
                        break;
                    case "7": // Assuming "7" is the new option for listing tasks by date
                        System.out.println("\nPlease enter the date in yyyy-MM-dd format to list tasks:");
                        String dateString = scanner.nextLine().trim();
                        try {
                            LocalDateTime date = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            taskManager.listTasksOn(date);
                        } catch (DateTimeParseException e) {
                            System.out.println("\nInvalid date format. Please enter the date in yyyy-MM-dd format.");
                        }
                        break;
                    case "8": // Assuming "8" is the new option for search
                        System.out.println("\nEnter a keyword to search for tasks:");
                        String keyword = scanner.nextLine().trim();
                        taskManager.findTasks(keyword);
                        break;
                    case "X":
                        System.out.println("\nSaving changes...");
                        System.out.println("----------------------------------");
                        storage.saveUserDetails(userDetails);
                        System.out.println("\nChanges saved. Exiting.");
                        System.out.println("----------------------------------");
                        System.out.println("\nThank you for using Erii. さよなら!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("\nUnknown command. Please try again.");
                        break;
                }

                menu();
            }
        }
    }

    /**
     * Displays the menu options.
     */
    private static void menu() {
        System.out.println("\nHow may I assist you today?");
        System.out.println("1. List tasks");
        System.out.println("2. Add a task");
        System.out.println("3. Add a deadline task");
        System.out.println("4. Add an event task");
        System.out.println("5. Mark a task as done");
        System.out.println("6. Delete a task");
        System.out.println("7. List tasks on a specific date");
        System.out.println("8. Search for a task by keyword");
        System.out.println("X. Exit");
        System.out.print("Enter the symbol corresponding to your choice: \n");
    }

    /**
     * Lists all tasks.
     */
    private void listTasks() {
        taskManager.listTasks();
    }

    /**
     * Adds a todo task with the given input.
     *
     * @param input the input string containing the task description and priority
     */
    private void addTodoTask(String input) {
        String[] parts = input.split(" ?/ ?");
        if (parts.length < 2 || parts[1].isEmpty()) {
            System.out.println("\n\"Incorrect format. Please ensure the task description is followed by '/' and a priority value (e.g., 'slain a dragon /SS').\"");
            return;
        }
        String description = parts[0];
        TaskManager.Priority priority;
        try {
            priority = TaskManager.Priority.valueOf(parts[1].trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("\nInvalid priority. Please enter a valid priority value (SS, S, A, B, C, D, E).");
            return;
        }
        taskManager.addTask(taskManager.new Todo("Todo", description, priority));
        storage.saveTasks(taskManager.getAllTasks());
    }

    /**
     * Adds a deadline task with the given input.
     *
     * @param input the input string containing the task description, deadline date, and priority
     */
    private void addDeadlineTask(String input) {
        // Adjusted split regex to account for spaces accurately
        String[] parts = input.split(" ?/by | ?/ ?");
        if (parts.length < 3) {
            System.out.println("\nIncorrect format. Please follow the correct input format 'description /by yyyy-MM-dd HH:mm /priority'.");
            return;
        }
        String description = parts[0];
        // Corrected to assume the parts include date and time together without needing to concatenate with "|"
        String dateTimeString = parts[1]; // This should be in "yyyy-MM-dd HH:mm" format directly from the input
        LocalDateTime by;
        try {
            // Adjusted the formatter pattern to match the expected input "yyyy-MM-dd HH:mm"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            by = LocalDateTime.parse(dateTimeString, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("\nInvalid date and time. Please enter in yyyy-MM-dd HH:mm format.");
            return;
        }
        TaskManager.Priority priority = TaskManager.Priority.valueOf(parts[2].trim().toUpperCase());
        taskManager.addTask(taskManager.new Deadline("Deadline", description, by, priority));
        storage.saveTasks(taskManager.getAllTasks());
    }

    /**
     * Adds an event task with the given input.
     *
     * @param input the input string containing the task description, start date, end date, and priority
     */
    private void addEventTask(String input) {
        String[] parts = input.split(" ?/from | ?/to | ?/");
        if (parts.length < 4) {
            System.out.println("\nIncorrect format. Please ensure the task description is followed by '/from', a start date, '/to', an end date, and then a priority value.");
            return;
        }
        String description = parts[0];
        String startDateString = parts[1];
        String endDateString = parts[2];
        TaskManager.Priority priority;

        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(startDateString, dateFormatter);
            LocalDate endDate = LocalDate.parse(endDateString, dateFormatter);
            priority = TaskManager.Priority.valueOf(parts[3].trim().toUpperCase());
            taskManager.addTask(taskManager.new Event("Event", description, startDate, endDate, priority));
            storage.saveTasks(taskManager.getAllTasks());
        } catch (DateTimeParseException e) {
            System.out.println("\nInvalid date format. Please enter the date in yyyy-MM-dd format.");
            return;
        } catch (IllegalArgumentException e) {
            System.out.println("\nInvalid priority. Please enter a valid priority value (SS, S, A, B, C, D, E).");
            return;
        }
    }

    /**
     * Marks a task as done with the given input.
     *
     * @param input the input string containing the task number1
     */
    private void markTaskAsDone(String input) {
        try {
            int taskNumber = Integer.parseInt(input)-1;
            if (taskNumber < 0 || taskNumber >= taskManager.listSize()) {
                System.out.println("\nTask number is out of range. Please enter a valid task number.");
                System.out.println("\nCurrent number of tasks: " + taskManager.listSize());
                return;
            }
            taskManager.markTaskAsDone(taskNumber);
            storage.saveTasks(taskManager.getAllTasks());
        } catch (NumberFormatException e) {
            System.out.println("\nPlease enter a valid task number.");
        }
    }

    /**
     * Deletes a task with the given input.
     *
     * @param input the input string containing the task number
     */
    private void deleteTask(String input) {
        try {
            int taskNumber = Integer.parseInt(input) - 1;
            if (taskNumber < 0 || taskNumber >= taskManager.listSize()) {
                System.out.println("\nTask number is out of range. Please enter a valid task number.");
                System.out.println("\nCurrent number of tasks: " + taskManager.listSize());
                return;
            }
            taskManager.deleteTask(taskNumber);
            storage.saveTasks(taskManager.getAllTasks());
        } catch (NumberFormatException e) {
            System.out.println("\nPlease enter a valid task number.");
        }
    }
}
