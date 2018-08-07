package com.github.olegik1719.godville.duels.arena;


import com.github.olegik1719.godville.duels.common.Turn;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.List;

/**Хранение хроники:
 * Дата;
 * ID хроники-дуэли;
 * Тип хроники;
 * Левый блок -- map<Имя, параметр>
 * Правый блок -- map<Имя, параметр>
 * Центральный блок -- List<Turn>
 */

@AllArgsConstructor
public class Duel {
    private Date dateFight;
    private final String chronicleID;
    private Participant leftHero;
    private List<Turn> turns;
    private Participant rightHero;

    public Result getResult(){
        boolean ZPG = isZPG();
        boolean young = isYoung();
        Participant winner = getWinner();
        Participant loser = getLoser();
        int sum = 0;
        return new Result(chronicleID, dateFight, ZPG, young
                , winner
                , loser
                , sum);
    }

    private boolean isZPG(){
        return turns.get(0).isZPG();
    }

    private boolean isYoung(){
        //return false//leftBlock.containsKey("Кирпичей для храма");
        return leftHero.getBricks()>0;
    }

    private Participant getWinner(){
        return getParticipant(true);
    }

    private Participant getLoser(){
        return getParticipant(false);
    }

    private Participant getParticipant(boolean isWinner){
        if (leftHero.isWinner() == isWinner) return leftHero;
        return rightHero;
    }
}
