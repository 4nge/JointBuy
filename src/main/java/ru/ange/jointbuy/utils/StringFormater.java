package ru.ange.jointbuy.utils;

import com.vdurmont.emoji.EmojiParser;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringFormater {

    public static String getRegexFromFormatString(String format) {
        String toReturn = format.replaceAll("\\.", "\\\\.")
                .replaceAll("\\!", "\\\\!"); // escape some special regexp chars

        if (toReturn.indexOf("%") >= 0) {
            toReturn = toReturn.replaceAll("%s", "(.+)");

            while (toReturn.matches(".*%([0-9]+)[d]{1}.*")) {
                String digitStr = toReturn.replaceFirst(".*%([0-9]+)[d]{1}.*", "$1");
                int numDigits = Integer.parseInt(digitStr);
                toReturn = toReturn.replaceFirst("(.*)(%[0-9]+[d]{1})(.*)", "$1[0-9]{" + numDigits + "}$3");
            }
        }
        return "^" + toReturn + "$";
    }

    private static Pattern getPattern(String str, String format) {
        return Pattern.compile( getRegexFromFormatString( format ), Pattern.CASE_INSENSITIVE );
    }

    public static List<String> extractParametersFromFormatString(String str, String format) {
        Pattern pattern = getPattern( str, format );
        Matcher matcher = pattern.matcher( str );
        if (matcher.find()) {
            List<String> parameters = new ArrayList<>(matcher.groupCount());
            for (int i = 0; i < matcher.groupCount(); i++) {
                parameters.add(matcher.group(i + 1));
            }
            return parameters;
        }
        return Collections.emptyList();
    }

    public static boolean matchesFormatString(String str, String format) {
        Pattern pattern = getPattern( str, format );
        return str.matches(String.valueOf( pattern ));
    }

    public static String removeMarkdownSyntax(String str) {
       return str.replace( "`", "" )
               .replace( "*", "" )
               .replace( "_", "" );
    }

}
