package be.alfapay.alfaplatform.mailingtool.rest.csv;

import be.alfapay.alfaplatform.mailingtool.domain.CSVData;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ICSVManager {
    void save(MultipartFile file);
    List<CSVData> getAll();
    CSVData getById(Long id);
}
