package com.github.olegik1719.godville.arena;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.util.Map;

public class ParserTest {

    @Test
    public void getLeft() {
        ChronicGetter acg = new AnyChronicGetter();
        String chronicle = acg.getHtml("0amta10sp");
        Document fight = Jsoup.parse(chronicle);
        System.out.printf("%s%n", ArenaParser.getID(fight));
//        Map<String, String> left = Parser.getLeft(fight);
//        for (String key: left.keySet()) {
//            System.out.printf("%s: %s%n", key, left.get(key));
//        }
//        Map<String, String> right = Parser.getRight(fight);
//        for (String key: right.keySet()) {
//            System.out.printf("%s: %s%n", key, right.get(key));
//        }
    }

}