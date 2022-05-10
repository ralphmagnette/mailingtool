package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.MailSendTo;
import be.alfapay.alfaplatform.mailingtool.resources.MailSendToDTO;
import be.alfapay.alfaplatform.mailingtool.domain.Mailing;
import be.alfapay.alfaplatform.mailingtool.rest.mail.message.ResponseMessage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IMailManager {
    List<MailSendToDTO> processMailing(MultipartFile csv, MultipartFile template, Integer articleId, String subject, String sendDate);

    List<Mailing> getAllMailings();
    Mailing getMailingById(String id);

    List<MailSendTo> getAllMailsSendTo();
    MailSendTo getMailSendToById(Long id);
    MailSendTo getMailSendToByMailingIdAndEmail(String mailingId, String email);
    String getAllMailsSendToByMailingIdAndExportCSV(String mailingId) throws IOException;

    ResponseMessage setOpened(String mailingId, String email);
    ResponseMessage setClicked(String mailingId, String email);
    ResponseMessage setDropped(String mailingId, String email);
}
