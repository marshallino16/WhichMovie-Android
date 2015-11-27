package genyus.com.whichmovie.utils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import genyus.com.whichmovie.R;

/**
 * Created by genyus on 11/08/15.
 */
public class TextUtils {

    public static void displayToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        View view = toast.getView();
        view.setBackgroundResource(R.color.colorPrimary);
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setTextColor(context.getResources().getColor(android.R.color.white));
        toast.show();
    }
}
