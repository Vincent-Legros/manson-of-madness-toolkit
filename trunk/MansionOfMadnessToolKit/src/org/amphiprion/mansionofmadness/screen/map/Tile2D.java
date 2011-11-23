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

import javax.microedition.khronos.opengles.GL10;

import org.amphiprion.gameengine3d.mesh.Image2D;
import org.amphiprion.mansionofmadness.dto.Tile;
import org.amphiprion.mansionofmadness.dto.TileInstance;

/**
 * @author Amphiprion
 * 
 */
public class Tile2D extends Image2D {
	private TileInstance tileInstance;
	private Tile tile;
	private Text2D imgName;
	private MapScreen mapScreen;

	public Tile2D(MapScreen mapScreen, TileInstance tileInstance, Tile tile) {
		super(tile.isEmbedded() ? "tiles/" + tile.getImageName() : tile.getImageName());
		this.tile = tile;
		this.tileInstance = tileInstance;
		imgName = new Text2D(tile.getDisplayName(), 25);
		this.mapScreen = mapScreen;
	}

	/**
	 * @return the tile
	 */
	public Tile getTile() {
		return tile;
	}

	/**
	 * @return the tileInstance
	 */
	public TileInstance getTileInstance() {
		return tileInstance;
	}

	public int getTileWidth() {
		if (getRotation() == 90 || getRotation() == 270) {
			return tile.getHeight();
		} else {
			return tile.getWidth();
		}
	}

	public int getTileHeight() {
		if (getRotation() == 90 || getRotation() == 270) {
			return tile.getWidth();
		} else {
			return tile.getHeight();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.amphiprion.gameengine3d.mesh.Image2D#draw(javax.microedition.khronos
	 * .opengles.GL10, float, int, int)
	 */
	@Override
	public void draw(GL10 gl, float screenScale, int screenWidth, int screenHeight) {
		super.draw(gl, screenScale, screenWidth, screenHeight);
		if (mapScreen.showLabels) {
			imgName.x = x;
			imgName.y = y;
			imgName.setGlobalScale(getGlobalScale());
			imgName.draw(gl, screenScale, screenWidth, screenHeight);
		}
	}
}
