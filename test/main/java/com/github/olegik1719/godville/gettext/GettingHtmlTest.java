package com.github.olegik1719.godville.gettext;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GettingHtmlTest {

    GettingHtml current;

    @Test
    public void getHtml() {
        System.out.println(new GettingHtml("https://gv.erinome.net/duels/log/2aqwf8sgd").getHtml());
    }

    @Test
    public void getGods() {
        System.out.println(current.getGod1());
        System.out.println(current.getGod2());
    }

    @Test
    public void isWinnerTheFirst() {
        System.out.println("winner:"
                + (current.isWinnerTheFirst()
                ? current.getGod1()
                : current.getGod2()
        ));
    }

    @Before
    public void setUp() throws Exception {
        current = new GettingHtml("https://gv.erinome.net/duels/log/2aqwf8sgd");
    }
}