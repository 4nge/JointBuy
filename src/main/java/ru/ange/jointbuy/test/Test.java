package ru.ange.jointbuy.test;

import ru.ange.jointbuy.utils.Constants;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main( String[] args ) {
        System.out.println(" - Test - ");

//        String str = "hello1234.0goodboy789very2345";
//        System.out.println("replace = " + str.replaceFirst("\\D*(\\d*).*", "$1") );

//
//        Pattern p = Pattern.compile("\\d+");
//        Matcher m = p.matcher("hello1234.0goodboy789very2345");
//        while(m.find()) {
//            System.out.println(m.group());
//        }

//        Pattern MY_PATTERN = Pattern.compile("\\[(.*?)\\]");
//
//        Matcher m = MY_PATTERN.matcher("FOO[BAR]");
//        while (m.find()) {
//            String s = m.group(1);
//            // s now contains "BAR"
//        }

        String string = "var1[value1],s var2[value2],s var3[value3]s";

        String patternS = String.format( Constants.INLINE_BUY_MSG_TEXT_PTT, "", "(.*?)", "", "", "");
        Pattern pattern = Pattern.compile(patternS);

        Matcher matcher = pattern.matcher(string);

        while(matcher.find()) {
            System.out.println("m = " + matcher.group(2));
        }


    }
}
