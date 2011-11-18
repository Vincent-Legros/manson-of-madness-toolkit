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

import org.amphiprion.gameengine3d.GameScreen;
import org.amphiprion.gameengine3d.ScreenProperty;
import org.amphiprion.gameengine3d.animation.Translation2DAnimation;
import org.amphiprion.gameengine3d.mesh.Image2D;
import org.amphiprion.mansionofmadness.dao.TileDao;
import org.amphiprion.mansionofmadness.dto.Tile;

import android.content.Context;
import android.view.MotionEvent;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * @author ng00124c
 * 
 */
public class MapScreen extends GameScreen {
	private Context context;

	private enum PointerState {
		NONE, ON_TILE_MENU_TAB, ON_TILE_MENU, ON_BOARD_TILE, ON_BOARD
	}

	private PointerState pointerState = PointerState.NONE;
	private int lastPointerX;
	private int lastPointerY;
	private int lastPointerDeltaX;
	private int lastPointerDeltaY;

	private List<Tile> availableTiles;
	private TileMenu tileMenu;
	private BoardMenu boardMenu;
	private Translation2DAnimation tileMenuTabAnimation;
	private Translation2DAnimation tileMenuAnimation;
	private Image2D imgTab;
	private Image2D imgTabBackground;
	private int tileIndex = -1;
	private Tile2D selectedTile;

