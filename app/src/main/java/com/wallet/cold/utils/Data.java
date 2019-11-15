package com.wallet.cold.utils;

import android.app.Application;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;

/**
 * 全局变量文件
 */
public class Data extends Application {
    private static UartService mService;
    private static Bitmap imgCode,ethimgCode,shareimgCode,xrpimgCode;
    private static List<String> txid,rlplist,scriptPubKey,amount,signdata,bledata,zxname,zxtime,zxneirong;
    private static List<Object> vout;
    private static Context context;
    private static Dialog dialog;
    private static Button startBtn;
    private static Handler handler;
    private static int n,count,button,restart,signcount1,percent,page;
    private static boolean uxto,btcsign,isdfu,islanguages,dfuupdate,isshangla;
    private static TextView balance,cardmoney,countamount,pttext,hiertext,btctext,btcrmbtext,ethtext,ethrmbtext,xrptext,xrprmbtext,aedtext,aedrmbtext;
    private static SQLiteDatabase db;
    private static BluetoothAdapter BluetoothAdapter;
    private static String limit,isapp,ethtype,returnbledata,btcsigndata,username,password,plaint,auth0type,resulterror,btcrmbbalance,ethrmbbalance,hieramount,amountrmb,
            result,btcaddress,ethaddress,deviceaddress,devicename,path,blename, receivedata,btcsignerror,txamount,auth0pubkey,auth0address,auth0sign,auth0uuid,hotzjc,
            btcbalance,ethbalance,type,btctype,pubkey,sign,end,hash,bizhong,scan,rlpdata,bletype,scriptPubKey1,resultdata,strhex1,strhex2,iseth,data,data2,saoma,yue,fee,to,
            hotethaddress,hotbtcaddress,apptype,hotethprv,hotbtcprv,hotbtcpub,hotpassword,fingerprints,fingerprintsname,http1,hiersign,objectId,ptamount,isblecomment,paytype,xrppub,
            xrpaddress,xrpamount,xrpserialnumber,xrprmbbalance;
    public static Handler gethandler() {
        return handler;
    }
    public static void sethandler(Handler handler) {
        Data.handler = handler;
    }
    public static Button getstartBtn() {
        return startBtn;
    }
    public static void setstartBtn(Button startBtn) {
        Data.startBtn = startBtn;
    }
    public static SQLiteDatabase getdb() {
        return db;
    }
    public static void setdb(SQLiteDatabase db) {
        Data.db = db;
    }
    public static BluetoothAdapter getBluetoothAdapter() {
        return BluetoothAdapter;
    }
    public static void setBluetoothAdapter(BluetoothAdapter BluetoothAdapter) {
        Data.BluetoothAdapter = BluetoothAdapter;
    }
    public static TextView getcardmoney() {
        return cardmoney;
    }//卡内余额
    public static void setcardmoney(TextView cardmoney) {
        Data.cardmoney = cardmoney;
    }
    public static TextView getbtctext() {
        return btctext;
    }
    public static void setbtctext(TextView btctext) {
        Data.btctext = btctext;
    }public static TextView getbtcrmbtext() {
        return btcrmbtext;
    }
    public static void setbtcrmbtext(TextView btcrmbtext) {
        Data.btcrmbtext = btcrmbtext;
    }public static TextView getethtext() {
        return ethtext;
    }
    public static void setethtext(TextView ethtext) {
        Data.ethtext = ethtext;
    }public static TextView getethrmbtext() {
        return ethrmbtext;
    }
    public static void setethrmbtext(TextView ethrmbtext) {
        Data.ethrmbtext = ethrmbtext;
    }public static TextView getxrptext() {
        return xrptext;
    }
    public static void setxrptext(TextView xrptext) {
        Data.xrptext = xrptext;
    }public static TextView getxrprmbtext() {
        return xrprmbtext;
    }
    public static void setxrprmbtext(TextView xrprmbtext) {
        Data.xrprmbtext = xrprmbtext;
    }
    public static String gethttp1() {
        return http1;
    }
    public static void sethttp1(String http1) {
        Data.http1 = http1;
    }
    public static TextView getcountamount() {
        return countamount;
    }//总金额
    public static void setcountamount(TextView countamount) {
        Data.countamount = countamount;
    }
    public static String getbtcrmbbalance() {
        return btcrmbbalance;
    }
    public static void setbtcrmbbalance(String btcrmbbalance) {//btc人民币余额
        Data.btcrmbbalance = btcrmbbalance;
    }
    public static String getethrmbbalance() {
        return ethrmbbalance;
    }
    public static void setethrmbbalance(String ethrmbbalance) {//eth人民币余额
        Data.ethrmbbalance = ethrmbbalance;
    }
    public static String getfingerprints() {
        return fingerprints;
    }
    public static void setfingerprints(String fingerprints) {//第几个指纹
        Data.fingerprints = fingerprints;
    }
    public static String getfingerprintsname() {//指纹名字
        return fingerprintsname;
    }
    public static void setfingerprintsname(String fingerprintsname) {
        Data.fingerprintsname = fingerprintsname;
    }
    public static String getresulterror() {//蓝牙返回错误与否
        return resulterror;
    }
    public static void setresulterror(String resulterror) {
        Data.resulterror = resulterror;
    }
    public static String getplaint() {
        return plaint;
    }//挑战符
    public static void setplaint(String plaint) {
        Data.plaint = plaint;
    }
    public static String getauth0type() {//auth0登录状态
        return auth0type;
    }
    public static void setauth0type(String auth0type) {
        Data.auth0type = auth0type;
    }
    public static String getusername() {
        return username;
    }//auth0注册用户名
    public static void setusername(String username) {
        Data.username = username;
    }
    public static String getpassword() {
        return password;
    }//auth0注册密码
    public static void setpassword(String password) {
        Data.password = password;
    }
    public static String getauth0uuid() {
        return auth0uuid;
    }
    public static void setauth0uuid(String auth0uuid) {
        Data.auth0uuid = auth0uuid;
    }
    public static String gethotethaddress() {
        return hotethaddress;
    }
    public static void sethotethaddress(String hotethaddress) {
        Data.hotethaddress = hotethaddress;
    }
    public static String gethotbtcaddress() {
        return hotbtcaddress;
    }
    public static void sethotbtcaddress(String hotbtcaddress) {
        Data.hotbtcaddress = hotbtcaddress;
    }
    public static String gethotethprv() {
        return hotethprv;
    }
    public static void sethotethprv(String hotethprv) {
        Data.hotethprv = hotethprv;
    }
    public static String gethotpassword() {
        return hotpassword;
    }
    public static void sethotpassword(String hotpassword) {
        Data.hotpassword = hotpassword;
    }
    public static String gethotbtcprv() {
        return hotbtcprv;
    }
    public static void sethotbtcprv(String hotbtcprv) {
        Data.hotbtcprv = hotbtcprv;
    }
    public static String gethotbtcpub() {
        return hotbtcpub;
    }
    public static void sethotbtcpub(String hotbtcpub) {
        Data.hotbtcpub = hotbtcpub;
    }
    public static String getapptype() {
        return apptype;
    }
    public static void setapptype(String apptype) {
        Data.apptype = apptype;
    }
    public static String getpath() {
        return path;
    }
    public static void setpath(String path) {
        Data.path = path;
    }
    public static String getauth0pubkey() {
        return auth0pubkey;
    }
    public static void setauth0pubkey(String auth0pubkey) {
        Data.auth0pubkey = auth0pubkey;
    }
    public static String getauth0address() {
        return auth0address;
    }
    public static void setauth0address(String auth0address) {
        Data.auth0address = auth0address;
    }
    public static String gethotzjc() {
        return hotzjc;
    }
    public static void sethotzjc(String hotzjc) {
        Data.hotzjc = hotzjc;
    }
    public static int getpage() {
        return page;
    }
    public static void setpage(int page) {
        Data.page = page;
    }
    public static String getisapp() {
        return isapp;
    }
    public static void setisapp(String isapp) {
        Data.isapp = isapp;
    }
    public static String getauth0sign() {
        return auth0sign;
    }
    public static void setauth0sign(String auth0sign) {
        Data.auth0sign = auth0sign;
    }
    public static TextView getbalance() {
        return balance;
    }
    public static void setbalance(TextView balance) {
        Data.balance = balance;
    }
    public static Dialog getdialog() {
        return dialog;
    }
    public static void setdialog(Dialog dialog) {
        Data.dialog = dialog;
    }
    public static UartService getmService() {
        return mService;
    }
    public static void setmService(UartService mService) {
        Data.mService = mService;
    }
    public static Context getcontext() {
        return context;
    }
    public static void setcontext(Context context) {
        Data.context = context;
    }
    public static String getto() {
        return to;
    }
    public static void setto(String to) {
        Data.to = to;
    }
    public static String getblename() {
        return blename;
    }
    public static void setblename(String blename) {
        Data.blename = blename;
    }
    public static String getbtcsignerror() {
        return btcsignerror;
    }
    public static void setbtcsignerror(String btcsignerror) {
        Data.btcsignerror = btcsignerror;
    }
    public static String getyue() {
        return yue;
    }
    public static void setyue(String yue) {
        Data.yue = yue;
    }
    public static String getfee() {
        return fee;
    }
    public static void setfee(String fee) {
        Data.fee = fee;
    }
    public static String getsaoma() {
        return saoma;
    }
    public static void setsaoma(String saoma) {
        Data.saoma = saoma;
    }
    public static String getdata2() {
        return data2;
    }
    public static void setdata2(String data2) {
        Data.data2 = data2;
    }
    public static String getdata() {
        return data;
    }
    public static void setdata(String data) {
        Data.data = data;
    }
    public static int getrestart() {
        return restart;
    }
    public static void setrestart(int restart) {
        Data.restart = restart;
    }
    public static int getsigncount1() {
        return signcount1;
    }
    public static void setsigncount1(int signcount1) {
        Data.signcount1 = signcount1;
    }
    public static int getpercent() {
        return percent;
    }
    public static void setpercent(int percent) {
        Data.percent = percent;
    }
    public static boolean getuxto() {
        return uxto;
    }
    public static void setuxto(boolean uxto) {
        Data.uxto = uxto;
    }
    public static String getreturnbledata() {
        return returnbledata;
    }
    public static void setreturnbledata(String returnbledata) {
        Data.returnbledata = returnbledata;
    }
    public static boolean getislanguages() {
        return islanguages;
    }
    public static void setislanguages(boolean islanguages) {
        Data.islanguages = islanguages;
    }
    public static String getisblecomment() {//蓝牙连接状态
        return isblecomment;
    }
    public static void setisblecomment(String isblecomment) {
        Data.isblecomment = isblecomment;
    }
    public static boolean getdfuupdate() {
        return dfuupdate;
    }
    public static void setdfuupdate(boolean dfuupdate) {
        Data.dfuupdate = dfuupdate;
    }
    public static boolean getisdfu() {
        return isdfu;
    }
    public static void setisdfu(boolean isdfu) {
        Data.isdfu = isdfu;
    }
    public static boolean getbtcsign() {
        return btcsign;
    }
    public static void setbtcsign(boolean btcsign) {
        Data.btcsign = btcsign;
    }
    public static boolean getisshangla() {
        return isshangla;
    }
    public static void setisshangla(boolean isshangla) {
        Data.isshangla = isshangla;
    }
    public static String getiseth() {
        return iseth;
    }
    public static void setiseth(String iseth) {
        Data.iseth = iseth;
    }
    public static String getstrhex2() {
        return strhex2;
    }
    public static void setstrhex2(String strhex2) {
        Data.strhex2 = strhex2;
    }
    public static String getstrhex1() {
        return strhex1;
    }
    public static void setstrhex1(String strhex1) {
        Data.strhex1 = strhex1;
    }
    public static String getresultdata() {
        return resultdata;
    }
    public static void setresultdata(String resultdata) {
        Data.resultdata = resultdata;
    }
    public static String getscriptPubKey1() {
        return scriptPubKey1;
    }
    public static void setscriptPubKey1(String scriptPubKey1) {
        Data.scriptPubKey1 = scriptPubKey1;
    }
    public static String getbletype() {
        return bletype;
    }
    public static void setbletype(String bletype) {
        Data.bletype = bletype;
    }
    public static String getrlpdata() {
        return rlpdata;
    }
    public static void setrlpdata(String rlpdata) {
        Data.rlpdata = rlpdata;
    }
    public static String getresult() {
        return result;
    }
    public static void setresult(String result) {
        Data.result = result;
    }
    public static String getbtctype() {
        return btctype;
    }
    public static void setbtctype(String btctype) {
        Data.btctype = btctype;
    }
    public static Bitmap getethimgCode() {
        return ethimgCode;
    }
    public static void setethimgCode(Bitmap ethimgCode) {
        Data.ethimgCode = ethimgCode;
    }
    public static Bitmap getshareimgCode() {
        return shareimgCode;
    }
    public static void setshareimgCode(Bitmap shareimgCode) {
        Data.shareimgCode = shareimgCode;
    }
    public static Bitmap getxrpimgCode() {
        return xrpimgCode;
    }
    public static void setxrpimgCode(Bitmap xrpimgCode) {
        Data.xrpimgCode = xrpimgCode;
    }
    public static String getsign() {
        return sign;
    }
    public static void setsign(String sign) {
        Data.sign = sign;
    }
    public static String getbizhong() {
        return bizhong;
    }
    public static void setbizhong(String bizhong) {
        Data.bizhong = bizhong;
    }
    public static String getpubkey() {
        return pubkey;
    }
    public static void setpubkey(String pubkey) {
        Data.pubkey = pubkey;
    }
    public static String getethbalance() {
        return ethbalance;
    }
    public static void setethbalance(String ethbalance) {
        Data.ethbalance = ethbalance;
    }
    public static String getbtcbalance() {
        return btcbalance;
    }
    public static void setbtcbalance(String btcbalance) {
        Data.btcbalance = btcbalance;
    }
    public static String getbtcaddress() {
        return btcaddress;
    }
    public static void setbtcaddress(String btcaddress) {
        Data.btcaddress = btcaddress;
    }
    public static String getethaddress() {
        return ethaddress;
    }
    public static void setethaddress(String ethaddress) {
        Data.ethaddress = ethaddress;
    }
    public static Bitmap getimgCode() {
        return imgCode;
    }
    public static void setimgCode(Bitmap imgCode) {
        Data.imgCode = imgCode;
    }
    public static String gettype() {
        return type;
    }
    public static void settype(String type) {
        Data.type = type;
    }
    public static int getbutton() {
        return button;
    }
    public static void setbutton(int button) {
        Data.button = button;
    }
    public static String getDeviceaddress(){
        return deviceaddress;
    }
    public static void setDeviceaddress(String deviceaddress){
        Data.deviceaddress= deviceaddress;
    }
    public static List<String> getbledata() {
        return bledata;
    }
    public static void setbledata(List<String> bledata) {//支持币种
        Data.bledata = bledata;
    }
    public static String getbtcsigndata() {
        return btcsigndata;
    }
    public static void setbtcsigndata(String btcsigndata) {//支持币种
        Data.btcsigndata = btcsigndata;
    }
    public static List<String> getsigndata() {
        return signdata;
    }
    public static void setsigndata(List<String> signdata) {
        Data.signdata = signdata;
    }
    public static List<String> gettxid() {
        return txid;
    }
    public static void settxid(List<String> txid) {
        Data.txid = txid;
    }
    public static List<Object> getvout() {
        return vout;
    }
    public static void setvout(List<Object> vout) {
        Data.vout = vout;
    }
    public static List<String> getrlplist() {
        return rlplist;
    }
    public static void setrlplist(List<String> rlplist) {
        Data.rlplist = rlplist;
    }
    public static List<String> getzxname() {
        return zxname;
    }
    public static void setzxname(List<String> zxname) {
        Data.zxname = zxname;
    }
    public static List<String> getzxtime() {
        return zxtime;
    }
    public static void setzxtime(List<String> zxtime) {
        Data.zxtime = zxtime;
    }
    public static List<String> getzxneirong() {
        return zxneirong;
    }
    public static void setzxneirong(List<String> zxneirong) {
        Data.zxneirong = zxneirong;
    }
    public static List<String> getamount() {
        return amount;
    }
    public static void setamount(List<String> amount) {
        Data.amount = amount;
    }
    public static String gethash() {
        return hash;
    }
    public static void sethash(String hash) {
        Data.hash = hash;
    }
    public static String getend() {
        return end;
    }
    public static void setend(String end) {
        Data.end = end;
    }
    public static String getscan() {
        return scan;
    }
    public static void setscan(String scan) {
        Data.scan = scan;
    }
    public static String getethtype() {
        return ethtype;
    }
    public static void setethtype(String ethtype) {
        Data.ethtype = ethtype;
    }
    public static int getn() {
        return n;
    }
    public static void setn(int n) {
        Data.n = n;
    }
    public static int getcount() {
        return count;
    }
    public static void setcount(int count) {
        Data.count = count;
    }
    public static List<String> getscriptPubKey() {
        return scriptPubKey;
    }
    public static void setscriptPubKey(List<String> scriptPubKey) {
        Data.scriptPubKey = scriptPubKey;
    }
    public static String getlimit() {
        return limit;
    }//文件路径
    public static void setlimit(String limit) {
        Data.limit = limit;
    }
    public static String getdevicename() {
        return devicename;
    }
    public static void setdevicename(String devicename) {
        Data.devicename = devicename;
    }
    public static String getreceivedata() {//蓝牙返回数据接收
        return receivedata;
    }
    public static void setreceivedata(String receivedata) {
        Data.receivedata = receivedata;
    }
    public static String gethieramount() {
        return hieramount;
    }//海博币金额
    public static void sethieramount(String hieramount) {
        Data.hieramount = hieramount;
    }
    public static String getamountrmb() {
        return amountrmb;
    }//总金额
    public static void setamountrmb(String amountrmb) {
        Data.amountrmb = amountrmb;
    }
    public static TextView getpttext() {
        return pttext;
    }//平台金额
    public static void setpttext(TextView pttext) {
        Data.pttext = pttext;
    }
    public static TextView gethiertext() {
        return hiertext;
    }//hier金额
    public static void sethiertext(TextView hiertext) {
        Data.hiertext = hiertext;
    }
    public static String gethiersign() {
        return hiersign;
    }//海博币充值待签名数据
    public static void sethiersign(String hiersign) {
        Data.hiersign = hiersign;
    }
    public static String gettxamount() {
        return txamount;
    }//海博币提现金额
    public static void settxamount(String txamount) {
        Data.txamount = txamount;
    }
    public static String getobjectId() {
        return objectId;
    }//典当物id
    public static void setobjectId(String objectId) {
        Data.objectId = objectId;
    }
    public static String getptamount() {
        return ptamount;
    }//平台余额
    public static void setptamount(String ptamount) {
        Data.ptamount = ptamount;
    }
    public static String getpaytype() {
        return paytype;
    }//支付方式
    public static void setpaytype(String paytype) {
        Data.paytype = paytype;
    }
    public static String getxrppub() {
        return xrppub;
    }//xrp公钥
    public static void setxrppub(String xrppub) {
        Data.xrppub = xrppub;
    }
    public static String getxrpaddress() {
        return xrpaddress;
    }//xrp地址
    public static void setxrpaddress(String xrpaddress) {
        Data.xrpaddress = xrpaddress;
    }
    public static String getxrpamount() {
        return xrpamount;
    }//xrp余额
    public static void setxrpamount(String xrpamount) {
        Data.xrpamount = xrpamount;
    }
    public static String getxrpserialnumber() {
        return xrpserialnumber;
    }//xrp交易序号
    public static void setxrpserialnumber(String xrpserialnumber) {
        Data.xrpserialnumber = xrpserialnumber;
    }
    public static String getxrprmbbalance() {
        return xrprmbbalance;
    }//xrp人民币
    public static void setxrprmbbalance(String xrprmbbalance) {
        Data.xrprmbbalance = xrprmbbalance;
    }
    public static TextView getaedtext() {
        return aedtext;
    }
    public static void setaedtext(TextView aedtext) {
        Data.aedtext = aedtext;
    }
    public static TextView getaedrmbtext() {
        return aedrmbtext;
    }
    public static void setaedrmbtext(TextView aedrmbtext) {
        Data.aedrmbtext = aedrmbtext;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        setDeviceaddress("");
        setislanguages(false);
        IWXAPI mWxApi = WXAPIFactory.createWXAPI(this, "wx5e85422500011114", true);
        mWxApi.registerApp("wx5e85422500011114");
    }
}
