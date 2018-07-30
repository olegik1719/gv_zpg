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


    private SailParser chronic;

    @Before
    public void setUp() throws Exception {
        WebChronicGetter logs = new WebChronicGetter("https://gv.erinome.net/duels/log/");
        String idLog  = "0e6s1wt";//"468zw77";//"0e6s1wt";//"d0gheyk";//"468zw77";"jqssxzx"
        String idPart = "Umbabarauma";//"Гигантская Флюктуация";//"Umbabarauma";//"Coyolxauhqui";//"Гигантская Флюктуация";"SirReindeer"
        String log = logs.getHtml(idLog);
        chronic = new SailParser(log, idPart);

    }

    @Test
    public void getID() {
        assertEquals("468zw77", chronic.getID());
    }

    @Test
    public void getMarine() {
        System.out.println("Date: " + chronic.getSailDate());
        System.out.println("ID:   " + chronic.getID());
        System.out.println("God:  " + chronic.getPartGod());
        System.out.println("Num:  " + chronic.getPartNumber());
        System.out.println("Hero: " + chronic.getPartHero());
        System.out.println("BOut: " + chronic.getBigOut());
        System.out.println("SOut: " + chronic.getSmallOut());
        System.out.println("AOut: " + chronic.getAllBig());
        System.out.println("sFis: " + chronic.getSmallGetFish());
        System.out.println("sIce: " + chronic.getSmallGetIceland());
        System.out.println("bFis: " + chronic.getBigGetFish());
        System.out.println("bIce: " + chronic.getBigGetIceland());
        System.out.println("Infl: " + chronic.getInfluence());
        System.out.println("Escs: " + chronic.getEscape());
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
        assertEquals(0,chronic.getSmallOut());
    }

    @Test
    public void getBigOut() {
        assertEquals(2,chronic.getBigOut());
    }

    @Test
    public void getPartNumber() {
        assertEquals(1,chronic.getPartNumber());
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