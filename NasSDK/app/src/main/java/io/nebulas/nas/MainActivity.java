package io.nebulas.nas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import io.nebulas.api.SmartContracts;
import io.nebulas.configuration.Configuration;
import io.nebulas.model.ContractModel;
import io.nebulas.nas.query.QueryAccountActivity;
import io.nebulas.nas.query.QueryTransactionActivity;
import io.nebulas.nas.transfer.ContractCallActivity;
import io.nebulas.nas.transfer.TransferActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private String value = "100";

    private String serialNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Configuration.INSTANCE.useDebug();
        Configuration.INSTANCE.enableLog();
        updateTitle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_release:
                Configuration.INSTANCE.useRelease();
                break;
            case R.id.menu_test_net:
                Configuration.INSTANCE.useTestNet();
                break;
            case R.id.menu_debug:
                Configuration.INSTANCE.useDebug();
                break;
        }
        updateTitle();
        return super.onOptionsItemSelected(item);
    }

    private void updateTitle() {
        setTitle(Configuration.INSTANCE.getCurrentReleaseType().getDesc());
    }

    public void transfer(View view) {
        TransferActivity.Companion.launch(this);
    }

    public void auth(View view) {
        SmartContracts.auth(this);
    }

    public void contractCall(View view) {
        ContractCallActivity.Companion.launch(this);
    }

    public void nasQueryTransferStatus(View view) {
        QueryTransactionActivity.Companion.launch(this);
    }

    public void nasQueryAccountState(View view) {
        QueryAccountActivity.Companion.launch(this);
    }

    public void nasCallContract(View view) {
        ContractModel contractModel = new ContractModel();

        //contractModel.addArg("美扑伪麻片");
        //contractModel.function = "getDrugItem";
        //String from = "n1haLy4Ka989bB7NujA9LxRMhU4FP6PFoK4";

        contractModel.addArg(1);
        contractModel.addArg(0);
        contractModel.addArg(1);
        contractModel.function = "history";
        String from = "n22Djb3G8dzLyeRMWAxov7j3ExLdhnLtwgw";

        SmartContracts.simulateCall(contractModel, from, from, 1, new SmartContracts.StatusCallback() {
            @Override
            public void onSuccess(final String response) {

                Log.i(TAG, "response:" + response);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFail(String error) {

                Log.i(TAG, "error:" + error);
            }
        });
    }
}

