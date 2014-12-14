package sk.fiit.vi.project.parsers;

import sk.fiit.vi.project.model.InfoBox;

import java.util.Arrays;
import java.util.List;

/**
 * Created by VB on 09/11/14.
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
        String type = getType(data.get(0).trim());
        if(type.isEmpty()) {
            return null;
        }
        return new InfoBox(type);
    }

    private static InfoBox getAttributesString(InfoBox model, List<String> data) {
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
        return data.replaceFirst("^([a-z\\sA-Z\\._-]+).*$", "$1").trim();
    }

}
