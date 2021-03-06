package genyus.com.whichmovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import genyus.com.whichmovie.R;
import genyus.com.whichmovie.model.Genre;

public class CategoryAdapter extends ArrayAdapter<Genre> {

    private Context context;
    private int textViewResourceId;
    private ArrayList<Genre> values;
    private LayoutInflater inflater;

    public CategoryAdapter(Context context, int textViewResourceId, ArrayList<Genre> values) {
        super(context, textViewResourceId, values);
        this.inflater = LayoutInflater.from(context);
        this.textViewResourceId = textViewResourceId;
        this.context = context;
        this.values = values;
    }

    public int getCount() {
        return values.size();
    }

    public Genre getItem(int position) {
        return values.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(textViewResourceId, null);

            holder = new ViewHolder();

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title = (TextView) convertView.findViewById(R.id.cat_title);
        holder.title.setText(""+values.get(position).getName());
        holder.title.setPadding(5,5,5,5);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_spinner_sub_category, null);

            holder = new ViewHolder();

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title = (TextView) convertView.findViewById(R.id.cat_title);
        holder.title.setText(""+values.get(position).getName());

        return convertView;
    }

    static class ViewHolder
    {
        TextView title;
    }
}