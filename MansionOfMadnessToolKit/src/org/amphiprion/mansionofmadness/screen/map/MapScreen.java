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
package org.amphiprion.mansionofmadness.screen.map;

import java.util.List;

import org.amphiprion.gameengine3d.GameScreen;
import org.amphiprion.gameengine3d.ScreenProperty;
import org.amphiprion.gameengine3d.animation.Translation2DAnimation;
import org.amphiprion.gameengine3d.mesh.Image2D;
import org.amphiprion.mansionofmadness.dao.CardDao;
import org.amphiprion.mansionofmadness.dao.CardPileCardDao;
import org.amphiprion.mansionofmadness.dao.CardPileInstanceDao;
import org.amphiprion.mansionofmadness.dao.RandomCardPileCardDao;
import org.amphiprion.mansionofmadness.dao.SoundDao;
import org.amphiprion.mansionofmadness.dao.SoundInstanceDao;
import org.amphiprion.mansionofmadness.dao.TileDao;
import org.amphiprion.mansionofmadness.dao.TileInstanceDao;
import org.amphiprion.mansionofmadness.dto.Card;
import org.amphiprion.mansionofmadness.dto.CardPileCard;
import org.amphiprion.mansionofmadness.dto.CardPileInstance;
import org.amphiprion.mansionofmadness.dto.RandomCardPileCard;
import org.amphiprion.mansionofmadness.dto.Scenario;
import org.amphiprion.mansionofmadness.dto.Sound;
import org.amphiprion.mansionofmadness.dto.SoundInstance;
import org.amphiprion.mansionofmadness.dto.Tile;
import org.amphiprion.mansionofmadness.dto.TileInstance;
import org.amphiprion.mansionofmadness.screen.map.TouchableGroup2D.PointerState;

import android.content.Context;
import android.view.MotionEvent;
import android.view.animation.BounceInterpolator;

/**
 * @author ng00124c
 * 
 */
public class MapScreen extends GameScreen {
	private Context context;
	private Scenario scenario;

	public enum ComponentKey {
		MOVE_ICON, DELETE_ICON, ROTATE_CLOCK_ICON, ROTATE_COUNTER_CLOCK_ICON, PLAY_ICON, ADD_CARD_PILE_ICON, EDIT_CARD_PILE_ICON
	}

	private PointerState pointerState = PointerState.NONE;
	// private int lastPointerX;
	// private int lastPointerY;
	// private int lastPointerDeltaX;
	// private int lastPointerDeltaY;
	// private float lastPointerDist;
	// private long lastPointerDownTime;
	private ComponentTab tileTab;
	private TileMenu tileMenu;
	protected BoardMenu boardMenu;
	protected RandomCardZone2D randomPile;
	private Translation2DAnimation tileMenuTabAnimation;
	// private Translation2DAnimation tileMenuAnimation;
	private Image2D imgTab;

	// private Image2D imgTabBackground;
	// private int tileIndex = -1;
	// private Tile2D selectedTile;

	public MapScreen(Context context, Scenario scenario) {
		this.context = context;
		this.scenario = scenario;

		// ##### load libray elements
		List<Tile> availableTiles = TileDao.getInstance(context).getTiles();
		List<Sound> availableSounds = SoundDao.getInstance(context).getSounds();
		List<Card> availableCards = CardDao.getInstance(context).getCards();

		// ######### build the background #########
		Image2D background = new Image2D("background/map_background.png", false, true);
		background.x = 1280 / 2;
		background.y = 800 / 2;
		objects2d.add(background);

		// ######### build the board #########
		boardMenu = new BoardMenu(this);
		// add the board to the rendering object tree
		objects2d.add(boardMenu);

		tileTab = new ComponentTab(this);
		// ######### build the tile Menu ##########
		imgTab = new Image2D("tiles/tab.png", false, true);
		imgTab.x = ComponentTab.WIDTH + 76 / 2;
		imgTab.y = 800 / 2;
		tileMenu = new TileMenu(this, "tiles/tab_background.png", availableTiles);

		tileTab.addContentTab(tileMenu, imgTab);

		// ######### build the sound Menu ##########
		Image2D imgSoundTab = new Image2D("sounds/tab.png", false, true);
		imgSoundTab.x = ComponentTab.WIDTH + 76 / 2;
		imgSoundTab.y = 800 / 2;
		SoundMenu soundMenu = new SoundMenu(this, "sounds/tab_background.png", availableSounds);

		tileTab.addContentTab(soundMenu, imgSoundTab);

		// ######### build the card Menu ##########
		Image2D imgCardTab = new Image2D("cards/tab.png", false, true);
		imgCardTab.x = ComponentTab.WIDTH + 76 / 2;
		imgCardTab.y = 800 / 2;
		CardMenu cardMenu = new CardMenu(this, "cards/tab_background.png", availableCards);

		tileTab.addContentTab(cardMenu, imgCardTab);

		// add the tile menu to the rendering object tree
		objects2d.add(tileTab);

		// ######## ADD Global icons ##########
		randomPile = new RandomCardZone2D();
		objects2d.add(randomPile);

		// start collapsed
		tileTab.setX(-ComponentTab.WIDTH / 2);

		// ### FILL Random Pile from database
		List<RandomCardPileCard> rndCards = RandomCardPileCardDao.getInstance(context).getRandomCardPileCards(scenario.getId());
		for (RandomCardPileCard rndCard : rndCards) {
			int index = availableCards.indexOf(rndCard.getCard());
			randomPile.addCard(availableCards.get(index));
		}

		// ### FILL Board tile from database
		List<TileInstance> tileInstances = TileInstanceDao.getInstance(context).getTileInstances(scenario.getId());
		for (TileInstance tileInstance : tileInstances) {
			int index = availableCards.indexOf(tileInstance.getTile());
			Tile2D tile = new Tile2D(availableTiles.get(index));
			tile.x = tileInstance.getPosX();
			tile.y = tileInstance.getPosY();
			tile.setRotation(tileInstance.getRotation());
			boardMenu.tileGroup.addObject(tile);
		}

		// ### FILL Board sound from database
		List<SoundInstance> soundInstances = SoundInstanceDao.getInstance(context).getSoundInstances(scenario.getId());
		for (SoundInstance soundInstance : soundInstances) {
			int index = availableSounds.indexOf(soundInstance.getSound());
			Sound2D sound = new Sound2D(availableSounds.get(index));
			sound.x = soundInstance.getPosX();
			sound.y = soundInstance.getPosY();
			boardMenu.soundGroup.addObject(sound);
		}

		// ### FILL Board pile from database
		List<CardPileInstance> cardPileInstances = CardPileInstanceDao.getInstance(context).getCardPileInstances(scenario.getId());
		for (CardPileInstance cardPileInstance : cardPileInstances) {
			CardPile2D pile = new CardPile2D();
			pile.x = cardPileInstance.getPosX();
			pile.y = cardPileInstance.getPosY();
			boardMenu.cardPileGroup.addObject(pile);

			List<CardPileCard> pileCards = CardPileCardDao.getInstance(context).getCardPileCards(cardPileInstance.getId());
			for (CardPileCard pileCard : pileCards) {
				int index = availableCards.indexOf(pileCard.getCard());
				pile.addCard(availableCards.get(index));
			}
		}
	}

