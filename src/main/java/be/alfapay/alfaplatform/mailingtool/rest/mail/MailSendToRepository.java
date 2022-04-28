package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.MailSendTo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailSendToRepository extends JpaRepository<MailSendTo, Long> {
    MailSendTo getMailSendToByMailingIdAndEmail(String mailingId, String email);
}
