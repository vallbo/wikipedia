package sk.fiit.vi.project.model;

import java.util.*;

/**
 * Created by Vallbo_work on 09/11/14.
 */
public class InfoBox {

    private String type;

    private Map<String, String> attributes = new HashMap<String, String>();

    public InfoBox(String type) {
        this.type = type;
    }

    public void addAttribute (String key, String value) {
        attributes.put(key, value);
    }

    public String getType() {
        return this.type;
    }

    public Map<String, String> getAttributes() {
        return this.attributes;
    }

}
