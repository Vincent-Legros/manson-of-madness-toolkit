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
import org.amphiprion.gameengine3d.mesh.Image2D;
import org.amphiprion.mansionofmadness.dao.TileDao;
import org.amphiprion.mansionofmadness.dto.Tile;

import android.content.Context;

/**
 * @author ng00124c
 * 
 */
public class MapScreen extends GameScreen {
	private Context context;

	private List<Tile> availableTiles;
	private TileMenu tileMenu;
	private BoardMenu boardMenu;

	public MapScreen(Context context) {
		this.context = context;
		// ######### build the board #########
		boardMenu = new BoardMenu();
		// add the board to the rendering object tree
		objects2d.add(boardMenu);

		// ######### build the tile Menu ##########
		tileMenu = new TileMenu();
		availableTiles = TileDao.getInstance(context).getTiles();
		int index = 0;
		for (Tile tile : availableTiles) {
			Image2D img = new Image2D(tile.getImageName());
			// rescale to enter in a 150x150 pixel
			float scale = Math.max(tile.getWidth(), tile.getHeight());
			img.setScale(1.0f / scale);
			img.x = 150 / 2;
			img.y = index * 150 + 150 / 2;

			tileMenu.addObject(img);

			index++;
		}

		// add the tile menu to the rendering object tree
		objects2d.add(tileMenu);
	}

}
