package io.nebulas.model;

import android.os.Bundle;

/**
 * 页面参数详情类
 * Created by donald99 on 18/5/23.
 */

public class TransferPageParamsModel extends PageParamsModel {

    public PayModel pay;

    public GoodsModel goods;

    public GasInfo gasInfo;

    public String serialNumber;

    public String callback;

    @Override
    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        bundle.putAll(pay.toBundle());
        bundle.putAll(goods.toBundle());
        bundle.putAll(gasInfo.toBundle());
//        bundle.putBundle("pay", pay.toBundle());
//        bundle.putBundle("goods", goods.toBundle());
        bundle.putString("serialNumber", serialNumber);
        bundle.putString("callback", callback);
        return bundle;
    }
}
