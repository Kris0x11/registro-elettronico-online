package christian.skillfactory.registro.controller.authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import christian.skillfactory.registro.repository.AccountRepository;

/**
 * Controller responsible for managing account-related views
 */
@Controller
@RequestMapping("/auth")
public class AuthController {
	
	
	private final AccountRepository repository;
	
	public AuthController (AccountRepository repository)
	{
		this.repository=repository;
	}
	
	/**
     * Displays the login page.
     * @param model Model to pass the empty Account object for form binding.
     * @return The path to the login HTML template.
     */
	@GetMapping("/login")
	public String showLoginForm(Model model) {
	   
	    return "authentication/login";
	}
}