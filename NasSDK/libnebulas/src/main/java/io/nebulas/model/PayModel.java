package io.nebulas.model;

import android.os.Bundle;

/**
 * 转账详情类
 * Created by donald99 on 18/5/23.
 */

public class PayModel {

    public String to;       //目标地址 (*)
    public String value;	// 转账金额**  单位为wei (1NAS =10^18 wei) （*）
    public String currency; // 转账种类NAS

    public PayloadModel payload;

    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("to", to);
        bundle.putString("value", value);
        bundle.putString("currency", currency);
//        bundle.putBundle("payload", payload.toBundle());
        bundle.putAll(payload.toBundle());
        return bundle;
    }
}
