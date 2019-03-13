package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.stanford.cs108.bunnyworld.R;

public class GridViewAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<ShapeResource> imgList;

    public GridViewAdapter(Context c, ArrayList<ShapeResource> list) {
        mContext = c;
        imgList = list;
    }

    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public Object getItem(int position) {
        return imgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_single, null);
        }

        ImageView image = (ImageView) convertView.findViewById(R.id.grid_image);
        image.setImageResource(imgList.get(position).getId());

        return convertView;
    }
}
