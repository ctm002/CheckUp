package cl.weekmark.checkup.models;

/**
 * Created by Carlos_Tapia on 05-10-2017.
 */

public class Patente {
    private  String content;

    public Patente() {
    }

    public String toJSON() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
