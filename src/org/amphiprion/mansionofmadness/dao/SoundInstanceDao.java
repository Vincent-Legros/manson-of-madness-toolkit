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
import org.amphiprion.mansionofmadness.dto.SoundInstance;

import android.content.Context;
import android.database.Cursor;

/**
 * This DAO is used to manage cardPileInstance entities.
 * 
 * @author ng00124c
 * 
 */
public class SoundInstanceDao extends AbstractDao {

	/** The singleton. */
	private static SoundInstanceDao instance;

	/**
	 * Hidden constructor.
	 * 
	 * @param context
	 *            the application context
	 */
	private SoundInstanceDao(Context context) {
		super(context);
	}

	/**
	 * Return the singleton.
	 * 
	 * @param context
	 *            the application context
	 * @return the singleton
	 */
	public static SoundInstanceDao getInstance(Context context) {
		if (instance == null) {
			instance = new SoundInstanceDao(context);
		}
		return instance;
	}

	/**
	 * 
	 * @return all existing SoundInstance
	 */
	public List<SoundInstance> getSoundInstances(String scenarioId) {

		String sql = "SELECT " + SoundInstance.DbField.ID + "," + SoundInstance.DbField.SCENARIO_ID + "," + SoundInstance.DbField.POS_X + "," + SoundInstance.DbField.POS_Y + ","
				+ SoundInstance.DbField.SOUND_ID + " from SOUND_INSTANCE WHERE " + SoundInstance.DbField.SCENARIO_ID + "=?";

		Cursor cursor = getDatabase().rawQuery(sql, new String[] { scenarioId });
		ArrayList<SoundInstance> result = new ArrayList<SoundInstance>();
		if (cursor.moveToFirst()) {
			do {
				SoundInstance entity = new SoundInstance(cursor.getString(0));
				entity.setScenario(new Scenario(cursor.getString(1)));
				entity.setPosX(cursor.getInt(2));
				entity.setPosY(cursor.getInt(3));
				entity.setSound(new Sound(cursor.getString(4)));
				result.add(entity);
			} while (cursor.moveToNext());
		}
		cursor.close();

		return result;
	}

	/**
	 * Persist a new SoundInstance.
	 * 
	 * @param entity
	 *            the new SoundInstance
	 */
	private void create(SoundInstance entity) {
		getDatabase().beginTransaction();
		try {
			String sql = "insert into SOUND_INSTANCE (" + SoundInstance.DbField.ID + "," + SoundInstance.DbField.SCENARIO_ID + "," + SoundInstance.DbField.POS_X + ","
					+ SoundInstance.DbField.POS_Y + "," + SoundInstance.DbField.SOUND_ID + ") values (?,?,?,?,?)";
			Object[] params = new Object[5];
			params[0] = entity.getId();
			params[1] = entity.getScenario().getId();
			params[2] = entity.getPosX();
			params[3] = entity.getPosY();
			params[4] = entity.getSound().getId();

			execSQL(sql, params);

			getDatabase().setTransactionSuccessful();
			entity.setState(DbState.LOADED);

		} finally {
			getDatabase().endTransaction();
		}
	}

	private void update(SoundInstance entity) {
		String sql = "update SOUND_INSTANCE set " + SoundInstance.DbField.SCENARIO_ID + "=?," + SoundInstance.DbField.POS_X + "=?," + SoundInstance.DbField.POS_Y + "=?,"
				+ SoundInstance.DbField.SOUND_ID + "=? WHERE " + SoundInstance.DbField.ID + "=?";
		Object[] params = new Object[5];
		params[0] = entity.getScenario().getId();
		params[1] = entity.getPosX();
		params[2] = entity.getPosY();
		params[3] = entity.getSound().getId();
		params[4] = entity.getId();

		execSQL(sql, params);

	}

	private void delete(SoundInstance entity) {
		String sql = "delete FROM SOUND_INSTANCE WHERE " + SoundInstance.DbField.ID + "=?";
		Object[] params = new Object[1];
		params[0] = entity.getId();

		execSQL(sql, params);

	}

	/**
	 * Persist the entity. Depending its state, this method will perform an
	 * insert or an update.
	 * 
	 * @param entity
	 *            the SoundInstance to persist
	 */
	public void persist(SoundInstance entity) {
		if (entity.getState() == DbState.NEW) {
			create(entity);
		} else if (entity.getState() == DbState.LOADED) {
			update(entity);
		} else if (entity.getState() == DbState.DELETE) {
			delete(entity);
		}
	}

}
