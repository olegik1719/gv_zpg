package com.github.olegik1719.godville.gettext;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class Fight {
    static final String ERINOME_PREFIX="https://gv.erinome.net/duels/log/";
    private String id;
    private String url;
    private boolean winnerTheFirst;
    private Document fight;
    private Element[] gods = new Element[2];


    public Fight(String url){
        this.url = url;
        try {
            fight = Jsoup.connect(url).get();
            for (int i = 0; i<2;i++)
                gods[i] = getHero(i+1);
            winnerTheFirst = !gods[0].getElementById("hp0").html().equals("1");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getHtml(){
        return fight.html();
    }

    private String getGods(int num){
        //Element left_block = fight.getElementById("left_block");
        Element hero = fight.getElementById("hero"+num);
        return hero.html();
    }

    private Element getHero(int num){
        return fight.getElementById("hero" + num);
    }

    private String getHeroInfo(int num){
        Element hero = fight.getElementById("hero" + num);
        Element info = hero.getElementById("hero" + num + "_info");
        Element god  = info.select("a").first();
        String godLink = god.attr("href");
        String godName = god.text();
        String health = hero.getElementById("hp" + (num-1)).html();
        if (!health.equals("1")) winnerTheFirst = num == 1;
        return "\"" + godName + "\":"+godLink + "\nhealth:" + health;
    }

    public String getGod1() {
        return getHeroInfo(1);
    }

    public String getGod2() {
        return getHeroInfo(2);
    }

    public boolean isWinnerTheFirst() {
        return winnerTheFirst;
    }

    class Hero{
        private String heroName;
        private String godName;
        private String godLink;
        private boolean isWinner;
        private int heroID;

        Hero(Element hero, int ID){
            heroID = ID;
            Element info = hero.getElementById("hero" + ID + "_info");
            Element god  = info.select("a").first();
            String godLink = god.attr("href");
            String godName = god.text();
            String health = hero.getElementById("hp" + (ID)).html();
            if (!health.equals("1")) winnerTheFirst = ID == 0;

        }
    }
}
