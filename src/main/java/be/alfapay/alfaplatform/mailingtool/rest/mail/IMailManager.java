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
    void setOpenedForMailing(Mailing mailing);
    void setClickedLinkInMailing(Mailing mailing);
    void setDroppedForMailing(Mailing mailing);

    List<MailSendTo> getAllMailsSendTo();
    MailSendTo getMailSendToById(Long id);
    MailSendTo getMailSendToByMailingIdAndEmail(UUID mailingId, String email);
    void setOpenedForMail(MailSendTo mail);
    void setClickedLinkInMail(MailSendTo mail);
    void setDroppedForMail(MailSendTo mail);
}
