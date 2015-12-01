package com.orasi.core.interfaces.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.StopWatch;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitWebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orasi.core.Beta;
import com.orasi.core.interfaces.Element;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestReporter;

/**
 * An implementation of the Element interface. Delegates its work to an
 * underlying WebElement instance for custom functionality.
 */
public class ElementImpl implements Element {

    protected WebElement element;
    protected OrasiDriver driver;
    private java.util.Date date = new java.util.Date();
    private java.util.Date dateAfter = new java.util.Date();
    private StopWatch stopwatch = new StopWatch();
    private long syncLoopTime = 250; // Milliseconds
    private long syncLoopTimeout = 5000; // Milliseconds

    public ElementImpl(final WebElement element) {
    	this.element = element;
    }
    
    public ElementImpl(final WebElement element, final OrasiDriver driver) {
    	this.element = element;
    	this.driver = driver;
    }


    /**
     * @see org.openqa.selenium.WebElement#click()
     */
    @Override
    public void click() {
	try {
	    element.click();
	} catch (RuntimeException rte) {
	    TestReporter
		    .interfaceLog("Clicked [ <font size = 2 color=\"red\"><b>@FindBy: "
			    + getElementLocatorInfo() + " </font></b>]");
	}
	/*TestReporter.interfaceLog("Clicked [ <b>@FindBy: "
		+ getElementLocatorInfo() + " </b>]");*/
    }

    @Override
    public void jsClick() {
	getWrappedDriver().executeJavaScript(
		"arguments[0].scrollIntoView(true);arguments[0].click();",
		element);
	TestReporter.interfaceLog("Clicked [ <b>@FindBy: "
		+ getElementLocatorInfo() + " </b>]");
    }

    @Override
    public void focus() {
	new Actions(getWrappedDriver()).moveToElement(element).click().perform();
    }

    @Override
    public void focusClick() {
	new Actions(getWrappedDriver()).moveToElement(element).click().perform();
	TestReporter.interfaceLog("Focus Clicked [ <b>@FindBy: "
		+ getElementLocatorInfo() + " </b>]");
    }

    /**
     * @see org.openqa.selenium.WebElement#getLocation()
     */
    @Override
    public Point getLocation() {
	return element.getLocation();
    }

    /**
     * @see org.openqa.selenium.WebElement#submit()
     */
    @Override
    public void submit() {
	element.submit();
    }

    /**
     * @see org.openqa.selenium.WebElement#getAttribute()
     */
    @Override
    public String getAttribute(String name) {
	return element.getAttribute(name);
    }

    /**
     * @see org.openqa.selenium.WebElement#getCssValue()
     */
    @Override
    public String getCssValue(String propertyName) {
	return element.getCssValue(propertyName);
    }

    /**
     * @see org.openqa.selenium.WebElement#getSize()
     */
    @Override
    public Dimension getSize() {
	try {
	    return element.getSize();
	} catch (WebDriverException wde) {
	    if (wde.getMessage().toLowerCase().contains("not implemented")) {
		TestReporter
			.logFailure("getSize has not been implemented by EdgeDriver");
	    }
	}
	return null;
    }

    /**
     * @see org.openqa.selenium.WebElement#findElements()
     */
    @Override
    public List<WebElement> findElements(By by) {
	return element.findElements(by);
    }

    /**
     * @see org.openqa.selenium.WebElement#getText()
     */
    @Override
    public String getText() {
	return element.getText();
    }

    /**
     * @see org.openqa.selenium.WebElement#getTagName()
     */
    @Override
    public String getTagName() {
	return element.getTagName();
    }

    /**
     * @see org.openqa.selenium.WebElement#findElement()
     */
    @Override
    public WebElement findElement(By by) {
	return element.findElement(by);
    }

    /**
     * @see org.openqa.selenium.WebElement#isEnabled()
     */
    @Override
    public boolean isEnabled() {
	return element.isEnabled();
    }

    /**
     * @see org.openqa.selenium.WebElement#isDisplayed()
     */
    @Override
    public boolean isDisplayed() {
	return element.isDisplayed();
    }

    /**
     * @see org.openqa.selenium.WebElement.isSelected()
     */
    @Override
    public boolean isSelected() {
	try {
	    return element.isSelected();
	} catch (WebDriverException wde) {
	    if (wde.getMessage().toLowerCase().contains("not implemented")) {
		TestReporter
			.logFailure(" isSelected has not been implemented by EdgeDriver");
	    }
	}
	return false;

    }

