package christian.skillfactory.registro.controller.professore;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import christian.skillfactory.registro.model.RegistroEntity;
import christian.skillfactory.registro.model.StudenteEntity;
import christian.skillfactory.registro.repository.RegistroRepository;
import christian.skillfactory.registro.repository.StudenteRepository;

/**
 * Controller for professors to manage and view their specific class registers.
 */
@Controller
@RequestMapping("/registroProf")
public class RegistroProfessoreController {
	
	private final RegistroRepository repRegistro;
	private final StudenteRepository repStudente;

	public RegistroProfessoreController(RegistroRepository repRegistro, StudenteRepository repStudente)
	{
		this.repRegistro=repRegistro;
		this.repStudente=repStudente;
	}
	/**
	 * Retrieves a paginated list of students belonging to a specific register
	 * and loads the register metadata for the view.
	 */
	@GetMapping("/mostraRegistro/{id}")
	public String mostraRegistro(@PathVariable String id,
	                             Model model,
	                             @RequestParam(defaultValue = "0") int pagina,
	                             @RequestParam(defaultValue = "5") int dimensione) {

	    Pageable pagination = PageRequest.of(pagina, dimensione);

	    Page<StudenteEntity> paginazioneStudenti =
	            repStudente.findByRegstudenti_NomeClasse(id, pagination);

	    RegistroEntity registro = repRegistro.findById(id).orElse(null);

	    model.addAttribute("registro", registro);
	    model.addAttribute("paginaStudenti", paginazioneStudenti);

	    return "prof/registro-aperto";
	}

	
	  
	

}
