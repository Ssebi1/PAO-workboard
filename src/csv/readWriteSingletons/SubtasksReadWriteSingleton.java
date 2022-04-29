package csv.readWriteSingletons;

import auth.User;
import workspace.Workspace;
import workspace.task.Subtask;
import workspace.task.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SubtasksReadWriteSingleton {
    private static SubtasksReadWriteSingleton instance = null;
    private final String fileName;

    private SubtasksReadWriteSingleton(String fileName) {
        this.fileName = fileName;
    }

    public static SubtasksReadWriteSingleton getInstance() {
        if (instance == null)
            instance = new SubtasksReadWriteSingleton("src/csv/data/subtasks.csv");

        return instance;
    }

    public List<Task> readDataFromFile(int userId, int workspaceId) {
        List<Task> list = new ArrayList<>();
        try (var in = new BufferedReader(new FileReader(fileName))) {
            String line = "";

            while ((line = in.readLine()) != null) {
                if (line.length() > 1) {
                    String[] fields = line.replaceAll(" ", "").split(",");
                    if (Integer.parseInt(fields[0]) == userId && Integer.parseInt(fields[1]) == workspaceId) {
                        list.add(new Subtask(Integer.parseInt(fields[3]), Integer.parseInt(fields[2]), fields[4]));
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
                    for (Task task : workspace.getTasks()) {
                        if(task.getParentTaskId() != null) {
                            data.append(user.getId()).append(',').append(workspace.getId()).append(',').append(task.getId()).append(',').append(task.getParentTaskId()).append(',').append(task.getTitle()).append('\n');
                        }
                    }
                }
            }
            writer.write(String.valueOf(data));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
