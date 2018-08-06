package com.github.olegik1719.godville.arena.oceanarium;

import com.github.olegik1719.godville.arena.DefaultIDCalculator;
import com.github.olegik1719.godville.arena.chronicgetters.ChronicGetter;
import com.github.olegik1719.godville.arena.chronicgetters.FileChronicGetter;
import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForumParser {

//    private Elements posts;
//    private Document page;
    //private int count = 50;
    private static Pattern godPattern = Pattern.compile("http.?://godville\\.net/gods/");
    //private static Pattern logPattern = Pattern.compile("http.?://(?:godville|gv\\.erinome)\\.net/duels/log/");
    private static Pattern idPostPtrn = Pattern.compile("post_(\\d*)-row");
    private static Pattern resultPtrn = Pattern.compile("Результат(.?) заплыв(а|ов)");
    private int     themeNumbr;
    @Getter
    private HashSet<String> brokenLinks = new HashSet<>();
    @Getter
    private HashSet<String> brokenPosts = new HashSet<>();

    public ForumParser(int thNumber){
        themeNumbr = thNumber;
    }

    public void parsePosts(String saved, Oceanarium oceanarium){
        ChronicGetter getter = new FileChronicGetter("res/forum/", ".htm");
        Document page = Jsoup.parse(getter.getHtml(saved));
        String pageNumParse = "span[class=\"current\"]";
        String pageNum = page.selectFirst(pageNumParse).text();
        Elements posts = page.select("tr[class$=\"post hentry\"]");
        int cnt = 0;
        for (Element post:posts){
        //    if (cnt <= count && cnt > min ) {
                parsePost(post, pageNum, oceanarium);
//            }//else  {return;}
//            cnt++;
        }
    }

    private void parsePost(Element post, String pageNum, Oceanarium oceanarium){
        String dateParse = "tr>td[class$=\"author vcard\"]>div[class$=\"post_info\"]>div[class$=\"date\"]> abbr[class$=\"updated\"]";
        String datePost  = post.select(dateParse).attr("title");//.text();
//        System.out.println(datePost);
        Matcher idPM = idPostPtrn.matcher(post.attr("id"));
        String idPost;
        if (idPM.find()) {
            idPost = idPM.group(1);
        }else{
            idPost = post.attr("id");
        }
        //System.out.println(idPost);
        String linkToPost = "https://godville.net/forums/show_topic/"+themeNumbr+"?post="+idPost;



        String postParse = "tr>td[class$=\"body entry-content p_content\"]";
        Element postText =  post.select(postParse).first();
        //Elements links = postText.select("a[href]");
        Elements children = postText.children();
        String idGog = null;
        String nomination = null;
        boolean searchResult = false;
        boolean existResult  = false;
        try {
            for (Element child : children) {
                //System.out.println(child.children().size());

                if (searchResult) {
                    Elements grandСhildren = child.children();
                    if (godPattern.matcher(grandСhildren.get(0).selectFirst("a[href]").attr("href")).find()){
                        idGog = grandСhildren.get(0).selectFirst("a[href]").text();
                        nomination = grandСhildren.get(1).text();
                        if(nomination.toLowerCase().lastIndexOf("ракон") > 0){
                            nomination = "Морские Драконы";
                        }else if (nomination.toLowerCase().lastIndexOf("лосос") > 0) {
                            nomination = "Лососи";
                        }else if(nomination.toLowerCase().lastIndexOf("дельфин") > 0){
                            nomination = "Дельфины";
                        }else if(nomination.toLowerCase().lastIndexOf("китов") > 0){
                            nomination = "Китовые акулы";
                        }else if(nomination.toLowerCase().lastIndexOf("акул") > 0){
                            nomination = "Акулы";
                        }else if(nomination.toLowerCase().lastIndexOf("косатк") > 0){
                            nomination = "Косатки";
                        }
                        Elements logLinks = grandСhildren.get(2).select("a[href]");
                        for (Element logLink : logLinks) {
                            String id = DefaultIDCalculator.getID(logLink.attr("href"));
                            oceanarium.addResult(linkToPost,datePost,idGog,nomination,id);
                            existResult  = true;
                        }

                        searchResult = false;
                    }else {
                        //System.out.println(postText.toString());
                        throw new RuntimeException("Not found Link to result");
                    }
                }
                if (resultPtrn.matcher(child.text()).find()){
                    searchResult = true;
                }
            }
        } catch (Exception e){
            brokenLinks.add(linkToPost + " | " + e.getMessage());
            brokenPosts.add(idPost);
            existResult = true;
        }
        if (!existResult){
            brokenLinks.add(linkToPost + " | " + "No ress");
            brokenPosts.add(idPost);
        }
    }


}
