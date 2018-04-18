package com.github.olegik1719.godville.arena;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
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

    public Turn(int number, String first_phrase) {
        this.number = number;
        phrases = new ArrayList<>();
        phrases.add(first_phrase);
    }

    public Turn add(String phrase) {
        phrases.add(phrase);
        return this;
    }

    @Override
    public String toString() {
        return phrases.stream().map(s -> "" + number + ": " + s + ";\n").collect(Collectors.joining()) + "\n";
    }


}

