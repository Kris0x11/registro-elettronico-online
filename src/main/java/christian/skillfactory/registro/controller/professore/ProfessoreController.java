package christian.skillfactory.registro.controller.professore;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import christian.skillfactory.registro.model.Account;
import christian.skillfactory.registro.model.ProfessoreEntity;
import christian.skillfactory.registro.model.RegistroEntity;
import christian.skillfactory.registro.repository.ProfessoreRepository;
import christian.skillfactory.registro.repository.RegistroRepository;
import jakarta.servlet.http.HttpSession;

/**
 * Controller for the Professor dashboard.
 * Manages access to assigned class registers based on the logged-in user's profile.
 */
@Controller
@RequestMapping("/userProfessore")
public class ProfessoreController {

private final RegistroRepository repRegistro;
private final ProfessoreRepository repProf;

public ProfessoreController(RegistroRepository repRegistro,ProfessoreRepository repProf)
{
	this.repRegistro=repRegistro;
	this.repProf=repProf;
}

/**
 * Loads the homepage for the authenticated professor, 
 * displaying all registers assigned to them.
 */
@GetMapping
public String home(Model model, Authentication auth)
{
	
	Account acc= (Account) auth.getPrincipal();
	ProfessoreEntity prof = repProf.findByAccountId(acc.getId()).orElseThrow( ()-> new RuntimeException("nessu account per questo prf"));
	
	List<RegistroEntity> registri = repRegistro.findByListaProf(prof);
	
	model.addAttribute("registri",registri);
	
	
	return "prof/prof-home";
}
}
