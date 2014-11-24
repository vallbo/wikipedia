package sk.fiit.vi.project.parsers;

import sk.fiit.vi.project.model.InfoBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Vallbo_work on 09/11/14.
 */
public class InfoBoxParser {

    public static InfoBox parseInfoBoxData(String data) {
            List<String> infoBoxList = getInfoBoxList(data);
            return createInfoBox(infoBoxList);
    }

    private static List<String> getInfoBoxList(String item) {
        String data = item.replaceAll("\\|\\s*([a-z_\\sA-Z0-9\\-]+)\\s*=", "@|@$1=");
        String[] exploded = data.split("@\\|@");
        return Arrays.asList(exploded);
    }

    private static InfoBox createInfoBox(List<String> data) {
        InfoBox model = new InfoBox(getType(data.get(0).trim()));
        for(int i=1; i<data.size(); i++) {
            String[] exploded = data.get(i).split("\\=");
            List<String> attribute = Arrays.asList(exploded);
            if(attribute.size()<2) {
                continue;
            }
            model.addAttribute(attribute.get(0).trim().toLowerCase().replaceAll("\\s+", "_"), attribute.get(1).trim());
        }
        return model;
    }

    private static String getType(String data) {
        return data.replaceAll("<!--.*-->", "").replaceAll("\\|", "").trim();
    }

}
