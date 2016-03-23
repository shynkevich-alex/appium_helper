package com.degsoft.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

import static com.degsoft.utils.LoggerUtil.*;


public class Driver {
    private static AndroidDriver driver;
    private static Server server;

    public static AppiumDriver getInstance() {

//        if (driver == null) {
//            createDriver();
//        }
//        return driver;
        return null;
    }

//    private static boolean createDriver(boolean isRunServer) {
//
//        printDebug("Driver has not been initialised...");
//        try {
//
//            if (isRunServer) {
//                server = new Server();
//                server.start();
//                Thread.sleep(3000);
//
//                while (!server.isRunning()) ;
//                printInfo("Appium server started.");
//
//            }
//
//            URL address = new URL("http://" + configManager.getSererAddress() + ":" + configManager.getServerPort() + "/wd/hub");
//
//            driver = new AndroidDriver(address, getDesiredCapabilities());
//        } catch (Exception e) {
//            e.printStackTrace();
//            printError("Cannot create Appium driver! : " + e.getMessage());
//
//            if (isRunServer) {
//                server.end();
//            }
//
//            printError("Appium driver was not created");
//            return false;
//        }
//        return true;
//    }
//
//    private static DesiredCapabilities getDesiredCapabilities() {
//        ConfigManager configManager = ConfigManager.getInstance();
//
//        DesiredCapabilities capabilities = new DesiredCapabilities();
//        capabilities.setCapability(CapabilityType.PLATFORM, "Android");
//        capabilities.setCapability(CapabilityType.VERSION, configManager.getAndroidVersion());
//        capabilities.setCapability("deviceName", configManager.getDeviceId());
//        capabilities.setCapability("udid", configManager.getDeviceId());
//        capabilities.setCapability("app", configManager.getAppPath());
//        capabilities.setCapability("appPackage", "");
//        capabilities.setCapability("appActivity", "");
//        capabilities.setCapability("deviceReadyTimeout", 10);
//        return capabilities;
//    }
//
//    public static void quit() {
//        if (driver != null) {
//            driver.quit();
//            driver = null;
//
//            if (ConfigManager.getInstance().isRunServer()) {
//                server.end();
//            }
//        }
//    }
}
