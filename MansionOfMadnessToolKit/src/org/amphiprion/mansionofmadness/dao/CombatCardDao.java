/*
 * @copyright 2011 Ridha Chelghaf
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

import org.amphiprion.mansionofmadness.dto.CombatCard;
import org.amphiprion.mansionofmadness.dto.Entity.DbState;

import android.content.Context;
import android.database.Cursor;

/**
 * This DAO is used to manage CombatCard entities.
 * 
 * @author ng00124c
 * 
 */
public class CombatCardDao extends AbstractDao {

	/** The singleton. */
	private static CombatCardDao instance;

	/**
	 * Hidden constructor.
	 * 
	 * @param context
	 *            the application context
	 */
	private CombatCardDao(Context context) {
		super(context);
	}

	/**
	 * Return the singleton.
	 * 
	 * @param context
	 *            the application context
	 * @return the singleton
	 */
	public static CombatCardDao getInstance(Context context) {
		if (instance == null) {
			instance = new CombatCardDao(context);
		}
		return instance;
	}

	/**
	 * Return the given CombatCard.
	 * 
	 * @param id
	 *            the unique identifier
	 * @return the CombatCard or null if not exists
	 */
	public CombatCard getCombatCard(String id) {

		String sql = "SELECT " + CombatCard.DbField.ID + "," + CombatCard.DbField.MONSTERCLASS + "," + CombatCard.DbField.ATKTYPEINV + "," + CombatCard.DbField.TESTINV + ","
				+ CombatCard.DbField.SUCCESSINV + "," + CombatCard.DbField.FAILUREINV + "," + CombatCard.DbField.ATKTYPEMON + "," + CombatCard.DbField.TESTMON + ","
				+ CombatCard.DbField.SUCCESSMON + "," + CombatCard.DbField.FAILUREMON + "," + CombatCard.DbField.IS_EMBEDDED + " from COMBATCARD where " + CombatCard.DbField.ID
				+ "=?";

		Cursor cursor = getDatabase().rawQuery(sql, new String[] { id });
		CombatCard result = null;
		if (cursor.moveToFirst()) {
			CombatCard entity = new CombatCard(cursor.getString(0));
			entity.setMonsterClass(cursor.getString(1));
			entity.setAtkTypeInv(cursor.getString(2));
			entity.setTestInv(cursor.getString(3));
			entity.setSuccessInv(cursor.getString(4));
			entity.setFailureInv(cursor.getString(5));
			entity.setAtkTypeMon(cursor.getString(6));
			entity.setTestMon(cursor.getString(7));
			entity.setSuccessMon(cursor.getString(8));
			entity.setFailureMon(cursor.getString(9));
			entity.setEmbedded(cursor.getInt(10) != 0);
			result = entity;
		}
		cursor.close();
		return result;
	}

	/**
	 * Return the given CombatCard.
	 * 
	 * @param id
	 *            the unique identifier
	 * @return the CombatCard or null if not exists
	 */
	public CombatCard drawCombatCard(String monsterClass, String atkType) {

		String sql = "SELECT " + CombatCard.DbField.ID + "," + CombatCard.DbField.MONSTERCLASS + "," + CombatCard.DbField.ATKTYPEINV + "," + CombatCard.DbField.TESTINV + ","
				+ CombatCard.DbField.SUCCESSINV + "," + CombatCard.DbField.FAILUREINV + "," + CombatCard.DbField.ATKTYPEMON + "," + CombatCard.DbField.TESTMON + ","
				+ CombatCard.DbField.SUCCESSMON + "," + CombatCard.DbField.FAILUREMON + "," + CombatCard.DbField.IS_EMBEDDED + " from COMBATCARD where "
				+ CombatCard.DbField.MONSTERCLASS + "=? and (" + CombatCard.DbField.ATKTYPEINV + "=? or " + CombatCard.DbField.ATKTYPEMON + "=?)";
		// System.out.println(sql + " : monsterClass=" + monsterClass +
		// ", atkType=" + atkType);
		Cursor cursor = getDatabase().rawQuery(sql, new String[] { monsterClass, atkType, atkType });
		// Log.d(ApplicationConstants.PACKAGE, sql);
		// Log.d(ApplicationConstants.PACKAGE, "     monsterClass=" +
		// monsterClass);
		// Log.d(ApplicationConstants.PACKAGE, "     atkType=" + atkType);
		CombatCard result = null;
		if (cursor.moveToFirst()) {
			CombatCard entity = new CombatCard(cursor.getString(0));
			entity.setMonsterClass(cursor.getString(1));
			entity.setAtkTypeInv(cursor.getString(2));
			entity.setTestInv(cursor.getString(3));
			entity.setSuccessInv(cursor.getString(4));
			entity.setFailureInv(cursor.getString(5));
			entity.setAtkTypeMon(cursor.getString(6));
			entity.setTestMon(cursor.getString(7));
			entity.setSuccessMon(cursor.getString(8));
			entity.setFailureMon(cursor.getString(9));
			entity.setEmbedded(cursor.getInt(10) != 0);
			// Log.d(ApplicationConstants.PACKAGE, "*" +
			// entity.getMonsterClass() + "*    *" + entity.getAtkTypeMon() +
			// "*");
			result = entity;
		}
		cursor.close();

		// TODO DELETE CARD AND REINSERT IT AT THE END
		return result;
	}

