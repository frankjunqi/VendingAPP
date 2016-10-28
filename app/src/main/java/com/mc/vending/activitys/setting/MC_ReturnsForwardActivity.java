package com.mc.vending.activitys.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.adapter.MC_ReturnForwardDetailAdapter;
import com.mc.vending.data.RetreatHeadData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnProductWrapperData;
import com.mc.vending.service.ReturnProductService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.DateHelper;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.StringHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MC_ReturnsForwardActivity extends BaseActivity {
    private MC_ReturnForwardDetailAdapter adapter;
    private TextView alert_msg;
    private TextView alert_msg_title;
    private Button back;
    private List<VendingChnProductWrapperData> dataList;
    private EditText et_order_number;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MC_ReturnsForwardActivity.this.stopLoading();
                    MC_ReturnsForwardActivity.this.resetAlertMsg((String) msg.obj);
                    return;
                case 5:
                    MC_ReturnsForwardActivity.this.stopLoading();
                    MC_ReturnsForwardActivity.this.resetAlertMsg((String) msg.obj);
                    Intent intent = new Intent();
                    intent.setClass(MC_ReturnsForwardActivity.this, MC_ReturnForwardListActivity.class);
                    MC_ReturnsForwardActivity.this.startActivityForResult(intent, 1000);
                    MC_ReturnsForwardActivity.this.finish();
                    return;
                case MC_SettingActivity.SET_SUCCESS /*999*/:
                    MC_ReturnsForwardActivity.this.adapter = new MC_ReturnForwardDetailAdapter(MC_ReturnsForwardActivity.this, MC_ReturnsForwardActivity.this.dataList, MC_ReturnsForwardActivity.this.listView, MC_ReturnsForwardActivity.this.retreatHeadData);
                    MC_ReturnsForwardActivity.this.listView.setAdapter(MC_ReturnsForwardActivity.this.adapter);
                    MC_ReturnsForwardActivity.this.stopLoading();
                    return;
                default:
                    return;
            }
        }
    };
    private ListView listView;
    private Button operate;
    private String orderNumber;
    private RetreatHeadData retreatHeadData;
    private TextView set_difference_replenishment;
    private TextView tv_number_title;
    private TextView tv_public_title;
    private VendingCardPowerWrapperData wrapperData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urgent_replenishment);
        ActivityManagerTool.getActivityManager().add(this);
        getParam();
        initComponents();
        initObject();
        startLoading();
    }

    private void getParam() {
        this.wrapperData = (VendingCardPowerWrapperData) getIntent().getSerializableExtra("wrapperData");
        this.retreatHeadData = (RetreatHeadData) getIntent().getSerializableExtra("retreatHeadData");
    }

    private void initComponents() {
        this.tv_public_title = (TextView) findViewById(R.id.tv_public_title);
        this.back = (Button) findViewById(R.id.back);
        this.operate = (Button) findViewById(R.id.operate);
        this.alert_msg_title = (TextView) findViewById(R.id.alert_msg_title);
        this.alert_msg = (TextView) findViewById(R.id.alert_msg);
        this.et_order_number = (EditText) findViewById(R.id.et_order_number);
        this.tv_number_title = (TextView) findViewById(R.id.tv_number_title);
        this.set_difference_replenishment = (TextView) findViewById(R.id.set_difference_replenishment);
        this.listView = (ListView) findViewById(R.id.listView);
    }

    private void initObject() {
        this.orderNumber = "T" + DateHelper.format(new Date(), "yyyy");
        this.tv_public_title.setText(getResources().getString(R.string.set_returns_forward));
        this.tv_number_title.setText(new StringBuilder(String.valueOf(getResources().getString(R.string.returns_order_id))).append(" ").append(this.orderNumber).toString());
        this.set_difference_replenishment.setText(getResources().getString(R.string.returns_number));
        this.et_order_number.setText(this.retreatHeadData.getRt1Rtcode().substring(5));
        this.back.setVisibility(0);
        this.back.setEnabled(true);
        this.operate.setVisibility(0);
        this.operate.setEnabled(true);
        this.operate.setText(getResources().getString(R.string.PUBLIC_SAVE));
        if (this.retreatHeadData.getRt1Status().equals("1")) {
            this.operate.setVisibility(4);
            this.operate.setEnabled(false);
        }
        this.dataList = new ArrayList();
        requestLst();
    }

    private void requestLst() {
        new Thread(new Runnable() {
            public void run() {
                MC_ReturnsForwardActivity.this.dataList = ReturnProductService.getInstance().getRetreatDetailsByRtId(MC_ReturnsForwardActivity.this.retreatHeadData.getRt1Id());
                MC_ReturnsForwardActivity.this.handler.sendEmptyMessage(MC_SettingActivity.SET_SUCCESS);
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
        if (StringHelper.isEmpty(this.et_order_number.getText().toString(), true)) {
            resetAlertMsg("请输入退货(正向)单号！");
            return;
        }
        for (VendingChnProductWrapperData data : this.dataList) {
            if (data.getActQty() > data.getStock()) {
                Message msg = this.handler.obtainMessage();
                msg.obj = "货道" + data.getVendingChn().getVc1Code() + "退货数量大于库存";
                msg.what = 0;
                this.handler.sendMessage(msg);
                return;
            }
        }
        startLoading();
        new Thread(new Runnable() {
            public void run() {
                ServiceResult<Boolean> result = ReturnProductService.getInstance().positiveReturnStockTransaction(new StringBuilder(String.valueOf(MC_ReturnsForwardActivity.this.orderNumber)).append(StringHelper.autoCompletionCode(StringHelper.trim(MC_ReturnsForwardActivity.this.et_order_number.getText().toString()))).toString(), MC_ReturnsForwardActivity.this.dataList, MC_ReturnsForwardActivity.this.wrapperData, MC_ReturnsForwardActivity.this.retreatHeadData.getRt1Id());
                if (result.isSuccess()) {
                    Message msg = MC_ReturnsForwardActivity.this.handler.obtainMessage();
                    msg.obj = "正向退货完成";
                    msg.what = 5;
                    MC_ReturnsForwardActivity.this.handler.sendMessage(msg);
                    return;
                }
                Message msg = MC_ReturnsForwardActivity.this.handler.obtainMessage();
                msg.obj = result.getMessage();
                msg.what = 0;
                MC_ReturnsForwardActivity.this.handler.sendMessage(msg);
            }
        }).start();
    }

    public void sumClicked(View view) {
        int index = Integer.parseInt(String.valueOf(view.getTag())) - 100;
        VendingChnProductWrapperData data = (VendingChnProductWrapperData) this.dataList.get(index);
        int number = data.getActQty() + 1;
        if (number > data.getStock()) {
            Message msg = this.handler.obtainMessage();
            msg.obj = "货道" + data.getVendingChn().getVc1Code() + "退货数量大于库存";
            msg.what = 0;
            this.handler.sendMessage(msg);
            return;
        }
        data.setActQty(number);
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

    private void resetTextView(String value, int serialType) {
        this.et_order_number.setText(new StringBuilder(String.valueOf(this.et_order_number.getText().toString())).append(value).toString());
    }
}
