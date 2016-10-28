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
import com.mc.vending.adapter.MC_ReurnForwardListAdapter;
import com.mc.vending.data.RetreatHeadData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.service.ReturnProductService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.ServiceResult;
import java.util.ArrayList;
import java.util.List;

public class MC_ReturnForwardListActivity extends BaseActivity {
    private static final int DETAIL = 0;
    private MC_ReurnForwardListAdapter adapter;
    private TextView alert_msg;
    private TextView alert_msg_title;
    private Button back;
    private List<RetreatHeadData> dataList;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MC_SettingActivity.SET_SELECT /*998*/:
                    MC_ReturnForwardListActivity.this.goRetreatHeadDataActivity(Integer.parseInt((String) msg.obj));
                    return;
                case MC_SettingActivity.SET_SUCCESS /*999*/:
                    if (MC_ReturnForwardListActivity.this.dataList.size() > 0) {
                        MC_ReturnForwardListActivity.this.adapter = new MC_ReurnForwardListAdapter(MC_ReturnForwardListActivity.this, MC_ReturnForwardListActivity.this.dataList, MC_ReturnForwardListActivity.this.listView);
                        MC_ReturnForwardListActivity.this.listView.setAdapter(MC_ReturnForwardListActivity.this.adapter);
                    } else {
                        MC_ReturnForwardListActivity.this.resetAlertMsg("没有退货单");
                    }
                    MC_ReturnForwardListActivity.this.stopLoading();
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
        setContentView(R.layout.activity_return_forward_list);
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
        this.tv_public_title.setText(getResources().getString(R.string.set_returns_forward_list));
        this.back.setVisibility(0);
        this.back.setEnabled(true);
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
                Message msg = new Message();
                msg.what = MC_SettingActivity.SET_SELECT;
                msg.obj = String.valueOf(position);
                MC_ReturnForwardListActivity.this.handler.sendMessage(msg);
            }
        });
        this.dataList = new ArrayList();
        requestLst();
    }

    private void requestLst() {
        new Thread(new Runnable() {
            public void run() {
                ServiceResult<List<RetreatHeadData>> result = ReturnProductService.getInstance().getRetreatHeadList();
                if (result.isSuccess()) {
                    MC_ReturnForwardListActivity.this.dataList = (List) result.getResult();
                    Message msg = new Message();
                    msg.what = MC_SettingActivity.SET_SUCCESS;
                    MC_ReturnForwardListActivity.this.handler.sendMessage(msg);
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

    private void goRetreatHeadDataActivity(int position) {
        startLoading();
        RetreatHeadData data = (RetreatHeadData) this.dataList.get(position);
        Intent intent = new Intent();
        intent.putExtra("retreatHeadData", data);
        intent.putExtra("wrapperData", this.wrapperData);
        intent.setClass(this, MC_ReturnsForwardActivity.class);
        startActivityForResult(intent, 1000);
        finish();
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
