package com.github.olegik1719.godville.arena;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Contest {
    //private Map<String, Player> players; // <nikname;players>
    private Map<String,Collection<Duel>> contest;

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

    public Contest (){
        contest = new HashMap<>();
    }

    public Contest addLog(String id){
        Parser fight = new Parser(id);
        try {
            Date fightTime = fight.getTime();
            if (fightTime.after(beginTime) && fightTime.before(endTime)) {
                String winner = fight.getWinner().getGodName();
                String loser = fight.getLoser().getGodName();
                contest.computeIfAbsent(winner,s -> new TreeSet<>()).add(new Duel(fight));
                contest.computeIfAbsent(loser,s -> new TreeSet<>()).add(new Duel(fight));
            } else {
                System.out.println(Common.getID(id) + " -- bad log");
            }

        } catch (RuntimeException e) {
            System.out.println(id + " isn't log?");
        }
        return this;
    }

    public Contest addLog(Collection<String> ids){
        ids.forEach(this::addLog);
        return this;
    }

    public Collection<String> getGood() {
        return contest.values().stream()
                .flatMap(Collection::stream).distinct()
                .map(Duel::getID)
                .collect(Collectors.toList());
    }

    private int getZPGCount() {
        return (int) contest.values()
                .stream()
                .flatMap(Collection::stream)
                .distinct()
                .filter(Duel::isZPG)
                .count()
                ;
    }

    private int getYoungCount() {
        return (int) contest.values()
                .stream()
                .flatMap(Collection::stream)
                .distinct()
                .filter(Duel::isYoung)
                .count();
    }

    private int getGodsCount() {
        return  contest.size();
        //players.size();
    }


    private int max_lose(String player,boolean isYoung){

        return contest.get(player)
                .stream()
                .filter(duel -> duel.getLoser().getGodName().equals(player))
                .filter(duel -> (duel.isYoung() == isYoung))
                .map(Duel::getSum)
                .max(Integer::compareTo).orElse(0)
                ;
    }

    private int max_win(String player,boolean isYoung){

        return contest.get(player)
                .stream()
                .filter(duel -> duel.getWinner().getGodName().equals(player))
                .filter(duel -> (duel.isYoung() == isYoung))
                .map(Duel::getSum)
                .max(Integer::compareTo).orElse(0)
                ;
    }

    private String getGods_max_lose(boolean isYoung) {
        //TODO
        return contest.keySet().stream()
                .sorted((o1, o2) -> Integer.compare(max_lose(o2,isYoung),max_lose(o1,isYoung)))
                .limit(10)
                .map(player -> "* \"" + player + "\":пс -- " + max_lose(player,isYoung) + ";\n")
                .collect(Collectors.joining()) + "\n";
        //return null;
//        "Max loses Old:\n"
//                +
//                //Stream.iterate(0,n->n+1).limit(players.size()-1).map(i->players.get(i))
//                players.values().stream()
//                        .sorted((o1, o2) -> (Integer.compare(
//                                o2.getDuels().stream()
//                                        .filter(Player.Duel::isZPG)
//                                        .filter(((Predicate<Player.Duel>) Player.Duel::isYoung).negate())
//                                        .filter(((Predicate<Player.Duel>) Player.Duel::isWinner).negate())
//                                        .map(Player.Duel::getSum)
//                                        .max(Integer::compareTo)
//                                        .orElse(0),
//                                o1.getDuels().stream()
//                                        .filter(Player.Duel::isZPG)
//                                        .filter(((Predicate<Player.Duel>) Player.Duel::isYoung).negate())
//                                        .filter(((Predicate<Player.Duel>) Player.Duel::isWinner).negate())
//                                        .map(Player.Duel::getSum)
//                                        .max(Integer::compareTo)
//                                        .orElse(0))))
//                        .limit(10)
//                        .map(player -> "* \"" + player.getNikName() + "\":пс -- " + player.getDuels().stream()
//                                .filter(Player.Duel::isZPG)
//                                .filter(((Predicate<Player.Duel>) Player.Duel::isYoung).negate())
//                                .filter(((Predicate<Player.Duel>) Player.Duel::isWinner).negate())
//                                .map(Player.Duel::getSum)
//                                .max(Integer::compareTo)
//                                .orElse(0) + "\n")
//                        .collect(Collectors.joining())+ "\n";
    }

    private String getGods_max_win(boolean isYoung) {
        //TODO
        return contest.keySet().stream()
                .sorted((o1, o2) -> Integer.compare(max_win(o2,isYoung),max_win(o1,isYoung)))
                .limit(10)
                .map(player -> "* \"" + player + "\":пс -- " + max_win(player,isYoung) + ";\n")
                .collect(Collectors.joining()) + "\n";
    }

    private String getGods_old_win() {
        return "bq. Самые большие выигрыши у храмовладельцев:\n\n"
                + getGods_max_win(false);
//        "Max wins Old:\n"
//                +
//                //players.stream.
//                players.values().stream()
//                        .sorted((o1, o2) -> (Integer.compare(
//                                o2.getDuels().stream()
//                                        .filter(Player.Duel::isZPG)
//                                        .filter(((Predicate<Player.Duel>) Player.Duel::isYoung).negate())
//                                        .filter(Player.Duel::isWinner)
//                                        .map(Player.Duel::getSum)
//                                        .max(Integer::compareTo)
//                                        .orElse(0),
//                                o1.getDuels().stream()
//                                        .filter(Player.Duel::isZPG)
//                                        .filter(((Predicate<Player.Duel>) Player.Duel::isYoung).negate())
//                                        .filter(Player.Duel::isWinner)
//                                        .map(Player.Duel::getSum)
//                                        .max(Integer::compareTo)
//                                        .orElse(0))))
//                        .limit(10)
//                        .map(player -> "* \"" + player.getNikName() + "\":пс -- " + player.getDuels().stream()
//                                .filter(Player.Duel::isZPG)
//                                .filter(((Predicate<Player.Duel>) Player.Duel::isYoung).negate())
//                                .filter(Player.Duel::isWinner)
//                                .map(Player.Duel::getSum)
//                                .max(Integer::compareTo)
//                                .orElse(0)
//                                + "\n")
//                        .collect(Collectors.joining())+ "\n";
    }

    private String getGods_young_win() {
        return "bq. Самые большие выигрыши у храмостроителей:\n\n"
                + getGods_max_win(true);
//        "Max wins Old:\n"
//                +
//                //Stream.iterate(0,n->n+1).limit(players.size()-1).map(i->players.get(i))
//                players.values().stream()
//                        .sorted((o1, o2) -> (Integer.compare(
//                                o2.getDuels().stream()
//                                        .filter(Player.Duel::isZPG)
//                                        .filter(Player.Duel::isYoung)
//                                        .filter(Player.Duel::isWinner)
//                                        .map(Player.Duel::getSum)
//                                        .max(Integer::compareTo)
//                                        .orElse(0),
//                                o1.getDuels().stream()
//                                        .filter(Player.Duel::isZPG)
//                                        .filter(Player.Duel::isYoung)
//                                        .filter(Player.Duel::isWinner)
//                                        .map(Player.Duel::getSum)
//                                        .max(Integer::compareTo)
//                                        .orElse(0))))
//                        .limit(10)
//                        .map(player -> "* \"" + player.getNikName() + "\":пс -- " + player.getDuels().stream()
//                                .filter(Player.Duel::isZPG)
//                                .filter(Player.Duel::isYoung)
//                                .filter(Player.Duel::isWinner)
//                                .map(Player.Duel::getSum)
//                                .max(Integer::compareTo)
//                                .orElse(0)
//                                + "\n")
//                        .collect(Collectors.joining())+ "\n";
    }

    private String getGods_old_lose(){
        return "bq. Самые большие проигрыши у храмовладельцев:\n\n"
                + getGods_max_lose(false);
    }

    private String getGods_young_lose() {
        return "bq. Самые большие проигрыши у храмостроителей:\n\n"
                + getGods_max_lose(true);
//        "Max loses:"
//                + //Stream.iterate(0,n->n+1).limit(players.size()-1).map(i->players.get(i))
//                players.values().stream()
//                        .filter(player -> player.getMaxLose() > 0)
//                        .sorted((o2, o1) -> (Integer.compare(o1.getMaxLose(), o2.getMaxLose())))
//                        .limit(10)
//                        .map(player -> "* \"" + player.getNikName() + "\":пс -- " + player.getMaxLose() + "\n")
//                        .collect(Collectors.joining())+ "\n";
    }

    private String getGods_Duels_count() {
        return null;
                //"Список богов упорядоченный по количеству дуэлей: \n"
//                + players.values().stream()
//                .sorted(((o2, o1) -> (Integer.compare(o1.getDuels().size(), o2.getDuels().size()))))
//                .map(player -> "* \"" + player.getNikName() + "\":пс -- " + player.getDuels().size() + " боев;\n")
//                .collect(Collectors.joining()) + "\n";
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
