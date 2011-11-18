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

import org.amphiprion.gameengine3d.Group2D;

/**
 * @author ng00124c
 * 
 */
public class TileMenu extends Group2D {
	public static int WIDTH = 200;
	public static int HEIGHT = 200;

	/**
	 * 
	 */
	public TileMenu() {
		setX(WIDTH / 2);
		setY(800 / 2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.amphiprion.gameengine3d.Group2D#setX(int)
	 */
	@Override
	public void setX(int x) {
		if (x < -WIDTH / 2) {
			super.setX(-WIDTH / 2);
		} else if (x > WIDTH / 2) {
			super.setX(WIDTH / 2);
		} else {
			super.setX(x);
		}
	}
}
