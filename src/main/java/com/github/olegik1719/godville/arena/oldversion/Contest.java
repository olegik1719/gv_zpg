package com.github.olegik1719.godville.arena.oldversion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
    }

    private String getGods_young_win() {
        return "bq. Самые большие выигрыши у храмостроителей:\n\n"
                + getGods_max_win(true);
    }

    private String getGods_old_lose(){
        return "bq. Самые большие проигрыши у храмовладельцев:\n\n"
                + getGods_max_lose(false);
    }

    private String getGods_young_lose() {
        return "bq. Самые большие проигрыши у храмостроителей:\n\n"
                + getGods_max_lose(true);
    }

    private String getGods_Duels_count() {
        return "bq. Лидеры по количеству боев:\n\n"
                + contest.keySet().stream()
                .sorted((o1, o2) -> Integer.compare(contest.get(o2).size(),contest.get(o1).size()))
                .limit(10).map(player -> "* \"" + player + "\":пс " + contest.get(player).size() + ";\n")
                .collect(Collectors.joining()) + "\n";
    }

    private int getGod_results_count(String player, boolean isWin){
        return (int) (isWin
                ? contest.get(player).stream().filter(duel -> duel.getWinner().getGodName().equals(player)).count()
                : contest.get(player).stream().filter(duel -> duel.getLoser().getGodName().equals(player)).count()
        );
    }

    private String getGods_Wins_count() {
        return "bq. Лидеры по количеству побед:\n\n"
                + contest.keySet().stream()
                .sorted((o1, o2) -> Integer.compare(getGod_results_count(o2,true),getGod_results_count(o1,true)))
                .limit(10).map(player -> "* \"" + player + "\":пс " + getGod_results_count(player,true) + ";\n")
                .collect(Collectors.joining()) + "\n";
    }

    private String getGods_Loses_count() {
        return "bq. Лидеры по количеству поражений:\n\n"
                + contest.keySet().stream()
                .sorted((o1, o2) -> Integer.compare(getGod_results_count(o2,false),getGod_results_count(o1,false)))
                .limit(10).map(player -> "* \"" + player + "\":пс " + getGod_results_count(player,false) + ";\n")
                .collect(Collectors.joining()) + "\n";
    }

    private int getGod_sum_play (String player, boolean isWin, boolean isYoung){
        return contest.get(player).stream()
                .filter(duel -> isWin
                        ?duel.getWinner().getGodName().equals(player)
                        :duel.getLoser().getGodName().equals(player))
                .filter(duel -> isYoung == duel.isYoung())
                .mapToInt(Duel::getSum)
                .sum();
    }

    private String getGods_Sum_win_old (){
        return "bq. Наибольшие сумманые выигрыши на взрослой ZPG-арене:\n\n"
                + contest.keySet().stream()
                .sorted((o1, o2) -> Integer.compare(getGod_sum_play(o2,true,false),getGod_sum_play(o1,true,false)))
                .limit(10).map(player -> "* \"" + player + "\":пс " + getGod_sum_play(player,true,false) + ";\n")
                .collect(Collectors.joining()) + "\n";

    }

    private String getGods_Sum_win_young (){
        return "bq. Наибольшие сумманые выигрыши на дохрамовой ZPG-арене:\n\n"
                + contest.keySet().stream()
                .sorted((o1, o2) -> Integer.compare(getGod_sum_play(o2,true,true),getGod_sum_play(o1,true,true)))
                .limit(10).map(player -> "* \"" + player + "\":пс " + getGod_sum_play(player,true,true) + ";\n")
                .collect(Collectors.joining()) + "\n";

    }

    private String getGods_Sum_lose_old (){
        return "bq. Наибольшие сумманые проигрыши на взрослой ZPG-арене:\n\n"
                + contest.keySet().stream()
                .sorted((o1, o2) -> Integer.compare(getGod_sum_play(o2,false,false),getGod_sum_play(o1,false,false)))
                .limit(10).map(player -> "* \"" + player + "\":пс " + getGod_sum_play(player,false,false) + ";\n")
                .collect(Collectors.joining()) + "\n";

    }

    private String getGods_Sum_lose_young (){
        return "bq. Наибольшие сумманые проигрыши на дохрамовой ZPG-арене:\n\n"
                + contest.keySet().stream()
                .sorted((o1, o2) -> Integer.compare(getGod_sum_play(o2,false,true),getGod_sum_play(o1,false,true)))
                .limit(10).map(player -> "* \"" + player + "\":пс " + getGod_sum_play(player,false,true) + ";\n")
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
                        + getGods_old_lose()
                        + getGods_old_win()
                        + getGods_young_lose()
                        + getGods_young_win()
                        + getGods_Duels_count()
                        + getGods_Loses_count()
                        + getGods_Wins_count()
                        + getGods_Sum_lose_old()
                        + getGods_Sum_win_old()
                        + getGods_Sum_lose_young()
                        + getGods_Sum_win_young()
                        + "?????"
                ;
    }


}
