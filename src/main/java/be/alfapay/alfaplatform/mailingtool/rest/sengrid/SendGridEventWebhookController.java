package be.alfapay.alfaplatform.mailingtool.rest.sengrid;

import be.alfapay.alfaplatform.mailingtool.domain.SendGridEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin(value = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/giftcard-alfaplatform/report/")
public class SendGridEventWebhookController {
    private List<SendGridEvent> sgEvents = new ArrayList<>();

//    @PostMapping("events")
//    public ResponseEntity<String> receivedSGEventHook(@RequestBody List<SendGridEvent> events) {
//        try {
//            sgEvents.addAll(events);
//            return new ResponseEntity<>(HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
//        }
//    }

    @PostMapping(value = "events")
    public void processInboundSendGridEmails(HttpServletRequest request, HttpServletResponse response,
                                             SendGridEvent event) {
        sgEvents.add(event);
    }

    @GetMapping("events")
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
