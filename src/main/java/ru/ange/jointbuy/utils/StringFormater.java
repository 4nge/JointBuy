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
            toReturn = toReturn.replaceAll("%s", "(.*)");

            while (toReturn.matches(".*%([0-9]+)[d]{1}.*")) {
                String digitStr = toReturn.replaceFirst(".*%([0-9]+)[d]{1}.*", "$1");
                int numDigits = Integer.parseInt(digitStr);
                toReturn = toReturn.replaceFirst("(.*)(%[0-9]+[d]{1})(.*)",
                        "$1[0-9]{" + numDigits + "}$3");
            }
        }
        return "^" + toReturn + "$";
    }



    public static List<String> extractParametersFromFormatString(String str, String format) {
        Pattern pattern = Pattern.compile( getRegexFromFormatString( format ), Pattern.CASE_INSENSITIVE );
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
        Pattern pattern = Pattern.compile( getRegexFromFormatString( format ), Pattern.CASE_INSENSITIVE );
        return str.matches(String.valueOf( pattern ));
    }

    public static String removeMarkdownSyntax(String str) {
       return str.replace( "`", "" )
               .replace( "*", "" )
               .replace( "_", "" );
    }


    public static void main(String[] args) {

        String ptt =
                "#remittance%s\n" +
                "Перевод\n" +
                "\uD83D\uDCB6 - %s \u20BD\n" +
                "✉ - %s\n" +
                "%s" +
                "";

        String text =
                "#remittance_2019_02_27_42_19_17\n" +
                "Перевод\n" +
                "\uD83D\uDCB6 - 36985.36 \u20BD\n" +
                "✉ - Fedor Murashko\n" +
                "\n" +
                "⏳ Идет загрузка списка получателей..." +
                "";

        String pp = ptt.replaceAll("\n", "");
        String tt = text.replaceAll("\n", "");

        System.out.println("text :\n" + text + "\n -------------");
        System.out.println("ptt :\n" + ptt + "\n -------------");
        System.out.println("\n");

        System.out.println("getRegexFromFormatString :\n" + getRegexFromFormatString(ptt) + "\n -------------");



        List<String> params = extractParametersFromFormatString( text, ptt );
        System.out.println("params = " + params);

    }
}
