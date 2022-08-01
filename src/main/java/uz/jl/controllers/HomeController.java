package uz.jl.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uz.jl.dto.User;

@Controller
public class HomeController {
    private final User user = new User("John", "Admin");

    @GetMapping
    private String homePage(Model model) {
        model.addAttribute("user", user);
        return "home";
    }
}
