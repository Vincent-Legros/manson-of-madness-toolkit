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

import org.amphiprion.mansionofmadness.dto.Entity.DbState;
import org.amphiprion.mansionofmadness.dto.Scenario;
import org.amphiprion.mansionofmadness.dto.Sound;

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
			result = entity;
		}
		cursor.close();
		return result;
	}

	/**
	 * 
	 * @return all existing Scenario
	 */
	public List<Scenario> getScenarios() {

		String sql = "SELECT " + Scenario.DbField.ID + "," + Scenario.DbField.NAME + "," + Scenario.DbField.IS_EMBEDDED + " from SCENARIO order by " + Sound.DbField.NAME;

		Cursor cursor = getDatabase().rawQuery(sql, null);
		ArrayList<Scenario> result = new ArrayList<Scenario>();
		if (cursor.moveToFirst()) {
			do {
				Scenario entity = new Scenario(cursor.getString(0));
				entity.setName(cursor.getString(1));
				entity.setEmbedded(cursor.getInt(2) != 0);
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
			params[2] = entity.isEmbedded() ? "0" : "1";

			execSQL(sql, params);

			getDatabase().setTransactionSuccessful();
			entity.setState(DbState.LOADED);

		} finally {
			getDatabase().endTransaction();
		}
	}

	private void update(Scenario entity) {
		String sql = "update SCENARIO set " + Scenario.DbField.NAME + "=? WHERE " + Scenario.DbField.ID + "=?";
		Object[] params = new Object[2];
		params[0] = entity.getName();
		params[1] = entity.getId();

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
		}
	}

}