	/**
	 * Persist a new CombatCard.
	 * 
	 * @param entity
	 *            the new CombatCard
	 */
	private void create(CombatCard entity) {
		getDatabase().beginTransaction();
		try {
			String sql = "insert into COMBATCARD (" + CombatCard.DbField.ID + "," + CombatCard.DbField.MONSTERCLASS + "," + CombatCard.DbField.ATKTYPEINV + ","
					+ CombatCard.DbField.TESTINV + "," + CombatCard.DbField.SUCCESSINV + "," + CombatCard.DbField.FAILUREINV + "," + CombatCard.DbField.ATKTYPEMON + ","
					+ CombatCard.DbField.TESTMON + "," + CombatCard.DbField.SUCCESSMON + "," + CombatCard.DbField.FAILUREMON + "," + CombatCard.DbField.IS_EMBEDDED
					+ ") values (?,?,?,?,?,?,?,?,?,?,?)";
			Object[] params = new Object[4];
			params[0] = entity.getId();
			params[1] = entity.getMonsterClass();
			params[2] = entity.getAtkTypeInv();
			params[3] = entity.getTestInv();
			params[4] = entity.getSuccessInv();
			params[5] = entity.getFailureInv();
			params[6] = entity.getAtkTypeMon();
			params[7] = entity.getTestMon();
			params[8] = entity.getSuccessMon();
			params[9] = entity.getFailureMon();
			params[10] = entity.isEmbedded() ? "1" : "0";

			execSQL(sql, params);

			getDatabase().setTransactionSuccessful();
			entity.setState(DbState.LOADED);

		} finally {
			getDatabase().endTransaction();
		}
	}

	private void update(CombatCard entity) {
		String sql = "update COMBATCARD set " + CombatCard.DbField.MONSTERCLASS + "=?," + CombatCard.DbField.ATKTYPEINV + "=?," + CombatCard.DbField.TESTINV + "=?,"
				+ CombatCard.DbField.SUCCESSINV + "=?," + CombatCard.DbField.FAILUREINV + "=?," + CombatCard.DbField.ATKTYPEMON + "=?," + CombatCard.DbField.TESTMON + "=?,"
				+ CombatCard.DbField.SUCCESSMON + "=?," + CombatCard.DbField.FAILUREMON + "=? WHERE " + CombatCard.DbField.ID + "=?";
		Object[] params = new Object[3];
		params[0] = entity.getMonsterClass();
		params[1] = entity.getAtkTypeInv();
		params[2] = entity.getTestInv();
		params[3] = entity.getSuccessInv();
		params[4] = entity.getFailureInv();
		params[5] = entity.getAtkTypeMon();
		params[6] = entity.getTestMon();
		params[7] = entity.getSuccessMon();
		params[8] = entity.getFailureMon();
		params[9] = entity.getId();

		execSQL(sql, params);

	}

	/**
	 * Persist the entity. Depending its state, this method will perform an
	 * insert or an update.
	 * 
	 * @param entity
	 *            the CombatCard to persist
	 */
	public void persist(CombatCard entity) {
		if (entity.getState() == DbState.NEW) {
			create(entity);
		} else if (entity.getState() == DbState.LOADED) {
			update(entity);
		} else if (entity.getState() == DbState.DELETE) {
			delete(entity);
		}
	}

	private void delete(CombatCard entity) {
		String sql = "delete FROM COMBATCARD where " + CombatCard.DbField.ID + "=?";
		Object[] params = new Object[1];
		params[0] = entity.getId();
		execSQL(sql, params);
	}
}
