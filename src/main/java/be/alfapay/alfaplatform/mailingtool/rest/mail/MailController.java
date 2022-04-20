package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.CSVData;
import be.alfapay.alfaplatform.mailingtool.domain.Mail;
import be.alfapay.alfaplatform.mailingtool.domain.SendGridEvent;
import be.alfapay.alfaplatform.mailingtool.rest.mail.exceptions.NotFoundException;
import be.alfapay.alfaplatform.mailingtool.rest.mail.message.ResponseMessage;
import be.alfapay.alfaplatform.mailingtool.util.CSVParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins="*", allowedHeaders="*")
@RestController
@RequestMapping("/alfaplatform-giftcard-next/mail/")
public class MailController {
    private List<SendGridEvent> sgEvents = new ArrayList<>();

    @Autowired
    private MailManager mailManager;

    @PostMapping("csv/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        if (!CSVParserUtil.hasCSVFormat(file)) {
            message = "Upload een csv file!";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        try {
            mailManager.saveCSVData(file);
            message = "File: " + file.getOriginalFilename() + " is geupload.";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Kan de file: " + file.getOriginalFilename() + " niet uploaden!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("csv/data")
    public ResponseEntity<List<CSVData>> getAllCSVData() {
        try {
            List<CSVData> data = mailManager.getAllCSVData();
            if (data.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }

    @GetMapping("csv/data/{id}")
    public ResponseEntity<CSVData> getCSVDataById(@PathVariable Long id) {
        CSVData data = mailManager.getCSVDataById(id);
        if (data == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(data);
    }

    @PostMapping("send")
    public ResponseEntity<ResponseMessage> sendMail(@RequestBody Mail mail) {
        try {
            mailManager.sendAndSaveMail(mail);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Email is verstuurd."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
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
    public ResponseEntity<Mail> getMailDataById(@PathVariable Long id) {
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
