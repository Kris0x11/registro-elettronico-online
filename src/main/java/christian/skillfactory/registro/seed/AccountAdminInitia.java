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
@Component
public class AccountAdminInitia implements CommandLineRunner {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private RuoloRepository ruoloRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        System.out.println("Inizializzazione account ADMIN...");

        // Creo ruolo admin se non esiste
        Ruolo adminRole = ruoloRepo.findById("ROLE_ADMIN")
                .orElseGet(() -> {
                    Ruolo r = new Ruolo();
                    r.setNome("ROLE_ADMIN");
                    return ruoloRepo.saveAndFlush(r); // flush immediato
                });

        // Creo account admin se non esiste
        if (accountRepo.findByUsername("admin").isEmpty()) {
            Account admin = new Account();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRuolo(adminRole);
            accountRepo.save(admin);
            System.out.println("Account admin creato!");
        }

    }
}


	        
	    


