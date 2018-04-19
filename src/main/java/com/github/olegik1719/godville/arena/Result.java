package com.github.olegik1719.godville.arena;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
@Getter
public class Result {

    private final String idFight;
    private Date dateFight;
    private boolean ZPG;
    private boolean young;
    private Participant winner;
    private Participant loser;
    private int sumInWin;

}
