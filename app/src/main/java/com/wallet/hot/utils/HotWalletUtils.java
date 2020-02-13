package com.wallet.hot.utils;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.wallet.R;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.Utils;
import com.wallet.utils.WeiboDialogUtils;
import com.wallet.hot.app.BackUpActivity;

import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.web3j.crypto.Sign;
import java.math.BigInteger;
import java.security.SecureRandom;

import static org.bitcoinj.crypto.HDUtils.parsePath;

/**
 * TODO 写清楚此类的作用
 *
 * @author Try
 * @date 2018/9/12 16:56
 */
public class HotWalletUtils {
    private static final SecureRandom secureRandom = new SecureRandom();

    public static void generateBip44Wallet() {
        //1.通过bip39生成助记词
//        byte[] initialEntropy = new byte[16];//12位
        byte[] initialEntropy=null;
        if(Data.getbutton()==15){
            initialEntropy = new byte[20];//15位
        }else if(Data.getbutton()==18){
            initialEntropy = new byte[24];//18位
        }else if(Data.getbutton()==21){
            initialEntropy = new byte[28];//21位
        }else if(Data.getbutton()==24){
            initialEntropy = new byte[32];//24位
        }
        secureRandom.nextBytes(initialEntropy);
        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
        if (Boolean.parseBoolean("true")) {
            Log.i("TAG", "generateBip44Wallet: 助记词 = " + mnemonic);
            Data.sethotzjc(mnemonic);
        }
        TestBip44ETH(mnemonic);
    }

