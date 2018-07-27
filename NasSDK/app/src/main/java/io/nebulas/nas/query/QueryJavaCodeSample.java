package io.nebulas.nas.query;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import io.nebulas.Constants;
import io.nebulas.api.SmartContracts;

/**
 * 此代码只是为了演示Java代码的接入示例
 */
public class QueryJavaCodeSample extends AppCompatActivity implements SmartContracts.StatusCallback {

    private Button button;
    private ProgressBar progressBar;
    private TextView textViewResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                SmartContracts.queryAccountState(
                        Constants.TEST_NET,
                        "<nas wallet address>",
                        QueryJavaCodeSample.this);
            }
        });
    }

    @Override
    public void onSuccess(@NotNull String response) {
        progressBar.setVisibility(View.GONE);
        textViewResult.setText(response);
    }

    @Override
    public void onFail(@NotNull String error) {
        progressBar.setVisibility(View.GONE);
        textViewResult.setText(error);
    }
}
