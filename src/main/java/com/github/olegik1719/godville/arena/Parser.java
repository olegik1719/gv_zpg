package com.github.olegik1719.godville.arena;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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

    private static Map<String,String> getLeft(Document fight){
        //TODO Make normal getter for blocks
        return new HashMap<>();
    }

    private static Map<String,String> getRight(Document fight){
        //TODO Make normal getter for blocks
        return new HashMap<>();
    }

    private static Date getTime(Document fight) {

        /*
        <div class="lastduelpl_f">
            <div>
                Дата: 29.03.2018 16:02 +03:00
            </div>
            <div>
                Хроники хранятся по мере возможности.
            </div>
        </div>
         */
        try {
            String date = fight.select("div.lastduelpl_f>div").first().text().substring(5);
            return LOG_DATE_FORMATTER.parse(date);
        } catch (ParseException exception) {
            //System.out.println(fight);
            throw new RuntimeException("It's not log");
        }
    }

    private static String getID(Document fight){
        //TODO Make normal getter for ID
//            <div id="wrap">
//                <div id="page_wrapper">
//                    <div class="lastduelpl">
//                        <span><a href="/duels/log/0amta10sp">Арена</a></span>
//                    </div>
//                    ...
//                </div>
//                ...
//            </div>
//            ...
//        </body>
        //String link = fight.select("div.lastduelpl>span>")
        return "";
    }
}
