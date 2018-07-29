package com.github.olegik1719.godville.arena.common;

import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
public class SailParser {
    private static final SimpleDateFormat LOG_DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy hh:mm X");

    private String ID;            // ИД похода
    private Date sailDate;        // Дата похода
    private Integer influence;    // Количество Влияний
    private Integer escape;       // Количество побегов
    private Integer smallFish;    // Количество сундуков с рыб
    private Integer smallIceland; // Количество сундуков с островов
    private Integer bigFish;      // Количество кладов с рыб
    private Integer bigIceland;   // Количество кладов с островов
    private Integer smallOut;     // Сундуков вывезено
    private Integer bigOut;       // Кладов вывезено

    private Integer drown;        // Количество утонувших
    private Integer tugs;         // Количестов отбуксированных

    private Integer AllBig;       // Всего вывезено кладов

    public SailParser(String HTMLLog){
        Document fight = Jsoup.parse(HTMLLog);
        sailDate = getTime(fight);
        ID = getID(fight);

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

}