    /**
     * @see org.openqa.selenium.WebElement#clear()
     */
    @Override
    public void clear() {
	element.clear();
	TestReporter.interfaceLog(" Clear text from Element [ <b>@FindBy: " + getElementLocatorInfo() + " </b> ]");
    }

    /**
     * @see org.openqa.selenium.WebElement#sendKeys()
     */
    @Override
    public void sendKeys(CharSequence... keysToSend) {
	if (keysToSend.toString() != "") {
	    element.sendKeys(keysToSend);
	    TestReporter.interfaceLog(" Send Keys [ <b>"
		    + keysToSend[0].toString()
		    + "</b> ] to Textbox [ <b>@FindBy: "
		    + getElementLocatorInfo() + " </b> ]");
	}
    }

    /**
     * @see org.openqa.selenium.WebElement#getWrappedElement()
     */
    @Override
    public WebElement getWrappedElement() {
	return element;
    }

    @Override
    public OrasiDriver getWrappedDriver() {
    	if (driver == null){
    		Field privateStringField = null;
    		 try {
    	        	privateStringField = element.getClass().getDeclaredField("driver");
    	        	privateStringField.setAccessible(true);
    	        	 return (OrasiDriver)privateStringField.get(element);
    		 }catch(  NoSuchFieldException | IllegalArgumentException | IllegalAccessException | SecurityException e ){ e.printStackTrace();}
    	}
    	return driver;
    }
    /**
     * @see org.openqa.selenium.internal.Locatable#getCoordinates();
     */
    @Override
    public Coordinates getCoordinates() {
	return ((Locatable) element).getCoordinates();
    }

    @Override
    public boolean elementWired() {
	return (element != null);
    }

    /**
     * Used in conjunction with WebObjectPresent to determine if the desired
     * element is present in the DOM Will loop for the time out listed in
     * com.orasi.utils.Constants If object is not present within the time, throw
     * an error
     * 
     * @author Justin
     */
    @Override
    public boolean syncPresent() {
	return syncPresent(getWrappedDriver().getElementTimeout());
    }

    /**
     * Used in conjunction with WebObjectPresent to determine if the desired
     * element is present in the DOM Will loop for the time out passed in
     * parameter timeout If object is not present within the time, throw an
     * error
     * 
     * @author Justin
     */
    public boolean syncPresent(int timeout) {
	return syncPresent(timeout, true);
    }

    /**
     * Used in conjunction with WebObjectPresent to determine if the desired
     * element is present in the DOM Will loop for the time out passed in
     * parameter timeout If object is not present within the time, handle error
     * based on returnError
     * 
     * @author Justin
     */
    @Override
    public boolean syncPresent(int timeout, boolean returnError) {
	boolean found = false;
	final OrasiDriver driver = getWrappedDriver();
	By locator = getElementLocator();
	TestReporter.interfaceLog("<i> Syncing to element [ <b>@FindBy: "
		+ getElementLocatorInfo()
		+ "</b> ] to be <b>PRESENT</b> in DOM within [ " + timeout
		+ " ] seconds.</i>");
	int currentTimeout = driver.getElementTimeout();
	driver.setElementTimeout(1, TimeUnit.MILLISECONDS);
	
	stopwatch.start();
	do {
	    if (webElementPresent(driver, locator)) {
		found = true;
		break;
	    }
	   
	} while (stopwatch.getTime() / 1000.0 < (long) timeout);
	stopwatch.stop();
	stopwatch.reset();
	
	driver.setElementTimeout(currentTimeout);
	
	if (!found && returnError) {
	    dateAfter = new java.util.Date();
	    TestReporter.interfaceLog("<i>Element [<b>@FindBy: "
		    + getElementLocatorInfo()
		    + " </b>] is not <b>PRESENT</b> on the page after [ "
		    + (dateAfter.getTime() - date.getTime()) / 1000.0
		    + " ] seconds.</i>");
	    throw new RuntimeException("Element [ @FindBy: "
		    + getElementLocatorInfo()
		    + " ] is not PRESENT on the page after [ "
		    + (dateAfter.getTime() - date.getTime()) / 1000.0
		    + " ] seconds.");
	}
	return found;
    }

