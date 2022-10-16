package com.urgent2k.exercice1.Controller;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.urgent2k.exercice1.DAL.RoleRepository;
import com.urgent2k.exercice1.Model.Role;
import com.urgent2k.exercice1.Model.User;
import com.urgent2k.exercice1.Service.MyUserDetailsService;
import com.urgent2k.exercice1.Service.MyUserPrincipal;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.*;

@Controller
public class UserController {
    @Autowired
    MyUserDetailsService myUserDetailsService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/Users")
    public ModelAndView createuser(@ModelAttribute User user){
        //we start by checking if there is already a similar user in our BDD

        try {
            myUserDetailsService.loadUserByUsername(user.getUsername());
            //if we stay here then user already exists we need to redirect to the login page
            //we shall also inlcude a conditional message on this page saying the user already exists
            ModelAndView mav = new ModelAndView("login");

            mav.addObject("statuscheck",true);


            return mav;
        } catch (UsernameNotFoundException e) {
            //if we end here then the user does not exist we need to persist he/her then redirect to the profile page
            //by default we will have the userrole initialised to id 1
            Role userrole=roleRepository.findById(1).get();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setEnabled(true);
            user.setRole(userrole);
            List<User> completeusers= userrole.getUsers();
            completeusers.add(user);
            userrole.setUsers(completeusers);
            //BeanUtils.copyProperties(new MyUserPrincipal(user),new Principal());

            roleRepository.save(userrole); //persistence of new user

            //let us find currency of this user so it can be displayed
            Locale userlocale=new Locale(user.getLanguage(),user.getCountry());
            Currency usercurrency = Currency.getInstance(userlocale);
            String usercurrencyformat = usercurrency.getDisplayName();

            ModelAndView mav = new ModelAndView("redirect:/profile/index");
            mav.addObject("user",user);
            mav.addObject("newuser",true); //t display welcome message
            mav.addObject("currencyformat",usercurrencyformat);
            return mav;
        }

    }
}
