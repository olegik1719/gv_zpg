package com.github.olegik1719.godville.arena;

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

    public void getYoung(){
        for (Fight f:fights){
            if (f.isYoung()){
                System.out.printf("%s : %s \\ %s %d%n",f.getId(),f.getWinner().getGodName(), f.getLoser().getGodName(), f.getMoney());
            }
        }
    }

    public String getResult(){
        String result = "";
        result += getSize() +"\n";
        result += getZPGCount() + "\n";
        result += getYoungCount() + "\n";
        result += getYoungMax() + "\n";
        result += getOldMax() + "\n";
        return result;
    }
}
