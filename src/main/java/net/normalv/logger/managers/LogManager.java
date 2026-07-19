package net.normalv.logger.managers;

import net.normalv.logger.Logger;

import java.io.*;
import java.nio.file.Paths;
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
        writer.flush();
        writer.close();

        log.renameTo(getRotated());

        cleanupOldLogs();
        if(createNew) createNewLogFile();
    }

    /**
     * Small method for getting the rotated file name
     */
    private File getRotated() {
        String time = Logger.getCurrentTime().replace(":", "-");
        return new File(folder,
                "log_" + time + ".txt");
    }

    /**
     * Compresses logs when to many log files to keep organized really simple
     */
    private void cleanupOldLogs() {
        File[] files = folder.listFiles((dir, name) ->
                name.endsWith(".txt") && !name.equals("current.log"));

        if (files == null || files.length < MAX_LOG_FILES) return;

        Arrays.sort(files, Comparator.comparingLong(File::lastModified));

        try {
            compress(files);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
    }

    /**
     * Simple compression I quickly learned this so it might be pretty bad
     * @param files The files to compress
     */
    private void compress(File[] files) throws IOException {
        final FileOutputStream fos = new FileOutputStream(files[1].getAbsolutePath().replace(".txt", ".zip"));
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        for (File file : files) {
            FileInputStream fis = new FileInputStream(file);
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }

        zipOut.close();
        fos.close();
    }

    /**
     * Synced closing function.
     * Called when the Main Program should be closed
     */
    public synchronized void close() {
        try {
            rotate(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}