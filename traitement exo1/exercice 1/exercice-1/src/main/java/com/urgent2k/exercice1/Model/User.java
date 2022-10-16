package com.urgent2k.exercice1.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Users")
@Getter
@Setter
public class User {

    /*@GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;*/
    @Id
    @Column(name="Username")
    @NotNull(message="You must enter at least your surname")
    @Size(min=3,max=25,message="the name is between 3-25 characters")
    private String username;

    @Column(name="Christian")
    private String Christianname;

    @Column(name="Country")
    @NotNull(message="you must provide a country")
    private String Country;

    @Column(name="Lang")
    @NotNull(message="country language is mandatory")
    private String Language;

    @Column(name="Balance")
    private float Balance=5000;

    @Column(name="Password")
    @NotNull(message="password is mandatory")
    private String password;

    @Column(name="Active")
    @NotNull(message="a state of activity")
    private boolean enabled;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="role_id")
    private Role role;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST,CascadeType.MERGE}
    )
    @JoinTable(
            name="Users_Transfer",
            joinColumns = @JoinColumn(name="Username"),
            inverseJoinColumns = @JoinColumn(name="transfer_Id")
    )
    private List<Transfer> Transfers = new ArrayList<Transfer>() ;
    //should I create a loaded (except Transfers) constructor?


    public User() {
    }

    public User(String name, String christianname, String country, String Language, String password) {
        this.username = name;
        Christianname = christianname;
        Country = country;
        this.password = password;
        this.enabled = true;
        this.Language=Language;
    }
}
