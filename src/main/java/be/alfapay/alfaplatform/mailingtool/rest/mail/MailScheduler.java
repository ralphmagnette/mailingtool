package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.MailSendTo;
import be.alfapay.alfaplatform.mailingtool.domain.Mailing;
import be.alfapay.alfaplatform.mailingtool.domain.Status;
import be.alfapay.alfaplatform.mailingtool.util.FileHelperUtil;
import be.alfapay.alfaplatform.mailingtool.util.MailSenderUtil;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Component
public class MailScheduler {
    @Autowired
    private IMailManager mailManager;

    @Autowired
    private FileHelperUtil fileHelperUtil;

    @Autowired
    private MailSenderUtil mailSenderUtil;

    @Scheduled(cron = "0 0/5 * * * *")
    public void processMailing() {
        System.out.println("Running scheduler");
        Long now = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Europe/Brussels")).toInstant().toEpochMilli();
        List<Mailing> mailings = mailManager.getMailingsByStatusAndSendDateAfter(Status.CREATED, now + 600000);
        for (Mailing mailing : mailings) {
            mailing.setStatus(Status.PROCESSING);
            mailManager.saveMailing(mailing);
            List<MailSendTo> mailsSendTo = mailManager.getAllMailsSendToByMailingId(mailing.getId());
            for (MailSendTo mailSendTo : mailsSendTo) {
                Personalization personalization = new Personalization();
                personalization.addTo(new Email(mailSendTo.getEmail()));
                personalization.addSubstitution("{MESSAGE}", mailSendTo.getFirstName() + " " + mailSendTo.getLastName());
                personalization.addCustomArg("mailing_id", mailSendTo.getMailingId());
                mailSenderUtil.sendMail("info@gift2give.org", mailSendTo.getEmail(), mailing.getSubject(),
                        fileHelperUtil.getContentFromHtmlTemplate(Paths.get(mailing.getTemplate())), personalization, mailSendTo.getAttachments());
            }
            mailing.setStatus(Status.FINISHED);
            mailManager.saveMailing(mailing);
        }
    }
}
