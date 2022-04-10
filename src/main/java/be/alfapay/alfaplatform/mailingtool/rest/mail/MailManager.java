package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.Mail;
import be.alfapay.alfaplatform.mailingtool.util.mail.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailManager implements IMailManager {
    @Autowired
    private MailSender mailSender;

    @Override
    public void sendMail(Mail mail) {
        mailSender.sendMail(mail.getFrom(), mail.getTo(), mail.getSubject(), mail.getPlainText(), mail.getHtmlText(), mail.getAttachments());
    }
}
