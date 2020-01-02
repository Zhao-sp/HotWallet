package com.wallet.cold.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * 作用： Android日志工具类  （支持打印重要日志，报错信息，到指定文件）
 */

public class LogCook implements Thread.UncaughtExceptionHandler {

    private static boolean isOpen; //是否打印日志
    private static boolean isSave; //是否保存日志到文件
    private static String logPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/com.hotwallet/log";//log日志存放路径
    private static String errorlogPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/com.hotwallet/errorlog";//log日志存放路径
    private static String logName;//日志文件名

    private static LogCook instance;

    public static LogCook getInstance() {
        if (instance == null) {
            instance = new LogCook();
        }
        return instance;
    }

    /**
     * 初始化异常奔溃信息监听
     */
    public void initialize() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 是否开启输出日志功能
     * @param isOpen  isOpen
     * @return LogCook
     */
    public LogCook isOpen(boolean isOpen) {
        LogCook.isOpen = isOpen;
        return this;
    }
    /**
     * 是否保存日志到文件
     * @param isSave  isSave
     * @return LogCook
     */
    public LogCook isSave(boolean isSave) {
        LogCook.isSave = isSave;
        return this;
    }

    /**
     * 设置保存log的文件路径
     * @param logPath 路径
     * @return  LogCook
     */
    public LogCook setLogPath(String logPath) {
        LogCook.logPath = logPath;
        return this;
    }

    /**
     * 设置保存log的文件名称
     * @param logName 文件名称  如：xxxx_xx_xx.log
     * @return  LogCook
     */
    public LogCook setLogName(String logName) {
        LogCook.logName = logName;
        return this;
    }

    public static void v(String tag, String msg) {
        if (isOpen)
            Log.v(tag, msg);
        if (isSave)
            writeToFile(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isOpen)
            Log.d(tag, msg);
        if (isSave)
            writeToFile(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (isOpen)
            Log.i(tag, msg);
        if (isSave)
            writeToFile(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (isOpen)
            Log.w(tag, msg);
        if (isSave)
            writeToFile(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isOpen)
            Log.e(tag, msg);
        if (isSave)
            writeToFile(tag, msg);
    }

    public static void log(String log) {
        if (isOpen)
            Log.i("", log);
        if (isSave)
            writeToFile(log);
    }

    public static void f(String tag, String msg) {
        if (isSave) {
            writeToFile(tag, msg);
        }
    }


    /**
     * 将log信息写入文件中
     *
     * @param tag TAG
     * @param msg MSG
     */
    private static void writeToFile(String tag, String msg) {
        String info = tag + " " + msg;
        writeToFile(info);
    }

    /**
     * 将log信息写入文件中
     *
     * @param info info
     */
    private static void writeToFile(String info) {
        if (null == logPath) {
            return;
        }

        String log = getNowTime() + ":" + info + "\n";//log日志内容，可以自行定制
        //如果父路径不存在
        File file = new File(logPath);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();//创建父路径
            if (!mkdirs) {
                return;
            }
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(logPath + File.separator
                    + logName, true);
            fw.write(log);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fw != null) {
                    fw.close();//关闭缓冲流
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间
     */
    private static String getNowTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 获取当前日期
     *
     * @return 当前日期
     */
    public static String getNowDay() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (logPath != null) {
            File file = new File(logPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            try {
                String logName = "AppError" + getNowDay() + ".log";
                FileWriter fw = new FileWriter(errorlogPath + File.separator
                        + logName, true);
                String model = android.os.Build.MODEL; //型号
                String brand = android.os.Build.BRAND; //品牌
                String release = android.os.Build.VERSION.RELEASE; //版本
                fw.write("设备型号：" + model + "\n");
                fw.write("设备品牌：" + brand + "\n");
                fw.write("设备系统版本：" + release + "\n");
                fw.write(getNowTime() + "错误原因：");
                // 错误信息
                StackTraceElement[] stackTrace = throwable.getStackTrace();
                fw.write(throwable.getMessage() + "\n");
                for (StackTraceElement aStackTrace : stackTrace) {
                    fw.write(aStackTrace.getFileName() + " CLASS:"
                            + aStackTrace.getClassName() + " METHOD:"
                            + aStackTrace.getMethodName() + " LINE:"
                            + aStackTrace.getLineNumber() + "\n");
                }
                fw.write("\n");
                fw.close();
                // 上传错误信息到服务器
                // uploadToServer();
            } catch (IOException e) {
                Log.e("crash handler", "执行保存App运行报错信息加载文件失败...", e.getCause());
            }
        }
        throwable.printStackTrace();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static boolean DeleteOverdueLogFile() {
        File mfolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.hotwallet/log"); //打开目录文件夹
        File errormfolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.hotwallet/errorlog");
        if (mfolder.isDirectory()) {
            File[] AllFiles = mfolder.listFiles(); //列出目录下的所有文件
            ArrayList<String> mFilesList = new ArrayList<String>();
            for (int i = 0; i < AllFiles.length; i++) {
                File mFile = AllFiles[i]; //得到文件
                String Name = mFile.getName(); //得到文件的名字
                if (Name == null || Name.length() < 1)
                    return false;
                if (Name.endsWith(".txt")) {  //筛选出log
                    mFilesList.add(Name); //把文件名添加到链表里
                }
            }
            Collections.sort(mFilesList);   // 将文件按自然排序升序排列
            //判断日志文件如果大于5，就要处理
            for (int i = 0; i < mFilesList.size() - 4; i++) {
                String Name = mFilesList.get(i); //得到链表最早的文件名
                File mFile = new File(mfolder, Name);  //得到最早的文件
                mFile.delete(); //删除
            }
        }
        if (errormfolder.isDirectory()) {
            File[] AllFiles = mfolder.listFiles(); //列出目录下的所有文件
            ArrayList<String> errormFilesList = new ArrayList<String>();
            for (int i = 0; i < AllFiles.length; i++) {
                File mFile = AllFiles[i]; //得到文件
                String Name = mFile.getName(); //得到文件的名字
                if (Name == null || Name.length() < 1)
                    return false;
                if (Name.endsWith(".log")) {
                    errormFilesList.add(Name);
                }
            }
            Collections.sort(errormFilesList);
            for (int i = 0; i < errormFilesList.size() - 4; i++) {
                String Name = errormFilesList.get(i); //得到链表最早的文件名
                File mFile = new File(mfolder, Name);  //得到最早的文件
                mFile.delete(); //删除
            }
        }
        return true;
    }
}