    /**
     *
     * Used in conjunction with WebObjectVisible to determine if the desired
     * element is visible on the screen Will loop for the time out listed in
     * org.orasi.chameleon.CONSTANT.TIMEOUT If object is not visible within the
     * time, throw an error
     * 
     * @author Justin
     */
    @Override
    public boolean syncVisible() {
	return syncVisible(getWrappedDriver().getElementTimeout());
    }

    /**
     * Used in conjunction with WebObjectVisible to determine if the desired
     * element is visible on the screen Will loop for the time out passed in the
     * variable timeout If object is not visible within the time, throw an error
     * 
     * @author Justin
     * 
     */
    public boolean syncVisible(int timeout) {
	return syncVisible(timeout, true);
    }

    /**
     * Used in conjunction with WebObjectVisible to determine if the desired
     * element is visible on the screen Will loop for the time out passed in the
     * variable timeout If object is not visible within the time, handle the
     * error based on the boolean
     *
     * @author Justin
     *
     */
    @Override
    public boolean syncVisible(int timeout,  boolean returnError) {
    final OrasiDriver driver = getWrappedDriver();
	boolean found = false;

	TestReporter.interfaceLog("<i>Syncing to element [<b>@FindBy: "
		+ getElementLocatorInfo()
		+ "</b> ] to be <b>VISIBLE<b> within [ " + timeout
		+ " ] seconds.</i>");
	int currentTimeout = driver.getElementTimeout();
	driver.setElementTimeout(1, TimeUnit.MILLISECONDS);
	
	stopwatch.start();
	do {
	    if (webElementVisible(driver, element)) {
		found = true;
		break;
	    }
	   
	} while (stopwatch.getTime() / 1000.0 < (long) timeout);
	stopwatch.stop();
	stopwatch.reset();
	
	driver.setElementTimeout(currentTimeout, TimeUnit.SECONDS);
	if (!found && returnError) {
	    dateAfter = new java.util.Date();
	    TestReporter.interfaceLog("<i>Element [<b>@FindBy: "
		    + getElementLocatorInfo()
		    + " </b>] is not <b>VISIBLE</b> on the page after [ "
		    + (dateAfter.getTime() - date.getTime()) / 1000.0
		    + " ] seconds.</i>");
	    throw new RuntimeException("Element [ @FindBy: "
		    + getElementLocatorInfo()
		    + " ] is not VISIBLE on the page after [ "
		    + (dateAfter.getTime() - date.getTime()) / 1000.0
		    + " ] seconds.");
	}
	return found;
    }

    /**
     * Used in conjunction with WebObjectVisible to determine if the desired
     * element is hidden from the screen Will loop for the time out listed in
     * org.orasi.chameleon.CONSTANT.TIMEOUT If object is not visible within the
     * time, throw an error
     * 
     * @author Justin
     * */
    @Override
    public boolean syncHidden() {
	return syncHidden( getWrappedDriver().getElementTimeout());
    }

    /**
     * Used in conjunction with WebObjectVisible to determine if the desired
     * element is hidden from the screen Will loop for the time out listed in
     * org.orasi.chameleon.CONSTANT.TIMEOUT If object is not visible within the
     * time, throw an error
     * 
     * @author Justin
     */
    public boolean syncHidden( int timeout) {
	return syncHidden(timeout, true);
    }

    /**
     * Used in conjunction with WebObjectVisible to determine if the desired
     * element is visible on the screen Will loop for the time out passed in the
     * variable timeout If object is not visible within the time, handle the
     * error based on the boolean
     * 
     * @author Justin
     */
    @Override
    public boolean syncHidden(int timeout, boolean returnError) {
	boolean found = false;
	final OrasiDriver driver = getWrappedDriver();
	TestReporter.interfaceLog("<i>Syncing to element [<b>@FindBy: "
		+ getElementLocatorInfo()
		+ "</b> ] to be <b>HIDDEN</b> within [ <b>" + timeout
		+ "</b> ] seconds.</i>");
	int currentTimeout = driver.getElementTimeout();
	driver.setElementTimeout(1, TimeUnit.MILLISECONDS);
	stopwatch.start();
	do {
	    if (!webElementVisible(driver, element)) {
		found = true;
		break;
	    }
	} while (stopwatch.getTime() / 1000.0 < (long) timeout);
	stopwatch.stop();
	stopwatch.reset();
	
	driver.setElementTimeout(currentTimeout, TimeUnit.SECONDS);
	if (!found && returnError) {
	    dateAfter = new java.util.Date();
	    TestReporter.interfaceLog("<i>Element [<b>@FindBy: "
		    + getElementLocatorInfo()
		    + " </b>] is not <b>HIDDEN</b> on the page after [ "
		    + (dateAfter.getTime() - date.getTime()) / 1000.0
		    + " ] seconds.</i>");
	    throw new RuntimeException("Element [ @FindBy: "
		    + getElementLocatorInfo()
		    + " ] is not HIDDEN on the page after [ "
		    + (dateAfter.getTime() - date.getTime()) / 1000.0
		    + " ] seconds.");
	}
	return found;
    }

