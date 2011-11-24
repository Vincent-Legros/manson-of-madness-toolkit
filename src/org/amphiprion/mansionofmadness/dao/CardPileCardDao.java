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
package org.amphiprion.mansionofmadness.dao;

import java.util.ArrayList;
import java.util.List;

import org.amphiprion.mansionofmadness.dto.Card;
import org.amphiprion.mansionofmadness.dto.CardPileCard;
import org.amphiprion.mansionofmadness.dto.CardPileInstance;
import org.amphiprion.mansionofmadness.dto.Entity.DbState;

import android.content.Context;
import android.database.Cursor;

/**
 * This DAO is used to manage cardPileCard entities.
 * 
 * @author ng00124c
 * 
 */
public class CardPileCardDao extends AbstractDao {

	/** The singleton. */
	private static CardPileCardDao instance;

	/**
	 * Hidden constructor.
	 * 
	 * @param context
	 *            the application context
	 */
	private CardPileCardDao(Context context) {
		super(context);
	}

	/**
	 * Return the singleton.
	 * 
	 * @param context
	 *            the application context
	 * @return the singleton
	 */
	public static CardPileCardDao getInstance(Context context) {
		if (instance == null) {
			instance = new CardPileCardDao(context);
		}
		return instance;
	}

	/**
	 * 
	 * @return all existing cardPileCard
	 */
	public List<CardPileCard> getCardPileCards(String cardPlieInstanceId) {

		String sql = "SELECT " + CardPileCard.DbField.ID + "," + CardPileCard.DbField.CARD_PILE_INSTANCE_ID + "," + CardPileCard.DbField.CARD_ID + ","
				+ CardPileCard.DbField.POS_ORDER + "," + CardPileCard.DbField.IS_DISCARDED + "," + CardPileCard.DbField.IS_TEMP + " from CARD_PILE_CARD WHERE "
				+ CardPileCard.DbField.CARD_PILE_INSTANCE_ID + "=? order by " + CardPileCard.DbField.POS_ORDER;

		Cursor cursor = getDatabase().rawQuery(sql, new String[] { cardPlieInstanceId });
		ArrayList<CardPileCard> result = new ArrayList<CardPileCard>();
		if (cursor.moveToFirst()) {
			do {
				CardPileCard entity = new CardPileCard(cursor.getString(0));
				entity.setCardPileInstance(new CardPileInstance(cursor.getString(1)));
				entity.setCard(new Card(cursor.getString(2)));
				entity.setOrder(cursor.getInt(3));
				entity.setDiscarded(cursor.getInt(4) != 0);
				entity.setTemporary(cursor.getInt(5) != 0);
				result.add(entity);
			} while (cursor.moveToNext());
		}
		cursor.close();

		return result;
	}

	/**
	 * Persist a new CardPileCard.
	 * 
	 * @param entity
	 *            the new CardPileCard
	 */
	private void create(CardPileCard entity) {
		getDatabase().beginTransaction();
		try {
			String sql = "insert into CARD_PILE_CARD (" + CardPileCard.DbField.ID + "," + CardPileCard.DbField.CARD_PILE_INSTANCE_ID + "," + CardPileCard.DbField.CARD_ID + ","
					+ CardPileCard.DbField.POS_ORDER + "," + CardPileCard.DbField.IS_DISCARDED + "," + CardPileCard.DbField.IS_TEMP + ") values (?,?,?,?,?,?)";
			Object[] params = new Object[6];
			params[0] = entity.getId();
			params[1] = entity.getCardPileInstance().getId();
			params[2] = entity.getCard().getId();
			params[3] = entity.getOrder();
			params[4] = entity.isDiscarded() ? "1" : "0";
			params[5] = entity.isTemporary() ? "1" : "0";

			execSQL(sql, params);

			getDatabase().setTransactionSuccessful();
			entity.setState(DbState.LOADED);

		} finally {
			getDatabase().endTransaction();
		}
	}

	private void update(CardPileCard entity) {
		String sql = "update CARD_PILE_CARD set " + CardPileCard.DbField.CARD_PILE_INSTANCE_ID + "=?," + CardPileCard.DbField.CARD_ID + "=?," + CardPileCard.DbField.POS_ORDER
				+ "=?," + CardPileCard.DbField.IS_DISCARDED + "=?," + CardPileCard.DbField.IS_TEMP + "=? WHERE " + CardPileCard.DbField.ID + "=?";
		Object[] params = new Object[6];
		params[0] = entity.getCardPileInstance().getId();
		params[1] = entity.getCard().getId();
		params[2] = entity.getOrder();
		params[3] = entity.isDiscarded() ? "1" : "0";
		params[4] = entity.isTemporary() ? "1" : "0";
		params[5] = entity.getId();

		execSQL(sql, params);

	}

	/**
	 * Persist the entity. Depending its state, this method will perform an
	 * insert or an update.
	 * 
	 * @param entity
	 *            the CardPileCard to persist
	 */
	public void persist(CardPileCard entity) {
		if (entity.getState() == DbState.NEW) {
			create(entity);
		} else if (entity.getState() == DbState.LOADED) {
			update(entity);
		}
	}

	/**
	 * @param entity
	 */
	public void deleteAll(CardPileInstance entity) {
		String sql = "delete FROM CARD_PILE_CARD WHERE " + CardPileCard.DbField.CARD_PILE_INSTANCE_ID + "=?";

		Object[] params = new Object[1];
		params[0] = entity.getId();

		execSQL(sql, params);
	}

}
