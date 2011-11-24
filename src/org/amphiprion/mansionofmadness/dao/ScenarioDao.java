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
import org.amphiprion.mansionofmadness.dto.CardPileCard;
import org.amphiprion.mansionofmadness.dto.CardPileInstance;
import org.amphiprion.mansionofmadness.dto.Entity.DbState;
import org.amphiprion.mansionofmadness.dto.RandomCardPileCard;
import org.amphiprion.mansionofmadness.dto.Scenario;
import org.amphiprion.mansionofmadness.dto.SoundInstance;
import org.amphiprion.mansionofmadness.dto.TileInstance;

import android.content.Context;
import android.database.Cursor;

/**
 * This DAO is used to manage Scenario entities.
 * 
 * @author ng00124c
 * 
 */
public class ScenarioDao extends AbstractDao {

	/** The singleton. */
	private static ScenarioDao instance;

	/**
	 * Hidden constructor.
	 * 
	 * @param context
	 *            the application context
	 */
	private ScenarioDao(Context context) {
		super(context);
	}

	/**
	 * Return the singleton.
	 * 
	 * @param context
	 *            the application context
	 * @return the singleton
	 */
	public static ScenarioDao getInstance(Context context) {
		if (instance == null) {
			instance = new ScenarioDao(context);
		}
		return instance;
	}

	/**
	 * Return the given Scenario.
	 * 
	 * @param id
	 *            the unique identifier
	 * @return the Scenario or null if not exists
	 */
	public Scenario getScenario(String id) {

		String sql = "SELECT " + Scenario.DbField.ID + "," + Scenario.DbField.NAME + "," + Scenario.DbField.IS_EMBEDDED + " from SCENARIO where " + Scenario.DbField.ID + "=?";

		Cursor cursor = getDatabase().rawQuery(sql, new String[] { id });
		Scenario result = null;
		if (cursor.moveToFirst()) {
			Scenario entity = new Scenario(cursor.getString(0));
			entity.setName(cursor.getString(1));
			entity.setEmbedded(cursor.getInt(2) != 0);
			updateDisplayName(entity);
			result = entity;
		}
		cursor.close();
		return result;
	}

	/**
	 * 
	 * @return all existing Scenario
	 */
	public List<Scenario> getScenarios(int pageIndex, int pageSize) {

		String sql = "SELECT " + Scenario.DbField.ID + "," + Scenario.DbField.NAME + "," + Scenario.DbField.IS_EMBEDDED + " from SCENARIO";
		sql += " order by " + Scenario.DbField.NAME + " asc limit " + (pageSize + 1) + " offset " + pageIndex * pageSize;

		Cursor cursor = getDatabase().rawQuery(sql, null);
		ArrayList<Scenario> result = new ArrayList<Scenario>();
		if (cursor.moveToFirst()) {
			do {
				Scenario entity = new Scenario(cursor.getString(0));
				entity.setName(cursor.getString(1));
				entity.setEmbedded(cursor.getInt(2) != 0);
				updateDisplayName(entity);
				result.add(entity);
			} while (cursor.moveToNext());
		}
		cursor.close();

		return result;
	}

	/**
	 * Persist a new Scenario.
	 * 
	 * @param entity
	 *            the new Scenario
	 */
	private void create(Scenario entity) {
		getDatabase().beginTransaction();
		try {
			String sql = "insert into SCENARIO (" + Scenario.DbField.ID + "," + Scenario.DbField.NAME + "," + Scenario.DbField.IS_EMBEDDED + ") values (?,?,?)";
			Object[] params = new Object[3];
			params[0] = entity.getId();
			params[1] = entity.getName();
			params[2] = entity.isEmbedded() ? "1" : "0";

			execSQL(sql, params);

			getDatabase().setTransactionSuccessful();
			entity.setState(DbState.LOADED);

		} finally {
			getDatabase().endTransaction();
		}
	}

	private void update(Scenario entity) {
		String sql = "update SCENARIO set " + Scenario.DbField.NAME + "=?," + Scenario.DbField.IS_EMBEDDED + "=? WHERE " + Scenario.DbField.ID + "=?";
		Object[] params = new Object[3];
		params[0] = entity.getName();
		params[1] = entity.isEmbedded() ? "1" : "0";
		params[2] = entity.getId();

		execSQL(sql, params);

	}

