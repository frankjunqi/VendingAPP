package com.mc.vending.parse.listener;

import com.mc.vending.data.BaseData;

public interface DataParseListener {
    void parseJson(BaseData baseData);

    void parseRequestError(BaseData baseData);
}
