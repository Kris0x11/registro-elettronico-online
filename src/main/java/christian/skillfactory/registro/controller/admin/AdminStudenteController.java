package christian.skillfactory.registro.controller.admin;

import christian.skillfactory.registro.model.Account;
import christian.skillfactory.registro.model.Presenza;
import christian.skillfactory.registro.model.ProfessoreEntity;
import christian.skillfactory.registro.model.RegistroEntity;
import christian.skillfactory.registro.model.Ruolo;
import christian.skillfactory.registro.model.Sanzione;
import christian.skillfactory.registro.model.StudenteEntity;
import christian.skillfactory.registro.model.Voto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import christian.skillfactory.registro.repository.AccountRepository;
import christian.skillfactory.registro.repository.PresenzaRepository;
import christian.skillfactory.registro.repository.RuoloRepository;
import christian.skillfactory.registro.repository.StudenteRepository;
import christian.skillfactory.registro.repository.VotoRepository;
import christian.skillfactory.registro.service.AccountService;
/**
 * Controller for managing Student data and their associated accounts.
 * * This controller handles enrollment, profile maintenance, and credential management.
 */
@Controller
@RequestMapping("/studente")
public class AdminStudenteController {
	

	private final StudenteRepository repository;
	private final AccountRepository repAccount; 
	private final AccountService accountService;
	private final RuoloRepository repRuolo;
	private final PasswordEncoder passwordEncoder;
	private final PresenzaRepository presenzaRep;
	private final VotoRepository repVoto;
	
	/**
     * Constructor Injection ensures all dependencies are required and immutable.
     */
	public AdminStudenteController(StudenteRepository repository,AccountRepository repAccount,AccountService accountService,RuoloRepository repRuolo,
			PasswordEncoder passwordEncoder,VotoRepository repVoto, PresenzaRepository presenzaRep)
	{
		this.repository=repository;
		this.repAccount=repAccount;
		this.accountService=accountService;
		this.repRuolo=repRuolo;
		this.passwordEncoder=passwordEncoder;
		this.repVoto=repVoto;
		this.presenzaRep = presenzaRep;
	}
	
	
	 /**
     * Retrieves a paginated list of students.
     * Uses Pageable to ensure database-level optimization.
     */
	@GetMapping
	public String paginazione(Model model, @RequestParam(defaultValue = "0") int pagina, @RequestParam(defaultValue="5") int dimensione)
	{
		Pageable pagination = PageRequest.of(pagina,dimensione);
		Page<StudenteEntity> paginazioneStudenti = repository.findAll(pagination);
		model.addAttribute("studentiPaginazione",paginazioneStudenti);
		return "admin/studenti-paginazione";
	}
	
	
	  /**
     * Renders the form for creating a Student (Studente).
     */
	 @GetMapping("/nuovo")
	 public String mostraForm(Model model) { 
		 model.addAttribute("studente", new StudenteEntity());	 
		 return "admin/studente-form";
	  }
	 
	 	/**
	     * Generates credentials for a professor and links the account.
	     */
	 @PostMapping("/account/{id}")
	    @ResponseBody
	    public Map<String, String> creaAccount(@PathVariable Long id) {
	        StudenteEntity cerca = repository.findById(id).orElseThrow();

	        String username = cerca.getMatricola();
	        String pw = accountService.generaPassword();

	        Account acc = new Account();
	        acc.setUsername(username);
	        acc.setPassword(passwordEncoder.encode(pw));
	        Ruolo ruolo = repRuolo.findById("ROLE_STUDENTE").orElseGet(() -> {
    	        Ruolo nuovoRuolo = new Ruolo("ROLE_STUDENTE");
    	        return repRuolo.save(nuovoRuolo);
    	    });
	        
	        acc.setRuolo(ruolo);
	        repAccount.save(acc);
	        

	        cerca.setAccount(acc);
	        repository.save(cerca);

	        return Map.of(
	            "username", username,
	            "password", pw
	        );
	    }
	 
	 
	
	
	 	/**
	     * Persists the student.
	     */
	@PostMapping("/salva")
	public String salva(@ModelAttribute("studente")StudenteEntity s)
	{
		repository.save(s);
	    return "redirect:/studente/nuovo";

	}
	
	/**
     * Updates an existing student's personal details using an inline field-by-field approach.
     * Validates the student's existence before applying changes.
     */
	@PutMapping("/modifica/{id}")
	public String modificaStudente(@PathVariable Long id,
	                               @RequestParam String nome,
	                               @RequestParam String cognome,
	                               @RequestParam String codiceFiscale,
	                               @RequestParam(required=false) LocalDate dataNascita) {

	    StudenteEntity studente = repository.findById(id).orElse(null);
	    if (studente==null)
	    {
	    	return "modifica-errore";
	    }

	    studente.setNome(nome);
	    studente.setCognome(cognome);
	    studente.setCodiceFiscale(codiceFiscale);
	    studente.setDataNascita(dataNascita);

	    
	    repository.save(studente);

	    
	    return "redirect:/studente";
	}
	
	/**
     * Searches for students by name and displays the results alongside the default paginated view.
     */
	@GetMapping("/cerca")
	public String cerca(@RequestParam String keyword,
	                    Model model) {

		// Setup search probe
	    StudenteEntity probe = new StudenteEntity();
	    probe.setNome(keyword);

	    // Perform search using the example   
	    ExampleMatcher matcher = ExampleMatcher.matchingAny()
	            .withIgnoreCase()
	            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
	    Example<StudenteEntity> example = Example.of(probe, matcher);
	    List<StudenteEntity> risultati = repository.findAll(example);
	    
	    // Add search results and default pagination to the model
	    model.addAttribute("risultati", risultati);
	    Pageable pagination = PageRequest.of(0, 5);
	    Page<StudenteEntity> studentiPaginazione = repository.findAll(pagination);
	    model.addAttribute("studentiPaginazione", studentiPaginazione);
	    
	    return "admin/studenti-paginazione";
	}

	/**
     * Deletes a student and cleans up all associated relationships to avoid 
     * database constraint violations.
     */

	@GetMapping("/elimina/{id}")
	public String elimina(@PathVariable Long id)
	{
	    StudenteEntity stud = repository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Studente non trovato"));
	    
	 // Remove links from grades
	    for (Voto v : stud.getVoti()) {
	    	v.setVotoStudente(null);    	    	
	    }
	    repVoto.saveAll(stud.getVoti());
	 // Remove links from sanctions (Many-to-Many management)
	   for (Sanzione s : stud.getListaSanzione())
	   {
		   s.getStudenti().remove(stud);
	   }
	   stud.getListaSanzione().clear();
	   
	// Nullify other references
	   stud.setRegstudenti(null);
	// Remove links from attendance records
	   for (Presenza p : stud.getListaPresenze())
	   {
		   p.setStudente(null);
	   }
	 presenzaRep.saveAll(stud.getListaPresenze());
	//  delete the student
	    repository.delete(stud);

	  
	    return "redirect:/studente";
	}
		
	
	


	
}

