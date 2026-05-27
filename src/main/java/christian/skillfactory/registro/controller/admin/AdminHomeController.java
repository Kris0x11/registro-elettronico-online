package christian.skillfactory.registro.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * Controller for the administrative dashboard home page.
 */
@Controller
@RequestMapping("admin/home")
public class AdminHomeController {
	
	
	/**
     * Renders the main administration dashboard view.
     * @return The path to the admin home HTML template.
     */
	@GetMapping
	public String home()
	{
		return "admin/home";
	}

}
