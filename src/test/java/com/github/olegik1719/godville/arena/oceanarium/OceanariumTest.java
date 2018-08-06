package com.github.olegik1719.godville.arena.oceanarium;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class OceanariumTest {

    @Test
    public void addForumPage() {
        Oceanarium oceanarium = new Oceanarium();
        oceanarium.addForumPage("112");
        oceanarium.addForumPage("113");
        oceanarium.addForumPage("114");
        oceanarium.addForumPage("115");
        oceanarium.addForumPage("116");
        oceanarium.addForumPage("117");

        LinkedHashSet<Integer> brokenPosts = oceanarium.getBroken().stream().map(Integer::parseInt).sorted().collect(Collectors.toCollection(LinkedHashSet::new));

        brokenPosts.remove(1564052);
        brokenPosts.remove(1564293);

        {
            oceanarium.addResult("https://godville.net/forums/redirect_to_post/3638?post=1564317"
                    ,"2018-07-24T08:00:57+03:00"
                    , "Katzenjammer"
                    , "Дельфины"
                    ,"erxw3jg");
            brokenPosts.remove(1564317);
        }

        brokenPosts.remove(1565648);

        {
            oceanarium.addResult("https://godville.net/forums/redirect_to_post/3638?post=1566718"
                    ,"2018-07-29T17:01:48+03:00"
                    , "Просто Богиня"
                    , "Дельфины"
                    ,"8gmuukd");
            brokenPosts.remove(1566718);
        }

        {
            oceanarium.addResult("https://godville.net/forums/redirect_to_post/3638?post=1567347"
                    ,"2018-07-30T22:57:22+03:00"
                    , "Просто Богиня"
                    , "Дельфины"
                    ,"022s32l");

            oceanarium.addResult("https://godville.net/forums/redirect_to_post/3638?post=1567347"
                    ,"2018-07-30T22:57:22+03:00"
                    , "Просто Богиня"
                    , "Дельфины"
                    ,"g5w8hq3");

            brokenPosts.remove(1567347);
        }

        brokenPosts.remove(1567544);

        {//1567799
            oceanarium.addResult("https://godville.net/forums/redirect_to_post/3638?post=1567799"
                    ,"2018-07-31T21:05:49+03:00"
                    , "Эльт"
                    , "Акулы"
                    ,"mxs62ql");

            brokenPosts.remove(1567799);
        }

        brokenPosts.remove(1567853);
        brokenPosts.remove(1567855);
        brokenPosts.remove(1568554);

        {//1568905
            oceanarium.addResult("https://godville.net/forums/redirect_to_post/3638?post=1568905"
                    ,"2018-08-03T14:04:43+03:00"
                    , "Просто Богиня"
                    , "Дельфины"
                    ,"rdnkew3");

            brokenPosts.remove(1568905);
        }

        {//1569207
            oceanarium.addResult("https://godville.net/forums/redirect_to_post/3638?post=1569207"
                    ,"2018-08-04T13:44:17+03:00"
                    , "Грарх"
                    , "Акулы"
                    ,"xp0k8q5");

            brokenPosts.remove(1569207);
        }

        {//1569214
            oceanarium.addResult("https://godville.net/forums/redirect_to_post/3638?post=1569214"
                    ,"2018-08-04T13:58:59+03:00"
                    , "Просто Богиня"
                    , "Дельфины"
                    ,"9s340jt");

            brokenPosts.remove(1569214);
        }

        brokenPosts.remove(1569285);
        brokenPosts.remove(1569471);

        {//1569528
            oceanarium.addResult("https://godville.net/forums/redirect_to_post/3638?post=1569528"
                    ,"2018-08-05T15:41:10+03:00"
                    ,"После Вас"
                    ,"Китовые акулы"
                    ,"2gxmtpr");
            oceanarium.addResult("https://godville.net/forums/redirect_to_post/3638?post=1569528"
                    ,"2018-08-05T15:41:10+03:00"
                    ,"После Вас"
                    ,"Китовые акулы"
                    ,"xj1srau");
            brokenPosts.remove(1569528);
        }
        {//1569701
            oceanarium.addResult("https://godville.net/forums/redirect_to_post/3638?post=1569701"
                    ,"2018-08-05T22:22:28+03:00"
                    , "Эльт"
                    , "Акулы"
                    ,"gst9ut9");
            oceanarium.addResult("https://godville.net/forums/redirect_to_post/3638?post=1569701"
                    ,"2018-08-05T22:22:28+03:00"
                    , "Эльт"
                    , "Акулы"
                    ,"g65443z");
            brokenPosts.remove(1569701);

        }

        if (brokenPosts.isEmpty()) {
            //System.out.println(oceanarium.getResults("|"));
        }else {
            System.out.println(brokenPosts);
        }

    }
}