package main;

import auth.User;
import console.Outputs;
import csv.audit.AuditSingleton;
import csv.readWriteSingletons.SubtasksReadWriteSingleton;
import csv.readWriteSingletons.TasksReadWriteSingleton;
import csv.readWriteSingletons.UsersReadWriteSingleton;
import csv.readWriteSingletons.WorkspacesReadWriteSingleton;
import data.Data;
import services.Services;
import workspace.Workspace;
import workspace.task.Task;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Data data = new Data();
        Services services = new Services(data);
        Scanner scanner = new Scanner(System.in);

        data.setUsers(UsersReadWriteSingleton.getInstance().readDataFromFile());
        for (User user : data.getUsers()) {
            user.setWorkspaces(WorkspacesReadWriteSingleton.getInstance().readDataFromFile(user.getId()));
            for (Workspace workspace : user.getWorkspaces()) {
                workspace.setTasks(TasksReadWriteSingleton.getInstance().readDataFromFile(user.getId(), workspace.getId()));
                List<Task> tasks = workspace.getTasks();
                tasks.addAll(SubtasksReadWriteSingleton.getInstance().readDataFromFile(user.getId(), workspace.getId()));
                workspace.setTasks(tasks);
            }
        }
        AuditSingleton.getInstance().addActionToFile("\nstart program");

        while (true) {
            if (data.getCurrentUserLogged() == -1) {
                Outputs.displayMenuBeforeLogin();
                System.out.print("Choose an action: ");
                int action = scanner.nextInt();
                scanner.nextLine();

                String email, password, username;
                switch (action) {
                    case 1 -> {
                        System.out.println("Enter your email: ");
                        email = scanner.nextLine();
                        System.out.println("Enter your username: ");
                        username = scanner.nextLine();
                        System.out.println("Enter your password: ");
                        password = scanner.nextLine();
                        services.register(email, username, password);
                    }
                    case 2 -> {
                        System.out.println("Enter your email: ");
                        email = scanner.nextLine();
                        System.out.println("Enter your password: ");
                        password = scanner.nextLine();
                        services.login(email, password);
                    }
                    default -> {
                        System.out.println("Bye!");
                        AuditSingleton.getInstance().addActionToFile("end program");
                        WorkspacesReadWriteSingleton.getInstance().writeDataToFile(data.getUsers());
                        TasksReadWriteSingleton.getInstance().writeDataToFile(data.getUsers());
                        SubtasksReadWriteSingleton.getInstance().writeDataToFile(data.getUsers());
                        return;
                    }
                }
            } else {
                Outputs.displayMenuAfterLogin();
                System.out.print("Choose an action: ");
                int action = scanner.nextInt();
                scanner.nextLine();

                String workspace;
                switch (action) {
                    case 1 -> {
                        services.logout();
                    }
                    case 2 -> {
                        services.account();
                    }
                    case 3 -> {
                        System.out.println("Enter workspace name: ");
                        workspace = scanner.nextLine();
                        services.createWorkspace(workspace);
                    }
                    case 4 -> {
                        services.listWorkspaces();
                    }
                    case 5 -> {
                        System.out.println("Enter the name of the workspace that will be deleted: ");
                        workspace = scanner.nextLine();
                        services.deleteWorkspace(workspace);
                    }
                    case 6 -> {
                        System.out.println("Enter the name of the workspace that will be renamed: ");
                        workspace = scanner.nextLine();
                        System.out.println("Enter the name of the workspace that will be renamed: ");
                        String newWorkspace = scanner.nextLine();
                        services.renameWorkspace(workspace, newWorkspace);
                    }
                    case 7 -> {
                        System.out.println("Enter workspace name: ");
                        workspace = scanner.nextLine();
                        System.out.println("Enter task name: ");
                        String task = scanner.nextLine();
                        services.createTask(workspace, task);
                    }
                    case 8 -> {
                        System.out.println("Enter workspace name: ");
                        workspace = scanner.nextLine();
                        services.listTasks(workspace);
                    }
                    case 9 -> {
                        System.out.println("Enter workspace name: ");
                        workspace = scanner.nextLine();
                        System.out.println("Enter task name: ");
                        String task = scanner.nextLine();
                        System.out.println("Enter subtask name: ");
                        String subtask = scanner.nextLine();
                        services.createSubtask(workspace, task, subtask);
                    }
                    case 10 -> {
                        System.out.println("Enter workspace name: ");
                        workspace = scanner.nextLine();
                        System.out.println("Enter task name: ");
                        String task = scanner.nextLine();
                        services.listSubtasks(workspace, task);
                    }
                    default -> {
                        System.out.println("Bye!");
                        AuditSingleton.getInstance().addActionToFile("end program");
                        WorkspacesReadWriteSingleton.getInstance().writeDataToFile(data.getUsers());
                        TasksReadWriteSingleton.getInstance().writeDataToFile(data.getUsers());
                        SubtasksReadWriteSingleton.getInstance().writeDataToFile(data.getUsers());
                        return;
                    }
                }
            }
        }
    }
}
