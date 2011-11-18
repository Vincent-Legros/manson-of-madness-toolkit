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
import org.amphiprion.mansionofmadness.dto.Tile;

import android.content.Context;
import android.database.Cursor;

/**
 * This DAO is used to manage tile entities.
 * 
 * @author ng00124c
 * 
 */
public class TileDao extends AbstractDao {

	/** The singleton. */
	private static TileDao instance;

	/**
	 * Hidden constructor.
	 * 
	 * @param context
	 *            the application context
	 */
	private TileDao(Context context) {
		super(context);
	}

	/**
	 * Return the singleton.
	 * 
	 * @param context
	 *            the application context
	 * @return the singleton
	 */
	public static TileDao getInstance(Context context) {
		if (instance == null) {
			instance = new TileDao(context);
		}
		return instance;
	}

	/**
	 * Return the given Tile.
	 * 
	 * @param id
	 *            the unique identifier
	 * @return the tile or null if not exists
	 */
	public Tile getTile(String id) {

		String sql = "SELECT " + Tile.DbField.ID + "," + Tile.DbField.NAME + "," + Tile.DbField.IMAGE_NAME + "," + Tile.DbField.IS_EMBEDDED + " from TILE where " + Tile.DbField.ID
				+ "=?";

		Cursor cursor = getDatabase().rawQuery(sql, new String[] { id });
		Tile result = null;
		if (cursor.moveToFirst()) {
			Tile entity = new Tile(cursor.getString(0));
			entity.setName(cursor.getString(1));
			entity.setImageName(cursor.getString(2));
			entity.setEmbedded(cursor.getInt(3) != 0);
			result = entity;
		}
		cursor.close();
		return result;
	}

	/**
	 * 
	 * @return all existing tiles
	 */
	public List<Tile> getCardProperties() {

		String sql = "SELECT " + Tile.DbField.ID + "," + Tile.DbField.NAME + "," + Tile.DbField.IMAGE_NAME + "," + Tile.DbField.IS_EMBEDDED + " from TILE order by "
				+ Tile.DbField.NAME;

		Cursor cursor = getDatabase().rawQuery(sql, null);
		ArrayList<Tile> result = new ArrayList<Tile>();
		if (cursor.moveToFirst()) {
			do {
				Tile entity = new Tile(cursor.getString(0));
				entity.setName(cursor.getString(1));
				entity.setImageName(cursor.getString(2));
				entity.setEmbedded(cursor.getInt(3) != 0);
				result.add(entity);
			} while (cursor.moveToNext());
		}
		cursor.close();

		return result;
	}

	/**
	 * Persist a new Tile.
	 * 
	 * @param entity
	 *            the new Tile
	 */
	private void create(Tile entity) {
		getDatabase().beginTransaction();
		try {
			String sql = "insert into TILE (" + Tile.DbField.ID + "," + Tile.DbField.NAME + "," + Tile.DbField.IMAGE_NAME + "," + Tile.DbField.IS_EMBEDDED + ") values (?,?,?,?)";
			Object[] params = new Object[4];
			params[0] = entity.getId();
			params[1] = entity.getName();
			params[2] = entity.getImageName();
			params[3] = entity.isEmbedded() ? "0" : "1";

			execSQL(sql, params);

			getDatabase().setTransactionSuccessful();
			entity.setState(DbState.LOADED);

		} finally {
			getDatabase().endTransaction();
		}
	}

	private void update(Tile entity) {
		String sql = "update TILE set " + Tile.DbField.NAME + "=?," + Tile.DbField.IMAGE_NAME + "=? WHERE " + Tile.DbField.ID + "=?";
		Object[] params = new Object[3];
		params[0] = entity.getName();
		params[1] = entity.getImageName();
		params[3] = entity.getId();

		execSQL(sql, params);

	}

	/**
	 * Persist the entity. Depending its state, this method will perform an
	 * insert or an update.
	 * 
	 * @param entity
	 *            the tile to persist
	 */
	public void persist(Tile entity) {
		if (entity.getState() == DbState.NEW) {
			create(entity);
		} else if (entity.getState() == DbState.LOADED) {
			update(entity);
		}
	}

}
