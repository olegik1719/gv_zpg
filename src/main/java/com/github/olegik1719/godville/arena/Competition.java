package com.github.olegik1719.godville.arena;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Competition {
    private Map<String, Player> players; // <nikname;players>
    //private ArrayList<Player> players;
    //private Set<Player> players;
    //private Set<String> logs;
    private static final String BEGIN_TIME = "26.03.2018 00:00 +03:00";
    private static final String END_TIME = "31.03.2018 00:59 +03:00";
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

    public Competition() {
        players = new HashMap<>();
        //players = new ArrayList<>();
        //logs = new HashSet<>();
    }

    public Competition addDuel(String id) {
        //logs.add(id);
        Parser fight = new Parser(id);
        try {
            Date fightTime = fight.getTime();
            if (fightTime.after(beginTime) && fightTime.before(endTime)) {
                //Player winner = null;
                //Player loser = null;
//                for(int i = 0; i < players.size()&&(winner==null||loser==null); i++) {
//                    if (players.get(i).getNikName().equals(fight.getWinner().getGodName())) {
//                        winner = players.get(i).addLog(fight);
//                    } else {
//                        if (players.get(i).getNikName().equals(fight.getLoser().getGodName())) {
//                            loser = players.get(i).addLog(fight);
//                        }
//                    }
//                }
                players.computeIfAbsent(fight.getWinner().getGodName(),Player::new).addLog(fight);
                players.computeIfAbsent(fight.getLoser().getGodName(),Player::new).addLog(fight);
            } else {
                System.out.println(Common.getID(id) + " -- bad log");
            }

        } catch (RuntimeException e) {
            System.out.println(id + " isn't log?");
        }
        return this;
    }

    public Competition addDuels(Collection<String> duels) {
        for (String duel : duels) {
            addDuel(duel);
        }
        return this;
    }

    public Collection<String> getGood() {
        return
                //Stream.iterate(0,n->n+1).limit(players.size()-1).map(i->players.get(i))
                players.values().stream()
                .map(Player::getDuels)
                .flatMap(Collection::stream)
                .distinct().map(Player.Duel::getId).sorted()
                .collect(Collectors.toList());
    }

    private int getZPGCount() {
        return (int)
                //Stream.iterate(0,n->n+1).limit(players.size()-1).map(i->players.get(i))
                players.values().stream()
                //.flatMap(key->players.get(key).getDuels().stream())
                .map(Player::getDuels)
                .flatMap(Collection::stream)
                .distinct()
                .filter(Player.Duel::isZPG).count();
    }

    private int getYoungCount() {
        return (int)
                //Stream.iterate(0,n->n+1).limit(players.size()-1).map(i->players.get(i))
                players.values().stream()
                .map(Player::getDuels)
                .flatMap(Collection::stream)
                .distinct()
                .filter(Player.Duel::isZPG)
                .filter(Player.Duel::isYoung).count();
    }

    private int getGodsCount() {
        return players.size();
    }


    private String getGods_old_lose() {
        return "Max loses Old:\n"
                +
                //Stream.iterate(0,n->n+1).limit(players.size()-1).map(i->players.get(i))
                players.values().stream()
                .sorted((o1, o2) -> (Integer.compare(
                        o2.getDuels().stream()
                                .filter(Player.Duel::isZPG)
                                .filter(((Predicate<Player.Duel>) Player.Duel::isYoung).negate())
                                .filter(((Predicate<Player.Duel>) Player.Duel::isWinner).negate())
                                .map(Player.Duel::getSum)
                                .max(Integer::compareTo)
                                .orElse(0),
                        o1.getDuels().stream()
                                .filter(Player.Duel::isZPG)
                                .filter(((Predicate<Player.Duel>) Player.Duel::isYoung).negate())
                                .filter(((Predicate<Player.Duel>) Player.Duel::isWinner).negate())
                                .map(Player.Duel::getSum)
                                .max(Integer::compareTo)
                                .orElse(0))))
                .limit(10)
                .map(player -> "* \"" + player.getNikName() + "\":пс -- " + player.getDuels().stream()
                        .filter(Player.Duel::isZPG)
                        .filter(((Predicate<Player.Duel>) Player.Duel::isYoung).negate())
                        .filter(((Predicate<Player.Duel>) Player.Duel::isWinner).negate())
                        .map(Player.Duel::getSum)
                        .max(Integer::compareTo)
                        .orElse(0) + "\n")
                .collect(Collectors.joining())+ "\n";
    }

    private String getGods_old_win() {
        return "Max wins Old:\n"
                +
                //players.stream.
                 players.values().stream()
                .sorted((o1, o2) -> (Integer.compare(
                        o2.getDuels().stream()
                                .filter(Player.Duel::isZPG)
                                .filter(((Predicate<Player.Duel>) Player.Duel::isYoung).negate())
                                .filter(Player.Duel::isWinner)
                                .map(Player.Duel::getSum)
                                .max(Integer::compareTo)
                                .orElse(0),
                        o1.getDuels().stream()
                                .filter(Player.Duel::isZPG)
                                .filter(((Predicate<Player.Duel>) Player.Duel::isYoung).negate())
                                .filter(Player.Duel::isWinner)
                                .map(Player.Duel::getSum)
                                .max(Integer::compareTo)
                                .orElse(0))))
                .limit(10)
                .map(player -> "* \"" + player.getNikName() + "\":пс -- " + player.getDuels().stream()
                        .filter(Player.Duel::isZPG)
                        .filter(((Predicate<Player.Duel>) Player.Duel::isYoung).negate())
                        .filter(Player.Duel::isWinner)
                        .map(Player.Duel::getSum)
                        .max(Integer::compareTo)
                        .orElse(0)
                        + "\n")
                .collect(Collectors.joining())+ "\n";
    }

    private String getGods_young_win() {
        return "Max wins Old:\n"
                +
                //Stream.iterate(0,n->n+1).limit(players.size()-1).map(i->players.get(i))
                players.values().stream()
                .sorted((o1, o2) -> (Integer.compare(
                        o2.getDuels().stream()
                                .filter(Player.Duel::isZPG)
                                .filter(Player.Duel::isYoung)
                                .filter(Player.Duel::isWinner)
                                .map(Player.Duel::getSum)
                                .max(Integer::compareTo)
                                .orElse(0),
                        o1.getDuels().stream()
                                .filter(Player.Duel::isZPG)
                                .filter(Player.Duel::isYoung)
                                .filter(Player.Duel::isWinner)
                                .map(Player.Duel::getSum)
                                .max(Integer::compareTo)
                                .orElse(0))))
                .limit(10)
                .map(player -> "* \"" + player.getNikName() + "\":пс -- " + player.getDuels().stream()
                        .filter(Player.Duel::isZPG)
                        .filter(Player.Duel::isYoung)
                        .filter(Player.Duel::isWinner)
                        .map(Player.Duel::getSum)
                        .max(Integer::compareTo)
                        .orElse(0)
                        + "\n")
                .collect(Collectors.joining())+ "\n";
    }

    private String getGods_young_lose() {
        return "Max loses:"
                + //Stream.iterate(0,n->n+1).limit(players.size()-1).map(i->players.get(i))
                 players.values().stream()
                .filter(player -> player.getMaxLose() > 0)
                .sorted((o2, o1) -> (Integer.compare(o1.getMaxLose(), o2.getMaxLose())))
                .limit(10)
                .map(player -> "* \"" + player.getNikName() + "\":пс -- " + player.getMaxLose() + "\n")
                .collect(Collectors.joining())+ "\n";
    }

    private String getGods_Duels_count() {
        return "Список богов упорядоченный по количеству дуэлей: \n"
                + players.values().stream()
                .sorted(((o2, o1) -> (Integer.compare(o1.getDuels().size(), o2.getDuels().size()))))
                .map(player -> "* \"" + player.getNikName() + "\":пс -- " + player.getDuels().size() + " боев;\n")
                .collect(Collectors.joining()) + "\n";
    }

    public String getResult() {
        return
                "bq. Немного статистики:\n\n* Всего логов получено:\t"
                        + getZPGCount() + "\n"
                        + "* Из них на дохраме:\t"
                        + getYoungCount() + "\n"
                        + "* Приняло участие богов:\t"
                        + getGodsCount() + "\n\n"
                        // + getGods_Duels_count() + "\n\n"
                        + getGods_young_lose()
                        + getGods_young_win()
                        + getGods_old_lose()
                        + getGods_old_win()
                        + "?????"
                ;
    }
}

