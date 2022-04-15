package be.alfapay.alfaplatform.mailingtool.util;

import be.alfapay.alfaplatform.mailingtool.domain.CSVData;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVParserUtil {
    public static String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<CSVData> csvToData(InputStream is) {
        List<CSVData> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                CSVData row = new CSVData();
                String[] dataSplit = line.split(" |;");
                row.setId(Long.parseLong(dataSplit[0]));
                row.setFirstName(dataSplit[1]);
                row.setLastName(dataSplit[2]);
                row.setEmail(dataSplit[3]);
                row.setAmount(Integer.parseInt(dataSplit[4]));
                data.add(row);
            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse csv file: " + e.getMessage());
        }
    }
}
