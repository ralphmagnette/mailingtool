package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.MailSendTo;
import be.alfapay.alfaplatform.mailingtool.domain.Mailing;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IMailManager {
    void processMailing(MultipartFile file);
    List<Mailing> getAllMailings();
    Mailing getMailingById(UUID id);
    List<MailSendTo> getAllMailsSendTo();
    MailSendTo getMailSendToById(Long id);
}
