package csv.readWriteSingletons;

import auth.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UsersReadWriteSingleton {
    private static UsersReadWriteSingleton instance = null;
    private final String fileName;

    private UsersReadWriteSingleton(String fileName) {
        this.fileName = fileName;
    }

    public static UsersReadWriteSingleton getInstance() {
        if (instance == null)
            instance = new UsersReadWriteSingleton("src/csv/data/users.csv");

        return instance;
    }

    public List<User> readDataFromFile() {
        List<User> list = new ArrayList<>();
        try (var in = new BufferedReader(new FileReader(fileName))) {
            String line = "";

            while ((line = in.readLine()) != null) {
                if(line.length()>1) {
                    String[] fields = line.replaceAll(" ", "").split(",");
                    User user = new User(Integer.parseInt(fields[0]), fields[1], fields[2], fields[3]);
                    list.add(user);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void writeDataToFile(User user) {
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.append('\n');
            writer.append(String.valueOf(user.getId())).append(String.valueOf(',')).append(user.getEmail()).append(String.valueOf(',')).append(user.getEmail()).append(String.valueOf(',')).append(user.getPassword()).append(String.valueOf(','));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
