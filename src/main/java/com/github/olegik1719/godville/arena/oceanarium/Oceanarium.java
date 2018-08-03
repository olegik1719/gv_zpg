package com.github.olegik1719.godville.arena.oceanarium;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class Oceanarium {

    private HashMap<Particiant, ArrayList<String>> results = new HashMap<>(); // Участник -- посты с результатом
    private HashMap<String,String> dateOfPosts = new HashMap<>();   // post -- дата
    private HashMap<String,ArrayList<String>> resLog = new HashMap<>(); // res -- Список логов

    public void addLog(String idGod, String nomination, String linkToPost, String dateOfPost, String idLog){
        Particiant particiant = new Particiant(idGod,nomination);
        dateOfPosts.put(linkToPost,dateOfPost);
        resLog.putIfAbsent(linkToPost,new ArrayList<>()).add(idLog);
        results.putIfAbsent(particiant,new ArrayList<>()).add(linkToPost);
    }

    public ArrayList<String> getResults(){
        ArrayList<String> getRes= new ArrayList<>();
        //HashSet<Particiant> particiants = results.keySet();
        //results.keySet().stream().
        return new ArrayList<>();
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    private class Particiant{
        String idGog;
        String nomination;
    }

}
