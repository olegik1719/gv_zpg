package com.github.olegik1719.godville.arena.oceanarium;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

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

//    private String getGodsResult(String idGod, char delim){
//        results.get(idGod).keySet().stream().map(s-> {
//            String result =
//        }
//        } )
//        return null;
//    }

    public String getResults(String delim){
        //results.keySet().stream().sorted().flatMap(s -> results.get(s).keySet().stream().map(t -> t+delim+s))
        String result = results.keySet().stream().flatMap(s -> results.get(s).keySet()
                .stream().map(t -> SailParser.justCalculateLog(t,s, delim) )).sorted().collect(Collectors.joining("\n"));
        return result;
    }

    private class Record{
        String idGod;
        String result;
    }
}
