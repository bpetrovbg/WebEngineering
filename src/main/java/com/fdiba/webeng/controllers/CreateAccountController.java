package com.fdiba.webeng.controllers;

import com.fdiba.webeng.models.User;
import com.fdiba.webeng.models.UserRepository;
import com.fdiba.webeng.utilities.SHA256Helper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@Controller
public class CreateAccountController {
    private final UserRepository userRepository;

    public CreateAccountController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/createaccount")
    public String getCreateAccountPage() {
        return "createaccount";
    }

    @GetMapping("/createaccounterror")
    public @ResponseBody
    String getCreateAccountPageError() {
        return "This account already exists!";
    }

    @GetMapping(value = "/createaccounterrorreq")
    public @ResponseBody
    String getCreateAccountPageSettingsError() {
        return "Username or Password doesn't match the minimum requirements!";
    }

    @PostMapping(value = "/createaccount", consumes = "application/x-www-form-urlencoded")
    public String createNewAccount(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (userRepository.findUserByUserName(user.getUserName()) != null) {
            return "redirect:/createaccounterror";
        } else if (!(user.getUserName().trim().equals("") && user.getUserName().trim().isEmpty() && user.getUserPassword().trim().equals("")) && user.getUserPassword().trim().length() > 6) {
            SHA256Helper sha256Helper = new SHA256Helper();
            String hashedPass = sha256Helper.inputPassHash(user.getUserPassword());
            user.setUserPassword(hashedPass);
            userRepository.save(user);
            return "redirect:/login";
        } else {
            return "redirect:/createaccounterrorreq";
        }
    }
}
