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

import android.view.MotionEvent;

/**
 * @author Amphiprion
 * 
 */
public class TouchableGroup2D extends Group2D {
	public enum PointerState {
		NONE, ON_TILE_MENU_TAB, ON_TILE_MENU, ON_BOARD_TILE, ON_BOARD
	}

	/**
	 * Inform a MotionEvent occurs. Return (other than NONE to consume the event
	 * (so next components will not receive the event)
	 * 
	 * @param event
	 *            the motion event
	 * @param nx
	 *            the normalized horizontal pointer position
	 * @param ny
	 *            the normalized vertical pointer position
	 * @return other than NONE if this method have processed the event and
	 *         consumed it
	 */
	public PointerState onTouch(MotionEvent event, int nx, int ny, PointerState current) {
		return PointerState.NONE;
	}
}
