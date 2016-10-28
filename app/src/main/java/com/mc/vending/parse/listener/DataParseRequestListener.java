package com.mc.vending.parse.listener;

import com.mc.vending.data.BaseData;

public interface DataParseRequestListener {
    void parseRequestFailure(BaseData baseData);

    void parseRequestFinised(BaseData baseData);
}