    /**
     *
     * Used in conjunction with WebObjectEnabled to determine if the desired
     * element is enabled on the screen Will loop for the time out listed in
     * org.orasi.chameleon.CONSTANT.TIMEOUT If object is not enabled within the
     * time, throw an error
     * 
     * @author Justin
     */
    @Override
    public boolean syncEnabled() {
	return syncEnabled(getWrappedDriver().getElementTimeout());
    }

    /**
     * 
     * Used in conjunction with WebObjectEnabled to determine if the desired
     * element is enabled on the screen Will loop for the time out passed in the
     * variable timeout If object is not enabled within the time, throw an error
     * 
     * @author Justin
     * 
     */
    public boolean syncEnabled(int timeout) {
	return syncEnabled(timeout, true);
    }

    /**
     * Used in conjunction with WebObjectEnabled to determine if the desired
     * element is enabled on the screen Will loop for the time out passed in the
     * variable timeout If object is not enabled within the time, handle the
     * error based on the boolean
     *
     * @author Justin
     *
     */
    @Override
    public boolean syncEnabled(int timeout, boolean returnError) {
	boolean found = false;
	final OrasiDriver driver = getWrappedDriver();
	
	TestReporter.interfaceLog("<i>Syncing to element [<b>@FindBy: "
		+ getElementLocatorInfo()
		+ "</b> ] to be <b>ENABLED</b> within [ <b>" + timeout
		+ "</b> ] seconds.</i>");
	int currentTimeout = driver.getElementTimeout();
	driver.setElementTimeout(1, TimeUnit.MILLISECONDS);

	stopwatch.start();
	do {
	    if (webElementEnabled(driver, element)) {
		found = true;
		break;
	    }
	   
	} while (stopwatch.getTime() / 1000.0 < (long) timeout);
	stopwatch.stop();
	stopwatch.reset();
	
	driver.setElementTimeout(currentTimeout, TimeUnit.SECONDS);
	if (!found && returnError) {
	    dateAfter = new java.util.Date();
	    TestReporter.interfaceLog("<i>Element [<b>@FindBy: "
		    + getElementLocatorInfo()
		    + " </b>] is not <b>ENABLED</b> on the page after [ "
		    + (dateAfter.getTime() - date.getTime()) / 1000.0
		    + " ] seconds.</i>");
	    throw new RuntimeException("Element [ @FindBy: "
		    + getElementLocatorInfo()
		    + " ] is not ENABLED on the page after [ "
		    + (dateAfter.getTime() - date.getTime()) / 1000.0
		    + " ] seconds.");
	}
	return found;
    }

    /**
     *
     * Used in conjunction with WebObjectEnabled to determine if the desired
     * element is disabled on the screen Will loop for the time out listed in
     * org.orasi.chameleon.CONSTANT.TIMEOUT If object is not disabled within the
     * time, throw an error
     * 
     * @author Justin
     */
    @Override
    public boolean syncDisabled() {
	return syncDisabled(getWrappedDriver().getElementTimeout());
    }

    /**
     * 
     * Used in conjunction with WebObjectDisabled to determine if the desired
     * element is disabled on the screen Will loop for the time out passed in
     * the variable timeout If object is not disabled within the time, throw an
     * error
     * 
     * @author Justin
     * 
     */
    public boolean syncDisabled( int timeout) {
	return syncDisabled(timeout, true);
    }

