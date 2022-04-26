package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.MailSendTo;
import be.alfapay.alfaplatform.mailingtool.domain.Mailing;
import be.alfapay.alfaplatform.mailingtool.util.FileParserUtil;
import be.alfapay.alfaplatform.mailingtool.util.MailSenderUtil;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class MailManager implements IMailManager {
    @Autowired
    private MailingRepository mailingRepository;

    @Autowired
    private MailSendToRepository mailSendToRepository;

    @Autowired
    private MailSenderUtil mailSenderUtil;

    @Override
    public void processMailing(MultipartFile file) {
        try {
            List<MailSendTo> receivers = FileParserUtil.readDataOutOfFile(file.getInputStream());
            Mailing mailing = new Mailing();
            mailing.setId(UUID.randomUUID());
            for (MailSendTo mail : receivers) {
                Personalization personalization = new Personalization();
                personalization.addTo(new Email(mail.getEmail()));
                mail.setMailingId(mailing.getId());
                mail.getMailing().setId(mailing.getId());
                mail.getMailing().setDate(LocalDate.now());
                if (mail.getMailing().getSendDate() == null) {
                    mail.getMailing().setSendDate(mail.getMailing().getDate());
                }
                personalization.addSubstitution("{MESSAGE}", "Beste " + mail.getFirstName() + " " + mail.getLastName());
                personalization.addCustomArg("mailing_id", mail.getMailingId().toString());
                mailSenderUtil.sendMail("info@gift2give.org", mail.getEmail(), mail.getMailing().getSubject(), mail.getMailing().getTemplate(), personalization, mail.getAttachments());
                mailing = mail.getMailing();
                mailSendToRepository.save(mail);
            }
            mailingRepository.save(mailing);
        } catch (IOException e) {
            throw new RuntimeException("Kan data uit CSV-bestand niet opslaan: " + e.getMessage());
        }
    }

    @Override
    public List<Mailing> getAllMailings() {
        return mailingRepository.findAll();
    }

    @Override
    public Mailing getMailingById(UUID id) {
        return mailingRepository.findById(id).orElse(null);
    }

    @Override
    public void setOpenedForMailing(Mailing mailing) {
        mailing.setOpen(mailing.getOpen() + 1);
        mailingRepository.save(mailing);
    }

    @Override
    public void setClickedLinkInMailing(Mailing mailing) {
        mailing.setClick(mailing.getClick() + 1);
        mailingRepository.save(mailing);
    }

    @Override
    public void setDroppedForMailing(Mailing mailing) {
        mailing.setDropped(mailing.getDropped() + 1);
        mailingRepository.save(mailing);
    }

    @Override
    public List<MailSendTo> getAllMailsSendTo() {
        return mailSendToRepository.findAll();
    }

    @Override
    public MailSendTo getMailSendToById(Long id) {
        return mailSendToRepository.findById(id).orElse(null);
    }

    @Override
    public MailSendTo getMailSendToByMailingIdAndEmail(UUID mailingId, String email) {
        return mailSendToRepository.getMailSendToByMailingIdAndEmail(mailingId, email);
    }

    @Override
    public void setOpenedForMail(MailSendTo mail) {
        mail.setOpen(mail.getOpen() + 1);
        mailSendToRepository.save(mail);
    }

    @Override
    public void setClickedLinkInMail(MailSendTo mail) {
        mail.setClick(mail.getClick() + 1);
        mailSendToRepository.save(mail);
    }

    @Override
    public void setDroppedForMail(MailSendTo mail) {
        mail.setDropped(mail.getDropped() + 1);
        mailSendToRepository.save(mail);
    }
}
