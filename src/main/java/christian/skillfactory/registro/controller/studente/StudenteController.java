package christian.skillfactory.registro.controller.studente;



import java.net.Authenticator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import christian.skillfactory.registro.model.Account;
import christian.skillfactory.registro.model.Presenza;
import christian.skillfactory.registro.model.Sanzione;
import christian.skillfactory.registro.model.StudenteEntity;
import christian.skillfactory.registro.model.Voto;
import christian.skillfactory.registro.repository.StudenteRepository;
import jakarta.servlet.http.HttpSession;
/**
 * Controller for the Student dashboard.
 * Provides secure access for students to view their own grades, disciplinary records, and attendance.
 */
@Controller
@RequestMapping("/userStudente")
public class StudenteController {

	
	
	private final StudenteRepository repStudente;
	
	public StudenteController(StudenteRepository repStudente)
	{
		this.repStudente=repStudente;
	}
	
	/**
	 * Loads the homepage for the authenticated student.
	 */
	@GetMapping
	public String studenteHome(Model model, Authentication auth)
	
	{
		Account account = (Account) auth.getPrincipal();
		
		StudenteEntity studente = repStudente.findByAccountId(account.getId()).orElseThrow( () -> new RuntimeException("nessun account per questo studente"));
		
		model.addAttribute("studente",studente);
		
		return "studente/stud-home";
	}
	/**
	 * Retrieves and displays the list of grades for the authenticated student.
	 */
	
	@GetMapping("/mostra/voto")
	public String mostraVoto(Model model, Authentication auth)
	{
		Account acc = (Account) auth.getPrincipal();
		
		StudenteEntity stud = repStudente.findByAccountId(acc.getId()).orElseThrow(() -> new RuntimeException("studente non trovato in mostra voto") );
		List<Voto> voti = stud.getVoti();
		
		model.addAttribute("voti",voti);
		model.addAttribute("studente",stud);
		
		return "voto/voto-mostra";
		
	
	}
	/**
	 * Retrieves and displays the disciplinary record for the authenticated student.
	 */
	@GetMapping("/mostra/sanzione")
	public String mostraSanzione( Model model, Authentication auth)
	{
		Account acc = (Account)auth.getPrincipal();
		
		StudenteEntity stud = repStudente.findByAccountId(acc.getId()).orElseThrow( ()-> new RuntimeException("Nessun studente trovato, controllare mostra sanzione"));
		
		List<Sanzione> sanzione = stud.getListaSanzione();
		
		model.addAttribute("sanzione",sanzione);
		model.addAttribute("studente",stud);
		
		return "sanzione/sanzione-mostra";
		
	}
	
	/**
	 * Retrieves and displays the attendance record for the authenticated student.
	 */
	@GetMapping("/mostra/presenza")
	public String mostraPresenza (Model model, Authentication auth)
	{
		Account acc = (Account)auth.getPrincipal();
		
		StudenteEntity stud = repStudente.findByAccountId(acc.getId()).orElseThrow( ()-> new RuntimeException("Nessun studente trovato, controllare mostra sanzione"));
		String classe = stud.getRegstudenti().getNomeClasse();
		List<Presenza> presenze = stud.getListaPresenze();
		
		model.addAttribute("presenza",presenze);
		model.addAttribute("studente",stud);
		model.addAttribute("classe",classe);
		
		return "presenza/presenza-mostra";
	}
	
}
