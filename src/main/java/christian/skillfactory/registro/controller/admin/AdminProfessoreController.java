package christian.skillfactory.registro.controller.admin;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import christian.skillfactory.registro.model.Account;
import christian.skillfactory.registro.model.ProfessoreEntity;
import christian.skillfactory.registro.model.RegistroEntity;
import christian.skillfactory.registro.model.Ruolo;
import christian.skillfactory.registro.model.TitoloStudio;
import christian.skillfactory.registro.repository.AccountRepository;
import christian.skillfactory.registro.repository.ProfessoreRepository;
import christian.skillfactory.registro.repository.RegistroRepository;
import christian.skillfactory.registro.repository.RuoloRepository;
import christian.skillfactory.registro.repository.TitoloStudioRepository;
import christian.skillfactory.registro.service.AccountService;
import jakarta.transaction.Transactional;

/**
 * Controller for managing Professor data and their associated accounts.
 * * This controller handles enrollment, profile maintenance, and credential management.
 */
@Controller
@RequestMapping("/professore")
public class AdminProfessoreController {

	
    private final ProfessoreRepository repository;
    private final TitoloStudioRepository titoloRepo;
	private final AccountRepository accountRepo;
	private final AccountService accountService;
	private final RuoloRepository repRuolo;
	private final PasswordEncoder passwordEncoder;
	private final RegistroRepository regRep;
	
	/**
     * Constructor Injection ensures all dependencies are required and immutable.
     */
    public AdminProfessoreController(ProfessoreRepository repository, TitoloStudioRepository titoloRepo, AccountRepository accountRepo, AccountService accountService,RuoloRepository repRuolo
    			, PasswordEncoder passwordEncoder, RegistroRepository regRep) {
        this.repository = repository;
        this.titoloRepo = titoloRepo;
        this.accountRepo=accountRepo;
        this.accountService= accountService;
        this.repRuolo=repRuolo;
        this.passwordEncoder=passwordEncoder;
        this.regRep=regRep;
    }
    
    /**
     * Retrieves a paginated list of professors.
     * Uses Pageable to ensure database-level optimization.
     */
  @GetMapping
  public String paginazione(Model model,
                             @RequestParam(defaultValue = "0") int pagina,
                             @RequestParam(defaultValue = "5") int dimensione) {

      Pageable pagination = PageRequest.of(pagina, dimensione);
      Page<ProfessoreEntity> page = repository.findAll(pagination);

      model.addAttribute("professoriPaginazione", page);

      return "admin/professore-paginazione";
  }

  /**
   * Renders the form for creating a Professor (Professore).
   */
    @GetMapping("/nuovo")
    public String mostraForm(Model model) {
        model.addAttribute("professore", new ProfessoreEntity());
        model.addAttribute("setTitoli", titoloRepo.findAll()); 
        return "admin/professore-form";
    }
    
    /**
     * Generates credentials for a professor and links the account.
     */
    @PostMapping("/account/{id}")
    @ResponseBody
    @Transactional
    public Map<String, String> creaAccount(@PathVariable Long id) {
        ProfessoreEntity cerca = repository.findById(id).orElseThrow();

        String username = cerca.getMatricola();
        String pw = accountService.generaPassword();

        Account acc = new Account();
        acc.setUsername(username);
        acc.setPassword(passwordEncoder.encode(pw));
        
        Ruolo ruolo = repRuolo.findById("ROLE_PROF")
        	    .orElseGet(() -> {
        	        Ruolo nuovoRuolo = new Ruolo("ROLE_PROF");
        	        return repRuolo.save(nuovoRuolo);
        	    });
        acc.setRuolo(ruolo);
        accountRepo.save(acc);
        

        cerca.setAccount(acc);
        repository.save(cerca);

        return Map.of(
            "username", username,
            "password", pw
        );
    }

    /**
     * Persists the professor and handles the Many-to-Many relationship with TitoloStudio.
     */ 
    @PostMapping("/salva")
    @Transactional
    public String salva(@ModelAttribute("professore") ProfessoreEntity prof,
                        @RequestParam(value = "titoliSelezionati", required = false)
                        List<Long> titoliIds) {

        if (titoliIds != null) {
            Set<TitoloStudio> titoli =
                    new HashSet<>(titoloRepo.findAllById(titoliIds));

            prof.setTitoli(titoli); //  
        }

        repository.save(prof);
        return "redirect:/professore";
    }

    /**
     * Updates an existing student's personal details using an inline field-by-field approach.
     
     */
    @PutMapping("/{id}")
    @ResponseBody
    @Transactional
    public Map<String,String> modifica(@PathVariable Long id,
                           @RequestBody Map<String,Object> payload) {

        ProfessoreEntity prof = repository.findById(id).orElse(null);
        

        if (payload.containsKey("nome")) prof.setNome((String) payload.get("nome"));
        if (payload.containsKey("cognome")) prof.setCognome((String) payload.get("cognome"));
        if (payload.containsKey("codiceFiscale")) prof.setCodiceFiscale((String) payload.get("codiceFiscale"));
        if (payload.containsKey("dataNascita")) 
            prof.setDataNascita(LocalDate.parse((String) payload.get("dataNascita")));

        repository.save(prof);

        return Map.of("status", "ok", "id", prof.getId().toString());
    }

    /**
     * Performs a partial search on professor names and surnames using ExampleMatcher.
     */
    @GetMapping("/cerca")
    public String cerca(@RequestParam String keyword, Model model) {

        ProfessoreEntity probe = new ProfessoreEntity();
        probe.setNome(keyword);
        probe.setCognome(keyword);

        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<ProfessoreEntity> example = Example.of(probe, matcher);

        List<ProfessoreEntity> risultati = repository.findAll(example);
        model.addAttribute("risultati", risultati);

        return "admin/professore-paginazione";
    }

   

    /**
     * Removes professor and ensures bidirectional relationship cleanup.
     */
    @GetMapping("/elimina/{id}")
    @Transactional
    public String elimina(@PathVariable Long id) {
        // Retrieve entity
        ProfessoreEntity prof = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professore non trovato"));

        //  Many-to-Many cleanup with REGISTRO
        // Remove the professor from each associated registry to break the bond in the prof_registro join table
        for (RegistroEntity reg : prof.getRegistro()) {
            reg.getLista_prof().remove(prof);
           
        }
        prof.getRegistro().clear();

        //  Many-to-Many cleanup with TITOLI
        // Clear the Set to remove the records from the professore_titolo join table
        prof.setTitoli(null); 

     // One-to-One cleanup with ACCOUNT
     // Disconnect the account or delete it to break the relationship in the account table
        if (prof.getAccount() != null) {
            Account account = prof.getAccount();
            prof.setAccount(null); // Rompiamo il riferimento lato Professore
            accountRepo.delete(account); // Eliminiamo l'account dal DB
        }

        // 5. Ora che il Professore non ha più dipendenze, possiamo eliminarlo
        repository.delete(prof);

        return "redirect:/professore";
    }
}
