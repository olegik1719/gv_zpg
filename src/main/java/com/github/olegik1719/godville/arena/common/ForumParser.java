package com.github.olegik1719.godville.arena.common;

import com.github.olegik1719.godville.arena.chronicgetters.ChronicGetter;
import com.github.olegik1719.godville.arena.chronicgetters.FileChronicGetter;
import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Pattern;

@Getter
public class ForumParser {

//    private Elements posts;
//    private Document page;

    public static void parsePosts(String saved){
        ChronicGetter getter = new FileChronicGetter("res/forum/", ".htm");
        Document page = Jsoup.parse(getter.getHtml(saved));
        Elements posts = page.select("tr[class$=\"post hentry\"]");
        Pattern godPattern = Pattern.compile("http.?://godville\\.net/gods/");
        Pattern logPattern = Pattern.compile("http.?://(?:godville|gv\\.erinome)\\.net/duels/log/");
        for (Element post:posts){
            //System.out.println(post.toString());
            String dateParse = "tr>td[class$=\"author vcard\"]>div[class$=\"post_info\"]>div[class$=\"date\"]";
            //System.out.println("Date: " + post.select(dateParse).text());
            String postParse = "tr>td[class$=\"body entry-content p_content\"]";
            Element postText =  post.select(postParse).first();
            //System.out.println(postText.text());
            //System.out.println(postText.toString());
            Elements links = postText.select("a[href]");
            //System.out.println(links.size());
            String idGog = null;

            for (Element link : links){
                String text  = link.text();
                //System.out.println(link.text());
                String linkNeeded = link.attr("href");
                //System.out.println(linkNeeded);
                if (godPattern.matcher(linkNeeded).find()){
                    idGog = text;
                    System.out.println("Это бог:");
                    System.out.println(idGog);
                    //System.out.println(link.toString());
                }else {
                    if (logPattern.matcher(linkNeeded).find()) {
                        System.out.println("Для бога '" + idGog + "' найдена хроника: " + linkNeeded);
                    } else {
                        System.out.println(link.toString());
                    }
                }
            }
            System.out.println("================");
            //return;
            //return;
        }
    }
}
