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
import org.amphiprion.mansionofmadness.dto.CardPileCard;
import org.amphiprion.mansionofmadness.dto.CardPileInstance;
import org.amphiprion.mansionofmadness.dto.RandomCardPileCard;
import org.amphiprion.mansionofmadness.dto.Scenario;
import org.amphiprion.mansionofmadness.dto.Sound;
import org.amphiprion.mansionofmadness.dto.SoundInstance;
import org.amphiprion.mansionofmadness.dto.Tile;
import org.amphiprion.mansionofmadness.dto.TileInstance;

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

			db.execSQL("create table SCENARIO (" + Scenario.DbField.ID + " text primary key, " + Scenario.DbField.NAME + " text not null," + Scenario.DbField.IS_EMBEDDED
					+ " number(1)" + ") ");
			db.execSQL("insert into SCENARIO values('1','history_1',1) ");

			db.execSQL("create table TILE_INSTANCE (" + TileInstance.DbField.ID + " text primary key, " + TileInstance.DbField.SCENARIO_ID + " text not null, "
					+ TileInstance.DbField.TILE_ID + " text not null, " + TileInstance.DbField.POS_X + " number not null, " + TileInstance.DbField.POS_Y + " number not null, "
					+ TileInstance.DbField.ROTATION + " number not null) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('c6a12334-6836-436d-aff7-019b26ab76b6','1',450,150,'13',90) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('ed0e7689-4e59-434d-8aa4-ba2f21bdf45f','1',225,150,'14',270) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('4031c6fb-0146-4085-b5db-06d6815bea6a','1',750,225,'20',180) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('c331c447-abfd-4451-b261-6ad9335c8739','1',1050,225,'18',0) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('58daccfe-3849-4f55-abae-edc901cec5f9','1',300,450,'3',180) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('11e902db-27d2-4530-b543-a76654d7bb67','1',750,75,'6',0) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('a3241545-7e4d-4181-a4ed-9afc6a0537ba','1',1050,450,'23',0) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('e033026e-bbef-4916-9f4e-8058772fc826','1',675,450,'7',0) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('248169c8-0745-428a-afb4-bd700bf39f87','1',1050,75,'24',180) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('36405fab-11a9-4e03-b455-777c53dd92c9','1',1050,750,'5',90) ");

			db.execSQL("create table CARD_PILE_INSTANCE (" + CardPileInstance.DbField.ID + " text primary key, " + CardPileInstance.DbField.SCENARIO_ID + " text not null, "
					+ CardPileInstance.DbField.POS_X + " number not null, " + CardPileInstance.DbField.POS_Y + " number not null) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('1865218e-b741-4593-a1a4-bb77c76dac2c','1',228,155) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('dacb4e2c-f4e3-40d4-a525-1b08ae0330d1','1',371,221) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('1ac9054f-ecbb-4b44-b626-fdf453d7f2f9','1',456,73) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('27679b0b-f53a-4362-b504-9a44daa5e0ba','1',518,220) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('fa9f40f9-1104-4ffd-b326-9bd6c6ef06f3','1',753,230) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('e93cc863-fb1c-4de0-8514-773c0233531d','1',706,79) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('feb2820a-0f2d-4bb6-a13d-d1e56d197c44','1',844,72) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('c15f18d3-e9bb-4787-abab-b1224a751e9f','1',1053,77) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('e2bd2f16-d054-4c47-b547-8f8ad8861d2b','1',1054,228) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('e888cc12-5c96-4e6b-942c-7ce1a4898d07','1',669,479) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('6048e6fa-ee45-4864-a193-ee331a9beb19','1',304,518) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('fd2fb2c6-7772-47f9-8c0c-2f44379dffc9','1',240,376) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('02227a75-b7d6-4aa1-983b-96842f15136e','1',392,371) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('a0e64c97-e013-43aa-ac7b-551c8d4abc6d','1',976,446) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('49e65658-fa3a-4679-8800-a9afb2b048ac','1',1117,488) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('5dd7b54e-3f93-42cc-9399-635ce9231e6a','1',1120,354) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('46f9f977-2f2d-4627-a0f7-d59b8fa5aa54','1',978,763) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('b502c184-a496-4298-a60a-8b7256dfe7b1','1',1115,660) ");

			db.execSQL("create table CARD_PILE_CARD (" + CardPileCard.DbField.ID + " text primary key, " + CardPileCard.DbField.CARD_PILE_INSTANCE_ID + " text not null, "
					+ CardPileCard.DbField.CARD_ID + " text not null, " + CardPileCard.DbField.POS_ORDER + " number not null) ");

			db.execSQL("create table RANDOM_CARD_PILE_CARD (" + RandomCardPileCard.DbField.ID + " text primary key, " + RandomCardPileCard.DbField.SCENARIO_ID + " text not null, "
					+ RandomCardPileCard.DbField.CARD_ID + " text not null, " + RandomCardPileCard.DbField.POS_ORDER + " number not null) ");

			db.execSQL("create table SOUND_INSTANCE (" + SoundInstance.DbField.ID + " text primary key, " + SoundInstance.DbField.SCENARIO_ID + " text not null, "
					+ SoundInstance.DbField.SOUND_ID + " text not null, " + SoundInstance.DbField.POS_X + " number not null, " + SoundInstance.DbField.POS_Y + " number not null) ");

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
