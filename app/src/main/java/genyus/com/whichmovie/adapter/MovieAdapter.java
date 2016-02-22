package genyus.com.whichmovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import genyus.com.whichmovie.R;
import genyus.com.whichmovie.model.Movie;

public class MovieAdapter extends ArrayAdapter<Movie> {

    private Context context;
    private final ArrayList<Movie> listMovies;

    public MovieAdapter(Context context, ArrayList<Movie> listMovies) {
        super(context, 0, listMovies);
        this.context = context;
        this.listMovies = listMovies;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_movie_suggestion, null);
        }

        TextView movieTitle = (TextView) convertView.findViewById(R.id.movie_title);
        if (null != listMovies.get(position).getDate() && listMovies.get(position).getDate().length() > 4) {
            movieTitle.setText(listMovies.get(position).getTitle() + "(" + listMovies.get(position).getDate().substring(0, 4) + ")");
        } else {
            movieTitle.setText(listMovies.get(position).getTitle());
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return listMovies.size();
    }

    @Override
    public Movie getItem(int position) {
        return listMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}