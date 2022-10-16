package com.urgent2k.exercice1.Controller;

import com.urgent2k.exercice1.DAL.UserRepository;
import com.urgent2k.exercice1.Model.Exchange;
import com.urgent2k.exercice1.Model.Transfer;
import com.urgent2k.exercice1.Model.User;
import com.urgent2k.exercice1.Service.MyUserDetailsService;
import com.urgent2k.exercice1.Service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("transfer")
public class TransferController {

    @Autowired
    MyUserDetailsService myUserDetailsService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TransferService transferService;


    @GetMapping("listuser")
    public String directorypage(Model model, Principal principal){
        //we want to return a list of all available users automatically excluding the current user
        User currentuser = userRepository.findByUsername(principal.getName());
        List<User> allusers = myUserDetailsService.allusers();
        allusers.remove(currentuser);
        model.addAttribute("users",allusers);
        return "transfer/listuser";
    }


    @GetMapping("{username}")
    public String transferpage(@PathVariable("username") String username,Principal principal, Model model){
        Transfer transfer = new Transfer();
        transfer.setSender(principal.getName());
        transfer.setReceiver(username);

        //other ops

        //first the ops to determine sender and receiver countries
        User sender= userRepository.findByUsername(principal.getName());
        User receiver= userRepository.findByUsername(username);

        //let us determine user and receiver currencies
        Locale senderlocale=new Locale(sender.getLanguage(),sender.getCountry());
        Currency sendercurrency = Currency.getInstance(senderlocale);
        String sendercurrencyformat = sendercurrency.getDisplayName();

        Locale receiverlocale=new Locale(receiver.getLanguage(),receiver.getCountry());
        Currency receivercurrency = Currency.getInstance(receiverlocale);
        String receivercurrencyformat = receivercurrency.getDisplayName();


        model.addAttribute("transfer",transfer);
        //in order to be able to decide which currency would work i need to add an attribute in this page that would
        //be a checkbox or dropdown list
        model.addAttribute("sendercurrencyformat",sendercurrencyformat);
        model.addAttribute("receivercurrencyformat",receivercurrencyformat);

        return "transfer/operation";
    }

