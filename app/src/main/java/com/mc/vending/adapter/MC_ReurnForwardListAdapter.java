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
import com.mc.vending.data.RetreatHeadData;
import java.util.ArrayList;
import java.util.List;

public class MC_ReurnForwardListAdapter extends BaseAdapter {
    Activity context;
    private List<RetreatHeadData> dataList;
    private LayoutInflater inflater;

    class ViewHodler {
        TextView replenishment_order_number;

        ViewHodler() {
        }
    }

    public MC_ReurnForwardListAdapter(Context context, List<RetreatHeadData> dataList, ListView lv) {
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
        RetreatHeadData data = (RetreatHeadData) getItem(position);
        viewHodler.replenishment_order_number.setText("退货单号：" + data.getRt1Rtcode() + "(" + (data.getRt1Status().equals("0") ? "创建" : "完成") + ")");
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
