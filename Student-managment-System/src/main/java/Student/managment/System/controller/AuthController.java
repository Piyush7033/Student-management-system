package Student.managment.System.controller;

import Student.managment.System.entity.User;
import Student.managment.System.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage(HttpSession session) {

        if (session.getAttribute("user") != null) {
            return "redirect:/";
        }

        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            model.addAttribute("error", "User not found");
            return "login";
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("error", "Invalid Password");
            return "login";
        }

        session.setAttribute("user", user);

        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerPage(HttpSession session) {

        if (session.getAttribute("user") != null) {
            return "redirect:/";
        }

        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                           Model model) {

        User existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser != null) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        userRepository.save(user);

        model.addAttribute("success",
                "Registration Successful. Please Login.");

        return "login";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login?logout=true";
    }
}