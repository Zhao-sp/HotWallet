package com.wallet.cold.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.googlecode.jsonrpc4j.Base64;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wallet.R;
import com.wallet.cold.app.index.Fragment1;
import com.wallet.cold.app.main.MainActivity;
import com.wallet.cold.app.index.Transfer;
import com.wallet.cold.app.auth0.auth0login;
import com.wallet.cold.app.main.IndexActivity;
import com.wallet.cold.app.main.BleActivity;
import com.wallet.cold.app.main.CreateActivity;
import com.wallet.cold.app.main.RecoverActivity;
import com.wallet.cold.app.util.Fingerprints;
import com.wallet.cold.app.util.FingerprintsXQ;
import com.wallet.cold.app.util.GengxinActivity;
import com.wallet.cold.app.util.LanguagesActivity;
import com.wallet.cold.dfu.DfuUpdateActivity;

import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wallet.cold.app.main.MainActivity.DEVICE_NAME;
import static com.wallet.cold.app.main.MainActivity.MAC_ADDRESS;
import static com.wallet.cold.app.main.MainActivity.TAG;
import static com.wallet.cold.app.util.Fingerprints.hexString2binaryString;
import static java.lang.String.valueOf;

public class Utils extends Activity {
    private final static String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";


    /**
     * btc(bch,usdt)地址是否有效
     * return: true有效,false无效
     */
    public static boolean bitCoinAddressValidate(String addr) {
        if (addr.length() < 26 || addr.length() > 35)
            return false;
        byte[] decoded = decodeBase58To25Bytes(addr);
        if (decoded == null)
            return false;
        byte[] hash1 = sha256(Arrays.copyOfRange(decoded, 0, 21));
        byte[] hash2 = sha256(hash1);
        return Arrays.equals(Arrays.copyOfRange(hash2, 0, 4), Arrays.copyOfRange(decoded, 21, 25));
    }
    private static byte[] decodeBase58To25Bytes(String input) {
        BigInteger num = BigInteger.ZERO;
        for (char t : input.toCharArray()) {
            int p = ALPHABET.indexOf(t);
            if (p == -1)
                return null;
            num = num.multiply(BigInteger.valueOf(58)).add(BigInteger.valueOf(p));
        }
        byte[] result = new byte[25];
        byte[] numBytes = num.toByteArray();
        System.arraycopy(numBytes, 0, result, result.length - numBytes.length, numBytes.length);
        return result;
    }
    public static boolean ETHAddressValidate(String addr) {
        if (addr.length() != 40)
            return false;
        String regex="^[A-Fa-f0-9]+$";
        if(addr.matches(regex)){
            System.out.println(addr.toUpperCase()+"是16进制数");
        }else{
            System.out.println(addr.toUpperCase()+"不是16进制数");
            return false;
        }
        return true;
    }
    /**
     * sha256加密算法
     * @param data
     * @return
     */
    public static byte[] sha256(byte[] data) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 数组转换成十六进制字符串
     * @param bArray
     * @return HexString
     */
    public static String bytesToHexString(byte[] bArray) {

        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 两个byte[]数组相加
     *
     * @param data1
     * @param data2
     * @return
     */
    public static byte[] add(byte[] data1, byte[] data2) {

        byte[] result = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, result, 0, data1.length);
        System.arraycopy(data2, 0, result, data1.length, data2.length);

        return result;
    }

    /**
     * 黑点颜色
     */
    public static final int BLACK = 0xFF000000;
    /**
     * 白色
     */
    public static final int WHITE = 0xFFFFFFFF;
    /**
     * 正方形二维码宽度
     */
    public static final int CODE_WIDTH = 440;
    /**
     * LOGO宽度值,最大不能大于二维码20%宽度值,大于可能会导致二维码信息失效
     */
    public static final int LOGO_WIDTH_MAX = CODE_WIDTH / 5;
    /**
     * LOGO宽度值,最小不能小鱼二维码10%宽度值,小于影响Logo与二维码的整体搭配
     */
    public static final int LOGO_WIDTH_MIN = CODE_WIDTH / 10;

