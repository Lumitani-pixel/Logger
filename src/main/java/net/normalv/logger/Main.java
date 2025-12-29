package net.normalv.logger;

public class Main {
    public static void main(String[] args) {
        Logger.info("Testing info with file write");
        Logger.debug("Testing debug with file write");
        Logger.warn("Testing warn with file write");
        Logger.error("Testing error with file write");

        Logger.setLogToFile(false);

        Logger.info("Testing info without file write");
        Logger.debug("Testing debug without file write");
        Logger.warn("Testing warn without file write");
        Logger.error("Testing error without file write");
        Logger.close();
    }
}