	/**
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.amphiprion.gameengine3d.GameScreen#onTouch(android.view.MotionEvent)
	 */
	@Override
	public void onTouch(MotionEvent event) {
		ScreenProperty sp = view.getScreenProperty();
		int nx = (int) (event.getX() / sp.screenScale);
		int ny = (int) (event.getY() / sp.screenScale);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			pointerState = tileTab.onTouch(event, nx, ny, pointerState);
			if (pointerState == PointerState.NONE) {
				pointerState = boardMenu.onTouch(event, nx, ny, pointerState);
			} else if (pointerState == PointerState.ON_TILE_MENU_TAB) {
				clearAnimation(tileMenuTabAnimation);
			}
		} else if (pointerState == PointerState.ON_TILE_MENU_TAB || pointerState == PointerState.ON_TILE_MENU) {
			pointerState = tileTab.onTouch(event, nx, ny, pointerState);
		} else if (pointerState == PointerState.ON_BOARD_TILE || pointerState == PointerState.ON_BOARD || pointerState == PointerState.ON_BOARD_SOUND
				|| pointerState == PointerState.ON_BOARD_CARD_DRAG || pointerState == PointerState.ON_BOARD_CARD_PILE) {
			pointerState = boardMenu.onTouch(event, nx, ny, pointerState);
		}

		// if (event.getAction() == MotionEvent.ACTION_UP) {
		// defineTileIcons();
		// }
	}

	// private void onTouchTileMenu(MotionEvent event, int nx, int ny) {
	// if (event.getAction() == MotionEvent.ACTION_MOVE) {
	// lastPointerDeltaX = nx - lastPointerX;
	// lastPointerDeltaY = ny - lastPointerY;
	// tileMenu.setY(tileMenu.getY() + lastPointerDeltaY);
	// // imgTab.setY(800 / 2);
	// // imgTabBackground.setY(800 / 2);
	// lastPointerX = nx;
	// lastPointerY = ny;
	// if (nx >= TileTab.WIDTH && tileIndex > -1) {
	// pointerState = PointerState.ON_BOARD_TILE;
	// collapseTileMenu();
	// Tile2D tile = new Tile2D(availableTiles.get(tileIndex));
	// tile.x = (int) (nx / boardMenu.getGlobalScale());
	// tile.y = (int) (ny / boardMenu.getGlobalScale());
	// boardMenu.addObject(tile);
	// selectedTile = tile;
	// }
	// } else if (event.getAction() == MotionEvent.ACTION_UP) {
	// pointerState = PointerState.NONE;
	// if (Math.abs(lastPointerDeltaY) > 10) {
	// tileMenuAnimation = new Translation2DAnimation(tileMenu, 1000, 0, 0,
	// lastPointerDeltaY * 20);
	// tileMenuAnimation.setInterpolation(new DecelerateInterpolator());
	// addAnimation(tileMenuAnimation);
	// }
	// }
	// }

	public void collapseTileMenu() {
		tileMenuTabAnimation = new Translation2DAnimation(tileTab, 500, 0, -ComponentTab.WIDTH / 2 - tileTab.getX(), 0);
		tileMenuTabAnimation.setInterpolation(new BounceInterpolator());
		addAnimation(tileMenuTabAnimation);
	}
}
