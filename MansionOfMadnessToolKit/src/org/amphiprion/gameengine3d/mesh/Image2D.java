package org.amphiprion.gameengine3d.mesh;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import org.amphiprion.gameengine3d.IHMIComponent;
import org.amphiprion.gameengine3d.IObject2D;
import org.amphiprion.gameengine3d.util.Texture;
import org.amphiprion.gameengine3d.util.TextureUtil;

public class Image2D implements IObject2D, IHMIComponent {
	private boolean enable = true;

	// The bitmap we want to load as a texture.
	private String uri; // New variable.
	private Texture texture;
	private int rotation;
	// Indicates if we need to load the texture.
	private boolean mShouldLoadTexture = false; // New variable.
	// Our texture id.
	public int x;
	public int y;
	private IObject2D parent;
	private float scale = 1.0f;
	private boolean lockedX;
	private boolean lockedY;
	private float globalScale = 1.0f;

	public Image2D(String uri) {
		this(uri, false, false);
	}

	public Image2D(String uri, boolean lockedX, boolean lockedY) {
		this.uri = uri;
		this.lockedX = lockedX;
		this.lockedY = lockedY;
		// this.contentWidth = contentWidth;
		// this.contentHeight = contentHeight;
		//
		// int mStrikeWidth = 1;
		// while (mStrikeWidth < contentWidth) {
		// mStrikeWidth <<= 1;
		// }
		//
		// int mStrikeHeight = 1;
		// while (mStrikeHeight < contentHeight) {
		// mStrikeHeight <<= 1;
		// }
		//
		// Bitmap.Config config = Bitmap.Config.ARGB_4444;
		// mBitmap = Bitmap.createBitmap(mStrikeWidth, mStrikeHeight, config);
		// Canvas mCanvas = new Canvas(mBitmap);
		// mCanvas.drawBitmap(bitmap, 0, 0, new Paint());
		// mCanvas = null;

		mShouldLoadTexture = true;
		// bitmap = null;
	}

	public void changeUri(String uri) {
		this.uri = uri;
		mShouldLoadTexture = true;
	}

	@Override
	public float getScale() {
		return scale;
	}

	@Override
	public void setScale(float scale) {
		this.scale = scale;
	}

	/**
	 * Loads the texture.
	 * 
	 * @param gl
	 */
	private void loadGLTexture(GL10 gl) { // New function
		texture = TextureUtil.loadTexture(uri, gl, rotation);
	}

	@Override
	public void draw(GL10 gl, float screenScale, int screenWidth, int screenHeight) {
		// Log.d("OPENGL", "w=" + screenWidth + "    h=" + screenHeight +
		// "  cc=" + contentHeight + "  scale=" + screenScale);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		// Set VERTEX buffer (see that we specify 2 coords per element
		// instead 3
		// because we have defined coords as 2 elements

		// short is 2 bytes, therefore we multiply the number if
		// vertices with 2.
		gl.glPushMatrix();
		// gl.glTranslatef(x * screenScale, y * screenScale, 0); // X and Y are
		// real pixel
		// coords

		// gl.glScalef(screenScale, screenScale, 1);
		if (mShouldLoadTexture) {
			loadGLTexture(gl);
			mShouldLoadTexture = false;
		}
		if (!enable) {
			// gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
			// GL10.GL_MODULATE);
			gl.glColor4f(0.4f, 0.4f, 0.4f, 1f);
		} else {
			gl.glColor4f(1f, 1f, 1f, 1f);
		}
		// gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		if (texture != null) {
			gl.glEnable(GL10.GL_TEXTURE_2D);
			// Enable the texture state
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

			// Point to our buffers
			// gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.textureId);
		}
		int[] crop = new int[4];
		crop[0] = 0;
		crop[1] = 0 + texture.originalHeight;
		crop[2] = texture.originalWidth;
		crop[3] = -texture.originalHeight;

		((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, crop, 0);
		// ((GL11Ext) gl).glDrawTexiOES(0, 0, 0, 212, 401);
		((GL11Ext) gl).glDrawTexiOES((int) ((x * globalScale - texture.originalWidth * globalScale * scale / 2.0f) * screenScale), (int) (screenHeight + (-texture.originalHeight
				* scale * globalScale / 2.0f - y * globalScale)
				* screenScale), 0, (int) (texture.originalWidth * screenScale * globalScale * scale), (int) (texture.originalHeight * screenScale * globalScale * scale));

		// gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, mNumOfIndices,
		// GL10.GL_UNSIGNED_SHORT, indexBuffer);

		gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
		gl.glColor4f(1f, 1f, 1f, 1f);

		gl.glPopMatrix();

	}

	@Override
	public void setParent(IObject2D parent) {
		this.parent = parent;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public void setX(int x) {
		if (lockedX) {
			return;
		}
		this.x = x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public void setY(int y) {
		if (lockedY) {
			return;
		}
		this.y = y;
	}

	@Override
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	@Override
	public boolean isEnable() {
		return enable;
	}

	@Override
	public boolean contains(int px, int py) {
		if (px < (x - texture.originalWidth * scale / 2) * globalScale) {
			return false;
		}
		if (px > (x + texture.originalWidth * scale / 2) * globalScale) {
			return false;
		}
		if (py < (y - texture.originalHeight * scale / 2) * globalScale) {
			return false;
		}
		if (py > (y + texture.originalHeight * scale / 2) * globalScale) {
			return false;
		}
		return true;
	}

	public Texture getTexture() {
		return texture;
	}

	/**
	 * @param globalScale
	 *            the globalScale to set
	 */
	@Override
	public void setGlobalScale(float globalScale) {
		this.globalScale = globalScale;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation % 360;

		mShouldLoadTexture = true;
	}

	/**
	 * @return the rotation
	 */
	public int getRotation() {
		return rotation;
	}
}
