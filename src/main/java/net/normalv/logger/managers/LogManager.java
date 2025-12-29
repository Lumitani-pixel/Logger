package net.normalv.logger.managers;

import net.normalv.logger.Logger;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class LogManager {

    //TODO: Make this configurable
    private static final long MAX_LOG_SIZE = 5 * 1024 * 1024;
    private static final int MAX_LOG_FILES = 10;

    private final File folder = new File("logs");
    private File log;
    private FileWriter writer;

    /**
     * When called ensures the right log folder is existing and we have a file to log to.
     */
    public LogManager() {
        if (!folder.exists()) folder.mkdirs();
        try {
            createNewLogFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Called when needing to create a new Log file
     * @throws IOException Security first :)
     */
    private void createNewLogFile() throws IOException {
        log = new File(folder, "current.log");
        writer = new FileWriter(log, true);
    }

    /**
     * Synced log action to log to a file (Not for console logging)
     * @param message What to log
     */
    public synchronized void log(String message) {
        try {
            writer.write(message + System.lineSeparator());
            writer.flush();
            if (log.length() >= MAX_LOG_SIZE) rotate(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finishes and renames logs and starts new one while cleaning up old logs
     * @throws IOException Security first :)
     */
    private void rotate(boolean createNew) throws IOException {
        writer.close();

        log.renameTo(getRotated());

        cleanupOldLogs();
        if(createNew) createNewLogFile();
    }

    /**
     * Small method for getting the rotated file name
     */
    private File getRotated() {
        return new File(folder,
                "log_" + Logger.getCurrentTime() + ".txt");
    }

    /**
     * Compresses logs when to many log files to keep organized really simple
     */
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

    /**
     * Simple compression I quickly learned this so it might be pretty bad
     * @param file The file to compress
     */
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

    /**
     * Synced closing function.
     * Called when the Main Program should be closed
     */
    public synchronized void close() {
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}