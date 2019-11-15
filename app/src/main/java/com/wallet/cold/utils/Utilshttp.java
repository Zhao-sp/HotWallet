package com.wallet.cold.utils;

import android.content.Intent;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.zxing.common.StringUtils;
import com.googlecode.jsonrpc4j.Base64;
import com.wallet.R;
import com.wallet.cold.app.index.Transfer;
import com.wallet.cold.app.pawn.CdActivity;
import com.wallet.cold.app.pawn.login;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wallet.cold.utils.Utils.getIndex;
import static com.wallet.cold.utils.Utils.walletamount;
import static java.lang.String.valueOf;

public class Utilshttp {
    /**
     * auth0查询用户
     */
    public void getauth0user() {
        new Thread(new Runnable() {
            public void run() {
                String result = "";
                try {
                    String data = "{\"ethaddress\":\"0x" + Data.getethaddress() + "\"}";
                    data = URLEncoder.encode(data, "UTF-8");
                    String urlName = Data.gethttp1()+"/hierstarQrCode/pawn/getUser?jsonParams="+data;
                    LogCook.d("发送参数", urlName);
                    URL U = new URL(urlName);
                    URLConnection connection = U.openConnection();
                    connection.connect();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result += line;
                    }
                    LogCook.d("auth0查询用户信息返回数据", result);
                    in.close();
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status_Sucess").equals("true")) {
                        String name= jsonObject.getString("status_Message");
                        if(name.equals("")) {
                            new Utils().balancebtc();
                        }else{
                            Data.setauth0type(name);
                            Data.getcontext().startActivity(new Intent(Data.getcontext(), login.class));
                        }
                    } else if (jsonObject.getString("status_Sucess").equals("false")) {//返回错误信息
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp3) +
                                jsonObject.getString("status_Result") + ":" + jsonObject.getString("status_Message"), Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Looper.loop();
                    }
                } catch (Exception e) {
                    Looper.prepare();
                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp3), Toast.LENGTH_SHORT).show();
                    Data.getmService().disconnect();
                    Data.getmService().close();
                    WeiboDialogUtils.closeDialog(Data.getdialog());
                    Looper.loop();
                    e.printStackTrace();
                }
            }
        }).start(); // 开启线程
    }

    /**
     * 查询海博币余额
     */
    public void gethieramount() {
        new Thread(new Runnable() {
            public void run() {
                String result = "";
                try {
                    String data = "{\"sData\":\"0x" + Data.getethaddress() + "\"}";
                    data = URLEncoder.encode(data, "UTF-8");
                    String urlName = Data.gethttp1()+"/hierstarQrCode/pawn/ethBalance?jsonParams="+data;
                    LogCook.d("发送参数", urlName);
                    URL U = new URL(urlName);
                    URLConnection connection = U.openConnection();
                    connection.connect();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result += line;
                    }
                    LogCook.d("查询海博币余额返回数据", result);
                    in.close();
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status_Sucess").equals("true")) {
                        String message= jsonObject.getString("status_Message");
                        if(message!="") {
                            message = message.substring(2, message.length());
                            String str = new BigInteger(message, 16).toString(10);
                            LogCook.d("海博币余额", str);
                            Data.sethieramount(str);
                            if(Data.gettype().equals("txactivity")||Data.gettype().equals("czactivity")) {
                                Data.gethiertext().setText(str);
                            }
                        }
                        if(Data.gettype().equals("login")||Data.gettype().equals("fragment1")||Data.gettype().equals("type")) {
                            Looper.prepare();
                            getxrpamount();
                            Looper.loop();
                        }
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                    } else if (jsonObject.getString("status_Sucess").equals("false")) {//返回错误信息
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp4) +
                                jsonObject.getString("status_Result") + ":" + jsonObject.getString("status_Message"), Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Looper.loop();
                    }
                } catch (Exception e) {
                    Looper.prepare();
                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp4), Toast.LENGTH_SHORT).show();
                    WeiboDialogUtils.closeDialog(Data.getdialog());
                    Looper.loop();
                    e.printStackTrace();
                }
            }
        }).start(); // 开启线程
    }

    /**
     * 查询平台余额
     */
    public void getptamount() {
        new Thread(new Runnable() {
            public void run() {
                String result = "";
                try {
                    String data = "{\"user\":\"" + Data.getauth0type() + "\",\"currencyId\":1}";
                    data = URLEncoder.encode(data, "UTF-8");
                    String urlName = Data.gethttp1()+"/hierstarQrCode/pawn/getBalance?jsonParams="+data;
                    LogCook.d("发送参数", urlName);
                    URL U = new URL(urlName);
                    URLConnection connection = U.openConnection();
                    connection.connect();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result += line;
                    }
                    LogCook.d("查询平台余额返回数据", result);
                    in.close();
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status_Sucess").equals("true")) {
                        String message= jsonObject.getString("status_Message");
                        if(message!="") {
                            LogCook.d("平台余额", message);
                            if(Data.gettype().equals("spxqactivity")) {
                                Data.setptamount(message);
                            }else{
                                Thread.sleep(2000);
                                Data.getpttext().setText(message);
                            }
                        }
                        if(Data.gettype().equals("cardamount")||Data.gettype().equals("spxqactivity")) {
                            WeiboDialogUtils.closeDialog(Data.getdialog());
                        }
                    } else if (jsonObject.getString("status_Sucess").equals("false")) {//返回错误信息
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp6) +
                                jsonObject.getString("status_Result") + ":" + jsonObject.getString("status_Message"), Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Looper.loop();
                    }
                    if(!Data.gettype().equals("cardamount")&&!Data.gettype().equals("spxqactivity")) {
                        gethieramount();
                    }
                } catch (Exception e) {
                    Looper.prepare();
                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp6), Toast.LENGTH_SHORT).show();
                    WeiboDialogUtils.closeDialog(Data.getdialog());
                    Looper.loop();
                    if(!Data.gettype().equals("cardamount")&&!Data.gettype().equals("spxqactivity")) {
                        gethieramount();
                    }
                    e.printStackTrace();
                }
            }
        }).start(); // 开启线程
    }

    /**
     * 充值，把海博币充值到平台上来
     */
    public void czhier(String sData) {
        new Thread(new Runnable() {
            public void run() {
                String result = "";
                try {
                    String data = "{\"user\":\"" + Data.getauth0type() + "\",\"currencyId\":1,\"sData\":\"0x"+sData+"\"}";
                    data = URLEncoder.encode(data, "UTF-8");
                    String urlName = Data.gethttp1()+"/hierstarQrCode/pawn/deposit?jsonParams="+data;
                    LogCook.d("发送参数", urlName);
                    URL U = new URL(urlName);
                    URLConnection connection = U.openConnection();
                    connection.connect();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result += line;
                    }
                    LogCook.d("把海博币充值到平台返回数据", result);
                    in.close();
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status_Sucess").equals("true")) {
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp7), Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Looper.loop();
                    } else if (jsonObject.getString("status_Sucess").equals("false")) {//返回错误信息
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp8) +
                                jsonObject.getString("status_Result") + ":" + jsonObject.getString("status_Message"), Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Looper.loop();
                    }
                } catch (Exception e) {
                    Looper.prepare();
                    WeiboDialogUtils.closeDialog(Data.getdialog());
                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp8), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }
            }
        }).start(); // 开启线程
    }

    /**
     * 提现，把平台上余额提现到卡内的eth地址的海博币上（海博币相当于一中代币）
     */
    public void txhier(String sData) {
        new Thread(new Runnable() {
            public void run() {
                String result = "";
                try {
                    String data = "{\"user\":\"" + Data.getauth0type() + "\",\"currencyId\":1,\"sAmount\":"+Data.gettxamount()+",\"sData\":\"0x"+sData+"\"}";
                    data = URLEncoder.encode(data, "UTF-8");
                    String urlName = Data.gethttp1()+"/hierstarQrCode/pawn/cashOut?jsonParams="+data;
                    LogCook.d("发送参数", urlName);
                    URL U = new URL(urlName);
                    URLConnection connection = U.openConnection();
                    connection.connect();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result += line;
                    }
                    LogCook.d("提现海博币返回数据", result);
                    in.close();
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status_Sucess").equals("true")) {
                        Looper.prepare();
                        if(Data.gettype().equals("spxqactivity")) {
                            shopping();
                        }else {
                            Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp9), Toast.LENGTH_SHORT).show();
                            WeiboDialogUtils.closeDialog(Data.getdialog());
                        }
                        Looper.loop();
                    } else if (jsonObject.getString("status_Sucess").equals("false")) {//返回错误信息
                        Looper.prepare();
                        if(Data.gettype().equals("spxqactivity")){
                            Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp10) +
                                    jsonObject.getString("status_Result") + ":" + jsonObject.getString("status_Message"), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp10) +
                                    jsonObject.getString("status_Result") + ":" + jsonObject.getString("status_Message"), Toast.LENGTH_SHORT).show();
                        }
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Looper.loop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Looper.prepare();
                    WeiboDialogUtils.closeDialog(Data.getdialog());
                    if(Data.gettype().equals("spxqactivity")){
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp12), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp10), Toast.LENGTH_SHORT).show();
                    }
                    Looper.loop();
                }
            }
        }).start(); // 开启线程
    }

    /**
     * 投资（对一个典当物进行投资）
     */
    public void tzpawn(String sData) {
        new Thread(new Runnable() {
            public void run() {
                String result = "";
                try {
                    String data = "{\"user\":\"" + Data.getauth0type() + "\",\"amount\":"+Data.gettxamount()+",\"objectId\":"+Data.getobjectId()+",\"sData\":\"0x"+sData+"\"}";
                    data = URLEncoder.encode(data, "UTF-8");
                    String urlName = Data.gethttp1()+"/hierstarQrCode/pawn/invest?jsonParams="+data;
                    LogCook.d("发送参数", urlName);
                    URL U = new URL(urlName);
                    URLConnection connection = U.openConnection();
                    connection.connect();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result += line;
                    }
                    LogCook.d("投资返回数据", result);
                    in.close();
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status_Sucess").equals("true")) {
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp13), Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Looper.loop();
                    } else if (jsonObject.getString("status_Sucess").equals("false")) {//返回错误信息
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp14) +
                                jsonObject.getString("status_Result") + ":" + jsonObject.getString("status_Message"), Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Looper.loop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Looper.prepare();
                    WeiboDialogUtils.closeDialog(Data.getdialog());
                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp14), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }).start(); // 开启线程
    }

    public void send() {
        HttpURLConnection conn = null;//声明连接对象
        String urlStr = Data.gethttp1()+"/hierstarQrCode/pawn/objectlist";
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
                String result = "";
                while ((inputLine = bufferReader.readLine()) != null) {
                    result += inputLine + "\n";
                }
                LogCook.d("投资数据",result);
                is.close();
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Data.getcontext(),Data.getcontext().getResources().getString(R.string.invest1) , Toast.LENGTH_SHORT).show();
            WeiboDialogUtils.closeDialog(Data.getdialog());
        }
    }

    /**
     * 赎回商品
     */
    public void shpawn(String sData) {
        new Thread(new Runnable() {
            public void run() {
                String result = "";
                try {
                    String data = "{\"user\":\"" + Data.getauth0type() + "\",\"objectId\":"+Data.getobjectId()+",\"sData\":\"0x"+sData+"\"}";
                    data = URLEncoder.encode(data, "UTF-8");
                    String urlName = Data.gethttp1()+"/hierstarQrCode/pawn/redeem?jsonParams="+data;
                    LogCook.d("发送参数", urlName);
                    URL U = new URL(urlName);
                    URLConnection connection = U.openConnection();
                    connection.connect();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result += line;
                    }
                    LogCook.d("赎回返回数据", result);
                    in.close();
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status_Sucess").equals("true")) {
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp15), Toast.LENGTH_SHORT).show();
                        new CdActivity().getptamount();
                        send();
                        Looper.loop();
                    } else if (jsonObject.getString("status_Sucess").equals("false")) {//返回错误信息
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp16) +
                                jsonObject.getString("status_Result") + ":" + jsonObject.getString("status_Message"), Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Looper.loop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Looper.prepare();
                    WeiboDialogUtils.closeDialog(Data.getdialog());
                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp16), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }).start(); // 开启线程
    }

    /**
     * 分红函数（只有当典当物被赎回了，投资过的记录才能分红）
     */
    public void fhpawn(String sData) {
        new Thread(new Runnable() {
            public void run() {
                String result = "";
                try {
                    String data = "{\"user\":\"" + Data.getauth0type() + "\",\"objectId\":\""+Data.getobjectId()+"\",\"sData\":\"0x"+sData+"\"}";
                    data = URLEncoder.encode(data, "UTF-8");
                    String urlName = Data.gethttp1()+"/hierstarQrCode/pawn/dividend?jsonParams="+data;
                    LogCook.d("发送参数", urlName);
                    URL U = new URL(urlName);
                    URLConnection connection = U.openConnection();
                    connection.connect();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result += line;
                    }
                    LogCook.d("分红返回数据", result);
                    in.close();
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status_Sucess").equals("true")) {
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp17), Toast.LENGTH_SHORT).show();
                        send();
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Looper.loop();
                    } else if (jsonObject.getString("status_Sucess").equals("false")) {//返回错误信息
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp18) +
                                jsonObject.getString("status_Result") + ":" + jsonObject.getString("status_Message"), Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Looper.loop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Looper.prepare();
                    WeiboDialogUtils.closeDialog(Data.getdialog());
                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp18), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }).start(); // 开启线程
    }

    /**
     * 圈存（相当于平台金额减少，卡内钱包金额增加。这个接口就是把平台的余额减少的）
     */
    public void transference() {
        new Thread(new Runnable() {
            public void run() {
                String result = "";
                try {
                    String data = "{\"user\":\"" + Data.getauth0type() + "\",\"amount\":"+Data.gettxamount()+"}";
                    data = URLEncoder.encode(data, "UTF-8");
                    String urlName = Data.gethttp1()+"/hierstarQrCode/pawn/transference?jsonParams="+data;
                    LogCook.d("发送参数", urlName);
                    URL U = new URL(urlName);
                    URLConnection connection = U.openConnection();
                    connection.connect();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result += line;
                    }
                    LogCook.d("圈存返回数据", result);
                    in.close();
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status_Sucess").equals("true")) {
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp19), Toast.LENGTH_SHORT).show();
                        new Utils().walletamount();
                        Looper.loop();
                    } else if (jsonObject.getString("status_Sucess").equals("false")) {//返回错误信息
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp20) +
                                jsonObject.getString("status_Result") + ":" + jsonObject.getString("status_Message"), Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Looper.loop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Looper.prepare();
                    WeiboDialogUtils.closeDialog(Data.getdialog());
                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp20), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }).start(); // 开启线程
    }

    /**
     * 转出（相当于平台余额增加，卡内钱包金额增加。这个接口使平台余额增加的）
     */
    public void rollout() {
        new Thread(new Runnable() {
            public void run() {
                String result = "";
                try {
                    String data = "{\"user\":\"" + Data.getauth0type() + "\",\"amount\":"+Data.gettxamount()+"}";
                    data = URLEncoder.encode(data, "UTF-8");
                    String urlName = Data.gethttp1()+"/hierstarQrCode/pawn/rollout?jsonParams="+data;
                    LogCook.d("发送参数", urlName);
                    URL U = new URL(urlName);
                    URLConnection connection = U.openConnection();
                    connection.connect();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result += line;
                    }
                    LogCook.d("卡内金额转出平台返回数据", result);
                    in.close();
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status_Sucess").equals("true")) {
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp21), Toast.LENGTH_SHORT).show();
                        new Utils().walletamount();
                        Looper.loop();
                    } else if (jsonObject.getString("status_Sucess").equals("false")) {//返回错误信息
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp22) +
                                jsonObject.getString("status_Result") + ":" + jsonObject.getString("status_Message"), Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Looper.loop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Looper.prepare();
                    WeiboDialogUtils.closeDialog(Data.getdialog());
                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp22), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }).start(); // 开启线程
    }

    /**
     * 购物
     */
    public void shopping() {
        new Thread(new Runnable() {
            public void run() {
                String result = "";
                try {
                    String data = "{\"user\":\"" + Data.getauth0type() + "\",\"nums\":1,\"goodsid\":"+Data.getobjectId()+"}";
                    data = URLEncoder.encode(data, "UTF-8");
                    String urlName = Data.gethttp1()+"/hierstarQrCode/pawnshop/shopping?jsonParams="+data;
                    LogCook.d("发送参数", urlName);
                    URL U = new URL(urlName);
                    URLConnection connection = U.openConnection();
                    connection.connect();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result += line;
                    }
                    LogCook.d("购物返回数据", result);
                    in.close();
                    if(Data.gettype().equals("spxqactivity")&&Data.getpaytype().equals("card")) {
                        walletamount();
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status_Sucess").equals("true")) {
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp11), Toast.LENGTH_SHORT).show();
                        if(Data.getpaytype().equals("pt")){
                            WeiboDialogUtils.closeDialog(Data.getdialog());
                        }
                        Looper.loop();
                    } else if (jsonObject.getString("status_Sucess").equals("false")) {//返回错误信息
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp23) +
                                jsonObject.getString("status_Result") + ":" + jsonObject.getString("status_Message"), Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Looper.loop();
                    }
                } catch (Exception e) {
                    if(Data.gettype().equals("spxqactivity")) {
                        walletamount();
                    }
                    e.printStackTrace();
                    Looper.prepare();
                    WeiboDialogUtils.closeDialog(Data.getdialog());
                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp23), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }).start(); // 开启线程
    }

    /**
     * 获取xrp余额
     */
    public void getxrpamount() {
        new Thread(new Runnable() {
            public void run() {
                String result = "";Looper.prepare();
                try {
                    String data = "{\"account\":\""+Data.getxrpaddress()+"\"}";
                    data = URLEncoder.encode(data, "UTF-8");
                    String urlName = Data.gethttp1()+"/hsRPCNodeServer/xrp/getBalance?jsonParams="+data;
                    LogCook.d("发送参数", urlName);
                    URL U = new URL(urlName);
                    URLConnection connection = U.openConnection();
                    connection.setConnectTimeout(30000);
                    connection.connect();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result += line;
                    }
                    LogCook.d("获取xrp余额/交易序号返回数据", result);
                    in.close();
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status_Sucess").equals("true")) {
                        result=jsonObject.getString("status_Result");
                        int index = getIndex(result,1,"#");
                        String balance = result.substring(0,index);
                        balance = String.valueOf(Double.parseDouble(balance)/1000000);
                        String sequence = result.substring(index+1,result.length());
                        LogCook.d("瑞波币余额", balance);Data.setxrpamount(balance);
                        LogCook.d("瑞波币交易序号", sequence);Data.setxrpserialnumber(sequence);
                        if(Data.gettype().equals("fragment3")){
                            if (Data.getbizhong().equals("XRP")) {
                                new Transfer().xrpcreatetransaction();
                            }else if (Data.getbizhong().equals("AED")) {
                                new Transfer().aedcreatetransaction();
                            }
                        }else {
                            new Utils().send2();
                        }
                    } else if (jsonObject.getString("status_Sucess").equals("false")) {//返回错误信息
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp24) +
                                jsonObject.getString("status_Result") + ":" + jsonObject.getString("status_Message"), Toast.LENGTH_SHORT).show();
                        if(Data.gettype().equals("fragment3")) {
                            WeiboDialogUtils.closeDialog(Data.getdialog());
                        }else {
                            Data.setxrpamount("0");
                            new Utils().send2();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp24), Toast.LENGTH_SHORT).show();
                    if(Data.gettype().equals("fragment3")) {

                    }else {
                        Data.setxrpamount("0");
                        new Utils().send2();
                    }
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }).start();
    }

    /**
     * xrp交易
     */
    public void getxrpsendtransaction(String signdata) {
        new Thread(new Runnable() {
            public void run() {
                String result = "";
                try {
                    String data = "{\"tx_blob\":\""+signdata+"\"}";
                    data = URLEncoder.encode(data, "UTF-8");
                    String urlName = Data.gethttp1()+"/hsRPCNodeServer/xrp/submit?jsonParams="+data;
                    LogCook.d("发送参数", urlName);
                    URL U = new URL(urlName);
                    URLConnection connection = U.openConnection();
                    connection.connect();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result += line;
                    }
                    LogCook.d("xrp交易返回数据", result);
                    in.close();
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status_Result").equals("success")) {
                        Looper.prepare();
                        if(Data.getbizhong().equals("trustset")){
                            Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff42), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff32), Toast.LENGTH_SHORT).show();
                        }
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Looper.loop();
                    } else if (jsonObject.getString("status_Result").equals("error")) {//返回错误信息
                        Looper.prepare();
                        if(Data.getbizhong().equals("trustset")){
                            Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff43), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff30) +
                                    jsonObject.getString("status_Result") + ":" + jsonObject.getString("status_Message"), Toast.LENGTH_SHORT).show();
                        }
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Looper.loop();
                    }
                } catch (Exception e) {
                    Looper.prepare();
                    if(Data.getbizhong().equals("trustset")){
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff43), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff30), Toast.LENGTH_SHORT).show();
                    }
                    WeiboDialogUtils.closeDialog(Data.getdialog());
                    Looper.loop();
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
