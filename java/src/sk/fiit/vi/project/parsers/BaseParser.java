package sk.fiit.vi.project.parsers;

import sk.fiit.vi.project.reader.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by VB on 12/10/14.
 */
public class BaseParser {

    private static Pattern patternInfoBoxHead = Pattern.compile("(?i)^.*\\{\\{infobox(.*)$");
    private static Pattern patternInfoBoxContent = Pattern.compile("^(.*)\\}\\}.*$");
    private static Pattern patternInfoBoxTail = Pattern.compile("^.*\\}\\}(.*)$");
    private static Pattern patternEquals  = Pattern.compile(".*=.*");


    public static String parseString(String data) {
        if(hasBox(data)) {
            String infoBox = getInfoBox(data);
            if(hasEquals(infoBox)) {
                return infoBox;
            }
//            parseString(getRest(data));
        }
        return "";
    }

    private static boolean hasEquals(String data) {
        Matcher matcher = patternEquals.matcher(data);
        return (matcher.find() ? true : false);
    }

    private static String getRest(String line) {
        Matcher headMatcher = patternInfoBoxHead.matcher(line);
        String tail = headMatcher.replaceFirst("$1");
        Matcher tailMatcher = patternInfoBoxTail.matcher(tail);
        String data = tailMatcher.replaceFirst("$1");
        return data;
    }

    private static String getInfoBox(String line) {
        Matcher headMatcher = patternInfoBoxHead.matcher(line);
        String tail = headMatcher.replaceFirst("$1");
        Matcher contentMatcher = patternInfoBoxContent.matcher(tail);
        String data = contentMatcher.replaceFirst("$1");
        return data;
    }

    private static boolean hasBox(String line) {
        Matcher matcher = patternInfoBoxHead.matcher(line);
        return (matcher.find() ? true : false);
    }

}
