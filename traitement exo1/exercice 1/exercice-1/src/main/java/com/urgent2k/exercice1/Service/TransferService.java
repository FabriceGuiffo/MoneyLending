package com.urgent2k.exercice1.Service;

import com.urgent2k.exercice1.DAL.TransferRepository;
import com.urgent2k.exercice1.Model.Exchange;
import com.urgent2k.exercice1.Model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransferService {
    @Autowired
    TransferRepository transferRepository;

    public Transfer savetransfert(Transfer transfer){
       return transferRepository.save(transfer);
    }

    public Exchange getanexchange(String sendercurrency, String receivercurrency, float Amount){
        String baseUrl= "https://v6.exchangerate-api.com/v6/a3431870b080c45d5e87cbfe/pair/";
        String completeUrl=baseUrl+sendercurrency+"/"+receivercurrency+"/"+Amount;
        RestTemplate httpclient = new RestTemplate();
        ResponseEntity<Exchange> response = httpclient.exchange(completeUrl, HttpMethod.GET,null,Exchange.class);

        return response.getBody();

    }


}


