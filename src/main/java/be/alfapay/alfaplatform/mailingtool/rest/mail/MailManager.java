package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.MailSendTo;
import be.alfapay.alfaplatform.mailingtool.domain.Mailing;
import be.alfapay.alfaplatform.mailingtool.domain.Status;
import be.alfapay.alfaplatform.mailingtool.resources.MailSendToDTO;
import be.alfapay.alfaplatform.mailingtool.rest.mail.message.ResponseMessage;
import be.alfapay.alfaplatform.mailingtool.util.FileHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MailManager implements IMailManager {
    @Autowired
    private MailingRepository mailingRepository;

    @Autowired
    private MailSendToRepository mailSendToRepository;

    @Autowired
    private FileHelperUtil fileHelperUtil;

    @Override
    public List<MailSendToDTO> scheduleMailing(MultipartFile csv, MultipartFile template, Integer articleId,
                                               String subject, Long sendDate) {
        try {
            List<MailSendToDTO> receivers = fileHelperUtil.readDataOutOfFile(csv.getInputStream());
            List<MailSendToDTO> errors = validate(receivers, articleId, subject);

            if (errors.isEmpty()) {
                String csvPath = "C:\\Users\\ralph\\OneDrive\\Documents\\Gift2Give\\assets\\" + csv.getOriginalFilename();
                String templatePath = "C:\\Users\\ralph\\OneDrive\\Documents\\Gift2Give\\assets\\" + template.getOriginalFilename();
                csv.transferTo(new File(csvPath));
                template.transferTo(new File(templatePath));
                Mailing mailing = new Mailing(UUID.randomUUID().toString());
                mailing.setArticleId(articleId);
                mailing.setSubject(subject);
                mailing.setCsv(csvPath);
                mailing.setTemplate(templatePath);
                Long now = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Europe/Brussels")).toInstant().toEpochMilli();
                mailing.setDate(now);
                if (sendDate == null) {
                    mailing.setSendDate(now + 3600000);
                } else {
                    mailing.setSendDate(sendDate + 3600000);
                }
                mailing.setStatus(Status.CREATED);
                mailingRepository.save(mailing);

                for (MailSendToDTO mail : receivers) {
                    mail.setMailingId(mailing.getId());
                    mailSendToRepository.save(mapMailSendToDTOToMailSendTo(mail));
                }
            } else {
                return errors;
            }
        } catch (IOException e) {
            throw new RuntimeException("Kan data uit CSV-bestand niet opslaan: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Mailing> getAllMailings() {
        return mailingRepository.findAllByOrderByDateDesc();
    }

    @Override
    public List<Mailing> getMailingsByStatusAndSendDateAfter(Status status, Long after) {
        return mailingRepository.findMailingsByStatusAndSendDateAfter(status, after);
    }

    @Override
    public Mailing getMailingById(String id) {
        return mailingRepository.findById(id).orElse(null);
    }

    @Override
    public Mailing saveMailing(Mailing mailing) {
        return mailingRepository.save(mailing);
    }

    @Override
    public List<MailSendTo> getAllMailsSendTo() {
        return mailSendToRepository.findAll();
    }

    @Override
    public String getAllMailsSendToByMailingIdAndExportCSV(String mailingId) {
        List<MailSendTo> mails = mailSendToRepository.getMailsSendToByMailingId(mailingId);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("id;");
        stringBuilder.append("mailingId;");
        stringBuilder.append("email;");
        stringBuilder.append("voornaam;");
        stringBuilder.append("naam;");
        stringBuilder.append("bedrag;");
        stringBuilder.append("bon;");
        stringBuilder.append("geopend;");
        stringBuilder.append("geklikt;");
        stringBuilder.append("niet afgeleverd" + "\n");
        for (MailSendTo mail : mails) {
            stringBuilder.append(mail.getId());
            stringBuilder.append(";");
            stringBuilder.append(mail.getMailingId());
            stringBuilder.append(";");
            stringBuilder.append(mail.getEmail());
            stringBuilder.append(";");
            stringBuilder.append(mail.getFirstName());
            stringBuilder.append(";");
            stringBuilder.append(mail.getLastName());
            stringBuilder.append(";");
            stringBuilder.append(mail.getAmount());
            stringBuilder.append(";");
            stringBuilder.append(mail.getGiftCard());
            stringBuilder.append(";");
            stringBuilder.append(mail.getOpen());
            stringBuilder.append(";");
            stringBuilder.append(mail.getClick());
            stringBuilder.append(";");
            stringBuilder.append(mail.getDropped());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public MailSendTo getMailSendToById(Long id) {
        return mailSendToRepository.findById(id).orElse(null);
    }

    @Override
    public List<MailSendTo> getAllMailsSendToByMailingId(String mailingId) {
        return mailSendToRepository.getMailsSendToByMailingId(mailingId);
    }

    @Override
    public MailSendTo getMailSendToByMailingIdAndEmail(String mailingId, String email) {
        return mailSendToRepository.getMailSendToByMailingIdAndEmail(mailingId, email);
    }

    @Override
    public ResponseMessage setOpened(String mailingId, String email) {
        MailSendTo mail = getMailSendToByMailingIdAndEmail(mailingId,email);
        if (mail == null) {
            return new ResponseMessage("Er kan geen verzonden mail gevonden worden voor mailing met id: " + mailingId + ".");
        }
        mail.setOpen(mail.getOpen() + 1);
        mailSendToRepository.save(mail);

        if (mail.getOpen() == 1) {
            Mailing mailing = getMailingById(mail.getMailingId());
            if (mailing == null) {
                return new ResponseMessage("Mailing met id: " + mailingId + " kan niet worden gevonden.");
            }
            mailing.setOpen(mailing.getOpen() + 1);
            mailingRepository.save(mailing);
        }
        return new ResponseMessage("Mailing met id: " + mailingId + " is geopend door " + mail.getFirstName()  + " " + mail.getLastName());
    }

    @Override
    public ResponseMessage setClicked(String mailingId, String email) {
        MailSendTo mail = getMailSendToByMailingIdAndEmail(mailingId, email);
        if (mail == null) {
            return new ResponseMessage("Er kan geen verzonden mail gevonden worden voor mailing met id: " + mailingId + ".");
        }
        mail.setClick(mail.getClick() + 1);
        mailSendToRepository.save(mail);

        if (mail.getClick() == 1) {
            Mailing mailing = getMailingById(mailingId);
            if (mailing == null) {
                return new ResponseMessage("Mailing met id: " + mailingId + " kan niet worden gevonden.");
            }
            mailing.setClick(mailing.getClick() + 1);
            mailingRepository.save(mailing);
        }
        return new ResponseMessage("Voor mailing met id: " + mailingId + " is er op de link geklikt door " + mail.getFirstName()  + " " + mail.getLastName());
    }

    @Override
    public ResponseMessage setDropped(String mailingId, String email) {
        MailSendTo mail = getMailSendToByMailingIdAndEmail(mailingId, email);
        if (mail == null) {
            return new ResponseMessage("Er kan geen verzonden mail gevonden worden voor mailing met id: " + mailingId + ".");
        }
        mail.setDropped(mail.getDropped() + 1);
        mailSendToRepository.save(mail);

        if (mail.getDropped() == 1) {
            Mailing mailing = getMailingById(mailingId);
            if (mailing == null) {
                return new ResponseMessage("Mailing met id: " + mailingId + " kan niet worden gevonden.");
            }
            mailing.setDropped(mailing.getDropped() + 1);
            mailingRepository.save(mailing);
        }
        return new ResponseMessage("Mailing met id: " + mailingId + " kan niet worden afgeleverd aan " + mail.getEmail() + ".");
    }

    @Override
    public ResponseMessage setStatus(String mailingId, Status status) {
        Mailing mailing = mailingRepository.findById(mailingId).orElse(null);
        if (mailing == null) {
            return new ResponseMessage("Mailing met id: " + mailingId + " kan niet worden gevonden.");
        }
        mailing.setStatus(status);
        mailingRepository.save(mailing);
        return new ResponseMessage("Mailing met id: " + mailingId + "is geannuleerd.");
    }

    private List<MailSendToDTO> validate(List<MailSendToDTO> receivers, Integer articleId, String subject) {
        List<MailSendToDTO> errors = new ArrayList<>();

       for (MailSendToDTO mail : receivers) {
            if (!isValid(mail, articleId, subject)) {
                errors.add(mail);
            }
        }
        return errors;
    }

    private boolean isValid(MailSendToDTO mail, Integer articleId, String subject) {
        List<String> errors = new ArrayList<>();

        if (articleId == null || articleId <= 0) {
            errors.add("Artikel ID mag niet leeg zijn of kleiner dan 0 zijn.");
        }

        if (subject == null || subject.isEmpty()) {
            errors.add("Subject mag niet leeg zijn.");
        }

        if (mail.getEmail() == null || !mail.getEmail().matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")) {
            errors.add("Er moet een geldig email adres worden ingegeven.");
        }

        if (mail.getFirstName() == null || mail.getFirstName().isEmpty()) {
            errors.add("Voornaam mag niet leeg zijn.");
        }

        if (mail.getAmount() < 0) {
            errors.add("Bedrag kan niet kleiner dan 0 zijn.");
        }

        if (errors.isEmpty()) {
            return true;
        }

        mail.setErrors(errors);
        return false;
    }

    private MailSendTo mapMailSendToDTOToMailSendTo(MailSendToDTO mailSendToDTO) {
        MailSendTo mailSendTo = new MailSendTo();
        mailSendTo.setMailingId(mailSendToDTO.getMailingId());
        mailSendTo.setEmail(mailSendToDTO.getEmail());
        mailSendTo.setFirstName(mailSendToDTO.getFirstName());
        mailSendTo.setLastName(mailSendToDTO.getLastName());
        mailSendTo.setAmount(mailSendToDTO.getAmount());

        return mailSendTo;
    }
}
