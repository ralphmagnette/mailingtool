package be.alfapay.alfaplatform.mailingtool.util;

import be.alfapay.alfaplatform.mailingtool.domain.Attachment;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Component
public class MailSenderUtil {
    private static final String SENDGRID_APIKEY = "";

    public MailSenderUtil() {

    }

    public boolean sendMail(String fromString, String toString, String subject, String template,
                            Personalization personalization, List<Attachment> mailAttachments) {
        if (toString == null || toString.isEmpty()) {
            return false;
        }
        boolean mailSent = true;
        Email from = new Email(fromString);
        Content content = new Content();
        content.setType("text/html");
        content.setValue(template);

        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setSubject(subject);
        mail.addContent(content);
        mail.addPersonalization(personalization);

        // Add attachment if any
        if (mailAttachments != null) {
            for (Attachment attachment : mailAttachments) {
                Attachments attachments = new Attachments();
                attachments.setFilename(attachment.getName());
                attachments.setType(attachment.getMimeType());
                attachments.setDisposition("attachment");

                String attachmentContent = Base64.getEncoder().encodeToString(attachment.getData());
                attachments.setContent(attachmentContent);
                mail.addAttachments(attachments);
            }
        }

        SendGrid sg = new SendGrid(SENDGRID_APIKEY);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
        } catch (IOException ioe) {
            mailSent = false;
            System.out.println(ioe.getMessage());
        }
        return mailSent;
    }
}
