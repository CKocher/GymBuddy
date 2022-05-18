package com.example.gymbuddy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gymbuddy.R;

import java.util.List;

public class BLEDeviceListViewAdapter extends BaseAdapter {

    private List<String> data;
    private List<String> data2;
    private Context context;

    public BLEDeviceListViewAdapter(Context context, List<String> data1, List<String> data2) {
        super();
        this.data = data1;
        this.data2 = data2;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = LayoutInflater.from(context).
                inflate(R.layout.two_line_icon, parent, false);

        TextView text1 = (TextView) rowView.findViewById(R.id.text1);
        TextView text2 = (TextView) rowView.findViewById(R.id.text2);
        ImageView icon = (ImageView) rowView.findViewById(R.id.icon);

        text1.setText(data.get(position));
        text2.setText(data2.get(position));
        icon.setImageResource(R.drawable.ble_not_connected);

        return rowView;
    }
}
