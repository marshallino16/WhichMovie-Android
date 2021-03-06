package genyus.com.whichmovie.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import genyus.com.whichmovie.utils.AppUtils;
import genyus.com.whichmovie.utils.ObjectUtils;

/**
 * Created by genyus on 29/11/15.
 */
public class Movie implements Serializable {

    private SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dfDate_day = new SimpleDateFormat("EEEE MMMM dd, yyyy");

    private boolean adult;
    private String backdrop_path;
    private ArrayList<Integer> genre_ids = new ArrayList<>();
    private ArrayList<String> productionCompanies = new ArrayList<>();
    private ArrayList<Image> images = new ArrayList<>();
    private ArrayList<Video> videos = new ArrayList<>();
    private String original_language = "";
    private String original_title = "";
    private String overview = "";
    private String release_date = "";
    private String poster_path;
    private String director;
    private Double popularity;
    private String title = "";
    private boolean video;
    private float vote_average;
    private int vote_count = 0;
    private int id = 0;

    private int budget = 0;
    private int revenue = 0;
    private int runtime = 0;
    private String imdb;
    private String homepage;

    private String vudu;
    private String googlePlay;

    private ArrayList<Crew> crew = new ArrayList<>();

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public ArrayList<Integer> getGenre_ids() {
        return genre_ids;
    }

    public ArrayList<Genre> getGenres() {
        ArrayList<Genre> genres = new ArrayList<>();
        for (int i = 0; i < genre_ids.size(); ++i) {
            genres.add(ObjectUtils.getGenreById(genre_ids.get(i)));
        }
        return genres;
    }

    public void setGenre_ids(ArrayList<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        try {
            return new String(overview.getBytes("ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return overview;
        } catch (NullPointerException e) {
            return overview;
        }
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        if(null!= release_date){
            if(AppUtils.isDeviceInFrench()){
                dfDate_day = new SimpleDateFormat("EEEE dd MMMM yyyy");
            }
            String str = release_date;
            Date d = null;
            try {
                d = dfDate.parse(str);
                if(null != d){
                    str = dfDate_day.format(d);
                }
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            return str;
        } else {
            return release_date;
        }
    }

    public String getDate(){
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getTitle() {
        try {
            return new String(title.getBytes("ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return title;
        } catch (NullPointerException e) {
            return title;
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public ArrayList<Crew> getCrew() {
        return crew;
    }

    public void setCrew(ArrayList<Crew> crew) {
        this.crew = crew;
    }

    public ArrayList<String> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(ArrayList<String> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }

    public String getVudu() {
        return vudu;
    }

    public void setVudu(String vudu) {
        this.vudu = vudu;
    }

    public String getGooglePlay() {
        return googlePlay;
    }

    public void setGooglePlay(String googlePlay) {
        this.googlePlay = googlePlay;
    }

    public String getDirector() {
        try {
            return new String(director.getBytes("ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return director;
        } catch (NullPointerException e) {
            return director;
        }
    }

    public void setDirector(String director) {
        this.director = director;
    }

    @Override
    public boolean equals(Object o) {
        if (((Movie) o).getId() == id) {
            return true;
        }
        return false;
    }
}