    /**
     * Used in conjunction with WebObjectDisabled to determine if the desired
     * element is disabled on the screen Will loop for the time out passed in
     * the variable timeout If object is not disabled within the time, handle
     * the error based on the boolean
     *
     * @author Justin
     *
     */
    @Override
    public boolean syncDisabled(int timeout, boolean returnError) {
	boolean found = false;
	final OrasiDriver driver = getWrappedDriver();
	TestReporter.interfaceLog("<i>Syncing to element [<b>@FindBy: "
		+ getElementLocatorInfo()
		+ "</b> ] to be <b>DISABLED</b> within [ <b>" + timeout
		+ "</b> ] seconds.</i>");
	int currentTimeout = driver.getElementTimeout();
	driver.setElementTimeout(1, TimeUnit.MILLISECONDS);
	
	stopwatch.start();
	do {
	    if (!webElementEnabled(driver, element)) {
		found = true;
		break;
	    }
	} while (stopwatch.getTime() / 1000.0 < (long) timeout);
	stopwatch.stop();
	stopwatch.reset();
	
	driver.setElementTimeout(currentTimeout, TimeUnit.SECONDS);
	if (!found && returnError) {
	    dateAfter = new java.util.Date();
	    TestReporter.interfaceLog("<i>Element [<b>@FindBy: "
		    + getElementLocatorInfo()
		    + " </b>] is not <b>DISABLED</b> on the page after [ "
		    + (dateAfter.getTime() - date.getTime()) / 1000.0
		    + " ] seconds.</i>");
	    throw new RuntimeException("Element [ @FindBy: "
		    + getElementLocatorInfo()
		    + " ] is not DISABLED on the page after [ "
		    + (dateAfter.getTime() - date.getTime()) / 1000.0
		    + " ] seconds.");
	}
	return found;
    }

    /**
     *
     * Used in conjunction with WebObjectText Present to determine if the
     * desired text is present in the desired element Will loop for the time out
     * listed in org.orasi.chameleon.CONSTANT.TIMEOUT If text is not present
     * within the time, throw an error
     * 
     * @author Justin
     */
    @Override
    public boolean syncTextInElement(String text) {
	return syncTextInElement( text, getWrappedDriver().getElementTimeout());
    }

    /**
     * 
     * Used in conjunction with WebObjectText Present to determine if the
     * desired text is present in the desired element Will loop for the time out
     * passed in the variable timeout If text is not present within the time,
     * throw an error
     * 
     * @author Justin
     * 
     */
    public boolean syncTextInElement( String text, int timeout) {
	return syncTextInElement(text, Constants.ELEMENT_TIMEOUT, true);
    }

    /**
     * Used in conjunction with WebObjectText Present to determine if the
     * desired text is present in the desired element Will loop for the time out
     * passed in the variable timeout If text is not present within the time,
     * handle the error based on the boolean
     *
     * @author Justin
     *
     */
    @Override
    public boolean syncTextInElement( String text, int timeout, boolean returnError) {
    final OrasiDriver driver = getWrappedDriver();
	boolean found = false;
	TestReporter.interfaceLog("<i>Syncing to text [<b>" + text
		+ "</b> ] in element [<b>@FindBy: " + getElementLocatorInfo()
		+ "</b> ] to be displayed within [ <b>" + timeout
		+ "</b> ] seconds.</i>");
	int currentTimeout = driver.getElementTimeout();
	driver.setElementTimeout(1, TimeUnit.MILLISECONDS);
	stopwatch.start();
	do {
	    if (webElementTextPresent(driver, element, text)) {
		found = true;
		break;
	    }
	   
	} while (stopwatch.getTime() / 1000.0 < (long) timeout);
	stopwatch.stop();
	stopwatch.reset();
	
	driver.setElementTimeout(currentTimeout, TimeUnit.SECONDS);
	if (!found && returnError) {
	    dateAfter = new java.util.Date();
	    TestReporter.interfaceLog("<i>Element [<b>@FindBy: "
		    + getElementLocatorInfo()
		    + " </b>] did not contain the text [ " + text
		    + " ] after [ " + (dateAfter.getTime() - date.getTime())
		    / 1000.0 + " ] seconds.</i>");
	    throw new RuntimeException("Element [ @FindBy: "
		    + getElementLocatorInfo()
		    + " ] did not contain the text [ " + text + " ] after [ "
		    + (dateAfter.getTime() - date.getTime()) / 1000.0
		    + " ] seconds.");
	}
	return found;
    }

