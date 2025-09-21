package vm.erik.sectors.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vm.erik.sectors.handler.HomeHandler;

@Controller
@RequestMapping("/")
public class HomeController {

    private final HomeHandler homeHandler;

    public HomeController(HomeHandler homeHandler) {
        this.homeHandler = homeHandler;
    }

    @GetMapping()
    public String home(Authentication authentication, Model model) {
        return homeHandler.handleHome(authentication, model);
    }
}
