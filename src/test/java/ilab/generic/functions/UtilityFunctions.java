package ilab.generic.functions;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


public class UtilityFunctions {

    private String processName;
    protected WebDriver driver;

    public static ExtentHtmlReporter htmlReporter;
    public static ExtentReports extent;
    public static ExtentTest extentTest;
    public static Reporting repo;


    public WebDriver getWebdriver() {
        return driver;
    }

    @SuppressWarnings("static-access")
    public void setWebDriver(WebDriver DriverTest) {

        this.driver = DriverTest;
    }

    @SuppressWarnings("rawtypes")
    public WebDriver initializeWedriver(String sdriverName, String strURL, DesiredCapabilities capabilities) {

        try {

            switch (sdriverName.toUpperCase()) {

                case "CHROME":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions ChromeOption = new ChromeOptions();
                    ChromeOption.setExperimentalOption("useAutomationExtension", false);
                    driver = new ChromeDriver(ChromeOption);
                    driver.manage().window().maximize();
                    break;

                case "FIREFOX":
                    WebDriverManager.firefoxdriver().setup();
                    ChromeOptions FireFoxOption = new ChromeOptions();
                    FireFoxOption.setExperimentalOption("useAutomationExtension", false);
                    driver = new ChromeDriver(FireFoxOption);
                    driver.manage().window().maximize();
                    break;

                case "IE":
                    WebDriverManager.iedriver().setup();
                    ChromeOptions IEOption = new ChromeOptions();
                    IEOption.setExperimentalOption("useAutomationExtension", false);
                    driver = new ChromeDriver(IEOption);
                    driver.manage().window().maximize();
                    break;

                case "EDGE":
                    WebDriverManager.edgedriver().setup();
                    ChromeOptions EdgeOption = new ChromeOptions();
                    EdgeOption.setExperimentalOption("useAutomationExtension", false);
                    driver = new ChromeDriver(EdgeOption);
                    driver.manage().window().maximize();
                    break;

                case "IOS":
                    driver = (IOSDriver) new IOSDriver(new URL(strURL), capabilities);
                    break;

                case "ANDROID":
                    driver = (AndroidDriver) new AndroidDriver(new URL(strURL), capabilities);
                    break;

                case "KOBITON":
                    driver = (AndroidDriver) new AndroidDriver<WebElement>(new URL(strURL), capabilities);
                    break;


            }

            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            //driver.manage().window().maximize();

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return driver;

    }

    /*******************************************************************************General Function Area***********************************************************************/
    public void WindowsProcess(String processName) {
        this.processName = processName;
    }

    public void CloseRunningProcess() throws Exception {
        if (isRunning()) {
            getRuntime().exec("taskkill /F /IM " + processName);
        }
    }

    private boolean isRunning() throws Exception {
        Process listTasksProcess = getRuntime().exec("tasklist");
        BufferedReader tasksListReader = new BufferedReader(
                new InputStreamReader(listTasksProcess.getInputStream()));

        String tasksLine;

        while ((tasksLine = tasksListReader.readLine()) != null) {
            if (tasksLine.contains(processName)) {
                return true;
            }
        }

        return false;
    }

    private Runtime getRuntime() {

        return Runtime.getRuntime();
    }

    public void navigate(String URL) {
        driver.get(URL);
    }


    public String getCurrentTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return sdf.format(timestamp);

    }


    public void SendEmail(String sfrom, String sto, String sReportName) {
        // Recipient's email ID needs to be mentioned.
        String to = sto;

        // Sender's email ID needs to be mentioned
        String from = sfrom;

        final String username = "tkokela@gmail.com";//change accordingly
        final String password = "kokela@001";//change accordingly

        // Assuming you are sending email through relay.jangosmtp.net
        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "25");

        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject("Please see attached automation results");

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText("This is message body");

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            String filename = "report/" + sReportName + ".html";
            DataSource source = new FileDataSource(filename);

            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully to: " + sto);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    /********************************************************************************************************************************************
     Selenium Area
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException */

