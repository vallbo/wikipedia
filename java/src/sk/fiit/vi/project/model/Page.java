package sk.fiit.vi.project.model;

/**
 * Created by VB on 22/11/14.
 */
public class Page {

    private String name;

    private InfoBox infoBox;

    public Page(String name, InfoBox infoBox) {
        this.name = name;
        this.infoBox = infoBox;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InfoBox getInfoBox() {
        return infoBox;
    }

    public void setInfoBox(InfoBox infoBox) {
        this.infoBox = infoBox;
    }

}
