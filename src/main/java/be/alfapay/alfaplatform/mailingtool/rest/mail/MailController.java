package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.MailSendTo;
import be.alfapay.alfaplatform.mailingtool.domain.Mailing;
import be.alfapay.alfaplatform.mailingtool.domain.SendGridEvent;
import be.alfapay.alfaplatform.mailingtool.rest.mail.message.ResponseMessage;
import be.alfapay.alfaplatform.mailingtool.util.FileHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins="*", allowedHeaders="*")
@RestController
@RequestMapping("/alfaplatform-giftcard-next/mail/")
public class MailController {
    @Autowired
    private MailManager mailManager;

    @PostMapping("send")
    public ResponseEntity<ResponseMessage> processMailing(@RequestParam("csv") MultipartFile csv,
                                                          @RequestParam("template") MultipartFile template,
                                                          @RequestParam("articleId") Integer articleId,
                                                          @RequestParam("subject") String subject,
                                                          @RequestParam("sendDate")String sendDate) {
        String message = "";
        if (!FileHelperUtil.hasCSVFormat(csv)) {
            message = "Upload een csv file!";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        if (!FileHelperUtil.hasHTMLFormat(template)) {
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

    @GetMapping("mailing/data")
    public ResponseEntity<List<Mailing>> getAllMailingData() {
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

    @GetMapping("mailing/data/{id}")
    public ResponseEntity<Mailing> getMailingDataById(@PathVariable String id) {
        Mailing mailing = mailManager.getMailingById(id);
        if (mailing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(mailing);
    }

    @GetMapping("sendto/data")
    public ResponseEntity<List<MailSendTo>> getAllMailSendTo() {
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

    @GetMapping("sendto/data/csv")
    public ResponseEntity<Resource> getAllMailsSendToAndExportCSV() {
        try {
            String filename = "mailssendto.csv";
            InputStreamResource file = new InputStreamResource(mailManager.getAllMailsSendToAndExportCSV());
            return ResponseEntity.status(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.parseMediaType("application/csv"))
                    .body(file);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }

    @GetMapping("sendto/data/{id}")
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
