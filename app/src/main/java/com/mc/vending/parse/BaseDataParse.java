package com.mc.vending.parse;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.BaseParseData;
import com.mc.vending.parse.listener.DataParseListener;

public class BaseDataParse implements DataParseListener {
    public void parseJson(BaseData baseData) {
    }

    public void parseRequestError(BaseData baseData) {
    }

    public void callBackLogversion(BaseParseData baseParseData) {
        new DataParseHelper(this).sendLogVersion(baseParseData.getLogVersion());
    }
}
