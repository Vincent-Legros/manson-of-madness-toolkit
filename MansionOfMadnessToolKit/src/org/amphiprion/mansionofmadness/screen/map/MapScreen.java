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
import org.amphiprion.gameengine3d.IObject2D;
import org.amphiprion.gameengine3d.ScreenProperty;
import org.amphiprion.gameengine3d.animation.Translation2DAnimation;
import org.amphiprion.gameengine3d.mesh.Image2D;
import org.amphiprion.mansionofmadness.ApplicationConstants;
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

	private enum ComponentKey {
		MOVE_ICON, DELETE_ICON, ROTATE_CLOCK_ICON, ROTATE_COUNTER_CLOCK_ICON
	}

	private PointerState pointerState = PointerState.NONE;
	private int lastPointerX;
	private int lastPointerY;
	private int lastPointerDeltaX;
	private int lastPointerDeltaY;
	private float lastPointerDist;
	private long lastPointerDownTime;
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
			String txt;
			if (tile.isEmbedded()) {
				txt = context.getString(context.getResources().getIdentifier("tile_" + tile.getName(), "string", ApplicationConstants.PACKAGE));
			} else {
				txt = tile.getName();
			}
			Image2D imgTxt = new Image2D("@String/" + txt);
			imgTxt.x = TileMenu.WIDTH / 2;
			imgTxt.y = index * TileMenu.HEIGHT + TileMenu.HEIGHT / 2 + (int) (tile.getHeight() * (150 / 2) / scale) + 15;
			tileMenu.addObject(imgTxt);

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
			clearTileIcons();
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
				lastPointerDownTime = System.currentTimeMillis();
				if (selectedTile != null) {
					Image2D img = (Image2D) getHMIComponent(ComponentKey.MOVE_ICON);
					if (img.contains(nx, ny)) {
						pointerState = PointerState.ON_BOARD_TILE;
					}
					img = (Image2D) getHMIComponent(ComponentKey.DELETE_ICON);
					if (img.contains(nx, ny)) {
						boardMenu.removeObject(selectedTile);
						selectedTile = null;
						pointerState = PointerState.NONE;
						return;
					}
					img = (Image2D) getHMIComponent(ComponentKey.ROTATE_CLOCK_ICON);
					if (img.contains(nx, ny)) {
						pointerState = PointerState.NONE;
						selectedTile.setRotation(selectedTile.getRotation() + 90);
						anchorTile(selectedTile);
						return;
					}
					img = (Image2D) getHMIComponent(ComponentKey.ROTATE_COUNTER_CLOCK_ICON);
					if (img.contains(nx, ny)) {
						pointerState = PointerState.NONE;

						selectedTile.setRotation(selectedTile.getRotation() + 270);
						anchorTile(selectedTile);

						return;
					}
				}
			}
			lastPointerX = nx;
			lastPointerY = ny;
			lastPointerDeltaX = 0;
			lastPointerDeltaY = 0;
			lastPointerDist = 0;
		} else if (pointerState == PointerState.ON_TILE_MENU_TAB) {
			onTouchTileMenuTab(event, nx, ny);
		} else if (pointerState == PointerState.ON_TILE_MENU) {
			onTouchTileMenu(event, nx, ny);
		} else if (pointerState == PointerState.ON_BOARD_TILE) {
			onTouchBoardTile(event, nx, ny);
		} else if (pointerState == PointerState.ON_BOARD) {
			onTouchBoard(event, nx, ny);
		}

		if (event.getAction() == MotionEvent.ACTION_UP) {
			defineTileIcons();
		}
	}

	private void clearTileIcons() {
		if (selectedTile != null) {
			boardMenu.removeObject(getHMIComponent(ComponentKey.MOVE_ICON));
			boardMenu.removeObject(getHMIComponent(ComponentKey.DELETE_ICON));
			boardMenu.removeObject(getHMIComponent(ComponentKey.ROTATE_CLOCK_ICON));
			boardMenu.removeObject(getHMIComponent(ComponentKey.ROTATE_COUNTER_CLOCK_ICON));
		}
	}

	private void defineTileIcons() {
		if (selectedTile != null) {

			Image2D img = new Image2D("tiles/icons/move.png");
			img.x = selectedTile.x;
			img.y = selectedTile.y;
			registerHMIComponent(ComponentKey.MOVE_ICON, img);
			boardMenu.addObject(img);

			img = new Image2D("tiles/icons/close.png");
			img.x = selectedTile.x + selectedTile.getTileWidth() * 150 / 2 - 32;
			img.y = selectedTile.y + selectedTile.getTileHeight() * 150 / 2 - 32;
			registerHMIComponent(ComponentKey.DELETE_ICON, img);
			boardMenu.addObject(img);

			img = new Image2D("tiles/icons/rotate_clock.png");
			img.x = selectedTile.x + selectedTile.getTileWidth() * 150 / 2 - 32;
			img.y = selectedTile.y - selectedTile.getTileHeight() * 150 / 2 + 32;
			registerHMIComponent(ComponentKey.ROTATE_CLOCK_ICON, img);
			boardMenu.addObject(img);

			img = new Image2D("tiles/icons/rotate_counter_clock.png");
			img.x = selectedTile.x - selectedTile.getTileWidth() * 150 / 2 + 32;
			img.y = selectedTile.y - selectedTile.getTileHeight() * 150 / 2 + 32;
			registerHMIComponent(ComponentKey.ROTATE_COUNTER_CLOCK_ICON, img);
			boardMenu.addObject(img);
		}
	}

	private void onTouchBoard(MotionEvent event, int nx, int ny) {
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (event.getPointerCount() > 1) {
				// zoom
				ScreenProperty sp = view.getScreenProperty();
				int nx2 = (int) (event.getX(1) / sp.screenScale);
				int ny2 = (int) (event.getY(1) / sp.screenScale);
				float newDist = (float) Math.sqrt((nx - nx2) * (nx - nx2) + (ny - ny2) * (ny - ny2));
				float deltaX = (nx + nx2) / 2 - lastPointerX;
				float deltaY = (ny + ny2) / 2 - lastPointerY;

				boardMenu.setX((int) (boardMenu.getX() + deltaX / boardMenu.getGlobalScale()));
				boardMenu.setY((int) (boardMenu.getY() + deltaY / boardMenu.getGlobalScale()));

				lastPointerX = (nx + nx2) / 2;
				lastPointerY = (ny + ny2) / 2;
				float x = lastPointerX / boardMenu.getGlobalScale() - boardMenu.getX();
				float y = lastPointerY / boardMenu.getGlobalScale() - boardMenu.getY();
				boardMenu.setGlobalScale(boardMenu.getGlobalScale() * newDist / lastPointerDist);
				boardMenu.setX((int) (lastPointerX / boardMenu.getGlobalScale() - x));
				boardMenu.setY((int) (lastPointerY / boardMenu.getGlobalScale() - y));
				lastPointerDist = newDist;
			} else {
				// move
				lastPointerDeltaX = nx - lastPointerX;
				lastPointerDeltaY = ny - lastPointerY;
				boardMenu.setX(boardMenu.getX() + (int) (lastPointerDeltaX / boardMenu.getGlobalScale()));
				boardMenu.setY(boardMenu.getY() + (int) (lastPointerDeltaY / boardMenu.getGlobalScale()));
				lastPointerX = nx;
				lastPointerY = ny;
			}
		} else if (event.getAction() == MotionEvent.ACTION_POINTER_2_DOWN) {
			ScreenProperty sp = view.getScreenProperty();
			int nx2 = (int) (event.getX(1) / sp.screenScale);
			int ny2 = (int) (event.getY(1) / sp.screenScale);
			lastPointerX = (nx + nx2) / 2;
			lastPointerY = (ny + ny2) / 2;
			lastPointerDist = (float) Math.sqrt((nx - nx2) * (nx - nx2) + (ny - ny2) * (ny - ny2));
		} else if (event.getAction() == MotionEvent.ACTION_POINTER_2_UP) {
			lastPointerDeltaX = 0;
			lastPointerDeltaY = 0;
			lastPointerX = nx;
			lastPointerY = ny;
		} else if (event.getAction() == MotionEvent.ACTION_POINTER_1_UP) {
			ScreenProperty sp = view.getScreenProperty();
			int nx2 = (int) (event.getX(1) / sp.screenScale);
			int ny2 = (int) (event.getY(1) / sp.screenScale);
			lastPointerDeltaX = 0;
			lastPointerDeltaY = 0;
			lastPointerX = nx2;
			lastPointerY = ny2;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			pointerState = PointerState.NONE;
			if (System.currentTimeMillis() - lastPointerDownTime < 300) {
				// simple click, try to select of tile
				selectedTile = null;
				for (IObject2D o : boardMenu.getObjects()) {
					if (o instanceof Tile2D && ((Tile2D) o).contains(nx, ny)) {
						selectedTile = (Tile2D) o;
						break;
					}
				}
			}
		}
	}

	private void onTouchBoardTile(MotionEvent event, int nx, int ny) {
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			lastPointerDeltaX = nx - lastPointerX;
			lastPointerDeltaY = ny - lastPointerY;
			selectedTile.x += (int) (lastPointerDeltaX / boardMenu.getGlobalScale());
			selectedTile.y += (int) (lastPointerDeltaY / boardMenu.getGlobalScale());
			lastPointerX = nx;
			lastPointerY = ny;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			pointerState = PointerState.NONE;
			anchorTile(selectedTile);
		}
	}

	private void anchorTile(Tile2D tileToAnchor) {
		int left = tileToAnchor.x - 150 * tileToAnchor.getTileWidth() / 2 - boardMenu.getX();
		int top = tileToAnchor.y - 150 * tileToAnchor.getTileHeight() / 2 - boardMenu.getY();
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
