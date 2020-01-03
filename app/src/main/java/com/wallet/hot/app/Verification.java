package com.wallet.hot.app;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.R;
import com.wallet.cold.app.main.IndexActivity;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.Utils;
import com.wallet.cold.utils.WeiboDialogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Verification extends AppCompatActivity {
    private TextView zjc1,zjc2,zjc3,zjc4,zjc5,zjc6,zjc7,zjc8,zjc9,zjc10,zjc11,zjc12,zjc13,zjc14,zjc15,zjc16,zjc17,zjc18,zjc19,zjc20,zjc21,zjc22,zjc23,zjc24;
    private Button xiayibu;
    private EditText zhujici;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_mnemonic_layout);Data.settype("verification");
        zhujici = findViewById(R.id.zhujici);
        zjc1 = findViewById(R.id.zjc1);zjc2 = findViewById(R.id.zjc2);zjc3 = findViewById(R.id.zjc3);zjc4 = findViewById(R.id.zjc4);
        zjc5 = findViewById(R.id.zjc5);zjc6 = findViewById(R.id.zjc6);zjc7 = findViewById(R.id.zjc7);zjc8 = findViewById(R.id.zjc8);
        zjc9 = findViewById(R.id.zjc9);zjc10 = findViewById(R.id.zjc10);zjc11 = findViewById(R.id.zjc11);zjc12 = findViewById(R.id.zjc12);
        zjc13 = findViewById(R.id.zjc13);zjc14 = findViewById(R.id.zjc14);zjc15 = findViewById(R.id.zjc15);zjc16 = findViewById(R.id.zjc16);
        zjc17 = findViewById(R.id.zjc17);zjc18 = findViewById(R.id.zjc18);zjc19 = findViewById(R.id.zjc19);zjc20 = findViewById(R.id.zjc20);
        zjc21 = findViewById(R.id.zjc21);zjc22 = findViewById(R.id.zjc22);zjc23 = findViewById(R.id.zjc23);zjc24 = findViewById(R.id.zjc24);
        String[] zjc = Data.gethotzjc().split("\\s+");
        List<String> list = new ArrayList<>();
        for (String ss : zjc){
            list.add(ss);
        }
        Collections.shuffle(list);
        zjc1.setText(list.get(0));zjc2.setText(list.get(1));zjc3.setText(list.get(2));zjc4.setText(list.get(3));
        zjc5.setText(list.get(4));zjc6.setText(list.get(5));zjc7.setText(list.get(6));zjc8.setText(list.get(7));
        zjc9.setText(list.get(8));zjc10.setText(list.get(9));zjc11.setText(list.get(10));zjc12.setText(list.get(11));
        if(list.size()==15){
            zjc13.setText(list.get(12));zjc14.setText(list.get(13));zjc15.setText(list.get(14));
        }else if(list.size()==18){
            zjc13.setText(list.get(12));zjc14.setText(list.get(13));zjc15.setText(list.get(14));
            zjc16.setText(list.get(15));zjc17.setText(list.get(16));zjc18.setText(list.get(17));
        }else if(list.size()==21){
            zjc13.setText(list.get(12));zjc14.setText(list.get(13));zjc15.setText(list.get(14));
            zjc16.setText(list.get(15));zjc17.setText(list.get(16));zjc18.setText(list.get(17));
            zjc19.setText(list.get(18));zjc20.setText(list.get(19));zjc21.setText(list.get(20));
        }else if(list.size()==24){
            zjc13.setText(list.get(12));zjc14.setText(list.get(13));zjc15.setText(list.get(14));
            zjc16.setText(list.get(15));zjc17.setText(list.get(16));zjc18.setText(list.get(17));
            zjc19.setText(list.get(18));zjc20.setText(list.get(19));zjc21.setText(list.get(20));
            zjc22.setText(list.get(21));zjc23.setText(list.get(22));zjc24.setText(list.get(23));
        }
        List<String> list1=new ArrayList<>();
        zjc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list1.contains(zjc1.getText().toString())) {
                    list1.add(zjc1.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }else {
                    list1.remove(zjc1.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }
            }
        });
        zjc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list1.contains(zjc2.getText().toString())) {
                    list1.add(zjc2.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }else {
                    list1.remove(zjc2.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }
            }
        });
        zjc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list1.contains(zjc3.getText().toString())) {
                    list1.add(zjc3.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }else {
                    list1.remove(zjc3.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }
            }
        });
        zjc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list1.contains(zjc4.getText().toString())) {
                    list1.add(zjc4.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }else {
                    list1.remove(zjc4.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }
            }
        });
        zjc5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list1.contains(zjc5.getText().toString())) {
                    list1.add(zjc5.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }else {
                    list1.remove(zjc5.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }
            }
        });
        zjc6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list1.contains(zjc6.getText().toString())) {
                    list1.add(zjc6.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }else {
                    list1.remove(zjc6.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }
            }
        });
        zjc7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list1.contains(zjc7.getText().toString())) {
                    list1.add(zjc7.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }else {
                    list1.remove(zjc7.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }
            }
        });
        zjc8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list1.contains(zjc8.getText().toString())) {
                    list1.add(zjc8.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }else {
                    list1.remove(zjc8.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }
            }
        });
        zjc9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list1.contains(zjc9.getText().toString())) {
                    list1.add(zjc9.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }else {
                    list1.remove(zjc9.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }
            }
        });
        zjc10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list1.contains(zjc10.getText().toString())) {
                    list1.add(zjc10.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }else {
                    list1.remove(zjc10.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }
            }
        });
        zjc11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list1.contains(zjc11.getText().toString())) {
                    list1.add(zjc11.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }else {
                    list1.remove(zjc11.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }
            }
        });
        zjc12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list1.contains(zjc12.getText().toString())) {
                    list1.add(zjc12.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }else {
                    list1.remove(zjc12.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }
            }
        });
        zjc13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list1.contains(zjc13.getText().toString())) {
                    list1.add(zjc13.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }else {
                    list1.remove(zjc13.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }
            }
        });
        zjc14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list1.contains(zjc14.getText().toString())) {
                    list1.add(zjc14.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }else {
                    list1.remove(zjc14.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }
            }
        });
        zjc15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list1.contains(zjc15.getText().toString())) {
                    list1.add(zjc15.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }else {
                    list1.remove(zjc15.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }
            }
        });
        zjc16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list1.contains(zjc16.getText().toString())) {
                    list1.add(zjc16.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }else {
                    list1.remove(zjc16.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }
            }
        });
        zjc17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list1.contains(zjc17.getText().toString())) {
                    list1.add(zjc17.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }else {
                    list1.remove(zjc17.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }
            }
        });
        zjc18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list1.contains(zjc18.getText().toString())) {
                    list1.add(zjc18.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }else {
                    list1.remove(zjc18.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }
            }
        });
        zjc19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list1.contains(zjc19.getText().toString())) {
                    list1.add(zjc19.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }else {
                    list1.remove(zjc19.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }
            }
        });
        zjc20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list1.contains(zjc20.getText().toString())) {
                    list1.add(zjc20.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }else {
                    list1.remove(zjc20.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }
            }
        });
        zjc21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list1.contains(zjc21.getText().toString())) {
                    list1.add(zjc21.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }else {
                    list1.remove(zjc21.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }
            }
        });
        zjc22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list1.contains(zjc22.getText().toString())) {
                    list1.add(zjc22.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }else {
                    list1.remove(zjc22.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }
            }
        });
        zjc23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list1.contains(zjc23.getText().toString())) {
                    list1.add(zjc23.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }else {
                    list1.remove(zjc23.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }
            }
        });
        zjc24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list1.contains(zjc24.getText().toString())) {
                    list1.add(zjc24.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }else {
                    list1.remove(zjc24.getText().toString());
                    zhujici.setText(list(list1.toString()));
                }
            }
        });
        xiayibu = findViewById(R.id.xiayibu);
        xiayibu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(zhujici.getText().toString().equals(Data.gethotzjc())) {
                    new Utils().balancebtc();
                    Dialog mWeiboDialog = WeiboDialogUtils.createLoadingDialog(Verification.this, Verification.this.getResources().getString(R.string.type4));
                    Data.setdialog(mWeiboDialog);
                }else{
                    Toast.makeText(Data.getcontext(), "验证失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String list(String list){
        String name="";
        String[] temp = list.split(",");
        for (int i = 0;i<temp.length;i++){
            //数据叠加且用空格替换
            name += temp[i];
        }
        return name.substring(1,name.length()-1);
    }
}
