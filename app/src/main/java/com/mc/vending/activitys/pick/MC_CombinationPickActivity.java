package com.mc.vending.activitys.pick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.adapter.MC_CombinationPickAdapter;
import com.mc.vending.data.ProductGroupHeadData;
import com.mc.vending.data.ProductGroupHeadWrapperData;
import com.mc.vending.data.VendingData;
import com.mc.vending.service.CompositeMaterialService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.utils.MC_SerialToolsListener;
import com.mc.vending.tools.utils.SerialTools;
import java.util.List;

public class MC_CombinationPickActivity extends BaseActivity implements MC_SerialToolsListener {
    private MC_CombinationPickAdapter adapter;
    private TextView alert_msg;
    private TextView alert_msg_title;
    private Button back;
    private String combinationNumber = "";
    private List<ProductGroupHeadData> dataList;
    private EditText et_number;
    private ListView listView;
    private TextView tv_public_title;
    private VendingData vendData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combination_pick);
        ActivityManagerTool.getActivityManager().add(this);
        getParam();
        initComponents();
        initObject();
        stopLoading();
    }

    private void getParam() {
        this.vendData = (VendingData) getIntent().getSerializableExtra("vendData");
    }

    private void initComponents() {
        this.tv_public_title = (TextView) findViewById(R.id.tv_public_title);
        this.back = (Button) findViewById(R.id.back);
        this.listView = (ListView) findViewById(R.id.listView);
        this.alert_msg_title = (TextView) findViewById(R.id.alert_msg_title);
        this.alert_msg = (TextView) findViewById(R.id.alert_msg);
        this.et_number = (EditText) findViewById(R.id.et_number);
    }

    private void initObject() {
        this.tv_public_title.setText(getResources().getString(R.string.combination_pick));
        ServiceResult<List<ProductGroupHeadData>> result = CompositeMaterialService.getInstance().findAllProductGroupHead();
        if (result.isSuccess()) {
            this.dataList = (List) result.getResult();
            this.adapter = new MC_CombinationPickAdapter(this, this.dataList, this.listView);
            this.listView.setAdapter(this.adapter);
            return;
        }
        resetAlertMsg(result.getMessage());
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
        openKeyBoard();
        this.combinationNumber = "";
        this.et_number.setText("");
        super.onResume();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        ActivityManagerTool.getActivityManager().removeActivity(this);
        super.onDestroy();
    }

    private void openKeyBoard() {
        SerialTools.getInstance().openKeyBoard();
        SerialTools.getInstance().addToolsListener(this);
    }

    public void serialReturn(String value, int serialType) {
        switch (serialType) {
            case 1:
                keyBoardReturn(value, serialType);
                return;
            default:
                return;
        }
    }

    private void keyBoardReturn(String value, int serialType) {
        if (!SerialTools.FUNCTION_KEY_COMBINATION.equals(value) && !SerialTools.FUNCTION_KEY_BORROW.equals(value) && !SerialTools.FUNCTION_KEY_BACK.equals(value) && !SerialTools.FUNCTION_KEY_SET.equals(value)) {
            if (SerialTools.FUNCTION_KEY_CANCEL.equals(value)) {
                hiddenAlertMsg();
                if (StringHelper.isEmpty(this.combinationNumber, true)) {
                    finish();
                    return;
                }
                this.combinationNumber = "";
                this.et_number.setText("");
            } else if (!SerialTools.FUNCTION_KEY_CONFIRM.equals(value)) {
                resetTextView(SerialTools.getInstance().getKeyValue(value), serialType);
                if (!StringHelper.isEmpty(SerialTools.getInstance().getKeyValue(value))) {
                    this.combinationNumber += SerialTools.getInstance().getKeyValue(value);
                }
            } else if (StringHelper.isEmpty(this.combinationNumber, true)) {
                resetAlertMsg("请选择组合编号,再选择输入按键！");
            } else {
                checkProduct();
            }
        }
    }

    private void checkProduct() {
        ServiceResult<ProductGroupHeadWrapperData> result = CompositeMaterialService.getInstance().checkProductGroupStock(this.combinationNumber);
        if (result.isSuccess()) {
            ProductGroupHeadWrapperData data = (ProductGroupHeadWrapperData) result.getResult();
            if (data.getWrapperList() == null) {
                resetAlertMsg("组合产品为空！");
                this.combinationNumber = "";
                this.et_number.setText("");
                return;
            }
            hiddenAlertMsg();
            Intent intent = new Intent();
            intent.putExtra("ProductGroupHeadWrapperData", data);
            intent.putExtra("vendData", this.vendData);
            intent.putExtra("combinationNumber", this.combinationNumber);
            intent.putExtra("Pg1Id", data.getPg1Id());
            intent.setClass(this, MC_CombinationPickDetailActivity.class);
            startActivityForResult(intent, 1000);
            return;
        }
        resetAlertMsg(result.getMessage());
        this.combinationNumber = "";
        this.et_number.setText("");
    }

    public void serialReturn(String value, int serialType, Object userInfo) {
    }

    private void resetTextView(String value, int serialType) {
        if (1 == serialType) {
            this.et_number.setText(new StringBuilder(String.valueOf(this.et_number.getText().toString())).append(value).toString());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000 && resultCode == 1001) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
