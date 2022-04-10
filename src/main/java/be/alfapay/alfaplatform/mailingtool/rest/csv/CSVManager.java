package be.alfapay.alfaplatform.mailingtool.rest.csv;

import be.alfapay.alfaplatform.mailingtool.domain.CSVData;
import be.alfapay.alfaplatform.mailingtool.util.CSVParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CSVManager implements ICSVManager {
    @Autowired
    private CSVRepository repository;

    public void save(MultipartFile file) {
        try {
            List<CSVData> data = CSVParserUtil.csvToData(file.getInputStream());
            repository.saveAll(data);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public List<CSVData> getAllData() {
        return repository.findAll();
    }
}
