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
package org.amphiprion.mansionofmadness.dto;

public class CardPileCard extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum DbField {
		ID, CARD_PILE_INSTANCE_ID, CARD_ID, POS_ORDER
	}

	private CardPileInstance cardPileInstance;
	private Card card;
	private int order;

	/**
	 * 
	 */
	public CardPileCard() {
		super();
	}

	public CardPileCard(String id) {
		super(id);
	}

	/**
	 * @return the cardPileInstance
	 */
	public CardPileInstance getCardPileInstance() {
		return cardPileInstance;
	}

	/**
	 * @param cardPileInstance
	 *            the cardPileInstance to set
	 */
	public void setCardPileInstance(CardPileInstance cardPileInstance) {
		this.cardPileInstance = cardPileInstance;
	}

	/**
	 * @return the card
	 */
	public Card getCard() {
		return card;
	}

	/**
	 * @param card
	 *            the card to set
	 */
	public void setCard(Card card) {
		this.card = card;
	}

	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @param order
	 *            the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}

}
