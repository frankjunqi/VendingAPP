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
import com.mc.vending.data.ReplenishmentHeadData;
import java.util.ArrayList;
import java.util.List;

public class MC_DifferenceReplenishmentOrderAdapter extends BaseAdapter {
    Activity context;
    private List<ReplenishmentHeadData> dataList;
    private LayoutInflater inflater;

    class ViewHodler {
        TextView replenishment_order_number;

        ViewHodler() {
        }
    }

    public MC_DifferenceReplenishmentOrderAdapter(Context context, List<ReplenishmentHeadData> dataList, ListView lv) {
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
            convertView = this.inflater.inflate(R.layout.difference_replenishment_order_item, parent, false);
            viewHodler.replenishment_order_number = (TextView) convertView.findViewById(R.id.replenishment_order_number);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        viewHodler.replenishment_order_number.setText("补货单号：" + ((ReplenishmentHeadData) getItem(position)).getRh1Rhcode());
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