	public MapScreen(Context context) {
		this.context = context;
		// ######### build the board #########
		boardMenu = new BoardMenu();
		// add the board to the rendering object tree
		objects2d.add(boardMenu);

		// ######### build the tile Menu ##########

		tileMenu = new TileMenu();

		imgTabBackground = new Image2D("tiles/tab_background.png", false, true);
		imgTabBackground.x = TileMenu.WIDTH / 2;
		imgTabBackground.y = 800 / 2;
		imgTabBackground.setScale(10);
		tileMenu.addObject(imgTabBackground);

		imgTab = new Image2D("tiles/tab.png", false, true);
		imgTab.x = TileMenu.WIDTH + 76 / 2;
		imgTab.y = 800 / 2;
		tileMenu.addObject(imgTab);

		objects2d.add(tileMenu);

		availableTiles = TileDao.getInstance(context).getTiles();
		int index = 0;
		for (Tile tile : availableTiles) {
			Image2D img = new Image2D("tiles/" + tile.getImageName());
			// rescale to enter in a 150x150 pixel (a case have a size of
			// 150x150pixel)
			float scale = Math.max(tile.getWidth(), tile.getHeight());
			img.setScale(1.0f / scale);
			img.x = TileMenu.WIDTH / 2;
			img.y = index * TileMenu.HEIGHT + TileMenu.HEIGHT / 2;

			tileMenu.addObject(img);

			index++;
		}

		// add the tile menu to the rendering object tree
		objects2d.add(tileMenu);

		// start collapsed
		tileMenu.setX(-TileMenu.WIDTH / 2);
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
			if (imgTab.contains(nx, ny)) {
				pointerState = PointerState.ON_TILE_MENU_TAB;
				removeAnimation(tileMenuTabAnimation);
				removeAnimation(tileMenuAnimation);
			} else if (imgTabBackground.contains(nx, ny)) {
				pointerState = PointerState.ON_TILE_MENU;
				tileIndex = (800 / 2 - tileMenu.getY() + ny) / TileMenu.HEIGHT;
				if (tileIndex < 0 || tileIndex >= availableTiles.size()) {
					tileIndex = -1;
				}
				removeAnimation(tileMenuAnimation);
			} else {
				pointerState = PointerState.ON_BOARD;
				boardMenu.setGlobalScale(0.5f);
			}
			lastPointerX = nx;
			lastPointerY = ny;
			lastPointerDeltaX = 0;
		} else if (pointerState == PointerState.ON_TILE_MENU_TAB) {
			onTouchTileMenuTab(event, nx, ny);
		} else if (pointerState == PointerState.ON_TILE_MENU) {
			onTouchTileMenu(event, nx, ny);
		} else if (pointerState == PointerState.ON_BOARD_TILE) {
			onTouchBoardTile(event, nx, ny);
		} else if (pointerState == PointerState.ON_BOARD) {
			onTouchBoard(event, nx, ny);
		}
	}

	private void onTouchBoard(MotionEvent event, int nx, int ny) {
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			lastPointerDeltaX = nx - lastPointerX;
			lastPointerDeltaY = ny - lastPointerY;
			boardMenu.setX(boardMenu.getX() + (int) (lastPointerDeltaX / boardMenu.getGlobalScale()));
			boardMenu.setY(boardMenu.getY() + (int) (lastPointerDeltaY / boardMenu.getGlobalScale()));
			lastPointerX = nx;
			lastPointerY = ny;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			pointerState = PointerState.NONE;
			int left = selectedTile.x - 150 * selectedTile.getTile().getWidth() / 2 - boardMenu.getX();
			int top = selectedTile.y - 150 * selectedTile.getTile().getHeight() / 2 - boardMenu.getY();

			selectedTile.x += (left + 75) / 150 * 150 - left;
			selectedTile.y += (top + 75) / 150 * 150 - top;
		}
	}

	private void onTouchBoardTile(MotionEvent event, int nx, int ny) {
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			// lastPointerDeltaX = nx - lastPointerX;
			// lastPointerDeltaY = ny - lastPointerY;
			selectedTile.x = (int) (nx / boardMenu.getGlobalScale());
			selectedTile.y = (int) (ny / boardMenu.getGlobalScale());
			// lastPointerX = nx;
			// lastPointerY = ny;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			pointerState = PointerState.NONE;
			int left = selectedTile.x - 150 * selectedTile.getTile().getWidth() / 2 - boardMenu.getX();
			int top = selectedTile.y - 150 * selectedTile.getTile().getHeight() / 2 - boardMenu.getY();

			selectedTile.x += (left + 75) / 150 * 150 - left;
			selectedTile.y += (top + 75) / 150 * 150 - top;
		}
	}

	private void onTouchTileMenu(MotionEvent event, int nx, int ny) {
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			lastPointerDeltaX = nx - lastPointerX;
			lastPointerDeltaY = ny - lastPointerY;
			tileMenu.setY(tileMenu.getY() + lastPointerDeltaY);
			// imgTab.setY(800 / 2);
			// imgTabBackground.setY(800 / 2);
			lastPointerX = nx;
			lastPointerY = ny;
			if (nx >= TileMenu.WIDTH && tileIndex > -1) {
				pointerState = PointerState.ON_BOARD_TILE;
				collapseTileMenu();
				Tile2D tile = new Tile2D(availableTiles.get(tileIndex));
				tile.x = (int) (nx / boardMenu.getGlobalScale());
				tile.y = (int) (ny / boardMenu.getGlobalScale());
				boardMenu.addObject(tile);
				selectedTile = tile;
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			pointerState = PointerState.NONE;
			if (Math.abs(lastPointerDeltaY) > 10) {
				tileMenuAnimation = new Translation2DAnimation(tileMenu, 1000, 0, 0, lastPointerDeltaY * 20);
				tileMenuAnimation.setInterpolation(new DecelerateInterpolator());
				addAnimation(tileMenuAnimation);
			}
		}
	}

	private void onTouchTileMenuTab(MotionEvent event, int nx, int ny) {
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (pointerState == PointerState.ON_TILE_MENU_TAB) {
				tileMenu.setX(nx - TileMenu.WIDTH / 2 - 76 / 2);
			}
			lastPointerDeltaX = nx - lastPointerX;
			lastPointerX = nx;
			lastPointerY = ny;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			pointerState = PointerState.NONE;
			if (lastPointerDeltaX < 0 || lastPointerDeltaX == 0 && tileMenu.getX() >= 0) {
				// close
				collapseTileMenu();
			}
			if (lastPointerDeltaX > 0 || lastPointerDeltaX == 0 && tileMenu.getX() < 0) {
				// open
				tileMenuTabAnimation = new Translation2DAnimation(tileMenu, 500, 0, TileMenu.WIDTH / 2 - tileMenu.getX(), 0);
				tileMenuTabAnimation.setInterpolation(new BounceInterpolator());
				addAnimation(tileMenuTabAnimation);
			}
		}
	}

	private void collapseTileMenu() {
		tileMenuTabAnimation = new Translation2DAnimation(tileMenu, 500, 0, -TileMenu.WIDTH / 2 - tileMenu.getX(), 0);
		tileMenuTabAnimation.setInterpolation(new BounceInterpolator());
		addAnimation(tileMenuTabAnimation);
	}
}
