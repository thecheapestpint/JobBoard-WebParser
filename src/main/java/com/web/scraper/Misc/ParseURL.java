package com.web.scraper.Misc;


/**
 * Created by Mitchell on 23/07/2017.
 */
public class ParseURL {

    private static String specialCharacters(String str) {
        final String[] metaCharacters = {"\\", "^", "$", "{", "}", "[", "]", "(", ")", ".", "*", "+", "?", "|", "<", ">", "-", "&"};
        for (String meta : metaCharacters) {
            if (str.equals(meta)) {
                return "\\" + str;
            }
        }
        return str;
    }

    public static String convertURL(String url, String toConvert) {

        String converted = "";
        String replaced = "";
        if (!toConvert.contains("http")) {

            // Assume that it's not absolute
            if (url.contains(String.valueOf(toConvert.charAt(0)))) {
                if (String.valueOf(toConvert.charAt(0)).equals("/")) {
                    int index = url.lastIndexOf('/');
                    replaced = url.substring(0, index);
                } else {
                    replaced = url.split(specialCharacters(String.valueOf(toConvert.charAt(0))), 2)[0];
                }
                converted = replaced + toConvert;

            }

        }

        return converted;
    }

}
