package com.degsoft.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.appium.java_client.service.local.flags.ServerArgument;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import static com.degsoft.utils.LoggerUtil.*;


public class Driver {
    private static AppiumDriver driver;
    private static AppiumDriverLocalService service;
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

        try {

            if (isRunServer) {

                new Server(serverAddress, serverPort).killAppium();

                DesiredCapabilities serverCapabilities = new DesiredCapabilities();
                serverCapabilities.setCapability(MobileCapabilityType.NO_RESET, true);

                AppiumServiceBuilder builder = new AppiumServiceBuilder().withCapabilities(serverCapabilities);
                builder.usingPort(Integer.parseInt(serverPort));
                builder.withIPAddress(serverAddress);
                builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
                builder.withArgument(GeneralServerFlag.LOG_LEVEL, "warn");

                service = builder.build();
                service.start();

                long startTime = System.currentTimeMillis();
                while (!service.isRunning()){
                    if (System.currentTimeMillis() - startTime > 20000){
                        throw new Exception("Driver was not initialised");
                    }
                }

                printInfo("Appium server started.");

            }

            URL url = new URL("http://" + serverAddress + ":" + serverPort + "/wd/hub");

            String platform = desiredCapabilities.getCapability(CapabilityType.PLATFORM).toString();
            printInfo("Platform: " + platform);

            switch (platform.toLowerCase()) {
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
                service.stop();
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

            if (isRunServer && service != null) {
                service.stop();
            }
        }
    }
}
