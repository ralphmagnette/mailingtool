package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.MailSendTo;
import be.alfapay.alfaplatform.mailingtool.domain.Mailing;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IMailManager {
    void processMailing(MultipartFile csv, MultipartFile template, Integer articleId, String sendDate);

    List<Mailing> getAllMailings();
    Mailing getMailingById(String id);
    void setOpenedForMailing(Mailing mailing);
    void setClickedLinkInMailing(Mailing mailing);
    void setDroppedForMailing(Mailing mailing);

    List<MailSendTo> getAllMailsSendTo();
    ByteArrayInputStream getAllMailsSendToAndExportCSV() throws IOException;
    MailSendTo getMailSendToById(Long id);
    MailSendTo getMailSendToByMailingIdAndEmail(String mailingId, String email);
    void setOpenedForMail(MailSendTo mail);
    void setClickedLinkInMail(MailSendTo mail);
    void setDroppedForMail(MailSendTo mail);
}
