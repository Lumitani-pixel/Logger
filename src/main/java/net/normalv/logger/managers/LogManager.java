package net.normalv.logger.managers;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class LogManager {

    private static final long MAX_LOG_SIZE = 5 * 1024 * 1024;
    private static final int MAX_LOG_FILES = 10;

    private final File folder = new File("logs");
    private File log;
    private FileWriter writer;

    public LogManager() {
        if (!folder.exists()) folder.mkdirs();
        try {
            createNewLogFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createNewLogFile() throws IOException {
        log = new File(folder, "current.log");
        writer = new FileWriter(log, true);
    }

    public synchronized void log(String message) {
        try {
            writer.write(message + System.lineSeparator());
            writer.flush();
            if (log.length() >= MAX_LOG_SIZE) rotate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void rotate() throws IOException {
        writer.close();

        File rotated = new File(folder,
                "log_" + System.currentTimeMillis() + ".txt");
        log.renameTo(rotated);

        cleanupOldLogs();
        createNewLogFile();
    }

    private void cleanupOldLogs() {
        File[] files = folder.listFiles((dir, name) ->
                name.endsWith(".txt") && !name.equals("current.log"));

        if (files == null || files.length <= MAX_LOG_FILES) return;

        Arrays.sort(files, Comparator.comparingLong(File::lastModified));

        for (int i = 0; i < files.length - MAX_LOG_FILES; i++) {
            compress(files[i]);
            files[i].delete();
        }
    }

    private void compress(File file) {
        File zipFile = new File(file.getAbsolutePath().replace(".txt", ".zip"));

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
             FileInputStream fis = new FileInputStream(file)) {

            zos.putNextEntry(new ZipEntry(file.getName()));

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }

            zos.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void close() {
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}