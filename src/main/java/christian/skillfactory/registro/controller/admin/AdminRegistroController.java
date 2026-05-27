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
	
	public AdminRegistroController(RegistroRepository repository,ProfessoreRepository repProf,StudenteRepository repStudente)
	{
		this.repository=repository;
		this.repProf=repProf;
		this.repStudente=repStudente;
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

	    // FILTRO RICERCA
	   RegistroEntity probe = new RegistroEntity();
	    probe.setNomeClasse(keyword);

	    ExampleMatcher matcher = ExampleMatcher.matchingAny()
	            .withIgnoreCase()
	            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
	            
	    Example<RegistroEntity> example = Example.of(probe, matcher);

	    // RISULTATI DELLA RICERCA
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
	public String elimina(@PathVariable String id)
	{
	    RegistroEntity reg = repository.findById(id)
	            .orElseThrow(() -> new RuntimeException("registro non trovato"));

	    for (StudenteEntity s : reg.getStudenti()) {
	    	s.setRegstudenti(null);    	    	
	    }
	    repStudente.saveAll(reg.getStudenti());
	    
	   for (ProfessoreEntity prof : reg.getLista_prof())
	   {
		   prof.getRegistro().remove(reg);
	   }
	   reg.getLista_prof().clear();
	    
	    repository.delete(reg);
	    repository.flush();	
	  

	    return "redirect:/registro/home";
	}
	

}
	
