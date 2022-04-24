package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.MailSendTo;
import be.alfapay.alfaplatform.mailingtool.domain.Mailing;
import be.alfapay.alfaplatform.mailingtool.domain.SendGridEvent;
import be.alfapay.alfaplatform.mailingtool.rest.mail.message.ResponseMessage;
import be.alfapay.alfaplatform.mailingtool.util.FileParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins="*", allowedHeaders="*")
@RestController
@RequestMapping("/alfaplatform-giftcard-next/mail/")
public class MailController {
    private List<SendGridEvent> sgEvents = new ArrayList<>();

    @Autowired
    private MailManager mailManager;

    @PostMapping("send")
    public ResponseEntity<ResponseMessage> processMailing(@RequestParam("file") MultipartFile file) {
        String message = "";
        if (!FileParserUtil.hasCSVFormat(file)) {
            message = "Upload een csv file!";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        try {
            mailManager.processMailing(file);
            message = "File: " + file.getOriginalFilename() + " is geupload en mail is verzonden.";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Kan de file: " + file.getOriginalFilename() + " niet uploaden!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
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
    public ResponseEntity<Mailing> getMailingDataById(@PathVariable UUID id) {
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

    @GetMapping("sendto/data/{id}")
    public ResponseEntity<MailSendTo> getMailSendToById(@PathVariable Long id) {
        MailSendTo mail = mailManager.getMailSendToById(id);
        if (mail == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(mail);
    }

    @PostMapping(value = "report/events")
    public ResponseEntity<String> processInboundSendGridEmails(@RequestBody List<SendGridEvent> events) {
        try {
            sgEvents.addAll(events);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }

    @GetMapping("report/events")
    public ResponseEntity<List<SendGridEvent>> sgEvents() {
        try {
            if (sgEvents.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }
}
