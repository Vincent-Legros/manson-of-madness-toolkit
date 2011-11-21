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
			db.execSQL("insert into SCENARIO values('1','1_history',1) ");
			db.execSQL("insert into SCENARIO values('2','2_history',1) ");
			db.execSQL("insert into SCENARIO values('3','3_history',1) ");
			db.execSQL("insert into SCENARIO values('4','4_history',1) ");
			db.execSQL("insert into SCENARIO values('5','5_history',1) ");

			db.execSQL("create table TILE_INSTANCE (" + TileInstance.DbField.ID + " text primary key, " + TileInstance.DbField.SCENARIO_ID + " text not null, "
					+ TileInstance.DbField.TILE_ID + " text not null, " + TileInstance.DbField.POS_X + " number not null, " + TileInstance.DbField.POS_Y + " number not null, "
					+ TileInstance.DbField.ROTATION + " number not null) ");
			// scenario 1
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
			// scenario 2
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('3640ef1b-d4c7-423f-84b0-4363c93e1712','2',300,75,'12',180) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('0dfba3e5-327e-4180-a22c-b4bcbd7bc088','2',750,450,'10',270) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('51b70061-f5f4-4774-8276-27ded0ef5c03','2',450,375,'28',0) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('df6cb54e-ebbe-4747-85ba-1edafe8113b3','2',450,525,'29',0) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('6dc9f6cf-e36e-4de7-8bbb-de384d91c11e','2',150,525,'27',180) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('69792e6d-5ace-4c25-8617-3f33226937e9','2',450,675,'30',0) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('fbee3bfb-06b3-4aac-8dc4-5acb63a704d2','2',750,675,'21',0) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('a394903b-60d2-4bfa-a666-4c17d0ded6fd','2',1050,450,'15',270) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('7ead87c2-54dc-4ecb-9bb4-5f1da3b9f4ed','2',600,150,'13',0) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('1c47692c-4cea-44e6-acf7-38a4a035ef5a','2',900,150,'2',0) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('c0619a1b-ca95-4f1e-8cf6-526c5ce2b2c4','2',300,225,'14',180) ");
			// scenario 3
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('d62c689a-26c4-45a6-9f56-086cb819345a','3',750,450,'26',270) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('58ba9815-6b6d-4145-bb44-3a5f758ae038','3',450,450,'25',270) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('bfd5f32d-836d-4d00-b909-2eacf82877ac','3',1050,225,'8',90) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('96c9880f-dc25-43f7-a4e5-004e91cfe943','3',750,150,'4',90) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('82b42ccf-2dda-4da0-9436-cb37379e4587','3',450,150,'5',90) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('39f639b8-cfd6-47d1-a3e2-10428af64a36','3',150,150,'22',0) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('bc938b80-c229-4a3c-9b81-b6286da24785','3',750,675,'27',180) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('0987b32c-e9cd-40cc-9284-bb3eba862291','3',450,675,'30',180) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('a602b0d2-da18-436a-94d1-57ae118092d3','3',1050,675,'29',180) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('e5c3e53b-1ab3-4577-8ece-d948e48942fa','3',150,675,'21',0) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('a0880811-a165-49c7-b7e1-96b25bf4b72d','3',1050,525,'12',180) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('a7f8ea12-eb5b-42bd-8601-770c759050cc','3',600,-75,'6',0) ");
			// scenario 4
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('b4065822-a0a7-4d64-9572-950604e4b8fc','4',750,600,'23',90) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('35591f59-383b-4c89-af40-b702c48c1f25','4',1050,450,'2',0) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('d5d9b163-0e50-42b7-a59a-175380c16b80','4',750,375,'20',180) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('56311e68-3220-4289-944c-0aced9caea3d','4',450,375,'19',0) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('ff3767ba-bde9-42dd-990c-d01ca0df2567','4',750,225,'17',0) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('869e2cbb-4dc1-40a2-8fc7-f3c7979c28a2','4',1050,225,'12',0) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('f69229fa-60b7-49fc-b3b1-5cf77c53152c','4',150,300,'1',0) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('762b47d0-828c-462a-80a9-220297b75441','4',1050,750,'9',180) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('6181b727-a9a9-49e9-a900-f63003ee31ec','4',450,150,'10',0) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('e54b3dd7-eeb6-4e81-826a-1c877965205e','4',150,0,'11',0) ");
			// scenario 5
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('85270e4a-887c-4f44-8aca-67cf1785b8d5','5',675,450,'28',270) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('a29e4d80-039d-48c5-aed2-3e7baa779f4f','5',825,450,'18',90) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('5e4f831e-0916-4e26-9394-ec2487be8605','5',450,825,'20',180) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('ae1efd8d-affb-4c9e-8d89-90e5425fdbff','5',150,825,'19',0) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('70fa406f-20c8-477f-97e8-e186c892703e','5',450,450,'23',0) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('0c584ca5-3914-48be-b465-6833d07911a7','5',150,525,'7',90) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('76986c33-efd6-489f-b3b2-5ba0072b3e42','5',450,675,'24',180) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('4cf6e0af-7565-4cba-a389-08052616bd67','5',1050,750,'11',180) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('9cd0d673-8fd1-4f64-b8aa-a35cee3361d5','5',900,150,'10',270) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('f05aa757-31f9-42ac-a784-6cdd5d400b84','5',150,150,'9',0) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('57faefde-df49-4a45-bf24-c722a960dd01','5',450,150,'3',0) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('995c5ceb-e2b2-457b-a8f3-f2a2ee5f715d','5',750,750,'2',90) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('685f2f1e-9356-4ab0-8f5f-e507ce50968f','5',675,150,'16',270) ");
			db.execSQL("insert into TILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y,TILE_ID,ROTATION) values ('536f978c-0698-490d-a97c-a6d9d2ac2a9f','5',1050,450,'1',180) ");

			db.execSQL("create table CARD_PILE_INSTANCE (" + CardPileInstance.DbField.ID + " text primary key, " + CardPileInstance.DbField.SCENARIO_ID + " text not null, "
					+ CardPileInstance.DbField.POS_X + " number not null, " + CardPileInstance.DbField.POS_Y + " number not null) ");
			// scenario 1
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
			// scenario 2
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('fdf8bb0b-de98-4a87-80a9-636a1c875316','2',822,447) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('c84adcbc-e370-4657-94cf-f4c714b4146f','2',686,447) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('bc502d7c-c8bf-4416-8bfc-9f26e1057792','2',372,372) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('d17f033d-df8d-4217-b52d-d71e2e7240f2','2',461,526) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('008658e7-af2f-40c5-bc35-16299b8e50d3','2',159,527) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('1066cb3a-17c2-44ef-a79e-56c16feda4b8','2',436,675) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('914c33d6-afc8-4cd3-b962-7ba6f514b3c5','2',750,669) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('bd04aafa-6602-4917-a8a1-154f7a4f9301','2',1046,435) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('290374db-b353-4218-bc43-e27480e57dad','2',532,148) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('593ac957-942a-4300-b0d5-87c3bbcb50de','2',666,223) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('bce9f30e-1259-497a-8e81-c70311dc5d1d','2',666,80) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('6886d5eb-05ba-4e05-b66c-a4a9369951b5','2',830,213) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('57fb66b0-c61d-4489-864c-48ce55c7ae1c','2',981,201) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('f93988fe-3351-48a9-a887-34cce80b63ad','2',841,72) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('7410cbc9-7152-4478-8dba-7ef4976fe92a','2',293,72) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('5f43eb5f-288e-4f7f-9cfa-32dad45035ae','2',303,220) ");
			// scenario 3
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('69d0d795-bb20-404e-b38b-6b7c9e5b4782','3',825,520) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('e6267a6b-7a8e-4ead-b8b3-9a7a3bd78284','3',688,381) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('9b48e91b-6925-4b33-95fa-7353b65b2240','3',391,445) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('4f5f2330-ecb8-4187-8c81-572c81a88da5','3',531,447) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('86489b0f-d457-4d89-a3e6-50cae815b331','3',1134,60) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('38e5a547-46f2-48b5-aa43-89d5a7bcc3b8','3',1025,265) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('d2300553-7e17-4af6-adbf-7d0d863db2fe','3',708,81) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('fb4692f1-329d-47da-a721-6efd86332f8f','3',819,213) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('f4f3dea8-36be-47ba-a7f4-cb3093da1648','3',524,67) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('9873b9d9-f93f-4a4e-a5d1-e3a6f618efc4','3',376,181) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('a699fd96-3b7b-4a56-9b75-9d3a47c042a9','3',145,149) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('ecb652de-b6d4-4c13-b796-a8018e987ad2','3',741,668) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('8110d8f0-95ee-4a0c-82e9-97fd50af52ee','3',469,667) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('6b4aaf13-6554-4dfa-b789-347658beaefd','3',1036,670) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('50b0226b-2b2d-46ee-9de0-c294314fa99e','3',152,677) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('bad7f6ce-509d-48e0-b50e-ed3ccdb9d98e','3',1034,527) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('701da449-3992-4d01-a253-d66d9424d235','3',556,-75) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('f30c9c94-88f6-400a-a9a7-647ebaf80349','3',691,-74) ");
			// scenario 4
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('3b4bdf45-b4a0-4d92-b594-330eb8d82a0e','4',697,670) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('99e6049e-2321-490b-bbc4-af374dd66f43','4',846,672) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('ed8137ee-7355-43e5-bc2e-3ab05a57168e','4',742,528) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('87d3844b-872b-47f2-af81-e7d8e10f5f05','4',980,530) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('1a75a754-a0e2-451a-89e7-79b88555b4b8','4',1121,505) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('590c7960-1b89-4691-80d3-3886c131cc83','4',995,375) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('d7a5f2b7-b98f-4c8b-9317-fbb214afb771','4',741,376) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('142cdf1c-9f7b-4217-8b5b-1f6f8c21087c','4',449,375) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('102fec3a-9aa8-4d3c-8be5-792081e0fe83','4',736,221) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('2fdcf849-a705-41b3-a3c6-1855f5dd99d3','4',1037,223) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('712c10c6-14b6-460b-9b9c-c3305c20da95','4',102,247) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('ad434f6c-0e95-47ff-a295-b05ed123cef8','4',211,389) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('c4243d06-5e7b-4426-8c4d-85c30bf384eb','4',1009,750) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('faa7ef4a-66bb-4d7c-a4b5-93c9ee43ceb5','4',1144,823) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('b762014e-2e94-41d7-ad68-7b10c48be8df','4',449,218) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('3cbc9edf-cbf2-440e-908f-a5591ff2266f','4',447,76) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('1edd1703-d771-4e46-b368-f1ef601f07c3','4',54,80) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('0d9f81b9-9e2c-4e52-b68b-b8184af186a6','4',141,40) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('3ef7d616-7618-43bb-8d17-4e84cbde8561','4',201,-74) ");
			// scenario 5
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('36278019-e3d5-40fd-9ad5-3e3f13e98ef9','5',677,449) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('921842d3-b6ae-453f-92f1-f7a2fe5dc723','5',824,447) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('99f97048-e254-4e56-9395-a818e44c3ecb','5',448,822) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('3cf892bc-fa9d-407a-beb2-e0a861f2071c','5',164,818) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('8dcbd9e2-73ab-4781-874c-c91fdb015105','5',368,453) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('e430cf4d-eae7-4c66-aff1-0ec74540c143','5',521,497) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('1af2cbbb-4e44-43c4-bdf2-3869b1d955ac','5',518,350) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('737c9d98-2019-4d0e-8320-3d5c1610ccd1','5',143,524) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('4235de84-b68e-45bf-9024-738037c408f4','5',446,667) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('88b198b2-47e9-49ed-9107-c378cabdbc27','5',1001,820) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('934bfdf3-1ca1-41aa-9a8d-a866cabe66b9','5',1143,655) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('fe466057-00dd-44df-85b3-ca5771476fdc','5',1034,690) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('d660ab9f-7e0f-4d45-9efe-6dad4f58ef60','5',835,147) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('88dee47c-7fe5-409b-b965-5f5695866a69','5',969,157) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('9011ee3f-43d7-4f2a-9c36-e77b399a5504','5',54,75) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('7153fe7b-50c6-407c-b983-c633e3fed1f9','5',170,154) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('ac748b2d-638b-4f44-b3a2-3998b0beedf0','5',358,219) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('233b35dc-134c-47d9-875a-9d2a5f6f094c','5',483,223) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('1961e3fa-aa84-421a-b5e2-66530fa03915','5',439,74) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('abd7fa04-eff2-47f7-a851-e9ace25a3dee','5',679,674) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('9cf2abfa-6e6a-4436-a666-c964fa1a1f26','5',691,821) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('c8bbfa03-04d3-4d5e-956e-263aa2c9173f','5',828,686) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('cfc413a2-4377-40bf-90db-909eb00a5ba1','5',670,200) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('ea5c5050-e489-4032-9ee2-9bef2aafd68a','5',670,58) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('6e40abb3-b810-4cbf-9647-9ba85f1ec256','5',973,357) ");
			db.execSQL("insert into CARD_PILE_INSTANCE (ID,SCENARIO_ID,POS_X,POS_Y) values ('eb7ac9a0-452c-42b3-9b8c-753dab2cba94','5',1046,501) ");

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
