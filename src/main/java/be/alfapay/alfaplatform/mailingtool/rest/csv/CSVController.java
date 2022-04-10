package be.alfapay.alfaplatform.mailingtool.rest.csv;

import be.alfapay.alfaplatform.mailingtool.domain.CSVData;
import be.alfapay.alfaplatform.mailingtool.rest.csv.message.ResponseMessage;
import be.alfapay.alfaplatform.mailingtool.util.CSVParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins="*", allowedHeaders="*")
@Controller
@RequestMapping("/giftcard-alfaplatform/csv/")
public class CSVController {
    @Autowired
    private CSVManager manager;

    @PostMapping("upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        if (!CSVParserUtil.hasCSVFormat(file)) {
            message = "Please upload a csv file!";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        try {
            manager.save(file);
            message = "Uploaded the file succesfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("data")
    public ResponseEntity<List<CSVData>> getAllData() {
        try {
            List<CSVData> data = manager.getAllData();
            if (data.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
