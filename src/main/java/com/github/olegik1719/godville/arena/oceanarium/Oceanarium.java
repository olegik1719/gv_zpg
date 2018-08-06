package com.github.olegik1719.godville.arena.oceanarium;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Oceanarium {

    private HashMap<String,HashMap<String,String>> results; // Участник Х (Лог Х Номинация)
    // Пост - результат
    private HashMap<Post, HashMap<String,String>> posts; // Пост X (Участник Х Лог)

    private ForumParser forumParser = new ForumParser(3638);

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

    public void addResult(String linkToPost, String dateToPost, String idGod, String nomination,String idLog){
        addLog(idGod,nomination,idLog);
        addPost(linkToPost,dateToPost,idGod,idLog);
    }

    public void addForumPage(String page){
        forumParser.parsePosts(page, this);
    }

    public String getResults(String delim){
        String result = results.keySet().stream()
                .flatMap(s -> results.get(s).keySet().stream()
                        .map(t -> results.get(s).get(t) + delim +  SailParser.justCalculateLog(t,s, delim) ))
                .sorted().collect(Collectors.joining("\n"));
        return result;
    }

    public String getErrors(){
        String result = forumParser.getBrokenLinks().stream().sorted().collect(Collectors.joining("\n"));
        return result;
    }

    public ArrayList<String> getBroken(){
        return forumParser.getBrokenPosts().stream().sorted().collect(Collectors.toCollection(ArrayList::new));
    }
}
