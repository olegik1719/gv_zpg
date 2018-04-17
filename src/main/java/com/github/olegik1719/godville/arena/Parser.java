package com.github.olegik1719.godville.arena;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Parser {

    private static final SimpleDateFormat LOG_DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy hh:mm X");

    public static Duel parseLog(String HTMLLog){
        Document fight = Jsoup.parse(HTMLLog);
        Date fightTime = getTime(fight);
        List<String>  turns = getTurns(fight);
        Map<String,String> leftBlock = getLeft(fight);
        Map<String,String> rightBlock = getRight(fight);
        String ID = getID(fight);
        return new Duel(fightTime,ID,leftBlock,turns,rightBlock);
    }

    private static List<String> getTurns(Document fight){
        //TODO Make normal getter for central block
        return new ArrayList<>();
    }

    private static Map<String,String> getHeroStats(Element hero){
        Map<String, String> heroStats = new HashMap<>();
        Elements heroInfo = hero.select("div.new_line");
        for (Element el : heroInfo) {
            if ((el.select(".l_capt") != null)
                    && (el.select(".field_content")!= null)) {
                heroStats.put(el.select(".l_capt").text(),el.select(".field_content").text());
            }
        }
        return heroStats;
    }

    private static Map<String,String> getLeft(Document fight){
        //TODO Make normal getter for blocks
        Element leftBlock = fight.getElementById("left_block");
        return getHeroStats(leftBlock);
    }

    private static Map<String,String> getRight(Document fight){
        //TODO Make normal getter for blocks
        Element rightBlock = fight.getElementById("right_block");
        return getHeroStats(rightBlock);
    }

    private static Date getTime(Document fight) {
        try {
            String date = fight.select("div.lastduelpl_f>div").first().text().substring(5);
            return LOG_DATE_FORMATTER.parse(date);
        } catch (ParseException exception) {
            throw new RuntimeException("It's not log");
        }
    }

    private static String getID(Document fight){
        //TODO Make normal getter for ID
        return "";
    }

}
