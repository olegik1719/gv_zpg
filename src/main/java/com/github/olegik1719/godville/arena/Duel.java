package com.github.olegik1719.godville.arena;

import lombok.Getter;

import java.util.Date;

/*
Класс дуэль:
1) Общая инфа (ID, Time, Sum, isZPG, etc.)
2) Индивидуальные поля каждого участника, характерные для дуэли.
    И тут уже у дуэли внутренний класс Hero с состоянием героя на момент дуэли:
        Количество побед
        Количество поражений

 */

@Getter
public class Duel {

    private final String ID;
    private Date startTime;
    private int sum;
    private boolean isZPG;
    private boolean isYoung;
    private Player winner;
    private Player loser;

    @Override
    public boolean equals(Object o){
        return o instanceof Duel && ID.equals(((Duel) o).ID);
    }

    @Override
    public int hashCode(){
        return ID.hashCode();
    }

    public Duel(Parser parser){
        ID = parser.getId();
        startTime = parser.getTime();
        sum = parser.getMoney();
        isZPG = parser.isZPG();
        isYoung = parser.isYoung();
        winner = new Player(parser.getWinner());
        loser = new Player(parser.getLoser());
    }

    @Getter
    class Player{
        private String godName;
        private int losesBefore;
        private int winsBefore;

        Player(Parser.Hero hero){
            godName = hero.getGodName();
            losesBefore = hero.getLoses();
            winsBefore = hero.getWins();
        }
    }
}
