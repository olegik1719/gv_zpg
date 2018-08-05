package com.github.olegik1719.godville.arena.oceanarium;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class Oceanarium {

    private HashMap<String,HashMap<String,String>> results; // Участник Х (Лог Х Номинация)
    // Пост - результат
    private HashMap<Post, HashMap<String,String>> posts; // Пост X (Участник Х Лог)

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    public class Post{
        private String post;
        private String date;
    }

    public Oceanarium(){
        results = new HashMap<>();
        posts = new HashMap<>();
    }

    public void addLog(String idGod, String nomination, String idLog){
        HashMap<String,String> result = new HashMap<>();
        result.put(idLog, nomination);
        results.merge(idGod,result, (old, now) -> {old.putAll(now); return old;});
    }

    public void addPost(String linkToPost, String dateToPost, String idGod, String idLog){
        HashMap<String, String> result = new HashMap<>();
        result.put(idGod, idLog);
        posts.merge(new Post(linkToPost,dateToPost),result,(old,now)->{old.putAll(now); return old;});
    }

    public String getResults(){
        return null;
    }
}
