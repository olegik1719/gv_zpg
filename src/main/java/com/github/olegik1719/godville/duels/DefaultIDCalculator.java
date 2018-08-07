package com.github.olegik1719.godville.duels;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultIDCalculator implements IDCalculator {

    private static final Pattern PATTERN_LOG_ID= Pattern.compile("http[s]?:\\/\\/[^\\/]+\\/duels\\/log\\/([A-Za-z0-9]+).*");

    public static String getID(String url) {
        Matcher matcher = PATTERN_LOG_ID.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
}
