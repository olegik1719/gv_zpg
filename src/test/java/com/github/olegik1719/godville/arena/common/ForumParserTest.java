package com.github.olegik1719.godville.arena.common;

import org.junit.Test;

import static org.junit.Assert.*;

public class ForumParserTest {

    @Test
    public void getHtmdoc() {
        //System.out.println(new ForumParser("112").getHtmdoc());
        ForumParser forumParser = new ForumParser(3638);
        forumParser.parsePosts("112");
        forumParser.parsePosts("113");
        forumParser.parsePosts("114");
        forumParser.parsePosts("115");
    }
}