	/**
	 * Persist the entity. Depending its state, this method will perform an
	 * insert or an update.
	 * 
	 * @param entity
	 *            the Scenario to persist
	 */
	public void persist(Scenario entity) {
		if (entity.getState() == DbState.NEW) {
			create(entity);
		} else if (entity.getState() == DbState.LOADED) {
			update(entity);
		} else if (entity.getState() == DbState.DELETE) {
			delete(entity);
		}
	}

	private void updateDisplayName(Scenario scenario) {
		if (scenario.isEmbedded()) {
			scenario.setDisplayName(getContext().getText(getContext().getResources().getIdentifier("scenario_" + scenario.getName(), "string", ApplicationConstants.PACKAGE))
					.toString());
		}

	}

	/**
	 * @param currentScenario
	 * @return
	 */
	public boolean isGameInProgress(Scenario scenario) {
		boolean inProgress = false;

		String sql = "SELECT 1 FROM CARD_PILE_CARD c WHERE c." + CardPileCard.DbField.IS_DISCARDED + "=1 AND c." + CardPileCard.DbField.CARD_PILE_INSTANCE_ID + " in (";
		sql += "SELECT i." + CardPileInstance.DbField.ID + " from CARD_PILE_INSTANCE i WHERE i." + CardPileInstance.DbField.SCENARIO_ID + "=?";
		sql += ")";
		sql += " limit 1 offset 0";
		Cursor cursor = getDatabase().rawQuery(sql, new String[] { scenario.getId() });
		if (cursor.moveToFirst()) {
			inProgress = true;
		}
		cursor.close();
		return inProgress;
	}

	/**
	 * Call this method before starting a new game session.
	 * 
	 * @param scenario
	 */
	public void initScenario(Scenario scenario) {
		String sql = "DELETE from CARD_PILE_CARD WHERE " + CardPileCard.DbField.IS_TEMP + "=1 AND " + CardPileCard.DbField.CARD_PILE_INSTANCE_ID + " in (";
		sql += "SELECT i." + CardPileInstance.DbField.ID + " from CARD_PILE_INSTANCE i WHERE i." + CardPileInstance.DbField.SCENARIO_ID + "='" + scenario.getId() + "'";
		sql += ")";
		execSQL(sql);

		sql = "UPDATE CARD_PILE_CARD set " + CardPileCard.DbField.IS_DISCARDED + "=0 WHERE " + CardPileCard.DbField.IS_DISCARDED + "=1 AND "
				+ CardPileCard.DbField.CARD_PILE_INSTANCE_ID + " in (";
		sql += "SELECT i." + CardPileInstance.DbField.ID + " from CARD_PILE_INSTANCE i WHERE i." + CardPileInstance.DbField.SCENARIO_ID + "='" + scenario.getId() + "'";
		sql += ")";
		execSQL(sql);
	}

	public void delete(Scenario scenario) {
		String sql = "DELETE from CARD_PILE_CARD WHERE " + CardPileCard.DbField.CARD_PILE_INSTANCE_ID + " in (";
		sql += "SELECT i." + CardPileInstance.DbField.ID + " from CARD_PILE_INSTANCE i WHERE i." + CardPileInstance.DbField.SCENARIO_ID + "=?";
		sql += ")";
		Object[] params = new Object[1];
		params[0] = scenario.getId();
		execSQL(sql, params);

		sql = "DELETE from RANDOM_CARD_PILE_CARD WHERE " + RandomCardPileCard.DbField.SCENARIO_ID + "=?";
		params = new Object[1];
		params[0] = scenario.getId();
		execSQL(sql, params);

		sql = "DELETE from SOUND_INSTANCE WHERE " + SoundInstance.DbField.SCENARIO_ID + "=?";
		params = new Object[1];
		params[0] = scenario.getId();
		execSQL(sql, params);

		sql = "DELETE from CARD_PILE_INSTANCE WHERE " + CardPileInstance.DbField.SCENARIO_ID + "=?";
		params = new Object[1];
		params[0] = scenario.getId();
		execSQL(sql, params);

		sql = "DELETE from TILE_INSTANCE WHERE " + TileInstance.DbField.SCENARIO_ID + "=?";
		params = new Object[1];
		params[0] = scenario.getId();
		execSQL(sql, params);

		sql = "DELETE from SCENARIO WHERE " + Scenario.DbField.ID + "=?";
		params = new Object[1];
		params[0] = scenario.getId();
		execSQL(sql, params);

	}

}
