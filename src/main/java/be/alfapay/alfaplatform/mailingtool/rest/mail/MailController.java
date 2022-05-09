package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.MailSendTo;
import be.alfapay.alfaplatform.mailingtool.domain.Mailing;
import be.alfapay.alfaplatform.mailingtool.domain.SendGridEvent;
import be.alfapay.alfaplatform.mailingtool.rest.mail.message.ResponseMessage;
import be.alfapay.alfaplatform.mailingtool.util.FileHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin(origins="*", allowedHeaders="*")
@RestController
@RequestMapping("/alfaplatform-giftcard-next/mail/")
public class MailController {
    @Autowired
    private IMailManager mailManager;

    @Autowired
    private FileHelperUtil fileHelperUtil;

    @PostMapping("send")
    public ResponseEntity<ResponseMessage> processMailing(@RequestParam("csv") MultipartFile csv,
                                                          @RequestParam("template") MultipartFile template,
                                                          @RequestParam("articleId") Integer articleId,
                                                          @RequestParam("subject") String subject,
                                                          @RequestParam("sendDate")String sendDate) {
        String message = "";
        if (!fileHelperUtil.hasCSVFormat(csv)) {
            message = "Upload een csv file!";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        if (!fileHelperUtil.hasHTMLFormat(template)) {
            message = "Upload een html file!";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        try {
            mailManager.processMailing(csv, template, articleId, subject, sendDate);
            message = "Mail is verzonden.";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
    }

    @GetMapping("mailing")
    public ResponseEntity<List<Mailing>> getAllMailings() {
        try {
            List<Mailing> mailings = mailManager.getAllMailings();
            if (mailings.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(mailings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }

    @GetMapping("mailing/{id}")
    public ResponseEntity<Mailing> getMailingById(@PathVariable String id) {
        Mailing mailing = mailManager.getMailingById(id);
        if (mailing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(mailing);
    }

    @GetMapping("sendto")
    public ResponseEntity<List<MailSendTo>> getAllMailsSendTo() {
        try {
            List<MailSendTo> mails = mailManager.getAllMailsSendTo();
            if (mails.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(mails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }

    @GetMapping(value = "sendto/export/csv/{mailingId}", produces = "text/csv")
    public ResponseEntity<String> getAllMailsSendToByMailingIdAndExportCSV(HttpServletResponse response,
                                                                           @PathVariable String mailingId) {
        try {
            String mails = mailManager.getAllMailsSendToByMailingIdAndExportCSV(mailingId);
            String mailingFilename = "mailing-" + mailingId;
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + mailingFilename + ".csv\"");
            return ResponseEntity.status(HttpStatus.OK).body(mails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }

    @GetMapping("sendto/{id}")
    public ResponseEntity<MailSendTo> getMailSendToById(@PathVariable Long id) {
        MailSendTo mail = mailManager.getMailSendToById(id);
        if (mail == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(mail);
    }

    @PostMapping(value = "report/events")
    public ResponseEntity<ResponseMessage> processInboundSendGridEmails(@RequestBody List<SendGridEvent> events) {
        try {
            MailSendTo mail;
            for (SendGridEvent event : events) {
                if (event.getMailingId() != null) {
                    switch (event.getEventType()) {
                        case "open":
                            mail = mailManager.getMailSendToByMailingIdAndEmail(event.getMailingId(), event.getEmail());
                            if (mail == null) {
                                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                            }
                            mailManager.setOpenedForMail(mail);
                            if (mail.getOpen() == 1) {
                                mailManager.setOpenedForMailing(mail.getMailing());
                            }
                            break;
                        case "click":
                            mail = mailManager.getMailSendToByMailingIdAndEmail(event.getMailingId(), event.getEmail());
                            if (mail == null) {
                                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                            }
                            mailManager.setClickedLinkInMail(mail);
                            if (mail.getClick() == 1) {
                                mailManager.setClickedLinkInMailing(mail.getMailing());
                            }
                            break;
                        case "dropped":
                            mail = mailManager.getMailSendToByMailingIdAndEmail(event.getMailingId(), event.getEmail());
                            if (mail == null) {
                                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                            }
                            mailManager.setDroppedForMail(mail);
                            if (mail.getDropped() == 1) {
                                mailManager.setDroppedForMailing(mail.getMailing());
                            }
                            break;
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }
}
