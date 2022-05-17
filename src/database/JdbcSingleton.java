package database;

import auth.User;
import workspace.Workspace;
import workspace.task.Subtask;
import workspace.task.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcSingleton {

    private static JdbcSingleton instance;
    private final String connectionUrl = "jdbc:mysql://127.0.0.1:3306/workboard";
    private final String connectionUsername = "root";
    private final String connectionPassword = "";
    private Connection connection = null;

    private JdbcSingleton() {
    }

    public static JdbcSingleton getInstance() {
        if (instance == null)
            instance = new JdbcSingleton();
        return instance;
    }

    public void start() {
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionUsername, connectionPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // USERS CRUD
    public void createUser(User user) {
        try {
            String query = "insert into user values(?,?,?,?);";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int user_id) {
        try {
            String query = "DELETE FROM user WHERE id = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, user_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {
        try  {
            String query = "UPDATE user SET email = ?, username = ?, password = ? WHERE id = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(1, user.getPassword());
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getUsers() {
        try {
            String sql = "SELECT * FROM user";

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            List<User> users = new ArrayList<>();

            while (result.next()){
                String id = result.getString("id");
                String email = result.getString("email");
                String username = result.getString("username");
                String password = result.getString("password");

                User user = new User(Integer.parseInt(id), email, username, password);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUser(int user_id) {
        try {
            String sql = "SELECT * FROM user WHERE id = " + user_id + ";";

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while (result.next()){
                String id = result.getString("id");
                String email = result.getString("email");
                String username = result.getString("username");
                String password = result.getString("password");

                return new User(Integer.parseInt(id), email, username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Workspace CRUD
    public void createWorkspace(Workspace workspace, int user_id) {
        try {
            String query = "insert into workspace values(?,?,?);";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, workspace.getId());
            preparedStatement.setString(2, workspace.getTitle());
            preparedStatement.setInt(3, user_id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteWorkspace(Workspace workspace) {
        try {
            String query = "DELETE FROM workspace WHERE id = " + workspace.getId();

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateWorkspace(Workspace workspace) {
        try  {
            String query = "UPDATE workspace SET title = (?) WHERE id = (?);";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, workspace.getTitle());
            preparedStatement.setInt(2, workspace.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Workspace> getWorkspaces(int user_id) {
        try {
            String sql = "SELECT * FROM workspace WHERE user_id = " + user_id + ";";

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            List<Workspace> workspaces = new ArrayList<>();

            while (result.next()){
                int id = result.getInt("id");
                String title = result.getString("title");

                Workspace workspace = new Workspace(id, title);
                workspaces.add(workspace);
            }
            return workspaces;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Workspace getWorkspaceById(int workspace_id) {
        try {
            String sql = "SELECT * FROM workspace WHERE id = " + workspace_id + ";";

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while (result.next()){
                int id = result.getInt("id");
                String title = result.getString("title");

                return new Workspace(id, title);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Workspace getWorkspaceByTitle(String workspace_title ,int user_id) {
        try {
            String sql = "SELECT * FROM workspace WHERE title LIKE '" + workspace_title + "' AND user_id = " + user_id + ";";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet result = preparedStatement.executeQuery(sql);

            while (result.next()){
                int id = result.getInt("id");
                String title = result.getString("title");

                return new Workspace(id, title);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Subtasks CRUD
    public void createSubtask(Subtask subtask, int workspace_id) {
        try {
            String query = "insert into task values(?,?,?,NULL,?,?);";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, subtask.getId());
            preparedStatement.setString(2, subtask.getTitle());
            preparedStatement.setDate(3, (Date) subtask.getDueDate());
            preparedStatement.setInt(4, subtask.getParentTaskId());
            preparedStatement.setInt(5, workspace_id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSubtask(Subtask subtask, int workspace_id) {
        try {
            String query = "DELETE FROM task WHERE id = " + subtask.getId() + " AND workspace_id = " + workspace_id + " AND parent_task_id IS NOT NULL";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSubtask(Subtask subtask, int workspace_id) {
        try  {
            String query = "UPDATE task SET title = ?, date = ?, status_id = ? WHERE id = ?  AND workspace_id = ? AND parent_task_id = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, subtask.getTitle());
            preparedStatement.setDate(2, (Date) subtask.getDueDate());
            preparedStatement.setInt(3, subtask.getStatus().getId());
            preparedStatement.setInt(4, subtask.getId());
            preparedStatement.setInt(5, subtask.getParentTaskId());
            preparedStatement.setInt(5, workspace_id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Subtask> getSubtasks(int workspace_id, int parent_task_id) {
        try {
            String sql = "SELECT * FROM task WHERE workspace_id = " + workspace_id + " AND parent_task_id = " + parent_task_id + ";";

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            List<Subtask> subtasks = new ArrayList<>();

            while (result.next()){
                int id = result.getInt("id");
                String title = result.getString("title");

                Subtask task = new Subtask(null, id, title);
                subtasks.add(task);
            }
            return subtasks;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Subtask getSubtaskById(int subtask_id) {
        try {
            String sql = "SELECT * FROM task WHERE id = " + subtask_id + " AND parent_task_id IS NOT NULL" + ";";

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while (result.next()){
                int id = result.getInt("id");
                String title = result.getString("title");

                return new Subtask(null, id, title);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Subtask getSubtaskByTitle(String subtask_title ,int workspace_id, int parent_task_id) {
        try {
            String sql = "SELECT * FROM task WHERE title LIKE '" + subtask_title + "' AND workspace_id = " + workspace_id + " AND parent_task_id = " + parent_task_id + ";";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet result = preparedStatement.executeQuery(sql);

            while (result.next()){
                int id = result.getInt("id");
                String title = result.getString("title");

                return new Subtask(null, id, title);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Task CRUD
    public void createTask(Task task, int workspace_id) {
        try {
            String query = "insert into task values(?,?,?,NULL,NULL,?);";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, task.getId());
            preparedStatement.setString(2, task.getTitle());
            preparedStatement.setDate(3, (Date) task.getDueDate());
            preparedStatement.setInt(4, workspace_id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(Task task, int workspace_id) {
        try {
            String query = "DELETE FROM task WHERE id = " + task.getId() + " AND workspace_id = " + workspace_id + " AND parent_task_id IS NULL";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTask(Task task, int workspace_id) {
        try  {
            String query = "UPDATE task SET title = ?, date = ?, status_id = ? WHERE id = ?  AND workspace_id = ? AND parent_task_id = NULL;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setDate(2, (Date) task.getDueDate());
            preparedStatement.setInt(3, task.getStatus().getId());
            preparedStatement.setInt(4, task.getId());
            preparedStatement.setInt(5, workspace_id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Task> getTasks(int workspace_id) {
        try {
            String sql = "SELECT * FROM task WHERE workspace_id = " + workspace_id + " AND parent_task_id IS NULL" + ";";

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            List<Task> tasks = new ArrayList<>();

            while (result.next()){
                int id = result.getInt("id");
                String title = result.getString("title");

                Subtask task = new Subtask(null, id, title);
                tasks.add(task);
            }
            return tasks;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Task getTaskById(int task_id) {
        try {
            String sql = "SELECT * FROM task WHERE id = " + task_id + " AND parent_task_id IS NULL" + ";";

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while (result.next()){
                int id = result.getInt("id");
                String title = result.getString("title");

                return new Subtask(null, id, title);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Task getTaskByTitle(String task_title ,int workspace_id) {
        try {
            String sql = "SELECT * FROM task WHERE title LIKE '" + task_title + "' AND workspace_id = " + workspace_id + " AND parent_task_id IS NULL" + ";";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet result = preparedStatement.executeQuery(sql);

            while (result.next()){
                int id = result.getInt("id");
                String title = result.getString("title");

                return new Subtask(null, id, title);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}