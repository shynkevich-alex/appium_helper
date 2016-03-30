package com.degsoft.appium;

public class LoggerUtil {
    protected static boolean isDebug = true;

    private static final String INFO = "INFO:";
    private static final String ERROR = "ERROR:";
    private static final String DEBUG = "DEBUG:";

    protected static void printInfo(String logMessage) {
        System.out.println(INFO + " " + logMessage);
    }

    protected static void printError(String logMessage) {
        System.out.println(ERROR + " " + logMessage);
    }

    protected static void printDebug(String logMessage) {
        if (isDebug) {
            System.out.println(DEBUG + " " + logMessage);
        }
    }
}
