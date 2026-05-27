package christian.skillfactory.registro.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import christian.skillfactory.registro.model.Account;
import christian.skillfactory.registro.repository.AccountRepository;
import jakarta.servlet.http.HttpSession;

/**
 * Controller responsible for managing account-related views
 */
@Controller
@RequestMapping("/account")
public class AdminAccountController {
	
	
	private final AccountRepository repository;
	
	public AdminAccountController (AccountRepository repository)
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
	    model.addAttribute("account", new Account());
	    return "authentication/login";
	}


		
	
	

}
