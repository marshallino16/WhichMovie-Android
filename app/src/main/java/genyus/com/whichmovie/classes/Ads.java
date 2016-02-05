package genyus.com.whichmovie.classes;

import java.util.Random;

/**
 * Created by GENyUS on 02/02/16.
 */
public class Ads {

    public final static String inter_image = "ca-app-pub-8173266224774166/7832831538";
    public final static String inter_video = "ca-app-pub-8173266224774166/1786297938";

    public boolean shouldDisplayInter(){
        Random r = new Random();
        int i1 = r.nextInt(5 - 0) + 0;
        if(i1 == 4){
            return true;
        } else {
            return false;
        }
    }

}
