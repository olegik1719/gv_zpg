package com.github.olegik1719.godville.arena;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.util.Map;

import org.junit.Assert;

public class ParserTest {

    @Test
    public void getLeft() {
        ChronicGetter acg = new AnyChronicGetter();
        //String chronicle = acg.getHtml("jdqrqmmgf");
        String chronicle = acg.getHtml("rdxmuwrmq");
        Document fight = Jsoup.parse(chronicle);
        Map<String, String> left = ArenaParser.getLeft(fight);
        Participant test = new Participant(left);
        Assert.assertEquals(841,test.getBricks());

    }

}