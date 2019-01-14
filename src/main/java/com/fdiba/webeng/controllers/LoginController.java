package com.fdiba.webeng.controllers;

import com.fdiba.webeng.models.User;
import com.fdiba.webeng.models.UserRepository;
import com.fdiba.webeng.utilities.SHA256Helper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Controller
public class LoginController {
    private final UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @PostMapping(value = "/login", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ModelAndView getHomePage(@RequestParam Map<String, String> body, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView("login");
        SHA256Helper sha256Helper = new SHA256Helper();
        try {
            String userPassword = sha256Helper.inputPassHash(body.get("password"));
            User user = userRepository.findUserByUserName(body.get("username"));
            if (user != null && user.getUserPassword().equals(userPassword)) {

                Cookie userNameCookie = new Cookie("username", user.getUserName());
                userNameCookie.setPath("/");
                response.addCookie(userNameCookie);
                Cookie isLoggedInCookie = new Cookie("isloggedin", "true");
                isLoggedInCookie.setPath("/");
                response.addCookie(isLoggedInCookie);

                return new ModelAndView("redirect:/main");
            } else {
                modelAndView.addObject("error", "Wrong password or user doesn't exist, please try again...");
                return modelAndView;
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return modelAndView;
    }
}
