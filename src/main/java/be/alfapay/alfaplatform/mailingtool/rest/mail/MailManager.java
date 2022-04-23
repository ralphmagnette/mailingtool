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

            Mail mail = new Mail();
            mail.setMailingId(UUID.randomUUID());
            for (Mail data : receivers) {
                Personalization personalization = new Personalization();
                personalization.addTo(new Email(data.getSendTo().getEmail()));
                data.setMailingId(mail.getMailingId());
                data.getSendTo().setMailingId(mail.getMailingId());
                data.setDate(LocalDate.now());
                if (data.getSendDate() == null) {
                    data.setSendDate(data.getDate());
                }
                personalization.addCustomArg("mailing_id", mail.getMailingId().toString());
                personalization.addSubstitution("{MESSAGE}", "Beste " + data.getSendTo().getFirstName() + " " + data.getSendTo().getLastName());
                mail.setArticleId(data.getArticleId());
                mail.setSendTo(data.getSendTo());
                mail.setUserId(data.getUserId());
                mail.setSubject(data.getSubject());
                mail.setCsv(data.getCsv());
                mail.setTemplate(data.getTemplate());
                mail.setDate(data.getDate());
                mail.setSendDate(data.getSendDate());
                mailSenderUtil.sendMail("info@gift2give.org", mail.getSendTo().getEmail(), mail.getSubject(), mail.getTemplate(), personalization, mail.getSendTo().getAttachments());
                mailSendToRepository.save(mail.getSendTo());
            }
            mailRepository.save(mail);
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
