package com.urgent2k.exercice1.Model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="role")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="authority",nullable = false)
    private String authority;

    @OneToMany(
            mappedBy = "role",
            cascade = CascadeType.ALL
    )
    List<User> users=new ArrayList<>();
}
