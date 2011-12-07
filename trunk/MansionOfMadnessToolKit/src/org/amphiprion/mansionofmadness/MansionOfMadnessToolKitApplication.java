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
package org.amphiprion.mansionofmadness;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.amphiprion.mansionofmadness.dao.TileDao;
import org.amphiprion.mansionofmadness.dto.Tile;
import org.amphiprion.mansionofmadness.util.DeviceUtil;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

/**
 * @author ng00124c
 * 
 */
public class MansionOfMadnessToolKitApplication extends Application {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		DeviceUtil.init(this);

		// Move to SDCARD images of embedded Tiles
		String path = Environment.getExternalStorageDirectory() + "/" + ApplicationConstants.DIRECTORY_IMAGE_TILE;
		File imgPath = new File(path);
		imgPath.mkdirs();

		byte[] b = new byte[2048];
		int len = 0;

		List<Tile> tiles = TileDao.getInstance(this).getEmbeddedTiles();
		for (Tile tile : tiles) {
			File f = new File(imgPath, tile.getImageName());
			if (!f.exists()) {
				try {
					FileOutputStream fos = new FileOutputStream(f);
					InputStream is = getClass().getResourceAsStream("/images/tiles/" + tile.getImageName());
					while ((len = is.read(b)) > 0) {
						fos.write(b, 0, len);
					}
					fos.close();
					is.close();
				} catch (IOException e) {
					Log.e(ApplicationConstants.PACKAGE, "", e);
				}
			}
		}

		// Move to SDCARD images of card back
		path = Environment.getExternalStorageDirectory() + "/" + ApplicationConstants.DIRECTORY_IMAGE_CARD;
		imgPath = new File(path);
		imgPath.mkdirs();

		String[] strs = new String[] { "back_exploration.png", "back_lock.png", "back_obstacle.png" };
		for (String str : strs) {
			File f = new File(imgPath, str);
			if (!f.exists()) {
				try {
					FileOutputStream fos = new FileOutputStream(f);
					InputStream is = getClass().getResourceAsStream("/images/cards/" + str);
					while ((len = is.read(b)) > 0) {
						fos.write(b, 0, len);
					}
					fos.close();
					is.close();
				} catch (IOException e) {
					Log.e(ApplicationConstants.PACKAGE, "", e);
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onTerminate()
	 */
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
}
