package com.urgent2k.exercice1.Controller;

import com.urgent2k.exercice1.DAL.UserRepository;
import com.urgent2k.exercice1.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Currency;
import java.util.Locale;

@Controller
@RequestMapping("profile")
public class ProfileController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("index")
    public String profilelanding(Model model, Principal principal){
        User user=userRepository.findByUsername(principal.getName());

        //let us find currency of this user so it can be displayed
        Locale userlocale=new Locale(user.getLanguage(),user.getCountry());
        Currency usercurrency = Currency.getInstance(userlocale);
        String usercurrencyformat = usercurrency.getDisplayName();

        model.addAttribute("user",user);
        model.addAttribute("currencyformat",usercurrencyformat);
        return "profile/index";
    }
}
