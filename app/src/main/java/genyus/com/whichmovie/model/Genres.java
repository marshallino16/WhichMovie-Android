package genyus.com.whichmovie.model;

/**
 * Created by genyus on 26/11/15.
 */
public class Genres {

    private int id;
    private String name;

    public Genres(int id) {
        this.id = id;
    }

    public Genres(String name) {
        this.name = name;
    }

    public Genres(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
