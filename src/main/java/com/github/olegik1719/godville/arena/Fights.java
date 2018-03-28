package com.github.olegik1719.godville.arena;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Fights{
    private Collection<Fight> fights;
    public Fights(Collection<String> logs) {
        fights = logs.stream().map(Fight::new).collect(Collectors.toList());
    }

    public int getSize(){
        return fights.size();
    }

    public Collection<String> getGood(){
        return fights.stream().map(Fight::getId).collect(Collectors.toList());
    }

    public int getZPGCount(){
        return (int) fights.stream().filter(Fight::isZPG).count();
    }

    public int getYoungCount(){
        return (int) fights.stream().filter(Fight::isYoung).count();
    }

    public int getYoungMax(){
        return fights.stream().filter(Fight::isYoung).map(Fight::getMoney).max(Integer::compareTo).get();
    }
    public int getOldMax(){
        return fights.stream()
                .filter(((Predicate<Fight>)(Fight::isYoung)).negate())
                .map(Fight::getMoney)
                .max(Integer::compareTo).get();
    }

    public int getGodsCount(){
        //fights.stream().map()
        return  (int)fights.stream()
                    .flatMap(fight -> Arrays.asList(fight.getWinner(),fight.getLoser()).stream())
                .distinct().count();
    }

    public int getYoungGodsCount(){
        //fights.stream().map()
        return  (int)fights.stream().filter(Fight::isYoung)
                .flatMap(fight -> Arrays.asList(fight.getWinner(),fight.getLoser()).stream())
                .distinct().count();
    }

    //public

    public String getResult(){
        String result = "bq. Немного статистики:\n\n* Всего логов получено:\t";
        //result += getSize() +"\n";
        result += getZPGCount() + "\n";
        result += "* Из них на дохраме:\t";
        result += getYoungCount() + "\n";
        result += "* Приняло участие богов:\t";
        result += getGodsCount() + "\n";
        result += "* Из них на дохраме:\t";
        result += getYoungGodsCount() + "\n";
        return result;
    }
}
