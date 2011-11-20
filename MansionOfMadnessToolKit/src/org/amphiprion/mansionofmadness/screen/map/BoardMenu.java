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

import org.amphiprion.gameengine3d.IObject2D;
import org.amphiprion.gameengine3d.ScreenProperty;
import org.amphiprion.gameengine3d.mesh.Image2D;
import org.amphiprion.mansionofmadness.screen.map.MapScreen.ComponentKey;

import android.view.MotionEvent;

/**
 * @author ng00124c
 * 
 */
public class BoardMenu extends TouchableGroup2D {
	private MapScreen mapScreen;
	private Tile2D selectedTile;

	private int lastPointerX;
	private int lastPointerY;
	private int lastPointerDeltaX;
	private int lastPointerDeltaY;
	private float lastPointerDist;
	private long lastPointerDownTime;

	/**
	 * 
	 */
	public BoardMenu(MapScreen mapScreen) {
		this.mapScreen = mapScreen;
	}

	public void addAndSelectTile(Tile2D tile, int nx, int ny) {
		super.addObject(tile);

		clearTileIcons();
		selectedTile = tile;
		lastPointerX = nx;
		lastPointerY = ny;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.amphiprion.mansionofmadness.screen.map.TouchableGroup2D#onTouch(android
	 * .view.MotionEvent, int, int,
	 * org.amphiprion.mansionofmadness.screen.map.TouchableGroup2D.PointerState)
	 */
	@Override
	public PointerState onTouch(MotionEvent event, int nx, int ny, PointerState current) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			lastPointerX = nx;
			lastPointerY = ny;
			lastPointerDeltaX = 0;
			lastPointerDeltaY = 0;

