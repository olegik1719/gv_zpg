package com.github.olegik1719.godville.arena;


import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private Map<String,String> leftBlock;
    private List<Turn> turns;
    private Map<String,String> rightBlock;

    public Result getResult(){
        boolean ZPG = isZPG();
        boolean young = isYoung();
        String winner = getPartiant(true);
        String loser = getPartiant(false);
        int winsWinner = getWinsPartiant(true);
        int winsLoser = getLosesPartiant(true);
        int losesWinner = getWinsPartiant(false);
        int losesLoser = getLosesPartiant(false);
        int sum = 0;
        return new Result(chronicleID,dateFight,ZPG,young,winner,loser,winsWinner,winsLoser,losesWinner,losesLoser,sum);
    }

    private boolean isZPG(){
        return false;
    }

    private boolean isYoung(){
        return false;
    }

    private String getPartiant(boolean winner){
        return null;
    }

    private int getWinsPartiant(boolean winner){
        return 0;
    }

    private int getLosesPartiant(boolean winner){
        return 0;
    }

    private int getSum(){
        return 0;
    }
}
