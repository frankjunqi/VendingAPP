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
import com.mc.vending.data.ProductGroupWrapperData;
import java.util.ArrayList;
import java.util.List;

public class MC_CombinationPickDetailAdapter extends BaseAdapter {
    Activity context;
    private List<ProductGroupWrapperData> dataList;
    private LayoutInflater inflater;

    class ViewHodler {
        TextView sku_name;
        TextView sku_number;

        ViewHodler() {
        }
    }

    public MC_CombinationPickDetailAdapter(Context context, List<ProductGroupWrapperData> dataList, ListView lv) {
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
            convertView = this.inflater.inflate(R.layout.combination_pick_detail_item, parent, false);
            viewHodler.sku_name = (TextView) convertView.findViewById(R.id.sku_name);
            viewHodler.sku_number = (TextView) convertView.findViewById(R.id.sku_number);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        ProductGroupWrapperData data = (ProductGroupWrapperData) getItem(position);
        viewHodler.sku_name.setText(data.getProductName());
        viewHodler.sku_number.setText(String.valueOf(data.getGroupQty()));
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
