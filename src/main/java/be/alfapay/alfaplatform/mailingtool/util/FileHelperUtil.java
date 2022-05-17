package be.alfapay.alfaplatform.mailingtool.util;

import be.alfapay.alfaplatform.mailingtool.resources.MailSendToDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileHelperUtil {
    private static final String TYPE_CSV = "text/csv";
    private static final String TYPE_HTML = "text/html";

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

    public List<MailSendToDTO> readDataOutOfFile(InputStream is) {
        List<MailSendToDTO> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            int lineNumber = 2;
            String headerLine = reader.readLine();
            while ((line = reader.readLine()) != null) {
                MailSendToDTO row = new MailSendToDTO();
                String[] dataSplit = line.split(" |;");
                row.setFirstName(dataSplit[0]);
                row.setLastName(dataSplit[1]);
                row.setEmail(dataSplit[2]);
                row.setAmount(Integer.parseInt(dataSplit[3]));
                row.setLineNumber(lineNumber++);
                data.add(row);
            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException("Kan CSV niet uitlezen: " + e.getMessage());
        }
    }

    public String getContentFromHtmlTemplate(Path path) {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
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
