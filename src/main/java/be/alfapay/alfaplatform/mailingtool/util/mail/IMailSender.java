package be.alfapay.alfaplatform.mailingtool.util.mail;

import be.alfapay.alfaplatform.mailingtool.domain.Attachment;

import java.util.List;

public interface IMailSender {
    boolean sendMail(String from, String to, String subject, String msgText);
    boolean sendMail(String from, String to, String subject, String plainText, String htmlText);
    boolean sendMail(String from, String to, String subject, String plainText, String htmlText, List<Attachment> attachments);
}
