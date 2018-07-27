package io.nebulas.nas.transfer;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import io.nebulas.Constants;
import io.nebulas.api.SmartContracts;
import io.nebulas.utils.Util;

/**
 * 此代码只是为了演示Java代码的接入示例
 */
public class TransferJavaCodeSample {

    private Button button;
    private Context context;

    private void invokeSample(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmartContracts.pay(
                        context,
                        Constants.TEST_NET,
                        "<toAddress>",
                        "1000000000000000000",
                        "goodsName -> Nullable/可以传null",
                        "goodsDesc -> Nullable/可以传null",
                        Util.getRandomCode(),
                        "gasPrice -> Nullable/可以传null",
                        "gasLimit -> Nullable/可以传null");
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmartContracts.call(
                        context,
                        Constants.TEST_NET,
                        "<toAddress>",
                        "1000000000000000000",
                        "functionName",
                        new String[]{"1", "2", "1"},
                        "goodsName -> Nullable/可以传null",
                        "goodsDesc -> Nullable/可以传null",
                        Util.getRandomCode(),
                        "gasPrice -> Nullable/可以传null",
                        "gasLimit -> Nullable/可以传null");
            }
        });
    }

}
