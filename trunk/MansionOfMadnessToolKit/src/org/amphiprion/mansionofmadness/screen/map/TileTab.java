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
import org.amphiprion.gameengine3d.IObject2D;
import org.amphiprion.gameengine3d.animation.Translation2DAnimation;
import org.amphiprion.gameengine3d.mesh.Image2D;

import android.view.MotionEvent;
import android.view.animation.BounceInterpolator;

/**
 * @author Amphiprion
 * 
 */
public class TileTab extends TouchableGroup2D {
	public static int WIDTH = 200;
	private int activeContentIndex;
	private Translation2DAnimation tileMenuTabAnimation;
	private MapScreen mapScreen;

	private int lastPointerX;
	private int lastPointerY;
	private int lastPointerDeltaX;
	private int lastPointerDeltaY;

	/**
	 * 
	 */
	public TileTab(MapScreen mapScreen) {
		this.mapScreen = mapScreen;
		setX(WIDTH / 2);
		setY(800 / 2);
		activeContentIndex = -1;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.amphiprion.gameengine3d.Group2D#addObject(org.amphiprion.gameengine3d
	 * .IObject2D)
	 */
	@Override
	public void addObject(IObject2D o) {
		throw new UnsupportedOperationException("You must use addContentTab instead of addObject method.");
	}

	public void addContentTab(Group2D content, Image2D tab) {
		super.addObject(tab);
		super.addObject(content);
		activeContentIndex++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.amphiprion.mansionofmadness.screen.map.TouchableGroup2D#onTouch(android
	 * .view.MotionEvent, int, int)
	 */
	@Override
	public PointerState onTouch(MotionEvent event, int nx, int ny, PointerState current) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			lastPointerX = nx;
			lastPointerY = ny;
			lastPointerDeltaX = 0;
			lastPointerDeltaY = 0;

			for (int i = 0; i < objects.size() / 2; i++) {
				Image2D imgTab = (Image2D) objects.get(i * 2);
				if (imgTab.contains(nx, ny)) {
					activeContentIndex = i;
					mapScreen.removeAnimation(tileMenuTabAnimation);
					// mapScreen.removeAnimation(tileMenuAnimation);
					return PointerState.ON_TILE_MENU_TAB;
				}
			}
			if (activeContentIndex > -1) {
				TouchableGroup2D grp = (TouchableGroup2D) objects.get(activeContentIndex * 2 + 1);
				return grp.onTouch(event, nx, ny, current);
			} else {
				return PointerState.NONE;
			}

		} else if (current == PointerState.ON_TILE_MENU_TAB) {
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				setX(nx - TileTab.WIDTH / 2 - 76 / 2);
				lastPointerDeltaX = nx - lastPointerX;
				lastPointerX = nx;
				lastPointerY = ny;
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				if (lastPointerDeltaX < 0 || lastPointerDeltaX == 0 && getX() >= 0) {
					// close
					mapScreen.collapseTileMenu();
				}
				if (lastPointerDeltaX > 0 || lastPointerDeltaX == 0 && getX() < 0) {
					// open
					tileMenuTabAnimation = new Translation2DAnimation(this, 500, 0, TileTab.WIDTH / 2 - getX(), 0);
					tileMenuTabAnimation.setInterpolation(new BounceInterpolator());
					mapScreen.addAnimation(tileMenuTabAnimation);
				}
				return PointerState.NONE;
			}
			return current;
		} else if (current == PointerState.ON_TILE_MENU) {
			if (activeContentIndex > -1) {
				TouchableGroup2D grp = (TouchableGroup2D) objects.get(activeContentIndex * 2 + 1);
				return grp.onTouch(event, nx, ny, current);
			} else {
				return current;
			}

		} else {
			return current;
		}

	}

}