    public static void TestBip44ETH(String mnemonic){
        try {
            if(Data.getbledata().contains("ETH")) {
                DeterministicSeed deterministicSeed = new DeterministicSeed(mnemonic, null, "Hierstar ComboWallet Salt", 0);
                Log.i("BIP39 seed:{}", deterministicSeed.toHexString());
                DeterministicKeyChain deterministicKeyChain = DeterministicKeyChain.builder().seed(deterministicSeed).build();
                BigInteger privKeyETH = deterministicKeyChain.getKeyByPath(parsePath("M/44H/60H/0H"), true).getPrivKey();
                String strprv = privKeyETH.toString(16);//非压缩私钥
                BigInteger privKey = new BigInteger(strprv, 16);
                BigInteger pubKey = Sign.publicKeyFromPrivate(privKey);
                System.out.println("ETH Private key (256 bits): " + privKey.toString(16));
                System.out.println("ETH Public key (512 bits): " + "04" + pubKey.toString(16));//非压缩公钥
                Data.setbizhong("ETH");
                Data.setdata("04" + pubKey.toString(16));
                String address = Utils.address();
                Data.setethaddress(address);
                Data.sethotethprv(strprv);
                Data.sethotethpub("04" + pubKey.toString(16));
                Bitmap codeBitmap = Utils.createCode(address);
                Data.setethimgCode(codeBitmap);
                if(Data.gettype().equals("addbiactivity")) {
                    Cursor cursor = Data.getdb().rawQuery("select * from HotAddressTb", null);
                    if (cursor != null && cursor.getCount() > 0) {
                        Data.getdb().execSQL("update HotAddressTb set ethaddress='" + Data.getethaddress() + "',ethpub='" + Data.gethotethpub() + "',ethprv='" + Data.gethotethprv() + "' where mnemonic='" + Data.gethotzjc() + "'");
                    }else {
                        Data.getdb().execSQL("insert into HotAddressTb (password,btcaddress,ethaddress,ethprv,ethpub,btcprv,btcpub,xrpaddress,xrppub,xrpprv,mnemonic) values " +
                                "('" + Data.gethotpassword() + "','" + "" + "','" + Data.getethaddress() + "','" + Data.gethotethprv() + "','" + Data.gethotethpub() + "','" + "" + "','" + "" + "'," +
                                "'" + "" + "','" + "" + "','" + "" + "','" + Data.gethotzjc() + "')");
                    }
                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.add6), Toast.LENGTH_SHORT).show();
                    WeiboDialogUtils.closeDialog(Data.getdialog());
                }
            }else{
                Data.setethaddress("");
                Data.sethotethprv("");
                Data.sethotethpub("");
                TestBip44BTC(mnemonic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void TestBip44BTC(String mnemonic){
        try {
            if(Data.getbledata().contains("BTC")) {
                DeterministicSeed deterministicSeed = new DeterministicSeed(mnemonic, null, "Hierstar ComboWallet Salt", 0);
                Log.i("BIP39 seed:{}", deterministicSeed.toHexString());
                DeterministicKeyChain deterministicKeyChain = DeterministicKeyChain.builder().seed(deterministicSeed).build();
                BigInteger privKeyBTC = deterministicKeyChain.getKeyByPath(parsePath("M/44H/1H/0H"), true).getPrivKey();
                String privKeywifBTC = deterministicKeyChain.getKeyByPath(parsePath("M/44H/1H/0H"), true).getPrivateKeyAsWiF(TestNet3Params.get());//压缩私钥
                String strprv = privKeyBTC.toString(16);//非压缩私钥
                BigInteger privKey = new BigInteger(strprv, 16);
                BigInteger pubKey = Sign.publicKeyFromPrivate(privKey);
                System.out.println("BTC Private key (256 bits): " + privKey.toString(16));
                System.out.println("BTC Public key (512 bits): " + "04" + pubKey.toString(16));
                Data.setbizhong("BTC");
                Data.setdata("04" + pubKey.toString(16));
                String address = Utils.address();
                Data.setbtcaddress(address);
                Data.sethotbtcprv(privKeywifBTC);
                Data.sethotbtcpub("04" + pubKey.toString(16));
                Bitmap codeBitmap = Utils.createCode(address);
                Data.setimgCode(codeBitmap);
                new Utils().zhuce();
                if (Data.gettype().equals("addbiactivity")) {
                    Cursor cursor = Data.getdb().rawQuery("select * from HotAddressTb", null);
                    if (cursor != null && cursor.getCount() > 0) {
                        Data.getdb().execSQL("update HotAddressTb set btcaddress='" + Data.getbtcaddress() + "',btcpub='" + Data.gethotbtcpub() + "',btcprv='" + Data.gethotbtcprv() + "' where mnemonic='" + Data.gethotzjc() + "'");
                    }else {
                        Data.getdb().execSQL("insert into HotAddressTb (password,btcaddress,ethaddress,ethprv,ethpub,btcprv,btcpub,xrpaddress,xrppub,xrpprv,mnemonic) values " +
                                "('" + Data.gethotpassword() + "','" + Data.getbtcaddress() + "','" + "" + "','" + "" + "','" + "" + "','" + Data.gethotbtcprv() + "','" + Data.gethotbtcpub() + "'," +
                                "'" + "" + "','" + "" + "','" + "" + "','" + Data.gethotzjc() + "')");
                    }
                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.add6), Toast.LENGTH_SHORT).show();
                    WeiboDialogUtils.closeDialog(Data.getdialog());
                }
            }else{
                Data.setbtcaddress("");
                Data.sethotbtcprv("");
                Data.sethotbtcpub("");
                TestBip44XRP(mnemonic);
            }
//            /**生成根私钥 root private key*/
//            DeterministicKey rootPrivateKey1 = HDKeyDerivation.createMasterPrivateKey(deterministicSeed.getSeedBytes());
//            /**根私钥进行 priB58编码*/
//            NetworkParameters params = TestNet3Params.get();
//            String priv = rootPrivateKey1.serializePrivB58(params);
//            Log.i("BIP32 Root Key:{}", priv);
//            /**由根私钥生成HD钱包*/
//            DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(rootPrivateKey1);
//            /**定义父路径*/
//            List<ChildNumber> parsePath = HDUtils.parsePath("44H/1H/0H");
//            DeterministicKey accountKey0 = deterministicHierarchy.get(parsePath, true, true);
//            Log.i("BIP32 extended private key:{}", accountKey0.serializePrivB58(params));
//            Log.i("BIP32 extended public key:{}", accountKey0.serializePubB58(params));
//            /**由父路径,派生出第一个子私钥*/
//            DeterministicKey childKey0 = HDKeyDerivation.deriveChildKey(accountKey0, 0);
//  //        DeterministicKey childKey0 = deterministicHierarchy.deriveChild(parsePath, true, true, new ChildNumber(0));
//            Log.i("0 private key:{}", childKey0.getPrivateKeyAsHex());
//            Log.i("0 private key:{}", childKey0.getPrivateKeyAsWiF(params));
//            Log.i("0 public key:{}", childKey0.getPublicKeyAsHex());
//            Log.i("0 address:{}", String.valueOf(childKey0.toAddress(params)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void TestBip44XRP(String mnemonic){
        try {
            if(Data.getbledata().contains("XRP")) {
                DeterministicSeed deterministicSeed = new DeterministicSeed(mnemonic, null, "Hierstar ComboWallet Salt", 0);
                Log.i("BIP39 seed:{}", deterministicSeed.toHexString());
                DeterministicKeyChain deterministicKeyChain = DeterministicKeyChain.builder().seed(deterministicSeed).build();
//            BigInteger privKeyXRP = deterministicKeyChain.getKeyByPath(parsePath("M/44H/144H/0H"), true).getPrivKey();
                String privKeywifXRP = deterministicKeyChain.getKeyByPath(parsePath("M/44H/144H/0H"), true).getPrivateKeyAsWiF(TestNet3Params.get());//压缩私钥
                String privKeyHexXRP = deterministicKeyChain.getKeyByPath(parsePath("M/44H/144H/0H"), true).getPrivateKeyAsHex();//非压缩私钥
                String pubKeyHexXRP = deterministicKeyChain.getKeyByPath(parsePath("M/44H/144H/0H"), true).getPublicKeyAsHex();//压缩公钥
//            String strprv = privKeyXRP.toString(16);
//            BigInteger privKey = new BigInteger(strprv, 16);
//            BigInteger pubKey = Sign.publicKeyFromPrivate(privKey);
                System.out.println("XRP Private key (256 bits): " + privKeyHexXRP);
                System.out.println("XRP Public key (512 bits): " + pubKeyHexXRP);
//            String pub=pubKey.toString(16);
//            String x=pub.substring(0,64);
//            String y=pub.substring(pub.length()-1,pub.length());
//            int value = Integer.parseInt(y,16);
//            if (value % 2 != 0) {//奇数
//                pub="03"+x;
//            } else {//偶数
//                pub="02"+x;
//            }
                Data.setbizhong("XRP");
                Data.setdata(pubKeyHexXRP);
                String address = Utils.address();
                Data.setxrppub(pubKeyHexXRP);
                Data.setxrpprv(privKeyHexXRP);
                Data.setxrpaddress(address);
                Bitmap codeBitmap = Utils.createCode(address);
                Data.setxrpimgCode(codeBitmap);
            }else{
                Data.setxrppub("");
                Data.setxrpprv("");
                Data.setxrpaddress("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(Data.gettype().equals("addbiactivity")) {
            Cursor cursor = Data.getdb().rawQuery("select * from HotAddressTb", null);
            if (cursor != null && cursor.getCount() > 0) {
                Data.getdb().execSQL("update HotAddressTb set xrpaddress='" + Data.getxrpaddress() + "',xrppub='" + Data.getxrppub() + "',xrpprv='" + Data.getxrpprv() + "' where mnemonic='" + Data.gethotzjc() + "'");
            }else {
                Data.getdb().execSQL("insert into HotAddressTb (password,btcaddress,ethaddress,ethprv,ethpub,btcprv,btcpub,xrpaddress,xrppub,xrpprv,mnemonic) values " +
                        "('" + Data.gethotpassword() + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" +"" + "'," +
                        "'" + Data.getxrpaddress() + "','" + Data.getxrppub() + "','" + Data.getxrpprv() + "','" + Data.gethotzjc() + "')");
            }
            Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.add6), Toast.LENGTH_SHORT).show();
            WeiboDialogUtils.closeDialog(Data.getdialog());
        }else {
            if (Data.gettype().equals("create")) {
                Intent intent = new Intent(Data.getcontext(), BackUpActivity.class);
                Data.getcontext().startActivity(intent);
            } else if (Data.gettype().equals("recover")) {
                Toast.makeText(Data.getcontext(), "导入成功", Toast.LENGTH_SHORT).show();
                new Utils().balancebtc();
            }
        }
    }

    public static void RecoverBip44Wallet(String mnemonic) {
        TestBip44ETH(mnemonic);
    }
}
