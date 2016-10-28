package com.mc.vending.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.data.VendingChnProductWrapperData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MC_UrgentReplenishmentAdapter extends BaseAdapter {
    Activity context;
    private List<VendingChnProductWrapperData> dataList;
    private LayoutInflater inflater;
    private Map<String, ViewHodler> viewMap;

    class ViewHodler {
        Button btn_sub;
        Button btn_sum;
        TextView channle_number;
        EditText replenishment_number;
        TextView sku_name;

        ViewHodler() {
        }

        public String toString() {
            return "ViewHodler [channle_number=" + this.channle_number + ", sku_name=" + this.sku_name + ", replenishment_number=" + this.replenishment_number + ", btn_sub=" + this.btn_sub + ", btn_sum=" + this.btn_sum + "]";
        }
    }

    public MC_UrgentReplenishmentAdapter(Context context, List<VendingChnProductWrapperData> dataList, ListView lv) {
        if (dataList == null) {
            this.dataList = new ArrayList();
        } else {
            this.dataList = dataList;
        }
        this.viewMap = new HashMap();
        this.context = (Activity) context;
        this.inflater = this.context.getLayoutInflater();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = this.inflater.inflate(R.layout.urgent_replenishment_item, parent, false);
            viewHodler.channle_number = (TextView) convertView.findViewById(R.id.channle_number);
            viewHodler.sku_name = (TextView) convertView.findViewById(R.id.sku_name);
            viewHodler.replenishment_number = (EditText) convertView.findViewById(R.id.replenishment_number);
            viewHodler.btn_sub = (Button) convertView.findViewById(R.id.btn_sub);
            viewHodler.btn_sum = (Button) convertView.findViewById(R.id.btn_sum);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        VendingChnProductWrapperData data = (VendingChnProductWrapperData) getItem(position);
        viewHodler.channle_number.setText(data.getVendingChn().getVc1Code());
        viewHodler.sku_name.setText(data.getProductName());
        viewHodler.replenishment_number.setText(String.valueOf(data.getActQty()));
        viewHodler.btn_sub.setTag(Integer.valueOf(position));
        viewHodler.btn_sum.setTag(Integer.valueOf(position + 100));
        convertView.setTag(viewHodler);
        this.viewMap.put(String.valueOf(position), viewHodler);
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

    public void reloadViewHolder(int index) {
        ((ViewHodler) this.viewMap.get(String.valueOf(index))).replenishment_number.setText(String.valueOf(((VendingChnProductWrapperData) getItem(index)).getActQty()));
    }
}
