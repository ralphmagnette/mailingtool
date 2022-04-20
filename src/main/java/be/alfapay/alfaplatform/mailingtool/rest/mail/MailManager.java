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
    public void save(MultipartFile file) {
        try {
            List<CSVData> data = CSVParserUtil.csvToData(file.getInputStream());
            csvRepository.saveAll(data);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    @Override
    public List<CSVData> getAll() {
        return csvRepository.findAll();
    }

    @Override
    public CSVData getById(Long id) {
        return csvRepository.findById(id).orElse(null);
    }

    @Override
    public void sendMail(Mail mail) {
        String[] toEmails = mail.getTo().split(",(\s)");
        for (String email : toEmails) {
            CSVData data = csvRepository.findByEmail(email);
            Personalization personalization = new Personalization();
            personalization.addTo(new Email(email));
            personalization.addCustomArg("mailing_id", UUID.randomUUID().toString());
            personalization.addSubstitution("{MESSAGE}", "Beste " + data.getFirstName() + " " + data.getLastName());
            mailSender.sendMail(mail.getFrom(), mail.getTo(), mail.getSubject(), mail.getPlainText(), mail.getHtmlText(), personalization, mail.getAttachments());
        }
    }
}
