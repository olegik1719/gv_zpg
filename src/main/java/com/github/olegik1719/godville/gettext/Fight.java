package com.github.olegik1719.godville.gettext;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fight {
    private static final String ERINOME_PREFIX="https://gv.erinome.net/duels/log/";

    private static final Pattern PATTERN_WINSLOSES = Pattern.compile("(\\d+) / (\\d+)");

    private static final String REGEXP_GOLD="золотой кирпич и (\\d+) (.+)\\.";

    private static final Pattern PATTERN_GOLD = Pattern.compile(REGEXP_GOLD);

    private static final SimpleDateFormat GV_DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy hh:mm");

    private static final String[] ZPG_BEGIN={"По обоюдному желанию богов поединок пройдет без их вмешательства."
            ,"Боги отложили пульты влияния и молча взирают с небес. Героям никто не помешает."};


    private String id;
    private Document fight;
    private Hero[] heroes = new Hero[2];
    private int turns;
    private int money;
    private int winner;
    private String currency;
    private Date dateFight;
    private boolean isZPG;

    public boolean equals(Object o){
        return id.equals(o);
    }

    public int hashCode(){
        return id.hashCode();
    }

    public Fight(String url) throws ParseException {
        id = url.substring(url.lastIndexOf('/')+1);
        try {
            fight = Jsoup.connect(ERINOME_PREFIX+id).get();
            dateFight = GV_DATE_FORMATTER.parse(fight.select("div.ft").first().text());
            for (int i = 0; i<2;i++)
                heroes[i] = new Hero(fight,i);
            winner = heroes[0].isWinner()?0:1;
            turns = Integer.parseInt(fight.getElementById("turn_num").text());
            {
                String lastTurn = fight.select("[data-t$=\""+turns+"\"]").text();
                Matcher matcher = PATTERN_GOLD.matcher(lastTurn);
                if (matcher.find()) {
                    money = Integer.parseInt(matcher.group(1));
                    currency = matcher.group(2);
                }
            }
            Element bonus =
                    fight.select("[data-t$=\"1\"][style='border-bottom: 1px dashed #888888;']").first();
            String bonus_text = bonus != null ? bonus.text(): null;
            for (String zpg_phrase:ZPG_BEGIN) {
                    if (bonus_text.contains(zpg_phrase)){
                        isZPG = true;
                    }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getTurns(){
        return turns;
    }

    public String getId(){
        return id;
    }

    public String getWinner(){
        return heroes[winner].godName;
    }

    public String getLoser(){
        return heroes[(winner+1)%2].godName;
    }

    public int getMoney(){
        return money;
    }

    public String getCurrency() {
        return currency;
    }

    public Date getTime(){
        return dateFight;
    }

    public boolean isZPG() {
        return isZPG;
    }

    class Hero{
        //private final Pattern PATTERN_WINSLOSES = Pattern.compile("(\\d+) / (\\d+)");
        private String godName;
        private String godLink;
        private boolean isWinner;
        private int heroID;
        private int wins;
        private int loses;

        private Hero(Element fight, int ID){
            heroID = ID;
            Element hero = fight.getElementById("hero" + (heroID+1));
            Element info = hero.getElementById("hero" + (heroID+1) + "_info");
            Element god  = info.select("a").first();
            godLink = god.attr("href");
            godName = god.text();
            Element stats = hero.getElementById("hero" + (heroID+1) + "_stats");
            Element health = stats.getElementById("hp0");
            isWinner = !(health != null?health.html().equals("1"):hero.getElementById("hp1").html().equals("1"));
            Elements new_lines = stats.select("div.new_line");
            for (Element el: new_lines) {
                if ((el.select("span.l_capt") != null)
                        &&(el.select("span.l_capt").text().equals("Побед / Поражений"))){
                    String winsLoses = el.select("span.field_content").text();
                    Matcher matcher = PATTERN_WINSLOSES.matcher(winsLoses);
                    if (matcher.find()) {
                        wins = Integer.parseInt(matcher.group(1));
                        loses = Integer.parseInt(matcher.group(2));
                    }
                }
            }
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

        public int getLoses() {
            return loses;
        }

        public int getWins() {
            return wins;
        }
    }
}
