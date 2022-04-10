package be.alfapay.alfaplatform.mailingtool.rest.mail;

import be.alfapay.alfaplatform.mailingtool.domain.Mail;

public interface IMailManager {
    void sendMail(Mail mail);
}
