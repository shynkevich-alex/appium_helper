package com.degsoft.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

import static com.degsoft.utils.LoggerUtil.*;


public class Driver {
    private static AppiumDriver driver;
    private static Server server;
    private static boolean isRunServer;

    public static AppiumDriver getInstance(boolean startServer, String serverAddress, String serverPort, DesiredCapabilities desiredCapabilities) {

        if (driver == null) {
            isRunServer = startServer;
           driver = createDriver(isRunServer, serverAddress, serverPort, desiredCapabilities);
        }
        return driver;
    }

    public static AppiumDriver createDriver(boolean isRunServer, String serverAddress,
                                        String serverPort, DesiredCapabilities desiredCapabilities) {
        AppiumDriver driver = null;

        printDebug("Driver has not been initialised...");
        try {

            if (isRunServer) {
                server = new Server(serverAddress, serverPort);
                server.start();
                Thread.sleep(3000);

                while (!server.isRunning()) ;
                printInfo("Appium server started.");

            }

            URL url = new URL("http://" + serverAddress + ":" + serverPort + "/wd/hub");

            String platform = desiredCapabilities.getCapability(CapabilityType.PLATFORM).toString();
            printInfo("Platform: " + platform);

            switch (platform.toLowerCase()){
                case "android":
                    printInfo("Create ANDROID driver");
                    driver = new AndroidDriver(url, desiredCapabilities);
                    break;

                case "ios":
                    printInfo("Create iOS driver");
                    driver = new IOSDriver(url, desiredCapabilities);
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            printError("Cannot create Appium driver! : " + e.getMessage());

            if (isRunServer) {
                server.end();
            }

            printError("Appium driver was not created");
            return null;
        }
        return driver;
    }

    public static void quit() {
        if (driver != null) {
            driver.quit();
            driver = null;

            if (isRunServer) {
                server.end();
            }
        }
    }
}
