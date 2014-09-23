import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* Created by Maruf on 9/12/2014.
*/
public class GetPic {
    WebDriver driver; MultiPartEmail email;
    public static String screenshotsFolder, screenShotPath;
    List<String> screenShotNames;


    @BeforeMethod
    public void setUp() {
        driver = new FirefoxDriver();
        driver.get("http://www.peoplentech.com");
    }

//    @Test
    public void getPics() throws IOException, InterruptedException {
        screenShotNames = new ArrayList<>();

        createScreenshotsFolder();
        getScreenShot();
    }

    @AfterMethod
    public void tearDown() throws EmailException {
        driver.quit();
        email = SetUpEmail();
        createAndAddAttachments(screenShotNames);
        email.send();
    }

    private MultiPartEmail SetUpEmail() throws EmailException {
        MultiPartEmail email = new MultiPartEmail();
        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("yourmail@gmail.com", "yourpassword"));
        email.setSSL(true);
        email.setFrom("yourmail@gmail.com");
        email.setSubject("hey");
        email.setMsg("This is a test mail... ");
        email.addTo("reciever@gmail.com");
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

        // takes screenshot and stores as bytecode in the program
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        // creates a image file in you computer
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
        attachment.setPath("C:src\\test\\git commands.PNG");
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