    /**
     * 生成带LOGO的二维码
     */
    public static Bitmap createCode(String content) throws WriterException {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);//设置容错级别,H为最高
        hints.put(EncodeHintType.MAX_SIZE, LOGO_WIDTH_MAX);// 设置图片的最大值
        hints.put(EncodeHintType.MIN_SIZE, LOGO_WIDTH_MIN);// 设置图片的最小值
        hints.put(EncodeHintType.MARGIN, 0);//设置白色边距值
        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, CODE_WIDTH, CODE_WIDTH, hints);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int halfW = width / 2;
        int halfH = height / 2;
        // 二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[y * width + x] = matrix.get(x, y) ? BLACK : WHITE;// 设置信息
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 比特币相关网络请求
     *
     * @param btcJson
     * @return
     */
    public static String getbtchttp(String btcJson) {
        String result = "";
        String urlName = "";
        try {
            btcJson = URLEncoder.encode(btcJson, "UTF-8");
            if (Data.getbtctype().equals("balance")) {
                urlName = Data.gethttp1()+"/hsRPCNodeServer/rpc/listunspent?jsonParams=" + btcJson;
                URL U = new URL(urlName);
                URLConnection connection = U.openConnection();
                connection.connect();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
                in.close();
            } else if (Data.getbtctype().equals("gouzaojiaoyi")) {
                urlName = Data.gethttp1()+"/hsRPCNodeServer/rpc/createrawtransaction?jsonParams=" + btcJson;
                URL U = new URL(urlName);
                URLConnection connection = U.openConnection();
                connection.connect();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
                in.close();
            } else if (Data.getbtctype().equals("sendrawtransaction")) {
                urlName = Data.gethttp1()+"/hsRPCNodeServer/rpc/sendrawtransaction?jsonParams=" + btcJson;
                URL U = new URL(urlName);
                URLConnection connection = U.openConnection();
                connection.connect();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
                in.close();
            }
            LogCook.d("发送参数",urlName);
            LogCook.d("BTC网络请求返回数据", result);
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getString("status_Sucess").equals("true")) {
                if (Data.getbtctype().equals("balance")) {
                    String result1 = jsonObject.getString("message");
                    List<Object> list1 = JSON.parseArray(result1);
                    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                    for (Object object : list1) {
                        Map<String, Object> ret = (Map<String, Object>) object;//取出list里面的值转为map
                        list.add(ret);
                    }
                    Collections.sort(list, new Comparator<Map<String, Object>>() {
                        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                            Double name1 = Double.valueOf(o1.get("amount").toString());//name1是从你list里面拿出来的一个
                            Double name2 = Double.valueOf(o2.get("amount").toString()); //name1是从你list里面拿出来的第二个name
                            return name2.compareTo(name1);//由大到小排序
                        }
                    });
                    Map<String, Object> m = new HashMap<String, Object>();
                    List<String> txid = new ArrayList<String>();
                    List<Object> vout = new ArrayList<Object>();
                    List<String> address = new ArrayList<String>();
                    List<String> account = new ArrayList<String>();
                    List<String> scriptPubKey = new ArrayList<String>();
                    List<String> amount = new ArrayList<String>();
                    List<String> confirmations = new ArrayList<String>();
                    List<String> spendable = new ArrayList<String>();
                    List<String> solvable = new ArrayList<String>();
                    List<String> safe = new ArrayList<String>();
                    for (int q = 0; q < list.size(); q++) {
                        LogCook.d("遍历返回余额信息", valueOf(list.get(q)));
                        m = (Map<String, Object>) list.get(q); //通过索引方式进行转换类型的强转
                        Set keySet = m.keySet(); // 读取map中的文件
                        Iterator<String> it = keySet.iterator();
                        while (it.hasNext()) {  //挨个遍历
                            Object k = it.next(); // key
                            if (valueOf(k).equals("txid")) {
                                Object v = m.get(k);
                                txid.add(valueOf(v));
                            } else if (valueOf(k).equals("vout")) {
                                Object v = m.get(k);
                                vout.add(v);
                            } else if (valueOf(k).equals("scriptPubKey")) {
                                Object v = m.get(k);
                                scriptPubKey.add(valueOf(v));
                            } else if (valueOf(k).equals("amount")) {
                                Object v = m.get(k);
                                BigDecimal bd = new BigDecimal(valueOf(v));
                                amount.add(bd.toString());
                            }
                        }
                    }
                    Data.settxid(txid);
                    Data.setvout(vout);
                    Data.setscriptPubKey(scriptPubKey);
                    Data.setamount(amount);
                    LogCook.d("txid", valueOf(txid));
                    LogCook.d("vout", valueOf(vout));
                    LogCook.d("scriptPubKey", valueOf(scriptPubKey));
                    LogCook.d("amount", valueOf(amount));
                    Double sum = 0d;
                    for (String d : amount) {
                        sum += Double.parseDouble(d);
                    }
                    LogCook.d("btc余额", String.format("%.8f", sum));
                    if(String.format("%.8f", sum).equals("0.00000000")){
                        Data.setbtcbalance("0");
                        return "0success";
                    }else{
                        Data.setbtcbalance(String.format("%.8f", sum));
                    }
                    return String.format("%.8f", sum) + "success";
                } else if (Data.getbtctype().equals("zhuce")) {
                    LogCook.d("注册BTC地址", "注册BTC地址成功");
                } else if (Data.getbtctype().equals("gouzaojiaoyi")) {
                    String result1 = jsonObject.getString("status_Message");
                    return result1 + "success";
                } else if (Data.getbtctype().equals("sendrawtransaction")) {
                    LogCook.d("BTC发送交易", "BTC交易成功");
                }
            } else if (jsonObject.getString("status_Sucess").equals("false")) {//返回错误信息
                if (Data.getbtctype().equals("balance")) {
                    Data.setbtcbalance("0");
                    if(!jsonObject.getString("status_Result").equals("")) {
                        return jsonObject.getString("status_Result") + ":" + jsonObject.getString("status_Message");
                    }
                } else if (Data.getbtctype().equals("gouzaojiaoyi")) {
                    return jsonObject.getString("status_Result") + ":" + jsonObject.getString("status_Message");
                } else if (Data.getbtctype().equals("sendrawtransaction")) {
                    return jsonObject.getString("status_Result") + ":" + jsonObject.getString("status_Message");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Data.getcontext().getResources().getString(R.string.u1);
        }
        return "";
    }

    /**
     * 以太坊相关网络请求
     *
     * @param address
     * @return
     */
    public static String getethhttp(String address) {
        LogCook.d("发送参数", address);
        String result = "";
        try {
            address = URLEncoder.encode(address, "UTF-8");
            String urlName = "";
            if (Data.getethtype().equals("balance")) {
                urlName = Data.gethttp1()+"/hsRPCNodeServer/ethRpc/eth_getBalance?jsonParams=" + address;
            } else if (Data.getethtype().equals("dealid")) {
                urlName = Data.gethttp1()+"/hsRPCNodeServer/ethRpc/eth_getTransactionCount?jsonParams=" + address;
            } else if (Data.getethtype().equals("sendtransaction")) {
                urlName = Data.gethttp1()+"/hsRPCNodeServer/ethRpc/eth_sendRawTransaction?jsonParams=" + address;
            }
            URL U = new URL(urlName);
            URLConnection connection = U.openConnection();
            connection.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            LogCook.d("ETH网络请求返回数据", result);
            in.close();
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getString("status_Sucess").equals("true")) {
                result = jsonObject.getString("status_Message");
                if (Data.getethtype().equals("balance")) {
                    String result1 = result.substring(2, result.length());
                    String str = new BigInteger(result1, 16).toString(10);
                    Double ethsum = Double.parseDouble(str);
                    Double ethsum1 = Double.parseDouble("1000000000000000000");
                    Double ethsum2 = ethsum / ethsum1;
                    String yue=String.valueOf(new BigDecimal(ethsum2).setScale(0, BigDecimal.ROUND_HALF_UP));
                    LogCook.d("eth余额", yue);
                    if(yue.equals("0")){
                        Data.setethbalance("0");
                        return "0success";
                    }else{
                        Data.setethbalance(yue);
                        return yue + "success";
                    }
                } else if (Data.getethtype().equals("dealid")) {
                    String dealid = result.substring(2, result.length());
                    if (dealid.length() < 2) {
                        dealid = "0" + dealid;
                    }
                    LogCook.d("ETH交易序号", dealid);
                    return dealid + "success";
                } else if (Data.getethtype().equals("sendtransaction")) {
                    LogCook.d("ETH交易", "交易成功");
                }
            } else if (jsonObject.getString("status_Sucess").equals("false")) {//返回错误信息
                if (Data.getethtype().equals("balance")) {
                    Data.setethbalance("0");
                    if(!jsonObject.getString("status_Result").equals("")) {
                        return jsonObject.getString("status_Result") + ":" + jsonObject.getString("status_Message");
                    }
                } else if (Data.getethtype().equals("dealid")) {
                    return jsonObject.getString("status_Result") + ":" + jsonObject.getString("status_Message");
                } else if (Data.getethtype().equals("sendtransaction")) {
                    return jsonObject.getString("status_Result") + ":" + jsonObject.getString("status_Message");
                }
            }
        } catch (IOException e) {
            Data.setethbalance("0.00000000");
            e.printStackTrace();
            return Data.getcontext().getResources().getString(R.string.u1);
        } catch (JSONException e) {
            e.printStackTrace();
            return Data.getcontext().getResources().getString(R.string.u1);
        } catch (Exception e) {
            e.printStackTrace();
            return Data.getcontext().getResources().getString(R.string.u1);
        }
        return "";
    }

    /**
     * 蓝牙返回错误信息
     *
     * @param data
     * @return
     */
    public static String bledata(String data) {
        String error = "";
        Data.setbtcsignerror("yes");
        if (data.contains("6A88")) {
            error = Data.getcontext().getResources().getString(R.string.u2);
        } else if (data.contains("6A81")) {
            if (Data.getbletype().equals("gainboot")) {
                data = "";
                gaintype();
            } else if (!data.equals("")&&!Data.getbletype().equals("type")) {
                error = Data.getcontext().getResources().getString(R.string.u3);
            }
        } else if (data.contains("6280")) {
            error = Data.getcontext().getResources().getString(R.string.u4);
        } else if (data.contains("6281")) {
            error = Data.getcontext().getResources().getString(R.string.u5);
        } else if (data.contains("6580")) {
            error = Data.getcontext().getResources().getString(R.string.u6);
        } else if (data.contains("6581")) {
            error = Data.getcontext().getResources().getString(R.string.u7);
        } else if (data.contains("6901")) {
            error = Data.getcontext().getResources().getString(R.string.u8);
        } else if (data.contains("6983")) {
            error = Data.getcontext().getResources().getString(R.string.u9);
        } else if (data.contains("6984")) {
            error = Data.getcontext().getResources().getString(R.string.u10);
        } else if (data.contains("6A80")) {
            error = Data.getcontext().getResources().getString(R.string.u11);
        } else if (data.contains("6A86")) {
            if (Data.getbletype().equals("recover")) {
                data = "";
                Data.setbletype("");
                error = Data.getcontext().getResources().getString(R.string.u12);
            } else if (!data.equals("")&&!Data.getbletype().equals("")) {
                error = Data.getcontext().getResources().getString(R.string.u12);
            }
        } else if (data.contains("6D00")) {
            if (Data.getbletype().equals("gainboot")) {
                data = "";
                Data.setbletype("");
                gaintype();
            } else if (!data.equals("")&&!Data.getbletype().equals("type")) {
                error = Data.getcontext().getResources().getString(R.string.u13);
            }
        } else if (data.contains("6F00")) {
            error = Data.getcontext().getResources().getString(R.string.u14);
        } else if (data.contains("6985")) {
            error = Data.getcontext().getResources().getString(R.string.u15);
        } else if (data.contains("6986")) {
            error = Data.getcontext().getResources().getString(R.string.u16);
        } else if (data.contains("6986")) {
            error = Data.getcontext().getResources().getString(R.string.u17);
        } else if (data.contains("6986")) {
            error = Data.getcontext().getResources().getString(R.string.u18);
        } else if (data.contains("6986")) {
            error = Data.getcontext().getResources().getString(R.string.u19);
        } else if (data.contains("6986")) {
            error = Data.getcontext().getResources().getString(R.string.u20);
        } else if (data.contains("F000")) {
            if(data.substring(12,14).equals("00")){
                error = "个性化失败-执行成功";
            }else if(data.substring(12,14).equals("01")){
                error = "个性化失败-执行失败";
            }else if(data.substring(12,14).equals("02")){
                error = "个性化失败-数据库满";
            }else if(data.substring(12,14).equals("03")){
                error = "个性化失败-没有这个指纹";
            }else if(data.substring(12,14).equals("04")){
                error = "个性化失败-指纹已存在";
            }else if(data.substring(12,14).equals("05")){
                error = "个性化失败-图像采集超时";
            }else if(data.substring(12,14).equals("06")){
                error = "个性化失败-图像不合格";
            }else if(data.substring(12,14).equals("07")){
                error = "个性化失败-按压面积太小";
            }else if(data.substring(12,14).equals("08")){
                error = "个性化失败-创建模板失败";
            }else if(data.substring(12,14).equals("09")){
                error = "个性化失败-模板不合并失败";
            }else if(data.substring(12,14).equals("10")){
                error = "个性化失败-无效ID";
            }else if(data.substring(12,14).equals("11")){
                error = "个性化失败-无指纹按下";
            }
        }
        WeiboDialogUtils.closeDialog(Data.getdialog());
        return error;
    }

    /**
     * byte数组转为字符串
     */
    public static String bytetostring(byte[] data){
        final StringBuilder stringBuilder = new StringBuilder();
        if (data != null && data.length > 0) {
            for (byte byteChar : data)
                stringBuilder.append(String.format("%02X", byteChar));
            return stringBuilder.toString().toLowerCase();
        }
        return null;
    }

    /**
     * 公钥转为地址
     *
     * @return
     */
    public static String address() {
        String address = "";
        if (Data.getbizhong().equals("BTC")) {
            Data.setpubkey(Data.getdata());
            LogCook.d("btc公钥", Data.getdata());
            byte[] publicKey = new BigInteger(Data.getdata(), 16).toByteArray();
            byte[] sha256Bytes = Utils.sha256(publicKey);
            System.out.println("sha256加密=" + Utils.bytesToHexString(sha256Bytes));
            RIPEMD160Digest digest = new RIPEMD160Digest();
            digest.update(sha256Bytes, 0, sha256Bytes.length);
            byte[] ripemd160Bytes = new byte[digest.getDigestSize()];
            digest.doFinal(ripemd160Bytes, 0);
            System.out.println("ripemd160加密=" + Utils.bytesToHexString(ripemd160Bytes));
            byte[] networkID = new BigInteger("6F", 16).toByteArray();//主网为00
            byte[] extendedRipemd160Bytes = Utils.add(networkID, ripemd160Bytes);
            System.out.println("添加NetworkID=" + Utils.bytesToHexString(extendedRipemd160Bytes));
            byte[] twiceSha256Bytes = Utils.sha256(Utils.sha256(extendedRipemd160Bytes));
            System.out.println("两次sha256加密=" + Utils.bytesToHexString(twiceSha256Bytes));
            byte[] checksum = new byte[4];
            System.arraycopy(twiceSha256Bytes, 0, checksum, 0, 4);
            System.out.println("checksum=" + Utils.bytesToHexString(checksum));
            byte[] binaryBitcoinAddressBytes = Utils.add(extendedRipemd160Bytes, checksum);
            System.out.println("添加checksum之后=" + Utils.bytesToHexString(binaryBitcoinAddressBytes));
            address = Base58.encode(binaryBitcoinAddressBytes);
            System.out.println("bitcoinAddress=" + address);
            Data.setbtcaddress(address);
        } else if (Data.getbizhong().equals("ETH")) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LogCook.d("eth公钥", Data.getdata());
            String data3 = Data.getdata().substring(2, Data.getdata().length());
            byte[] bytes = new byte[data3.length() / 2];
            for (int i = 0; i < data3.length() / 2; i++) {//16进制字符串转byte[]
                String subStr = data3.substring(i * 2, i * 2 + 2);
                bytes[i] = (byte) Integer.parseInt(subStr, 16);
            }
            byte[] byteAddress = KECCAK256.keccak256(bytes);
            final StringBuilder stringBuilder = new StringBuilder();
            if (byteAddress != null && byteAddress.length > 0) {
                for (byte byteChar : byteAddress)
                    stringBuilder.append(String.format("%02X", byteChar));
                LogCook.d("以太坊地址", stringBuilder.toString().substring(24, stringBuilder.length()).toLowerCase());
            }
            address = stringBuilder.toString().substring(24, stringBuilder.length()).toLowerCase();
            Data.setethaddress(address);
        } else if (Data.getbizhong().equals("XRP")) {
            LogCook.d("xrp公钥", Data.getdata());
            byte[] publicKey = new BigInteger(Data.getdata(), 16).toByteArray();
            byte[] sha256Bytes = Utils.sha256(publicKey);
            System.out.println("sha256加密=" + Utils.bytesToHexString(sha256Bytes));
            RIPEMD160Digest digest = new RIPEMD160Digest();
            digest.update(sha256Bytes, 0, sha256Bytes.length);
            byte[] ripemd160Bytes = new byte[digest.getDigestSize()];
            digest.doFinal(ripemd160Bytes, 0);
            System.out.println("ripemd160加密=" + Utils.bytesToHexString(ripemd160Bytes));
            byte[] networkID = new BigInteger("00", 16).toByteArray();
            byte[] extendedRipemd160Bytes = Utils.add(networkID, ripemd160Bytes);
            System.out.println("添加NetworkID=" + Utils.bytesToHexString(extendedRipemd160Bytes));
            byte[] twiceSha256Bytes = Utils.sha256(Utils.sha256(extendedRipemd160Bytes));
            System.out.println("两次sha256加密=" + Utils.bytesToHexString(twiceSha256Bytes));
            byte[] checksum = new byte[4];
            System.arraycopy(twiceSha256Bytes, 0, checksum, 0, 4);
            System.out.println("checksum=" + Utils.bytesToHexString(checksum));
            byte[] binaryXRPAddressBytes = Utils.add(extendedRipemd160Bytes,checksum);
            System.out.println("添加checksum之后=" + Utils.bytesToHexString(binaryXRPAddressBytes));
            address = Base58.xrpencode(binaryXRPAddressBytes);
            System.out.println("XRPAddress=" + address);
            Data.setxrpaddress(address);
        }
        Data.setdata("");
        return address;
    }

    /**
     * 十进制转为16进制
     * @param s
     * @return
     */
    public static String strTo16(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    /**
     * 16进制转换成为string类型字符串
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    public static void btc() {//获取btc公钥
        Data.setbizhong("BTC");
        String a = "55aa04020000000100000002000005aa55";
        sendble(a, Data.getmService());
    }

    public static void eth() {//获取eth公钥
        Data.setbizhong("ETH");
        String a = "55aa04020000000200000002000006aa55";
        sendble(a, Data.getmService());
    }

    public static void auth0() {//获取auth0公钥
        Data.setbizhong("AUTH0");
        String a = "55aa860100000000000087aa55";
        sendble(a, Data.getmService());
    }

    public static void xrp() {//获取xrp公钥
        Data.setbizhong("XRP");
        String a = "55aa04020000000400000001000003aa55";
        sendble(a, Data.getmService());
    }

    /**
     * sha256哈希值
     * @param strSrc
     * @return strDes
     */
    public static String Encrypt(String strSrc) {
        MessageDigest md = null;
        String strDes = null;
        String encName = "SHA-256";
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance(encName);
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

    public static void end() {
        String a1 = "55aa260110000002000035aa55";//结束签名
        Data.setsign("end");
        Data.setsaoma("yes");
        sendble(a1, Data.getmService());
    }

    /**
     * 获取卡片状态
     */
    public static void gaintype() {
        Data.setbletype("type");
        String a1 = "55aa1e010000000f000010aa55";//获取蓝牙状态指令
        sendble(a1, Data.getmService());
    }

    /**
     * 获取卡片模式
     */
    public static void gainboot() {
        Data.setbletype("gainboot");
        String a1 = "55aa3100000031aa55";
        sendble(a1, Data.getmService());
    }

    /**
     * 进入boot模式Z32
     */
    public static void openboot() {
        if (Data.getbletype().equals("gainboot")) {
            Utils.readboot();
        } else {
            Data.setbletype("openboot");
            String a1 = "55aa5a0000005aaa55";
            sendble(a1, Data.getmService());
        }
    }

    /**
     * 读取boot信息
     */
    public static void readboot() {
        Data.setbletype("readboot");
        String a1 = "55aa4b0000004baa55";
        sendble(a1, Data.getmService());
    }

    /**
     * dfu升级
     */
    public static void dfuupdate(){
        Data.setbletype("dfuupdate");
        String a1 = "55aa3000000030aa55";
        sendble(a1, Data.getmService());
    }

    /**
     * 获取文件sha1
     *
     * @param file
     * @return
     */
    public static String getFileSha1(File file) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[1024 * 1024 * 10];
            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                digest.update(buffer, 0, len);
            }
            String sha1 = new BigInteger(1, digest.digest()).toString(16);
            int length = 40 - sha1.length();
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    sha1 = "0" + sha1;
                }
            }
            return sha1;
        } catch (IOException e) {
            System.out.println(e);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return null;
    }

    /**
     * 握手
     */
    public static void woshou() {
        Data.setbletype("woshou");
//        String path = Environment.getExternalStorageDirectory() + File.separator;//sd根目录
//        File file = new File(path, "ComboWallet_Uart_Only1.bin");
        File file = new File(Data.getpath().substring(0, Data.getpath().lastIndexOf("/")), Data.getpath().substring(Data.getpath().lastIndexOf("/")));
        if (file.exists()) {
            String l1 = String.valueOf(file.length());
            String length = Integer.toHexString(Integer.parseInt(l1));
            if (length.length() == 3) {
                length = "00000" + length;
            } else if (length.length() == 4) {
                length = "0000" + length;
            } else if (length.length() == 5) {
                length = "000" + length;
            } else if (length.length() == 6) {
                length = "00" + length;
            } else if (length.length() == 7) {
                length = "0" + length;
            }
            String sha1 = getFileSha1(file);
            String strhex = strhex("2d00002c" + length + "abcdabcdffffffffffffffffffffffffffffffff" + sha1);
            String a1 = "55aa2d00002c" + length + "abcdabcdffffffffffffffffffffffffffffffff" + sha1 + strhex + "aa55";
            sendble(a1, Data.getmService());
        } else {
            Toast.makeText(Data.getcontext(), "升级文件不存在", Toast.LENGTH_SHORT).show();
            WeiboDialogUtils.closeDialog(Data.getdialog());
        }
    }

    private byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),Data.getcontext().getResources().getString(R.string.invest1) , Toast.LENGTH_SHORT).show();
        }
        return buffer;
    }

    /**
     * 发送数据
     */
    public void send(){
        Data.setbletype("send");
        //String path = Environment.getExternalStorageDirectory() + File.separator+"ComboWallet_Uart_Only1.bin";
        byte[] file=getBytes(Data.getpath());
        final StringBuilder stringBuilder= new StringBuilder();
        if (file != null && file.length > 0) {
            for (byte byteChar : file)
                stringBuilder.append(String.format("%02X", byteChar));
        }
        ArrayList list = new ArrayList();
        int k = 0;
        for (k = 0; k < stringBuilder.toString().length() - 1024; k += 1024) {
            list.add(stringBuilder.toString().substring(k, k + 1024));
        }
        list.add(stringBuilder.toString().substring(k));
        for (int j = 0; j < list.size(); j++) {
                if (Data.getbletype().equals("restartsend")) {
                    if(j!=0) {
                        j = j - 1;
                    }
                    for (int w = 0; w < w + 1; w++) {
                        if (j == list.size() - 1) {
                            Data.setbletype("sendend");
                        } else {
                            Data.setbletype("send...");
                        }
                        String strlength = strlength(list.get(j).toString());
                        LogCook.d("第几个包", String.valueOf(j));
                        if (strlength.length() == 3) {
                            strlength = "0" + strlength;
                        } else if (strlength.length() == 2) {
                            strlength = "00" + strlength;
                        } else if (strlength.length() == 1) {
                            strlength = "000" + strlength;
                        }
                        String strhex = strhex("2200" + strlength + list.get(j).toString());
                        String a1 = "55aa2200" + strlength + list.get(j).toString() + strhex + "aa55";
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                        Date curDate = new Date(System.currentTimeMillis());
                        String str = formatter.format(curDate);
                        LogCook.d("发送时间", str);
                        sendble(a1, Data.getmService());
                        break;
                    }
                }else {
                    for (int w = 0; w < w + 1; w++) {
                        if (Data.getbletype().equals("send")) {
                            if (j == list.size() - 1) {
                                Data.setbletype("sendend");
                            } else {
                                Data.setbletype("send...");
                            }
                            int l2=list.get(j).toString().length();
                            String strlength = strlength(list.get(j).toString());
                            LogCook.d("第几个包", String.valueOf(j));
                            if (strlength.length() == 3) {
                                strlength = "0" + strlength;
                            } else if (strlength.length() == 2) {
                                strlength = "00" + strlength;
                            } else if (strlength.length() == 1) {
                                strlength = "000" + strlength;
                            }
                            String strhex = strhex("2200" + strlength + list.get(j).toString());
                            String a1 = "55aa2200" + strlength + list.get(j).toString() + strhex + "aa55";
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                            Date curDate = new Date(System.currentTimeMillis());
                            String str = formatter.format(curDate);
                            LogCook.d("发送时间", str);
                            sendble(a1, Data.getmService());
                            break;
                        }
                        if (Data.getbletype().equals("restartsend")) {
                            if(j!=0) {
                                j = j - 1;
                            }
                            for (int q = 0; q< q + 1; q++) {
                                if (j == list.size() - 1) {
                                    Data.setbletype("sendend");
                                } else {
                                    Data.setbletype("send...");
                                }
                                String strlength = strlength(list.get(j).toString());
                                LogCook.d("第几个包", String.valueOf(j));
                                if (strlength.length() == 3) {
                                    strlength = "0" + strlength;
                                } else if (strlength.length() == 2) {
                                    strlength = "00" + strlength;
                                } else if (strlength.length() == 1) {
                                    strlength = "000" + strlength;
                                }
                                String strhex = strhex("2200" + strlength   + list.get(j).toString());
                                String a1 = "55aa2200" + strlength + list.get(j).toString() + strhex + "aa55";
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                                Date curDate = new Date(System.currentTimeMillis());
                                String str = formatter.format(curDate);
                                LogCook.d("发送时间", str);
                                sendble(a1, Data.getmService());
                                break;
                            }
                        }
                    }
            }
        }
    }

    /**
     * boot复位
     */
    public static void resetboot(){
        Data.setbletype("resetboot");
        String a1 = "55aa3c0000003caa55";
        sendble(a1, Data.getmService());
    }

    /**
     * 更新结束
     */
    public static void updateend(){
        Data.setbletype("updateend");
        String a1 = "55aa1f0000001faa55";
        sendble(a1, Data.getmService());
    }
    public static int getSubCount_2(String str, String key) {//判断字符串里存在某个字符的个数
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(key, index)) != -1) {
            index = index + key.length();
            count++;
        }
        return count;
    }
    public static int getIndex(String string, int i, String str) {//计算字符串出现的位置
        Matcher slashMatcher = Pattern.compile(str).matcher(string);
        int mIdx = 0;
        while (slashMatcher.find()) {
            mIdx++;
            //当"/"符号第三次出现的位置
            if (mIdx == i) {
                break;
            }
        }
        return slashMatcher.start();
    }

    /**
     * 初始化卡片
     */
    public static void csh(){
        String data = "ed01485343570000";
        String ret = strhex(data);
        String a1 = "55aa" + data + ret + "aa55";
        byte[] bytes = new byte[a1.length() / 2];
        for (int k = 0; k < a1.length() / 2; k++) {//16进制字符串转byte[]
            String subStr = a1.substring(k * 2, k * 2 + 2);
            bytes[k] = (byte) Integer.parseInt(subStr, 16);
        }
        Data.getmService().writeRXCharacteristic(bytes);
    }

    /**
     * 生成助记词
     * @param pin
     * @param count
     */
    public static void generate(String pin,int count){
        byte[] data= Utils.sha256(pin.getBytes());
        final StringBuilder stringBuilder= new StringBuilder();
        if (data != null && data.length > 0) {
            for (byte byteChar : data)
                stringBuilder.append(String.format("%02X", byteChar));
            LogCook.d("哈希值", stringBuilder.toString());
        }
        String strHex = Integer.toHexString(count);
        if(strHex.length()==1){
            strHex="0"+strHex;
        }
        String strlength = strlength(stringBuilder.toString());
        String data1 = "2401000000"+strHex+"00" + strlength + stringBuilder.toString();
        String ret=strhex(data1);
        String a = "55aa2401000000"+strHex+"00" + strlength + stringBuilder.toString() + ret + "aa55";
        sendble(a, Data.getmService());
    }

    /**
     * 恢复助记词
     * @param pin
     * @param zhujici
     */
    public static void recover(String pin,String zhujici){
        int count = 0;//统计空格个数
        for (int i = 0; i < zhujici.length(); i++) {
            char tem = zhujici.charAt(i);
            if (tem == ' ') // 空格
                count++;
        }
        int count1 = count + 1;//助记词个数
        String strHex = Integer.toHexString(count1);//16进制助记词位数
        if(strHex.length()==1){
            strHex="0"+strHex;
        }
        byte[] data= Utils.sha256(pin.getBytes());
        final StringBuilder stringBuilder= new StringBuilder();
        if (data != null && data.length > 0) {
            for (byte byteChar : data)
                stringBuilder.append(String.format("%02X", byteChar));
            LogCook.d("哈希值", stringBuilder.toString());//pin码sha256结果
        }
        String zhujici16= Utils.strTo16(zhujici);
        if((zhujici16.length()&1) != 1){   //是偶数
            zhujici16=zhujici16+"00";
        }else{
            zhujici16=zhujici16+"0";
        }
        String strlength = strlength(stringBuilder.toString()+zhujici16);
        String data1 = "2401010000" + strHex + "00" + strlength +stringBuilder.toString()+zhujici16;
        String ret=strhex(data1);
        String a = "55aa"+data1+ ret + "aa55";
        sendble(a, Data.getmService());
    }

    /**
     * dfu升级
     */
    public static void dfu(){
        String a1 = "55aa3200000032aa55";
        sendble(a1, Data.getmService());
    }

    /**
     * 卡内钱包初始化
     */
    public static void walletamount(){
        Data.setbletype("walletamount");
        String ret=Utils.strhex("8C01000000030000");
        String a = "55aa8C01000000030000"+ret+"aa55";
        Data.setresulterror("no");
        sendble(a,Data.getmService());
    }

    /**
     * 重置pin码
     * @param pin
     * @param pin1
     */
    public static void reset(String pin,String pin1,String zjc){
        String password3 = Encrypt(pin);
        int count = 0;//统计空格个数
        for (int i = 0; i < zjc.length(); i++) {
            char tem = zjc.charAt(i);
            if (tem == ' ') // 空格
                count++;
        }
        int count1 = count + 1;//助记词个数
        if (pin1.equals("") || pin.equals("")) {
            Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.f516), Toast.LENGTH_LONG).show();
        } else if (4 != count1) {
            Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.f517), Toast.LENGTH_SHORT).show();
        } else if (!pin1.equals(pin)) {
            Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.f519), Toast.LENGTH_SHORT).show();
        } else {
            Dialog mWeiboDialog = WeiboDialogUtils.createLoadingDialog(Data.getcontext(), Data.getcontext().getResources().getString(R.string.f518));
            Data.setdialog(mWeiboDialog);
            Data.setbletype("resetpin");
            String data = Utils.strTo16(zjc) + "00";
            String strlength = strlength(password3 + data);
            String data1 = "280000" + strlength + password3 + data;
            String ret = strhex(data1);
            String a = "55aa280000" + strlength + password3 + data + ret + "aa55";
            sendble(a, Data.getmService());
        }
    }
    public static void sendble(String a, UartService mService){//发送蓝牙卡待签名的数据
        Data.setbtcsigndata("no");
        ArrayList list = new ArrayList();
        int k = 0;
        for (k = 0; k < a.length() - 40; k += 40) {
            list.add(a.substring(k, k + 40));
        }
        list.add(a.substring(k));
        LogCook.d("发送Ble数据", valueOf(a));
        for (int j = 0; j < list.size(); j++) {
            try {
                byte[] bytes = new byte[list.get(j).toString().length() / 2];
                for (int i = 0; i < list.get(j).toString().length() / 2; i++) {//16进制字符串转byte[]
                    String subStr = list.get(j).toString().substring(i * 2, i * 2 + 2);
                    bytes[i] = (byte) Integer.parseInt(subStr, 16);
                }
                LogCook.d("发送每20个字节数据", list.get(j).toString());
                mService.writeRXCharacteristic(bytes);
                Thread.sleep(20);
                if(Data.getsign().equals("signing")&&j==list.size()-1){//判断线程等待超时处理
                    new Thread(new MyThread()).start();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 蓝牙连接超时处理
     */
    static final Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case 1:
                    if(Data.getreceivedata().equals("no")){
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.u21), Toast.LENGTH_SHORT).show();
                        end();
                        Data.setreceivedata("yes");
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Data.getmService().disconnect();
                    }
            }
            super.handleMessage(msg);
        }
    };

    public static class MyThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    if(Data.getreceivedata().equals("yes")){
                        break;
                    }else {
                        Thread.sleep(60000);
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    public static String strlength(String s){//获取长度
        StringBuilder sb = new StringBuilder(s);
        byte[] bytes2 = new byte[sb.length() / 2];
        for (int i = 0; i < sb.length() / 2; i++) {//16进制字符串转byte[]
            String subStr = sb.substring(i * 2, i * 2 + 2);
            bytes2[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return Integer.toHexString(Integer.parseInt(valueOf(bytes2.length)));
    }
    public static String strhex(String data){//获取校验和
        byte[] bytes1 = new byte[data.length() / 2];
        for (int i = 0; i < data.length() / 2; i++) {//16进制字符串转byte[]
            String subStr = data.substring(i * 2, i * 2 + 2);
            bytes1[i] = (byte) Integer.parseInt(subStr, 16);
        }
        String ret = "";
        byte BCC[] = new byte[1];
        for (int i = 0; i < bytes1.length; i++) {
            BCC[0] = (byte) (BCC[0] ^ bytes1[i]);
        }
        String hex = Integer.toHexString(BCC[0] & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        ret += hex.toUpperCase();
        return ret;
    }

    /**
     * 蓝牙断开连接后弹窗
     * @param mService
     * @param context
     */
    public static void dialog(UartService mService, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.show();
        builder.setCancelable(false)//设置点击对话框外部区域不关闭对话框
        .setTitle("Blutooth");
        if(Data.getrestart()==1) {
            builder.setMessage(R.string.u22);
            Data.setrestart(0);
        }else{
            builder.setMessage(R.string.u23);
        }
        builder.setNegativeButton(R.string.u24, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                WeiboDialogUtils.closeDialog(Data.getdialog());
            }
        });
        builder.setPositiveButton(R.string.u25, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(Data.getisblecomment().equals("2")) {
                    signcount1 = 0;
                    Dialog mWeiboDialog = WeiboDialogUtils.createLoadingDialog(context, context.getResources().getString(R.string.type));
                    Data.setdialog(mWeiboDialog);
                    mService.connect(Data.getDeviceaddress());
                }
                dialog.dismiss();
            }
        })
        .show();
    }

    /**
     * 接收蓝牙返回信息处理逻辑
     * @param context
     */
    public void service_init(Context context) {
        Intent bindIntent = new Intent(context, UartService.class);
        context.bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        LocalBroadcastManager.getInstance(context).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());
    }
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UartService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(UartService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(UartService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(UartService.DEVICE_DOES_NOT_SUPPORT_UART);
        return intentFilter;
    }
    private UartService mService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            mService = ((UartService.LocalBinder) rawBinder).getService();
            Data.setmService(mService);
            if (!mService.initialize()) {
                finish();
            }
            if(Data.gettype().equals("fingerprints")){
                Data.setbletype("select fingerprints");
                String data = "55aaf1000000f1aa55";
                sendble(data, Data.getmService());
            }else if(Data.gettype().equals("cardamount")||Data.gettype().equals("spxqactivity")){
                walletamount();
            }
        }
        public void onServiceDisconnected(ComponentName classname) {
            mService = null;
        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(UARTStatusChangeReceiver);
        } catch (Exception ignore) {
            Log.e(TAG, ignore.toString());
        }
        unbindService(mServiceConnection);
        mService.stopSelf();
        mService= null;
    }
    public BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(device.getName()==null||device==null){

                            }else {
                                if (Data.getbletype().equals("dfuupdate")) {
                                    if (device.getName().equals("DfuTarg")) {
                                        Intent intent = new Intent(Data.getcontext(), DfuUpdateActivity.class);
                                        intent.putExtra(MAC_ADDRESS, device.getAddress());
                                        intent.putExtra(DEVICE_NAME, device.getName());
                                        Data.getcontext().startActivity(intent);
                                        Data.getBluetoothAdapter().stopLeScan(mLeScanCallback);
                                    }
                                }
                            }
                        }
                    });
                }
            });
        }
    };
    private int signcount=0;
    public static int signcount1=0;
    /**
     * 与蓝牙交互返回结果逻辑
     */
    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(UartService.ACTION_GATT_CONNECTED)) {
                LogCook.d("ble状态：","连接");
                Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.u26), Toast.LENGTH_SHORT).show();
                if(Data.getisblecomment().equals("2")) {
                    WeiboDialogUtils.closeDialog(Data.getdialog());
                }
            }
            if (action.equals(UartService.ACTION_GATT_DISCONNECTED)) {//蓝牙断开连接
                Data.setisblecomment("2");LogCook.d("ble状态：","断开");
                if(signcount1==0|| Data.getsigncount1()==0) {
                    signcount1=1;
                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.u27), Toast.LENGTH_SHORT).show();
                    if(Data.getbletype().equals("openboot")) {//进入boot成功
                        Data.setisapp("no");Data.settype("");
                        LogCook.d("boot状态", "进入boot成功");
                        mService.connect(Data.getDeviceaddress());
                    }else if(Data.getbletype().equals("dfuupdate")) {//进入boot成功
                        LogCook.d("boot状态","进入boot成功(dfu升级)");Data.settype("");
                        Data.getBluetoothAdapter().startLeScan(mLeScanCallback);
                    } else {
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        if (Data.getbletype().equals("chushihua")) {
                            Data.setbletype("");Data.settype("");
                            Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.u28), Toast.LENGTH_SHORT).show();
                            Data.settype("main");
                        }
                        if((Data.getscan().equals("1")||Data.getscan().equals("2"))&&Data.gettype().equals("type")){//正在配对

                        }else {
                            Utils.dialog(mService, Data.getcontext());
                        }
                    }
                }
            }
            if (action.equals(UartService.ACTION_DATA_AVAILABLE)) {//蓝牙交互结果
                final String data = intent.getStringExtra(UartService.EXTRA_DATA);
                final String data2 = intent.getStringExtra(UartService.EXTRA_DATA2);
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (!data.contains("bleerror")&& data !=null) {
                                if(Data.getbletype().contains("Recording fingerprints")&& Data.getresulterror().equals("no")) {
                                    Data.setresulterror("yes");
                                    Toast.makeText(Data.getcontext(), "录入指纹成功", Toast.LENGTH_SHORT).show();
                                    if (Data.gettype().equals("main+c")) {
                                        Intent intent = new Intent(Data.getcontext(), CreateActivity.class);
                                        Data.getcontext().startActivity(intent);
                                    } else if (Data.gettype().equals("main+r")) {
                                        Intent intent = new Intent(Data.getcontext(), RecoverActivity.class);
                                        Data.getcontext().startActivity(intent);
                                    }
                                }else if(Data.getbletype().contains("Recording")&& Data.getresulterror().equals("no")) {//添加指纹
                                    Data.setresulterror("yes");
                                    Toast.makeText(Data.getcontext(), "录入指纹成功", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Data.getcontext(), FingerprintsXQ.class);
                                    Data.getcontext().startActivity(intent);
                                }else if(Data.getbletype().contains("Delete")&& Data.getresulterror().equals("no")) {//删除指纹
                                    Data.setresulterror("yes");
                                    Toast.makeText(Data.getcontext(), "删除指纹成功", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Data.getcontext(), Fingerprints.class);
                                    Data.getcontext().startActivity(intent);
                                }else if(Data.getbletype().equals("select fingerprints")) {//获取指纹信息
                                    new Fingerprints().bleresult(data2);
                                }else if(Data.getbletype().equals("reset select")) {//获取指纹信息
                                    String data=data2.substring(6,data2.length());
                                    String a=hexString2binaryString(data);
                                    if(a.substring(0,3).equals("000")){
                                        Toast.makeText(Data.getcontext(), "录入指纹失败，请重新录入", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Intent intent = new Intent(Data.getcontext(), CreateActivity.class);
                                        Data.getcontext().startActivity(intent);
                                    }
                                }else if(Data.getbletype().equals("resetselect")) {//获取指纹信息
                                    String data=data2.substring(6,data2.length());
                                    String a=hexString2binaryString(data);
                                    if(a.substring(0,3).equals("000")){
                                        new BleActivity().fingerprint();
                                    }else{
                                        if(Data.gettype().equals("main+c")) {
                                            Intent intent = new Intent(Data.getcontext(), CreateActivity.class);
                                            Data.getcontext().startActivity(intent);
                                        }else if(Data.gettype().equals("main+r")){
                                            Intent intent = new Intent(Data.getcontext(), RecoverActivity.class);
                                            Data.getcontext().startActivity(intent);
                                        }
                                    }
                                }else if(Data.getbletype().equals("gainboot")) {//获取卡片模式
                                    if(Data.getisapp().equals("no")) {
                                        readboot();
                                    }else {
                                        if (data2.equals("00")) {//app
                                            gaintype();
                                        } else if (data2.equals("01")) {//BootLoader
                                            Intent intent = new Intent(Data.getcontext(), GengxinActivity.class);
                                            Data.getcontext().startActivity(intent);
                                        }
                                    }
                                }else if (Data.getbletype().equals("resetpin")) {//重置pin码
                                    Data.setbletype("");
                                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.u29), Toast.LENGTH_SHORT).show();
                                    WeiboDialogUtils.closeDialog(Data.getdialog());
                                }else if (Data.getbletype().equals("recover")) {//导入助记词
                                    if (Data.getbizhong().equals("1")) {
                                        Intent intent = new Intent(Data.getcontext(), IndexActivity.class);
                                        Data.getcontext().startActivity(intent);
                                    }
                                }else if (Data.getbletype().equals("Initialize")) {//生成助记词
                                    Intent intent = new Intent(Data.getcontext(), IndexActivity.class);
                                    Data.getcontext().startActivity(intent);
                                }else if (Data.getbletype().equals("address")) {//生成公钥 地址
                                    if (Data.getbizhong().equals("BTC")) {
                                        String address = Utils.address();
                                        if (!TextUtils.isEmpty(address)) {
                                            Bitmap codeBitmap = null;
                                            try {
                                                codeBitmap = Utils.createCode(address);
                                                Data.setimgCode(codeBitmap);
                                            } catch (WriterException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        eth();
                                    } else if (Data.getbizhong().equals("ETH")) {
                                        String address = Utils.address();
                                        if (!TextUtils.isEmpty(address)) {
                                            Bitmap codeBitmap = null;
                                            try {
                                                codeBitmap = Utils.createCode(address);
                                                Data.setethimgCode(codeBitmap);
                                            } catch (WriterException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        //auth0();
                                        xrp();
                                    } else if(Data.getbizhong().equals("AUTH0")){
                                        try {
                                            Thread.sleep(2000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        String auth0pubkey = Data.getdata().substring(0,128);
                                        LogCook.d("auth0公钥", auth0pubkey);
                                        Data.setauth0pubkey(auth0pubkey);
                                        String auth0address = Data.getdata().substring(128, Data.getdata().length());
                                        LogCook.d("auth0地址", auth0address);
                                        Data.setauth0address(auth0address);
                                        //xrp();
                                    } else if(Data.getbizhong().equals("XRP")){
                                        String xrppubkey = Data.getdata();
                                        Data.setxrppub(xrppubkey);
                                        String address = Utils.address();
                                        if (!TextUtils.isEmpty(address)) {
                                            Bitmap codeBitmap = null;
                                            try {
                                                codeBitmap = Utils.createCode(address);
                                                Data.setxrpimgCode(codeBitmap);
                                            } catch (WriterException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        Data.setbletype("");
                                        //new Utilshttp().getauth0user();
                                        zhuce();
                                    }
                                }else if (Data.getbletype().equals("type")) {
                                    if (data2.substring(2, 4).equals("01") && data2.substring(0, 2).equals("01")) {//存在助记词存在pin码
                                        Data.setbizhong("BTC");
                                        Data.setbletype("address");
                                        Dialog mWeiboDialog = WeiboDialogUtils.createLoadingDialog(Data.getcontext(), Data.getcontext().getResources().getString(R.string.utils1));
                                        Data.setdialog(mWeiboDialog);
                                        Utils.btc();
                                    } else {
                                        Intent intent1 = new Intent(Data.getcontext(), BleActivity.class);
                                        Data.getcontext().startActivity(intent1);
                                    }
                                }else if(Data.getsaoma().equals("yes")) {//签名交易
                                    if(Data.getsign().equals("end0")){
                                        if(Data.gettype().equals("czactivity")){
                                            Dialog mWeiboDialog = WeiboDialogUtils.createLoadingDialog(Data.getcontext(), Data.getcontext().getResources().getString(R.string.utils2));
                                            Data.setdialog(mWeiboDialog);
                                        }else if(Data.gettype().equals("txactivity")){
                                            Dialog mWeiboDialog = WeiboDialogUtils.createLoadingDialog(Data.getcontext(), Data.getcontext().getResources().getString(R.string.utils3));
                                            Data.setdialog(mWeiboDialog);
                                        }else if(Data.gettype().equals("spxqactivity")){
                                            Dialog mWeiboDialog = WeiboDialogUtils.createLoadingDialog(Data.getcontext(), Data.getcontext().getResources().getString(R.string.utils4));
                                            Data.setdialog(mWeiboDialog);
                                        }else if(Data.gettype().equals("cdactivity")||Data.gettype().equals("tzactivity")||Data.gettype().equals("tztypeactivity")){
                                            Dialog mWeiboDialog = WeiboDialogUtils.createLoadingDialog(Data.getcontext(), Data.getcontext().getResources().getString(R.string.utils5));
                                            Data.setdialog(mWeiboDialog);
                                        }
                                        new Transfer().chushihua();
                                    }else if (Data.getsign().equals("number")) {
                                        if(!data2.equals("")) {
                                            LogCook.d("随机数", data2);
                                            Data.setsaoma("no");
                                            new Transfer().initialize(data2);
                                        }
                                    } else if (Data.getsign().equals("chushihua")) {
                                        if(Data.getresulterror().equals("csh")) {
                                            Data.setresulterror("");
                                            Data.setend("2");
                                            Data.setsaoma("no");
                                            LogCook.d("BLE状态", "初始化成功");
                                            Timer timer = new Timer();
                                            TimerTask tast = new TimerTask() {
                                                @Override
                                                public void run() {
                                                    Looper.prepare();
                                                    if (Data.getbizhong().equals("BTC")) {
                                                        new Transfer().btcchushihua();
                                                    } else if (Data.getbizhong().equals("ETH") || Data.getbizhong().equals("ERC20")||Data.getbizhong().equals("Hier")||
                                                            Data.getbizhong().equals("Pawn")) {
                                                        Data.setethtype("dealid");
                                                        new Transfer().eth();
                                                    } else if (Data.getbizhong().equals("XRP")||Data.getbizhong().equals("AED")) {
                                                        new Utilshttp().getxrpamount();
                                                    }else if (Data.getbizhong().equals("trustset")) {
                                                        new Transfer().trustsetcreatetransaction();
                                                    }
                                                    Looper.loop();
                                                }
                                            };
                                            timer.schedule(tast, 1000);
                                        }
                                    } else if (Data.getsign().equals("signing")) {
                                        if(data2.equals("取消按键")) {

                                        }else {
                                            if (Data.getbizhong().equals("BTC")&& Data.getbtcsigndata().equals("yes")) {
                                                if(!Data.getbtcsign()){
                                                    Data.setbtcsign(true);
                                                }else {
                                                    if (Data.getuxto()) {
                                                        if (!data2.equals("")) {
                                                            signcount++;
                                                            Data.setn(Data.getn() + 1);
                                                            try {
                                                                Thread.sleep(200);
                                                            } catch (InterruptedException e) {
                                                                e.printStackTrace();
                                                            }
                                                            if (Data.getn() == Data.getcount()) {
                                                                Data.setn(0);
                                                                signcount = 0;
                                                                Utils.end();
                                                            } else {
                                                                new Transfer().sign(Data.getresult());
                                                            }
                                                        }
                                                    } else {
                                                        if (!Data.getdata().equals("")) {
                                                            Data.setdata2(Data.getdata().substring(2, Data.getdata().length()));
                                                            Data.setsaoma("no");
                                                            Utils.end();
                                                        }
                                                    }
                                                }
                                            } else if (Data.getbizhong().equals("ETH")|| Data.getbizhong().equals("ERC20")||Data.getbizhong().equals("Hier")
                                                    ||Data.getbizhong().equals("Pawn")) {
                                                if(Data.getauth0sign().equals("login_register")&&Data.gettype().equals("login")) {
                                                    balancebtc();
                                                }else {
                                                    if (!Data.getdata().equals("")) {
                                                        Data.setdata2(Data.getdata().substring(2, Data.getdata().length()));
                                                        Data.setrlpdata(Data.getdata2());
                                                        Data.setethtype("sendtransaction");
                                                        Data.setsaoma("no");
                                                        if (Data.getbizhong().equals("ETH")) {
                                                            end();
                                                        }else if(Data.getbizhong().equals("Hier")||Data.getbizhong().equals("Pawn")){
                                                            new Transfer().eth();
                                                        } else {
                                                            if (Data.getauth0sign().equals("login")) {
                                                                new auth0login().auth0login(Data.getdata());
                                                            } else {
                                                                new Transfer().eth();
                                                            }
                                                        }
                                                    }
                                                }
                                            }else if (Data.getbizhong().equals("XRP")&&!Data.getdata().equals("")) {
                                                LogCook.d("xrpsign",data2.substring(2,data2.length()));
                                                new Transfer().xrpsendtransaction(data2.substring(2,data2.length()));
                                                Data.setdata("");
                                            }else if (Data.getbizhong().equals("AED")&&!Data.getdata().equals("")) {
                                                LogCook.d("aedsign",data2.substring(2,data2.length()));
                                                new Transfer().aedsendtransaction(data2.substring(2,data2.length()));
                                                Data.setdata("");
                                            }else if (Data.getbizhong().equals("trustset")&&!Data.getdata().equals("")) {
                                                LogCook.d("trustsetsign",data2.substring(2,data2.length()));
                                                new Transfer().trustsetsendtransaction(data2.substring(2,data2.length()));
                                                Data.setdata("");
                                            }
                                            Data.setresultdata(Data.getdata2());
                                        }
                                    }else if (Data.getsign().equals("end") && Data.getend().equals("2") && !Data.getbletype().equals("1")) {
                                        Data.setend("1");
                                        Data.setsaoma("no");
                                        if (Data.getbizhong().equals("BTC")) {
                                            new Transfer().btc();
                                        } else if (Data.getbizhong().equals("ETH")|| Data.getbizhong().equals("ERC20")) {
                                            new Transfer().eth();//发送eth交易请求
                                        }
                                    } else if (Data.getsign().equals("end") && Data.getend().equals("1") && !Data.getbletype().equals("1")) {
                                        Data.setsign("");
                                        WeiboDialogUtils.closeDialog(Data.getdialog());
                                    }else if (Data.getsign().equals("end") && Data.getend().equals("3") && !Data.getbletype().equals("1") &&Data.getresulterror().equals("no")) {
                                        Data.setresulterror("yes");
                                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.u30), Toast.LENGTH_SHORT).show();
                                        WeiboDialogUtils.closeDialog(Data.getdialog());
                                    }
                                }else if(Data.getbletype().equals("readboot")){//读取boot成功
                                    LogCook.d("boot状态","读取boot成功");
                                    Data.setisapp("yes");
//                                    if(data2.substring(8,48).equals(data2.substring(48,data2.length()))){
//                                        Toast.makeText(Data.getcontext(), "当前是最新固件，无需下载", Toast.LENGTH_SHORT).show();
//                                        WeiboDialogUtils.closeDialog(Data.getdialog());
//                                    }else {
                                        woshou();
                                    //}
                                }else if(Data.getbletype().equals("woshou")){//握手成功
                                    LogCook.d("boot状态","握手成功");
                                    send();
                                }else if(Data.getbletype().equals("sendend")) {
                                    LogCook.d("boot状态", "发送数据成功");
                                    updateend();
                                }else if(Data.getbletype().equals("updateend")) {//结束成功
                                    try {
                                        LogCook.d("boot状态","结束成功");
                                        Thread.sleep(5000);
                                        resetboot();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }else if(Data.getbletype().equals("resetboot")) {//复位成功
                                    LogCook.d("boot状态","复位成功");
                                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.u31), Toast.LENGTH_SHORT).show();
                                    WeiboDialogUtils.closeDialog(Data.getdialog());
                                    Data.setbletype("");
                                    Data.setrestart(1);
                                }else if(Data.getbletype().equals("dfureadapp")){
                                    dfuupdate();
                                }else if(Data.getbletype().equals("walletamount")&& Data.getresulterror().equals("no")){
                                    Data.setresulterror("yes");
                                    BigInteger bigint=new BigInteger(data2, 16);
                                    int numb=bigint.intValue();
                                    String amount="";
                                    if(numb==0){
                                        amount="0";
                                    }else {
                                        amount = String.valueOf(numb).substring(0, String.valueOf(numb).length() - 2);
                                    }
                                    LogCook.d("卡内余额",amount);
                                    Data.getcardmoney().setText(amount);
                                    if(Data.gettype().equals("cardamount")||Data.gettype().equals("spxqactivity")) {
                                        new Utilshttp().getptamount();
                                    }else{
                                        WeiboDialogUtils.closeDialog(Data.getdialog());
                                    }
                                }else if(Data.getbletype().equals("walletcsh")&& Data.getresulterror().equals("no")){
                                    Data.setresulterror("yes");
                                    LogCook.d("典当电子钱包","钱包初始化成功");
                                }else if(Data.getbletype().equals("walletcz")&& Data.getresulterror().equals("no")){
                                    Data.setresulterror("yes");
                                    new Utilshttp().transference();
                                }else if(Data.getbletype().equals("wallettx")&& Data.getresulterror().equals("no")){
                                    Data.setresulterror("yes");
                                    if(Data.gettype().equals("spxqactivity")){
                                        new Utilshttp().shopping();
                                    }else {
                                        new Utilshttp().rollout();
                                    }
                                }else if(Data.getbletype().equals("chushihuazhiwen")){
                                    Data.setbletype("chushihua");
                                    Utils.csh();
                                }
                        } else if(data !=null){//蓝牙返回错误信息处理
                            if(Data.gettype().equals("fragment3")&&!Data.gettype().equals("")) {
                                if (Data.getsaoma().equals("yes")) {
                                    String error = Utils.bledata(data);
                                    if (!error.equals("")) {
                                        Toast.makeText(Data.getcontext(), error, Toast.LENGTH_SHORT).show();
                                    }
                                    Data.setend("1");
                                    Utils.end();
                                    Data.settype("");
                                }
                            }else if ((Data.getbletype().equals("Initialize")||Data.gettype().equals("main+c"))&& Data.getresulterror().equals("no")) {//生成助记词
                                Data.setresulterror("yes");
                                String error = Utils.bledata(data);
                                if (!error.equals("")) {
                                    Toast.makeText(Data.getcontext(), error, Toast.LENGTH_SHORT).show();
                                }
                                Intent intent = new Intent(Data.getcontext(), CreateActivity.class);
                                Data.getcontext().startActivity(intent);
                            }else if ((Data.getbletype().equals("recover")||Data.gettype().equals("main+r"))&& Data.getresulterror().equals("no")) {//导入助记词
                                Data.setresulterror("yes");
                                String error = Utils.bledata(data);
                                if (!error.equals("")) {
                                    Toast.makeText(Data.getcontext(), error, Toast.LENGTH_SHORT).show();
                                }
                                Intent intent = new Intent(Data.getcontext(), RecoverActivity.class);
                                Data.getcontext().startActivity(intent);
                            }else if(!Data.gettype().equals("")){
                                if((Data.gettype().contains("main+")|| Data.getbletype().contains("Recording")|| Data.getbletype().contains("Delete"))&& Data.getresulterror().equals("no")) {
                                    Data.setresulterror("yes");
                                    if(Data.getbletype().equals("Recording fingerprints3")){
                                        Data.setbletype("reset select");
                                        String data = "55aaf1000000f1aa55";
                                        sendble(data, Data.getmService());
                                    }else {
                                        String error = Utils.bledata(data);
                                        if (!error.equals("")) {
                                            Toast.makeText(Data.getcontext(), error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }else if(!Data.gettype().contains("main+")){
                                    if(Data.getbletype().equals("walletamount")){
                                        if(data.contains("6985")){
                                            Data.setbletype("walletcsh");
                                            String ret= Utils.strhex("8C01000000000000");
                                            String a = "55aa8C01000000000000"+ret+"aa55";
                                            Data.setresulterror("no");
                                            sendble(a, Data.getmService());
                                        }
                                    }
                                    String error = Utils.bledata(data);
                                    if (!error.equals("")) {
                                        if(Data.gettype().equals("spxqactivity")){
                                            Toast.makeText(Data.getcontext(), "购买失败"+error, Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (Data.getauth0sign().equals("login_register") && Data.gettype().equals("login") && Data.getsign().equals("signing")) {
                                            balancebtc();
                                        }
                                        Toast.makeText(Data.getcontext(), error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }
                });
            }
            if (action.equals(UartService.ACTION_GATT_SERVICES_DISCOVERED)) {
                try {
                    mService.enableTXNotification();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            if (action.equals(UartService.DEVICE_DOES_NOT_SUPPORT_UART)) {
                mService.disconnect();
            }
        }
    };

    /**
     * 查询币种余额
     */
    public void balancebtc(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //比特币余额查询
                Data.setbtctype("balance");
                String btcJson="";
                if(Data.getapptype().equals("cold")) {
                    btcJson = "{\"min\":0,\"max\":9999999,\"address\":\""+ Data.getbtcaddress()+"\"}";
                }else if(Data.getapptype().equals("hot")) {
                    btcJson = "{\"min\":0,\"max\":9999999,\"address\":\""+ Data.gethotbtcaddress()+"\"}";
                }
                //String btcJson = "{\"method\": \"listunspent\", \"params\": [0,9999999,[\"" + Data.getbtcaddress() + "\"]]}";
                String btcerror = Utils.getbtchttp(btcJson);
                if(!btcerror.equals("")) {
                    if (btcerror.contains("success")) {

                    } else {
                        String finalBtcerror = btcerror;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.u32) + finalBtcerror, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                balanceeth();
            }
        }).start();
    }

    private String current_price;
    private String current_price1;
    private String result="";
    public void balanceeth(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //以太坊余额查询
                Data.setethtype("balance");
                String address="";
                if(Data.getapptype().equals("cold")) {
                    address ="{\"address\":\"0x"+ Data.getethaddress()+"\"}";
                }else if(Data.getapptype().equals("hot")) {
                    address ="{\"address\":\"0x"+ Data.gethotethaddress()+"\"}";
                }
                String etherror = Utils.getethhttp(address);
                if(!etherror.equals("")) {
                    if (etherror.contains("success")) {

                    } else {
                        String finalEtherror = etherror;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.u33) + finalEtherror, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                Data.setbizhong("BTC");
                //new Utilshttp().gethieramount();
                new Utilshttp().getxrpamount();
            }
        }).start();
    }


    public void send2(){
        new Thread(new Runnable() {
            public void run() {
                send("http://111.225.200.132:8023/cgi-bin/getmarket");
                if (result != null) {
                    try {
                        Data.getbledata().clear();
                        LogCook.d("行情数据", result);
                        int count = getSubCount_2(result, "{");Data.getbledata().add("ETH");
                        Data.getbledata().add("BTC");
                        //Data.getbledata().add("Hier");
                        Data.getbledata().add("XRP");Data.getbledata().add("AED");
                        if (count == 1) {
                            JSONObject jsonObject = new JSONObject(result);
                            current_price = jsonObject.getString("base");
                            DecimalFormat df = new DecimalFormat("0.00000000");
                            BigDecimal b1 = null;
                            BigDecimal b2 = null;
                            Looper.prepare();
                            if(current_price.equals("ETH")){
                                LogCook.d("ETH行情数据", result);
                                Toast.makeText(Data.getcontext(), "BTC行情获取失败，请刷新重试", Toast.LENGTH_SHORT).show();
                                current_price = jsonObject.getString("high");
                                Data.setethrmbbalance(current_price);
                                Data.setbtcrmbbalance("0");
                                b1 = new BigDecimal(Double.parseDouble(current_price));
                                b2 = new BigDecimal(Double.parseDouble(Data.getethbalance()));
                            }else if(current_price.equals("BTC")){
                                Data.getbledata().add("BTC");
                                Data.getbledata().add("Hier");Data.getbledata().add("XRP");
                                LogCook.d("BTC行情数据", result);
                                Toast.makeText(Data.getcontext(), "ETH行情获取失败，请刷新重试", Toast.LENGTH_SHORT).show();
                                current_price = jsonObject.getString("high");
                                Data.setbtcrmbbalance(current_price);
                                Data.setethrmbbalance("0");
                                b1 = new BigDecimal(Double.parseDouble(current_price));
                                b2 = new BigDecimal(Double.parseDouble(Data.getbtcbalance()));
                            }
                            Looper.loop();
                            Double d = b1.multiply(b2).doubleValue();
                            d1 = df.format(d);
                            BigDecimal a1 = new BigDecimal(d1);
                            //BigDecimal hier = new BigDecimal(Data.gethieramount());
                            BigDecimal xrp = new BigDecimal(Double.parseDouble(Data.getxrpamount())*1.9*7);
                            BigDecimal amount1 = a1.add(xrp);
                            //BigDecimal amount2 = amount1.add(hier);
                            df1 = new DecimalFormat("0.00");
                            Data.setamountrmb(df1.format(amount1));
                            if(!Data.gettype().equals("fragment1")) {
                                Data.getcontext().startActivity(new Intent(Data.getcontext(), IndexActivity.class));
                            }else{
                                Looper.prepare();
                                Data.gethandler().post(runnableUi1);
                            }
                        } else {
                            int index = getIndex(result, 1, "[}]");
                            String btchq = result.substring(0, index + 1);
                            LogCook.d("BTC行情数据", btchq);
                            String ethhq = result.substring(index + 2, result.length() - 1);
                            LogCook.d("ETH行情数据", ethhq);
                            DecimalFormat df = new DecimalFormat("0.00000000");
                            JSONObject jsonObject = new JSONObject(ethhq);
                            current_price = jsonObject.getString("high");
                            JSONObject jsonObject1 = new JSONObject(btchq);
                            current_price1 = jsonObject1.getString("high");
                            Data.setethrmbbalance(current_price);
                            Data.setbtcrmbbalance(current_price1);
                            BigDecimal b1 = new BigDecimal(Double.parseDouble(current_price));
                            BigDecimal b2 = new BigDecimal(Double.parseDouble(Data.getethbalance()));
                            Double d = b1.multiply(b2).doubleValue();
                            d1 = df.format(d);//eth人民币价格
                            BigDecimal b3 = new BigDecimal(Double.parseDouble(current_price1));
                            BigDecimal b4 = new BigDecimal(Double.parseDouble(Data.getbtcbalance()));
                            Double d2 = b3.multiply(b4).doubleValue();
                            d3 = df.format(d2);//btc人民币价格
                            BigDecimal btc = new BigDecimal(d1);
                            BigDecimal eth = new BigDecimal(d3);
                            //BigDecimal hier = new BigDecimal(Data.gethieramount());
                            xrp = new BigDecimal(Double.parseDouble(Data.getxrpamount())*1.9*7);
                            BigDecimal amount1 = btc.add(eth);
                            BigDecimal amount2 = amount1.add(xrp);
                            //BigDecimal amount3 = amount2.add(hier);
                            df1 = new DecimalFormat("0.00");
                            Data.setamountrmb(df1.format(amount2));
                            if(!Data.gettype().equals("fragment1")) {
                                Data.getcontext().startActivity(new Intent(Data.getcontext(), IndexActivity.class));
                            }else{
                                Looper.prepare();
                                Data.gethandler().post(runnableUi);
                            }
                        }
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                    } catch(JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    DecimalFormat df1;String d3;String d1;BigDecimal xrp;
    Runnable runnableUi=new Runnable(){
        @Override
        public void run() {
            Data.getbtctext().setText(Data.getbtcbalance());
            Data.getbtcrmbtext().setText("￥"+d3);
            Data.getethtext().setText(Data.getethbalance());
            Data.getethrmbtext().setText("￥"+d1);
            Data.getxrptext().setText(Data.getxrpamount());
            Data.getxrprmbtext().setText("￥"+df1.format(xrp));
            Data.getcountamount().setText(Data.getamountrmb());
            Looper.loop();
        }
    };
    Runnable runnableUi1=new Runnable(){
        @Override
        public void run() {
            if(current_price.equals("BTC")){
                Data.getbtctext().setText(Data.getbtcbalance());
                Data.getbtcrmbtext().setText("￥"+d1);
            }else if(current_price.equals("ETH")){
                Data.getethtext().setText(Data.getethbalance());
                Data.getethrmbtext().setText("￥"+d1);
            }
            Data.getxrptext().setText(Data.getxrpamount());
            Data.getxrprmbtext().setText("￥"+df1.format(xrp));
            Data.getcountamount().setText(Data.getamountrmb());
            Looper.loop();
        }
    };

    public void send(String urlStr) {
        HttpURLConnection conn = null;//声明连接对象
        InputStream is = null;
        try {
            URL url = new URL(urlStr); //URL对象
            conn = (HttpURLConnection) url.openConnection(); //使用URL打开一个链接,下面设置这个连接
            conn.setRequestMethod("GET"); //使用get请求
            conn.setRequestProperty("Content-Type", "application/json");
            if (conn.getResponseCode() == 200) {//返回200表示连接成功
                is = conn.getInputStream(); //获取输入流
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader bufferReader = new BufferedReader(isr);
                String inputLine = "";
                while ((inputLine = bufferReader.readLine()) != null) {
                    result += inputLine + "\n";
                }
                is.close();
            }
            if(result==null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Data.getcontext(), "获取行情失败", Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                    }
                });
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),Data.getcontext().getResources().getString(R.string.invest1) , Toast.LENGTH_SHORT).show();
            WeiboDialogUtils.closeDialog(Data.getdialog());
        }
    }

    public void zhuce(){
        (new Thread(mRun1)).start();
    }
    /**
     * btc注册地址
     */
    Runnable mRun1 = new Runnable() {
        @Override
        public void run() {
            String btcJson="";
            if(Data.getapptype().equals("cold")) {
                btcJson = "{\"method\": \"importaddress\", \"params\": [\"" + Data.getbtcaddress() + "\",\"test1\",false]}";
            }else if(Data.getapptype().equals("hot")) {
                btcJson = "{\"method\": \"importaddress\", \"params\": [\"" + Data.gethotbtcaddress() + "\",\"test1\",false]}";
            }
            LogCook.d("发送参数",btcJson);
            BufferedReader btcreader = null;
            String btcresult="";
            try {
                URL url = new URL("http://111.225.200.132:18332");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                String cred = Base64.encodeBytes(("liuguo" + ":" + "123456").getBytes());
                conn.setRequestProperty("Authorization", "Basic "+cred);
                conn.setRequestProperty("Content-Type","application/json");
                if (btcJson != null && !TextUtils.isEmpty(btcJson)) {
                    byte[] writebytes = btcJson.getBytes();
                    conn.setRequestProperty("Content-Length", valueOf(writebytes.length));
                    conn.connect();
                    OutputStream outwritestream = conn.getOutputStream();
                    outwritestream.write(btcJson.getBytes());
                    outwritestream.flush();
                    outwritestream.close();
                }
                if (conn.getResponseCode() == 200) {
                    btcreader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    btcresult = btcreader.readLine();
                }else{
                    btcreader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    btcresult = btcreader.readLine();
                }
                if (btcresult != null) {
                    JSONObject jsonObject = new JSONObject(btcresult);
                    LogCook.d("BTC网络请求返回数据",btcresult);
                    if (jsonObject.getString("error").equals("null")) {  //判断JSONObject是否包含属性值
                        LogCook.d("注册BTC地址", "注册BTC地址成功");
                    }else{
                        String error1=jsonObject.getString("error");
                        if(Data.getbtctype().equals("balance")) {
                            Data.setbtcbalance("0.00000000");
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.u34) + error1, Toast.LENGTH_SHORT).show();
                                WeiboDialogUtils.closeDialog(Data.getdialog());
                            }
                        });
                    }
                }
                balancebtc();
            } catch (Exception e) {
                e.printStackTrace();
                balancebtc();
            } finally {
                if (btcreader != null) {
                    try {
                        btcreader.close();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),Data.getcontext().getResources().getString(R.string.invest1) , Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    /**
     * 获取状态栏高度
     * @param content
     * @return
     */
    public static int getstatus(Context content){
        int result=0;
        int resourceId = content.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = content.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 打印长字符串log
     * @param tag
     * @param msg
     */
//    public static void log(String tag, String msg) {  //信息太长,分段打印
//        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
//        //  把4*1024的MAX字节打印长度改为2001字符数
//        int max_str_length = 2001 - tag.length();
//        //大于4000时
//        while (msg.length() > max_str_length) {
//            Log.i(tag, msg.substring(0, max_str_length));
//            msg = msg.substring(max_str_length);
//        }
//        //剩余部分
//        Log.i(tag, msg);
//    }

    /**
     * 语言切换
     * @param context
     */
    public static void reStart(Context context) {
        Intent intent = new Intent(context, LanguagesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static void mainreStart(Context context) {
        Data.setislanguages(true);
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 判断是否有网络连接
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 分享网页类型至微信
     *
     * @param context 上下文
     * @param appId   微信的appId
     * @param webUrl  网页的url
     * @param title   网页标题
     * @param content 网页描述XX
     * @param bitmap  位图
     * @param id  分享到哪里
     */
    public static void shareWeb(Context context, String appId, String webUrl, String title, String content, Bitmap bitmap, int id) {
        // 通过appId得到IWXAPI这个对象
        IWXAPI wxapi = WXAPIFactory.createWXAPI(context, appId);
        if (!wxapi.isWXAppInstalled()) {
            Toast.makeText(Data.getcontext(), context.getResources().getString(R.string.ff14), Toast.LENGTH_SHORT).show();
            return;
        }
        // 初始化一个WXWebpageObject对象
        WXWebpageObject webpageObject = new WXWebpageObject();
        // 填写网页的url
        webpageObject.webpageUrl = webUrl;
        // 用WXWebpageObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        // 填写网页标题、描述、位图
        msg.title = title;
        msg.description = content;
        // 如果没有位图，可以传null，会显示默认的图片
        msg.setThumbImage(bitmap);
        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        // transaction用于唯一标识一个请求（可自定义）
        req.transaction = "webpage";
        // 上文的WXMediaMessage对象
        req.message = msg;
        // SendMessageToWX.Req.WXSceneSession是分享到好友会话
        // SendMessageToWX.Req.WXSceneTimeline是分享到朋友圈
        if(id==0) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        }else if(id==1){
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        // 向微信发送请求
        wxapi.sendReq(req);
    }
}
