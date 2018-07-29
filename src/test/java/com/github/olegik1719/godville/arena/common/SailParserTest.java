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
        Pattern small   = Pattern.compile("\uD83D\uDCE6$");
        Pattern bag     = Pattern.compile("\uD83D\uDCB0$");
        Pattern male    = Pattern.compile("♂$");
        Pattern female  = Pattern.compile("♀$");
        for (int i = 0; i <= 100; i++) {


            Elements turns = marine.select("div[id$=fight_chronicle]>div[class$=\"afl block\"]>div[class$=\"d_content\"]>div[class$=\"new_line dtc t"+i+"  saild_1\"]");
            for (Element turn : turns) {
                //System.out.println(turn.text());
                //System.out.println("--------------------------------------------------");
                Matcher smMatch  = small.matcher(turn.text());
                Matcher smBag    = bag.matcher(turn.text());
                Matcher smMale   = male.matcher(turn.text());
                Matcher smFemale = female.matcher(turn.text());
                if (smMatch.find()){
                    System.out.println(turn.text());
                    System.out.println("--------------------------------------------------");
                }
                if (smBag.find()){
                    System.out.println(turn.text());
                    System.out.println("--------------------------------------------------");
                }
                if (smMale.find()){
                    System.out.println(turn.text());
                    System.out.println("--------------------------------------------------");
                }
                if (smFemale.find()){
                    System.out.println(turn.text());
                    System.out.println("--------------------------------------------------");
                }
            }
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