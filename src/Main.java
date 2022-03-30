public class Main {
    public static void main(String[] args) {
        Data data = new Data();
        Services services = new Services(data);

        // register
        services.register("sebidancau@yahoo.com", "Ssebi", "1234");

        // login
        services.login("sebidancau@yahoo.com", "1234");

        // account info
        services.account();

        // logout
        services.logout();

        services.login("sebidancau@yahoo.com", "1234");

        // create workspace
        services.createWorkspace("test");
        services.createWorkspace("personal");

        // list workspaces
        services.listWorkspaces();

        // delete workspace
        services.deleteWorkspace("test");
        services.listWorkspaces();

        // rename workspace
        services.renameWorkspace("personal", "university");
        services.listWorkspaces();

        // create task
        services.createTask("university","Implement login");
        services.createTask("university","Implement register");
        services.createTask("university","Update ui");

        // delete task
        services.deleteTask("university", "Update ui");

        // list tasks
        services.listTasks("university");

        // create subtask
        services.createSubtask("university","Implement login", "api");
        services.createSubtask("university","Implement login", "frontend");

        services.listTasks("university");

        // list subtasks of task
        services.listSubtasks("university", "Implement login");

    }
}
