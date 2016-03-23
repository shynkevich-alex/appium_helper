package com.degsoft.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    public List<WebElement> findElementsByText(String text){
        return findElementsByText(text, true);
    }

    public abstract List<WebElement> findElementsByText(String text, boolean isElementDisplayed);
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
    public String getTextOfElement(WebElement element) {
        String text = "";

        try {
            text = element.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    public boolean isElementSelected(WebElement element) {
        boolean isSelected = false;
        try {
            isSelected = element.isSelected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isSelected;
    }

    public Point getCenter(WebElement element) {

        Point upperLeft = element.getLocation();
        Dimension dimensions = element.getSize();
        return new Point(upperLeft.getX() + dimensions.getWidth() / 2, upperLeft.getY() + dimensions.getHeight() / 2);
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
}
