package com.github.olegik1719.godville.arena.common;

import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class SailParser {
    private static final SimpleDateFormat LOG_DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy hh:mm X");

    private String ID = "";            // ИД похода
    private Date sailDate;        // Дата похода
    private Integer influence;    // Количество Влияний
    private Integer escape;       // Количество побегов
    private Integer smallFish;    // Количество сундуков с рыб
    private Integer smallIceland; // Количество сундуков с островов
    private Integer bigFish;      // Количество кладов с рыб
    private Integer bigIceland;   // Количество кладов с островов
    private Integer smallOut;     // Сундуков вывезено
    private Integer bigOut;       // Кладов вывезено

    private Integer partNumber;   // Номер участника в походе
    private String  partHero;     // Герой участника в походе
    private String  partGod;      // Имя участника в походе

    private Integer drown;        // Количество утонувших
    private Integer tugs;         // Количестов отбуксированных

    private Integer AllBig;       // Всего вывезено кладов
    private Document marine;      // Документ Jsoup с хроникой

    public SailParser(String HTMLLog, String god){
        marine = Jsoup.parse(HTMLLog);
        sailDate = getTime(marine);

        // set ID
        Elements lastDuel = marine.select(".lastduelpl");
        for (Element ld: lastDuel){
            String url = ld.select("a").first().attr("href");
            if (url.length()>0 && (ID.length() < 1)){
                ID = url.substring(url.lastIndexOf('/') + 1);
            }
        }
        // set Hero, god, number
        partGod = god;
        String godHeroText = "(\\d)\\. (.+) / (.+)";
        Pattern godHeroPattern = Pattern.compile(godHeroText);
        for (int i = 1; i < 5 ; i++) {
            Elements Part = marine.select("div[id$=h_tbl] > div[class$=\"t_line saild_"+ i +"\"] > div[class$=c1]");
            Matcher matcher = godHeroPattern.matcher(Part.text());
            String result = marine.select("div[id$=h_tbl] > div[class$=\"t_line saild_"+ i +"\"] > div[class$=c2] > span[class$=ple]").text();
            if (matcher.find()) {
                if (god.equalsIgnoreCase(matcher.group(3))){
                    partNumber = Integer.parseInt(matcher.group(1));
                    partHero   = matcher.group(2);
                }
            }


            if (result.equalsIgnoreCase("утонул")){
                drown++;
            }

            if (result.text().equalsIgnoreCase("отбуксирован")){
                tugs++;
            }


        }
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

    public String getPart(){
        Elements lastDuel = marine.select(".t_line saild_1");
        System.out.println(lastDuel.text());
        return "";
    }



}
