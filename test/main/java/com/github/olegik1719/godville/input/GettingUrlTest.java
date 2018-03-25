package com.github.olegik1719.godville.input;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GettingUrlTest {
    GettingUrl gettingUrl;
    @Before
    public void setUp() throws Exception {
        gettingUrl = new GettingUrl("https://godville.net/duels/log/2aqwf8sgd");
    }

    @Test
    public void getUrl() {
        System.out.println(gettingUrl.getUrl());
    }
}