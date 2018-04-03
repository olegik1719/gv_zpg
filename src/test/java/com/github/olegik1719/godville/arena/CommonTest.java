package com.github.olegik1719.godville.arena;

import org.junit.Test;

public class CommonTest {

    @Test
    public void main() {
        Common.main("res/links_list.lst","res/bad_links.lst","res/good_links.lst");
    }

    @Test
    public void getID() {
        System.out.println(Common.getID("https://gv.erinome.net/duels/log/u9rx57p9q"));
    }
}