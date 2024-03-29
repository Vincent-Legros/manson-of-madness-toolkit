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

import org.amphiprion.mansionofmadness.dto.CardPileInstance;
import org.amphiprion.mansionofmadness.dto.Entity.DbState;
import org.amphiprion.mansionofmadness.dto.Scenario;

import android.content.Context;
import android.database.Cursor;

/**
 * This DAO is used to manage cardPileInstance entities.
 * 
 * @author ng00124c
 * 
 */
public class CardPileInstanceDao extends AbstractDao {

	/** The singleton. */
	private static CardPileInstanceDao instance;

	/**
	 * Hidden constructor.
	 * 
	 * @param context
	 *            the application context
	 */
	private CardPileInstanceDao(Context context) {
		super(context);
	}

	/**
	 * Return the singleton.
	 * 
	 * @param context
	 *            the application context
	 * @return the singleton
	 */
	public static CardPileInstanceDao getInstance(Context context) {
		if (instance == null) {
			instance = new CardPileInstanceDao(context);
		}
		return instance;
	}

	/**
	 * 
	 * @return all existing cardPileInstance
	 */
	public List<CardPileInstance> getCardPileInstances(String scenarioId) {

		String sql = "SELECT " + CardPileInstance.DbField.ID + "," + CardPileInstance.DbField.SCENARIO_ID + "," + CardPileInstance.DbField.POS_X + ","
				+ CardPileInstance.DbField.POS_Y + " from CARD_PILE_INSTANCE WHERE " + CardPileInstance.DbField.SCENARIO_ID + "=?";

		Cursor cursor = getDatabase().rawQuery(sql, new String[] { scenarioId });
		ArrayList<CardPileInstance> result = new ArrayList<CardPileInstance>();
		if (cursor.moveToFirst()) {
			do {
				CardPileInstance entity = new CardPileInstance(cursor.getString(0));
				entity.setScenario(new Scenario(cursor.getString(1)));
				entity.setPosX(cursor.getInt(2));
				entity.setPosY(cursor.getInt(3));
				result.add(entity);
			} while (cursor.moveToNext());
		}
		cursor.close();

		return result;
	}

	/**
	 * Persist a new CardPileInstance.
	 * 
	 * @param entity
	 *            the new CardPileInstance
	 */
	private void create(CardPileInstance entity) {
		getDatabase().beginTransaction();
		try {
			String sql = "insert into CARD_PILE_INSTANCE (" + CardPileInstance.DbField.ID + "," + CardPileInstance.DbField.SCENARIO_ID + "," + CardPileInstance.DbField.POS_X + ","
					+ CardPileInstance.DbField.POS_Y + ") values (?,?,?,?)";
			Object[] params = new Object[4];
			params[0] = entity.getId();
			params[1] = entity.getScenario().getId();
			params[2] = entity.getPosX();
			params[3] = entity.getPosY();

			execSQL(sql, params);

			getDatabase().setTransactionSuccessful();
			entity.setState(DbState.LOADED);

		} finally {
			getDatabase().endTransaction();
		}
	}

	private void update(CardPileInstance entity) {
		String sql = "update CARD_PILE_INSTANCE set " + CardPileInstance.DbField.SCENARIO_ID + "=?," + CardPileInstance.DbField.POS_X + "=?," + CardPileInstance.DbField.POS_Y
				+ "=? WHERE " + CardPileInstance.DbField.ID + "=?";
		Object[] params = new Object[4];
		params[0] = entity.getScenario().getId();
		params[1] = entity.getPosX();
		params[2] = entity.getPosY();
		params[3] = entity.getId();

		execSQL(sql, params);

	}

	private void delete(CardPileInstance entity) {
		// delete children
		CardPileCardDao.getInstance(context).deleteAll(entity);

		// delete the instance
		String sql = "delete FROM CARD_PILE_INSTANCE WHERE " + CardPileInstance.DbField.ID + "=?";
		Object[] params = new Object[1];
		params[0] = entity.getId();

		execSQL(sql, params);

	}

	/**
	 * Persist the entity. Depending its state, this method will perform an
	 * insert or an update.
	 * 
	 * @param entity
	 *            the CardPileInstance to persist
	 */
	public void persist(CardPileInstance entity) {
		if (entity.getState() == DbState.NEW) {
			create(entity);
		} else if (entity.getState() == DbState.LOADED) {
			update(entity);
		} else if (entity.getState() == DbState.DELETE) {
			delete(entity);
		}
	}

}
