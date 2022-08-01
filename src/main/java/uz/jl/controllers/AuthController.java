package uz.jl.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.jl.configs.security.SecurityContextHolder;
import uz.jl.dto.LoginDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final SecurityContextHolder contextHolder;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDTO dto, HttpServletResponse response) {
        try {
            contextHolder.authenticate(dto, response);
            return "redirect:/";
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return "redirect:/auth/login";
    }


}
