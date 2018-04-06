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
    //private final String chronicleID;
    private Map<String,String> leftBlock;
    private Map<String,String> rightBlock;
    private List<String> turns;
}
