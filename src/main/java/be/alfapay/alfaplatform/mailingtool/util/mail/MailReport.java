package be.alfapay.alfaplatform.mailingtool.util.mail;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

import java.io.IOException;
import java.time.LocalDate;

public class MailReport implements IMailReport {
    private static final String SENDGRID_APIKEY = "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";

    public MailReport() {

    }

    public static void main(String[] args) {
        MailReport report = new MailReport();
        report.mailSettings();
        report.mailStatistics(LocalDate.of(2022, 1, 1));
    }

    @Override
    public void mailSettings() {
        try {
            SendGrid sg = new SendGrid(SENDGRID_APIKEY);
            Request request = new Request();
            request.setMethod(Method.GET);
            request.setEndpoint("/mail_settings");
            Response response = sg.api(request);
            System.out.printf("Status: %d%n", response.getStatusCode());
            System.out.printf("Body: %s%n", response.getBody());
            System.out.printf("Headers: %s", response.getHeaders());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    @Override
    public void mailStatistics(LocalDate date) {
        try {
            SendGrid sg = new SendGrid(SENDGRID_APIKEY);
            Request request = new Request();
            request.setMethod(Method.GET);
            request.setEndpoint("/stats");
            request.addQueryParam("start_date", String.valueOf(date));
            Response response = sg.api(request);
            System.out.printf("Status: %d%n", response.getStatusCode());
            System.out.printf("Body: %s%n", response.getBody());
            System.out.printf("Headers: %s", response.getHeaders());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }
}
