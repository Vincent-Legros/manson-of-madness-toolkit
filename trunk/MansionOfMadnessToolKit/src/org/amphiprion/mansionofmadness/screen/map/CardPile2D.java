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

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import org.amphiprion.gameengine3d.mesh.Image2D;
import org.amphiprion.mansionofmadness.dto.Card;

/**
 * @author Amphiprion
 * 
 */
public class CardPile2D extends Image2D {
	private List<Card> cards = new ArrayList<Card>();
	private Image2D imgPileSize;

	public CardPile2D() {
		// super("cards/back_exploration.png");
		super("cards/card_pile.png");
		imgPileSize = new Image2D("@String/0");
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
		imgPileSize.x = x;
		imgPileSize.y = y;
		imgPileSize.draw(gl, screenScale, screenWidth, screenHeight);
	}

	public void addCard(Card card) {
		cards.add(card);
		computeImage();
	}

	public void removeCard(Card card) {
		cards.remove(card);
		computeImage();
	}

	private void computeImage() {
		if (cards.size() == 0) {
			changeUri("cards/card_pile.png");
		} else {
			Card card = cards.get(cards.size() - 1);
			changeUri("cards/back_" + card.getType() + ".png");
		}
		imgPileSize.changeUri("@String/" + cards.size());
	}
}
