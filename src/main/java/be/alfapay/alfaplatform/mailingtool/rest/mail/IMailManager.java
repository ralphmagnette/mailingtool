package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.CSVData;
import be.alfapay.alfaplatform.mailingtool.domain.Mail;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IMailManager {
    void saveCSVData(MultipartFile file);
    List<CSVData> getAllCSVData();
    CSVData getCSVDataById(Long id);

    void sendAndSaveMail(Mail mail);
    List<Mail> getAllMailData();
    Mail getMailDataById(Long id);
}
