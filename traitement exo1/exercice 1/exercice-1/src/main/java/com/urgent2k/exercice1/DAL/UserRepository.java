package com.urgent2k.exercice1.DAL;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.urgent2k.exercice1.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    User findByUsername(String username);
}
