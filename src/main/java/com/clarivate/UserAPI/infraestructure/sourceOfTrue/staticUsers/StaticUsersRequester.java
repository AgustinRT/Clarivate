package com.clarivate.UserAPI.infraestructure.sourceOfTrue.staticUsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.clarivate.UserAPI.domain.model.User;
import com.clarivate.UserAPI.infraestructure.sourceOfTrue.dto.RandomUsersDTO;
import com.clarivate.UserAPI.infraestructure.sourceOfTrue.portOut.StaticUserPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
public class StaticUsersRequester implements StaticUserPort {

    public StaticUsersRequester() {
        if(staticUsers == null || staticUsers.isEmpty()){
            this.builderListUsers();
        }
    }

    private List<User> staticUsers;

    private void builderListUsers(){
        try {
            ObjectMapper parserObj = new ObjectMapper().registerModule(new JavaTimeModule());
            InputStream input = new ClassPathResource("sampleUsers.json").getInputStream();
            RandomUsersDTO tempSave = parserObj.readValue(input, RandomUsersDTO.class);
            this.staticUsers = tempSave.results();
        } catch (IOException ex) {
            System.getLogger(StaticUsersRequester.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @Override
    public List<User> getUsers(){
        return this.staticUsers;
    }

    @Override
    public List<User> getRandomSubList(Integer numberUsers){
        List<User> randomList = new ArrayList<>();
        Random randSeed = new Random();
        while(randomList.size() < numberUsers){
            int idxUse = randSeed.nextInt(0, (this.staticUsers.size()-1));
            randomList.add(this.staticUsers.get(idxUse));
        }
        return randomList;
    }
}
