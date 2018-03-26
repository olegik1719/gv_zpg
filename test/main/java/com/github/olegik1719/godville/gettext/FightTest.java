package com.github.olegik1719.godville.gettext;

import org.junit.Before;
import org.junit.Test;


public class FightTest {

    Fight current;

    @Before
    public void setUp() {
        current = new Fight("2aqwf8sgd");
    }

    @Test
    public void getHtml() {
        current.getHtml();
    }

    @Test
    public void getGods() {
        System.out.println(current.getGod1());
        System.out.println(current.getGod2());
    }



    @Test
    public void getTurns() {
        System.out.println(current.getTurns());
    }

    @Test
    public void getLastTurn() {
        System.out.println(current.getLastTurn());
    }
}