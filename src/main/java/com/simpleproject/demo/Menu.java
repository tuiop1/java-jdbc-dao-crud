package com.simpleproject.demo;

import com.simpleproject.demo.config.DataSourceFactory;
import com.simpleproject.demo.model.Task;
import com.simpleproject.demo.service.TaskService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private static final TaskService ts = new TaskService(DataSourceFactory.getDataSource());

    public static void printStartMenu() {
        System.out.print("""
                ==================================
                Welcome to simple db task manager!
                ==================================
                """);
    }

    public static void printMainMenu() {
        System.out.print("""
                
                ==================================
                         Choose an option!
                ==================================
                1 - list all saved tasks
                2 - create and save new task
                3 - delete task
                4 - alter saved task
                5 - find task by id
                0 - exit
                ==================================
                :""");
    }

    public static void createAndSaveTaskMenu(Scanner scanner) {
        System.out.print("Enter the name of the task:");
        String name = scanner.nextLine();
        System.out.print("Enter description of the task:");
        String description = scanner.nextLine();
        try {
            ts.createTask(name, description);

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Task was successfully created!");


    }


    public static void deleteTaskMenu(Scanner scanner) {
        System.out.print("Enter id of the task you would like to delete:");
        String idStr = scanner.nextLine();
        long id;
        try {
            id = Long.parseLong(idStr);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
            System.out.println("Failed to delete the task!");
            return;
        }
        try {

            ts.deleteTask(id);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to delete the task!");
            return;
        }
        System.out.printf("Task with id %d was successfully deleted!\n", id);

    }

    public static void printAllTasksMenu() {
        List<Task> tasks;
        try {
            tasks = ts.getAllTasks();
        } catch (RuntimeException e) {
            System.out.println("Failed to print all tasks!");
            System.out.println(e.getMessage());
            return;
        }
        System.out.print("""
                
                ==================================
                           ALL SAVED TASKS
                ==================================
                
                """);
        if (tasks.isEmpty()) {
            System.out.println("There are no tasks yet!");
        }
        tasks.forEach(System.out::println);

    }

    public static void alterTaskMenu(Scanner scanner) {
        System.out.print("Enter id of the task you want to alter:");
        String idStr = scanner.nextLine();
        long id;
        try {
            id = Long.parseLong(idStr);
            if (id < 1) throw new IllegalArgumentException("Id can not be negative!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid id!");
            return;
        }
        Task oldTask;

        try {
            oldTask = ts.findTaskByID(id);
            if(oldTask == null) throw new RuntimeException("No task with this id was found!");
        } catch (Exception e) {
            System.out.println("No task with this id was found!");
            System.out.println("Failed to alter Task!");
            return;
        }
        System.out.println("Just press \"enter\" to keep old property!");
        String name;
        System.out.print("Enter new name of the task:");
        try {
            name = scanner.nextLine();


            if (name.isEmpty()) {
                name = oldTask.getName();
            }
            if (name.length() < 3) throw new IllegalArgumentException("Name is too short!");


        } catch (IllegalArgumentException e) {
            System.out.println("Invalid name!");
            System.out.println("Failed to alter the task!");
            return;
        }
        System.out.print("Is task completed(true/false):");
        String isCompletedStr = scanner.nextLine();

        boolean isCompleted;
        try {
            isCompleted = switch (isCompletedStr) {
                case "" -> oldTask.isCompleted();
                case "true" -> true;
                case "false" -> false;
                default -> throw new IllegalArgumentException("Invalid input!");
            };
        } catch (RuntimeException e) {
            System.out.println("Invalid input!");
            System.out.println("Failed to alter the task!");
            return;
        }


        System.out.print("Enter new description of the task:");
        String description = scanner.nextLine();
        if (description.isEmpty()) {
            description = oldTask.getDescription();
        }

        try {
            ts.alterTask(id, new Task(null, name, isCompleted, description));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to alter the task");
        }

    }

    public static void findTaskMenu(Scanner scanner) {

        long id;
        System.out.print("Enter id of the task you want to find:");
        String idStr = scanner.nextLine();
        try {
            id = Long.parseLong(idStr);
            if (id < 1) throw new IllegalArgumentException("Id is not in range!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid Input!");
            System.out.println("Failed to find task!");
            return;
        } catch (IllegalArgumentException e) {
            System.out.println("Id is not in range!");
            System.out.println("Failed to find task!");
            return;
        }
        Task task;
        try {
            task = ts.findTaskByID(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to find task!");
            return;
        }
        if (task == null) {
            System.out.println("No task was found!");
            return;
        }
        System.out.println("Task was successfully found:");
        System.out.println();
        System.out.println(task);


    }

    public static void endMenu() {
        System.out.print("""
                
                ==================================
                      Thank you for using me!
                ==================================
                
                """);
    }


    private enum MenuOption {
        EXIT(0),
        LISTALL(1),
        CREATEANDSAVE(2),
        DELETE(3),
        ALTER(4),
        FIND(5);

        private final int code;

        MenuOption(int code) {
            this.code = code;
        }


        public int getCode() {
            return code;
        }

        public static MenuOption fromCode(int code) {
            for (MenuOption option : MenuOption.values()) {
                if (option.getCode() == code)
                    return option;
            }
            throw new IllegalArgumentException("Invalid code");
        }
    }


    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        printStartMenu();

        while (running) {


            printMainMenu();
            int choice;
            try {

                String choiceStr = scanner.nextLine();
                choice = Integer.parseInt(choiceStr);
                if (choice < 0 || choice > 5) throw new InputMismatchException("Choice is not in the range!");


            } catch (NumberFormatException e) {
                System.out.println("Input is not valid!");
                continue;
            } catch (InputMismatchException e) {
                System.out.println("Choice is not in the range!");
                continue;
            }


            MenuOption option = MenuOption.fromCode(choice);

            switch (option) {
                case LISTALL -> printAllTasksMenu();
                case CREATEANDSAVE -> createAndSaveTaskMenu(scanner);
                case DELETE -> deleteTaskMenu(scanner);
                case ALTER -> alterTaskMenu(scanner);
                case FIND -> findTaskMenu(scanner);
                case EXIT -> endMenu();

            }
            if (option == MenuOption.EXIT) {
                running = false;
            }


        }
        scanner.close();
        DataSourceFactory.getDataSource().close();


    }
}