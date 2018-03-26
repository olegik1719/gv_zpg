package com.github.olegik1719.godville.gettext;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fight {
    private static final String ERINOME_PREFIX="https://gv.erinome.net/duels/log/";
    private static final String regexpGold="золотой кирпич и (\\d+) (.+)\\.";
    private static final Pattern pattern = Pattern.compile(regexpGold);
    private String id;
    private Document fight;
    private Hero[] heroes = new Hero[2];
    private int turns;
    private int money;
    private int winner;
    private String currency;


    public Fight(String url){
        id = url.substring(url.lastIndexOf('/')+1);
        try {
            fight = Jsoup.connect(ERINOME_PREFIX+id).get();
            for (int i = 0; i<2;i++)
                heroes[i] = new Hero(fight,i);
            winner = heroes[0].isWinner()?0:1;
            turns = Integer.parseInt(fight.getElementById("turn_num").text());
            {
                String lastTurn = fight.select("[data-t$=\""+turns+"\"]").text();
                Matcher matcher = pattern.matcher(lastTurn);
                if (matcher.find()) {
                    money = Integer.parseInt(matcher.group(1));
                    currency = matcher.group(2);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getHeroInfo(int num){
        return '\"'+heroes[num].getGodName()+"\":" + heroes[num].getGodLink() + (heroes[num].isWinner()? " win":" lose");
    }

    public String getGod1() {
        return getHeroInfo(0);
    }

    public String getGod2() {
        return getHeroInfo(1);
    }

    public int getTurns(){
        return turns;
    }

    class Hero{
        private String godName;
        private String godLink;
        private boolean isWinner;
        private int heroID;

        private Hero(Element fight, int ID){
            heroID = ID;
            //System.out.println(ID);
            Element hero = fight.getElementById("hero" + (heroID+1));
            Element info = hero.getElementById("hero" + (heroID+1) + "_info");
            Element god  = info.select("a").first();
            godLink = god.attr("href");
            godName = god.text();
            //System.out.println(heroID);
            Element health = hero.getElementById("hp0");
            isWinner = !(health != null?health.html().equals("1"):hero.getElementById("hp1").html().equals("1"));
        }

        public boolean isWinner(){
            return isWinner;
        }

        public String getGodName(){
            return godName;
        }

        public String getGodLink() {
            return godLink;
        }
    }
}
