package com.wallet.hot.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.wallet.R;
import com.wallet.cold.app.main.IndexActivity;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.Utils;
import com.wallet.hot.utils.BaiBeiWalletUtils;
import com.wallet.hot.utils.BaibeiWallet;
import com.wallet.hot.utils.WalletManager;

import org.web3j.crypto.CipherException;

import java.io.IOException;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HotRecoverActivity extends AppCompatActivity {
    @BindView(R.id.et_data_inprot)
    public EditText mEtData;
    @BindView(R.id.et_pwd)
    public EditText mEtPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inprot_wallet);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_inprot_mnemonic)
    public void onClickInportMnemonic(){
        String pwd = mEtPwd.getText().toString();
        String mnemonic = mEtData.getText().toString();
        if (!checkPwd(pwd) ) {
            return;
        }
        if (TextUtils.isEmpty(mnemonic)) {
            Toast.makeText(this, "助记词不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            Data.sethotpassword(mEtPwd.getText().toString());
            BaibeiWallet baibeiWallet = BaiBeiWalletUtils.generateBip44Wallet(mnemonic,pwd);
            WalletManager.getInstance().addWallet(baibeiWallet);
            Toast.makeText(this, "导入成功", Toast.LENGTH_SHORT).show();
            new Utils().balancebtc();
            Intent i = new Intent(HotRecoverActivity.this, IndexActivity.class);
            startActivity(i);
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    @OnClick(R.id.btn_inprot_keystore)
//    public void onClickInportKeystore(){
//        String pwd = mEtPwd.getText().toString();
//        String keystore = mEtData.getText().toString();
//        if (!checkPwd(pwd) ) {
//            return;
//        }
//        if (TextUtils.isEmpty(keystore)) {
//            Toast.makeText(this, "Keystore不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        try {
//            BaibeiWallet baibeiWallet = BaiBeiWalletUtils.loadByKeyStore(pwd,keystore);
//            WalletManager.getInstance().addWallet(baibeiWallet);
//            Toast.makeText(this, "导入成功", Toast.LENGTH_SHORT).show();
//            Intent i = new Intent(HotRecoverActivity.this, HotIndexActivity.class);
//            startActivity(i);
//        } catch (CipherException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @OnClick(R.id.btn_inprot_key)
//    public void onClickInportKey(){
//        String pwd = mEtPwd.getText().toString();
//        String privateKey = mEtData.getText().toString();
//        if (!checkPwd(pwd) ) {
//            return;
//        }
//        if (TextUtils.isEmpty(privateKey)) {
//            Toast.makeText(this, "私钥不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        try {
//            BaibeiWallet baibeiWallet = BaiBeiWalletUtils.generateBip44WalletByPrivateKey(privateKey,pwd);
//            WalletManager.getInstance().addWallet(baibeiWallet);
//            Toast.makeText(this, "导入成功", Toast.LENGTH_SHORT).show();
//            Intent i = new Intent(HotRecoverActivity.this, HotIndexActivity.class);
//            startActivity(i);
//        } catch (CipherException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    private boolean checkPwd(String pwd) {
        if (TextUtils.isEmpty(pwd)||pwd.length()<8) {
            Toast.makeText(this, "请输入8位数以上密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
