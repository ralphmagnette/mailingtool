package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.CSVData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CSVRepository extends JpaRepository<CSVData, Long> {
    CSVData findByEmail(String email);
}
