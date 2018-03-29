package com.github.olegik1719.godville.arena;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Competition {
    private Map<String,Player> players; // <nikname;players>
    private Set<String> logs;
    private static final String BEGIN_TIME ="26.03.2018 00:00 +03:00";
    private static final String END_TIME ="31.03.2018 00:59 +03:00";
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy hh:mm X");
    private static Date beginTime;
    private static Date endTime;

    static {
        try {
            beginTime = DATE_FORMATTER.parse(BEGIN_TIME);
            endTime = DATE_FORMATTER.parse(END_TIME);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Competition(){
        players = new HashMap<>();
        logs = new HashSet<>();
    }

    public Competition addDuel(String id){
        logs.add(id);
        Parser fight = new Parser(id);
        try {
            Date fightTime = fight.getTime();
            if (fightTime.after(beginTime)&&fightTime.before(endTime)) {
                players.computeIfAbsent(fight.getWinner().getGodName(), Player::new).addLog(fight);
                players.computeIfAbsent(fight.getWinner().getGodName(), Player::new).addLog(fight);
            }else {
                System.out.println(Common.getID(id) + " -- bad log");
            }

        }catch (RuntimeException e){
            System.out.println(id + " isn't log?");
        }
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
        //getGods_lose();
        //getGods_Wins();
        return result;
    }
}
