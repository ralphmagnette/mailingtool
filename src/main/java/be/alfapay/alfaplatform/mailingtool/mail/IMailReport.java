package be.alfapay.alfaplatform.mailingtool.mail;

import java.time.LocalDate;

public interface IMailReport {
    void mailSettings();
    void mailStatistics(LocalDate date);
}