    /**
     * Use WebDriver Wait to determine if object is present in the DOM or not
     * 
     * @author Justin
     * @param driver
     *            Main WebDriver
     * @param locator
     *            {@link By} object to search for
     * @return TRUE if element is currently present in the DOM, FALSE if the
     *         element is not present in the DOM
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean webElementPresent(WebDriver driver, By locator) {
	Wait wait = new WebDriverWait(driver, 0);

	try {
	    return wait.until(ExpectedConditions
		    .presenceOfElementLocated(locator)) != null;
	} catch (NoSuchElementException | ClassCastException
		| StaleElementReferenceException | TimeoutException e) {
	    return false;
	}

    }

    /**
     * Use WebDriver Wait to determine if object is visible on the screen or not
     * 
     * @author Justin
     * @param driver
     *            Main WebDriver
     * @param element
     *            Element to search for
     * @return TRUE if element is currently visible on the screen, FALSE if the
     *         element is not visible on the screen
     */
    private boolean webElementVisible(WebDriver driver, WebElement element) {
	try {
	    Point location = element.getLocation();

	    Dimension size = element.getSize();
	    if ((location.getX() > 0 & location.getY() > 0)
		    | (size.getHeight() > 0 & size.getWidth() > 0)) {
		if (element.getAttribute("hidden") != null)
		    return false;
		if (element.getAttribute("type") != null) {
		    if (element.getAttribute("type").equals("hidden"))
			return false;
		}
		return true;
	    } else {
		return false;
	    }

	} catch (WebDriverException | ClassCastException e) {
	    return false;
	}
    }

