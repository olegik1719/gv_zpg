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

    private static String[] smallFishGet =
            {".+ послана на дно морское\\. %hero% находит сундучок\\. \uD83D\uDCE6$",
             "Освобождённая от груза .+ камнем падает на дно\\. %hero% находит ларец\\. \uD83D\uDCE6$"};

    private static String[] smallIcelandGet =
            {"Не желая плавать с пустым трюмом, %hero% отбирает у туземцев сундучок с чем-то ценным\\. \uD83D\uDCE6$"};

    private static String[] bigFishGet =
            {"С последним ударом тварь обернулась прекрасной самкой, поблагодарила за спасение от колдовских чар и попросила её приютить. %hero% не прочь\\. ♀$",
             "захлебнулась. %hero% выхватывает из морской пучины клад\\. \uD83D\uDCB0$",
             "идёт на дно, а %hero% — получать клад\\. \uD83D\uDCB0$",
             "%hero% аккуратно вытаскивает самку, стараясь не повредить ценный мех ржавым багром\\. ♀$",
             "%hero% помогает обнаруженной самке взобраться на ковчег, но .+\\. ♀$",
             "Поверженная .+ вытащена на палубу, и в её брюхе %hero% обнаруживает клад\\. \uD83D\uDCB0$"};

    private static String[] bigIcelandGet =
            {"Хотя надпись на камне гласит, что именно здесь зарыта собака, %hero% выкапывает только самца\\. ♂$",
             "Выкопав сундук и закопав свидетелей, %hero% уносит клад на ковчег. \uD83D\uDCB0$",
             "%hero% радостно заталкивает орущего что-то про дискриминацию самца в ковчег\\. ♂$",
             "Настрадавшийся от царящего на острове матриархата самец стремглав несётся на ковчег и запирается в трюме. %hero% не возражает\\. ♂$"};

    private static String[] escapes =
            {"%hero% берёт якорь в руки и плывёт подальше от противника\\.$"
            ,"%hero% хватает ноги в руки, но отпускает штурвал и остаётся на месте\\.$"
            ,"%hero% скрывается за поднявшейся волной и уходит из боя\\.$"
            ,"%hero% скручен приступом пацифизма и вынужден отступить\\.$"
            ,"%hero% понимает, что надо бежать, но позорить адмиральский мундир пока не готов\\.$"
            ,"%hero% поднимает все паруса и даже тельняшку, но не может выйти из боя\\.$"
            ,"%hero% пробует скрыться, но в море спрятаться сложно\\.$"
            ,"%hero% старается, но не может уйти от сражения\\.$"
            ,"%hero% выжимает из ковчега немыслимое число узлов и уносится прочь из боя\\.$"
            ,"%hero% чуть было не сломал штурвал, но от врага ушёл\\.$"};

    private static String[] empties =
            {"Среди текучих вод .+ белая плывёт\\.$",
            "Внутрия обменивает свою жизнь на провизию\\.$",
            "Ровную линию горизонта ломают .+\\.$",
            ".+ не смогла защитить подсказку. %hero% радуется наживе\\.$",
            "Депрессивная .+ тонет в собственной печали\\.$",
            "%hero% ценит поверженное чудище не только за внешность, но и за богатый внутренний жир\\.$",
            "По морю аки по суху прогуливается .+\\.$",
            "На горизонте обнаруживаются .+\\.$",
            "Взобравшись на гору, %hero% любуется открывшимися видами\\.$",
            "Вместе с водой поверженная .+ выплёскивает на борт ковчега провизию\\.$",
            "Поверженная .+ вытащена на палубу, и в её брюхе %hero% обнаруживает провизию\\.$",
            "Утопленная .+ обмену и возврату не подлежит\\.$",
            "Поблизости белугой воет .+\\.$",
            "Один водоворот засасывает ковчег капитана %hero%, а другой тут же высасывает\\.$",
            ".+ — из самой глубинки, а вон как поднялась\\.$",
            "%hero% предвкушает жирный куш и, предпочитая новые клады старым, выпихивает за борт .+\\.$",
            "%hero% заполнил ковчег добычей до отказа\\.$",
            "%hero% лихо кружится в водовороте и вывинчивается на другом краю карты\\.$",
            "Столкнувшись с чихающим кашалотом, %hero% c кораблём перемещается на другой конец карты\\.$",
            "Отрицание, гнев, торг, депрессия, принятие и, наконец, телепорт в другую локацию — %hero% прошёл через всё, побывав в водовороте\\.$",
            "Ощутив себя на своей волне, %hero% стремительно перемещается по карте\\.$",
            "Заперевшись в капитанской каюте, %hero% смело направляется в самое сердце водоворота\\.$",
            "%hero% что-то кричит, уцепившись за мачту, но водоворот равнодушно уносит ковчег на другой край карты\\.$",
            "%hero% обращает живущих здесь дикарей в свою веру и возносит вместе с ними молитву\\.$",
            "%hero% лихо кружится в водовороте и вывинчивается на другом краю карты\\.$",
            "Штопором ввернувшись в водоворот, %hero% со своим ковчегом пробкой вылетает на другой стороне моря\\.$",
            "%hero% что-то кричит, уцепившись за мачту, но водоворот равнодушно уносит ковчег на другой край карты\\.$",
            "%hero% предвкушает жирный куш и, предпочитая новые клады старым, выпихивает за борт клад\\.$",
            "%hero% заполнил ковчег добычей до отказа\\.$",
            ".+ выпотрошена и выброшена, %hero% находит самку\\.$"};

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

        // set Date
        try {
            String date = marine.select("div.lastduelpl_f>div").first().text().substring(5);
            sailDate = LOG_DATE_FORMATTER.parse(date);
        } catch (ParseException exception) {
            throw new RuntimeException("It's not log");
        }

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
        Pattern godHeroPattern = Pattern.compile("(\\d)\\. (.+) / (.+)");
        Pattern outputPattern  = Pattern.compile("\\[(.)]\\[(.+)]");
        // Parse Table
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

            //Set output
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
        // parse turns
        for (int i = 0; i <= 100; i++) {
            Elements turns = marine.select("div[id$=fight_chronicle]>div[class$=\"afl block\"]>div[class$=\"d_content\"]>div[class$=\"new_line dtc t" + i + "  saild_"+ partNumber +"\"]");
            for (Element turn : turns) {
                if (!(turn.select("div[class$=\"text_content opp_infl\"]").isEmpty()) || !(turn.select("div[class$=\"text_content infl\"]").isEmpty())) {
                    influence++;
                } else {
                    String turnText = turn.text();
                    int found = 0;
                    for (String pat : escapes) {
                        if (found == 0) {
                            Pattern pattern = Pattern.compile(pat.replace("%hero%", partHero));
                            Matcher match = pattern.matcher(turnText);
                            if (match.find()) {
                                found++;
                                escape++;
                            }
                        }
                    }
                    if (found == 0) {
                        Matcher smMatch = small.matcher(turnText);
                        Matcher smBag = bag.matcher(turnText);
                        Matcher smMale = male.matcher(turnText);
                        Matcher smFemale = female.matcher(turnText);
                        if (smMatch.find()) {
                            for (String pat : smallFishGet) {
                                if (found == 0) {
                                    Pattern pattern = Pattern.compile(pat.replace("%hero%", partHero));
                                    Matcher match = pattern.matcher(turnText);
                                    if (match.find()) {
                                        found++;
                                        smallGetFish++;
                                    }
                                }
                            }
                            if (found == 0) for (String pat : smallIcelandGet) {
                                if (found == 0) {
                                    Pattern pattern = Pattern.compile(pat.replace("%hero%", partHero));
                                    Matcher match = pattern.matcher(turnText);
                                    if (match.find()) {
                                        found++;
                                        smallGetIceland++;
                                    }
                                }
                            }
                            if (found == 0) System.out.println(turnText);
                        }
                        if (smBag.find() || smMale.find() || smFemale.find()) {
                            for (String pat : bigFishGet) {
                                if (found == 0) {
                                    Pattern pattern = Pattern.compile(pat.replace("%hero%", partHero));
                                    Matcher match = pattern.matcher(turnText);
                                    if (match.find()) {
                                        found++;
                                        bigGetFish++;
                                    }
                                }
                            }
                            if (found == 0) for (String pat : bigIcelandGet) {
                                if (found == 0) {
                                    Pattern pattern = Pattern.compile(pat.replace("%hero%", partHero));
                                    Matcher match = pattern.matcher(turnText);
                                    if (match.find()) {
                                        found++;
                                        bigGetIceland++;
                                    }
                                }
                            }
                            if (found == 0) System.out.println(turnText);
                        }
                    }
                    if (found == 0){
                        for (String pat : empties) {
                            if (found == 0) {
                                Pattern pattern = Pattern.compile(pat.replace("%hero%", partHero));
                                Matcher match = pattern.matcher(turnText);
                                if (match.find()) {
                                    found++;
                                }
                            }
                        }
                    }
                    if (found == 0){
                        System.out.println('"' + turnText.replace(partHero, "%hero%") + "$\",");
                    }
                }
            }
        }
    }
}
