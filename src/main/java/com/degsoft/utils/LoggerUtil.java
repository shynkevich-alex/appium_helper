package com.degsoft.utils;

public class LoggerUtil {
    private static boolean isDebug = true;

    private static final String INFO = "INFO:";
    private static final String ERROR = "ERROR:";
    private static final String DEBUG = "DEBUG:";

    public static void printInfo(String logMessage) {
            System.out.println(INFO + " " + logMessage);
    }

    public static void printError(String logMessage) {
            System.out.println(ERROR + " " + logMessage);
    }

    public static void printDebug(String logMessage){
        if(isDebug) {
            System.out.println(DEBUG + " " + logMessage);
        }
    }
}
