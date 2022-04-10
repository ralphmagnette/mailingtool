package be.alfapay.alfaplatform.mailingtool.rest.csv.exceptions;

import be.alfapay.alfaplatform.mailingtool.rest.csv.message.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CSVUploadException extends ResponseEntityExceptionHandler {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity handleMaxSizeException() {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage("File too large!"));
    }
}
