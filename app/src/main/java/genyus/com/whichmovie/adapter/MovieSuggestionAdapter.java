package genyus.com.whichmovie.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import genyus.com.whichmovie.R;

public class MovieSuggestionAdapter extends CursorAdapter {

    public MovieSuggestionAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.row_movie_suggestion, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView movieTitle = (TextView) view.findViewById(R.id.movie_title);
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
        if(null != date){
            movieTitle.setText(title+"("+date.substring(0,4)+")");
        }
    }
}