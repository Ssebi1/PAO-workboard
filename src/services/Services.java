package services;

import auth.User;
import data.Data;
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

        for (User user : data.getUsers()) {
            if (Objects.equals(user.getEmail(), email) && Objects.equals(user.getPassword(), password)) {
                data.setCurrentUserLogged(user.getId());
                System.out.println("Logged in succesfully.");
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
        for (User user : data.getUsers()) {
            if (Objects.equals(user.getEmail(), email)) {
                System.out.println("There's already a user with this email.");
                return;
            }
            if (user.getId() > latestId) {
                latestId = user.getId();
            }
        }
        data.addUser(new User(latestId, email, username, password));
        System.out.println("User created. Please log in.");
    }

    public void logout() {
        if (data.getCurrentUserLogged() == -1) {
            System.out.println("You're not logged in.");
            return;
        }
        data.setCurrentUserLogged(-1);
        System.out.println("Logged out succesfully.");
    }

    public void account() {
        if (data.getCurrentUserLogged() == -1) {
            System.out.println("You're not logged in.");
            return;
        }

        for (User user : data.getUsers()) {
            if (Objects.equals(user.getId(), data.getCurrentUserLogged())) {
                System.out.println("Logged in as " + user.getUsername() + " with the email " + user.getEmail());
                return;
            }
        }
        System.out.println("An error occured. Please contact an administrator.");
    }

    public void createWorkspace(String title) {
        User user = data.getCurrentUserLoggedObject();
        if (user == null) {
            System.out.println("You have to be logged in");
            return;
        }

        List<Workspace> workspaces = user.getWorkspaces();
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

        workspaces.add(new Workspace(latestId, title));
        user.setWorkspaces(workspaces);
        data.updateCurrentLoggedUser(user);
        System.out.println("Workspace " + title + " created.");
    }

    public void listWorkspaces() {
        User user = data.getCurrentUserLoggedObject();
        if (user == null) {
            System.out.println("You have to be logged in");
            return;
        }

        for (Workspace workspace : user.getWorkspaces()) {
            System.out.println("workspace-" + workspace.getTitle());
        }
    }

    public void deleteWorkspace(String title) {
        User user = data.getCurrentUserLoggedObject();
        if (user == null) {
            System.out.println("You have to be logged in");
            return;
        }

        List<Workspace> workspaces = user.getWorkspaces();
        workspaces.removeIf(workspace -> Objects.equals(workspace.getTitle(), title));
        user.setWorkspaces(workspaces);
        System.out.println("Workspace deleted.");
    }

    public void renameWorkspace(String oldTitle, String newTitle) {
        User user = data.getCurrentUserLoggedObject();
        if (user == null) {
            System.out.println("You have to be logged in");
            return;
        }

        List<Workspace> workspaces = user.getWorkspaces();
        for (Workspace workspace : workspaces) {
            if (Objects.equals(workspace.getTitle(), oldTitle)) {
                for (Workspace w : workspaces) {
                    if (Objects.equals(w.getTitle(), newTitle)) {
                        System.out.println("There's already a workspace with this name");
                        return;
                    }
                }
                workspace.setTitle(newTitle);
                System.out.println("Workspace title updated from '" + oldTitle + "' to '" + newTitle + "'");
            }
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
        tasks.add(new Task(latestId, title) {
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
            if(task.getClass().getName().equals("Task")) {
                System.out.println(task.getTitle());
            }
        }
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
        tasks.add(new Subtask(parentTaskId, latestId, title));
        workspace.setTasks(tasks);
        System.out.println("Subtask '" + title + "' created.");
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
    }

}