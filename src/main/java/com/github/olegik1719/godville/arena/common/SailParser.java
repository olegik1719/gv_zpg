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

    public static String[] smallFishGet =
            {"послана на дно морское. %hero% находит сундучок. \uD83D\uDCE6$"};

    public static String[] smallIcelandGet =
            {"Не желая плавать с пустым трюмом, %hero% отбирает у туземцев сундучок с чем-то ценным. \uD83D\uDCE6$"};

    public static String[] bigFishGet =
            {"С последним ударом тварь обернулась прекрасной самкой, поблагодарила за спасение от колдовских чар и попросила её приютить. %hero% не прочь. ♀$",
             ".+ захлебнулась. %hero% выхватывает из морской пучины клад. \uD83D\uDCB0",
             ".+ идёт на дно, а %hero% — получать клад. \uD83D\uDCB0 "};

    public static String[] bigIcelandGet =
            {"Хотя надпись на камне гласит, что именно здесь зарыта собака, %hero% выкапывает только самца. ♂$",
             "Выкопав сундук и закопав свидетелей, %hero% уносит клад на ковчег. \uD83D\uDCB0$"};

    private String ID = "";            // ИД похода
    private Date sailDate;        // Дата похода
    private int influence;    // Количество Влияний
    private int escape;       // Количество побегов
    private int smallGetFish;    // Количество сундуков с рыб
    private int smallGetIceland; // Количество сундуков с островов
    private int bigGetFish;      // Количество кладов с рыб
    private int bigGetIceland;   // Количество кладов с островов
    private int smallOut;     // Сундуков вывезено
    private int bigOut;       // Кладов вывезено

    private int partNumber;   // Номер участника в походе
    private String  partHero;     // Герой участника в походе
    private String  partGod;      // Имя участника в походе

    private int drown;        // Количество утонувших
    private int tugs;         // Количестов отбуксированных

    private int allBig;       // Всего вывезено кладов
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
        Pattern outputPattern  = Pattern.compile("\\[(.)]\\[(.+)]");
        for (int i = 1; i < 5 ; i++) {
            Elements Part = marine.select("div[id$=h_tbl] > div[class$=\"t_line saild_"+ i +"\"] > div[class$=c1]");
            Matcher matcher = godHeroPattern.matcher(Part.text());
            boolean isPart = false;

            if (matcher.find()) {
                isPart = god.equalsIgnoreCase(matcher.group(3));
                if (isPart){
                    partNumber = Integer.parseInt(matcher.group(1));
                    partHero   = matcher.group(2);
                }
            }

            //Drowned & Tugs
            String result = marine.select("div[id$=h_tbl] > div[class$=\"t_line saild_"+ i +"\"] > div[class$=c2] > span[class$=ple]").text();
            if (result.equalsIgnoreCase("утонул")){
                drown++;
            }
            if (result.equalsIgnoreCase("отбуксирован")){
                tugs++;
            }

            String output = marine.select("div[id$=h_tbl] > div[class$=\"t_line saild_"+ i +"\"] > div[class$=c2] > div > span[id$=pl_"+i+"_c]").text();
            Matcher outputMatcher = outputPattern.matcher(output);
            if (outputMatcher.find()){
                for (int j = 1; j < 3; j++) {
                    switch (outputMatcher.group(j)) {
                        case "\uD83D\uDCE6":
                            if (isPart) smallOut++;
                            break;
                        case "♂":
                            if (isPart) bigOut++;
                            allBig++;
                            break;
                        case "♀":
                            if (isPart) bigOut++;
                            allBig++;
                            break;
                        case "\uD83D\uDCB0":
                            if (isPart) bigOut++;
                            allBig++;
                            break;
                    }
                }
            }
        }
        Pattern small   = Pattern.compile("\uD83D\uDCE6$");
        Pattern bag     = Pattern.compile("\uD83D\uDCB0$");
        Pattern male    = Pattern.compile("♂$");
        Pattern female  = Pattern.compile("♀$");
        for (int i = 0; i <= 100; i++) {
            Elements turns = marine.select("div[id$=fight_chronicle]>div[class$=\"afl block\"]>div[class$=\"d_content\"]>div[class$=\"new_line dtc t" + i + "  saild_"+ partNumber +"\"]");
            for (Element turn : turns) {
                String turnText = turn.text();
                //System.out.println(turnText);
                Matcher smMatch = small.matcher(turnText);
                Matcher smBag = bag.matcher(turnText);
                Matcher smMale = male.matcher(turnText);
                Matcher smFemale = female.matcher(turnText);
                int found = 0;
                if (smMatch.find()) {
                    for (String pat:smallFishGet){
                        if (found == 0) {
                            Pattern pattern = Pattern.compile(pat.replace("%hero%", partHero));
                            Matcher match   = pattern.matcher(turnText);
                            if (match.find()){
                                found++;
                                smallGetFish++;
                            }
                        }
                    }
                    if (found == 0) for (String pat:smallIcelandGet){
                        if (found == 0) {
                            Pattern pattern = Pattern.compile(pat.replace("%hero%", partHero));
                            Matcher match   = pattern.matcher(turnText);
                            if (match.find()){
                                found++;
                                smallGetIceland++;
                            }
                        }
                    }
                    if (found == 0) System.out.println(turnText);
                }
                if (smBag.find() || smMale.find() ||smFemale.find()) {
                    for (String pat:bigFishGet){
                        if (found == 0) {
                            Pattern pattern = Pattern.compile(pat.replace("%hero%", partHero));
                            Matcher match   = pattern.matcher(turnText);
                            if (match.find()){
                                found++;
                                bigGetFish++;
                            }
                        }
                    }
                    if (found == 0) for (String pat:bigIcelandGet){
                        if (found == 0) {
                            Pattern pattern = Pattern.compile(pat.replace("%hero%", partHero));
                            Matcher match   = pattern.matcher(turnText);
                            if (match.find()){
                                found++;
                                bigGetIceland++;
                            }
                        }
                    }
                    if (found == 0) System.out.println(turnText);
                }
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



}
