package com.degsoft.appium;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static com.degsoft.utils.LoggerUtil.printDebug;

public class IOSHelper extends AppiumHelper {

    public IOSHelper(IOSDriver iosDriver) {
        super(iosDriver);
    }

    public WebElement findElementByText(String text, boolean isElementDisplayed) {
        return findElement(By.xpath("//*[@name=\'" + text + "\']"), isElementDisplayed);
    }

    public WebElement findElementByText(WebElement parent, String text, boolean isElementDisplayed) {
        if (parent == null) {
            printDebug("Parent element is null");
            return null;
        }

        String parentXpath = getElementXpath(parent);

        String xpath = parentXpath + "/descendant::*[@name=\'" + text + "\']";
        printDebug("##### FIND CHILD BY XPATH WITH NAME LOCATOR: " + xpath);

        return findElement(By.xpath(xpath), isElementDisplayed);
    }

    @Override
    public void pressKey(int androidKeyCode) {
        //todo implement for iOS
    }

    /*
        Method returns not 100% xpath value.
        Use carefully.
         */
    protected String getElementXpath(WebElement parent) {

        String parentTagName = getTagNameOfWebElement(parent);
        String locator = "";

        String parentName = getAttributeOfWebElement(parent, "name");
        parentName = parentName.equals("") ? "" : "@name=\'" + parentName + "\'";

        String parentLabel = getAttributeOfWebElement(parent, "label");
        parentLabel = parentLabel.equals("") ? "" : "@label=\'" + parentLabel + "\'";

        String parentValue = getAttributeOfWebElement(parent, "value");
        parentValue = parentValue.equals("") ? "" : "@value=\'" + parentValue + "\'";

        String parentX, parentY, parentWidth, parentHeight = "";

        Point location = getLocationOfElement(parent);
        int x = location.getX();
        parentX = location == null ? "" : "@x>=\'" + Integer.toString(x) + "\' and @x<\'" + Integer.toString(x + 1) + "\'";

        int y = location.getY();
        parentY = location == null ? "" : "@y>=\'" + Integer.toString(y) + "\' and @y<\'" + Integer.toString(y + 1) + "\'";

        Dimension size = getSizeOfElement(parent);
        int width = size.getWidth();
        parentWidth = size == null ? "" : "@width>=\'" + Integer.toString(width) + "\' and @width<\'" + Integer.toString(width + 1) + "\'";

        int height = size.getHeight();
        parentHeight = size == null ? "" : "@height>=\'" + Integer.toString(height) + "\' and @height<\'" + Integer.toString(height + 1) + "\'";

        ArrayList<String> locators = new ArrayList<>();
        locators.add(parentName);
        locators.add(parentLabel);
        locators.add(parentValue);
        locators.add(parentX);
        locators.add(parentY);
        locators.add(parentWidth);
        locators.add(parentHeight);

        boolean flag = false;

        for (String tempLocator : locators) {

            if (!tempLocator.equals("")) {
                locator = locator + (flag ? " and " : "") + tempLocator;

                flag = true;
            }
        }

        locator = "[" + locator + "]";

        return "//" + parentTagName + locator;
    }

    public WebElement findElementByText(String className, String text, boolean isElementDisplayed) {
        return findElement(By.xpath("//*//" + className + "[@name=\'" + text + "\']"), isElementDisplayed);
    }

    public List<WebElement> findElementsByText(String text, boolean isElementDisplayed) {
        return findElements(By.xpath("//*[@name=\'" + text + "\']"), isElementDisplayed);
    }

    public List<WebElement> findElementsById(String id, boolean isDisplayed) {
        try {

            List<WebElement> webElements = new ArrayList<>();
            List elements = appiumDriver.findElementsByAccessibilityId(id);

            if (isDisplayed) {

                for (int x = 0; x < elements.size(); x++) {
                    WebElement webElement = (WebElement) elements.get(x);

                    if (isElementDisplayed(webElement)) {
                        webElements.add(webElement);
                    }
                }
            } else {
                webElements = elements;
            }

            return webElements;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public WebElement findElementById(String id, boolean isElementDisplayed) {
        WebElement element = null;
        try {
            element = appiumDriver.findElementByAccessibilityId(id);
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

    /*
    Enter text to element
     */
    public void setValueInElement(WebElement webElement, String value) {
        try {
            IOSElement iosElement = (IOSElement) webElement;
            iosElement.setValue(value);
        } catch (Exception e) {
            printDebug("SET_VALUE method error");
            e.printStackTrace();
        }
    }

    /*
    WAITERS
     */

    public WebElement waitForElementByTextFromParent(final String parentAccessibilityId, final String name, long timeMs) {
        return waitForElementByTextFromParent(parentAccessibilityId, name, timeMs, true);
    }

    public WebElement waitForElementByTextFromParent(final String parentAccessibilityId, final String name, long timeMs, boolean isElementDisplayed) {
        long waiterStartTime = System.currentTimeMillis();
        WebElement parent = null;
        WebElement element = null;

        while (element == null) {
            if (System.currentTimeMillis() - waiterStartTime >= timeMs) {
                printDebug("button by name: \"" + name + "\" was not found.");
                return null;
            }
            parent = findElementById(parentAccessibilityId);

            if (parent != null) {
                element = findElementByText(parent, name, isElementDisplayed);
            }
        }
        return element;
    }

}
