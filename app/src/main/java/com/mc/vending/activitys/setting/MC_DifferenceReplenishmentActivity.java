package com.mc.vending.activitys.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.adapter.MC_DifferenceReplenishmentAdapter;
import com.mc.vending.data.ReplenishmentDetailWrapperData;
import com.mc.vending.data.ReplenishmentHeadData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.service.ReplenishmentService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.ServiceResult;
import java.util.ArrayList;
import java.util.List;

public class MC_DifferenceReplenishmentActivity extends BaseActivity {
    private MC_DifferenceReplenishmentAdapter adapter;
    private TextView alert_msg;
    private TextView alert_msg_title;
    private Button back;
    private List<ReplenishmentDetailWrapperData> dataList;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MC_DifferenceReplenishmentActivity.this.resetAlertMsg((String) msg.obj);
                    return;
                case 2:
                    MC_DifferenceReplenishmentActivity.this.resetAlertMsg((String) msg.obj);
                    MC_DifferenceReplenishmentActivity.this.goBack();
                    return;
                case MC_SettingActivity.SET_SUCCESS /*999*/:
                    MC_DifferenceReplenishmentActivity.this.adapter = new MC_DifferenceReplenishmentAdapter(MC_DifferenceReplenishmentActivity.this, MC_DifferenceReplenishmentActivity.this.dataList, MC_DifferenceReplenishmentActivity.this.listView);
                    MC_DifferenceReplenishmentActivity.this.listView.setAdapter(MC_DifferenceReplenishmentActivity.this.adapter);
                    MC_DifferenceReplenishmentActivity.this.stopLoading();
                    return;
                default:
                    return;
            }
        }
    };
    private ReplenishmentHeadData headData;
    private ListView listView;
    private Button operate;
    private TextView tv_public_title;
    private VendingCardPowerWrapperData wrapperData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difference_replenishment);
        ActivityManagerTool.getActivityManager().add(this);
        getParam();
        initComponents();
        initObject();
        startLoading();
    }

    private void getParam() {
        this.headData = (ReplenishmentHeadData) getIntent().getSerializableExtra("ReplenishmentHeadData");
        this.wrapperData = (VendingCardPowerWrapperData) getIntent().getSerializableExtra("wrapperData");
    }

    private void initComponents() {
        this.tv_public_title = (TextView) findViewById(R.id.tv_public_title);
        this.alert_msg_title = (TextView) findViewById(R.id.alert_msg_title);
        this.alert_msg = (TextView) findViewById(R.id.alert_msg);
        this.back = (Button) findViewById(R.id.back);
        this.operate = (Button) findViewById(R.id.operate);
        this.listView = (ListView) findViewById(R.id.listView);
    }

    private void initObject() {
        this.tv_public_title.setText(getResources().getString(R.string.set_difference_replenishment));
        this.back.setVisibility(0);
        this.back.setEnabled(true);
        this.operate.setVisibility(0);
        this.operate.setEnabled(true);
        this.operate.setText(getResources().getString(R.string.PUBLIC_SAVE));
        this.dataList = new ArrayList();
        requestLst();
    }

    private void requestLst() {
        new Thread(new Runnable() {
            public void run() {
                MC_DifferenceReplenishmentActivity.this.dataList = ReplenishmentService.getInstance().findReplenishmentDetail(MC_DifferenceReplenishmentActivity.this.headData.getRh1Id());
                Message msg = new Message();
                msg.what = MC_SettingActivity.SET_SUCCESS;
                MC_DifferenceReplenishmentActivity.this.handler.sendMessage(msg);
            }
        }).start();
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

    public void backClicked(View view) {
        setResult(1002, new Intent(this, MC_DifferenceReplenishmentOrderActivity.class));
        finish();
    }

    private void goBack() {
        setResult(1001, new Intent(this, MC_DifferenceReplenishmentOrderActivity.class));
        finish();
    }

    public void saveClicked(View view) {
        startLoading();
        new Thread(new Runnable() {
            public void run() {
                ServiceResult<Boolean> result = ReplenishmentService.getInstance().updateReplenishmentDetail(MC_DifferenceReplenishmentActivity.this.headData, MC_DifferenceReplenishmentActivity.this.dataList, MC_DifferenceReplenishmentActivity.this.wrapperData, "1");
                if (result.isSuccess()) {
                    Message msg = MC_DifferenceReplenishmentActivity.this.handler.obtainMessage();
                    msg.obj = "差异补货完成";
                    msg.what = 2;
                    MC_DifferenceReplenishmentActivity.this.handler.sendMessage(msg);
                    return;
                }
                Message msg = MC_DifferenceReplenishmentActivity.this.handler.obtainMessage();
                msg.obj = result.getMessage();
                msg.what = 0;
                MC_DifferenceReplenishmentActivity.this.handler.sendMessage(msg);
            }
        }).start();
    }

    public void sumClicked(View view) {
        int index = Integer.parseInt(String.valueOf(view.getTag())) - 100;
        ReplenishmentDetailWrapperData data = (ReplenishmentDetailWrapperData) this.dataList.get(index);
        data.getReplenishmentDetail().setRh2DifferentiaQty(Integer.valueOf(data.getReplenishmentDetail().getRh2DifferentiaQty().intValue() + 1));
        reloadTableWithLine(index);
    }

    public void subClicked(View view) {
        int index = Integer.parseInt(String.valueOf(view.getTag()));
        ReplenishmentDetailWrapperData data = (ReplenishmentDetailWrapperData) this.dataList.get(index);
        int number = data.getReplenishmentDetail().getRh2DifferentiaQty().intValue();
        if (number > (-data.getReplenishmentDetail().getRh2ActualQty().intValue())) {
            number--;
        }
        data.getReplenishmentDetail().setRh2DifferentiaQty(Integer.valueOf(number));
        reloadTableWithLine(index);
    }

    private void reloadTableWithLine(int index) {
        this.adapter.reloadViewHolder(index);
    }
}
