package com.github.olegik1719.godville.arena;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Parser {

    private static final SimpleDateFormat LOG_DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy hh:mm X");

    public static Duel parseLog(String HTMLLog){
        Document fight = Jsoup.parse(HTMLLog);
        Date fightTime = getTime(fight);
        //String ID = getID(fight);
        return null;
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

//    private static String getID(Document fight){
////            <div id="wrap">
////                <div id="page_wrapper">
////                    <div class="lastduelpl">
////                        <span><a href="/duels/log/0amta10sp">Арена</a></span>
////                    </div>
////                    ...
////                </div>
////                ...
////            </div>
////            ...
////        </body>
//        String link = fight.select("div.lastduelpl>span>")
//        return "";
//    }
}
