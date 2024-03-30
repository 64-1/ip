package com.erii;
import java.util.List;
import com.erii.core.Erii;
import com.erii.core.TaskManager;
import com.erii.user.UserDetails;
import com.erii.data.DataStorage;
import com.erii.ui.ControlPanel;

/**
 * The main class of the program.
 * It initializes the necessary objects and starts the program execution.
 */
public class Main {
    public static void main(String[] args) {
        DataStorage storage = new DataStorage();
        TaskManager taskManager = new TaskManager();
        UserDetails userDetails = storage.loadUserDetails();
        ControlPanel controlPanel = new ControlPanel(taskManager, storage, userDetails);

        Erii.main(args);

        System.out.println("Initializing Kassel Academy...");

        if (userDetails == null || userDetails.getUserName() == null || userDetails.getUserName().isEmpty()) {
            userDetails = new UserDetails();
            userDetails.inputName();
            userDetails.inputBirthday();
            userDetails.inputGender();
            storage.saveUserDetails(userDetails);
            String message = String.format(
                "Verification passed.\nOptions enabled.\n%s Born on %s\n%s\nID A.D.0013\n" +
                    "Rank 'S'\nListed in the Kassel Academy roster.\n" +
                    "Database access granted\nAccount activated\nCourse schedule generated\n" +
                    "I am Erii, the secretary of Kassel Academy, pleased to serve you.",
                    userDetails.getUserName(), userDetails.getUserBirthday(), userDetails.getUserGender());
            System.out.println(message);
        } else {
            String message = "Welcome back, " + userDetails.getUserName();
            System.out.println(message);
        }

        List<TaskManager.Task> loadedTasks = storage.loadTasks(taskManager);
        for (TaskManager.Task task : loadedTasks) {
            taskManager.loadTask(task);
        }
        taskManager.listTasks();

        controlPanel.start();
    }
}