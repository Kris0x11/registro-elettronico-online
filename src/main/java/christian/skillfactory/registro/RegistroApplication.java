package christian.skillfactory.registro;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import christian.skillfactory.registro.model.Account;
import christian.skillfactory.registro.model.Ruolo;
import christian.skillfactory.registro.repository.AccountRepository;
import christian.skillfactory.registro.repository.RuoloRepository;

/**
 * Main entry point for the Register application.
 */
@SpringBootApplication
public class RegistroApplication {

	public static void main(String[] args) {
		// Bootstraps the Spring context and starts the embedded server
		SpringApplication.run(RegistroApplication.class, args);
		System.out.print("app in ascolto");
	}
	



}
