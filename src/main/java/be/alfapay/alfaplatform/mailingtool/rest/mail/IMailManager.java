package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.Mail;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IMailManager {
    void uploadFileAndSendMail(MultipartFile file);
    List<Mail> getAllMailData();
    Mail getMailDataById(UUID id);
}
