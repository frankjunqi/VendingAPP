package com.mc.vending.activitys.setting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.adapter.MC_UrgentReplenishmentAdapter;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnProductWrapperData;
import com.mc.vending.service.ReturnProductService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.ServiceResult;
import java.util.ArrayList;
import java.util.List;

public class MC_ReturnsReverseActivity extends BaseActivity {
    private MC_UrgentReplenishmentAdapter adapter;
    private TextView alert_msg;
    private TextView alert_msg_title;
    private Button back;
    private List<VendingChnProductWrapperData> dataList;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MC_ReturnsReverseActivity.this.stopLoading();
                    MC_ReturnsReverseActivity.this.resetAlertMsg((String) msg.obj);
                    return;
                case 6:
                    MC_ReturnsReverseActivity.this.stopLoading();
                    MC_ReturnsReverseActivity.this.resetAlertMsg((String) msg.obj);
                    MC_ReturnsReverseActivity.this.finish();
                    return;
                case MC_SettingActivity.SET_SUCCESS /*999*/:
                    MC_ReturnsReverseActivity.this.adapter = new MC_UrgentReplenishmentAdapter(MC_ReturnsReverseActivity.this, MC_ReturnsReverseActivity.this.dataList, MC_ReturnsReverseActivity.this.listView);
                    MC_ReturnsReverseActivity.this.listView.setAdapter(MC_ReturnsReverseActivity.this.adapter);
                    MC_ReturnsReverseActivity.this.stopLoading();
                    return;
                default:
                    return;
            }
        }
    };
    private ListView listView;
    private Button operate;
    private TextView set_difference_replenishment;
    private TextView tv_public_title;
    private VendingCardPowerWrapperData wrapperData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returns_reverse);
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
        this.operate = (Button) findViewById(R.id.operate);
        this.set_difference_replenishment = (TextView) findViewById(R.id.set_difference_replenishment);
        this.alert_msg_title = (TextView) findViewById(R.id.alert_msg_title);
        this.alert_msg = (TextView) findViewById(R.id.alert_msg);
        this.listView = (ListView) findViewById(R.id.listView);
    }

    private void initObject() {
        this.tv_public_title.setText(getResources().getString(R.string.set_returns_reverse));
        this.set_difference_replenishment.setText(getResources().getString(R.string.returns_number));
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
                MC_ReturnsReverseActivity.this.dataList = ReturnProductService.getInstance().findChnCodeProductName();
                MC_ReturnsReverseActivity.this.handler.sendEmptyMessage(MC_SettingActivity.SET_SUCCESS);
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
        startLoading();
        new Thread(new Runnable() {
            public void run() {
                ServiceResult<Boolean> result = ReturnProductService.getInstance().reverseReturnStockTransaction(MC_ReturnsReverseActivity.this.dataList, MC_ReturnsReverseActivity.this.wrapperData);
                if (result.isSuccess()) {
                    Message msg = MC_ReturnsReverseActivity.this.handler.obtainMessage();
                    msg.obj = "反向退货完成";
                    msg.what = 6;
                    MC_ReturnsReverseActivity.this.handler.sendMessage(msg);
                    return;
                }
                Message msg = MC_ReturnsReverseActivity.this.handler.obtainMessage();
                msg.obj = result.getMessage();
                msg.what = 0;
                MC_ReturnsReverseActivity.this.handler.sendMessage(msg);
            }
        }).start();
    }

    public void sumClicked(View view) {
        int index = Integer.parseInt(String.valueOf(view.getTag())) - 100;
        VendingChnProductWrapperData data = (VendingChnProductWrapperData) this.dataList.get(index);
        data.setActQty(data.getActQty() + 1);
        reloadTableWithLine(index);
    }

    public void subClicked(View view) {
        int index = Integer.parseInt(String.valueOf(view.getTag()));
        VendingChnProductWrapperData data = (VendingChnProductWrapperData) this.dataList.get(index);
        int number = data.getActQty();
        if (number > 0) {
            number--;
        }
        data.setActQty(number);
        reloadTableWithLine(index);
    }

    private void reloadTableWithLine(int index) {
        this.adapter.reloadViewHolder(index);
    }
}
