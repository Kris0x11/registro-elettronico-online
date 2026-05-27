package christian.skillfactory.registro.controller.admin;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import christian.skillfactory.registro.model.RegistroEntity;
import christian.skillfactory.registro.model.Sanzione;
import christian.skillfactory.registro.model.StudenteEntity;
import christian.skillfactory.registro.model.Voto;
import christian.skillfactory.registro.model.ProfessoreEntity;
import christian.skillfactory.registro.repository.ProfessoreRepository;
import christian.skillfactory.registro.repository.RegistroRepository;
import christian.skillfactory.registro.repository.SanzioneRepository;
import christian.skillfactory.registro.repository.StudenteRepository;
import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Controller for managing School Registers (Registri).
 * Handles the association between Students, Professors and their specific classes.
 */
@Controller
@RequestMapping("/registro")
public class AdminRegistroController {
	
	private final RegistroRepository repository;
	private final ProfessoreRepository repProf;
	private final StudenteRepository repStudente;
	private final SanzioneRepository repSanzione;
	
	public AdminRegistroController(RegistroRepository repository,ProfessoreRepository repProf,
			StudenteRepository repStudente, SanzioneRepository repSazione)
	{
		this.repository=repository;
		this.repProf=repProf;
		this.repStudente=repStudente;
		this.repSanzione=repSazione;
	}

	
@GetMapping("/home")
public String paginazione(Model model,
             @RequestParam(defaultValue = "0") int pagina,
             @RequestParam(defaultValue = "5") int dimensione) {

Pageable pagination = PageRequest.of(pagina, dimensione);
Page<RegistroEntity> page = repository.findAll(pagination);

model.addAttribute("registroPaginazione", page);

return "admin/registro-home";
}
/**
 * Renders the form for creating a Register (Registro).
 */
	@GetMapping("/nuovo")
	@Transactional
	public String registroForm(Model model)
	{
		model.addAttribute("registro", new RegistroEntity());
		model.addAttribute("prof", repProf.findAll());
		model.addAttribute("studente",repStudente.findAll());
		
		return "admin/registro-form";
	}
	
	/**
     * Saves the register and establishes Many-to-Many and One-to-Many relationships.
     */
	@PostMapping("/salva")
	@Transactional
	public String salvaRegistro(@ModelAttribute("registro") RegistroEntity registro,
	                            @RequestParam(value="studentiSelect", required=false) List<Long> studentiId,
	                            @RequestParam(value="profSelect", required=false) List<Long> profId) {

	    // salva il registro senza studenti
	    repository.save(registro);

	    // associa professori se presenti
	    if (profId != null) {
	        List<ProfessoreEntity> listaProfessori = repProf.findAllById(profId);
	        registro.setLista_prof(listaProfessori);
	    }

	    // associa studenti se presenti
	    if (studentiId != null) {
	        List<StudenteEntity> listaStudenti = repStudente.findAllById(studentiId);
	        for (StudenteEntity studente : listaStudenti) {
	            studente.setRegstudenti(registro); // aggiorna il lato ManyToOne
	        }
	        registro.setStudenti(listaStudenti); // aggiorna il lato OneToMany
	    }

	    // salva tutto in un colpo, cascade si occupa degli studenti
	    repository.save(registro);

	    return "redirect:/registro/home";
	}

	/**
     * Performs a partial search on Register names and surnames using ExampleMatcher.
     */
	@GetMapping("/cerca")
	public String cerca(@RequestParam String keyword,
	                    Model model) {

		// Setup search probe
	   RegistroEntity probe = new RegistroEntity();
	    probe.setNomeClasse(keyword);

	    ExampleMatcher matcher = ExampleMatcher.matchingAny()
	            .withIgnoreCase()
	            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
	    // Perform search using the example   
	    Example<RegistroEntity> example = Example.of(probe, matcher);

	    // Add search results and default pagination to the model
	    List<RegistroEntity> risultati = repository.findAll(example);
	    model.addAttribute("risultati", risultati);
	    Pageable pagination = PageRequest.of(0, 5);
	    Page<RegistroEntity> registroPaginazione = repository.findAll(pagination);
	    model.addAttribute("studentiPaginazione", registroPaginazione);
	    return "admin/registro-home";
	}

	/**
     * Deletes a register, manually breaking associations with students and professors 
     * to avoid foreign key constraint violations.
     */
	@GetMapping("/elimina/{id}")
	@Transactional
	public String elimina(@PathVariable String id) {
	    // 1. Retrieve the registry entity or throw an exception if not found
	    RegistroEntity reg = repository.findById(id)
	            .orElseThrow(() -> new RuntimeException("registro non trovato"));

	    // Unlink sanctions
	    if (reg.getSanzione() != null) {
	        repSanzione.deleteAll(reg.getSanzione());
	        reg.getSanzione().clear(); // Clear the in-memory list to sync Hibernate state
	    }

	    //  Unlink students (One-To-Many relationship handling)
	    if (reg.getStudenti() != null) {
	        for (StudenteEntity s : reg.getStudenti()) {
	            s.setRegstudenti(null); // Break the bidirectional reference to the registry     
	        }
	        repStudente.saveAll(reg.getStudenti());
	        reg.getStudenti().clear(); // Clear the in-memory collection for Hibernate cache consistency
	    }
	    
	    //  Unlink professors (Many-To-Many relationship handling)
	    if (reg.getLista_prof() != null) {
	        for (ProfessoreEntity prof : reg.getLista_prof()) {
	            prof.getRegistro().remove(reg); // Remove this registry from the professor's list
	        }
	        reg.getLista_prof().clear(); // Clear the inverted relationship list
	    }
	    
	    //  The entity is now completely isolated, safe to delete
	    repository.delete(reg);
	    
	   

	    return "redirect:/registro/home";
	}
	

}
	
