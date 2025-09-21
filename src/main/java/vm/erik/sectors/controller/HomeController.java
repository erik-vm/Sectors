package vm.erik.sectors.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping()
    public String home(Authentication authentication, Model model) {
        // Redirect to appropriate dashboard based on authentication
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return "redirect:/admin";
            } else {
                return "redirect:/user";
            }
        }

        // For unauthenticated users, ensure userRole is null
        model.addAttribute("userRole", null);
        return "home/index";
    }

}
