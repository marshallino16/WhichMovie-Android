package genyus.com.whichmovie.model;

import java.io.Serializable;

/**
 * Created by GENyUS on 30/01/16.
 */
public class Video implements Serializable {

    public final static String SITE_YOUTUBE = "YouTube";

    private String key;
    private String name;
    private String site;
    private String type; //trailer
    private int size;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if(((Video)o).getKey().equals(key)){
            return true;
        }
        return false;
    }
}
