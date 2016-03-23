package com.degsoft.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import java.awt.*;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.degsoft.utils.LoggerUtil.*;

public abstract class AppiumHelper {

    protected AppiumDriver<WebElement> appiumDriver;

    protected AppiumHelper(AppiumDriver appiumDriver) {
        this.appiumDriver = appiumDriver;
    }

    public boolean isElementDisplayed(WebElement webElement) {
        try {
            return webElement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /*
           Find elements methods
    */

    public ArrayList<WebElement> findElements(By byElement) {
        return findElements(byElement, true);
    }

    public ArrayList<WebElement> findElements(By byElement, boolean isDisplayed) {
        try {

            ArrayList<WebElement> elements = new ArrayList<>();

            List<WebElement> webElements = appiumDriver.findElements(byElement);

            if (isDisplayed) {
                for (WebElement webElement : webElements) {

                    WebElement element = webElement;

                    if (isElementDisplayed(element)) {

                        elements.add(webElement);
                    }
                }
            } else {
                elements = (ArrayList<WebElement>) webElements;
            }
            return elements;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<WebElement> findElements(WebElement parent, By byElement) {
        return findElements(parent, byElement, true);
    }

    public ArrayList<WebElement> findElements(WebElement parent, By byElement, boolean isDisplayed) {
        try {
            ArrayList<WebElement> webElements = new ArrayList<>();

            List<WebElement> elements = parent.findElements(byElement);

            if (isDisplayed) {

                for (WebElement webElement : elements) {
                    WebElement element = webElement;

                    if (isElementDisplayed(element)) {
                        webElements.add(webElement);
                    }
                }
            } else {
                webElements = (ArrayList<WebElement>) elements;
            }
            return webElements;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<WebElement> findElementsById(String className, String id, boolean isDisplayed) {
        List<WebElement> elements = this.findElementsById(id, isDisplayed);
        ArrayList<WebElement> webElements = new ArrayList<>();

        if (elements == null || elements.isEmpty() || elements.size() == 0)
            return null;
        else {
            for (WebElement element : elements) {
                if (element.getTagName().equals(className)) {
                    webElements.add(element);
                }
            }
        }
        return webElements;
    }

    public List<WebElement> findElementsByText(String text){
        return findElementsByText(text, true);
    }

    public abstract List<WebElement> findElementsByText(String text, boolean isElementDisplayed);

    public abstract List<WebElement> findElementsById(String id, boolean isDisplayed);

    public WebElement findElement(By byElement) {
        return findElement(byElement, true);
    }

    public WebElement findElement(By byElement, boolean isElementDisplayed) {
        WebElement element = null;
        try {
            element = appiumDriver.findElement(byElement);
        } catch (Exception e) {
            return null;
        }
        if (isElementDisplayed) {
            if (!isElementDisplayed(element)) {
                return null;
            }
        }
        return element;
    }

    public WebElement findElement(WebElement parent, By byElement) {
        return findElement(parent, byElement, true);
    }

    public WebElement findElement(WebElement parent, By byElement, boolean isElementDisplayed) {
        WebElement element = null;
        try {
            element = parent.findElement(byElement);

            if (isElementDisplayed) {
                if (!element.isDisplayed()) {
                    return null;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return element;
    }


    public WebElement findElementByText(String text) {
        return findElementByText(text, true);
    }

    public abstract WebElement findElementByText(String text, boolean isElementDisplayed);

    public WebElement findElementByText(WebElement parent, String text){
        return findElementByText(parent, text, true);
    }

    public abstract WebElement findElementByText(WebElement parent, String text, boolean isElementDisplayed);

    public WebElement findElementByText(String className, String text) {
        return findElementByText(className, text, true);
    }

    public abstract WebElement findElementByText(String className, String text, boolean isElementDisplayed);

    public WebElement findElementById(String id) {
        return findElementById(id, true);
    }

    public abstract WebElement findElementById(String id, boolean isElementDisplayed);

    public WebElement findElementById(String className, String id) {
        return findElementById(className, id, true);
    }

    public WebElement findElementById(String className, String id, boolean isDisplayed) {
        List<WebElement> elements = this.findElementsById(id, isDisplayed);

        if (elements == null || elements.isEmpty() || elements.size() == 0)
            return null;
        else {
            for (WebElement element : elements) {
                if (element.getTagName().equals(className)) {
                    return element;
                }
            }
        }
        return null;
    }

    /*
    Waiters
     */

    public WebElement waitForElement(final By by, long timeMs) {
        return waitForElement(by, timeMs, true);
    }

    public WebElement waitForElement(final By by, long timeMs, boolean isElementDisplayed) {
        WebElement element = null;

        printDebug("wait element by: \"" + by.toString() + "\".");

        long waiterStartTime = System.currentTimeMillis();

//        long previousVal = -1;

        do {

            if (System.currentTimeMillis() - waiterStartTime >= timeMs) {
                printDebug("element by: \"" + by.toString() + "\" was not found.");
                return null;
            }

            element = findElement(by, isElementDisplayed);

//            long waitTime = timeMs - (System.currentTimeMillis() - waiterStartTime);
//            if (waitTime != previousVal) {
//                System.out.print("Wait countdown: " + (waitTime / 1000) + " sec" + "\r");
//                previousVal = waitTime;
//            }
        } while (element == null);

        return element;
    }

    public WebElement waitForElementByText(String text, long timeMs) {
        return waitForElementByText(text, timeMs, true);
    }

    public WebElement waitForElementByText(String text, long timeMs, boolean isElementDisplayed) {
        long waiterStartTime = System.currentTimeMillis();
        long startTime = 0;

        WebElement element = findElementByText(text, isElementDisplayed);
        while (element == null) {
            if (System.currentTimeMillis() - waiterStartTime >= timeMs) {
                printDebug("element by name: \"" + text + "\" was not found.");
                return null;
            }
            element = findElementByText(text, isElementDisplayed);
        }

        return element;
    }

    public WebElement waitForElementByText(String className, String text, long timeMs) {
        return waitForElementByText(className, text, timeMs, true);
    }

    public WebElement waitForElementByText(String className, String text, long timeMs, boolean isElementDisplayed) {
        long waiterStartTime = System.currentTimeMillis();

        WebElement element = findElementByText(className, text, isElementDisplayed);
        while (element == null) {
            if (System.currentTimeMillis() - waiterStartTime >= timeMs) {
                printDebug("element by name: \"" + text + "\" was not found.");
                return null;
            }
            element = findElementByText(className, text, isElementDisplayed);
        }
        return element;
    }

    public WebElement waitForElementByText(final By parentBy, final String text, long timeMs) {
        return waitForElementByText(parentBy, text, timeMs, true);
    }

    public WebElement waitForElementByText(final By parentBy, final String text, long timeMs, boolean isElementDisplayed) {
        long waiterStartTime = System.currentTimeMillis();
        WebElement parent = null;
        WebElement element = null;

        while (element == null) {
            if (System.currentTimeMillis() - waiterStartTime >= timeMs) {
                printDebug("button by name: \"" + text + "\" was not found.");
                return null;
            }
            parent = findElement(parentBy);
            if (parent != null) {
                element = findElementByText(parent, text, isElementDisplayed);
            }
        }
        return element;
    }

    public WebElement waitForElementById(String id, long timeMs) {
        return waitForElementById(id, timeMs, true);
    }

    public WebElement waitForElementById(String id, long timeMs, boolean isElementDisplayed) {
        long waiterStartTime = System.currentTimeMillis();
        long startTime = 0;

        WebElement element = this.findElementById(id, isElementDisplayed);
        while (element == null) {
            if (System.currentTimeMillis() - waiterStartTime >= timeMs) {
                printDebug("element by AccessibilityId: \"" + id + "\" was not found.");
                return null;
            }
            element = this.findElementById(id, isElementDisplayed);
        }

        return element;
    }

    public WebElement waitForElementById(String className, String id, long timeMs) {
        return waitForElementById(className, id, timeMs, true);
    }

    public WebElement waitForElementById(String className, String id, long timeMs, boolean isElementDisplayed) {
        long waiterStartTime = System.currentTimeMillis();
        long startTime = 0;

        WebElement element = findElementById(className, id, isElementDisplayed);
        while (element == null) {
            if (System.currentTimeMillis() - waiterStartTime >= timeMs) {
                printDebug("element by AccessibilityId: \"" + id + "\" was not found.");
                return null;
            }
            element = findElementById(className, id, isElementDisplayed);
        }

        return element;
    }

    public WebElement waitForElementByTextFromParent(final String parentClassName, final String parentId, final String name, long timeMs) {
        return waitForElementByTextFromParent(parentClassName, parentId, name, timeMs, true);
    }

    public WebElement waitForElementByTextFromParent(final String parentClassName, final String parentId, final String name, long timeMs, boolean isElementDisplayed) {
        long waiterStartTime = System.currentTimeMillis();
        WebElement parent = null;
        WebElement element = null;

        while (element == null) {
            if (System.currentTimeMillis() - waiterStartTime >= timeMs) {
                printDebug("button by name: \"" + name + "\" was not found.");
                return null;
            }
            parent = findElementById(parentClassName, parentId);

            if (parent != null) {
                element = findElementByText(parent, name, isElementDisplayed);
            }
        }
        return element;
    }

    public boolean waitGoneElement(final By by, long timeMs) {
        return waitGoneElement(by, timeMs, true);
    }

    public boolean waitGoneElement(final By by, long timeMs, boolean isElementDisplayed) {
        long waiterStartTime = System.currentTimeMillis();
        WebElement element = null;

        do {
            element = findElement(by, isElementDisplayed);

            if (System.currentTimeMillis() - waiterStartTime >= timeMs) {
                printDebug("element by: \"" + by.toString() + "\" exist.");
                return false;
            }

        } while (element != null);

        return true;
    }

    public boolean waitGoneElementByText(final String name, long timeMs) {
        return waitGoneElementByText(name, timeMs, true);
    }

    public boolean waitGoneElementByText(final String name, long timeMs, boolean isElementDisplayed) {
        long waiterStartTime = System.currentTimeMillis();

        WebElement element = null;
        do {
            element = findElementByText(name, isElementDisplayed);

            if (System.currentTimeMillis() - waiterStartTime >= timeMs) {
                printDebug("element by name: \"" + name + "\" exist.");
                return false;
            }

        } while (element != null);

        return true;
    }

    /*
    Scrolls, Swipes
     */
    public void swipe(int startX, int startY, int endX, int endY, int duration) {
        try {
            appiumDriver.swipe(startX, startY, endX, endY, duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean swipeIntoWebElement(WebElement webElement, Side swipeSide) {
        return swipeIntoWebElement(webElement, swipeSide, 0.75f, 1000);
    }

    public enum Side {
        UP, DOWN, LEFT, RIGHT
    }

    /*
    Available swipePrecent from 0.1 till 1
     */
    public boolean swipeIntoWebElement(WebElement webElement, Side swipeSide, float swipePercent, int duration) {
        if (webElement == null) {
            printDebug("Web element is null");
            return false;
        }

        int width = webElement.getSize().getWidth();
        int height = webElement.getSize().getHeight();
        int startPoint;
        int distance;

        Point point = webElement.getLocation();
        int x = point.getX();
        int y = point.getY();

        switch (swipeSide) {
            case LEFT:
                startPoint = x + (int) (width * swipePercent);
                distance = x;
                swipe(startPoint, y + height / 2, distance, y + height / 2, duration);
                break;
            case RIGHT:
                startPoint = (x + width) - (int) (width * swipePercent);
                distance = x + width;
                swipe(startPoint, y + height / 2, distance, y + height / 2, duration);
                break;
            case UP:
                startPoint = y + (int) (height * swipePercent);
                distance = y;
                swipe(x + width / 2, startPoint, x + width / 2, distance, duration);
                break;
            case DOWN:
                startPoint = (y + height) - (int) (height * swipePercent);
                distance = y + height;
                swipe(x + width / 2, startPoint, x + width / 2, distance, duration);
                break;
        }
        return true;
    }

    /*
    Method 'takeScreenshot' allows take and save screenshot from device.
     */
    public void takeScreenshot(String path, String name) {
        try {
            File file = appiumDriver.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(file, new File(path + name + ".png"));
            printDebug("Take screenshot: " + name + " by path : " + path);
        } catch (Exception e) {
            printDebug("Error in taking screenshot (" + name + ")");
        }
    }

    /*
    Getters of attribute
     */

    public boolean isElementSelected(WebElement element) {
        boolean isSelected = false;
        try {
            isSelected = element.isSelected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isSelected;
    }

    public boolean isElementEnabled(WebElement webElement) {
        try {
            return webElement.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isElementDisplayed(MobileElement mobileElement) {
        try {
            return mobileElement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    //todo check methods
    public ArrayList<WebElement> getChildrenOfElement(WebElement element, boolean isElementsDisplayed) {
        String parentXpath = getElementXpath(element);

        String xpath = parentXpath + "/child::*";
        printDebug("##### FIND CHILDREN BY XPATH WITH NAME LOCATOR: " + xpath);

        return findElements(By.xpath(xpath), isElementsDisplayed);
    }

    //Getting all cgildren
    public ArrayList<WebElement> getDescChildrenOfElement(WebElement element, boolean isElementsDisplayed) {
        String parentXpath = getElementXpath(element);

        String xpath = parentXpath + "/descendant::*";
        printDebug("##### FIND DESCENDANT CHILDREN BY XPATH WITH NAME LOCATOR: " + xpath);

        return findElements(By.xpath(xpath), isElementsDisplayed);
    }

    public Point getCenter(WebElement element) {

        Point upperLeft = element.getLocation();
        Dimension dimensions = element.getSize();
        return new Point(upperLeft.getX() + dimensions.getWidth() / 2, upperLeft.getY() + dimensions.getHeight() / 2);
    }

    public Dimension getSizeOfElement(WebElement webElement) {
        try {
            return webElement.getSize();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Point getLocationOfElement(WebElement webElement) {
        try {
            return webElement.getLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getTextOfWebElement(WebElement webElement) {
        try {
            return webElement.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getAttributeOfWebElement(WebElement webElement, String attribute) {
        try {
            return webElement.getAttribute(attribute);
        } catch (Exception e) {
            return "";
        }
    }

    public String getTagNameOfWebElement(WebElement element) {
        try {
            return element.getTagName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getTextOfElement(WebElement element) {
        String text = "";

        try {
            text = element.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    public static String getRandomString(int length) {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(rnd.nextInt("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".length())));
        }
        return sb.toString();
    }

    public static int getRandomNumber(int max) {
        if (max <= 0)
            return 0;
        int random = (int) (Math.random() * max + 1);
        return random;
    }

    public static double getRandomNumber(double start, double end) {
        double random = new Random().nextDouble();
        double result = start + (random * (end - start));
        return result;
    }

    /*
    Enter text to element
     */
    public void sendKeysToWebElement(WebElement webElement, String value) {
        try {
            webElement.sendKeys(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setValueInElement(WebElement webElement, String value){
        sendKeysToWebElement(webElement, value);
    }

    /*
    Sleepers
     */
    public void sleep(long timeMs) {
        try {
            new Thread().sleep(timeMs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void clickOnElement(WebElement element) {
        try {
            element.click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    Keyevent
    Sample: AndroidKeyCode.Enter
    */
    public abstract void pressKey(int androidKeyCode);

    /*
  Method returns not 100% xpath value.
  Use carefully.
   */
   protected abstract String getElementXpath(WebElement parent);

  /*
    Info
     */

    public void printElementInfo(WebElement element){
        if (element == null){
            printDebug("Provided Element is NULL");
        }

        printDebug("**********\n" +
                "TagName : " + getTagNameOfWebElement(element) + " \n" +
                "Value (text) : " + getTextOfWebElement(element) + " \n" +
                "Label : " + getAttributeOfWebElement(element, "label") + " \n" +
                "Is Displayed : " + isElementDisplayed(element) + " \n" +
                "Location : " + "x=" + getLocationOfElement(element).getX() + " y=" + getLocationOfElement(element).getY() + " \n" +
                "Size : " + "width=" + getSizeOfElement(element).getWidth() + " height=" + getSizeOfElement(element).getHeight() + " \n" +
                "\n************");
    }

    /***
     * comparing 2 images
     * method arguments should be string value of the file path
     * that should consist of /data/local/tmp/filename.png
     * where filename.png is the name of your screenshot image
     ***/
    public boolean compareTwoImages(String img1path, String img2path) {
        if (!new File(img1path).exists() || !new File(img2path).exists()) {
            printError("Some of screenshots not found:\n" +           img1path + "\n" + img2path);
            return false;
        }
        Image image1 = Toolkit.getDefaultToolkit().getImage(img1path);
        Image image2 = Toolkit.getDefaultToolkit().getImage(img2path);

        try {
            PixelGrabber grab1 = new PixelGrabber(image1, 0, 0, -1, -1, false);
            PixelGrabber grab2 = new PixelGrabber(image2, 0, 0, -1, -1, false);
            int[] data1 = null;

            if (grab1.grabPixels()) {
                int width = grab1.getWidth();
                int height = grab1.getHeight();
                data1 = new int[width * height];
                data1 = (int[]) grab1.getPixels();
            }

            int[] data2 = null;

            if (grab2.grabPixels()) {
                int width = grab2.getWidth();
                int height = grab2.getHeight();
                data2 = new int[width * height];
                data2 = (int[]) grab2.getPixels();
            }
            printInfo("Pixels equal: " + java.util.Arrays.equals(data1, data2));
            return java.util.Arrays.equals(data1, data2);

        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        return false;
    }
}
