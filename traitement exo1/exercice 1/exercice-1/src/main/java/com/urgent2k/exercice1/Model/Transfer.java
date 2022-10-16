package com.urgent2k.exercice1.Model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="Transfer")
@Data
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transfer_Id;

    @Column(name="sendername")
    @NotNull(message="the sender's username is cumpolsory")
    @Length(min=3,max=25,message="the name is between 3-25 characters")
    private String Sender;

    @Column(name="receivername")
    @NotNull(message="the receiver's username is cumpolsory")
    @Length(min=3,max=25,message="the name is between 3-25 characters")
    private String Receiver;

    @Column(name="amount")
    @NotNull(message="a transfer must have an amount")
    private float Amount;

    @Column(name="mycurrency",nullable = false)
    private String Currency; //default is false

    @Column(name="message")
    @Size(max=100, message = "Just a 100char message to state your intent")
    private String Message;

    //methods

    public float convert(boolean mycurrency,float amount){
        return 0;
    }


}
