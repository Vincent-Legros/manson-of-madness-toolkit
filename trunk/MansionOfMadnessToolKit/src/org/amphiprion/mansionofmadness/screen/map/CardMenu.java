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
import org.amphiprion.mansionofmadness.ApplicationConstants;
import org.amphiprion.mansionofmadness.dao.CardDao;
import org.amphiprion.mansionofmadness.dto.Card;

import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;

/**
 * @author ng00124c
 * 
 */
public class CardMenu extends TouchableGroup2D {
	public static int HEIGHT = 100;

	private int lastPointerX;
	private int lastPointerY;
	private int lastPointerDeltaX;
	private int lastPointerDeltaY;
	private long lastPointerDownTime;

	private Image2D background;
	private MapScreen mapScreen;
	private Translation2DAnimation cardMenuAnimation;
	private int cardIndex = -1;
	private List<Card> availableCards;

	/**
	 * 
	 */
	public CardMenu(MapScreen mapScreen, String backgroundUri) {
		this.mapScreen = mapScreen;
		setX(ComponentTab.WIDTH / 2);
		setY(800 / 2);

		background = new Image2D(backgroundUri, false, true);
		background.x = ComponentTab.WIDTH / 2;
		background.y = 800 / 2;
		background.setScale(10);
		addObject(background);

		availableCards = CardDao.getInstance(mapScreen.getContext()).getCards();
		int index = 0;
		for (Card card : availableCards) {
			Image2D img = new Image2D("cards/back_" + card.getType() + ".png");
			// rescale to enter in a 150x150 pixel (a case have a size of
			// 150x150pixel)

			img.x = ComponentTab.WIDTH / 2;
			img.y = index * CardMenu.HEIGHT + CardMenu.HEIGHT / 2;

			addObject(img);
			String txt;
			if (card.isEmbedded()) {
				txt = mapScreen.getContext().getString(
						mapScreen.getContext().getResources().getIdentifier(card.getType() + "_" + card.getName(), "string", ApplicationConstants.PACKAGE));
			} else {
				txt = card.getName();
			}
			Image2D imgTxt = new Image2D("@String/" + txt);
			imgTxt.x = ComponentTab.WIDTH / 2;
			imgTxt.y = index * CardMenu.HEIGHT + CardMenu.HEIGHT / 2 + 45;
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
				cardIndex = (800 / 2 - getY() + ny) / CardMenu.HEIGHT;
				if (cardIndex < 0 || cardIndex >= availableCards.size()) {
					cardIndex = -1;
				}
				mapScreen.removeAnimation(cardMenuAnimation);
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
			if (nx >= ComponentTab.WIDTH && cardIndex > -1) {
				// mapScreen.collapseTileMenu();
				// Card2D card = new Card2D(availableCards.get(cardIndex));
				// card.x = (int) (nx / mapScreen.boardMenu.getGlobalScale());
				// card.y = (int) (ny / mapScreen.boardMenu.getGlobalScale());
				// mapScreen.boardMenu.addAndSelectSound(sound, nx, ny);
				// return PointerState.ON_BOARD_SOUND;
			}
			return current;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (Math.abs(lastPointerDeltaY) > 10) {
				cardMenuAnimation = new Translation2DAnimation(this, 1000, 0, 0, lastPointerDeltaY * 20);
				cardMenuAnimation.setInterpolation(new DecelerateInterpolator());
				mapScreen.addAnimation(cardMenuAnimation);
			} else if (System.currentTimeMillis() - lastPointerDownTime < 300) {
				if (cardIndex > -1) {
					// Sound sound = availableCards.get(cardIndex);
					// DeviceUtil.playSound(sound);
				}
			}
			return PointerState.NONE;
		}
		return PointerState.NONE;
	}
}
