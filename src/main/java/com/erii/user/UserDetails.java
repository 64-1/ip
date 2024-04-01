package com.erii.user;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The UserDetails class represents the details of a user.
 * It contains information such as the user's name, birthday, and gender.
 */
public class UserDetails {
    private String userName = "";
    private String userBirthday = "";
    private String userGender = "";
    private Scanner scanner = new Scanner(System.in);

    /**
     * Constructs a new UserDetails instance.
     */
    public UserDetails() {
        // Constructor to initialize the UserDetails instance
    }

    /**
     * Sets the user name.
     * 
     * @param userName the user name to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Sets the user birthday.
     * 
     * @param userBirthday the user birthday to set
     */
    public void setUserBirthday(String userBirthday) {
        this.userBirthday = userBirthday;
    }

    /**
     * Sets the user gender.
     * 
     * @param userGender the user gender to set
     */
    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    /**
     * Gets the user name.
     * 
     * @return the user name
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Gets the user birthday.
     * 
     * @return the user birthday
     */
    public String getUserBirthday() {
        return this.userBirthday;
    }

    /**
     * Gets the user gender.
     * 
     * @return the user gender
     */
    public String getUserGender() {
        return this.userGender;
    }

    /**
     * Prompts the user to input their name.
     */
    public void inputName() {
        System.out.println("\nPlease enter your full name (First Name Last Name): ");
        String name;

        while (true) {
            name = scanner.nextLine().trim();
            if (name.split("\\s+").length >= 2) {
                this.userName = name;
                break;
            } else {
                System.out.println("\nInvalid name. Please enter both your first name and last name.");
            }
        }
    }

    /**
     * Prompts the user to input their birthday.
     */
    public void inputBirthday() {
        System.out.println("\nPlease enter your birthday (DD/MM/YYYY): ");
        String birthday;
        Pattern datePattern = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");

        while (true) {
            birthday = scanner.nextLine().trim();
            Matcher matcher = datePattern.matcher(birthday);
            if (matcher.matches()) {
                this.userBirthday = birthday;
                break;
            } else {
                System.out.println("\nInvalid format. Please enter your birthday in DD/MM/YYYY format.");
            }
        }
    }

    /**
     * Prompts the user to input their gender.
     */
    public void inputGender() {
        System.out.println("\nPlease enter your gender: ");
        System.out.println("1. Male");
        System.out.println("2. Female");
        System.out.println("3. Other");
        String gender = "";

        while (true) {
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    gender = "Male";
                    break;
                case "2":
                    gender = "Female";
                    break;
                case "3":
                    gender = "Other";
                    break;
                default:
                    System.out.println("\nInvalid option selected. Please enter 1, 2, or 3.");
                    continue;
            }
            this.userGender = gender;
            break;
        }
    }
}