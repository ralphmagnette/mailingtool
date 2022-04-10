package be.alfapay.alfaplatform.mailingtool.rest.csv;

import be.alfapay.alfaplatform.mailingtool.domain.CSVData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CSVRepository extends JpaRepository<CSVData, Long> {

}