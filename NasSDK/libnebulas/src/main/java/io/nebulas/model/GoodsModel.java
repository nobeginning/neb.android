package io.nebulas.model;

import android.os.Bundle;

/**
 * 商品类
 * Created by donald99 on 18/5/23.
 */

public class GoodsModel {

    public String desc;
    public String name;

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("desc", desc);
        bundle.putString("name", name);
        return bundle;
    }
}
