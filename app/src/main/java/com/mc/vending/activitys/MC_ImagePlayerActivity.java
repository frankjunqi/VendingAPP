package com.mc.vending.activitys;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.mc.vending.R;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.data.VendingPictureData;
import com.mc.vending.db.VendingPictureDbOper;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.AsyncImageLoader;
import com.mc.vending.tools.AsyncImageLoader.ImageCallback;
import com.mc.vending.tools.ZillionLog;
import com.mc.vending.tools.utils.MC_SerialToolsListener;
import com.mc.vending.tools.utils.SerialTools;
import com.zillion.evm.jssc.SerialPortException;
import java.util.List;

public class MC_ImagePlayerActivity extends BaseActivity implements MC_SerialToolsListener {
    private final int IMAGE_PLAY = 1001;
    private AsyncImageLoader asyncImageLoader;
    private int currentindex;
    private ImageView image_player;
    boolean isStop;
    public final Handler mHander = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String value = (String) msg.obj;
                    if (SerialTools.FUNCTION_KEY_COMBINATION.equals(value) || SerialTools.FUNCTION_KEY_BORROW.equals(value) || SerialTools.FUNCTION_KEY_BACK.equals(value) || SerialTools.FUNCTION_KEY_SET.equals(value) || SerialTools.FUNCTION_KEY_CANCEL.equals(value) || SerialTools.FUNCTION_KEY_CONFIRM.equals(value) || "0".equals(SerialTools.getInstance().getKeyValue(value)) || "1".equals(SerialTools.getInstance().getKeyValue(value)) || "2".equals(SerialTools.getInstance().getKeyValue(value)) || "3".equals(SerialTools.getInstance().getKeyValue(value)) || StockTransactionData.BILL_TYPE_GET.equals(SerialTools.getInstance().getKeyValue(value)) || StockTransactionData.BILL_TYPE_RETURN.equals(SerialTools.getInstance().getKeyValue(value)) || StockTransactionData.BILL_TYPE_BORROW.equals(SerialTools.getInstance().getKeyValue(value)) || StockTransactionData.BILL_TYPE_All.equals(SerialTools.getInstance().getKeyValue(value)) || StockTransactionData.BILL_TYPE_DIFFAll.equals(SerialTools.getInstance().getKeyValue(value)) || "9".equals(SerialTools.getInstance().getKeyValue(value))) {
                        MC_ImagePlayerActivity.this.isStop = true;
                        MC_ImagePlayerActivity.this.finish();
                        return;
                    }
                    return;
                case 1001:
                    MC_ImagePlayerActivity.this.showNext();
                    return;
                default:
                    return;
            }
        }
    };
    private List<VendingPictureData> pictrueList;

    protected void onCreate(Bundle savedInstanceState) {
        stopLoading();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_player);
        ActivityManagerTool.getActivityManager().add(this);
        initComponents();
        initObject();
    }

    private void initComponents() {
        this.image_player = (ImageView) findViewById(R.id.image_player);
        this.asyncImageLoader = new AsyncImageLoader();
    }

    private void initObject() {
        this.currentindex = 0;
        this.isStop = false;
        this.pictrueList = new VendingPictureDbOper().findVendingPicture();
        showNext();
    }

    private void openKeyBoard() {
        SerialTools.getInstance().addToolsListener(this);
        SerialTools.getInstance().openKeyBoard();
    }

    private void closeKeyBoard() {
        try {
            SerialTools.getInstance().addToolsListener(this);
            SerialTools.getInstance().closeKeyBoard();
        } catch (SerialPortException e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
            e.printStackTrace();
        }
    }

    protected void onResume() {
        openKeyBoard();
        super.onResume();
    }

    private void showNext() {
        if (!this.isStop && this.pictrueList.size() > 0) {
            VendingPictureData data = (VendingPictureData) this.pictrueList.get(this.currentindex);
            loadImage(data.getVp2FilePath());
            Message msg = new Message();
            msg.what = 1001;
            this.mHander.sendMessageDelayed(msg, (long) (data.getVp2RunTime() * 1000));
            if (this.currentindex < this.pictrueList.size() - 1) {
                this.currentindex++;
            } else {
                this.currentindex = 0;
            }
        }
    }

    private void loadImage(String imageURL) {
        Drawable cachedImage = this.asyncImageLoader.loadDrawable(imageURL, new ImageCallback() {
            public void imageLoaded(Drawable imageDrawable, String imageUrl, String tag) {
                MC_ImagePlayerActivity.this.image_player.setImageDrawable(imageDrawable);
            }
        });
        if (cachedImage != null) {
            this.image_player.setImageDrawable(cachedImage);
        }
    }

    public void imagePlayerClicked(View view) {
        this.isStop = true;
        finish();
    }

    public void serialReturn(String value, int serialType) {
        switch (serialType) {
            case 1:
                Message msg = new Message();
                msg.what = serialType;
                msg.obj = value;
                this.mHander.sendMessage(msg);
                return;
            default:
                return;
        }
    }

    public void serialReturn(String value, int serialType, Object userInfo) {
    }
}
