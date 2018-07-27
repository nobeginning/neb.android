package io.nebulas.model;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by donald99 on 18/6/1.
 */

public class AccountState {
    public String address;
    public String balance;
    public int nonce;
    public String type;

    public String toJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("address", address);
            jsonObject.put("balance", balance);
            jsonObject.put("nonce", nonce);
            jsonObject.put("type", type);
        } catch (Exception e) {
            Log.e("AccountState", "To JSON Error : " + e.getMessage());
        }
        return jsonObject.toString();
    }
}
