package be.alfapay.alfaplatform.mailingtool.util;

import be.alfapay.alfaplatform.mailingtool.domain.Attachment;
import be.alfapay.alfaplatform.mailingtool.domain.MailSendTo;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;

@Component
public class MailSenderUtil {
    private static final String SENDGRID_APIKEY = "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";

    public MailSenderUtil() {

    }

    public boolean sendMail(String fromString, String toString, String subject, String template,
                            Personalization personalization, List<Attachment> mailAttachments) {
        if (toString == null || toString.isEmpty()) {
            return false;
        }
        boolean mailSent = true;
        Email from = new Email(fromString);
        Path htmlTemplate = Path.of(template);
        Content content = new Content();
        content.setType("text/html");
        content.setValue(getContentFromHtmlTemplate(htmlTemplate));

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

    private String getContentFromHtmlTemplate(Path htmlTemplate) {
        try (BufferedReader reader = Files.newBufferedReader(htmlTemplate)) {
            String line;
            StringBuilder template = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                template.append(line);
            }
            return template.toString();
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse csv file: " + e.getMessage());
        }
    }
}
