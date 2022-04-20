package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.CSVData;
import be.alfapay.alfaplatform.mailingtool.domain.Mail;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IMailManager {
    void save(MultipartFile file);
    List<CSVData> getAll();
    CSVData getById(Long id);
    void sendMail(Mail mail);
}
