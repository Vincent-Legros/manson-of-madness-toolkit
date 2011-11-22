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
import org.amphiprion.mansionofmadness.dto.Entity.DbState;
import org.amphiprion.mansionofmadness.dto.Sound;

import android.content.Context;
import android.database.Cursor;

/**
 * This DAO is used to manage sound entities.
 * 
 * @author ng00124c
 * 
 */
public class SoundDao extends AbstractDao {

	/** The singleton. */
	private static SoundDao instance;

	/**
	 * Hidden constructor.
	 * 
	 * @param context
	 *            the application context
	 */
	private SoundDao(Context context) {
		super(context);
	}

	/**
	 * Return the singleton.
	 * 
	 * @param context
	 *            the application context
	 * @return the singleton
	 */
	public static SoundDao getInstance(Context context) {
		if (instance == null) {
			instance = new SoundDao(context);
		}
		return instance;
	}

	/**
	 * Return the given Sound.
	 * 
	 * @param id
	 *            the unique identifier
	 * @return the sound or null if not exists
	 */
	public Sound getSound(String id) {

		String sql = "SELECT " + Sound.DbField.ID + "," + Sound.DbField.NAME + "," + Sound.DbField.SOUND_NAME + "," + Sound.DbField.IS_EMBEDDED + " from SOUND where "
				+ Sound.DbField.ID + "=?";

		Cursor cursor = getDatabase().rawQuery(sql, new String[] { id });
		Sound result = null;
		if (cursor.moveToFirst()) {
			Sound entity = new Sound(cursor.getString(0));
			entity.setName(cursor.getString(1));
			entity.setSoundName(cursor.getString(2));
			entity.setEmbedded(cursor.getInt(3) != 0);
			updateDisplayName(entity);
			result = entity;
		}
		cursor.close();
		return result;
	}

	/**
	 * 
	 * @return all existing sounds
	 */
	public List<Sound> getSounds() {
		return getSounds("");
	}

	public List<Sound> getSounds(int pageIndex, int pageSize, boolean addEmbedded) {
		String addOn = null;
		if (addEmbedded) {
			addOn = " order by " + Sound.DbField.NAME + " limit " + (pageSize + 1) + " offset " + pageIndex * pageSize;
		} else {
			addOn = " where " + Sound.DbField.IS_EMBEDDED + "=0 order by " + Sound.DbField.NAME + " limit " + (pageSize + 1) + " offset " + pageIndex * pageSize;
		}
		return getSounds(addOn);
	}

	private List<Sound> getSounds(String sqlAddon) {

		String sql = "SELECT " + Sound.DbField.ID + "," + Sound.DbField.NAME + "," + Sound.DbField.SOUND_NAME + "," + Sound.DbField.IS_EMBEDDED + " from SOUND";
		sql += sqlAddon;

		Cursor cursor = getDatabase().rawQuery(sql, null);
		ArrayList<Sound> result = new ArrayList<Sound>();
		if (cursor.moveToFirst()) {
			do {
				Sound entity = new Sound(cursor.getString(0));
				entity.setName(cursor.getString(1));
				entity.setSoundName(cursor.getString(2));
				entity.setEmbedded(cursor.getInt(3) != 0);
				updateDisplayName(entity);
				result.add(entity);
			} while (cursor.moveToNext());
		}
		cursor.close();

		return result;
	}

	private void updateDisplayName(Sound sound) {
		if (sound.isEmbedded()) {
			sound.setDisplayName(getContext().getText(getContext().getResources().getIdentifier("sound_" + sound.getName(), "string", ApplicationConstants.PACKAGE)).toString());
		}
	}

	/**
	 * Persist a new Sound.
	 * 
	 * @param entity
	 *            the new Sound
	 */
	private void create(Sound entity) {
		getDatabase().beginTransaction();
		try {
			String sql = "insert into SOUND (" + Sound.DbField.ID + "," + Sound.DbField.NAME + "," + Sound.DbField.SOUND_NAME + "," + Sound.DbField.IS_EMBEDDED
					+ ") values (?,?,?,?)";
			Object[] params = new Object[4];
			params[0] = entity.getId();
			params[1] = entity.getName();
			params[2] = entity.getSoundName();
			params[3] = entity.isEmbedded() ? "1" : "0";

			execSQL(sql, params);

			getDatabase().setTransactionSuccessful();
			entity.setState(DbState.LOADED);

		} finally {
			getDatabase().endTransaction();
		}
	}

	private void update(Sound entity) {
		String sql = "update SOUND set " + Sound.DbField.NAME + "=?," + Sound.DbField.SOUND_NAME + "=? WHERE " + Sound.DbField.ID + "=?";
		Object[] params = new Object[3];
		params[0] = entity.getName();
		params[1] = entity.getSoundName();
		params[2] = entity.getId();

		execSQL(sql, params);

	}

	/**
	 * Persist the entity. Depending its state, this method will perform an
	 * insert or an update.
	 * 
	 * @param entity
	 *            the Sound to persist
	 */
	public void persist(Sound entity) {
		if (entity.getState() == DbState.NEW) {
			create(entity);
		} else if (entity.getState() == DbState.LOADED) {
			update(entity);
		}
	}

}
