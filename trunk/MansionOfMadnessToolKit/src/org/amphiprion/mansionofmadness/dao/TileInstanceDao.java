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
import org.amphiprion.mansionofmadness.dto.Tile;
import org.amphiprion.mansionofmadness.dto.TileInstance;

import android.content.Context;
import android.database.Cursor;

/**
 * This DAO is used to manage TileInstance entities.
 * 
 * @author ng00124c
 * 
 */
public class TileInstanceDao extends AbstractDao {

	/** The singleton. */
	private static TileInstanceDao instance;

	/**
	 * Hidden constructor.
	 * 
	 * @param context
	 *            the application context
	 */
	private TileInstanceDao(Context context) {
		super(context);
	}

	/**
	 * Return the singleton.
	 * 
	 * @param context
	 *            the application context
	 * @return the singleton
	 */
	public static TileInstanceDao getInstance(Context context) {
		if (instance == null) {
			instance = new TileInstanceDao(context);
		}
		return instance;
	}

	/**
	 * 
	 * @return all existing TileInstance
	 */
	public List<TileInstance> getTileInstances(String scenarioId) {

		String sql = "SELECT " + TileInstance.DbField.ID + "," + TileInstance.DbField.SCENARIO_ID + "," + TileInstance.DbField.POS_X + "," + TileInstance.DbField.POS_Y + ","
				+ TileInstance.DbField.TILE_ID + "," + TileInstance.DbField.ROTATION + " from TILE_INSTANCE WHERE " + TileInstance.DbField.SCENARIO_ID + "=?";

		Cursor cursor = getDatabase().rawQuery(sql, new String[] { scenarioId });
		ArrayList<TileInstance> result = new ArrayList<TileInstance>();
		if (cursor.moveToFirst()) {
			do {
				TileInstance entity = new TileInstance(cursor.getString(0));
				entity.setScenario(new Scenario(cursor.getString(1)));
				entity.setPosX(cursor.getInt(2));
				entity.setPosY(cursor.getInt(3));
				entity.setTile(new Tile(cursor.getString(4)));
				entity.setRotation(cursor.getInt(5));
				result.add(entity);
			} while (cursor.moveToNext());
		}
		cursor.close();

		return result;
	}

	/**
	 * Persist a new TileInstance.
	 * 
	 * @param entity
	 *            the new TileInstance
	 */
	private void create(TileInstance entity) {
		getDatabase().beginTransaction();
		try {
			String sql = "insert into TILE_INSTANCE (" + TileInstance.DbField.ID + "," + TileInstance.DbField.SCENARIO_ID + "," + TileInstance.DbField.POS_X + ","
					+ TileInstance.DbField.POS_Y + "," + TileInstance.DbField.TILE_ID + "," + TileInstance.DbField.ROTATION + ") values (?,?,?,?,?,?)";
			Object[] params = new Object[6];
			params[0] = entity.getId();
			params[1] = entity.getScenario().getId();
			params[2] = entity.getPosX();
			params[3] = entity.getPosY();
			params[4] = entity.getTile().getId();
			params[5] = entity.getRotation();

			execSQL(sql, params);

			getDatabase().setTransactionSuccessful();
			entity.setState(DbState.LOADED);

		} finally {
			getDatabase().endTransaction();
		}
	}

	private void update(TileInstance entity) {
		String sql = "update TILE_INSTANCE set " + TileInstance.DbField.SCENARIO_ID + "=?," + TileInstance.DbField.POS_X + "=?," + TileInstance.DbField.POS_Y + "=?,"
				+ TileInstance.DbField.TILE_ID + "=?," + TileInstance.DbField.ROTATION + "=? WHERE " + TileInstance.DbField.ID + "=?";
		Object[] params = new Object[6];
		params[0] = entity.getScenario().getId();
		params[1] = entity.getPosX();
		params[2] = entity.getPosY();
		params[3] = entity.getTile().getId();
		params[4] = entity.getRotation();
		params[5] = entity.getId();

		execSQL(sql, params);

	}

	private void delete(TileInstance entity) {
		String sql = "delete FROM TILE_INSTANCE WHERE " + TileInstance.DbField.ID + "=?";
		Object[] params = new Object[1];
		params[0] = entity.getId();

		execSQL(sql, params);

	}

	/**
	 * Persist the entity. Depending its state, this method will perform an
	 * insert or an update.
	 * 
	 * @param entity
	 *            the TileInstance to persist
	 */
	public void persist(TileInstance entity) {
		if (entity.getState() == DbState.NEW) {
			create(entity);
		} else if (entity.getState() == DbState.LOADED) {
			update(entity);
		} else if (entity.getState() == DbState.DELETE) {
			delete(entity);
		}
	}

}
