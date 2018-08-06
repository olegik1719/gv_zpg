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
    //private HashMap<Post, HashMap<String,HashSet<String>>> posts; // Пост X (Участник Х Лог)

    private HashMap<String,HashMap<String,Post>> posts;// Участник Х (Лог Х Пост)

    private ForumParser forumParser = new ForumParser(3638);

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    public class Post{
        private String post;
        private String date;

        @Override
        public String toString() {
            return toString("|");
        }

        public String toString(String delim) {
            return post + delim + date;
        }
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
        System.out.println(linkToPost + " " + dateToPost + " " + idGod + " " + idLog);
        posts.putIfAbsent(idGod,new HashMap<>());
        System.out.println(posts.get(idGod).size());
        posts.get(idGod).computeIfPresent(idLog,(l,p)->{if (p.post.compareTo(linkToPost)<0)
            return new Post(linkToPost, dateToPost);
        else return p;});
        posts.get(idGod).putIfAbsent(idLog,new Post(linkToPost,dateToPost));
        System.out.println(posts.get(idGod).get(idLog));

        //posts.merge(idGod,result,(old,now) -> {old.get(idLog).addAll(now.get(idLog));return old;});
        //posts.merge(idGod,result,(old,now) -> {now.merge(idLog,post,(s1,s2)->{s2.addAll(s1);return s2;}); return now;});
        //old.get(idGod),post,(s1,s2)->{s1.addAll(s2); return s1;})
    }

    public void addResult(String linkToPost, String dateToPost, String idGod, String nomination,String idLog){
        addLog(idGod,nomination,idLog);
        addPost(linkToPost,dateToPost,idGod,idLog);
    }

    public void addForumPage(String page){
        forumParser.parsePosts(page, this);
    }

    public String getResults(String delim){
        String result = results.keySet().stream() // Список участников
                .flatMap(s -> results.get(s).keySet().stream() // список логов
                        .map(t -> results.get(s).get(t) + delim // номинация
                                + s + delim // имя бога
                                //+ results.get(s) + delim // лог?
                                + posts.get(s).get(t).toString(delim) + delim
                                +  SailParser.justCalculateLog(t,s, delim) )) // результат
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
