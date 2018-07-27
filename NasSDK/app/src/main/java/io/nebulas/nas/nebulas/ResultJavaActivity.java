package io.nebulas.nas.nebulas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import io.nebulas.api.ResponseHandler;
import io.nebulas.api.ResultHelper;
import io.nebulas.nas.R;
import io.nebulas.nas.auth.AuthResultActivity;
import io.nebulas.nas.transfer.TransferResultActivity;
import io.nebulas.response.AuthResponse;
import io.nebulas.response.Response;
import io.nebulas.response.TransferResponse;

/**
 * 请忽略此类名，此代码只是为了演示Java代码的接入示例
 * 真实接入，【请务必于your_package.nebulas包下创建ResultActivity，并在清单文件中配置exported=true】
 */
public class ResultJavaActivity extends AppCompatActivity implements ResponseHandler {

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        textView = findViewById(R.id.textView);
        ResultHelper.INSTANCE.handleIntent(getIntent(), this);//用于将Intent中的返回值转为Response对象
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ResultHelper.INSTANCE.handleIntent(getIntent(), this);//用于将Intent中的返回值转为Response对象
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResponse(@NonNull Response response) {
        if (response.getErrorCode() != 0) {
            textView.setText(response.getErrorMessage());
            return;
        }
        if (response instanceof TransferResponse) {
            TransferResultActivity.Companion.launch(this, (TransferResponse) response);
            finish();
        } else if (response instanceof AuthResponse) {
            AuthResultActivity.Companion.launch(this, (AuthResponse) response);
            finish();
        } else {
            textView.setText("Unknown Result\n\n"+response.toString());
        }
    }
}
