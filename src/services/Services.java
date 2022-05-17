package services;

import auth.User;
import csv.audit.AuditSingleton;
import csv.readWriteSingletons.UsersReadWriteSingleton;
import data.Data;
import database.JdbcSingleton;
import workspace.Workspace;
import workspace.task.Subtask;
import workspace.task.Task;

import java.util.List;
import java.util.Objects;

public class Services {
    private Data data;

    public Services(Data data) {
        this.data = data;
    }

    public void login(String email, String password) {
        if (data.getCurrentUserLogged() != -1) {
            System.out.println("You're already logged in. Log out first.");
            return;
        }

        for (User user : JdbcSingleton.getInstance().getUsers()) {
            if (Objects.equals(user.getEmail(), email) && Objects.equals(user.getPassword(), password)) {
                data.setCurrentUserLogged(user.getId());
                System.out.println("Logged in succesfully.");
                AuditSingleton.getInstance().addActionToFile("login");
                return;
            }
        }
        data.setCurrentUserLogged(-1);
        System.out.println("The credentials are incorrect.");
    }

    public void register(String email, String username, String password) {
        if (data.getCurrentUserLogged() != -1) {
            System.out.println("You're already logged in. Log out first.");
            return;
        }

        Integer latestId = 0;
        for (User user : JdbcSingleton.getInstance().getUsers()) {
            if (Objects.equals(user.getEmail(), email)) {
                System.out.println("There's already a user with this email.");
                return;
            }
            if (user.getId() > latestId) {
                latestId = user.getId();
            }
        }
        User newUser = new User(latestId + 1, email, username, password);
//        data.addUser(newUser);
        JdbcSingleton.getInstance().createUser(newUser);
        UsersReadWriteSingleton.getInstance().writeDataToFile(newUser);
        System.out.println("User created. Please log in.");
        AuditSingleton.getInstance().addActionToFile("register");
    }

    public void logout() {
        if (data.getCurrentUserLogged() == -1) {
            System.out.println("You're not logged in.");
            return;
        }
        data.setCurrentUserLogged(-1);
        System.out.println("Logged out succesfully.");
        AuditSingleton.getInstance().addActionToFile("logout");
    }

    public void account() {
        if (data.getCurrentUserLogged() == -1) {
            System.out.println("You're not logged in.");
            return;
        }

        User user = JdbcSingleton.getInstance().getUser(data.getCurrentUserLogged());
        if (user != null) {
            System.out.println("Logged in as " + user.getUsername() + " with the email " + user.getEmail());
            AuditSingleton.getInstance().addActionToFile("account info");
            return;
        }
        System.out.println("An error occured. Please contact an administrator.");
    }

    public void createWorkspace(String title) {
        User user = JdbcSingleton.getInstance().getUser(data.getCurrentUserLogged());
        if (user == null) {
            System.out.println("You have to be logged in");
            return;
        }

        List<Workspace> workspaces = JdbcSingleton.getInstance().getWorkspaces(user.getId());
        Integer latestId = -1;
        for (Workspace workspace : workspaces) {
            if (Objects.equals(workspace.getTitle(), title)) {
                System.out.println("This workspace already exists");
                return;
            }

            if (workspace.getId() > latestId) {
                latestId = workspace.getId();
            }
        }

        JdbcSingleton.getInstance().createWorkspace(new Workspace(latestId + 1, title), user.getId());
//        workspaces.add(new Workspace(latestId + 1, title));
//        user.setWorkspaces(workspaces);
//        data.updateCurrentLoggedUser(user);
        System.out.println("Workspace " + title + " created.");
        AuditSingleton.getInstance().addActionToFile("create workspace");
    }

    public void listWorkspaces() {
        if (data.getCurrentUserLogged() == -1) {
            System.out.println("You have to be logged in");
            return;
        }

        for (Workspace workspace : JdbcSingleton.getInstance().getWorkspaces(data.getCurrentUserLogged())) {
            System.out.println(workspace.getTitle());
        }
        AuditSingleton.getInstance().addActionToFile("list workspaces");
    }

    public void deleteWorkspace(String title) {
        if (data.getCurrentUserLogged() == -1) {
            System.out.println("You have to be logged in");
            return;
        }

//        List<Workspace> workspaces = user.getWorkspaces();
//        workspaces.removeIf(workspace -> Objects.equals(workspace.getTitle(), title));
//        user.setWorkspaces(workspaces);
        if(JdbcSingleton.getInstance().getWorkspaceByTitle(title, data.getCurrentUserLogged()) == null) {
            System.out.println("Workspace " + title + " not existing");
            return;
        }
        JdbcSingleton.getInstance().deleteWorkspace(JdbcSingleton.getInstance().getWorkspaceByTitle(title, data.getCurrentUserLogged()));

        System.out.println("Workspace deleted.");
        AuditSingleton.getInstance().addActionToFile("delete workspace");
    }

    public void renameWorkspace(String oldTitle, String newTitle) {
        if (data.getCurrentUserLogged() == -1) {
            System.out.println("You have to be logged in");
            return;
        }
        Workspace workspace = JdbcSingleton.getInstance().getWorkspaceByTitle(oldTitle, data.getCurrentUserLogged());
        if(workspace == null) {
            System.out.println("Workspace " + oldTitle + " not existing");
            return;
        }
        if(JdbcSingleton.getInstance().getWorkspaceByTitle(newTitle, data.getCurrentUserLogged()) == null) {
            workspace.setTitle(newTitle);
            JdbcSingleton.getInstance().updateWorkspace(workspace);
            System.out.println("Workspace title updated from '" + oldTitle + "' to '" + newTitle + "'");
            AuditSingleton.getInstance().addActionToFile("rename workspace");
        } else {
            System.out.println("There's already a workspace with this name");
            return;
        }
    }

