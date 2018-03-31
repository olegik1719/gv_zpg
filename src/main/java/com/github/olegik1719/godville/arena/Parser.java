package com.github.olegik1719.godville.arena;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static final Pattern PATTERN_WINSLOSES = Pattern.compile("(\\d+) / (\\d+)");

    private static final String REGEXP_GOLD_1 = "золотой кирпич и (\\d+) (.+?)\\.";

    private static final Pattern PATTERN_GOLD_1 = Pattern.compile(REGEXP_GOLD_1);

    private static final String REGEXP_GOLD_2 = "на (\\d+) (.+?) и золотой кирпич";

    private static final Pattern PATTERN_GOLD_2 = Pattern.compile(REGEXP_GOLD_2);

    private static final SimpleDateFormat ERINOME_DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy hh:mm X");

    private static final String[] ZPG_BEGIN = {"По обоюдному желанию богов поединок пройдет без их вмешательства."
            , "Боги отложили пульты влияния и молча взирают с небес. Героям никто не помешает."};


    private String id;
    private Hero[] heroes = new Hero[2];
    private int winner;
    private boolean young;
    private Document fight;

    public Parser(String id) {
        this.id = id;
        try {
            String filePath = "res/log/" + id + ".html";
            File logfile = new File(filePath);

            if (!logfile.exists()) {
                fight = Jsoup.connect(Common.getLink(id)).get();
                try (FileWriter writer = new FileWriter(logfile, true)) {
                    writer.write(fight.html() + '\n');
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            } else {
                fight = Jsoup.parse(logfile, "UTF-8", "/");
            }
            for (int i = 0; i < 2; i++)
                heroes[i] = new Hero(fight, i);
            winner = heroes[0].isWinner() ? 0 : 1;
            young = heroes[0].isYoung();
        } catch (IOException e) {
            System.out.println(e.getMessage() + " " + id);
        }
    }

    public int getTurns() {
        return Integer.parseInt(fight.getElementById("turn_num").text());
    }

    public String getId() {
        return id;
    }

    public Hero getWinner() {
        return heroes[winner];
    }

    public Hero getLoser() {
        return heroes[(winner + 1) % 2];
    }

    public int getMoney() {
        int money = 0;
        int turns = getTurns();
        String lastTurn = fight.select("[data-t$=\"" + turns + "\"]").text();
        Matcher matcher = PATTERN_GOLD_1.matcher(lastTurn);

        if (matcher.find()) {
            int sum = Integer.parseInt(matcher.group(1));
            String currency = matcher.group(2);
            money = Common.getMoney(sum, currency);
        }

        if (money == 0) {
            matcher = PATTERN_GOLD_2.matcher(lastTurn);
            if (matcher.find()) {
                int sum = Integer.parseInt(matcher.group(1));
                String currency = matcher.group(2);
                money = Common.getMoney(sum, currency);
            }
        }

        if (money == 0) {
            System.out.println("Сумма опять ноль");
        }
        return money;
    }

    public Date getTime() {
        try {
            String date = fight.select("div.lastduelpl_f>div").first().text().substring(5);
            return ERINOME_DATE_FORMATTER.parse(date);
        } catch (ParseException exception) {
            System.out.println(id);
            throw new RuntimeException("It's not log");
        }
    }

    public boolean isZPG() {
        Element bonus =
                fight.select("[data-t$=\"1\"][style='border-bottom: 1px dashed #888888;']").first();
        String bonus_text = bonus != null ? bonus.text() : null;
        for (String zpg_phrase : ZPG_BEGIN) {
            if (bonus_text != null && bonus_text.contains(zpg_phrase)) {
                return true;
            }
        }
        return false;
    }

    public boolean isYoung() {
        return young;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Parser && this.id.equals(((Parser) o).id);
    }

    class Hero {
        private String godName;
        private int heroID;

        @Override
        public int hashCode() {
            return godName.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Hero && this.godName.equals(((Hero) o).godName);
        }

        private Hero(Element fight, int ID) {
            heroID = ID;
            Element info = fight.getElementById("hero" + (heroID + 1) + "_info");
            Element god = info.select("a").first();
            godName = god.text();
            //System.out.println(id + " " + godName);
        }

        public boolean isWinner() {
            Element stats = fight.getElementById("hero" + (heroID + 1) + "_stats");
            Element health = stats.getElementById("hp0");
            Boolean isWinner = !(health != null ? health.html().equals("1") : stats.getElementById("hp1").html().equals("1"));
            return isWinner;
        }

        public String getGodName() {
            return godName;
        }

        public int getLoses() {
            Element stats = fight.getElementById("hero" + (heroID + 1) + "_stats");
            //Element health = stats.getElementById("hp0");
            //isWinner = !(health != null ? health.html().equals("1") : stats.getElementById("hp1").html().equals("1"));
            Elements new_lines = stats.select("div.new_line");
            int loses = -1;
            for (Element el : new_lines) {//
                if ((el.select("span.l_capt") != null)
                        && (el.select("span.l_capt").text().contains("Побед / Поражений"))) {
                    String winsLoses = el.select("span.field_content").text();
                    Matcher matcher = PATTERN_WINSLOSES.matcher(winsLoses);
                    if (matcher.find()) {
                        //wins = Integer.parseInt(matcher.group(1));
                        loses = Integer.parseInt(matcher.group(2));
                    }
                }
            }
            return loses;
        }

        public int getWins() {
            //int wins = -1;
            Element stats = fight.getElementById("hero" + (heroID + 1) + "_stats");
            Elements new_lines = stats.select("div.new_line");
            for (Element el : new_lines) {//
                if ((el.select("span.l_capt") != null)
                        && (el.select("span.l_capt").text().contains("Побед / Поражений"))) {
                    String winsLoses = el.select("span.field_content").text();
                    Matcher matcher = PATTERN_WINSLOSES.matcher(winsLoses);
                    if (matcher.find()) {
                        int wins = Integer.parseInt(matcher.group(1));
                        return wins;
                        //loses = Integer.parseInt(matcher.group(2));
                    }
                }
            }
            throw  new RuntimeException(id + " isn't log?");
        }

        public boolean isYoung() {
            Element stats = fight.getElementById("hero" + (heroID + 1) + "_stats");
            Elements new_lines = stats.select("div.new_line");
            for (Element el : new_lines) {//
                if ((el.select("span.l_capt") != null)
                        && (el.select("span.l_capt").text().contains("Кирпичей для храма"))) {
                    return true;
                }
            }
            return false;
        }
    }
}