    public void WaitForElementVisibility(String property, String path, int intWait) throws SAXException, IOException, ParserConfigurationException {
        WebDriverWait wait = new WebDriverWait(driver, intWait);
        String[] element = xmlParser(path, property);
        try {
            switch (element[0].toUpperCase()) {
                case "XPATH":
                    wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(element[1])));
                    break;

                case "ID":
                    wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id(element[1])));
                    break;

                case "NAME":
                    wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.name(element[1])));
                    break;

                case "LINKTEXT":
                    wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.linkText(element[1])));
                    break;

                case "CSSSELECTOR":
                    wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(element[1])));
                    break;

            }
        } catch (Exception e) {
            System.out.println("Element " + element[1] + " NOT found.");
        }
    }

    public void WaitForElementToBeClickable(String property, String path, int intWait) throws SAXException, IOException, ParserConfigurationException {
        WebDriverWait wait = new WebDriverWait(driver, intWait);
        String[] element = xmlParser(path, property);
        try {
            switch (element[0].toUpperCase()) {
                case "XPATH":
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element[1])));
                    break;

                case "ID":
                    wait.until(ExpectedConditions.elementToBeClickable(By.id(element[1])));
                    break;

                case "NAME":
                    wait.until(ExpectedConditions.elementToBeClickable(By.name(element[1])));
                    break;

                case "LINKTEXT":
                    wait.until(ExpectedConditions.elementToBeClickable(By.linkText(element[1])));
                    break;

                case "CSSSELECTOR":
                    wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(element[1])));
                    break;
            }

        } catch (Exception e) {
            System.out.println("Element " + element[1] + " NOT found.");
        }
    }

    public boolean waitFoInvisibilityOfElement(String property, String path, int intWait) throws ParserConfigurationException, SAXException, IOException {
        boolean object;
        WebDriverWait wait = new WebDriverWait(driver, intWait);
        String[] element = xmlParser(path, property);
        try {
            switch (element[0].toUpperCase()) {
                case "XPATH":
                    object = checkObject(element,intWait);
                    customException(object);
                    break;

                case "ID":
                    wait.until(ExpectedConditions.invisibilityOfElementLocated((By) By.id(element[1])));
                    break;

                case "NAME":
                    wait.until(ExpectedConditions.invisibilityOfElementLocated((By) By.name(element[1])));
                    break;

                case "LINKTEXT":
                    wait.until(ExpectedConditions.invisibilityOfElementLocated((By) By.linkText(element[1])));
                    break;

                case "CSSSELECTOR":
                    wait.until(ExpectedConditions.invisibilityOfElementLocated((By) By.cssSelector(element[1])));
                    break;
            }
            System.out.println("Element " + element[1] + " Not found Clear To Continue.");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean checkObject(String[] element, int WaitCount){
        int counter = 0;
        try
        {
            while (true)
            {
                try
                {
                    if (getWebdriver().findElement(By.xpath(element[1])).isDisplayed() == true) {
                        counter = counter + 1;
                        Thread.sleep(1000);
                        try{
                            if(counter == WaitCount){
                                customException(false);
                            }
                        }catch (Exception e){
                            System.out.println(e.getMessage());
                            return false;
                        }
                    }
                }
                catch (NoSuchElementException ex)
                {
                    System.out.println(ex.getMessage());
                    break;
                }
            }
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void customException(boolean object) throws Exception {
        if(object ==false){
            throw new Exception("Element still visible");
        }
    }

    /*****************************************************************************
     Function Name: 	ClickObject
     Description:	click an object in an application using either xpath, ID, Name, linktext and CssSelector and maximum wait time
     Date Created:	13/09/2017
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     ******************************************************************************/
    public void ClickObject(String property, String path,ExtentTest logger) throws SAXException, IOException, ParserConfigurationException {
        try{
            String[] element = xmlParser(path, property);
            switch (element[0].toUpperCase()) {
                case "XPATH":
                    driver.findElement(By.xpath(element[1])).click();
                    break;

                case "ID":
                    driver.findElement(By.id(element[1])).click();
                    break;

                case "NAME":
                    driver.findElement(By.name(element[1])).click();
                    break;

                case "LINKTEXT":
                    driver.findElement(By.linkText(element[1])).click();
                    break;

                case "CSSSELECTOR":
                    driver.findElement(By.cssSelector(element[1])).click();
                    break;
            }
        }catch (Exception e) {
            System.out.println(e.getStackTrace());
            logger.fail(e.getMessage());
        }
        //get object properties from the xml file repository

    }


    public void ClickObjectContainsText(String property, String path, String uniqueValue) throws SAXException, IOException, ParserConfigurationException {
        //get object properties from the xml file repository
        String[] element = xmlParser(path, property);
        switch (element[0].toUpperCase()) {
            case "XPATH":
                String replaceString = element[1].replace("#uniqueValue#", uniqueValue);
                driver.findElement(By.xpath(replaceString)).click();
                break;
        }
    }

    public void CheckIfObjectTextExist(String property, String path, String strExpected, String strPass, ExtentTest logger) throws Exception {
        try {

            String strText = null;
            String[] element = xmlParser(path, property);
            switch (element[0].toUpperCase()) {
                case "XPATH":
                    strText = driver.findElement(By.xpath(element[1])).getText();
                    break;

                case "ID":
                    strText = driver.findElement(By.id(element[1])).getText();
                    break;

                case "NAME":
                    strText = driver.findElement(By.name(element[1])).getText();
                    break;

                case "LINKTEXT":
                    strText = driver.findElement(By.linkText(element[1])).getText();
                    break;

                case "CSSSELECTOR":
                    strText = driver.findElement(By.cssSelector(element[1])).getText();
                    break;

            }

            if (strText.contains(strExpected)) {
                logger.pass("<b>" + strPass + "</b><br/>" + strText + " Displayed Successfully");
            } else {
                logger.fail("<b>" + strPass + "</b><br/>" + strExpected + " Not Displayed");
            }
        } catch (Exception e) {
            logger.fail(e.getMessage());
        }

    }

    public void HoverAndClickObject(String property1, String property2, String path) throws SAXException, IOException, ParserConfigurationException {
        //get object properties from the xml file repository
        Actions action = new Actions(driver);
        String[] element1 = xmlParser(path, property1);
        String[] element2 = xmlParser(path, property2);
        switch (element1[0].toUpperCase()) {
            case "XPATH":
                driver.findElement(By.xpath(element1[1])).click();
                action.moveToElement(driver.findElement(By.xpath(element2[1]))).click().build().perform();
                break;

            case "ID":
                driver.findElement(By.id(element1[1])).click();
                break;

            case "NAME":
                driver.findElement(By.name(element1[1])).click();
                break;

            case "LINKTEXT":
                driver.findElement(By.linkText(element1[1])).click();
                break;

            case "CSSSELECTOR":
                driver.findElement(By.cssSelector(element1[1])).click();
                break;

        }


    }

    /*****************************************************************************
     Function Name: 	ClickObjectUsingAction
     Description:	click on the application using action builder using either xpath, ID, Name, linktext and CssSelector and maximum wait time
     Date Created:	13/09/2017
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     ******************************************************************************/
    public void ClickObjectUsingAction(String property, String path) throws SAXException, IOException, ParserConfigurationException {
        Actions action = new Actions(driver);
        //get object properties from the xml file repository
        String[] element = xmlParser(path, property);
        switch (element[0].toUpperCase()) {
            case "XPATH":
                action.moveToElement(driver.findElement(By.name(element[1]))).click().build().perform();
                break;

            case "ID":
                action.moveToElement(driver.findElement(By.id(element[1]))).click().build().perform();
                break;

            case "NAME":
                action.moveToElement(driver.findElement(By.name(element[1]))).click().build().perform();
                break;

            case "LINKTEXT":
                action.moveToElement(driver.findElement(By.linkText(element[1]))).click().build().perform();
                break;

            case "CSSSELECTOR":
                action.moveToElement(driver.findElement(By.cssSelector(element[1]))).click().build().perform();
                break;

        }

    }


    /*****************************************************************************
     Function Name: 	DoubleClickObjectUsingActionBuilder
     Description:	double click on the application using action builder using either xpath, ID, Name, linktext and CssSelector and maximum wait time
     Date Created:	13/09/2017
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     ******************************************************************************/
    public void DoubleClickObjectUsingActionBuilder(String property, String path) throws SAXException, IOException, ParserConfigurationException {

        Actions action = new Actions(driver);
        //get object properties from the xml file repository
        String[] element = xmlParser(path, property);
        switch (element[0].toUpperCase()) {
            case "XPATH":
                action.moveToElement(driver.findElement(By.xpath(element[1]))).doubleClick().build().perform();
                break;

            case "ID":
                action.moveToElement(driver.findElement(By.id(element[1]))).doubleClick().build().perform();
                break;

            case "NAME":
                action.moveToElement(driver.findElement(By.name(element[1]))).doubleClick().build().perform();
                break;

            case "LINKTEXT":
                action.moveToElement(driver.findElement(By.linkText(element[1]))).doubleClick().build().perform();
                break;

            case "CSSSELECTOR":
                action.moveToElement(driver.findElement(By.cssSelector(element[1]))).doubleClick().build().perform();
                break;

        }

    }


    /*****************************************************************************
     Function Name: 	EnterText
     Description:	Enter text to the application using either xpath, ID, Name, linktext and CssSelector and maximum wait time
     Date Created:	13/09/2017
     ******************************************************************************/
    public void EnterText(String property, String sText, String path) throws SAXException, IOException, ParserConfigurationException {
        //get object properties from the xml file repository
        String[] element = xmlParser(path, property);
        switch (element[0].toUpperCase()) {
            case "XPATH":
                driver.findElement(By.xpath(element[1])).sendKeys(sText);
                break;

            case "ID":
                driver.findElement(By.id(element[1])).sendKeys(sText);
                break;

            case "NAME":
                driver.findElement(By.name(element[1])).sendKeys(sText);
                break;

            case "LINKTEXT":
                driver.findElement(By.linkText(element[1])).sendKeys(sText);
                break;

            case "CSSSELECTOR":
                driver.findElement(By.cssSelector(element[1])).sendKeys(sText);
                break;
        }


    }


    //driver.switchTo().parentFrame();
    //Swtiching to another frame
    public void SwitchToiFrame(String property, String path, ExtentTest logger) throws SAXException, IOException, ParserConfigurationException, Exception {
        try {

            //get object properties from the xml file repository
            String[] element = xmlParser(path, property);
            switch (element[0].toUpperCase()) {
                case "XPATH":
                    WebElement xpElement = driver.findElement(By.xpath(element[1]));
                    driver.switchTo().frame(xpElement);
                    break;

                case "ID":
                    WebElement idElement = driver.findElement(By.id(element[1]));
                    driver.switchTo().frame(idElement);
                    break;

                case "NAME":
                    WebElement nameElement = driver.findElement(By.name(element[1]));
                    driver.switchTo().frame(nameElement);
                    break;

                case "LINKTEXT":
                    WebElement lnkElement = driver.findElement(By.linkText(element[1]));
                    driver.switchTo().frame(lnkElement);
                    break;

                case "CSSSELECTOR":
                    WebElement cssElement = driver.findElement(By.cssSelector(element[1]));
                    driver.switchTo().frame(cssElement);
                    break;
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            logger.fail(e.getMessage());
        }


    }


    /*****************************************************************************
     Function Name: 	SelectTextByUsingValue
     Description:	Select text using value from the dropdown using either xpath, ID, Name, linktext and CssSelector and maximum wait time
     Date Created:	13/09/2017
     ******************************************************************************/
    public void SelectTextUsingValue(String property, String sText, String path) throws SAXException, IOException, ParserConfigurationException {
        //get object properties from the xml file repository
        String[] element = xmlParser(path, property);
        Select oSelect = null;
        switch (element[0].toUpperCase()) {
            case "XPATH":
                oSelect = new Select(driver.findElement(By.xpath(element[1])));
                break;

            case "ID":
                oSelect = new Select(driver.findElement(By.id(element[1])));
                break;

            case "NAME":
                oSelect = new Select(driver.findElement(By.name(element[1])));
                break;

            case "LINKTEXT":
                oSelect = new Select(driver.findElement(By.linkText(element[1])));
                break;

            case "CSSSELECTOR":
                oSelect = new Select(driver.findElement(By.cssSelector(element[1])));
                break;

        }

        oSelect.selectByValue(sText);

    }


    /*****************************************************************************
     Function Name: 	SelectTextByUsingIndex
     Description:	Select text using an index from the dropdown using either xpath, ID, Name, linktext and CssSelector and maximum wait time
     Date Created:	13/09/2017
     ******************************************************************************/
    public void SelectTextUsingIndex(String property, int iIndex, String path) throws SAXException, IOException, ParserConfigurationException {
        //get object properties from the xml file repository
        String[] element = xmlParser(path, property);
        Select oSelect = null;
        switch (element[0].toUpperCase()) {
            case "XPATH":
                oSelect = new Select(driver.findElement(By.xpath(element[1])));
                break;

            case "ID":
                oSelect = new Select(driver.findElement(By.id(element[1])));
                break;

            case "NAME":
                oSelect = new Select(driver.findElement(By.name(element[1])));
                break;

            case "LINKTEXT":
                oSelect = new Select(driver.findElement(By.linkText(element[1])));
                break;

            case "CSSSELECTOR":
                oSelect = new Select(driver.findElement(By.cssSelector(element[1])));
                break;

        }

        oSelect.selectByIndex(iIndex);

    }


    public void highlightElement(String property, String path) throws SAXException, IOException, ParserConfigurationException, Exception {

        String[] element = xmlParser(path, property);
        switch (element[0].toUpperCase()) {
            case "XPATH":
                higFunc(getWebdriver().findElement(By.xpath(element[1])));
                break;

            case "ID":
                higFunc(getWebdriver().findElement(By.id(element[1])));
                break;

            case "NAME":
                higFunc(getWebdriver().findElement(By.name(element[1])));
                break;

            case "LINKTEXT":
                higFunc(getWebdriver().findElement(By.linkText(element[1])));
                break;

            case "CSSSELECTOR":
                higFunc(getWebdriver().findElement(By.cssSelector(element[1])));
                break;
        }
    }

    public void higFunc(WebElement element) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            JavascriptExecutor js = (JavascriptExecutor) getWebdriver();
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "color: blue; border: 6px solid blue;");
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
            Thread.sleep(200);
        }
    }


    /*****************************************************************************
     Function Name: 	SelectTextByUsingVisibeText
     Description:	Select text using a visible text from the dropdown using either xpath, ID, Name, linktext and CssSelector and maximum wait time
     Date Created:	13/09/2017
     ******************************************************************************/
    public void SelectTextUsingVisibeText(String property, String sText, String path,ExtentTest logger) throws SAXException, IOException, ParserConfigurationException {
        try{
            //get object properties from the xml file repository
            String[] element = xmlParser(path, property);
            Select oSelect = null;
            switch (element[0].toUpperCase()) {
                case "XPATH":
                    oSelect = new Select(driver.findElement(By.xpath(element[1])));
                    break;

                case "ID":
                    oSelect = new Select(driver.findElement(By.id(element[1])));
                    break;

                case "NAME":
                    oSelect = new Select(driver.findElement(By.name(element[1])));
                    break;

                case "LINKTEXT":
                    oSelect = new Select(driver.findElement(By.linkText(element[1])));
                    break;

                case "CSSSELECTOR":
                    oSelect = new Select(driver.findElement(By.cssSelector(element[1])));
                    break;

            }

            oSelect.selectByVisibleText(sText);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            logger.fail(e.getMessage());
        }

    }


    /*****************************************************************************
     Function Name: 	waitforProperty
     Description:	wait for the property to appear using either xpath, ID, Name, linktext and CssSelector and maximum wait time
     Date Created:	13/09/2017
     ******************************************************************************/
    public void waitforProperty(String property, int sWait, String path) throws SAXException, IOException, ParserConfigurationException {
        WebDriverWait wait = new WebDriverWait(driver, sWait);
        //get object properties from the xml file repository
        String[] element = xmlParser(path, property);
        switch (element[0].toUpperCase()) {
            case "XPATH":
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element[1])));
                break;

            case "ID":
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element[1])));
                break;

            case "NAME":
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(element[1])));
                break;

            case "LINKTEXT":
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(element[1])));
                break;

            case "CSSSELECTOR":
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(element[1])));
                break;

        }

    }

    public boolean checkElementExist(String property, String path,int waitTime) throws Exception {
        boolean isPresent = false;
        try {
            WaitForElementToBeClickable(property,path,waitTime);
            String[] element = xmlParser(path, property);

            switch (element[0].toUpperCase()) {
                case "XPATH":
                    if (getWebdriver().findElements(By.xpath(element[1])).size() != 0) {

                        isPresent = true;
                    } else {
                        isPresent = false;
                    }
                    break;

                case "ID":
                    if (getWebdriver().findElements(By.id(element[1])).size() != 0) {
                        isPresent = true;
                    } else {
                        isPresent = false;
                    }
                    break;

                case "NAME":
                    if (getWebdriver().findElements(By.name(element[1])).size() != 0) {
                        isPresent = true;
                    } else {
                        isPresent = false;
                    }
                    break;

                case "LINKTEXT":
                    if (getWebdriver().findElements(By.linkText(element[1])).size() != 0) {
                        isPresent = true;
                    } else {
                        isPresent = false;
                    }
                    break;

                case "CSSSELECTOR":
                    if (getWebdriver().findElements(By.cssSelector(element[1])).size() != 0) {
                        isPresent = true;
                    } else {
                        isPresent = false;
                    }
                    break;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            isPresent = false;
        }
        return isPresent;
    }

    //Close Windows Pop Up
    public void CloseWindowsPopUp() {
        for (int a = driver.getWindowHandles().size() - 1; a > 0; a--) {

            String winHandle = driver.getWindowHandles().toArray()[a].toString();

            driver.switchTo().window(winHandle);

            driver.close();
        }

    }

    //Generate Random String
    public String GenerateRandomString(int count) {
        return RandomStringUtils.randomAlphabetic(count).toLowerCase();

    }

    //Generate Random Integer
    public String GenerateRandomInt(int count) {
        return RandomStringUtils.randomNumeric(count);

    }

    //Generate Random Alpha Numeric
    public String GenerateAlphaNumeric(int count) {
        return RandomStringUtils.randomAlphanumeric(10);

    }


    /*****************************************************************************
     Function Name: 	GetText
     Description:	get text from the application using either xpath, ID, Name, linktext and CssSelector
     Date Created:	13/09/2017
     ******************************************************************************/
    public String GetText(String property, String path) throws
            SAXException, IOException, ParserConfigurationException {
        String strTextToReturn = null;
        //get object properties from the xml file repository
        String[] element = xmlParser(path, property);
        switch (element[0].toUpperCase()) {
            case "XPATH":
                strTextToReturn = driver.findElement(By.xpath(element[1])).getText();
                break;

            case "ID":
                strTextToReturn = driver.findElement(By.id(element[1])).getText();
                break;

            case "NAME":
                strTextToReturn = driver.findElement(By.name(element[1])).getText();
                break;

            case "LINKTEXT":
                strTextToReturn = driver.findElement(By.linkText(element[1])).getText();
                break;

            case "CSSSELECTOR":
                strTextToReturn = driver.findElement(By.cssSelector(element[1])).getText();
                break;

        }
        return strTextToReturn;

    }


    /*****************************************************************************
     Function Name: 	GetAttributeValue
     Description:	get an attribute value from the application using either xpath, ID, Name, linktext and CssSelector
     Date Created:	13/09/2017
     ******************************************************************************/
    public String GetAttributeValue(String property, String attribute, String path) throws
            SAXException, IOException, ParserConfigurationException {
        String strTextToReturn = null;
        //get object properties from the xml file repository
        String[] element = xmlParser(path, property);
        switch (element[0].toUpperCase()) {
            case "XPATH":
                strTextToReturn = driver.findElement(By.xpath(element[1])).getAttribute(attribute);
                break;

            case "ID":
                strTextToReturn = driver.findElement(By.id(element[1])).getAttribute(attribute);
                break;

            case "NAME":
                strTextToReturn = driver.findElement(By.name(element[1])).getAttribute(attribute);
                break;

            case "LINKTEXT":
                strTextToReturn = driver.findElement(By.linkText(element[1])).getAttribute(attribute);
                break;

            case "CSSSELECTOR":
                strTextToReturn = driver.findElement(By.cssSelector(element[1])).getAttribute(attribute);
                break;

        }
        return strTextToReturn;

    }


    /*****************************************************************************
     Function Name: 	ClearObject
     Description:	Clear object on the application using either xpath, ID, Name, linktext and CssSelector
     Date Created:	13/09/2017
     ******************************************************************************/
    public void ClearObject(String property, String path) throws
            SAXException, IOException, ParserConfigurationException {
        //get object properties from the xml file repository
        String[] element = xmlParser(path, property);
        switch (element[0].toUpperCase()) {
            case "XPATH":
                driver.findElement(By.name(element[1])).clear();
                break;

            case "ID":
                driver.findElement(By.id(element[1])).clear();
                break;

            case "NAME":
                driver.findElement(By.name(element[1])).clear();
                break;

            case "LINKTEXT":
                driver.findElement(By.linkText(element[1])).clear();
                break;

            case "CSSSELECTOR":
                driver.findElement(By.cssSelector(element[1])).clear();
                break;

        }


    }


    /*****************************************************************************
     Function Name: 	checkIfObjectExists
     Description:	Checks if an object exists using either an xpath, ID or a Name
     Date Created:	13/09/2017
     ******************************************************************************/

    public boolean checkIfObjectExists(String property, String path, ExtentTest logger) {
        boolean exists = false;
        try {
            //get object properties from the xml file repository
            String[] element = xmlParser(path, property);
            switch (element[0].toUpperCase()) {
                case "XPATH":
                    if ((driver.findElement(By.xpath(element[1])) != null) || (driver.findElements(By.xpath(element[1])).isEmpty())) {
                        exists = true;
                    } else {
                        exists = false;
                    }
                    break;

                case "ID":
                    if ((driver.findElement(By.id(element[1])) != null) || (driver.findElements(By.id(element[1])).isEmpty())) {
                        exists = true;
                    } else {
                        exists = false;
                    }
                    break;

                case "NAME":
                    if ((driver.findElement(By.name(element[1])) != null) || (driver.findElements(By.name(element[1])).isEmpty())) {
                        exists = true;
                    } else {
                        exists = false;
                    }
                    break;
                case "LINKTEXT":
                    if ((driver.findElement(By.linkText(element[1])) != null) || (driver.findElements(By.linkText(element[1])).isEmpty())) {
                        exists = true;
                    } else {
                        exists = false;
                    }
                    break;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            exists = false;
            //logger.info(e.getMessage());
        }
        return exists;


    }


    /*****************************************************************************
     Function Name: 	checkIfObjectIsDisplayed
     Description:	Checks if an object is displayed using either an xpath, ID or a Name
     Date Created:	13/09/2017
     ******************************************************************************/
    public boolean checkIfObjectIsDisplayed(String property, String path, ExtentTest logger) {
        boolean exists = false;
        try {
            //get object properties from the xml file repository
            String[] element = xmlParser(path, property);
            switch (element[0].toUpperCase()) {
                case "XPATH":
                    if (driver.findElement(By.xpath(element[1])).isDisplayed() == true) {
                        exists = true;
                    } else {
                        exists = false;
                    }
                    break;

                case "ID":
                    if (driver.findElement(By.id(element[1])).isDisplayed() == true) {
                        exists = true;
                    } else {
                        exists = false;
                    }
                    break;

                case "NAME":
                    if (driver.findElement(By.name(element[1])).isDisplayed() == true) {
                        exists = true;
                    } else {
                        exists = false;
                    }
                    break;
                case "LINKTEXT":
                    if (driver.findElement(By.linkText(element[1])).isDisplayed() == true) {
                        exists = true;
                    } else {
                        exists = false;
                    }
                    break;
            }
            WaitForElementToBeClickable(property,path,10);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            exists = false;
           // logger.info(e.getMessage());
        }
        return exists;

    }


    /*****************************************************************************
     Function Name: 	checkIfObjectEnabled
     Description:	Checks if an object is enabled using either an xpath, ID or a Name
     Date Created:	13/09/2017
     / @param sDefaultPath
     ******************************************************************************/
    public boolean checkIfObjectEnabled(String property, String path) {

        boolean exists = false;
        try {
            String[] element = xmlParser(path, property);
            switch (element[0].toUpperCase()) {
                case "XPATH":
                    if (driver.findElement(By.xpath(element[1])).isEnabled() == true) {
                        exists = true;
                    } else {
                        exists = false;
                    }
                    break;

                case "ID":
                    if (driver.findElement(By.id(element[1])).isEnabled() == true) {
                        exists = true;
                    } else {
                        exists = false;
                    }
                    break;

                case "NAME":
                    if (driver.findElement(By.name(element[1])).isEnabled() == true) {
                        exists = true;
                    } else {
                        exists = false;
                    }
                    break;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            exists = false;
        }
        return exists;


    }


    //xmlParser(String xmlPath, String tagName, String fieldName);

    /************************************************************end Selenium***************************************************************************************/

    /*****************************************************robot******************************************************************************************************/

    //Press Shift and Tab
    public void PressEnter(int iteration) throws InterruptedException, AWTException {
        int i = 1;
        while (i <= iteration) {
            Thread.sleep(1000);
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            i++;
        }
    }

    //Press Down Key on a page
    public void PressDownKey() throws InterruptedException, AWTException, AWTException {
        Thread.sleep(5000);
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_PAGE_DOWN);
        robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
    }

    //Press Down Key on a page
    public void PressUpKey() throws InterruptedException, AWTException {
        Thread.sleep(5000);
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_PAGE_UP);
        robot.keyRelease(KeyEvent.VK_PAGE_UP);
    }


    //Press Down Key on a page
    public void RefreshPage() throws InterruptedException, AWTException {
        Thread.sleep(5000);
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_F5);
        robot.keyRelease(KeyEvent.VK_F5);
    }

    //Press Shift and Tab
    public void PressShiftTab(int iteration) throws InterruptedException, AWTException {
        int i = 1;
        while (i <= iteration) {
            Thread.sleep(1000);
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_SHIFT);
            robot.keyRelease(KeyEvent.VK_TAB);
            i++;
        }
    }

    //Press Shift and Tab
    public void PressLeftArrow(int iteration) throws InterruptedException, AWTException {
        int i = 1;
        while (i <= iteration) {
            Thread.sleep(1000);
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_LEFT);
            robot.keyRelease(KeyEvent.VK_LEFT);
            i++;
        }
    }

    public void pressTAB() throws AWTException, InterruptedException {

        Robot r = new Robot();
        r.keyPress(KeyEvent.VK_TAB);
        r.keyRelease(KeyEvent.VK_TAB);
        Thread.sleep(1000);
    }

    public void pressF2() throws AWTException, InterruptedException {

        Robot r = new Robot();
        r.keyPress(KeyEvent.VK_F2);
        r.keyRelease(KeyEvent.VK_F2);
        Thread.sleep(1000);
    }

    public void pressA() throws AWTException, InterruptedException {

        Robot r = new Robot();
        r.keyPress(KeyEvent.VK_A);
        r.keyRelease(KeyEvent.VK_A);
        Thread.sleep(1000);
    }

    public void pressTAB(int iterations) throws AWTException, InterruptedException {

        int i = 1;
        while (i <= iterations) {
            Robot r = new Robot();
            r.keyPress(KeyEvent.VK_TAB);
            r.keyRelease(KeyEvent.VK_TAB);
            Thread.sleep(1000);
            i++;
        }
    }

    //Press Down Key on a page
    public void pressCtrlShiftA() throws InterruptedException, AWTException {
        Thread.sleep(5000);
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_SHIFT);
        robot.keyRelease(KeyEvent.VK_A);

    }

    /*****************************************************************end robot*************************************************************************************/

    public String[] xmlParser(String xmlPath, String tagName) throws
            SAXException, IOException, ParserConfigurationException {
        // File fXmlFile = new File();
        //InputStream requestContent = new InputStr();
        //String sLine = outputData;
        //requestContent.append(sLine);
        //String element = null;
        String[] element2 = new String[2];
        File fXmlFile = new File(xmlPath);
        DocumentBuilderFactory dbFactory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder =
                dbFactory.newDocumentBuilder();

        Document doc = dBuilder.parse(fXmlFile);
				
				/*InputSource is = new InputSource(new
				StringReader(outputData));
				org.w3c.dom.Document doc = dBuilder.parse(is);*/


        doc.getDocumentElement().normalize();

        //	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

        NodeList nList = doc.getElementsByTagName(tagName);


        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);

            //System.out.println("\nCurrent Element :" + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;

                String element = eElement.getElementsByTagName("identifier").item(0).getTextContent();
                String element1 = eElement.getElementsByTagName("Element").item(0).getTextContent();
                element2[0] = element;
                element2[1] = element1;


            } // end if
        } // end for loop

        return element2;
    } // end function

    public void VerifyElement(String property, String path, int intWait) throws Exception {
        String[] element = xmlParser(path, property);

        WebDriverWait wait = new WebDriverWait(driver, intWait);
        // WebElement WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element[1])));

        switch (element[0].toUpperCase()) {
            case "XPATH":
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element[1])));
                break;

            case "ID":
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element[1])));
                break;

            case "NAME":
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(element[1])));
                break;

            case "LINKTEXT":
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(element[1])));
                break;

            case "CSSSELECTOR":
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(element[1])));
                break;

        }
    }

    public void VerifyElementNotDisplayed(String property, String path, int intWait) throws Exception {
        String[] element = xmlParser(path, property);

        WebDriverWait wait = new WebDriverWait(driver, intWait);
        // WebElement WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element[1])));

        switch (element[0].toUpperCase()) {
            case "XPATH":
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(element[1])));
                break;

            case "ID":
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(element[1])));
                break;

            case "NAME":
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name(element[1])));
                break;

            case "LINKTEXT":
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.linkText(element[1])));
                break;

            case "CSSSELECTOR":
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(element[1])));
                break;

        }
    }

    public void VerifyWebElementDisplayed(WebElement Object, int intWait) throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, intWait);
        wait.until(ExpectedConditions.visibilityOf(Object));

    }

    public void VerifyWebElementNotDisplayed(WebElement Object, int intWait) throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, intWait);
        wait.until(ExpectedConditions.invisibilityOf(Object));

    }

    public void ClickWebElement(WebElement Object) throws SAXException, IOException, ParserConfigurationException {
        Object.click();
    }

    public void synchronisePage() {
        waitforPagetoLoad();
    }

    private void waitforPagetoLoad() {

        try {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        } catch (TimeoutException exp) {
            exp.printStackTrace();
        }
    }
    
    public String escapeMetaCharacters(String inputString){
        final String[] metaCharacters = {"\\","^","$","{","}","[","]","(",")",".","*","+","?","|","<",">","-","&","%"};

        for (int i = 0 ; i < metaCharacters.length ; i++){
            if(inputString.contains(metaCharacters[i])){
                inputString = inputString.replace(metaCharacters[i],"\\"+metaCharacters[i]);
            }
        }
        return inputString;
    }


}
