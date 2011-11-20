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

import org.amphiprion.gameengine3d.GameScreen;
import org.amphiprion.gameengine3d.ScreenProperty;
import org.amphiprion.gameengine3d.animation.Translation2DAnimation;
import org.amphiprion.gameengine3d.mesh.Image2D;
import org.amphiprion.mansionofmadness.screen.map.TouchableGroup2D.PointerState;

import android.content.Context;
import android.view.MotionEvent;
import android.view.animation.BounceInterpolator;

/**
 * @author ng00124c
 * 
 */
public class MapScreen extends GameScreen {
	private Context context;

	public enum ComponentKey {
		MOVE_ICON, DELETE_ICON, ROTATE_CLOCK_ICON, ROTATE_COUNTER_CLOCK_ICON
	}

	private PointerState pointerState = PointerState.NONE;
	// private int lastPointerX;
	// private int lastPointerY;
	// private int lastPointerDeltaX;
	// private int lastPointerDeltaY;
	// private float lastPointerDist;
	// private long lastPointerDownTime;
	private ComponentTab tileTab;
	private TileMenu tileMenu;
	protected BoardMenu boardMenu;
	private Translation2DAnimation tileMenuTabAnimation;
	// private Translation2DAnimation tileMenuAnimation;
	private Image2D imgTab;

	// private Image2D imgTabBackground;
	// private int tileIndex = -1;
	// private Tile2D selectedTile;

	public MapScreen(Context context) {
		this.context = context;
		// ######### build the board #########
		boardMenu = new BoardMenu(this);
		// add the board to the rendering object tree
		objects2d.add(boardMenu);

		// ######### build the tile Menu ##########

		tileTab = new ComponentTab(this);
		imgTab = new Image2D("tiles/tab.png", false, true);
		imgTab.x = ComponentTab.WIDTH + 76 / 2;
		imgTab.y = 800 / 2;

		tileMenu = new TileMenu(this, "tiles/tab_background.png");

		tileTab.addContentTab(tileMenu, imgTab);

		// add the tile menu to the rendering object tree
		objects2d.add(tileTab);

		// start collapsed
		tileTab.setX(-ComponentTab.WIDTH / 2);
	}

	/**
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.amphiprion.gameengine3d.GameScreen#onTouch(android.view.MotionEvent)
	 */
	@Override
	public void onTouch(MotionEvent event) {
		ScreenProperty sp = view.getScreenProperty();
		int nx = (int) (event.getX() / sp.screenScale);
		int ny = (int) (event.getY() / sp.screenScale);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			pointerState = tileTab.onTouch(event, nx, ny, pointerState);
			if (pointerState == PointerState.NONE) {
				pointerState = boardMenu.onTouch(event, nx, ny, pointerState);
			} else if (pointerState == PointerState.ON_TILE_MENU_TAB) {
				clearAnimation(tileMenuTabAnimation);
			}
		} else if (pointerState == PointerState.ON_TILE_MENU_TAB || pointerState == PointerState.ON_TILE_MENU) {
			pointerState = tileTab.onTouch(event, nx, ny, pointerState);
		} else if (pointerState == PointerState.ON_BOARD_TILE || pointerState == PointerState.ON_BOARD) {
			pointerState = boardMenu.onTouch(event, nx, ny, pointerState);
		}

		// if (event.getAction() == MotionEvent.ACTION_UP) {
		// defineTileIcons();
		// }
	}

	// private void onTouchTileMenu(MotionEvent event, int nx, int ny) {
	// if (event.getAction() == MotionEvent.ACTION_MOVE) {
	// lastPointerDeltaX = nx - lastPointerX;
	// lastPointerDeltaY = ny - lastPointerY;
	// tileMenu.setY(tileMenu.getY() + lastPointerDeltaY);
	// // imgTab.setY(800 / 2);
	// // imgTabBackground.setY(800 / 2);
	// lastPointerX = nx;
	// lastPointerY = ny;
	// if (nx >= TileTab.WIDTH && tileIndex > -1) {
	// pointerState = PointerState.ON_BOARD_TILE;
	// collapseTileMenu();
	// Tile2D tile = new Tile2D(availableTiles.get(tileIndex));
	// tile.x = (int) (nx / boardMenu.getGlobalScale());
	// tile.y = (int) (ny / boardMenu.getGlobalScale());
	// boardMenu.addObject(tile);
	// selectedTile = tile;
	// }
	// } else if (event.getAction() == MotionEvent.ACTION_UP) {
	// pointerState = PointerState.NONE;
	// if (Math.abs(lastPointerDeltaY) > 10) {
	// tileMenuAnimation = new Translation2DAnimation(tileMenu, 1000, 0, 0,
	// lastPointerDeltaY * 20);
	// tileMenuAnimation.setInterpolation(new DecelerateInterpolator());
	// addAnimation(tileMenuAnimation);
	// }
	// }
	// }

	public void collapseTileMenu() {
		tileMenuTabAnimation = new Translation2DAnimation(tileTab, 500, 0, -ComponentTab.WIDTH / 2 - tileTab.getX(), 0);
		tileMenuTabAnimation.setInterpolation(new BounceInterpolator());
		addAnimation(tileMenuTabAnimation);
	}
}