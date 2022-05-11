package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.MailSendTo;
import be.alfapay.alfaplatform.mailingtool.domain.Mailing;
import be.alfapay.alfaplatform.mailingtool.domain.SendGridEvent;
import be.alfapay.alfaplatform.mailingtool.resources.MailSendToDTO;
import be.alfapay.alfaplatform.mailingtool.rest.mail.message.ResponseMessage;
import be.alfapay.alfaplatform.mailingtool.util.FileHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin(origins="*", allowedHeaders="*")
@RestController
@RequestMapping("/alfaplatform-giftcard-next/mail")
public class MailController {
    @Autowired
    private IMailManager mailManager;

    @Autowired
    private FileHelperUtil fileHelperUtil;

    @PostMapping("/send")
    public ResponseEntity<List<MailSendToDTO>> processMailing(@RequestParam("csv") MultipartFile csv,
                                                              @RequestParam("template") MultipartFile template,
                                                              @RequestParam("articleId") Integer articleId,
                                                              @RequestParam("subject") String subject,
                                                              @RequestParam("sendDate")String sendDate) {
        if (!fileHelperUtil.hasCSVFormat(csv)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (!fileHelperUtil.hasHTMLFormat(template)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        try {
            List<MailSendToDTO> mailing = mailManager.processMailing(csv, template, articleId, subject, sendDate);
            return ResponseEntity.status(HttpStatus.OK).body(mailing);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
    }

    @GetMapping("/mailing")
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

    @GetMapping("/mailing/{id}")
    public ResponseEntity<Mailing> getMailingById(@PathVariable String id) {
        Mailing mailing = mailManager.getMailingById(id);
        if (mailing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(mailing);
    }

    @GetMapping("/sendto")
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

    @GetMapping("/sendto/{id}")
    public ResponseEntity<MailSendTo> getMailSendToById(@PathVariable Long id) {
        MailSendTo mail = mailManager.getMailSendToById(id);
        if (mail == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(mail);
    }

    @GetMapping(value = "/sendto/export/csv/{mailingId}", produces = "text/csv")
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

    @PostMapping(value = "/report/events")
    public ResponseEntity<ResponseMessage> processInboundSendGridEmails(@RequestBody List<SendGridEvent> events) {
        try {
            ResponseMessage message = new ResponseMessage("");
            for (SendGridEvent event : events) {
                if (event.getMailingId() != null) {
                    switch (event.getEventType()) {
                        case "open":
                            message = mailManager.setOpened(event.getMailingId(), event.getEmail());
                            break;
                        case "click":
                            message = mailManager.setClicked(event.getMailingId(), event.getEmail());
                            break;
                        case "dropped":
                            message = mailManager.setDropped(event.getMailingId(), event.getEmail());
                            break;
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }
}
