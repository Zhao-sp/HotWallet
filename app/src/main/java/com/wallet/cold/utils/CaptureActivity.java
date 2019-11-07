package com.wallet.cold.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.zxing.Result;
import com.wallet.R;
import com.wallet.cold.app.index.Linkman;
import com.wallet.cold.app.index.Transfer;
import com.wallet.cold.app.auth0.auth0login;
import com.wallet.cold.app.auth0.auth0register;
import com.wallet.cold.app.main.IndexActivity;
import com.wallet.cold.camera.CameraManager;
import com.wallet.cold.decode.DecodeThread;
import com.wallet.hot.app.HotTransfer;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

import static com.wallet.cold.utils.Utils.getIndex;

public final class CaptureActivity extends Activity implements SurfaceHolder.Callback,OnClickListener {
	private static final String TAG = CaptureActivity.class.getSimpleName();
	private static final int REQUEST_CODE = 100;
	private static final int PARSE_BARCODE_FAIL = 300;
	private static final int PARSE_BARCODE_SUC = 200;
	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	private InactivityTimer inactivityTimer;
	private BeepManager beepManager;
	private SurfaceView scanPreview = null;
	private RelativeLayout scanContainer;
	private RelativeLayout scanCropView;
	private ImageView scanLine;
	private Rect mCropRect = null;
	private boolean isFlashlightOpen;
	/**
	 * 图片的路径
	 */
	private String photoPath;
	private Handler mHandler = new MyHandler(this);
	static class MyHandler extends Handler {
		private WeakReference<Activity> activityReference;
		public MyHandler(Activity activity) {
			activityReference = new WeakReference<Activity>(activity);
		}
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case PARSE_BARCODE_SUC: // 解析图片成功
					Toast.makeText(activityReference.get(),
							Data.getcontext().getResources().getString(R.string.c1) + msg.obj, Toast.LENGTH_SHORT).show();
					break;
				case PARSE_BARCODE_FAIL:// 解析图片失败
					Toast.makeText(activityReference.get(), Data.getcontext().getResources().getString(R.string.c2),
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
			super.handleMessage(msg);
		}
	}
	public Handler getHandler() {
		return handler;
	}
	public CameraManager getCameraManager() {
		return cameraManager;
	}
	private boolean isHasSurface = false;
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_capture);
		scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
		scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
		scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
		scanLine = (ImageView) findViewById(R.id.capture_scan_line);
		inactivityTimer = new InactivityTimer(this);
		beepManager = new BeepManager(this);
		TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
				0.9f);
		animation.setDuration(4500);
		animation.setRepeatCount(-1);
		animation.setRepeatMode(Animation.RESTART);
		scanLine.startAnimation(animation);
		findViewById(R.id.capture_flashlight).setOnClickListener(this);
		findViewById(R.id.capture_scan_photo).setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		cameraManager = new CameraManager(getApplication());

		handler = null;

		if (isHasSurface) {
			initCamera(scanPreview.getHolder());
		} else {
			scanPreview.getHolder().addCallback(this);
		}

		inactivityTimer.onResume();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		beepManager.close();
		cameraManager.closeDriver();
		if (!isHasSurface) {
			scanPreview.getHolder().removeCallback(this);
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!isHasSurface) {
			isHasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isHasSurface = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	public void handleDecode(Result rawResult, Bundle bundle) {
		inactivityTimer.onActivity();
		beepManager.playBeepSoundAndVibrate();

		bundle.putInt("width", mCropRect.width());
		bundle.putInt("height", mCropRect.height());
		bundle.putString("result", rawResult.getText());
		if(Data.gettype().equals("hottransfer")|| Data.gettype().equals("hotindex")){
			startActivity(new Intent(CaptureActivity.this, HotTransfer.class).putExtras(bundle));
		}else if(Data.gettype().equals("Linkman")){
			startActivity(new Intent(CaptureActivity.this, Linkman.class).putExtras(bundle));
		}else {
			if (rawResult.getText().contains("//www.hierstar.com?type=")) {
				int index = getIndex(rawResult.getText(), 1, "=");
				int index1 = getIndex(rawResult.getText(), 2, "=");
				Data.setauth0uuid(rawResult.getText().substring(index1 + 1, rawResult.getText().length()));
				String type = rawResult.getText().substring(index + 1, index + 2);
				Cursor cursor = Data.getdb().rawQuery("select name from Auth0AddressTb where blename='"+ Data.getdevicename()+"'and address ='"+ Data.getethaddress()+"'", null);
				if (cursor != null && cursor.getCount() > 0) {//本地存在用户名 跳转登录
					while (cursor.moveToNext()) {//游标是否继续向下移动
						String name = cursor.getString(cursor.getColumnIndex("name"));
						Data.setusername(name);
						Data.setauth0type(name);
						cursor.close();
					}
					if(type.equals("0")) {
						Toast.makeText(Data.getcontext(), "本用户已注册，无需再注册", Toast.LENGTH_SHORT).show();
						bundle.putString("type", "1");
						startActivity(new Intent(CaptureActivity.this, IndexActivity.class).putExtras(bundle));
					}else if(type.equals("1")){
						startActivity(new Intent(CaptureActivity.this, auth0login.class).putExtras(bundle));
					}
				}else if (type.equals("0")) {//本地不存在用户名 进行注册
					startActivity(new Intent(CaptureActivity.this, auth0register.class).putExtras(bundle));
				} else if (type.equals("1")) {
					if (Data.getusername().equals("")) {
						bundle.putString("type", "0");
						startActivity(new Intent(CaptureActivity.this, IndexActivity.class).putExtras(bundle));
					} else {
						startActivity(new Intent(CaptureActivity.this, auth0login.class).putExtras(bundle));
					}
				}
			} else {
				startActivity(new Intent(CaptureActivity.this, Transfer.class).putExtras(bundle));
			}
		}
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
			if (handler == null) {
				handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
			}

			initCrop();
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			Log.w(TAG, "Unexpected error initializing camera", e);
			displayFrameworkBugMessageAndExit();
		}
	}

	private void displayFrameworkBugMessageAndExit() {
		// camera error
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage(R.string.c3);
		builder.setPositiveButton(R.string.f514, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}

		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				finish();
			}
		});
		builder.show();
	}

	public Rect getCropRect() {
		return mCropRect;
	}

	/**
	 * 初始化截取的矩形区域
	 */
	private void initCrop() {
		int cameraWidth = cameraManager.getCameraResolution().y;
		int cameraHeight = cameraManager.getCameraResolution().x;

		/** 获取布局中扫描框的位置信息 */
		int[] location = new int[2];
		scanCropView.getLocationInWindow(location);

		int cropLeft = location[0];
		int cropTop = location[1] - getStatusBarHeight();

		int cropWidth = scanCropView.getWidth();
		int cropHeight = scanCropView.getHeight();

		/** 获取布局容器的宽高 */
		int containerWidth = scanContainer.getWidth();
		int containerHeight = scanContainer.getHeight();

		/** 计算最终截取的矩形的左上角顶点x坐标 */
		int x = cropLeft * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的左上角顶点y坐标 */
		int y = cropTop * cameraHeight / containerHeight;

		/** 计算最终截取的矩形的宽度 */
		int width = cropWidth * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的高度 */
		int height = cropHeight * cameraHeight / containerHeight;

		/** 生成最终的截取的矩形 */
		mCropRect = new Rect(x, y, width + x, height + y);
	}

	private int getStatusBarHeight() {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, final Intent intent) {

		if (resultCode == RESULT_OK) {
			final ProgressDialog progressDialog;
			switch (requestCode) {
				case REQUEST_CODE:
					// 获取选中图片的路径
					Cursor cursor = getContentResolver().query(intent.getData(), null, null, null, null);
					if (cursor.moveToFirst()) {
						photoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
					}
					cursor.close();
					progressDialog = new ProgressDialog(this);
					progressDialog.setMessage(Data.getcontext().getResources().getString(R.string.c4));
					progressDialog.setCancelable(false);
					progressDialog.show();
					new Thread(new Runnable() {
						@Override
						public void run() {
							Bitmap img = BitmapUtils.getCompressedBitmap(photoPath);
							String result =TextUtils.isEmpty(BitmapUtils.getResult(img)) ? Data.getcontext().getResources().getString(R.string.c5): BitmapUtils.getResult(img);
							Bundle bundle = new Bundle();
							bundle.putString("result",result);
							startActivity(new Intent(CaptureActivity.this, Transfer.class).putExtras(bundle));
							progressDialog.dismiss();
						}
					}).start();
					break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		// 图片识别
		if (id == R.id.capture_scan_photo) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_PICK);
			intent.setType("image/*");
			startActivityForResult(intent, REQUEST_CODE);
		} else if (id == R.id.capture_flashlight) {
			if (isFlashlightOpen) {
				cameraManager.setTorch(false); // 关闭闪光灯
				isFlashlightOpen = false;
			} else {
				cameraManager.setTorch(true); // 打开闪光灯
				isFlashlightOpen = true;
			}
		} else {
		}
	}
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(LocalManageUtil.setLocal(newBase));
	}
}
