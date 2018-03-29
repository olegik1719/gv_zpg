package com.github.olegik1719.godville.arena;

import lombok.Getter;

import java.io.IOException;
import java.util.*;

public class Player {
    private final String nikName;
    private Collection<Duel> duels;

    public boolean equals(Object o){
        return o instanceof Player && nikName.equals(((Player) o).nikName);
    }

    public int hashCode(){
        return nikName.hashCode();
    }

    public Player(String nikName){
        this.nikName = nikName;
        duels = new HashSet<>();
    }

    public Player addLog(Parser parser) {
        duels.add(new Duel(parser));
        return this;
    }

    public Collection<Duel> getDuels() {
        return new ArrayList<>(duels);
    }

    public int getMaxLose(){
        //duels.stream().filter(d->!d.isWinner).distinct().map(duel -> duel.sum).max(Integer::compareTo);
        return duels.stream().filter(d->!d.isWinner).map(duel -> duel.sum).max(Integer::compareTo).orElse(0);
    }

    public int getMaxWin(){

        return duels.stream().filter(d->d.isWinner).map(duel -> duel.sum).max(Integer::compareTo).orElse(0);
    }
    @Getter
    public class Duel{
        //Fight fight;
        private String id;
        private int sum;
        private boolean isWinner;
        private int ownLoses;
        private int ownWins;
        private Date duelTime;
        private boolean young;
        private boolean isZPG;

        public boolean equals(Object o){
            return o instanceof Duel && id.equals(((Duel) o).id);
        }

        public int hashCode(){
            return id.hashCode();
        }

        private Duel(Parser parser) {
            this.id = parser.getId();
            isZPG = parser.isZPG();
            duelTime = parser.getTime();
            sum = parser.getMoney();
            young = parser.isYoung();
            isWinner = parser.getWinner().getGodName().equals(nikName);
            Parser.Hero hero = isWinner ? parser.getWinner() : parser.getLoser();
            ownLoses = hero.getLoses();
            ownWins = hero.getWins();

        }

        public int getOwnFights(){
            return ownLoses + ownWins;
        }
    }
}
