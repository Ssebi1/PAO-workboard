package database;

import auth.User;

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
            String query = "DELETE FROM user WHERE id = (?);";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, user_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {
        try  {
            String query = "UPDATE user SET email = (?), username = (?), password = (?) WHERE id = (?);";

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
                String email = result.getString("emaili");
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
                String email = result.getString("emaili");
                String username = result.getString("username");
                String password = result.getString("password");

                return new User(Integer.parseInt(id), email, username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}