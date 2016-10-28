package com.mc.vending.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.data.ProductGroupHeadData;
import java.util.ArrayList;
import java.util.List;

public class MC_CombinationPickAdapter extends BaseAdapter {
    Activity context;
    private List<ProductGroupHeadData> dataList;
    private LayoutInflater inflater;

    class ViewHodler {
        TextView combination_name;
        TextView combination_number;

        ViewHodler() {
        }
    }

    public MC_CombinationPickAdapter(Context context, List<ProductGroupHeadData> dataList, ListView lv) {
        if (dataList == null) {
            this.dataList = new ArrayList();
        } else {
            this.dataList = dataList;
        }
        this.context = (Activity) context;
        this.inflater = this.context.getLayoutInflater();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = this.inflater.inflate(R.layout.combination_pick_item, parent, false);
            viewHodler.combination_number = (TextView) convertView.findViewById(R.id.combination_number);
            viewHodler.combination_name = (TextView) convertView.findViewById(R.id.combination_name);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        ProductGroupHeadData data = (ProductGroupHeadData) getItem(position);
        viewHodler.combination_number.setText(data.getPg1Code());
        viewHodler.combination_name.setText(data.getPg1Name());
        convertView.setTag(viewHodler);
        return convertView;
    }

    public int getCount() {
        return this.dataList.size();
    }

    public Object getItem(int position) {
        return this.dataList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }
}