    @PostMapping("validation")
    public ModelAndView transfertreatment(@ModelAttribute Transfer transfer){
        //we start by verifying if there would be a currency conversion operation
        User sender=userRepository.findByUsername(transfer.getSender());
        User receiver=userRepository.findByUsername(transfer.getReceiver());
        Locale senderlocale=new Locale(sender.getLanguage(),sender.getCountry());
        Currency sendercurrency = Currency.getInstance(senderlocale);
        String sendercurrencyformat = sendercurrency.getDisplayName();
        String sendercurrencycode=sendercurrency.getCurrencyCode();

        String defcurrency=transfer.getCurrency();

        Locale receiverlocale=new Locale(receiver.getLanguage(),receiver.getCountry());
        Currency receivercurrency = Currency.getInstance(receiverlocale);
        String receivercurrencyformat = receivercurrency.getDisplayName();
        String receivercurrencycode=receivercurrency.getCurrencyCode();


        if(sendercurrencyformat.equalsIgnoreCase(defcurrency)){
            if(sender.getCountry().equals(receiver.getCountry())){
                //in this case we need no conversion the receiver and sender share the same nationality hence currency
                //we perform the changes and persist the users and the transaction
                //we check if the user has enough funds and if not we return him to the listuser page if he has we redirect him to his profile
                if(sender.getBalance()>= transfer.getAmount()){
                    sender.setBalance(sender.getBalance()- transfer.getAmount());
                    transfer=transferService.savetransfert(transfer);

                    List<Transfer> senderstransfers=sender.getTransfers();
                    senderstransfers.add(transfer);
                    sender.setTransfers(senderstransfers);
                    User updatedsender = myUserDetailsService.newUser(sender); //we use the repository save method which is supposed to make updates too
                    receiver.setBalance(receiver.getBalance()+ transfer.getAmount());
                    List<Transfer> receivertransfers=receiver.getTransfers();
                    receivertransfers.add(transfer);
                    receiver.setTransfers(receivertransfers);
                    myUserDetailsService.newUser(receiver);
                    //transferService.savetransfert(transfer);//just adding the operation to the transfer bdd just in case
                    ModelAndView mavprofile = new ModelAndView("redirect:/profile/index");
                    mavprofile.addObject("user",updatedsender);
                    return mavprofile; //add a success message?
                }
                else{
                    //the sender has insufficient funds he should receive a message upon redirection to listuser
                    ModelAndView userlistredirect= new ModelAndView("transfer/listuser");
                    userlistredirect.addObject("transactionfail",true);
                    userlistredirect.addObject("users",userRepository.findAll());
                    return userlistredirect;
                }
            }
            else{
                //in this case sender and receiver dont have the same country but the sender defined amount to be sent in his currency
                //hence there is some conversion to be done
                //we need to check whether his funds are enough
                if(sender.getBalance() < transfer.getAmount()){
                    //we redirect to listuser with message of failure
                    ModelAndView userlistredirect= new ModelAndView("transfer/listuser");
                    userlistredirect.addObject("transactionfail",true);
                    userlistredirect.addObject("users",userRepository.findAll());
                    return userlistredirect;
                }
                else{
                    //we need to convert the amount then perform 3tier persistence in each system
                    //we will be doing some rest calls to get the amount
                    sender.setBalance(sender.getBalance()- transfer.getAmount());
                    transfer=transferService.savetransfert(transfer);

                    List<Transfer> senderstransfers=sender.getTransfers();
                    senderstransfers.add(transfer);
                    sender.setTransfers(senderstransfers);
                    User updatedsender = myUserDetailsService.newUser(sender); //we use the repository save method which is supposed to make updates too

                    Exchange opconversion=transferService.getanexchange(sendercurrencycode,receivercurrencycode, transfer.getAmount());
                    float amountreceivercurrency= opconversion.getConversion_result();

                    receiver.setBalance(receiver.getBalance()+amountreceivercurrency);
                    List<Transfer> receivertransfers=receiver.getTransfers();
                    receivertransfers.add(transfer);
                    receiver.setTransfers(receivertransfers);
                    myUserDetailsService.newUser(receiver);
                    //transferService.savetransfert(transfer);//just adding the operation to the transfer bdd just in case
                    ModelAndView mavprofile = new ModelAndView("redirect:/profile/index");
                    mavprofile.addObject("user",updatedsender);
                    return mavprofile; //add a success message?
                }

            }
        }
        else{
            //the sender and the receiver are not of the same country and the sender specified amount in receiver currency
            //we set off by reconverting amount into senders currency in order to know if he has enough funds
            Exchange opconversion=transferService.getanexchange(receivercurrencycode,sendercurrencycode, transfer.getAmount());
            float amountsendercurrency= opconversion.getConversion_result();

            if(sender.getBalance()<amountsendercurrency){
                //we redirect to listuser with message of failure
                ModelAndView userlistredirect= new ModelAndView("transfer/listuser");
                userlistredirect.addObject("transactionfail",true);
                userlistredirect.addObject("users",userRepository.findAll());
                return userlistredirect;
            }
            else{
                //here the sender has enough so we simply do the persistence
                sender.setBalance(sender.getBalance()- amountsendercurrency);
                transfer=transferService.savetransfert(transfer);

                List<Transfer> senderstransfers=sender.getTransfers();
                senderstransfers.add(transfer);
                sender.setTransfers(senderstransfers);
                User updatedsender = myUserDetailsService.newUser(sender); //we use the repository save method which is supposed to make updates too

                receiver.setBalance(receiver.getBalance()+ transfer.getAmount());
                List<Transfer> receivertransfers=receiver.getTransfers();
                receivertransfers.add(transfer);
                receiver.setTransfers(receivertransfers);
                myUserDetailsService.newUser(receiver);
                //transferService.savetransfert(transfer);//just adding the operation to the transfer bdd just in case
                ModelAndView mavprofile = new ModelAndView("redirect:/profile/index");
                mavprofile.addObject("user",updatedsender);
                return mavprofile; //add a success message?

            }

        }

    }
}
