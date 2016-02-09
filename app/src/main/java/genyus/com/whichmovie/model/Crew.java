package genyus.com.whichmovie.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * Created by genyus on 09/12/15.
 */
public class Crew implements Serializable{

    private int id;
    private int cast_id;
    private int credit_id;
    private String character;
    private String name;
    private String profile_path;
    public boolean isClicked = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCast_id() {
        return cast_id;
    }

    public void setCast_id(int cast_id) {
        this.cast_id = cast_id;
    }

    public int getCredit_id() {
        return credit_id;
    }

    public void setCredit_id(int credit_id) {
        this.credit_id = credit_id;
    }

    public String getCharacter() {
        try {
            return new String(character.getBytes("ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return character;
        } catch (NullPointerException e) {
            return character;
        }
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getName() {
        try {
            return new String(name.getBytes("ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return name;
        } catch (NullPointerException e) {
            return name;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }

    @Override
    public boolean equals(Object o) {
        if(((Crew)o).getId() == id){
            return true;
        }
        return false;
    }
}
