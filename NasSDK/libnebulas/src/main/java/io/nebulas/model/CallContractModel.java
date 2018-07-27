package io.nebulas.model;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by donald99 on 18/6/1.
 */

public class CallContractModel {
    public String from;
    public String to;
    public String value;
    public String gasLimit;
    public String gasPrice;
    public int nonce;
    public ContractModel contract;

    public String toJsonString(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("from", from);
            jsonObject.put("to", to);
            jsonObject.put("value", value);
            jsonObject.put("gasLimit", gasLimit);
            jsonObject.put("gasPrice", gasPrice);
            jsonObject.put("nonce", nonce);
            jsonObject.put("contract", contract.toJsonObject());
        } catch (Exception e) {
            Log.e("CallContractModel", "To JSON Error : " + e.getMessage());
        }
        return jsonObject.toString();
    }
}
