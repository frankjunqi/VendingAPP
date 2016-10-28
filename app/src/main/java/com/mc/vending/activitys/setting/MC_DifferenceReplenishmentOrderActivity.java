package com.mc.vending.activitys.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.adapter.MC_DifferenceReplenishmentOrderAdapter;
import com.mc.vending.data.ReplenishmentHeadData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.service.ReplenishmentService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.ServiceResult;
import java.util.ArrayList;
import java.util.List;

public class MC_DifferenceReplenishmentOrderActivity extends BaseActivity {
    private static final int DETAIL = 0;
    private MC_DifferenceReplenishmentOrderAdapter adapter;
    private TextView alert_msg;
    private TextView alert_msg_title;
    private Button back;
    private List<ReplenishmentHeadData> dataList;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MC_SettingActivity.SET_SELECT /*998*/:
                    MC_DifferenceReplenishmentOrderActivity.this.goDifferenceReplenishmentActivity(Integer.parseInt((String) msg.obj));
                    return;
                case MC_SettingActivity.SET_SUCCESS /*999*/:
                    if (MC_DifferenceReplenishmentOrderActivity.this.dataList.size() > 0) {
                        MC_DifferenceReplenishmentOrderActivity.this.adapter = new MC_DifferenceReplenishmentOrderAdapter(MC_DifferenceReplenishmentOrderActivity.this, MC_DifferenceReplenishmentOrderActivity.this.dataList, MC_DifferenceReplenishmentOrderActivity.this.listView);
                        MC_DifferenceReplenishmentOrderActivity.this.listView.setAdapter(MC_DifferenceReplenishmentOrderActivity.this.adapter);
                    } else {
                        MC_DifferenceReplenishmentOrderActivity.this.resetAlertMsg("没有补货单，请联系管理员或先进行一键补货！");
                    }
                    MC_DifferenceReplenishmentOrderActivity.this.stopLoading();
                    return;
                default:
                    return;
            }
        }
    };
    private ListView listView;
    private TextView tv_public_title;
    private VendingCardPowerWrapperData wrapperData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difference_replenishment_order);
        ActivityManagerTool.getActivityManager().add(this);
        getParam();
        initComponents();
        initObject();
        startLoading();
    }

    private void getParam() {
        this.wrapperData = (VendingCardPowerWrapperData) getIntent().getSerializableExtra("wrapperData");
    }

    private void initComponents() {
        this.tv_public_title = (TextView) findViewById(R.id.tv_public_title);
        this.back = (Button) findViewById(R.id.back);
        this.listView = (ListView) findViewById(R.id.listView);
        this.alert_msg_title = (TextView) findViewById(R.id.alert_msg_title);
        this.alert_msg = (TextView) findViewById(R.id.alert_msg);
    }

    private void initObject() {
        this.tv_public_title.setText(getResources().getString(R.string.set_difference_replenishment));
        this.back.setVisibility(0);
        this.back.setEnabled(true);
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
                Message msg = new Message();
                msg.what = MC_SettingActivity.SET_SELECT;
                msg.obj = String.valueOf(position);
                MC_DifferenceReplenishmentOrderActivity.this.handler.sendMessage(msg);
            }
        });
        this.dataList = new ArrayList();
        requestLst();
    }

    private void requestLst() {
        new Thread(new Runnable() {
            public void run() {
                ServiceResult<List<ReplenishmentHeadData>> result = ReplenishmentService.getInstance().getReplenishmentHead("0");
                if (result.isSuccess()) {
                    MC_DifferenceReplenishmentOrderActivity.this.dataList = (List) result.getResult();
                    Message msg = new Message();
                    msg.what = MC_SettingActivity.SET_SUCCESS;
                    MC_DifferenceReplenishmentOrderActivity.this.handler.sendMessage(msg);
                }
            }
        }).start();
    }

    private void resetAlertMsg(String msg) {
        this.alert_msg.setText(msg);
        this.alert_msg_title.setVisibility(0);
        this.alert_msg.setVisibility(0);
    }

    private void hiddenAlertMsg() {
        this.alert_msg.setText("");
        this.alert_msg_title.setVisibility(4);
        this.alert_msg.setVisibility(4);
    }

    private void goDifferenceReplenishmentActivity(int position) {
        startLoading();
        ReplenishmentHeadData data = (ReplenishmentHeadData) this.dataList.get(position);
        Intent intent = new Intent();
        intent.putExtra("ReplenishmentHeadData", data);
        intent.putExtra("wrapperData", this.wrapperData);
        intent.setClass(this, MC_DifferenceReplenishmentActivity.class);
        startActivityForResult(intent, 1000);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000 && resultCode == 1001) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        ActivityManagerTool.getActivityManager().removeActivity(this);
        super.onDestroy();
    }

    public void backClicked(View view) {
        finish();
    }

    public void saveClicked(View view) {
    }
}
