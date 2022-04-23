package be.alfapay.alfaplatform.mailingtool.util;

import be.alfapay.alfaplatform.mailingtool.domain.Mail;
import be.alfapay.alfaplatform.mailingtool.domain.MailSendTo;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileParserUtil {
    private static final String TYPE = "text/csv";
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<Mail> readDataOutOfFile(InputStream is) {
        List<Mail> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Mail row = new Mail();
                MailSendTo sendTo = new MailSendTo();
                String[] dataSplit = line.split(" |;");
                sendTo.setFirstName(dataSplit[0]);
                sendTo.setLastName(dataSplit[1]);
                sendTo.setEmail(dataSplit[2]);
                row.setSubject(dataSplit[3]);
                row.setTemplate(dataSplit[4]);
                row.setArticleId(Integer.parseInt(dataSplit[5]));
                sendTo.setAmount(Integer.parseInt(dataSplit[6]));
                row.setSendDate(LocalDate.parse(dataSplit[7], FORMAT));
                row.setSendTo(sendTo);
                data.add(row);
            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse csv file: " + e.getMessage());
        }
    }
}
