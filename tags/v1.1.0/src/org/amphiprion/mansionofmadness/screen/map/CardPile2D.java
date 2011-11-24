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
import org.amphiprion.mansionofmadness.dao.CardPileCardDao;
import org.amphiprion.mansionofmadness.dto.Card;
import org.amphiprion.mansionofmadness.dto.CardPileCard;
import org.amphiprion.mansionofmadness.dto.CardPileInstance;

/**
 * @author Amphiprion
 * 
 */
public class CardPile2D extends Image2D implements ICardPile {
	private List<CardPileCard> cardPilesCards = new ArrayList<CardPileCard>();
	private Text2D imgPileSize;
	private CardPileInstance cardPileInstance;

	public CardPile2D(CardPileInstance cardPileInstance) {
		// super("cards/back_exploration.png");
		super("cards/card_pile.png");
		imgPileSize = new Text2D("0", 20);
		this.cardPileInstance = cardPileInstance;
	}

	/**
	 * @return the cardPileInstance
	 */
	public CardPileInstance getCardPileInstance() {
		return cardPileInstance;
	}

	/**
	 * @return the cards
	 */
	@Override
	public List<Card> getCards() {
		List<Card> cards = new ArrayList<Card>();
		for (CardPileCard cpc : cardPilesCards) {
			cards.add(cpc.getCard());
		}
		return cards;
	}

	public int count() {
		return cardPilesCards.size();
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
		imgPileSize.setGlobalScale(getGlobalScale());
		imgPileSize.draw(gl, screenScale, screenWidth, screenHeight);
	}

	public void addCard(CardPileCard card) {
		cardPilesCards.add(card);
		computeImage();
	}

	public void removeCard(CardPileCard card) {
		cardPilesCards.remove(card);
		computeImage();
	}

	private void computeImage() {
		if (cardPilesCards.size() == 0) {
			changeUri("cards/card_pile.png");
		} else {
			Card card = cardPilesCards.get(cardPilesCards.size() - 1).getCard();
			changeUri("cards/back_" + card.getType() + ".png");
		}
		imgPileSize.setText("" + cardPilesCards.size());
	}

	/**
	 * @param _selection
	 *            the selection start with top card and end with bottom card
	 */
	@Override
	public void removeSelection(MapScreen mapScreen, boolean[] _selection) {
		int index = cardPilesCards.size() - 1;
		for (int i = 0; i < _selection.length; i++) {
			if (_selection[i]) {
				if (!mapScreen.inEdition) {
					CardPileCard cpc = cardPilesCards.get(index);
					cpc.setDiscarded(true);
					CardPileCardDao.getInstance(mapScreen.getContext()).persist(cpc);
				}
				cardPilesCards.remove(index);

			}
			index--;
		}
		computeImage();

	}
}
