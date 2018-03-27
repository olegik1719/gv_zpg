package com.github.olegik1719.godville.arena;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;


public class FightTest {

    Fight current;

    @Before
    public void setUp() throws ParseException {
        //current = new Fight("2aqwf8sgd");//e183mbz38;n85c5rqbl
        current = new Fight("n85c5rqbl");
    }




    @Test
    public void getTurns() {
        System.out.println(current.getTurns());
    }

    @Test
    public void getTime() {
        System.out.println(current.getTime());
    }

    @Test
    public void isZPG() {
        System.out.println(current.isZPG());
    }
}