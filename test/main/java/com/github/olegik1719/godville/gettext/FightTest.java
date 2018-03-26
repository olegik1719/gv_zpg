package com.github.olegik1719.godville.gettext;

import org.junit.Before;
import org.junit.Test;


public class FightTest {

    Fight current;

    @Before
    public void setUp() {
        //current = new Fight("2aqwf8sgd");e183mbz38
        current = new Fight("e183mbz38");
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
}