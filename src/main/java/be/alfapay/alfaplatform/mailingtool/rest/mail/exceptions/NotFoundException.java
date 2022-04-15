package be.alfapay.alfaplatform.mailingtool.rest.mail.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
