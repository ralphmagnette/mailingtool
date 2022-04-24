package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.Mail;
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
    private MailRepository mailRepository;

    @Autowired
    private MailSendToRepository mailSendToRepository;

    @Autowired
    private MailSenderUtil mailSenderUtil;

    public void uploadFileAndSendMail(MultipartFile file) {
        try {
            List<Mail> receivers = FileParserUtil.readDataOutOfFile(file.getInputStream());
            for (Mail mail : receivers) {
                Personalization personalization = new Personalization();
                personalization.addTo(new Email(mail.getSendTo().getEmail()));
                mail.setDate(LocalDate.now());
                if (mail.getSendDate() == null) {
                    mail.setSendDate(mail.getDate());
                }
                personalization.addCustomArg("mailing_id", mail.getId().toString());
                personalization.addSubstitution("{MESSAGE}", "Beste " + mail.getSendTo().getFirstName() + " " + mail.getSendTo().getLastName());
                mailSenderUtil.sendMail("info@gift2give.org", mail.getSendTo().getEmail(), mail.getSubject(), mail.getTemplate(), personalization, mail.getSendTo().getAttachments());
                mailRepository.save(mail);
                mailSendToRepository.save(mail.getSendTo());
            }
        } catch (IOException e) {
            throw new RuntimeException("Kan data uit CSV-bestand niet opslaan: " + e.getMessage());
        }
    }

    @Override
    public List<Mail> getAllMailData() {
        return mailRepository.findAll();
    }

    @Override
    public Mail getMailDataById(UUID id) {
        return mailRepository.findById(id).orElse(null);
    }
}
