/*
 * @copyright 2010 Gerald Jacobson
 * @license GNU General Public License
 * 
 * This file is part of MansionOfMadnessToolKit.
 *
 * MansionOfMadnessToolKit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MansionOfMadnessToolKit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MansionOfMadnessToolKit.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.amphiprion.mansionofmadness.screen.map;

import javax.microedition.khronos.opengles.GL10;

import org.amphiprion.gameengine3d.mesh.Image2D;
import org.amphiprion.gameengine3d.util.TextureUtil;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * @author Amphiprion
 * 
 */
public class Text2D extends Image2D {
	Paint textPaint;

	public Text2D(String text, int size) {
		this(text);
		textPaint = new Paint();
		textPaint.setFakeBoldText(true);
		textPaint.setAntiAlias(true);
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(size);
	}

	public Text2D(String text) {
		super("@String/" + text);
	}

	public void setText(String text) {
		changeUri("@String/" + text);
	}

	@Override
	protected void loadGLTexture(GL10 gl) { // New function
		if (textPaint == null) {
			texture = TextureUtil.loadTexture(uri, gl, 0);
		} else {
			texture = TextureUtil.loadTexture(uri, gl, 0, textPaint);
		}
	}

}
