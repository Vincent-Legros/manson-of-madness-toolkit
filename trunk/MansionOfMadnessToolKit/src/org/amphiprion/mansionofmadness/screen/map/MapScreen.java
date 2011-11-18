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

/**
 * @author ng00124c
 * 
 */
public class MapScreen extends GameScreen {
	private Context context;

	private enum PointerState {
		NONE, ON_TILE_MENU_TAB
	}

	private PointerState pointerState = PointerState.NONE;
	private float lastPointerX;
	private float lastPointerY;
	private float lastPointerDeltaX;

	private List<Tile> availableTiles;
	private TileMenu tileMenu;
	private BoardMenu boardMenu;

	private Image2D imgTab;

	public MapScreen(Context context) {
		this.context = context;
		// ######### build the board #########
		boardMenu = new BoardMenu();
		// add the board to the rendering object tree
		objects2d.add(boardMenu);

		// ######### build the tile Menu ##########
		tileMenu = new TileMenu();

		Image2D imgTabBack = new Image2D("tiles/tab_background.png");
		imgTabBack.x = TileMenu.WIDTH / 2;
		imgTabBack.y = 800 / 2;
		imgTabBack.setScale(10);
		tileMenu.addObject(imgTabBack);

		imgTab = new Image2D("tiles/tab.png");
		imgTab.x = TileMenu.WIDTH + 76 / 2;
		imgTab.y = 800 / 2;
		tileMenu.addObject(imgTab);

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
		float nx = event.getX() / sp.screenScale;
		float ny = event.getY() / sp.screenScale;
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (imgTab.contains((int) nx, (int) ny)) {
				pointerState = PointerState.ON_TILE_MENU_TAB;
			}
			lastPointerX = nx;
			lastPointerY = ny;
			lastPointerDeltaX = 0;
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (pointerState == PointerState.ON_TILE_MENU_TAB) {
				tileMenu.setX((int) nx - TileMenu.WIDTH / 2 - 76 / 2);
			}
			lastPointerDeltaX = nx - lastPointerX;
			lastPointerX = nx;
			lastPointerY = ny;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			pointerState = PointerState.NONE;
			if (lastPointerDeltaX < 0 || lastPointerDeltaX == 0 && tileMenu.getX() >= 0) {
				// close
				clearAnimation();
				Translation2DAnimation anim = new Translation2DAnimation(tileMenu, 500, 0, -TileMenu.WIDTH / 2 - tileMenu.getX(), 0);
				anim.setInterpolation(new BounceInterpolator());
				addAnimation(anim);
			}
			if (lastPointerDeltaX > 0 || lastPointerDeltaX == 0 && tileMenu.getX() < 0) {
				// open
				clearAnimation();
				Translation2DAnimation anim = new Translation2DAnimation(tileMenu, 500, 0, TileMenu.WIDTH / 2 - tileMenu.getX(), 0);
				anim.setInterpolation(new BounceInterpolator());
				addAnimation(anim);
			}
		}
	}
}
