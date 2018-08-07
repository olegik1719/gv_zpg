package com.github.olegik1719.godville.duels.oceanarium;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Oceanarium {

    private static final SimpleDateFormat FOR_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
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

        private Date parseDate(){
            try {
                return FOR_DATE_FORMATTER.parse(date);
            }catch (Exception e){
                System.out.println(date);
                return null;
            }
        }

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
//        System.out.println(linkToPost + " " + dateToPost + " " + idGod + " " + idLog);
        posts.putIfAbsent(idGod,new HashMap<>());
//        System.out.println(posts.get(idGod).size());
        posts.get(idGod).computeIfPresent(idLog,(l,p)->{if (p.post.compareTo(linkToPost)>0)
            return new Post(linkToPost, dateToPost);
        else return p;});
        posts.get(idGod).putIfAbsent(idLog,new Post(linkToPost,dateToPost));
    }

    public void addResult(String linkToPost, String dateToPost, String idGod, String nomination,String idLog){
        addLog(idGod,nomination,idLog);
        addPost(linkToPost,dateToPost,idGod,idLog);
    }

    public void addForumPage(String page){
        forumParser.parsePosts(page, this);
    }



    @SneakyThrows
    public String getResults(String delim){
        String result = results.keySet().stream() // Список участников
                .flatMap(s -> results.get(s).keySet().stream() // список логов
                        .map(t -> { SailParser sailParser = new SailParser(t,s);

                                    return results.get(s).get(t) + delim // номинация
                                            + s + delim // имя бога
                                            //+ results.get(s) + delim // лог?
                                            + posts.get(s).get(t).toString(delim) + delim
                                            + sailParser.toString(delim) + delim// результат
                                            + (posts.get(s).get(t).parseDate().getTime() - sailParser.getSailDate().getTime())/ (60 * 60 * 1000);
                                }
                        ))

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
