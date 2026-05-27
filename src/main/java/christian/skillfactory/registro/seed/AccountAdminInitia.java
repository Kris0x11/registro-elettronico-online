package christian.skillfactory.registro.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import christian.skillfactory.registro.model.Account;
import christian.skillfactory.registro.model.Ruolo;
import christian.skillfactory.registro.repository.AccountRepository;
import christian.skillfactory.registro.repository.RuoloRepository;

import jakarta.transaction.Transactional;

/**
 * Database seeder that executes on application startup.
 * Automatically initializes the default ADMIN role and account if they do not exist.
 */
@Component
public class AccountAdminInitia implements CommandLineRunner {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private RuoloRepository ruoloRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Callback method used to run the bean on Spring Boot application startup.
     */
    @Override
    @Transactional
    public void run(String... args) {
        System.out.println("Initializing default ADMIN credentials...");

        //  Check or create the ADMIN role
        // Fetch the role from the database or create it dynamically using a lambda supplier expression
        Ruolo adminRole = ruoloRepo.findById("ROLE_ADMIN")
                .orElseGet(() -> {
                    Ruolo r = new Ruolo();
                    r.setNome("ROLE_ADMIN");
                    return ruoloRepo.saveAndFlush(r); // Immediate flush to guarantee persistence before mapping the account
                });

        // Check or create the default ADMIN account
        // If the username 'admin' isn't registered, create the default credentials and encrypt the password
        if (accountRepo.findByUsername("admin").isEmpty()) {
            Account admin = new Account();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); // Encrypt plain text using BCrypt via PasswordEncoder
            admin.setRuolo(adminRole);
            
            accountRepo.save(admin);
            System.out.println("Default admin account successfully created!");
        }
    }
}


	        
	    


