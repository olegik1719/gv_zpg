package com.github.olegik1719.godville.arena;

import lombok.AllArgsConstructor;

import java.util.Date;

/**
 * Результат:
 • Дата;
 • ID дуэли-хроники;
 • Участники: победитель/проигравший;
 • Сумма выигрыша;
 • Наличие храма;
 */
@AllArgsConstructor
public class Result {

    private final String idFight;
    private Date dateFight;
    private boolean ZPG;
    private boolean young;
    private String winner;
    private String loser;
    private int winsWinner;
    private int winsLoser;
    private int losesWinner;
    private int losesLoser;
    private int sumInWin;

}
