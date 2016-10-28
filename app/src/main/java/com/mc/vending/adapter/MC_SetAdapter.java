package com.mc.vending.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.config.Constant;
import java.util.ArrayList;
import java.util.List;

public class MC_SetAdapter extends BaseAdapter {
    Activity context;
    private List<String> dataList;
    private LayoutInflater inflater;

    class ViewHodler {
        ImageView set_icon;
        TextView set_name;

        ViewHodler() {
        }
    }

    public MC_SetAdapter(Context context, List<String> dataList, ListView lv) {
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
            convertView = this.inflater.inflate(R.layout.set_item, parent, false);
            viewHodler.set_icon = (ImageView) convertView.findViewById(R.id.set_icon);
            viewHodler.set_name = (TextView) convertView.findViewById(R.id.set_name);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        viewHodler.set_name.setText((String) getItem(position));
        if (position == 0 || position == 3) {
            viewHodler.set_icon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.icon_replenishment));
        } else if (position == 1 || position == 4) {
            viewHodler.set_icon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.icon_replenishment));
        } else if (position == 2) {
            viewHodler.set_icon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.icon_urgent_replenishment));
        } else if (position == 5) {
            viewHodler.set_icon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.icon_inventory));
        } else if (position == 6) {
            viewHodler.set_icon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.icon_returns_forward));
        } else if (position == 7) {
            viewHodler.set_icon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.icon_returns_forward));
        } else if (position == 8) {
            viewHodler.set_icon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.icon_test));
        } else if (position == 9) {
            viewHodler.set_icon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.icon_synchronous));
        } else if (position == 10) {
            if (Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")).intValue() >= Constant.VERSION_STOCK_SYNC) {
                viewHodler.set_icon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.icon_init));
            } else {
                viewHodler.set_icon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.icon_synchronous));
            }
        } else if (position == 11) {
            if (Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")).intValue() >= Constant.VERSION_STOCK_SYNC) {
                viewHodler.set_icon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.icon_back));
            } else {
                viewHodler.set_icon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.icon_init));
            }
        } else if (position == 12 && Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")).intValue() < Constant.VERSION_STOCK_SYNC) {
            viewHodler.set_icon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.icon_back));
        }
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
