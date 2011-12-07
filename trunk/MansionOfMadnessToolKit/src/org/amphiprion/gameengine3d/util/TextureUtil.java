package org.amphiprion.gameengine3d.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

import org.amphiprion.mansionofmadness.ApplicationConstants;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.opengl.GLUtils;
import android.os.Environment;

public class TextureUtil {
	private static Map<String, Texture> textures = new HashMap<String, Texture>();
	private static GL10 gl;
	private static Paint textPaint;
	static {
		textPaint = new Paint();
		textPaint.setFakeBoldText(true);
		textPaint.setAntiAlias(true);
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(16);
	}

	public static Texture loadTexture(String uri, GL10 gl) {
		return loadTexture(uri, gl, 0);
	}

	public static Texture loadTexture(String uri, GL10 gl, int angle) {
		return loadTexture(uri, gl, angle, 16);
	}

	public static Texture loadTexture(String uri, GL10 gl, int angle, int textSize) {
		if (TextureUtil.gl != null && TextureUtil.gl != gl) {
			unloadAll();
		}
		TextureUtil.gl = gl;

		Texture texture = textures.get(uri + "|" + angle + "|" + textSize);
		if (texture == null) {
			if (uri.startsWith("@String/")) {
				String str = uri.substring(8);
				int ascent = 0;
				int descent = 0;
				int measuredTextWidth = 0;

				textPaint.setTextSize(textSize);
				// Paint.ascent is negative, so negate it.
				ascent = (int) Math.ceil(-textPaint.ascent());
				descent = (int) Math.ceil(textPaint.descent());
				measuredTextWidth = (int) Math.ceil(textPaint.measureText(str));
				int contentWidth = measuredTextWidth;
				int contentHeight = (ascent + descent) * 2;

				int mStrikeWidth = 1;
				while (mStrikeWidth < contentWidth) {
					mStrikeWidth <<= 1;
				}

				int mStrikeHeight = 1;
				while (mStrikeHeight < contentHeight) {
					mStrikeHeight <<= 1;
				}
				Bitmap.Config config = Bitmap.Config.ARGB_4444;
				Bitmap mBitmap = Bitmap.createBitmap(mStrikeWidth, mStrikeHeight, config);
				Canvas mCanvas = new Canvas(mBitmap);
				mCanvas.drawText(str, 0, ascent + descent, textPaint);
				mCanvas = null;
				texture = loadGLTexture(mBitmap, gl);
				mBitmap.recycle();
				mBitmap = null;
				// } else {
				// texture = loadGLTexture(bitmap, gl);
				// }
				texture.height = mStrikeHeight;
				texture.width = mStrikeWidth;
				texture.originalHeight = contentHeight;
				texture.originalWidth = contentWidth;

				textures.put(uri + "|" + angle + "|" + textSize, texture);
			} else {
				InputStream is = null;
				if (uri.startsWith("file://")) {
					try {
						is = new FileInputStream(uri.substring(7));
					} catch (Exception e) {
					}
				} else {
					try {
						is = new FileInputStream(Environment.getExternalStorageDirectory() + "/" + ApplicationConstants.DIRECTORY_IMAGE + "/" + uri);
					} catch (Exception e) {
						is = TextureUtil.class.getResourceAsStream("/images/" + uri);
					}
				}
				if (is == null) {
					is = TextureUtil.class.getResourceAsStream("/images/tiles/icons/close.png");
				}

				// Log.d("OPENGL", "load texture:" + uri);
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				int contentWidth = bitmap.getWidth();
				int contentHeight = bitmap.getHeight();
				if (angle == 90 || angle == 270) {
					contentWidth = bitmap.getHeight();
					contentHeight = bitmap.getWidth();
				}

				int mStrikeWidth = 1;
				while (mStrikeWidth < contentWidth) {
					mStrikeWidth <<= 1;
				}

				int mStrikeHeight = 1;
				while (mStrikeHeight < contentHeight) {
					mStrikeHeight <<= 1;
				}
				// if (contentWidth != mStrikeWidth || contentHeight !=
				// mStrikeHeight) {
				Bitmap.Config config = Bitmap.Config.ARGB_4444;
				Bitmap mBitmap = Bitmap.createBitmap(mStrikeWidth, mStrikeHeight, config);
				Canvas mCanvas = new Canvas(mBitmap);

				if (angle != 0) {
					Matrix orig = mCanvas.getMatrix();
					orig.postTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2);
					orig.postRotate(angle, 0, 0);
					orig.postTranslate(contentWidth / 2, contentHeight / 2);
					mCanvas.setMatrix(orig);
				}
				mCanvas.drawBitmap(bitmap, 0, 0, new Paint());
				mCanvas = null;
				texture = loadGLTexture(mBitmap, gl);
				mBitmap.recycle();
				mBitmap = null;
				// } else {
				// texture = loadGLTexture(bitmap, gl);
				// }
				texture.height = mStrikeHeight;
				texture.width = mStrikeWidth;
				texture.originalHeight = contentHeight;
				texture.originalWidth = contentWidth;
				bitmap.recycle();
				bitmap = null;
				textures.put(uri + "|" + angle, texture);
			}
		}
		return texture;
	}

	private static Texture loadGLTexture(Bitmap bitmap, GL10 gl) { // New
																	// function
		Texture texture = new Texture();
		// Generate one texture pointer...
		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);
		texture.textureId = textures[0];
		// ...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.textureId);

		// Create Nearest Filtered Texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		// Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

		// Use the Android GLUtils to specify a two-dimensional texture image
		// from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		return texture;
	}

	public static void unloadAll() {
		if (TextureUtil.gl != null) {
			int[] indexs = new int[1];
			for (Texture t : textures.values()) {
				indexs[0] = t.textureId;
				gl.glDeleteTextures(1, indexs, 0);
			}
		}
		textures.clear();
	}
}
