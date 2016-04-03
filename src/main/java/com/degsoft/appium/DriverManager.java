package com.degsoft.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

import static com.degsoft.appium.LoggerUtil.*;


public class DriverManager {
    private AppiumDriver driver;
    private AppiumDriverLocalService service;
    private boolean isRunServer;
    private String serverAddress;
    private String serverPort;
    private DesiredCapabilities desiredCapabilities;

    public DriverManager(boolean runAppiumServer, String serverAddress, String serverPort, DesiredCapabilities desiredCapabilities) {
        this.isRunServer = runAppiumServer;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.desiredCapabilities = desiredCapabilities;
    }

    public AppiumDriver createAppiumDriver() {
        try {

            if (isRunServer) {

                new Server(serverAddress, serverPort).killAppiumServer();

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
                while (!service.isRunning()) {
                    if (System.currentTimeMillis() - startTime > 20000) {
                        throw new Exception("DriverManager was not initialised");
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

    public void quitAppiumDriver() {
        if (driver != null) {
            driver.quit();

            if (service != null) {
                service.stop();
            }
        }
    }
}
