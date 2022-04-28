package csv.audit;

import auth.User;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AuditSingleton {
    private static AuditSingleton instance = null;
    private final String fileName;

    private AuditSingleton(String fileName) {
        this.fileName = fileName;
    }

    public static AuditSingleton getInstance() {
        if (instance == null)
            instance = new AuditSingleton("src/csv/audit/audit.csv");

        return instance;
    }

    public void addActionToFile(String action) {
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.append(action + ',' + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new java.util.Date()));
            writer.append('\n');
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
