package com.github.olegik1719.godville.arena.common;

import com.github.olegik1719.godville.arena.chronicgetters.ChronicGetter;
import com.github.olegik1719.godville.arena.chronicgetters.FileChronicGetter;
import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Getter
public class ForumParser {

    private Elements posts;
    private Document page;

    public ForumParser(String saved){
        ChronicGetter getter = new FileChronicGetter("res/forum/", ".htm");
        page = Jsoup.parse(getter.getHtml(saved));
        posts = page.select("tr[class$=\"post hentry\"]");
        for (Element post:posts){
            //System.out.println(post.toString());
            String dateParse = "tr>td[class$=\"author vcard\"]>div[class$=\"post_info\"]>div[class$=\"date\"]";
            System.out.println("Date: " + post.select(dateParse).text());
            String postParse = "tr>td[class$=\"body entry-content p_content\"]";
            Element postText =  post.select(postParse).first();
            System.out.println(postText.text());
            System.out.println(postText.toString());
            Elements links = postText.select("*>a.href");
            System.out.println(links.size());

            for (Element link : links){
                System.out.println(link.text());
                System.out.println(link.toString());

            }
            System.out.println("================");
            return;
            //return;
        }
    }
}
