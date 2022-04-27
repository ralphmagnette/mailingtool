package be.alfapay.alfaplatform.mailingtool.util;

import be.alfapay.alfaplatform.mailingtool.domain.MailSendTo;
import be.alfapay.alfaplatform.mailingtool.domain.Mailing;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CSVHelperUtil {
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
            String headerLine = reader.readLine();
            Mailing mailing = new Mailing();
            while ((line = reader.readLine()) != null) {
                MailSendTo row = new MailSendTo();
                String[] dataSplit = line.split(" |;");
                row.setFirstName(dataSplit[0]);
                row.setLastName(dataSplit[1]);
                row.setEmail(dataSplit[2]);
                mailing.setSubject(dataSplit[3]);
                mailing.setCsv(dataSplit[4]);
                mailing.setTemplate(dataSplit[5]);
                mailing.setArticleId(Integer.parseInt(dataSplit[6]));
                row.setAmount(Integer.parseInt(dataSplit[7]));
                mailing.setSendDate(LocalDate.parse(dataSplit[8], FORMATTER));
                row.setMailing(mailing);
                data.add(row);
            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException("Kan CSV-bestand niet omzetten: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream createCSVFile(List<MailSendTo> mails) {
        final CSVFormat format = CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(';');
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);
            csvPrinter.printRecord("ID", "MailingID", "Email", "Voornaam", "Naam", "Bedrag", "Bon", "Mail geopend", "Link in mail geklikt", "Niet afgeleverd");
            for (MailSendTo mail : mails) {
                List<? extends Serializable> data = Arrays.asList(
                        String.valueOf(mail.getId()),
                        mail.getMailingId(),
                        mail.getEmail(),
                        mail.getFirstName(),
                        mail.getLastName(),
                        String.valueOf(mail.getAmount()),
                        mail.getGiftCard(),
                        String.valueOf(mail.getOpen()),
                        String.valueOf(mail.getClick()),
                        String.valueOf(mail.getDropped())
                );
                csvPrinter.printRecord(data);
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Kan data niet importeren naar CSV-bestand: " + e.getMessage());
        }
    }
}
