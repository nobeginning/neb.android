package io.nebulas.model;

import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by donald99 on 18/6/1.
 */

public class ContractModel {
    private String args;//[1, 0, 1]
    public String function;

    private transient List<String> list = new ArrayList<>();

    /**
     * 按顺序添加，智能合约需要的参数
     * @param arg
     */
    public void addArg(int arg){
        list.add(String.valueOf(arg));
        args = Arrays.toString(list.toArray());
    }

    /**
     * 按顺序添加，智能合约需要的参数
     * @param arg
     */
    public void addArg(String arg){
        list.add(arg);
        args = Arrays.toString(list.toArray());
    }

    public JSONObject toJsonObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("args", args);
            jsonObject.put("function", function);
        } catch (Exception e) {
            Log.e("ContractModel", "To JSON Error : " + e.getMessage());
        }
        return jsonObject;
    }
}