package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.CSVData;
import be.alfapay.alfaplatform.mailingtool.domain.Mail;
import be.alfapay.alfaplatform.mailingtool.util.CSVParserUtil;
import be.alfapay.alfaplatform.mailingtool.util.mail.MailSender;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MailManager implements IMailManager {
    @Autowired
    private CSVRepository csvRepository;

    @Autowired
    private MailRepository mailRepository;

    @Autowired
    private MailSender mailSender;

    @Override
    public void saveCSVData(MultipartFile file) {
        try {
            List<CSVData> data = CSVParserUtil.csvToData(file.getInputStream());
            csvRepository.saveAll(data);
        } catch (IOException e) {
            throw new RuntimeException("Kan data uit CSV-bestand niet opslaan: " + e.getMessage());
        }
    }

    @Override
    public List<CSVData> getAllCSVData() {
        return csvRepository.findAll();
    }

    @Override
    public CSVData getCSVDataById(Long id) {
        return csvRepository.findById(id).orElse(null);
    }

    @Override
    public void sendAndSaveMail(Mail mail) {
        String[] toEmails = mail.getTo().split(",(\s)");
        for (String email : toEmails) {
            CSVData data = csvRepository.findByEmail(email);
            Personalization personalization = new Personalization();
            personalization.addTo(new Email(email));
            mail.setCsvId(data.getId());
            mail.setSendDate(LocalDateTime.now());
            personalization.addCustomArg("mailing_id", UUID.randomUUID().toString());
            personalization.addSubstitution("{MESSAGE}", "Beste " + data.getFirstName() + " " + data.getLastName());
            mailSender.sendMail(mail.getFrom(), mail.getTo(), mail.getSubject(), mail.getPlainText(), mail.getHtmlText(), personalization, mail.getAttachments());
            mailRepository.save(mail);
        }
    }

    @Override
    public List<Mail> getAllMailData() {
        return mailRepository.findAll();
    }

    @Override
    public Mail getMailDataById(Long id) {
        return mailRepository.findById(id).orElse(null);
    }
}
