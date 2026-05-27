package christian.skillfactory.registro.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import christian.skillfactory.registro.model.TitoloStudio;
import christian.skillfactory.registro.repository.TitoloStudioRepository;
import jakarta.transaction.Transactional;
/**
 * Controller for managing Title data
 */
@Controller
@RequestMapping("/titolo")
public class AdminTitoloStudioController {

  
    private final TitoloStudioRepository repository;

    public AdminTitoloStudioController(TitoloStudioRepository repository)
    {
    	this.repository = repository;
    }
    
    /**
     * Renders the form for creating a new educational qualification (Titolo di Studio).
     */
    @GetMapping("/nuovo")
    @Transactional
    public String nuovoTitolo(Model model) {
        model.addAttribute("titolo", new TitoloStudio());
        return "admin/titolo_studio-form";
    }

    /**
     * Persists a new educational qualification to the database.
     */
    @PostMapping("/salva")
    @Transactional
    public String salva(@ModelAttribute TitoloStudio titolo) {
        repository.save(titolo);
        return "redirect:/titolo/nuovo";
     
    
    }

}