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
import org.amphiprion.mansionofmadness.dto.Card;
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

			db.execSQL("insert into SOUND values('1','door_lock','door_lock',1) ");
			db.execSQL("insert into SOUND values('2','open_creaky_door','open_creaky_door',1) ");

			db.execSQL("create table CARD (" + Card.DbField.ID + " text primary key, " + Card.DbField.NAME + " text not null, " + Card.DbField.TYPE + " text" + ","
					+ Card.DbField.IS_EMBEDDED + " number(1)" + ") ");
			db.execSQL("insert into CARD values('1','nothing','exploration',1) ");
			db.execSQL("insert into CARD values('2','lantern','exploration',1) ");
			db.execSQL("insert into CARD values('3','unspeakable_cults','exploration',1) ");
			db.execSQL("insert into CARD values('4','silver_key','exploration',1) ");
			db.execSQL("insert into CARD values('5','sledgehammer','exploration',1) ");
			db.execSQL("insert into CARD values('6','colt_38','exploration',1) ");
			db.execSQL("insert into CARD values('7','whateley_diary','exploration',1) ");
			db.execSQL("insert into CARD values('8','crowbar','exploration',1) ");
			db.execSQL("insert into CARD values('9','ceremonial_skull','exploration',1) ");
			db.execSQL("insert into CARD values('10','ruby_rlyeh','exploration',1) ");
			db.execSQL("insert into CARD values('11','ancient_sign','exploration',1) ");
			db.execSQL("insert into CARD values('12','whisky','exploration',1) ");
			db.execSQL("insert into CARD values('13','fire_extinguisher','exploration',1) ");
			db.execSQL("insert into CARD values('14','shotgun','exploration',1) ");
			db.execSQL("insert into CARD values('15','de_vermis_mysreriis','exploration',1) ");
			db.execSQL("insert into CARD values('16','axe','exploration',1) ");
			db.execSQL("insert into CARD values('17','knife','exploration',1) ");
			db.execSQL("insert into CARD values('18','sedative','exploration',1) ");
			db.execSQL("insert into CARD values('19','cult_dress','exploration',1) ");
			db.execSQL("insert into CARD values('20','crucifix','exploration',1) ");
			db.execSQL("insert into CARD values('21','obvious_proof','exploration',1) ");
			db.execSQL("insert into CARD values('22','brass_key','exploration',1) ");
			db.execSQL("insert into CARD values('23','torch','exploration',1) ");
			db.execSQL("insert into CARD values('24','dhol_song','exploration',1) ");
			db.execSQL("insert into CARD values('25','saturnien_wine','exploration',1) ");
			db.execSQL("insert into CARD values('26','magic_sentence','exploration',1) ");
			db.execSQL("insert into CARD values('27','password','exploration',1) ");
			db.execSQL("insert into CARD values('29','clue_1a','exploration',1) ");
			db.execSQL("insert into CARD values('30','clue_1b','exploration',1) ");
			db.execSQL("insert into CARD values('31','clue_1c','exploration',1) ");
			db.execSQL("insert into CARD values('32','clue_2a','exploration',1) ");
			db.execSQL("insert into CARD values('33','clue_2b','exploration',1) ");
			db.execSQL("insert into CARD values('34','clue_3a','exploration',1) ");
			db.execSQL("insert into CARD values('35','clue_3b','exploration',1) ");
			db.execSQL("insert into CARD values('36','clue_4a','exploration',1) ");
			db.execSQL("insert into CARD values('37','clue_4b','exploration',1) ");
			db.execSQL("insert into CARD values('38','clue_5a','exploration',1) ");
			db.execSQL("insert into CARD values('39','clue_5b','exploration',1) ");
			db.execSQL("insert into CARD values('40','clue_6a','exploration',1) ");
			db.execSQL("insert into CARD values('41','clue_6b','exploration',1) ");

			db.execSQL("insert into CARD values('42','magic_lock','lock',1) ");
			db.execSQL("insert into CARD values('43','runic_door','lock',1) ");
			db.execSQL("insert into CARD values('44','blocked_entry','lock',1) ");
			db.execSQL("insert into CARD values('45','terrifying_way','lock',1) ");
			db.execSQL("insert into CARD values('46','looked_door','lock',1) ");
			db.execSQL("insert into CARD values('47','black_room','lock',1) ");
			db.execSQL("insert into CARD values('48','jammed_door','lock',1) ");
			db.execSQL("insert into CARD values('50','open_portal','lock',1) ");
			db.execSQL("insert into CARD values('51','guarded_way','lock',1) ");
			db.execSQL("insert into CARD values('52','electric_lock','lock',1) ");
			db.execSQL("insert into CARD values('53','sealed_door','lock',1) ");
			db.execSQL("insert into CARD values('54','closed_door','lock',1) ");

			db.execSQL("insert into CARD values('55','casket','obstacle',1) ");
			db.execSQL("insert into CARD values('56','suitcase','obstacle',1) ");
			db.execSQL("insert into CARD values('57','power_cut','obstacle',1) ");
			db.execSQL("insert into CARD values('58','manhole_plate','obstacle',1) ");
			db.execSQL("insert into CARD values('59','secret_box','obstacle',1) ");
			db.execSQL("insert into CARD values('60','short_circuit','obstacle',1) ");
			db.execSQL("insert into CARD values('61','locked_cupboard','obstacle',1) ");

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