			lastPointerDownTime = System.currentTimeMillis();
			if (selectedTile != null) {
				Image2D img = (Image2D) mapScreen.getHMIComponent(MapScreen.ComponentKey.MOVE_ICON);
				if (img.contains(nx, ny)) {
					clearTileIcons();
					return PointerState.ON_BOARD_TILE;
				}
				img = (Image2D) mapScreen.getHMIComponent(MapScreen.ComponentKey.DELETE_ICON);
				if (img.contains(nx, ny)) {
					clearTileIcons();
					removeObject(selectedTile);
					selectedTile = null;
					return PointerState.NONE;
				}
				img = (Image2D) mapScreen.getHMIComponent(MapScreen.ComponentKey.ROTATE_CLOCK_ICON);
				if (img.contains(nx, ny)) {
					clearTileIcons();
					selectedTile.setRotation(selectedTile.getRotation() + 90);
					anchorTile(selectedTile);
					defineTileIcons();
					return PointerState.NONE;
				}
				img = (Image2D) mapScreen.getHMIComponent(MapScreen.ComponentKey.ROTATE_COUNTER_CLOCK_ICON);
				if (img.contains(nx, ny)) {
					clearTileIcons();
					selectedTile.setRotation(selectedTile.getRotation() + 270);
					anchorTile(selectedTile);
					defineTileIcons();
					return PointerState.NONE;
				}
			}
			return PointerState.ON_BOARD;
		} else if (current == PointerState.ON_BOARD_TILE) {
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				lastPointerDeltaX = nx - lastPointerX;
				lastPointerDeltaY = ny - lastPointerY;
				selectedTile.x += (int) (lastPointerDeltaX / getGlobalScale());
				selectedTile.y += (int) (lastPointerDeltaY / getGlobalScale());
				lastPointerX = nx;
				lastPointerY = ny;
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				anchorTile(selectedTile);
				defineTileIcons();
				return PointerState.NONE;
			}
			return current;
		} else if (current == PointerState.ON_BOARD) {
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if (event.getPointerCount() > 1) {
					// zoom
					ScreenProperty sp = mapScreen.getView().getScreenProperty();
					int nx2 = (int) (event.getX(1) / sp.screenScale);
					int ny2 = (int) (event.getY(1) / sp.screenScale);
					float newDist = (float) Math.sqrt((nx - nx2) * (nx - nx2) + (ny - ny2) * (ny - ny2));
					float deltaX = (nx + nx2) / 2 - lastPointerX;
					float deltaY = (ny + ny2) / 2 - lastPointerY;

					setX((int) (getX() + deltaX / getGlobalScale()));
					setY((int) (getY() + deltaY / getGlobalScale()));

					lastPointerX = (nx + nx2) / 2;
					lastPointerY = (ny + ny2) / 2;
					float x = lastPointerX / getGlobalScale() - getX();
					float y = lastPointerY / getGlobalScale() - getY();
					setGlobalScale(getGlobalScale() * newDist / lastPointerDist);
					setX((int) (lastPointerX / getGlobalScale() - x));
					setY((int) (lastPointerY / getGlobalScale() - y));
					lastPointerDist = newDist;
				} else {
					// move
					lastPointerDeltaX = nx - lastPointerX;
					lastPointerDeltaY = ny - lastPointerY;
					setX(getX() + (int) (lastPointerDeltaX / getGlobalScale()));
					setY(getY() + (int) (lastPointerDeltaY / getGlobalScale()));
					lastPointerX = nx;
					lastPointerY = ny;
				}
				return current;
			} else if (event.getAction() == MotionEvent.ACTION_POINTER_2_DOWN) {
				ScreenProperty sp = mapScreen.getView().getScreenProperty();
				int nx2 = (int) (event.getX(1) / sp.screenScale);
				int ny2 = (int) (event.getY(1) / sp.screenScale);
				lastPointerX = (nx + nx2) / 2;
				lastPointerY = (ny + ny2) / 2;
				lastPointerDist = (float) Math.sqrt((nx - nx2) * (nx - nx2) + (ny - ny2) * (ny - ny2));
				return current;
			} else if (event.getAction() == MotionEvent.ACTION_POINTER_2_UP) {
				lastPointerDeltaX = 0;
				lastPointerDeltaY = 0;
				lastPointerX = nx;
				lastPointerY = ny;
				return current;
			} else if (event.getAction() == MotionEvent.ACTION_POINTER_1_UP) {
				ScreenProperty sp = mapScreen.getView().getScreenProperty();
				int nx2 = (int) (event.getX(1) / sp.screenScale);
				int ny2 = (int) (event.getY(1) / sp.screenScale);
				lastPointerDeltaX = 0;
				lastPointerDeltaY = 0;
				lastPointerX = nx2;
				lastPointerY = ny2;
				return current;
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				if (System.currentTimeMillis() - lastPointerDownTime < 300) {
					// simple click, try to select of tile
					clearTileIcons();
					selectedTile = null;
					for (IObject2D o : getObjects()) {
						if (o instanceof Tile2D && ((Tile2D) o).contains(nx, ny)) {
							selectedTile = (Tile2D) o;
							defineTileIcons();
							break;
						}
					}
				}
				return PointerState.NONE;
			} else {
				return current;
			}
		} else {
			return current;
		}
	}

	private void anchorTile(Tile2D tileToAnchor) {
		int left = tileToAnchor.x - 150 * tileToAnchor.getTileWidth() / 2 - getX();
		int top = tileToAnchor.y - 150 * tileToAnchor.getTileHeight() / 2 - getY();
		if (left < 0) {
			tileToAnchor.x += (left - 75) / 150 * 150 - left;
		} else {
			tileToAnchor.x += (left + 75) / 150 * 150 - left;
		}
		if (top < 0) {
			tileToAnchor.y += (top - 75) / 150 * 150 - top;
		} else {
			tileToAnchor.y += (top + 75) / 150 * 150 - top;
		}
	}

	private void clearTileIcons() {
		if (selectedTile != null) {
			removeObject(mapScreen.getHMIComponent(ComponentKey.MOVE_ICON));
			removeObject(mapScreen.getHMIComponent(ComponentKey.DELETE_ICON));
			removeObject(mapScreen.getHMIComponent(ComponentKey.ROTATE_CLOCK_ICON));
			removeObject(mapScreen.getHMIComponent(ComponentKey.ROTATE_COUNTER_CLOCK_ICON));
		}
	}

	private void defineTileIcons() {
		if (selectedTile != null) {

			Image2D img = new Image2D("tiles/icons/move.png");
			img.x = selectedTile.x;
			img.y = selectedTile.y;
			mapScreen.registerHMIComponent(ComponentKey.MOVE_ICON, img);
			addObject(img);

			img = new Image2D("tiles/icons/close.png");
			img.x = selectedTile.x + selectedTile.getTileWidth() * 150 / 2 - 32;
			img.y = selectedTile.y + selectedTile.getTileHeight() * 150 / 2 - 32;
			mapScreen.registerHMIComponent(ComponentKey.DELETE_ICON, img);
			addObject(img);

			img = new Image2D("tiles/icons/rotate_clock.png");
			img.x = selectedTile.x + selectedTile.getTileWidth() * 150 / 2 - 32;
			img.y = selectedTile.y - selectedTile.getTileHeight() * 150 / 2 + 32;
			mapScreen.registerHMIComponent(ComponentKey.ROTATE_CLOCK_ICON, img);
			addObject(img);

			img = new Image2D("tiles/icons/rotate_counter_clock.png");
			img.x = selectedTile.x - selectedTile.getTileWidth() * 150 / 2 + 32;
			img.y = selectedTile.y - selectedTile.getTileHeight() * 150 / 2 + 32;
			mapScreen.registerHMIComponent(ComponentKey.ROTATE_COUNTER_CLOCK_ICON, img);
			addObject(img);
		}
	}
}
