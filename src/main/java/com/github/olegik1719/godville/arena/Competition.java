package com.github.olegik1719.godville.arena;

import java.util.*;
import java.util.stream.Collectors;

public class Competition {
    private Map<String,Player> players; // <nikname;players>
    private Set<String> logs;

    public Competition(){
        players = new HashMap<>();
        logs = new HashSet<>();
    }

    public Competition addDuel(String id){
        logs.add(id);
        Parser fight = new Parser(id);
        players.computeIfAbsent(fight.getWinner().getGodName(), Player::new).addLog(fight);
        players.computeIfAbsent(fight.getWinner().getGodName(), Player::new).addLog(fight);
        return this;
    }

    public Competition addDuels(Collection<String> duels){
        for (String duel:duels){
            addDuel(duel);
        }
        return this;
    }

    public Collection<String> getGood(){
        return logs.stream().sorted().collect(Collectors.toList());
    }

    private int getZPGCount(){
        //players.keySet().stream().flatMap(key->players.get(key).getDuels().stream()).filter(Player.Duel::isZPG).count() / 2;
        return (int)players.keySet().stream()
                .flatMap(key->players.get(key).getDuels().stream())
                .distinct()
                .filter(Player.Duel::isZPG).count();
    }

    private int getYoungCount(){
        return (int)players.keySet().stream()
                .flatMap(key->players.get(key).getDuels().stream())
                .distinct()
                .filter(Player.Duel::isYoung).count();
    }

    private int getGodsCount(){
        return players.size();
    }

    private void getGods_Wins(){
        players.keySet().forEach(key->System.out.println("* " + key + ":пс -- " + players.get(key).getMaxWin() + "\n"));
    }

    private void getGods_lose(){
        players.keySet().forEach(key->System.out.println("* " + key + ":пс -- " + players.get(key).getMaxWin() + "\n"));
    }

    public String getResult(){
        String result = "bq. Немного статистики:\n\n* Всего логов получено:\t";
        //result += getSize() +"\n";
        result += getZPGCount() + "\n";
        result += "* Из них на дохраме:\t";
        result += getYoungCount() + "\n";
        result += "* Приняло участие богов:\t";
        result += getGodsCount() + "\n";
        getGods_lose();
        getGods_Wins();
        return result;
    }
}
