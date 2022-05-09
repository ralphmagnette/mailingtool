package be.alfapay.alfaplatform.mailingtool.util;

import be.alfapay.alfaplatform.mailingtool.domain.MailSendTo;
import be.alfapay.alfaplatform.mailingtool.domain.Mailing;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class FileHelperUtil {
    private static final String TYPE_CSV = "text/csv";
    private static final String TYPE_HTML = "text/html";
    private static final String LOCAL_PATH = "C:\\Users\\ralph\\OneDrive\\Documents\\Gift2Give\\";
    private static final String ASSETS_PATH = "assets\\";

    public boolean hasCSVFormat(MultipartFile csv) {
        if (!TYPE_CSV.equals(csv.getContentType())) {
            return false;
        }
        return true;
    }

    public boolean hasHTMLFormat(MultipartFile template) {
        if (!TYPE_HTML.equals(template.getContentType())) {
            return false;
        }
        return true;
    }

    public String getFilePath(String fileName) {
        return LOCAL_PATH + ASSETS_PATH + fileName;
    }

    public List<MailSendTo> readDataOutOfFile(InputStream is) {
        List<MailSendTo> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            String headerLine = reader.readLine();
            Mailing mailing = new Mailing();
            while ((line = reader.readLine()) != null) {
                MailSendTo row = new MailSendTo();
                String[] dataSplit = line.split(" |;");
                row.setFirstName(dataSplit[0]);
                row.setLastName(dataSplit[1]);
                row.setEmail(dataSplit[2]);
                row.setAmount(Integer.parseInt(dataSplit[3]));
                row.setMailing(mailing);
                data.add(row);
            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException("Kan CSV niet uitlezen: " + e.getMessage());
        }
    }

    public String getContentFromHtmlTemplate(InputStream is) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            StringBuilder template = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                template.append(line);
            }
            return template.toString();
        } catch (IOException e) {
            throw new RuntimeException("Kan template niet uitlezen: " + e.getMessage());
        }
    }
}
