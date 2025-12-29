package net.normalv.logger;

import net.normalv.logger.managers.LogManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private final static String prefix = "Bobby Log";
    public static boolean logToFile = true;
    private static boolean logIntern = true;
    private final static LogManager logManager = new LogManager();

    public static void info(String msg)  { log(LogLevel.INFO, msg); }
    public static void warn(String msg)  { log(LogLevel.WARN, msg); }
    public static void error(String msg) { log(LogLevel.ERROR, msg); }
    public static void debug(String msg) { log(LogLevel.DEBUG, msg); }

    public static void log(LogLevel level, String message) {
        String time = getCurrentTime();

        if (logIntern) {
            System.out.println(
                    formatConsole(level, time, message)
            );
        }

        if (logToFile) {
            logManager.log(
                    formatFile(level, time, message)
            );
        }
    }

    private static String formatConsole(LogLevel level, String time, String msg) {
        return "\u001b[1m\u001b[32m[" + prefix + "]\u001b[0m"
                +":["+time+"]"
                +":["+level.getName()+"] "
                +levelToAnsi(level)+msg+"\u001b[0m";
    }

    private static String formatFile(LogLevel level, String time, String msg) {
        return "["+prefix+"]"
                +":["+time+"]"
                +":["+level.getName()+"] "
                +msg;
    }

    public static void setLogToFile(boolean toFile) {
        logToFile=toFile;
    }

    public static String getCurrentTime() {return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));}

    public static void close() {
        debug("==========Closing Logger==========");
        logManager.close();
    }

    private static String levelToAnsi(LogLevel level) {
        return switch (level) {
            case INFO -> "\u001b[0m";           // white
            case WARN -> "\u001b[33;1m";        // bold yellow
            case ERROR -> "\u001b[31;1m";       // bold red
            case DEBUG -> "\u001b[33m";         // cyan
        };
    }

    public enum LogLevel{
        INFO("INFO"),
        WARN("WARN"),
        ERROR("ERROR"),
        DEBUG("DEBUG");

        private String name;

        LogLevel(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
