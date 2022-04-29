package csv.readWriteSingletons;

import auth.User;
import workspace.Workspace;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WorkspacesReadWriteSingleton {
    private static WorkspacesReadWriteSingleton instance = null;
    private final String fileName;

    private WorkspacesReadWriteSingleton(String fileName) {
        this.fileName = fileName;
    }

    public static WorkspacesReadWriteSingleton getInstance() {
        if (instance == null)
            instance = new WorkspacesReadWriteSingleton("src/csv/data/workspaces.csv");

        return instance;
    }

    public List<Workspace> readDataFromFile(int userId) {
        List<Workspace> list = new ArrayList<>();
        try (var in = new BufferedReader(new FileReader(fileName))) {
            String line = "";

            while ((line = in.readLine()) != null) {
                if (line.length() > 1) {
                    String[] fields = line.replaceAll(" ", "").split(",");
                    if (Integer.parseInt(fields[0]) == userId) {
                        Workspace workspace = new Workspace(Integer.parseInt(fields[1]), fields[2]);
                        list.add(workspace);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void writeDataToFile(List<User> users) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            StringBuilder data = new StringBuilder();
            for (User user : users) {
                for (Workspace workspace : user.getWorkspaces()) {
                    data.append(user.getId()).append(',').append(workspace.getId()).append(',').append(workspace.getTitle()).append('\n');
                }
            }
            writer.write(String.valueOf(data));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
