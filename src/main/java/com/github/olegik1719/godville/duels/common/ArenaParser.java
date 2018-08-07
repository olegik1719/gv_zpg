package com.github.olegik1719.godville.duels.common;

import com.github.olegik1719.godville.duels.arena.Duel;
import com.github.olegik1719.godville.duels.arena.Participant;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ArenaParser {

    private static final SimpleDateFormat LOG_DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy hh:mm X");

    public static Duel parseLog(String HTMLLog){
        Document fight = Jsoup.parse(HTMLLog);
        Date fightTime = getTime(fight);
        List<Turn>  turns = getTurns(fight);
        Map<String,String> leftBlock = getLeft(fight);
        Participant leftHero = new Participant(leftBlock);
        Map<String,String> rightBlock = getRight(fight);
        Participant rightHero = new Participant(rightBlock);
        String ID = getID(fight);
        return new Duel(fightTime,ID,leftHero,turns,rightHero);
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

    public static Map<String,String> getLeft(Document fight){
        Element leftBlock = fight.getElementById("left_block");
        return getHeroStats(leftBlock);
    }

    private static Map<String,String> getRight(Document fight){
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
        Elements lastDuel = fight.select(".lastduelpl");

        for (Element ld: lastDuel){
            String url = ld.select("a").first().attr("href");
            if (url.length()>0) return url.substring(url.lastIndexOf('/')+1);
        }

        return "";
    }

    private static List<Turn> getTurns(Document fight){

        List<Turn> turnsList = new ArrayList<>();
        Element duelContent = fight.selectFirst(".d_content");
        Elements turns = duelContent.select("[data-t]");

        for (Element turn:turns){
            String turnNumber = turn.attr("data-t");
            int num = Integer.parseInt(turnNumber);
            Turn currentTurn;// = turnsList.get(num-1);
            if (turnsList.size() < num || (currentTurn = turnsList.get(num-1) ) == null) {
                currentTurn = new Turn(num, turn.selectFirst(".text_content").text());
                turnsList.add(num-1,currentTurn);
            }else {
                currentTurn.add(turn.selectFirst(".text_content").text());
            }
        }

        return turnsList;
    }

}
