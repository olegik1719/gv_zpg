package com.github.olegik1719.godville.arena;

import com.github.olegik1719.godville.arena.oldversion.Parser;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;


public class ParserTest {

    Parser current;

    @Before
    public void setUp() throws ParseException {
        //current = new Fight("2aqwf8sgd");//e183mbz38;n85c5rqbl;ehd2kuls6
        current = new Parser("ehd2kuls6");
    }




    @Test
    public void getTurns() {
        System.out.println(current.getTurns());
    }

    @Test
    public void getTime() {
        //System.out.println(current.getTime());
    }

    @Test
    public void isZPG() {
        System.out.println(current.isZPG());
    }
}