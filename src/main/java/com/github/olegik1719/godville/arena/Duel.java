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
    private List<String> turns;
    private Map<String,String> rightBlock;

    public Result getResult(){
        boolean ZPG = true;
        boolean young = false;
        String winner = "";
        String loser = "";
        int winsWinner = 0;
        int winsLoser = 0;
        int losesWinner = 0;
        int losesLoser = 0;
        int sum = 0;
        return new Result(chronicleID,dateFight,ZPG,young,winner,loser,winsWinner,winsLoser,losesWinner,losesLoser,sum);
    }
}
