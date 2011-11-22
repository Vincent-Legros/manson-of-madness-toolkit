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

import org.amphiprion.mansionofmadness.ApplicationConstants;
import org.amphiprion.mansionofmadness.dto.Card;
import org.amphiprion.mansionofmadness.dto.Entity.DbState;

import android.content.Context;
import android.database.Cursor;

/**
 * This DAO is used to manage card entities.
 * 
 * @author ng00124c
 * 
 */
public class CardDao extends AbstractDao {

	/** The singleton. */
	private static CardDao instance;

	/**
	 * Hidden constructor.
	 * 
	 * @param context
	 *            the application context
	 */
	private CardDao(Context context) {
		super(context);
	}

	/**
	 * Return the singleton.
	 * 
	 * @param context
	 *            the application context
	 * @return the singleton
	 */
	public static CardDao getInstance(Context context) {
		if (instance == null) {
			instance = new CardDao(context);
		}
		return instance;
	}

	/**
	 * Return the given Card.
	 * 
	 * @param id
	 *            the unique identifier
	 * @return the card or null if not exists
	 */
	public Card getCard(String id) {

		String sql = "SELECT " + Card.DbField.ID + "," + Card.DbField.NAME + "," + Card.DbField.TYPE + "," + Card.DbField.IS_EMBEDDED + " from CARD where " + Card.DbField.ID
				+ "=?";

		Cursor cursor = getDatabase().rawQuery(sql, new String[] { id });
		Card result = null;
		if (cursor.moveToFirst()) {
			Card entity = new Card(cursor.getString(0));
			entity.setName(cursor.getString(1));
			entity.setType(cursor.getString(2));
			entity.setEmbedded(cursor.getInt(3) != 0);
			updateDisplayName(entity);
			result = entity;
		}
		cursor.close();
		return result;
	}

	public List<Card> getCards() {
		return getCards("");
	}

	public List<Card> getCards(int pageIndex, int pageSize, boolean addEmbedded) {
		String addOn = null;
		if (addEmbedded) {
			addOn = " order by " + Card.DbField.TYPE + "," + Card.DbField.NAME + " limit " + (pageSize + 1) + " offset " + pageIndex * pageSize;
		} else {
			addOn = " where " + Card.DbField.IS_EMBEDDED + "=0 order by " + Card.DbField.TYPE + "," + Card.DbField.NAME + " limit " + (pageSize + 1) + " offset " + pageIndex
					* pageSize;
		}
		return getCards(addOn);
	}

	private List<Card> getCards(String sqlAddon) {
		String sql = "SELECT " + Card.DbField.ID + "," + Card.DbField.NAME + "," + Card.DbField.TYPE + "," + Card.DbField.IS_EMBEDDED + " from CARD ";
		sql += sqlAddon;

		Cursor cursor = getDatabase().rawQuery(sql, null);
		ArrayList<Card> result = new ArrayList<Card>();
		if (cursor.moveToFirst()) {
			do {
				Card entity = new Card(cursor.getString(0));
				entity.setName(cursor.getString(1));
				entity.setType(cursor.getString(2));
				entity.setEmbedded(cursor.getInt(3) != 0);
				updateDisplayName(entity);
				result.add(entity);
			} while (cursor.moveToNext());
		}
		cursor.close();

		return result;
	}

	/**
	 * Persist a new Card.
	 * 
	 * @param entity
	 *            the new Card
	 */
	private void create(Card entity) {
		getDatabase().beginTransaction();
		try {
			String sql = "insert into CARD (" + Card.DbField.ID + "," + Card.DbField.NAME + "," + Card.DbField.TYPE + "," + Card.DbField.IS_EMBEDDED + ") values (?,?,?,?)";
			Object[] params = new Object[4];
			params[0] = entity.getId();
			params[1] = entity.getName();
			params[2] = entity.getType();
			params[3] = entity.isEmbedded() ? "1" : "0";

			execSQL(sql, params);

			getDatabase().setTransactionSuccessful();
			entity.setState(DbState.LOADED);

		} finally {
			getDatabase().endTransaction();
		}
	}

	private void update(Card entity) {
		String sql = "update CARD set " + Card.DbField.NAME + "=?," + Card.DbField.TYPE + "=? WHERE " + Card.DbField.ID + "=?";
		Object[] params = new Object[3];
		params[0] = entity.getName();
		params[1] = entity.getType();
		params[2] = entity.getId();

		execSQL(sql, params);

	}

	/**
	 * Persist the entity. Depending its state, this method will perform an
	 * insert or an update.
	 * 
	 * @param entity
	 *            the Card to persist
	 */
	public void persist(Card entity) {
		if (entity.getState() == DbState.NEW) {
			create(entity);
		} else if (entity.getState() == DbState.LOADED) {
			update(entity);
		}
	}

	private void updateDisplayName(Card card) {
		if (card.isEmbedded()) {
			card.setDisplayName(context.getString(context.getResources().getIdentifier(card.getType() + "_" + card.getName(), "string", ApplicationConstants.PACKAGE)));
		}

	}
}
