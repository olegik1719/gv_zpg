package com.github.olegik1719.godville.arena;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultIDCalculator implements IDCalculator {

    private static final String LOG_ID_REGEXP = "http[s]?:\\/\\/[^\\/]+\\/duels\\/log\\/([A-Za-z0-9]+).*";
    private static final Pattern PATTERN_LOG = Pattern.compile(LOG_ID_REGEXP);

    @Override
    public String getID(String url) {
        Matcher matcher = PATTERN_LOG.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
}
