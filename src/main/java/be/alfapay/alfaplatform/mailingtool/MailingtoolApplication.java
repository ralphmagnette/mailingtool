package be.alfapay.alfaplatform.mailingtool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MailingtoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailingtoolApplication.class, args);
	}

}
