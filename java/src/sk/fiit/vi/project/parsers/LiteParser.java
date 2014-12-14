package sk.fiit.vi.project.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by VB on 14/12/14.
 */
public class LiteParser {

    private static Pattern patternInfoBoxType = Pattern.compile("(?i)^.*\\{\\{infobox\\s+(.+)\\s+\\|.*$");

    public static String parseString(String line) {
        Matcher typeMatcher = patternInfoBoxType.matcher(line);
        String data = typeMatcher.replaceFirst("$1");
        return data.trim();
    }

}
