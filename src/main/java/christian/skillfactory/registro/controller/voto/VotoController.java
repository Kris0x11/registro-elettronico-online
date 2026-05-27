package christian.skillfactory.registro.controller.voto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
import christian.skillfactory.registro.model.Sanzione;
import christian.skillfactory.registro.model.StudenteEntity;
import christian.skillfactory.registro.model.Voto;
import christian.skillfactory.registro.repository.ProfessoreRepository;
import christian.skillfactory.registro.repository.StudenteRepository;
import christian.skillfactory.registro.repository.VotoRepository;
import jakarta.servlet.http.HttpSession;
/**
 * Controller for managing student grades.
 * Handles the assignment of new grades, listing grades for students, and updating existing records.
 */
@Controller
@RequestMapping("/voto")
public class VotoController {

	private final ProfessoreRepository repProf;
	private final StudenteRepository repStud;
	private final VotoRepository repVoto;
	
	public VotoController(ProfessoreRepository repProf, StudenteRepository repStud,  VotoRepository repVoto)
	{
		this.repStud=repStud;
		this.repProf= repProf;
		this.repVoto=repVoto;
	}
	
	/**
	 * Renders the form to insert a new grade for a specific student.
	 */
	
	@GetMapping("/nuovo/{idStudente}")
	public String aggiungiVoto(Model model, Authentication auth, @PathVariable Long idStudente)
	{
		Account acc= (Account)auth.getPrincipal();
		
		if (acc == null) return "redirect:/account/login";
		
		ProfessoreEntity professore = repProf.findByAccountId(acc.getId()).orElseThrow( ()-> new RuntimeException("prof non trovato"));
		StudenteEntity studente = repStud.findById(idStudente).orElseThrow(() -> new RuntimeException("Studente non trovato!"));

		
		model.addAttribute("professore",professore);
		model.addAttribute("stud", studente);
		
		Voto voto= new Voto();
		model.addAttribute("voto", voto);		
		
		return "voto/voto-inserimento";
	}
	/**
	 * Persists a new grade and redirects to the appropriate class register.
	 */
	
	@PostMapping("/salva")
	public String salvaVoto(@ModelAttribute("voto") Voto voto, @RequestParam Long studId) {
	    
	     

		StudenteEntity studente = repStud.findById(studId)
	            .orElseThrow(() -> new RuntimeException("Studente non trovato"));

	    voto.setVotoStudente(studente);
	    repVoto.save(voto);

	    String idSt = URLEncoder.encode(
	        studente.getRegstudenti().getNomeClasse(),
	        StandardCharsets.UTF_8
	    );

	    return "redirect:/registroProf/mostraRegistro/" + idSt;
		
}
	/**
	 * Displays the list of grades for a specific student, with optional teacher-specific actions.
	 */
	@GetMapping("/visualizzaVotiStudente/{idStudente}")
	public String visualizzaVotiStudente(Model model, @PathVariable Long idStudente, Authentication auth)
	{
		Account acc = (Account) auth.getPrincipal();
		boolean prof = acc.getRuolo().equals("PROF");
		StudenteEntity studente = repStud.findById(idStudente).orElseThrow(() -> new RuntimeException("errore in visualizzavotistudente endpoint - studente non trovato"));
		List<Voto> voti = studente.getVoti();
		model.addAttribute("isProf",prof);
		model.addAttribute("voti",voti);
		model.addAttribute("studente",studente);
		return "voto/voto-mostra";
	}
	
	/**
	 * Updates existing grade details using an inline patch approach.
	 */
    @PutMapping("/{id}")
    @ResponseBody
    public Map<String,String> modifica(@PathVariable Long id,
                           @RequestBody Map<String,Object> payload) {

       Voto voto = repVoto.findById(id).orElse(null);
        

        if (payload.containsKey("votazione"))
        	{
        	String votazione= payload.get("votazione").toString();
        	voto.setVoto(Double.parseDouble(votazione));
        	}
        if (payload.containsKey("data")) 
            voto.setData(LocalDate.parse((String) payload.get("data")));

        repVoto.save(voto);

        return Map.of("status", "ok", "id", voto.getId().toString());
    }
}
