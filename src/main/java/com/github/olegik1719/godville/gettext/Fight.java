package com.github.olegik1719.godville.gettext;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class Fight {
    static final String ERINOME_PREFIX="https://gv.erinome.net/duels/log/";
    static final String regexpGold="золотой кирпич и (\\d+) (.+)\\.";
    private String id;
    private Document fight;
    private Hero[] heroes = new Hero[2];
    private int turns;


    public Fight(String url){
        //this.url = url;
        id = url.substring(url.lastIndexOf('/')+1);
        try {
            fight = Jsoup.connect(ERINOME_PREFIX+id).get();
            for (int i = 0; i<2;i++)
                heroes[i] = new Hero(fight,i);
            turns = Integer.parseInt(fight.getElementById("turn_num").text());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getHtml(){
        return fight.html();
    }

    private String getGods(int num){
        Element hero = fight.getElementById("hero"+num);
        return hero.html();
    }

//    private Element getHero(int num){
//        return fight.getElementById("hero" + num);
//    }

    private String getHeroInfo(int num){
        return '\"'+heroes[num].getGodName()+"\":" + heroes[num].getGodLink() + (heroes[num].isWinner()? " win":" lose");
    }

    public String getGod1() {
        return getHeroInfo(0);
    }

    public String getGod2() {
        return getHeroInfo(1);
    }

//    public boolean isWinnerTheFirst() {
//        return winnerTheFirst;
//    }

    public int getTurns(){
        return turns;
    }

    public String getLastTurn(){
        return fight.select("[data-t$=\""+24+"\"]").text();
    }

    class Hero{
        private String godName;
        private String godLink;
        private boolean isWinner;
        private int heroID;

        private Hero(Element fight, int ID){
            heroID = ID;
            Element hero = fight.getElementById("hero" + (heroID+1));
            Element info = hero.getElementById("hero" + (heroID+1) + "_info");
            Element god  = info.select("a").first();
            godLink = god.attr("href");
            godName = god.text();
            String health = hero.getElementById("hp" + (heroID)).html();
            isWinner = !health.equals("1");
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
