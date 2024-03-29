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

import java.util.List;

import org.amphiprion.gameengine3d.animation.Translation2DAnimation;
import org.amphiprion.gameengine3d.mesh.Image2D;
import org.amphiprion.mansionofmadness.dto.Tile;
import org.amphiprion.mansionofmadness.dto.TileInstance;

import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;

/**
 * @author ng00124c
 * 
 */
public class TileMenu extends TouchableGroup2D {
	public static int HEIGHT = 200;

	private int lastPointerX;
	private int lastPointerY;
	private int lastPointerDeltaX;
	private int lastPointerDeltaY;

	private Image2D background;
	private MapScreen mapScreen;
	private Translation2DAnimation tileMenuAnimation;
	private int tileIndex = -1;
	private List<Tile> availableTiles;

	/**
	 * 
	 */
	public TileMenu(MapScreen mapScreen, String backgroundUri, List<Tile> availableTiles) {
		this.mapScreen = mapScreen;
		this.availableTiles = availableTiles;
		setX(ComponentTab.WIDTH / 2);
		setY(800 / 2);

		background = new Image2D(backgroundUri, false, true);
		background.x = ComponentTab.WIDTH;
		background.y = 800 / 2;
		//background.setScale(10);
		addObject(background);

		int index = 0;
		for (Tile tile : availableTiles) {
			Image2D img = new Image2D(tile.isEmbedded() ? "tiles/" + tile.getImageName() : tile.getImageName());
			// rescale to enter in a 150x150 pixel (a case have a size of
			// 150x150pixel)
			float scale = Math.max(tile.getWidth(), tile.getHeight());
			img.setScale(1.0f / scale);
			img.x = ComponentTab.WIDTH / 2;
			img.y = index * TileMenu.HEIGHT + TileMenu.HEIGHT / 2;

			addObject(img);

			Image2D imgTxt = new Image2D("@String/" + tile.getDisplayName());
			imgTxt.x = ComponentTab.WIDTH / 2;
			imgTxt.y = index * TileMenu.HEIGHT + TileMenu.HEIGHT / 2 + (int) (tile.getHeight() * (150 / 2) / scale) + 15;
			addObject(imgTxt);

			index++;
		}
		
		background = new Image2D("sounds/tab_background_top.png", false, true);
		background.x = ComponentTab.WIDTH;
		background.y = 800 / 2;
		addObject(background);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.amphiprion.gameengine3d.Group2D#setY(int)
	 */
	@Override
	public void setY(int y) {
		if (y > 800 / 2 || count() <= 3) {
			super.setY(800 / 2);
		} else if (y < 800 / 2 - HEIGHT * (count() - 3)) {
			super.setY(-HEIGHT * (count() - 3) + 800 / 2);
		} else {
			super.setY(y);
		}
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

			if (background.contains(nx, ny)) {
				tileIndex = (800 / 2 - getY() + ny) / TileMenu.HEIGHT;
				if (tileIndex < 0 || tileIndex >= availableTiles.size()) {
					tileIndex = -1;
				}
				mapScreen.removeAnimation(tileMenuAnimation);
				return PointerState.ON_TILE_MENU;
			}
			return current;
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			lastPointerDeltaX = nx - lastPointerX;
			lastPointerDeltaY = ny - lastPointerY;
			setY(getY() + lastPointerDeltaY);
			// imgTab.setY(800 / 2);
			// imgTabBackground.setY(800 / 2);
			lastPointerX = nx;
			lastPointerY = ny;
			if (nx >= ComponentTab.WIDTH && tileIndex > -1) {
				mapScreen.collapseTileMenu();
				Tile2D tile = new Tile2D(mapScreen, new TileInstance(), availableTiles.get(tileIndex));
				tile.x = (int) (nx / mapScreen.boardMenu.getGlobalScale());
				tile.y = (int) (ny / mapScreen.boardMenu.getGlobalScale());
				mapScreen.boardMenu.addAndSelectTile(tile, nx, ny);
				return PointerState.ON_BOARD_TILE;
			}
			return current;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (Math.abs(lastPointerDeltaY) > 10) {
				tileMenuAnimation = new Translation2DAnimation(this, 1000, 0, 0, lastPointerDeltaY * 20);
				tileMenuAnimation.setInterpolation(new DecelerateInterpolator());
				mapScreen.addAnimation(tileMenuAnimation);
			}
			return PointerState.NONE;
		}
		return PointerState.NONE;
	}
}
