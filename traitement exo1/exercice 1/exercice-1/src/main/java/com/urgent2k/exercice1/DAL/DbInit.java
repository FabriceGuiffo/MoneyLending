package com.urgent2k.exercice1.DAL;

import com.urgent2k.exercice1.Model.Role;
import com.urgent2k.exercice1.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbInit implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        List<User> userList= userRepository.findAll();
        if (userList.isEmpty()){
            //if the database is empty upon creation add a set of users
            User testuser1 = new User("Ethan","Hawke","Fr", "fr",passwordEncoder.encode("Hawke123"));
            User testuser2 = new User("Ivan","Mba","US","en_US",passwordEncoder.encode("urgent2k"));

            /*userList.add(testuser1);
            userList.add(testuser2);
            userRepository.saveAll(userList);

            Role userrole= new Role();
            userrole.setAuthority("USER");
            roleRepository.save(userrole);*/



            //we will try to do role association upon creation
            //NB the Role is the parent entity and user is the child in 1-n relationship
            //plus we have used cascadetype.all which includes type persist
            Role userrole= new Role();
            userrole.setAuthority("USER");
            testuser1.setRole(userrole);
            testuser2.setRole(userrole);
            userList.add(testuser1);
            userList.add(testuser2);
            userrole.setUsers(userList);
            roleRepository.save(userrole);
       
        }
    }
}
