package ilab.generic.functions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;


public class Reporting {

    public static ExtentHtmlReporter htmlReporter;
    public static ExtentReports extent;
    public static ExtentTest extentTest;
    public static ExtentTest parentTest;
    public static ExtentTest childTest;
    public static ExtentTest childNodeTest;


    public ExtentTest setParent(String parentName) {
        parentTest = extent.createTest(parentName);
        return parentTest;
    }

    public ExtentTest setChild(String childName, ExtentTest parentName) {
        childTest = parentName.createNode(childName);
        return childTest;
    }

    public ExtentTest setChildNode(String childNodeName, ExtentTest parentName) {
        childNodeTest = parentName.createNode(childNodeName);
        return childNodeTest;
    }

    public void childLogPass(String sStepName, String sExpected, String sActual, Boolean Screenshot, ExtentTest logger, UtilityFunctions utils) throws Exception {
        if (Screenshot) {
            logger.pass("<b>Step Name : </b><br/>" + sStepName + "</b><br/><b>Expected : </b><br/>" + sExpected + "<br/><b>Actual Result :</b><br/>" + sActual, MediaEntityBuilder.createScreenCaptureFromPath(captureScreen(utils)).build());
        } else {
            logger.pass("<b>Step Name : </b><br/>" + sStepName + "</b><br/><b>Expected : </b><br/>" + sExpected + "<br/><b>Actual Result :</b><br/>" + sActual);
        }

    }

    public void childLogFail(String sStepName, String sExpected, String sActual, Boolean Screenshot, ExtentTest logger, UtilityFunctions utils) throws Exception {
        if (Screenshot) {
            logger.fail("<b>Step Name : </b><br/>" + sStepName + "</b><br/><b>Expected : </b><br/>" + sExpected + "<br/><b>Actual Result :</b><br/>" + sActual, MediaEntityBuilder.createScreenCaptureFromPath(captureScreen(utils)).build());
        } else {
            logger.fail("<b>Step Name : </b><br/>" + sStepName + "</b><br/><b>Expected : </b><br/>" + sExpected + "<br/><b>Actual Result :</b><br/>" + sActual);
        }
    }

    public void childLogError(String sError, ExtentTest logger) throws Exception {
        logger.error(sError);
    }

    public ExtentReports getExtent() {

        return extent;
    }

    public void setExtent(ExtentReports extentTest) {
        Reporting.extent = extentTest;
    }

    public ExtentTest getExtentTest() {
        return extentTest;
    }

    public void setExtentTest(ExtentTest extentTest) {

        Reporting.extentTest = extentTest;
    }


    public ExtentReports initializeExtentReports(String sReportName, String sAppend, UtilityFunctions utils) {

        if (sAppend.toUpperCase().equals("TRUE")) {
            htmlReporter = new ExtentHtmlReporter("report/" + sReportName + ".html");
            htmlReporter.setAppendExisting(true);

        } else {
            htmlReporter = new ExtentHtmlReporter("report/" + sReportName + utils.getCurrentTimeStamp() + ".html");

        }

        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setReportName("<img src='../logo/logo.png'");
        htmlReporter.config().setCSS(".report-name { padding-right: 10px; } .report-name > img { height: 80%;margin-left: 0px;margin-bottom: -10px;width: auto; }");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        return extent;

    }

    public void SendEmail(String sfrom, String sto, String sReportName) {
        // Recipient's email ID needs to be mentioned.
        String to = sto;

        // Sender's email ID needs to be mentioned
        String from = sfrom;

        final String username = "mbucwa@gmail.com";//change accordingly
        final String password = "Z1@qwenhliva";//change accordingly

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
     * Extent Reporting
     /* @param xmlpath
     * @throws Exception
     */

    public void ExtentLogPassDB(String sMessagePass, ExtentTest logger) throws Exception {
        logger.pass(sMessagePass);

    }

    public void ExtentLogInfoDB(String sMessagePass, ExtentTest logger) throws Exception {
        logger.info(sMessagePass);

    }

    public void ExtentLogPassFail(String sObject, String sStepName, String sExpected, String sMessagePass, String sMessageFail, Boolean Screenshot, String xmlpath, UtilityFunctions utils, ExtentTest test) throws Exception {
        //utils.waitforProperty(sObject, 30000, xmlpath);

        if (utils.checkIfObjectIsDisplayed(sObject, xmlpath,test)) {
            utils.highlightElement(sObject, xmlpath);
            ExtentLogPass("<b>Step Name : </b><br/>" + sStepName + "</b><br/><b> Expected :</b><br/>" + sExpected + "</b><br/><b> Actual Results</b><br/>" + sMessagePass, Screenshot, utils, test);

        } else {
            ExtentLogFail("<b>Step Name : </b><br/>" + sStepName + "</b><br/><b> Expected :</b><br/>" + sExpected + "</b><br/><b> Actual Results</b><br/>" + sMessageFail, Screenshot, utils, test);
        }

    }


    public void ExtentLogPass(String sMessagePass, Boolean Screenshot,  UtilityFunctions utils,ExtentTest test) throws Exception {
        if (Screenshot) {
       
			
            String fileName = takeScreenShot("ExtentLogPass", utils);
            test.pass(sMessagePass, MediaEntityBuilder.createScreenCaptureFromPath(fileName).build());
        } else {
            test.pass(sMessagePass);
        }
    }

    public void ExtentLogFail(String sMessageFail, Boolean Screenshot, UtilityFunctions utils, ExtentTest test) throws Exception {


        if (Screenshot) {
            String fileName = takeScreenShot("ExtentLogFail", utils);
            test.fail(sMessageFail, MediaEntityBuilder.createScreenCaptureFromPath(fileName).build());
        } else {
            test.fail(sMessageFail);
        }
    }

    public void ExtentLogInfo(String sMessageInfo, Boolean Screenshot, UtilityFunctions utils, ExtentTest test) throws Exception {


        if (Screenshot) {
            String fileName = takeScreenShot("ExtentLogFail", utils);
            test.info(sMessageInfo, MediaEntityBuilder.createScreenCaptureFromPath(fileName).build());
        } else {
            test.info(sMessageInfo);

        }
    }


    public String takeScreenShot(String FileName, UtilityFunctions utils) throws Exception {
        String fileName = "Empty";

        try {
            String sDefaultPath = System.getProperty("user.dir");
           // sDefaultPath = sDefaultPath.replace("batch", "");
            File scrFile = ((TakesScreenshot) utils.getWebdriver()).getScreenshotAs(OutputType.FILE);

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            fileName = sDefaultPath + "/screenshots/" + FileName + timeStamp + ".png";

            // try {
            FileUtils.copyFile(scrFile, new File(fileName));
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return fileName;
    }


    public String captureScreen(UtilityFunctions utils) throws Exception {
        String dest = null;

        try {
            String sDefaultPath = System.getProperty("user.dir");
            sDefaultPath = sDefaultPath.replace("batch", "");

            TakesScreenshot screen = (TakesScreenshot) utils.getWebdriver();
            File src = screen.getScreenshotAs(OutputType.FILE);

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            dest = sDefaultPath + "/screenshots/Discovery" + timeStamp + ".png";
            File target = new File(dest);
            FileUtils.copyFile(src, target);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return dest;
    }
    public void validationLogPass(String sMessageFail, ExtentTest logger) throws Exception {
        logger.pass(sMessageFail);
    }
}
