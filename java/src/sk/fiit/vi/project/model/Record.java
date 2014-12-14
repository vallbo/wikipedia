package sk.fiit.vi.project.model;

/**
 * Created by VB on 14/12/14.
 */
public class Record {

    private int id;
    private String title;
    private String type;

    public Record(int id, String title, String type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

}
