
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

import java.net.MalformedURLException;

public class EmailableClass {


//    @Test
    public void apache() throws EmailException, MalformedURLException {
        MultiPartEmail email = new MultiPartEmail();
        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("yourmail@gmail.com", "yourpassword"));
        email.setSSL(true);
        email.setFrom("yourmail@gmail.com");
        email.setSubject("hey");
        email.setMsg("Test results attached!");
        email.addTo("reciever@gmail.com");
        // Create the attachment
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath("C:\\Users\\src\\test\\git commands.PNG");
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription("picture.png");
        attachment.setName("picture.png");
        email.attach(attachment);
        email.send();
    }

}
