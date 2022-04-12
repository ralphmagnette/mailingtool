package be.alfapay.alfaplatform.mailingtool.rest.csv;

import be.alfapay.alfaplatform.mailingtool.domain.CSVData;
import be.alfapay.alfaplatform.mailingtool.rest.csv.exceptions.NotFoundException;
import be.alfapay.alfaplatform.mailingtool.util.CSVParserUtil;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CSVManager implements ICSVManager {
    @Autowired
    private CSVRepository repository;

    @Override
    public void save(MultipartFile file) {
        try {
            List<CSVData> data = CSVParserUtil.csvToData(file.getInputStream());
            repository.saveAll(data);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    @Override
    public List<CSVData> getAll() {
        return repository.findAll();
    }

    @Override
    public CSVData getById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
