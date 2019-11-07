package com.wallet.hot.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wallet.R;
import com.wallet.cold.utils.Data;

public class BackUpMnemonicActivity extends AppCompatActivity {
    private TextView zhujici;
    private Button xiayibu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backup_mnemonic_layout);
        zhujici = findViewById(R.id.zhujici);
        xiayibu = findViewById(R.id.xiayibu);
        zhujici.setText(Data.gethotzjc());
        xiayibu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BackUpMnemonicActivity.this, Verification.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showWaringDialog();
    }

    private void showWaringDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.mnemonic_dialog_layout, null);
        Button btn = v.findViewById(R.id.dialog_button);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
