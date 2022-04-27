package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.MailSendTo;
import be.alfapay.alfaplatform.mailingtool.domain.Mailing;
import be.alfapay.alfaplatform.mailingtool.domain.SendGridEvent;
import be.alfapay.alfaplatform.mailingtool.rest.mail.message.ResponseMessage;
import be.alfapay.alfaplatform.mailingtool.util.CSVHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.sql.init.dependency.AbstractBeansOfTypeDatabaseInitializerDetector;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@CrossOrigin(origins="*", allowedHeaders="*")
@RestController
@RequestMapping("/alfaplatform-giftcard-next/mail/")
public class MailController {
    private final Map<String, Set<String>> openedEmails = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> clickedLinksInEmails = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> droppedEmails = new ConcurrentHashMap<>();

    @Autowired
    private MailManager mailManager;

    @PostMapping("send")
    public ResponseEntity<ResponseMessage> processMailing(@RequestParam("file") MultipartFile file) {
        String message = "";
        if (!CSVHelperUtil.hasCSVFormat(file)) {
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
            String message = "";
            MailSendTo mail;
            for (SendGridEvent event : events) {
                if (event.getMailingId() != null) {
                    switch (event.getEventType()) {
                        case "open":
                            openedEmails.computeIfAbsent(event.getMailingId().toString(), e -> new HashSet<>()).add(event.getEmail());
                            mail = mailManager.getMailSendToByMailingIdAndEmail(event.getMailingId(), event.getEmail());
                            if (openedEmails.containsKey(mail.getMailingId().toString())) {
                                mailManager.setOpenedForMail(mail);
                                if (mail.getOpen() == 1) {
                                    mailManager.setOpenedForMailing(mail.getMailing());
                                }
                            }
                            break;
                        case "click":
                            clickedLinksInEmails.computeIfAbsent(event.getMailingId().toString(),e -> new HashSet<>()).add(event.getEmail());
                            mail = mailManager.getMailSendToByMailingIdAndEmail(event.getMailingId(), event.getEmail());
                            if (clickedLinksInEmails.containsKey(mail.getMailingId().toString())) {
                                mailManager.setClickedLinkInMail(mail);
                                if (mail.getClick() == 1) {
                                    mailManager.setClickedLinkInMailing(mail.getMailing());
                                }
                            }
                            break;
                        case "dropped":
                            droppedEmails.computeIfAbsent(event.getMailingId().toString(), e -> new HashSet<>()).add(event.getEmail());
                            mail = mailManager.getMailSendToByMailingIdAndEmail(event.getMailingId(), event.getEmail());
                            if (droppedEmails.containsKey(mail.getMailingId().toString())) {
                                mailManager.setDroppedForMail(mail);
                                if (mail.getDropped() == 1) {
                                    mailManager.setDroppedForMailing(mail.getMailing());
                                }
                            }
                            break;
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }

    @GetMapping("report/events/opened")
    public ResponseEntity<Map<String, Set<String>>> openedEmails() {
        if (openedEmails.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(openedEmails);
    }

    @GetMapping("report/events/clicked")
    public ResponseEntity<Map<String, Set<String>>> clickedLinksInEmails() {
        if (clickedLinksInEmails.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(clickedLinksInEmails);
    }

    @GetMapping("report/events/dropped")
    public ResponseEntity<Map<String, Set<String>>> droppedEmails() {
        if (droppedEmails.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(droppedEmails);
    }
}
