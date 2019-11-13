package com.wallet.cold.app.pawn;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.wallet.R;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;

public class PawnActivity extends AppCompatActivity implements OnClickListener {

    private RelativeLayout view_cd,view_sc,view_fx,view_tz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pawn);
        Data.settype("pawnactivity");
        Data.setcontext(PawnActivity.this);
        view_cd=findViewById(R.id.view_cd);view_cd.setOnClickListener(this);
        view_sc=findViewById(R.id.view_sc);view_sc.setOnClickListener(this);
        view_fx=findViewById(R.id.view_fx);view_fx.setOnClickListener(this);
        view_tz=findViewById(R.id.view_tz);view_tz.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.view_cd) {
            Intent intent = new Intent(this, CdActivity.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.view_sc) {
            Intent intent = new Intent(this, ShopActivity.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.view_fx) {

        }
        if(v.getId() == R.id.view_tz) {
            Intent intent = new Intent(this, InvestActivity.class);
            startActivity(intent);
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
