package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.Mailing;
import be.alfapay.alfaplatform.mailingtool.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MailingRepository extends JpaRepository<Mailing, String> {
    List<Mailing> findAllByOrderByDateAsc();
    List<Mailing> findMailingsByStatusAndSendDateAfter(Status status, Long after);
}