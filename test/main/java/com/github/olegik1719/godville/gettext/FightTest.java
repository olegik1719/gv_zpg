package com.github.olegik1719.godville.gettext;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FightTest {

    Fight current;

    @Before
    public void setUp() throws ParseException {
        //current = new Fight("2aqwf8sgd");e183mbz38
        current = new Fight("e183mbz38");
    }




    @Test
    public void getTurns() {
        System.out.println(current.getTurns());
    }

    @Test
    public void getTime() throws ParseException {
        System.out.println(current.getTime());
    }

    @Test
    public void isZPG() {
        System.out.println(current.isZPG());
    }
}