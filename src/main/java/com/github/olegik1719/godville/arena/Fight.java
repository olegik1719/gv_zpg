package com.github.olegik1719.godville.arena;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fight{
    private static final Pattern PATTERN_WINSLOSES = Pattern.compile("(\\d+) / (\\d+)");

    private static final String REGEXP_GOLD_1="золотой кирпич и (\\d+) (.+?)\\.";

    private static final Pattern PATTERN_GOLD_1 = Pattern.compile(REGEXP_GOLD_1);

    private static final String REGEXP_GOLD_2="на (\\d+) (.+?) и золотой кирпич";

    private static final Pattern PATTERN_GOLD_2 = Pattern.compile(REGEXP_GOLD_2);

    private static final SimpleDateFormat ERINOME_DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy hh:mm X");

    private static final String[] ZPG_BEGIN={"По обоюдному желанию богов поединок пройдет без их вмешательства."
            ,"Боги отложили пульты влияния и молча взирают с небес. Героям никто не помешает."};


    private String id;
    private Hero[] heroes = new Hero[2];
    private int turns;
    private int winner;
    private int money;
    //private String currency;
    private Date dateFight;
    private boolean isZPG;
    private boolean young;

    public Fight(String id) {
        this.id = id;
        try {
            String filePath = "res/log/"+id+".html";
            File logfile = new File(filePath);
            Document fight;
            if (!logfile.exists()) {
                fight = Jsoup.connect(Common.getLink(id)).get();
                try(FileWriter writer = new FileWriter(logfile,true))
                {
                    writer.write(fight.html() + '\n');
                }
                catch(IOException ex){
                    System.out.println(ex.getMessage());
                }
            }else {
                fight = Jsoup.parse(logfile, "UTF-8", "/");
            }
            if (fight.select("div.lastduelpl_f").first() == null) {
                throw new IOException("It's not log");
            }
            try {
                String date = fight.select("div.lastduelpl_f>div").first().text().substring(5);
                dateFight = ERINOME_DATE_FORMATTER.parse(date);
            }catch (ParseException exception){
                System.out.println(id);
                exception.printStackTrace();
            }
            for (int i = 0; i<2;i++)
                heroes[i] = new Hero(fight,i);
            winner = heroes[0].isWinner()?0:1;
            young = heroes[0].isYoung();
            turns = Integer.parseInt(fight.getElementById("turn_num").text());
            {
                String lastTurn = fight.select("[data-t$=\""+turns+"\"]").text();
//                System.out.println(id);
//                System.out.println(lastTurn);
                Matcher matcher = PATTERN_GOLD_1.matcher(lastTurn);
                if (matcher.find()) {
                    int sum = Integer.parseInt(matcher.group(1));
                    String currency = matcher.group(2);
                    money = Common.getMoney(sum,currency);
                }

                if (money == 0){
//                    System.out.println(lastTurn);
                    matcher = PATTERN_GOLD_2.matcher(lastTurn);
                    if (matcher.find()) {
                        int sum = Integer.parseInt(matcher.group(1));
                        String currency = matcher.group(2);
                        money = Common.getMoney(sum,currency);
                    }
                }

                if (money == 0){
                    System.out.println("Сумма опять ноль");
                }
            }
            Element bonus =
                    fight.select("[data-t$=\"1\"][style='border-bottom: 1px dashed #888888;']").first();
            String bonus_text = bonus != null ? bonus.text(): null;
            for (String zpg_phrase:ZPG_BEGIN) {
                    if (bonus_text != null && bonus_text.contains(zpg_phrase)){
                        isZPG = true;
                    }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage()+" " + id);
        }
    }

    public int getTurns(){
        return turns;
    }

    public String getId(){
        return id;
    }

    public Hero getWinner(){
        return heroes[winner];
    }

    public Hero getLoser(){
        return heroes[(winner+1)%2];
    }

    public int getMoney(){
        return money;
    }

    public Date getTime(){
        return dateFight;
    }

    public boolean isZPG() {
        return isZPG;
    }

    public boolean isYoung(){
        return young;
    }

    @Override
    public int hashCode(){
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Fight && this.id.equals(((Fight) o).id);
    }

    class Hero{
        private String godName;
        private String godLink;
        private boolean isWinner;
        private int heroID;
        private int wins;
        private int loses;
        private boolean young;

        @Override
        public int hashCode(){
            return godName.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Hero && this.godName.equals(((Hero) o).godName);
        }

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
            for (Element el: new_lines) {//
                if ((el.select("span.l_capt") != null)
                        &&(el.select("span.l_capt").text().contains("Побед / Поражений"))){
                    String winsLoses = el.select("span.field_content").text();
                    Matcher matcher = PATTERN_WINSLOSES.matcher(winsLoses);
                    if (matcher.find()) {
                        wins = Integer.parseInt(matcher.group(1));
                        loses = Integer.parseInt(matcher.group(2));
                    }
                }else{
                    if ((el.select("span.l_capt") != null)
                            &&(el.select("span.l_capt").text().contains("Кирпичей для храма"))){
                        young = true;
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

        public boolean isYoung() {
            return young;
        }
    }
}