    public void createTask(String workspaceTitle, String title) {
        User user = data.getCurrentUserLoggedObject();
        if (user == null) {
            System.out.println("You have to be logged in");
            return;
        }

        Workspace workspace = null;
        for (Workspace w : user.getWorkspaces()) {
            if (Objects.equals(w.getTitle(), workspaceTitle)) {
                workspace = w;
            }
        }

        if (workspace == null) {
            System.out.println("This workspace does not exist");
            return;
        }

        Integer latestId = 0;
        for (Task task : workspace.getTasks()) {
            if (Objects.equals(task.getTitle(), title)) {
                System.out.println("This task already exists");
                return;
            }
            if (latestId < task.getId()) {
                latestId = task.getId();
            }
        }

        List<Task> tasks = workspace.getTasks();
        tasks.add(new Task(latestId + 1, title) {
            @Override
            public Integer getParentTaskId() {
                return null;
            }

            @Override
            public void setParentTaskId(Integer parentTaskId) {

            }
        });
        workspace.setTasks(tasks);
        System.out.println("Task '" + title + "' created.");
        AuditSingleton.getInstance().addActionToFile("create task");
    }

    public void deleteTask(String workspaceTitle, String title) {
        User user = data.getCurrentUserLoggedObject();
        if (user == null) {
            System.out.println("You have to be logged in");
            return;
        }

        Workspace workspace = null;
        for (Workspace w : user.getWorkspaces()) {
            if (Objects.equals(w.getTitle(), workspaceTitle)) {
                workspace = w;
            }
        }

        if (workspace == null) {
            System.out.println("This workspace does not exist");
            return;
        }

        List<Task> tasks = workspace.getTasks();
        tasks.removeIf(task -> Objects.equals(task.getTitle(), title));
        workspace.setTasks(tasks);
        System.out.println("Task '" + title + "' deleted.");
        AuditSingleton.getInstance().addActionToFile("delete task");
    }

    public void listTasks(String workspaceTitle) {
        User user = data.getCurrentUserLoggedObject();
        if (user == null) {
            System.out.println("You have to be logged in");
            return;
        }

        Workspace workspace = null;
        for (Workspace w : user.getWorkspaces()) {
            if (Objects.equals(w.getTitle(), workspaceTitle)) {
                workspace = w;
            }
        }

        if (workspace == null) {
            System.out.println("This workspace does not exist");
            return;
        }

        for (Task task : workspace.getTasks()) {
            if(task.getParentTaskId() == null) {
                System.out.println(task.getTitle());
            }
        }
        AuditSingleton.getInstance().addActionToFile("list tasks");
    }

    public void createSubtask(String workspaceTitle, String parentTaskTitle, String title) {
        User user = data.getCurrentUserLoggedObject();
        if (user == null) {
            System.out.println("You have to be logged in");
            return;
        }

        Workspace workspace = null;
        for (Workspace w : user.getWorkspaces()) {
            if (Objects.equals(w.getTitle(), workspaceTitle)) {
                workspace = w;
            }
        }

        if (workspace == null) {
            System.out.println("This workspace does not exist");
            return;
        }

        Integer latestId = 0;
        Integer parentTaskId = -1;
        for (Task task : workspace.getTasks()) {
            if (Objects.equals(task.getTitle(), title)) {
                System.out.println("This subtask already exists");
                return;
            }
            if (latestId < task.getId()) {
                latestId = task.getId();
            }
            if (Objects.equals(task.getTitle(), parentTaskTitle)) {
                parentTaskId = task.getId();
            }
        }

        if (parentTaskId == -1) {
            System.out.println("Parent task does not exist.");
            return;
        }

        List<Task> tasks = workspace.getTasks();
        tasks.add(new Subtask(parentTaskId, latestId + 1, title));
        workspace.setTasks(tasks);
        System.out.println("Subtask '" + title + "' created.");
        AuditSingleton.getInstance().addActionToFile("create subtask");
    }

    public void listSubtasks(String workspaceTitle, String parentTaskTitle) {
        User user = data.getCurrentUserLoggedObject();
        if (user == null) {
            System.out.println("You have to be logged in");
            return;
        }

        Workspace workspace = null;
        for (Workspace w : user.getWorkspaces()) {
            if (Objects.equals(w.getTitle(), workspaceTitle)) {
                workspace = w;
            }
        }

        if (workspace == null) {
            System.out.println("This workspace does not exist");
            return;
        }

        Integer latestId = 0;
        Integer parentTaskId = -1;
        for (Task task : workspace.getTasks()) {
            if (Objects.equals(task.getTitle(), parentTaskTitle)) {
                parentTaskId = task.getId();
            }
        }

        if (parentTaskId == -1) {
            System.out.println("Parent task does not exist.");
            return;
        }

        for (Task task : workspace.getTasks()) {
            if (Objects.equals(task.getParentTaskId(), parentTaskId)) {
                System.out.println(task.getTitle());
            }
        }
        AuditSingleton.getInstance().addActionToFile("list subtasks");
    }

}
