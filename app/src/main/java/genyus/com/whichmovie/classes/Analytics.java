package genyus.com.whichmovie.classes;

/**
 * Created by genyus on 04/09/15.
 */
public interface Analytics {

    public final static String PROPERTY_ID = "UA-70817529-1";

    public enum EVENT {

        SCREEN_ENTER,
        SCREEN_QUIT,
        ELEMENT_CLICK,
        STREAM,
        CATEGORY,
        SUGGESTIONS,
        DEEP_LINKING,
        SHARE,
        RATE,
        SAVE,
    }
}
