package be.alfapay.alfaplatform.mailingtool.util;

import be.alfapay.alfaplatform.mailingtool.domain.MailSendTo;
import be.alfapay.alfaplatform.mailingtool.domain.Mailing;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileParserUtil {
    private static final String TYPE = "text/csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<MailSendTo> readDataOutOfFile(InputStream is) {
        List<MailSendTo> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            Mailing mailing = new Mailing();
            while ((line = reader.readLine()) != null) {
                MailSendTo row = new MailSendTo();
                String[] dataSplit = line.split(" |;");
                row.setFirstName(dataSplit[0]);
                row.setLastName(dataSplit[1]);
                row.setEmail(dataSplit[2]);
                mailing.setSubject(dataSplit[3]);
                mailing.setTemplate(dataSplit[4]);
                mailing.setArticleId(Integer.parseInt(dataSplit[5]));
                row.setAmount(Integer.parseInt(dataSplit[6]));
                mailing.setSendDate(LocalDate.parse(dataSplit[7], FORMATTER));
                row.setMailing(mailing);
                data.add(row);
            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse csv file: " + e.getMessage());
        }
    }
}
