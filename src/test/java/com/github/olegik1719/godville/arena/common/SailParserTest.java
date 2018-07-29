package com.github.olegik1719.godville.arena.common;

import com.github.olegik1719.godville.arena.chronicgetters.WebChronicGetter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class SailParserTest {


    SailParser chronic;

    @Before
    public void setUp() throws Exception {
        WebChronicGetter logs = new WebChronicGetter("https://gv.erinome.net/duels/log/");
        String log = logs.getHtml("468zw77");
        chronic = new SailParser(log, "Гигантская Флюктуация");

    }

    @Test
    public void getID() {
        assertEquals("468zw77", chronic.getID());
    }

    @Test
    public void getMarine() {
        Document marine = chronic.getMarine();
        for (int i = 1; i < 5 ; i++) {
            Elements result = marine.select("div[id$=h_tbl] > div[class$=\"t_line saild_"+ i +"\"] > div[class$=c2] > span[class$=ple]");

            System.out.println(result.text());

            //System.out.println("---------------------------");
        }
    }

    @Test
    public void getSailDate() {
        System.out.println(chronic.getSailDate());
    }

    @Test
    public void getInfluence() {
    }

    @Test
    public void getEscape() {
    }

    @Test
    public void getSmallFish() {
    }

    @Test
    public void getSmallIceland() {
    }

    @Test
    public void getBigFish() {
    }

    @Test
    public void getBigIceland() {
    }

    @Test
    public void getSmallOut() {
    }

    @Test
    public void getBigOut() {
    }

    @Test
    public void getPartNumber() {
        assertEquals((long)1,(long)chronic.getPartNumber());
    }

    @Test
    public void getPartHero() {
        assertTrue(chronic.getPartHero().equalsIgnoreCase("Абсолютный Рандом"));
    }

    @Test
    public void getDrown() {
    }

    @Test
    public void getTugs() {
    }

    @Test
    public void getAllBig() {
    }

    @Test
    public void getPartGod() {
        assertTrue(chronic.getPartGod().equalsIgnoreCase("Гигантская Флюктуация"));
    }
}