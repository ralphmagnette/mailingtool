package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.CSVData;
import be.alfapay.alfaplatform.mailingtool.domain.Mail;
import be.alfapay.alfaplatform.mailingtool.util.CSVParserUtil;
import be.alfapay.alfaplatform.mailingtool.util.mail.MailSender;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class MailManager implements IMailManager {
    @Autowired
    private MailRepository repository;

    @Autowired
    private MailSender mailSender;

    @Override
    public void save(MultipartFile file) {
        try {
            List<CSVData> data = CSVParserUtil.csvToData(file.getInputStream());
            repository.saveAll(data);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    @Override
    public void sendMail(Mail mail) {
        mailSender.sendMail(mail.getFrom(), mail.getTo(), mail.getSubject(), mail.getPlainText(), mail.getHtmlText(), mail.getPersonalization(), mail.getAttachments());
    }

    @Override
    public List<CSVData> getAll() {
        return repository.findAll();
    }

    @Override
    public CSVData getById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
