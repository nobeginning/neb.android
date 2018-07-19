package io.nebulas.model;

import android.os.Bundle;

import java.util.ArrayList;

/**
 * payload 详情类
 * Created by donald99 on 18/5/23.
 */

public class PayloadModel {

    public String type;      // 调用合约
    public String function;  // 合约中的方法名(*)
    public String[] args;    // 函数参数列表 (*)
    public String source;
    public String sourceType;

    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("function", function);
        bundle.putStringArray("args", args);
        bundle.putString("source", source);
        bundle.putString("sourceType", sourceType);
        return bundle;
    }
}
