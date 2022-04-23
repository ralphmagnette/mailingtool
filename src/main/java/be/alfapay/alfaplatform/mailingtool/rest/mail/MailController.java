package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.Mail;
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
    public ResponseEntity<ResponseMessage> uploadFileAndSendMail(@RequestParam("file") MultipartFile file) {
        String message = "";
        if (!FileParserUtil.hasCSVFormat(file)) {
            message = "Upload een csv file!";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        try {
            mailManager.uploadFileAndSendMail(file);
            message = "File: " + file.getOriginalFilename() + " is geupload en mail is verzonden.";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Kan de file: " + file.getOriginalFilename() + " niet uploaden!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("data")
    public ResponseEntity<List<Mail>> getAllMailData() {
        try {
            List<Mail> sendMails = mailManager.getAllMailData();
            if (sendMails.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(sendMails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }

    @GetMapping("data/{id}")
    public ResponseEntity<Mail> getMailDataById(@PathVariable UUID id) {
        Mail data = mailManager.getMailDataById(id);
        if (data == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(data);
    }

    @PostMapping(value = "report/events")
    public ResponseEntity<String> processInboundSendGridEmails(@RequestBody List<SendGridEvent> events) {
        try {
            sgEvents.addAll(events);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping("report/events")
    public ResponseEntity<List<SendGridEvent>> sgEvents() {
        try {
            if (sgEvents.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(sgEvents, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