    /**
     * Use WebDriver Wait to determine if object is enabled on the screen or not
     * 
     * @author Justin
     * @param driver
     *            Main WebDriver
     * @param element
     *            Element to search for
     * @return TRUE if element is currently enabled on the screen, FALSE if the
     *         element is not enabled on the screen
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean webElementEnabled(WebDriver driver, WebElement element) {
	Wait wait = new WebDriverWait(driver, 0);

	try {
	    return wait.until(ExpectedConditions.elementToBeClickable(element)) != null;
	    // return element.isEnabled();
	} catch (NoSuchElementException | ClassCastException
		| StaleElementReferenceException | TimeoutException e) {
	    return false;
	}

    }

    /**
     * Use WebDriver Wait to determine if object contains the expected text
     * 
     * @author Justin
     * @param driver
     *            Main WebDriver
     * @param element
     *            Element to search for
     * @param text
     *            The text to search for
     * @return TRUE if element is currently visible on the screen, FALSE if the
     *         element is not visible on the screen
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean webElementTextPresent(WebDriver driver, WebElement element,
	    String text) {
	Wait wait = new WebDriverWait(driver, 0);
	try {

	    if (wait.until(ExpectedConditions.textToBePresentInElement(element,
		    text)) != null) {
		return true;
	    } else if (wait.until(ExpectedConditions
		    .textToBePresentInElementValue(element, text)) != null) {
		return true;
	    } else {
		return false;
	    }

	} catch (NoSuchElementException | ClassCastException
		| StaleElementReferenceException | TimeoutException e) {
	    try {
		if (wait.until(ExpectedConditions
			.textToBePresentInElementValue(element, text)) != null) {
		    return true;
		} else {
		    return false;
		}
	    } catch (NoSuchElementException | ClassCastException
		    | StaleElementReferenceException | TimeoutException e2) {
		return false;
	    } catch (WebDriverException wde) {
		/*
		 * if(driver instanceof EdgeDriver){ TestReporter.logFailure(
		 * "ExpectedConditions.textToBePresentInElementValue has not been implemented by Edge Driver"
		 * ); }
		 */
		return false;
	    }

	}
    }

    /**
     * Get the By Locator object used to create this element
     * 
     * @author Justin
     * @return {@link By} Return the By object to reuse
     */
    @Override
    public By getElementLocator() {
	By by = null;
	String locator = "";
	int startPosition = 0;
	try {
	    locator = getElementLocatorAsString();
	    switch (locator) {
	    case "className":
		by = new ByClassName(getElementIdentifier());
		break;
	    case "cssSelector":
		by = By.cssSelector(getElementIdentifier());
		break;
	    case "id":
		by = By.id(getElementIdentifier());
		break;
	    case "linkText":
		by = By.linkText(getElementIdentifier());
		break;
	    case "name":
		by = By.name(getElementIdentifier());
		break;
	    case "tagName":
		by = By.tagName(getElementIdentifier());
		break;
	    case "xpath":
		by = By.xpath(getElementIdentifier());
		break;
	    }
	    return by;
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }

    @Override
    public String getElementIdentifier() {
	String locator = "";
	int startPosition = 0;
	int endPosition = 0;
	if (element instanceof HtmlUnitWebElement) {
	    startPosition = element.toString().indexOf("=\"") + 2;
	    endPosition = element.toString().indexOf("\"",
		    element.toString().indexOf("=\"") + 3);
	    if (startPosition == -1 | endPosition == -1)
		locator = element.toString();
	    else
		locator = element.toString().substring(startPosition,
			endPosition);
	} else if (element instanceof ElementImpl){
	    Field elementField = null;
	    try {
		elementField = element.getClass().getDeclaredField("element");
		elementField.setAccessible(true);
		
		startPosition = elementField.get(element).toString().lastIndexOf(": ") + 2;
		locator = elementField.get(element).toString().substring(startPosition,
			elementField.get(element).toString().lastIndexOf("]"));
	    }catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e){
		e.printStackTrace();	    
	    }
	    
	} else {
	    startPosition = element.toString().lastIndexOf(": ") + 2;
	    locator = element.toString().substring(startPosition,
		    element.toString().lastIndexOf("]"));
	}
	return locator.trim();
    }

    /**
     * Get the By Locator object used to create this element
     * 
     * @author Justin
     * @return {@link By} Return the By object to reuse
     */
    private String getElementLocatorAsString() {
	int startPosition = 0;
	String locator = "";

	if (element instanceof HtmlUnitWebElement) {
	    startPosition = element.toString().indexOf(" ");
	    if (startPosition == -1)
		locator = element.toString();
	    else
		locator = element.toString().substring(startPosition,
			element.toString().indexOf("="));
	} else if (element instanceof ElementImpl){
	    Field elementField = null;
	    try {
		elementField = element.getClass().getDeclaredField("element");
		elementField.setAccessible(true);
		
		  startPosition = elementField.get(element).toString().lastIndexOf("->") + 3;
		  locator = elementField.get(element).toString().substring(startPosition,
			  elementField.get(element).toString().lastIndexOf(":"));
	    }catch (IllegalAccessException | NoSuchFieldException | SecurityException e){
		e.printStackTrace();	    
	    }

	}else {
	
	    // if (element instanceof HtmlUnitWebElement)
	    startPosition = element.toString().lastIndexOf("->") + 3;
	    locator = element.toString().substring(startPosition,
		    element.toString().lastIndexOf(":"));
	}
	locator = locator.trim();
	return locator;

    }

    @Override
    public String getElementLocatorInfo() {
	return getElementLocatorAsString() + " = " + getElementIdentifier();
    }

    @Override
    public void highlight() {
	
	driver.executeJavaScript("arguments[0].style.border='3px solid red'",this);
    }

    @Override
    public void scrollIntoView() {
	
	driver.executeJavaScript("arguments[0].scrollIntoView(true);", element);
    }

    @Override
    public ArrayList getAllAttributes() {
	
	return (ArrayList)  driver.executeJavaScript("var s = []; var attrs = arguments[0].attributes; for (var l = 0; l < attrs.length; ++l) { var a = attrs[l]; s.push(a.name + ':' + a.value); } ; return s;",
			getWrappedElement());
    }
    
    @Beta
	@Override
	public <X> X getScreenshotAs(OutputType<X> target) {
		// TODO Auto-generated method stub
		return ((TakesScreenshot)driver.getDriver()).getScreenshotAs(target);
	}
	
 /*   @Override
    public <X> X getScreenshotAs(OutputType<X> target)
	    throws WebDriverException {
    	getScreenshotAs(target);
	// String base64 =
	// execute(DriverCommand.SCREENSHOT).getValue().toString();
	// ... and convert it.

	System.out.println("getScreenShotAs is unimplemented");
	return null; // target.convertFromBase64Png(base64);
    }*/
}