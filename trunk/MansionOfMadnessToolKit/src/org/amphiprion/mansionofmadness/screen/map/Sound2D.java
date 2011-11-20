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

import org.amphiprion.gameengine3d.mesh.Image2D;
import org.amphiprion.mansionofmadness.dto.Sound;

/**
 * @author Amphiprion
 * 
 */
public class Sound2D extends Image2D {
	private Sound sound;

	public Sound2D(Sound sound) {
		super("sounds/sound.png");
		this.sound = sound;
	}

	/**
	 * @return the sound
	 */
	public Sound getSound() {
		return sound;
	}

}
