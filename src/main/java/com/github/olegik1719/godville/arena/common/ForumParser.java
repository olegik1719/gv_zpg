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
            System.out.println(post.text());
            System.out.println("================");
        }
    }
}
