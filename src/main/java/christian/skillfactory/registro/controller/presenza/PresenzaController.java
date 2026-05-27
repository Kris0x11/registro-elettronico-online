package christian.skillfactory.registro.controller.presenza;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import christian.skillfactory.registro.model.Presenza;
import christian.skillfactory.registro.model.RegistroEntity;
import christian.skillfactory.registro.model.StudenteEntity;
import christian.skillfactory.registro.repository.PresenzaRepository;
import christian.skillfactory.registro.repository.RegistroRepository;
import jakarta.transaction.Transactional;
/**
 * Controller for managing student attendance records.
 * Handles class roll calls and persistence of daily attendance status.
 */
@Controller
@RequestMapping("/presenza")
public class PresenzaController {
	
	
	private final PresenzaRepository repPresenza;
	
	private final RegistroRepository repReg;

	public PresenzaController(PresenzaRepository repPresenza,RegistroRepository repReg ) {
		super();
		this.repPresenza = repPresenza;
		this.repReg= repReg;
	}
	
	/**
	 * Retrieves the list of students for a specific class to perform the daily roll call.
	 */
	@GetMapping("/appello/{nomeClasse}")
	public String appello(@PathVariable String nomeClasse, Model model)
	{
		RegistroEntity reg = repReg.findById(nomeClasse).orElseThrow(()-> new RuntimeException("Errore in controller presenza"));
		List<StudenteEntity> studenti = reg.getStudenti();
		model.addAttribute("studenti",studenti);
		
		return "presenza/presenza-form";
	}
	/**
	 * Processes the roll call by creating attendance records for all students in the class.
	 * Marks students as present or absent based on the provided ID list.
	 */
	@PostMapping("/salva")
	@Transactional
	public String salvaAppello(
            @RequestParam String nomeClasse,
            @RequestParam(required = false) List<Long> studentiPresenti,
            RedirectAttributes redirectAttributes) {
			RegistroEntity reg = repReg.findById(nomeClasse).orElseThrow(()-> new RuntimeException("Errore in controller presenza"));
			List<StudenteEntity> studenti = reg.getStudenti();
			if (studentiPresenti == null) {
		        studentiPresenti = new ArrayList<>();
		    }
			for (StudenteEntity s : studenti)
			{
				Presenza p = new Presenza();
				p.setData(LocalDate.now());
	             p.setStudente(s);
			if (studentiPresenti.contains(s.getId()))
			{
	             p.setPresente(true);
			}
			else {p.setPresente(false);}
			repPresenza.save(p);
			}
			redirectAttributes.addFlashAttribute("messaggioSuccesso", "Presenze salvate con successo per la classe " + nomeClasse + "!");
            
       
        return "redirect:/presenza/appello/" + nomeClasse;
    }

}
