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
import org.amphiprion.mansionofmadness.dto.Entity.DbState;
import org.amphiprion.mansionofmadness.dto.RandomCardPileCard;
import org.amphiprion.mansionofmadness.dto.Scenario;

import android.content.Context;
import android.database.Cursor;

/**
 * This DAO is used to manage cardPileCard entities.
 * 
 * @author ng00124c
 * 
 */
public class RandomCardPileCardDao extends AbstractDao {

	/** The singleton. */
	private static RandomCardPileCardDao instance;

	/**
	 * Hidden constructor.
	 * 
	 * @param context
	 *            the application context
	 */
	private RandomCardPileCardDao(Context context) {
		super(context);
	}

	/**
	 * Return the singleton.
	 * 
	 * @param context
	 *            the application context
	 * @return the singleton
	 */
	public static RandomCardPileCardDao getInstance(Context context) {
		if (instance == null) {
			instance = new RandomCardPileCardDao(context);
		}
		return instance;
	}

	/**
	 * 
	 * @return all existing RandomCardPileCard
	 */
	public List<RandomCardPileCard> getRandomCardPileCards(String scenarioId) {

		String sql = "SELECT " + RandomCardPileCard.DbField.ID + "," + RandomCardPileCard.DbField.SCENARIO_ID + "," + RandomCardPileCard.DbField.CARD_ID + ","
				+ RandomCardPileCard.DbField.POS_ORDER + " from RANDOM_CARD_PILE_CARD WHERE " + RandomCardPileCard.DbField.SCENARIO_ID + "=? order by "
				+ CardPileCard.DbField.POS_ORDER;

		Cursor cursor = getDatabase().rawQuery(sql, new String[] { scenarioId });
		ArrayList<RandomCardPileCard> result = new ArrayList<RandomCardPileCard>();
		if (cursor.moveToFirst()) {
			do {
				RandomCardPileCard entity = new RandomCardPileCard(cursor.getString(0));
				entity.setScenario(new Scenario(cursor.getString(1)));
				entity.setCard(new Card(cursor.getString(2)));
				entity.setOrder(cursor.getInt(3));
				result.add(entity);
			} while (cursor.moveToNext());
		}
		cursor.close();

		return result;
	}

	/**
	 * Persist a new RandomCardPileCard.
	 * 
	 * @param entity
	 *            the new RandomCardPileCard
	 */
	private void create(RandomCardPileCard entity) {
		getDatabase().beginTransaction();
		try {
			String sql = "insert into RANDOM_CARD_PILE_CARD (" + RandomCardPileCard.DbField.ID + "," + RandomCardPileCard.DbField.SCENARIO_ID + ","
					+ RandomCardPileCard.DbField.CARD_ID + "," + RandomCardPileCard.DbField.POS_ORDER + ") values (?,?,?,?)";
			Object[] params = new Object[4];
			params[0] = entity.getId();
			params[1] = entity.getScenario().getId();
			params[2] = entity.getCard().getId();
			params[3] = entity.getOrder();

			execSQL(sql, params);

			getDatabase().setTransactionSuccessful();
			entity.setState(DbState.LOADED);

		} finally {
			getDatabase().endTransaction();
		}
	}

	private void update(RandomCardPileCard entity) {
		String sql = "update RANDOM_CARD_PILE_CARD set " + RandomCardPileCard.DbField.SCENARIO_ID + "=?," + RandomCardPileCard.DbField.CARD_ID + "=?,"
				+ RandomCardPileCard.DbField.POS_ORDER + "=? WHERE " + RandomCardPileCard.DbField.ID + "=?";
		Object[] params = new Object[4];
		params[0] = entity.getScenario().getId();
		params[1] = entity.getCard().getId();
		params[2] = entity.getOrder();
		params[3] = entity.getId();

		execSQL(sql, params);

	}

	/**
	 * Persist the entity. Depending its state, this method will perform an
	 * insert or an update.
	 * 
	 * @param entity
	 *            the RandomCardPileCard to persist
	 */
	public void persist(RandomCardPileCard entity) {
		if (entity.getState() == DbState.NEW) {
			create(entity);
		} else if (entity.getState() == DbState.LOADED) {
			update(entity);
		}
	}

}
