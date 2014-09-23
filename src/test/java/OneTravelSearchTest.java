import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertTrue;


/**
* Created by Maruf on 9/12/2014.
*/
public class OneTravelSearchTest {

    WebDriver driver; MultiPartEmail email;
    public static String screenshotsFolder, screenShotPath;
    List<String> screenShotNames;
    static Logger logger;

    @BeforeMethod
    public void setup() {
        screenShotNames = new ArrayList<>();
        screenShotNames.add("C:\\logs\\applog.html");
        logger = Logger.getLogger(OneTravelSearchTest.class);
        logger.info("------ SETTING UP TEST -------");
        driver = new FirefoxDriver();
        logger.info("created driver with" + driver.getClass().toString().substring(driver.getClass().toString().lastIndexOf(".") + 1));
        createScreenshotsFolder();
        logger.info("Screensoht folder created");
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        logger.info("set implicit wait for 5 seconds");
        driver.get("http://www.onetravel.com");
        logger.info("opened onetravel.com");
        logger.info("TEST SET UP COMPLETE");
    }

//    @Test
    public void test() throws IOException {
        try {
            logger.info("------ BEGIN TEST -------");
            driver.findElement(By.xpath("//*[@id='rbOW']")).click();
            logger.info("clicked on way link");
            driver.findElement(By.xpath("//*[@id='tbFrom']")).clear();
            logger.info("cleared FROM field");
            driver.findElement(By.xpath("//*[@id='tbFrom']")).sendKeys("WAS");
            logger.info("entered 'WAS' to FROM field");
            driver.findElement(By.xpath("//*[@id='tbTo']")).clear();
            logger.info("cleared TO field");
            driver.findElement(By.xpath("//*[@id='tbTo']")).sendKeys("JFK");
            logger.info("entered 'JFK' to TO field");
            driver.findElement(By.xpath("//*[@id='tbDTime']")).sendKeys("12/12/14");
            logger.info("entered '12/12/14' to DEPART field");


            try {
                Select select = new Select(driver.findElement(By.xpath("//*[@id='depTime']")));
                logger.info("created 'Choose Time' dropdown menu");
                select.selectByVisibleText("Any time");
                logger.info("selected 'Any time' from dropdown");
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
                getScreenShot();
                logger.info("screenshot captured");
            }


            driver.findElement(By.xpath("//*[@class='searchbtn2']")).click();
            logger.info("clicked on search button");


            try {
                assertTrue(driver.findElement(By.xpath("//*[@id='spnlistingPgSearch']")).isDisplayed());
                logger.info("verified search results");
            } catch (Exception e) {
                logger.error("verify search results failed \n" + e.getLocalizedMessage());
                getScreenShot();
                logger.info("screenshot captured");
            }
            logger.info("TEST COMPLETE");
        } catch (WebDriverException e) {
            logger.fatal("wedriver crashed");
        }


    }

    @AfterMethod
    public void teardown() throws EmailException {
        driver.quit();
        logger.info("quit driver");
        email= createAndSendEmailWithAttachments();
        createAndAddAttachments(screenShotNames);
        email.send();
        logger.info("email with attachments sent");
    }

    private MultiPartEmail createAndSendEmailWithAttachments() throws EmailException {
        MultiPartEmail email = new MultiPartEmail();
        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("yourmail@gmail.com", "yourpassword"));
        email.setSSL(true);
        email.setFrom("yourmail@gmail.com");
        email.setSubject("hey");
        email.setMsg("deal with  ");
        email.addTo("receiver@gmail.com");
        email.addCc("receiver2@gmail.com ");
        return email;
    }

    private void createAndAddAttachments(List<String> screenShotNames) throws EmailException {
        for (String screenShotName : screenShotNames) {
            EmailAttachment attachment = new EmailAttachment();
            attachment.setPath(screenShotName);
            attachment.setDisposition(EmailAttachment.ATTACHMENT);
            attachment.setDescription("screenshot.png");
            attachment.setName(screenShotName.substring(screenShotName.lastIndexOf("\\")));
            email.attach(attachment);
        }
    }

    private void getScreenShot() throws IOException {
        // create file name suing date time
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd   HH.mm.ss");
        Date date = new Date();
        String dateTime= dateFormat.format(date);
        // create name for the screen shot file
        screenShotPath=screenshotsFolder+"\\"+dateTime+".png";
        // capture screenshot
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File(screenShotPath));
        // add screenshot to list of screenshots
        screenShotNames.add(screenShotPath);
    }


    private static void createScreenshotsFolder() {
        String projectFolder = System.getProperty("user.dir");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd   HH.mm");
        Date date = new Date();
        String dateTime= dateFormat.format(date);
        String screenshotFolderPath= projectFolder+"\\target\\"+dateTime;
        screenshotsFolder=screenshotFolderPath;
        new File(screenshotFolderPath).mkdirs();
        createScreenshotAttachment();
    }

    private static void createScreenshotAttachment() {
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath("C:\\test\\git commands.PNG");
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription("picture.png");
        attachment.setName("picture.png");
    }

    private static String getdateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd   HH.mm.ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
