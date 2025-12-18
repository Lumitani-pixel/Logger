package net.normalv.managers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogManager {
    private static final long MAX_LOG_SIZE = 5 * 1024 * 1024; // 5 MB

    private File folder = new File("logs");
    private File log;
    private FileWriter writer;

    public LogManager() {
        int logFileCount = 1;
        if (!folder.exists()) folder.mkdirs();

        // Make a new log
        log = new File(folder, "log"+logFileCount+".txt");
        while (log.exists()) {
            logFileCount++;
            log = new File(folder, "log"+logFileCount+".txt");
        }

        // Make a new Instance of the File Writer
        try {
            log.createNewFile();
            writer = new FileWriter(log);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void log(String message) {
        try {
            writer.write(message);
            writer.write(System.lineSeparator());
            writer.flush();
            checkRotate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkRotate() throws IOException {
        if (log.length() >= MAX_LOG_SIZE) {
            rotate();
        }
    }

    private void rotate() throws IOException {
        writer.close();

        File rotated = new File(folder, log.getName().replace(".txt",
                "_" + System.currentTimeMillis() + ".txt"));
        log.renameTo(rotated);

        createNewLogFile();
    }

    private void createNewLogFile() throws IOException {
        log = new File(folder, "current.log");
        writer = new FileWriter(log, true);
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
