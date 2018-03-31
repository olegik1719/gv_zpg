package com.github.olegik1719.godville.arena;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Competition {
    private Map<String,Player> players; // <nikname;players>
    //private Set<Player> players;
    //private Set<String> logs;
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
        //logs = new HashSet<>();
    }

    public Competition addDuel(String id){
        //logs.add(id);
        Parser fight = new Parser(id);
        try {
            Date fightTime = fight.getTime();
            if (fightTime.after(beginTime)&&fightTime.before(endTime)) {
                players.computeIfAbsent(fight.getWinner().getGodName(), Player::new).addLog(fight);
                players.computeIfAbsent(fight.getLoser().getGodName(), Player::new).addLog(fight);
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
        return players.values().stream()
                .map(Player::getDuels)
                .flatMap(Collection::stream)
                .distinct().map(Player.Duel::getId).sorted()
                .collect(Collectors.toList());
    }

    private int getZPGCount(){
        return (int)players.values().stream()
                //.flatMap(key->players.get(key).getDuels().stream())
                .map(Player::getDuels)
                .flatMap(Collection::stream)
                .distinct()
                .filter(Player.Duel::isZPG).count();
    }

    private int getYoungCount(){
        return (int)players.values().stream()
                //.flatMap(key->players.get(key).getDuels().stream())
                .map(Player::getDuels)
                .flatMap(Collection::stream)
                .distinct()
                .filter(Player.Duel::isYoung).count();
    }

    private int getGodsCount(){
        return players.size();
    }

    private void getGods_Wins(){
        System.out.println("Max wins:");
        players.values().stream().filter(player -> player.getMaxWin() > 0)
                .sorted((o2, o1) -> (Integer.compare(o1.getMaxWin(),o2.getMaxWin())))
                .forEach(player->System.out.println("* " + player.getNikName() + ":пс -- " + player.getMaxWin()));
        //players.keySet().forEach(key->System.out.println("* " + key + ":пс -- " + players.get(key).getMaxWin()));
        System.out.println();
    }

    private void getGods_lose(){
        System.out.println("Max loses:");
        players.values().stream().filter(player -> player.getMaxLose() > 0)
                .sorted((o2, o1) -> (Integer.compare(o1.getMaxLose(),o2.getMaxLose())))
                .forEach(player->System.out.println("* \"" + player.getNikName() + "\":пс -- " + player.getMaxLose()));
        System.out.println();
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
