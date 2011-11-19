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
package org.amphiprion.mansionofmadness.database;

import org.amphiprion.mansionofmadness.ApplicationConstants;
import org.amphiprion.mansionofmadness.dto.Sound;
import org.amphiprion.mansionofmadness.dto.Tile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "amphiprion_mansionofmadness";
	private static final int DATABASE_VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL("create table TILE (" + Tile.DbField.ID + " text primary key, " + Tile.DbField.NAME + " text not null, " + Tile.DbField.IMAGE_NAME + " text" + ","
					+ Tile.DbField.IS_EMBEDDED + " number(1)" + "," + Tile.DbField.WIDTH + " number(2)" + "," + Tile.DbField.HEIGHT + " number(2)" + ") ");

			db.execSQL("insert into TILE values('1','attic','attic.png',1,2,2) ");
			db.execSQL("insert into TILE values('2','corridor_angle','corridor_angle.png',1,2,2) ");
			db.execSQL("insert into TILE values('3','diner_room','diner_room.png',1,2,2) ");
			db.execSQL("insert into TILE values('4','driveway','driveway.png',1,2,2) ");
			db.execSQL("insert into TILE values('5','garden','garden.png',1,2,2) ");
			db.execSQL("insert into TILE values('6','guest_room','guest_room.png',1,2,1) ");
			db.execSQL("insert into TILE values('7','loby','loby.png',1,3,2) ");
			db.execSQL("insert into TILE values('8','park','park.png',1,3,2) ");
			db.execSQL("insert into TILE values('9','salon','salon.png',1,2,2) ");
			db.execSQL("insert into TILE values('10','study','study.png',1,2,2) ");
			db.execSQL("insert into TILE values('11','tower','tower.png',1,2,2) ");
			db.execSQL("insert into TILE values('12','boiler_room','boiler_room.png',1,2,1) ");
			db.execSQL("insert into TILE values('13','cave','cave.png',1,2,2) ");
			db.execSQL("insert into TILE values('14','ceremonial','ceremonial.png',1,2,1) ");
			db.execSQL("insert into TILE values('15','chapel','chapel.png',1,2,2) ");
			db.execSQL("insert into TILE values('16','children_home','children_home.png',1,2,1) ");
			db.execSQL("insert into TILE values('17','corridor1','corridor1.png',1,2,1) ");
			db.execSQL("insert into TILE values('18','corridor2','corridor2.png',1,2,1) ");
			db.execSQL("insert into TILE values('19','corridor3','corridor3.png',1,2,1) ");
			db.execSQL("insert into TILE values('20','corridor4','corridor4.png',1,2,1) ");
			db.execSQL("insert into TILE values('21','crypt','crypt.png',1,2,1) ");
			db.execSQL("insert into TILE values('22','graveyard','graveyard.png',1,2,2) ");
			db.execSQL("insert into TILE values('23','laboratory','laboratory.png',1,2,2) ");
			db.execSQL("insert into TILE values('24','main_bedroom','main_bedroom.png',1,2,1) ");
			db.execSQL("insert into TILE values('25','park_back','park_back.png',1,2,2) ");
			db.execSQL("insert into TILE values('26','patio','patio.png',1,2,2) ");
			db.execSQL("insert into TILE values('27','pit','pit.png',1,2,1) ");
			db.execSQL("insert into TILE values('28','secret_way','secret_way.png',1,2,1) ");
			db.execSQL("insert into TILE values('29','underground1','underground1.png',1,2,1) ");
			db.execSQL("insert into TILE values('30','underground2','underground2.png',1,2,1) ");

			db.execSQL("create table SOUND (" + Sound.DbField.ID + " text primary key, " + Sound.DbField.NAME + " text not null, " + Sound.DbField.SOUND_NAME + " text" + ","
					+ Sound.DbField.IS_EMBEDDED + " number(1)" + ") ");

			db.execSQL("insert into SOUND values('1','door_lock','door_lock.wav',1) ");
			db.execSQL("insert into SOUND values('2','open_creaky_door','open_creaky_door.wav',1) ");

			onUpgrade(db, 1, DATABASE_VERSION);
		} catch (Throwable e) {
			Log.e(ApplicationConstants.PACKAGE, "", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// if (oldVersion == 1) {
		// db.execSQL("ALTER TABLE COUNTER ADD " + Counter.DbField.DEFAULT_VALUE
		// + " integer default 0");
		// oldVersion++;
		// }
	}

}
