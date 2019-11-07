package com.wallet.cold.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.wallet.cold.decode.DecodeFormatManager;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BitmapUtils {

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	public static Bitmap getCompressedBitmap(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		BitmapFactory.decodeFile(path, options);
		if (options.outWidth >= 300 || options.outHeight >= 300) {
			int size = Math.max(options.outWidth / 300, options.outHeight / 300);
			options.inSampleSize = size >= 1 ? size : 1;
		}
		options.inJustDecodeBounds = false;
		Bitmap dscBitmap = BitmapFactory.decodeFile(path, options);
		return dscBitmap;
	}
	/**
	 * 获取结果
	 *
	 * @param bitmap 需要解析的bitmap
	 * @return
	 */
	public static String getResult(Bitmap bitmap) {
		//Zxing自带的解析类
		byte data[];
		int[] datas = new int[bitmap.getWidth() * bitmap.getHeight()];
		data = new byte[bitmap.getWidth() * bitmap.getHeight()];
		bitmap.getPixels(datas, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
		for (int i = 0; i < datas.length; i++) {
			data[i] = (byte) datas[i];
		}
		Set<BarcodeFormat> decodeFormats = new HashSet<>();
		decodeFormats.addAll(DecodeFormatManager.PRODUCT_FORMATS);
		decodeFormats.addAll(DecodeFormatManager.INDUSTRIAL_FORMATS);
		decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
		decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
		decodeFormats.addAll(DecodeFormatManager.AZTEC_FORMATS);
		decodeFormats.addAll(DecodeFormatManager.PDF417_FORMATS);
		Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
		hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
		MultiFormatReader multiFormatReader = new MultiFormatReader();
		multiFormatReader.setHints(hints);
		Result rawResult = null;
		PlanarYUVLuminanceSource source = buildLuminanceSource(data, bitmap.getWidth(), bitmap.getHeight());
		if (source != null) {
			BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
			try {
				rawResult = multiFormatReader.decodeWithState(bitmap1);
			} catch (ReaderException re) {
				// continue
			} finally {
				multiFormatReader.reset();
			}
		}

		return rawResult != null ? rawResult.getText() : "";
	}
	public static PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
		Rect rect = new Rect(0, 0, width, height);
		// Go ahead and assume it's YUV rather than die.
		return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top,
				rect.width(), rect.height(), false);
	}
}
