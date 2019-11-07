package com.wallet.hot.app;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Verification extends AppCompatActivity {
    private TextView zjc1,zjc2,zjc3,zjc4,zjc5,zjc6,zjc7,zjc8,zjc9,zjc10,zjc11,zjc12;
    private Button xiayibu;
    private EditText zhujici;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_mnemonic_layout);
        zhujici = findViewById(R.id.zhujici);
        zhujici.setFocusable(false);
        zhujici.setFocusableInTouchMode(false);
        zjc1 = findViewById(R.id.zjc1);zjc2 = findViewById(R.id.zjc2);zjc3 = findViewById(R.id.zjc3);zjc4 = findViewById(R.id.zjc4);
        zjc5 = findViewById(R.id.zjc5);zjc6 = findViewById(R.id.zjc6);zjc7 = findViewById(R.id.zjc7);zjc8 = findViewById(R.id.zjc8);
        zjc9 = findViewById(R.id.zjc9);zjc10 = findViewById(R.id.zjc10);zjc11 = findViewById(R.id.zjc11);zjc12 = findViewById(R.id.zjc12);
        String[] zjc = Data.gethotzjc().split("\\s+");
        List<String> list = new ArrayList<>();
        for (String ss : zjc){
            list.add(ss);
        }
        Collections.shuffle(list);
        zjc1.setText(list.get(0));zjc2.setText(list.get(1));zjc3.setText(list.get(2));zjc4.setText(list.get(3));
        zjc5.setText(list.get(4));zjc6.setText(list.get(5));zjc7.setText(list.get(6));zjc8.setText(list.get(7));
        zjc9.setText(list.get(8));zjc10.setText(list.get(9));zjc11.setText(list.get(10));zjc12.setText(list.get(11));
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
        xiayibu = findViewById(R.id.xiayibu);
        xiayibu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(zhujici.getText().toString().equals(Data.gethotzjc())) {
                    new Utils().balancebtc();
                    Intent intent = new Intent(Verification.this, IndexActivity.class);
                    startActivity(intent);
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
