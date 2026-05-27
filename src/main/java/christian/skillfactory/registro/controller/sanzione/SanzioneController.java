package christian.skillfactory.registro.controller.sanzione;

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
import christian.skillfactory.registro.model.RegistroEntity;
import christian.skillfactory.registro.model.Sanzione;
import christian.skillfactory.registro.model.StudenteEntity;
import christian.skillfactory.registro.model.Voto;
import christian.skillfactory.registro.repository.ProfessoreRepository;
import christian.skillfactory.registro.repository.RegistroRepository;
import christian.skillfactory.registro.repository.SanzioneRepository;
import christian.skillfactory.registro.repository.StudenteRepository;
/**
 * Controller for managing disciplinary sanctions.
 * Handles the creation, viewing, updating, and deletion of sanctions assigned to students.
 */
@Controller
@RequestMapping("/sanzione")
public class SanzioneController {
	
	

	private final ProfessoreRepository repProf;
	private final StudenteRepository repStud;
	private final RegistroRepository repReg;
	private final SanzioneRepository repSanzione;
	
	public SanzioneController(ProfessoreRepository repProf,StudenteRepository repStud, RegistroRepository repReg, SanzioneRepository repSanzione)
	{
		this.repProf=repProf;
		this.repStud=repStud;
		this.repReg=repReg;
		this.repSanzione=repSanzione;
	}
	
	/**
	 * Prepares the form to assign a new sanction to a student within a specific class.
	 */
	@GetMapping("prof/nuovaSanzione/{idClasse}")
	public String inserisciSanzione(Model model, Authentication auth, @PathVariable String idClasse)
	{
		Account acc = (Account) auth.getPrincipal();
		
		ProfessoreEntity prof = repProf.findByAccountId(acc.getId()).orElseThrow( () -> new RuntimeException("Prof non trovato in nuova sanzione"));
		RegistroEntity reg = repReg.findById(idClasse).orElseThrow( ()-> new RuntimeException("registro non trovato in nuova sanzione"));	
		List<StudenteEntity> listaStudenti = repStud.findByRegstudenti(reg);
		//model.addAttribute("prof",prof);
		System.out.println(listaStudenti);
		model.addAttribute("listaStudenti",listaStudenti);
		model.addAttribute("sanzione", new Sanzione());
		
		String classe = reg.getNomeClasse();
		model.addAttribute("classe",classe);
		
		return "sanzione/sanzione-form";
	
	}
	/**
	 * Persists a new sanction and redirects to the class register view.
	 */
	@PostMapping("prof/nuovaSanzione")
	public String salvaSanzione(@ModelAttribute Sanzione sanzione, @RequestParam String nomeClasse)
	{
		RegistroEntity reg = repReg.findById(nomeClasse).orElseThrow(()-> new RuntimeException("Errore in salva sanzione, causa registro"));
		sanzione.setRegistro(reg);
		repSanzione.save(sanzione);
		
		
		return "redirect:/registroProf/mostraRegistro/"+nomeClasse;
	}
	/**
	 * Displays the full list of sanctions assigned to a specific student.
	 */
	@GetMapping("prof/visualizzaSanzioneStudente/{idStudente}")
	public String visualizzaSanzioneStudente(Model model, @PathVariable Long idStudente)
	{
		StudenteEntity studente = repStud.findById(idStudente).orElseThrow(()-> new RuntimeException("Errore in visualizzaSanzioneStudente"));
		
		List<Sanzione> sanzione = studente.getListaSanzione();
		
		model.addAttribute("studente",studente);
		model.addAttribute("sanzione",sanzione);
		
		return "sanzione/sanzione-mostra";
		
	}
	
		
	/**
     * Updates sanction details using an inline patch approach.
     */   
	@PutMapping("/{id}")
    @ResponseBody
    public Map<String,String> modifica(@PathVariable Long id,
                           @RequestBody Map<String,Object> payload) {

       Sanzione sanzione = repSanzione.findById(id).orElse(null);
        

        if (payload.containsKey("descr")) sanzione.setDescrizione((String) payload.get("descr"));
        if (payload.containsKey("tipo")) sanzione.setTipo((String) payload.get("tipo"));
        if (payload.containsKey("data")) 
            sanzione.setData(LocalDate.parse((String) payload.get("data")));

        repSanzione.save(sanzione);

        return Map.of("status", "ok", "id", sanzione.getId().toString());
    }
	
	/**
     * Deletes a sanction record and cleans up the bidirectional relationship 
     * with the affected students.
     */
    @GetMapping("/elimina/{id}/{idStudente}")
    public String elimina(@PathVariable Long id, @PathVariable Long idStudente) {
    	
    	Sanzione sanzione = repSanzione.findById(id).orElseThrow(() -> new RuntimeException("Errore in elimina sanzione")); 
    	for (StudenteEntity studente : sanzione.getStudenti() )
    	{
    		studente.getListaSanzione().remove(sanzione);
    	}
    	sanzione.getStudenti().clear();
        repSanzione.deleteById(id);
        return "redirect:/sanzione/prof/visualizzaSanzioneStudente/" + idStudente ;
    }

}
