package com.github.olegik1719.godville.arena;

/*
Общие:
Имя
Бог-хранитель
Пол
Возраст (???)
Девиз (???)
Характер (???)
Гильдия (???)

Данные:
Уровень (???)
Инвентарь (Заполненность) (???)
Здоровье (Сейчас и Всего) -- как вариант -- определение победы
Золота (Строкой) -- как вариант -- определение победы
Смертей
Побед / Поражений
Кирпичей для храма

Потом можно добавить инвентарь
 */

import lombok.AccessLevel;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class Participant {

    private static final Pattern PATTERN_TWO_INTs = Pattern.compile("(\\d+) / (\\d+)");
    private static final Pattern PATTERN_INT_PERCENT = Pattern.compile("(\\d+).(\\d)%");

    private String heroName;
    private String godName;
    private boolean male; //true -- male;
    private String age;
    private String motto;
    private String personality;
    private String guild;

    //private int[] fullLevel; //Level[0] and percents[1];
    private int level;
    @Getter(AccessLevel.PRIVATE) private int[] inventory; //Now[0] and Full[1];
    @Getter(AccessLevel.PRIVATE) private int[] health; //Now[0] and Full[1];
    private String capital; //
    private int deaths;
    @Getter(AccessLevel.PRIVATE) private int[] duelHistory; //wins[0] and loses[1]
    private int bricks;

    private List<String> trophies;

    public Participant(Map<String,String> block){
        heroName = block.get("Имя");
        godName = block.get("Бог-хранитель");
        male = block.get("Пол").equals("мужской");
        age = block.get("Возраст");
        motto = block.get("Девиз");
        personality = block.get("Характер");
        guild = block.get("Гильдия");

        level = Integer.parseInt(block.get("Уровень"));
        inventory = parseStringToInts(block.get("Инвентарь"));
        health = parseStringToInts(block.get("Здоровье"));
        capital = block.get("Золота");
        deaths = Integer.parseInt(block.get("Уровень"));
        duelHistory = parseStringToInts(block.get("Побед / Поражений"));
        bricks = parseBricks(block.get("Кирпичей для храма"));

    }

    private int[] parseStringToInts(String twoIntsViaSlash){
        int[] result = new int[2];
        Matcher matcher = PATTERN_TWO_INTs.matcher(twoIntsViaSlash);
        if (matcher.find()) {
            result[0] = Integer.parseInt(matcher.group(1));
            result[1] = Integer.parseInt(matcher.group(2));
        }
        return result;
    }

    private int parseBricks(String bricks){
        //84.1%
        if (bricks == null) return 0;
        int result = 0;
        Matcher matcher = PATTERN_INT_PERCENT.matcher(bricks);
        if (matcher.find()){
            result = Integer.parseInt(matcher.group(1)) * 10
                    + Integer.parseInt(matcher.group(2));
        }
        return result;
    }

    public boolean isWinner(){
        return health[0] > 1;
    }

    public int getWinsBefore(){
        return duelHistory[0];
    }

    public int getLosesBefore(){
        return duelHistory[1];
    }
}
