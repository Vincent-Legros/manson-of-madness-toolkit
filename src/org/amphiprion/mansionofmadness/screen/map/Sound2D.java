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
import org.amphiprion.mansionofmadness.dto.Sound;
import org.amphiprion.mansionofmadness.dto.SoundInstance;

/**
 * @author Amphiprion
 * 
 */
public class Sound2D extends Image2D {
	private Sound sound;
	private SoundInstance soundInstance;
	private Text2D imgName;
	private MapScreen mapScreen;

	public Sound2D(MapScreen mapScreen, SoundInstance soundInstance, Sound sound) {
		super("sounds/sound.png");
		this.sound = sound;
		this.soundInstance = soundInstance;
		imgName = new Text2D(sound.getDisplayName(), 18);
		this.mapScreen = mapScreen;
	}

	/**
	 * @return the sound
	 */
	public Sound getSound() {
		return sound;
	}

	/**
	 * @return the soundInstance
	 */
	public SoundInstance getSoundInstance() {
		return soundInstance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.amphiprion.gameengine3d.mesh.Image2D#draw(javax.microedition.khronos
	 * .opengles.GL10, float, int, int)
	 */
	@Override
	public void draw(GL10 gl, float screenScale, int screenWidth, int screenHeight) {
		super.draw(gl, screenScale, screenWidth, screenHeight);
		if (mapScreen.showLabels) {
			imgName.x = x;
			imgName.y = y + 40;
			imgName.setGlobalScale(getGlobalScale());
			imgName.draw(gl, screenScale, screenWidth, screenHeight);
		}
	}
}
