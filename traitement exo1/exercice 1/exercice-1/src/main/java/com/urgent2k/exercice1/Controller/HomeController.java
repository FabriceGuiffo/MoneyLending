package com.urgent2k.exercice1.Controller;

import com.urgent2k.exercice1.Model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("index")
    public String landingpage(){
        return "index";
    }

    @GetMapping("login")
    public String loginpage(Model model){

        model.addAttribute("statuscheck",false);

        return "login";
    }

    @GetMapping("signup")
    public String signuppage(Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "signup";
    }

}
