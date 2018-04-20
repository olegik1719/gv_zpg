package com.github.olegik1719.godville.arena.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/* Абстракция. По факту(сейчас) достаточно: Золото(int) и ZPG(boolean)
Ход.
Ход представляет собой список _фраз_.
Каждая фраза:
1. Текст фразы.
2. Чье влияние
3. Смысл влияния.

1. Текст фразы -- строка
2. Чье влияние -- три варианта:
а. Ничье -- просто событие
б. Левого блока
в. Правого блока
3. Смысл влияния:
а. Ничего
б. Удачно/Нет
в. Количественная оценка.
Чье влияние: Определяется по тегам:
(цвет белый/синий/красный)
Смысл влияния:
Парсинг.
Что это может быть:
1. сумма, проигранная на арене
2. Тип прохождения влияния:
а. Глас: Лечение успешно
б. Глас: Лечение -> удар
в. Глас: Лечение -> обоюдное
г. Глас: Удар
д. Глас: Молитва
...
п. Влияние Плохо: обоюдка...
...
3. Тип арены (и данжа, как вариант)
а. ЗПГ
б. Тройка
в. Глушня
...
4. Ход:
а. Ход левого противника успешный
б. Ход левого противника пустой
в. Ход левого противника обоюдный
г. Ход левого противника сам себя.
д. Ход левого противника минус себе и плюс правому.
 */

public class Turn {
    private int number;
    private List<String> phrases;
    private int gold;
    private boolean ZPG;

    private static final String REGEXP_GOLD_1 = "золотой кирпич и (\\d+) (.+?)\\.";
    private static final Pattern PATTERN_GOLD_1 = Pattern.compile(REGEXP_GOLD_1);
    private static final String REGEXP_GOLD_2 = "на (\\d+) (.+?) и золотой кирпич";
    private static final Pattern PATTERN_GOLD_2 = Pattern.compile(REGEXP_GOLD_2);

    private static Map<String, Integer> exchange;
    static {
        exchange = new LinkedHashMap<>();
        exchange.put("монет",1);//золотые монеты
        exchange.put("монеты",1);
        exchange.put("монету",1);
        exchange.put("золотых монет",1);
        exchange.put("золотые монеты",1);
        exchange.put("золотых",1);
        exchange.put("золотой",1);
        exchange.put("золотую монету",1);
        exchange.put("золотых червонца",10);
        exchange.put("золотых червонцев",10);
    }

    private static final String[] ZPG_BEGIN = {"По обоюдному желанию богов поединок пройдет без их вмешательства."
            , "Боги отложили пульты влияния и молча взирают с небес. Героям никто не помешает."};

    public Turn(int number, String first_phrase) {
        this.number = number;
        phrases = new ArrayList<>();
        add(first_phrase);
    }

    public Turn add(String phrase) {
        phrases.add(phrase);
        if (gold == 0) gold = checkGold(phrase);
        if (!ZPG) ZPG = checkZPG(phrase);
        return this;
    }

    @Override
    public String toString() {
        return phrases.stream().map(s -> "" + number + ": " + s + ";\n").collect(Collectors.joining()) + "\n";
    }

    private int checkGold(String phrase) {
        Matcher matcher = PATTERN_GOLD_1.matcher(phrase);

        int money = 0;

        if (matcher.find()) {
            int sum = Integer.parseInt(matcher.group(1));
            String currency = matcher.group(2);
            money = getMoney(sum, currency);
        }

        if (money == 0) {
            matcher = PATTERN_GOLD_2.matcher(phrase);
            if (matcher.find()) {
                int sum = Integer.parseInt(matcher.group(1));
                String currency = matcher.group(2);
                money = getMoney(sum, currency);
            }
        }

        return money;
    }

    private static int getMoney(int sum, String currency){
        for (String cur:exchange.keySet()) {
            if (currency.equals(cur)) return sum * exchange.get(cur);
        }
        throw new RuntimeException(currency);
    }

    private boolean checkZPG(String phrase) {
        for (String zpg_phrase : ZPG_BEGIN) {
            if (phrase.contains(zpg_phrase)) {
                return true;
            }
        }
        return false;
    }

    public boolean isZPG(){
        return ZPG;
    }

    public int getGold(){
        return gold;
    }
}

