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
import org.amphiprion.mansionofmadness.dto.Sound;
import org.amphiprion.mansionofmadness.dto.SoundInstance;
import org.amphiprion.mansionofmadness.util.DeviceUtil;

import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;

/**
 * @author ng00124c
 * 
 */
public class SoundMenu extends TouchableGroup2D {
	public static int HEIGHT = 80;

	private int lastPointerX;
	private int lastPointerY;
	private int lastPointerDeltaX;
	private int lastPointerDeltaY;
	private long lastPointerDownTime;

	private Image2D background;
	private MapScreen mapScreen;
	private Translation2DAnimation soundMenuAnimation;
	private int soundIndex = -1;
	private List<Sound> availableSounds;

	/**
	 * 
	 */
	public SoundMenu(MapScreen mapScreen, String backgroundUri, List<Sound> availableSounds) {
		this.mapScreen = mapScreen;
		this.availableSounds = availableSounds;
		setX(ComponentTab.WIDTH / 2);
		setY(800 / 2);

		background = new Image2D(backgroundUri, false, true);
		background.x = ComponentTab.WIDTH / 2;
		background.y = 800 / 2;
		background.setScale(10);
		addObject(background);

		int index = 0;
		for (Sound sound : availableSounds) {
			Image2D img = new Image2D("sounds/sound.png");
			// rescale to enter in a 150x150 pixel (a case have a size of
			// 150x150pixel)

			img.x = ComponentTab.WIDTH / 2;
			img.y = index * SoundMenu.HEIGHT + SoundMenu.HEIGHT / 2;

			addObject(img);

			Image2D imgTxt = new Image2D("@String/" + sound.getDisplayName());
			imgTxt.x = ComponentTab.WIDTH / 2;
			imgTxt.y = index * SoundMenu.HEIGHT + SoundMenu.HEIGHT / 2 + 40;
			addObject(imgTxt);

			index++;
		}
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
			lastPointerDownTime = System.currentTimeMillis();

			lastPointerX = nx;
			lastPointerY = ny;
			lastPointerDeltaX = 0;
			lastPointerDeltaY = 0;

			if (background.contains(nx, ny)) {
				soundIndex = (800 / 2 - getY() + ny) / SoundMenu.HEIGHT;
				if (soundIndex < 0 || soundIndex >= availableSounds.size()) {
					soundIndex = -1;
				}
				mapScreen.removeAnimation(soundMenuAnimation);
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
			if (mapScreen.inEdition && nx >= ComponentTab.WIDTH && soundIndex > -1) {
				mapScreen.collapseTileMenu();
				Sound2D sound = new Sound2D(mapScreen, new SoundInstance(), availableSounds.get(soundIndex));
				sound.x = (int) (nx / mapScreen.boardMenu.getGlobalScale());
				sound.y = (int) (ny / mapScreen.boardMenu.getGlobalScale());
				mapScreen.boardMenu.addAndSelectSound(sound, nx, ny);
				return PointerState.ON_BOARD_SOUND;
			}
			return current;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (Math.abs(lastPointerDeltaY) > 10) {
				soundMenuAnimation = new Translation2DAnimation(this, 1000, 0, 0, lastPointerDeltaY * 20);
				soundMenuAnimation.setInterpolation(new DecelerateInterpolator());
				mapScreen.addAnimation(soundMenuAnimation);
			} else if (System.currentTimeMillis() - lastPointerDownTime < 300) {
				if (soundIndex > -1) {
					Sound sound = availableSounds.get(soundIndex);
					DeviceUtil.playSound(sound);
				}
			}
			return PointerState.NONE;
		}
		return PointerState.NONE;
	}
}
