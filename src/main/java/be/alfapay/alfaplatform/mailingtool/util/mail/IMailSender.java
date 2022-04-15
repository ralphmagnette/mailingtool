package be.alfapay.alfaplatform.mailingtool.util.mail;

import be.alfapay.alfaplatform.mailingtool.domain.Attachment;
import com.sendgrid.helpers.mail.objects.Personalization;

import java.util.List;

public interface IMailSender {
    boolean sendMail(String from, String to, String subject, String msgText, Personalization personalization);
    boolean sendMail(String from, String to, String subject, String plainText, String htmlText, Personalization personalization);
    boolean sendMail(String from, String to, String subject, String plainText, String htmlText, Personalization personalization, List<Attachment> attachments);
}
