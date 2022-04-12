package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.Mail;
import be.alfapay.alfaplatform.mailingtool.rest.csv.message.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin(origins="*", allowedHeaders="*")
@Controller
@RequestMapping("/giftcard-alfaplatform/mail/")
public class MailController {
    @Autowired
    private MailManager manager;

    @PostMapping("send")
    public ResponseEntity<ResponseMessage> sendMail(@RequestBody Mail mail) {
        try {
            manager.sendMail(mail);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Email is verstuurd."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }
}
