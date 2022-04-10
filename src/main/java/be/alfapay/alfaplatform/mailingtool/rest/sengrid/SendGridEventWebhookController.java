package be.alfapay.alfaplatform.mailingtool.rest.sengrid;

import be.alfapay.alfaplatform.mailingtool.domain.SendGridEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(value = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/giftcard-alfaplatform/report/")
public class SendGridEventWebhookController {
    private List<SendGridEvent> sgEvents = new ArrayList<>();

    @PostMapping("events")
    public ResponseEntity<String> receivedSGEventHook(@RequestBody List<SendGridEvent> events) {
        try {
            sgEvents.addAll(events);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
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
