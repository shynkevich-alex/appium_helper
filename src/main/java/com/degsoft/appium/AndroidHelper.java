package com.degsoft.appium;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static com.degsoft.utils.LoggerUtil.printDebug;

public class AndroidHelper extends AppiumHelper {

    public AndroidHelper(AndroidDriver androidDriver) {
        super(androidDriver);
    }

    /*****
    FIND
     *****/

    @Override
    public WebElement findElementByText(String text, boolean isElementDisplayed) {
        return findElement(By.xpath("//*[@text=\'" + text + "\']"), isElementDisplayed);
    }

    @Override
    public WebElement findElementByText(WebElement parent, String text, boolean isElementDisplayed) {
        if (parent == null) {
            printDebug("Parent element is null");
            return null;
        }

        String parentXpath = getElementXpath(parent);

        String xpath = parentXpath + "/descendant::*[@text=\'" + text + "\']";
        printDebug("##### FIND CHILD BY XPATH WITH NAME LOCATOR: " + xpath);

        return findElement(By.xpath(xpath), isElementDisplayed);
    }

    @Override
    public WebElement findElementByText(String className, String text, boolean isElementDisplayed) {
        return findElement(By.xpath("//*//" + className + "[@text=\'" + text + "\']"), isElementDisplayed);
    }

    @Override
    public List<WebElement> findElementsByText(String text, boolean isElementDisplayed) {
        return findElements(By.xpath("//*[@text=\'" + text + "\']"), isElementDisplayed);
    }

    /*
  Keyevent
  Sample: AndroidKeyCode.Enter
   */
    public void pressKey(int androidKeyCode) {
        try {
            AndroidDriver androidDriver = (AndroidDriver) appiumDriver;
            androidDriver.pressKeyCode(androidKeyCode);
            printDebug("Key " + androidKeyCode + " clicked");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getElementXpath(WebElement parent) {

        String parentTagName = getTagNameOfWebElement(parent);
        String locator = "";

        String parentText = getAttributeOfWebElement(parent, "text");
        parentText = parentText.equals("") ? "" : "@text=\'" + parentText + "\'";

        String parentCheckable = getAttributeOfWebElement(parent, "checkable");
        parentCheckable = parentCheckable.equals("") ? "" : "@checkable=\'" + parentCheckable + "\'";

        String parentFocusable = getAttributeOfWebElement(parent, "focusable");
        parentFocusable = parentFocusable.equals("") ? "" : "@focusable=\'" + parentFocusable + "\'";

        String parentFocused = getAttributeOfWebElement(parent, "focused");
        parentFocused = parentFocused.equals("") ? "" : "@focused=\'" + parentFocused + "\'";

        String parentScrollable = getAttributeOfWebElement(parent, "scrollable");
        parentScrollable = parentScrollable.equals("") ? "" : "@scrollable=\'" + parentScrollable + "\'";

        String parentX, parentY, parentXend, parentYend = "";

        String parentBounds;

        Point location = getLocationOfElement(parent);
        int x = location.getX();
        parentX = Integer.toString(x);

        int y = location.getY();
        parentY = Integer.toString(y);

        Dimension size = getSizeOfElement(parent);
        int width = size.getWidth();
        parentXend = Integer.toString(x + width);

        int height = size.getHeight();
        parentYend = Integer.toString(y + height);

        parentBounds = "@bounds='["+ parentX +","+ parentY +"]["+ parentXend +","+ parentYend +"]'";

        ArrayList<String> locators = new ArrayList<>();
        locators.add(parentText);
        locators.add(parentCheckable);
        locators.add(parentFocusable);
        locators.add(parentFocused);
        locators.add(parentScrollable);
        locators.add(parentBounds);

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
}
