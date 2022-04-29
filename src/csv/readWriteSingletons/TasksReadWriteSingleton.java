package csv.readWriteSingletons;

import auth.User;
import workspace.Workspace;
import workspace.task.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TasksReadWriteSingleton {
    private static TasksReadWriteSingleton instance = null;
    private final String fileName;

    private TasksReadWriteSingleton(String fileName) {
        this.fileName = fileName;
    }

    public static TasksReadWriteSingleton getInstance() {
        if (instance == null)
            instance = new TasksReadWriteSingleton("src/csv/data/tasks.csv");

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
                        list.add(new Task(Integer.parseInt(fields[2]), fields[3]) {
                            @Override
                            public Integer getParentTaskId() {
                                return null;
                            }

                            @Override
                            public void setParentTaskId(Integer parentTaskId) {

                            }
                        });
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
                        data.append(user.getId()).append(',').append(workspace.getId()).append(',').append(task.getId()).append(',').append(task.getTitle()).append('\n');
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
