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

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.amphiprion.gameengine3d.Group2D;
import org.amphiprion.gameengine3d.IObject2D;
import org.amphiprion.gameengine3d.ScreenProperty;
import org.amphiprion.gameengine3d.mesh.Image2D;
import org.amphiprion.mansionofmadness.ApplicationConstants;
import org.amphiprion.mansionofmadness.R;
import org.amphiprion.mansionofmadness.dao.CardPileCardDao;
import org.amphiprion.mansionofmadness.dao.CardPileInstanceDao;
import org.amphiprion.mansionofmadness.dao.RandomCardPileCardDao;
import org.amphiprion.mansionofmadness.dao.SoundInstanceDao;
import org.amphiprion.mansionofmadness.dao.TileInstanceDao;
import org.amphiprion.mansionofmadness.dto.Card;
import org.amphiprion.mansionofmadness.dto.CardPileCard;
import org.amphiprion.mansionofmadness.dto.CardPileInstance;
import org.amphiprion.mansionofmadness.dto.Entity;
import org.amphiprion.mansionofmadness.dto.Entity.DbState;
import org.amphiprion.mansionofmadness.dto.RandomCardPileCard;
import org.amphiprion.mansionofmadness.dto.SoundInstance;
import org.amphiprion.mansionofmadness.dto.TileInstance;
import org.amphiprion.mansionofmadness.screen.map.MapScreen.ComponentKey;
import org.amphiprion.mansionofmadness.util.DeviceUtil;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * @author ng00124c
 * 
 */
public class BoardMenu extends TouchableGroup2D {
	private List<Entity> deletedEntities = new ArrayList<Entity>();

	private MapScreen mapScreen;
	private Tile2D selectedTile;
	private Sound2D selectedSound;
	private CardDrag2D selectedCardDrag;
	private CardPile2D selectedCardPile;

	private int lastPointerX;
	private int lastPointerY;
	private int lastPointerDeltaX;
	private int lastPointerDeltaY;
	private float lastPointerDist;
	private long lastPointerDownTime;

	protected Group2D tileGroup;
	protected Group2D cardPileGroup;
	protected Group2D soundGroup;

	/**
	 * 
	 */
	public BoardMenu(MapScreen mapScreen) {
		this.mapScreen = mapScreen;
		tileGroup = new Group2D();
		cardPileGroup = new Group2D();
		soundGroup = new Group2D();
		super.addObject(tileGroup);
		super.addObject(cardPileGroup);
		super.addObject(soundGroup);
	}

	public void addAndSelectTile(Tile2D tile, int nx, int ny) {
		tileGroup.addObject(tile);

		clearTileIcons();
		selectedTile = tile;
		selectedSound = null;
		selectedCardDrag = null;
		selectedCardPile = null;

		lastPointerX = nx;
		lastPointerY = ny;
	}

	public void addAndSelectSound(Sound2D sound, int nx, int ny) {
		soundGroup.addObject(sound);

		clearTileIcons();
		selectedTile = null;
		selectedSound = sound;
		selectedCardDrag = null;
		selectedCardPile = null;

		lastPointerX = nx;
		lastPointerY = ny;
	}

	public void addAndSelectCardDrag(CardDrag2D card, int nx, int ny) {
		super.addObject(card);

		clearTileIcons();
		selectedTile = null;
		selectedSound = null;
		selectedCardDrag = card;
		selectedCardPile = null;

		lastPointerX = nx;
		lastPointerY = ny;
	}

	public void addAndSelectCardPile(CardPile2D cardPile, int nx, int ny) {
		cardPileGroup.addObject(cardPile);

		clearTileIcons();
		selectedTile = null;
		selectedSound = null;
		selectedCardDrag = null;
		selectedCardPile = cardPile;

		lastPointerX = nx;
		lastPointerY = ny;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.amphiprion.mansionofmadness.screen.map.TouchableGroup2D#onTouch(android
	 * .view.MotionEvent, int, int,
	 * org.amphiprion.mansionofmadness.screen.map.TouchableGroup2D.PointerState)
	 */
	@Override
	public PointerState onTouch(MotionEvent event, int nx, int ny, PointerState current) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			lastPointerX = nx;
			lastPointerY = ny;
			lastPointerDeltaX = 0;
			lastPointerDeltaY = 0;

			lastPointerDownTime = System.currentTimeMillis();
			if (mapScreen.inEdition && selectedTile != null) {
				Image2D img = (Image2D) mapScreen.getHMIComponent(MapScreen.ComponentKey.MOVE_ICON);
				if (img.contains(nx, ny)) {
					clearTileIcons();
					return PointerState.ON_BOARD_TILE;
				}

				img = (Image2D) mapScreen.getHMIComponent(MapScreen.ComponentKey.ADD_CARD_PILE_ICON);
				if (img.contains(nx, ny)) {
					clearTileIcons();
					selectedCardPile = new CardPile2D(new CardPileInstance());
					selectedCardPile.x = nx;
					selectedCardPile.y = ny;
					addAndSelectCardPile(selectedCardPile, nx, ny);
					return PointerState.ON_BOARD_CARD_PILE;
				}

				img = (Image2D) mapScreen.getHMIComponent(MapScreen.ComponentKey.DELETE_ICON);
				if (img.contains(nx, ny)) {
					clearTileIcons();
					tileGroup.removeObject(selectedTile);
					if (selectedTile.getTileInstance().getState() != DbState.NEW) {
						deletedEntities.add(selectedTile.getTileInstance());
					}
					selectedTile = null;
					return PointerState.NONE;
				}
				img = (Image2D) mapScreen.getHMIComponent(MapScreen.ComponentKey.ROTATE_CLOCK_ICON);
				if (img.contains(nx, ny)) {
					clearTileIcons();
					selectedTile.setRotation(selectedTile.getRotation() + 90);
					anchorTile(selectedTile);
					defineTileIcons();
					return PointerState.NONE;
				}
				img = (Image2D) mapScreen.getHMIComponent(MapScreen.ComponentKey.ROTATE_COUNTER_CLOCK_ICON);
				if (img.contains(nx, ny)) {
					clearTileIcons();
					selectedTile.setRotation(selectedTile.getRotation() + 270);
					anchorTile(selectedTile);
					defineTileIcons();
					return PointerState.NONE;
				}
			} else if (mapScreen.inEdition && selectedSound != null) {
				Image2D img = (Image2D) mapScreen.getHMIComponent(MapScreen.ComponentKey.MOVE_ICON);
				if (img.contains(nx, ny)) {
					clearTileIcons();
					return PointerState.ON_BOARD_SOUND;
				}
				img = (Image2D) mapScreen.getHMIComponent(MapScreen.ComponentKey.DELETE_ICON);
				if (img.contains(nx, ny)) {
					clearTileIcons();
					soundGroup.removeObject(selectedSound);
					if (selectedSound.getSoundInstance().getState() != DbState.NEW) {
						deletedEntities.add(selectedSound.getSoundInstance());
					}
					selectedSound = null;
					return PointerState.NONE;
				}
				img = (Image2D) mapScreen.getHMIComponent(MapScreen.ComponentKey.PLAY_ICON);
				if (img.contains(nx, ny)) {
					DeviceUtil.playSound(selectedSound.getSound());
					return PointerState.NONE;
				}
			} else if (mapScreen.inEdition && selectedCardPile != null) {
				Image2D img = (Image2D) mapScreen.getHMIComponent(MapScreen.ComponentKey.MOVE_ICON);
				if (img.contains(nx, ny)) {
					clearTileIcons();
					return PointerState.ON_BOARD_CARD_PILE;
				}
				img = (Image2D) mapScreen.getHMIComponent(MapScreen.ComponentKey.DELETE_ICON);
				if (img.contains(nx, ny)) {
					clearTileIcons();
					cardPileGroup.removeObject(selectedCardPile);
					if (selectedCardPile.getCardPileInstance().getState() != DbState.NEW) {
						deletedEntities.add(selectedCardPile.getCardPileInstance());
					}
					selectedCardPile = null;
					return PointerState.NONE;
				}
				if (selectedCardPile.count() > 0) {
					img = (Image2D) mapScreen.getHMIComponent(MapScreen.ComponentKey.EDIT_CARD_PILE_ICON);
					if (img.contains(nx, ny)) {
						clearTileIcons();
						editCardInPile(selectedCardPile);
						selectedCardPile = null;
						return PointerState.NONE;
					}
				}
			}
			return PointerState.ON_BOARD;
		} else if (current == PointerState.ON_BOARD_CARD_PILE) {
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				lastPointerDeltaX = nx - lastPointerX;
				lastPointerDeltaY = ny - lastPointerY;
				selectedCardPile.x += (int) (lastPointerDeltaX / getGlobalScale());
				selectedCardPile.y += (int) (lastPointerDeltaY / getGlobalScale());
				lastPointerX = nx;
				lastPointerY = ny;
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				defineCardPileIcons();
				return PointerState.NONE;
			}
			return current;
		} else if (current == PointerState.ON_BOARD_TILE) {
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				lastPointerDeltaX = nx - lastPointerX;
				lastPointerDeltaY = ny - lastPointerY;
				selectedTile.x += (int) (lastPointerDeltaX / getGlobalScale());
				selectedTile.y += (int) (lastPointerDeltaY / getGlobalScale());
				lastPointerX = nx;
				lastPointerY = ny;
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				anchorTile(selectedTile);
				defineTileIcons();
				return PointerState.NONE;
			}
			return current;
		} else if (current == PointerState.ON_BOARD_SOUND) {
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				lastPointerDeltaX = nx - lastPointerX;
				lastPointerDeltaY = ny - lastPointerY;
				selectedSound.x += (int) (lastPointerDeltaX / getGlobalScale());
				selectedSound.y += (int) (lastPointerDeltaY / getGlobalScale());
				lastPointerX = nx;
				lastPointerY = ny;
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				defineSoundIcons();
				return PointerState.NONE;
			}
			return current;
		} else if (current == PointerState.ON_BOARD_CARD_DRAG) {
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				lastPointerDeltaX = nx - lastPointerX;
				lastPointerDeltaY = ny - lastPointerY;
				selectedCardDrag.x += (int) (lastPointerDeltaX / getGlobalScale());
				selectedCardDrag.y += (int) (lastPointerDeltaY / getGlobalScale());
				lastPointerX = nx;
				lastPointerY = ny;
				if (mapScreen.randomPile.contains(nx, ny)) {
					mapScreen.randomPile.setScale(1.3f);
				} else {
					mapScreen.randomPile.setScale(1);
					boolean alreadyAdded = false;
					for (IObject2D o : cardPileGroup.getObjects()) {
						if (o instanceof CardPile2D) {
							((CardPile2D) o).setScale(1);
							if (!alreadyAdded && ((CardPile2D) o).contains(nx, ny)) {
								((CardPile2D) o).setScale(1.3f);
								alreadyAdded = true;
							}
						}
					}
				}
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				// defineSoundIcons();
				super.removeObject(selectedCardDrag);
				if (mapScreen.randomPile.contains(nx, ny)) {
					mapScreen.randomPile.addCard(selectedCardDrag.getCard());
				} else {
					boolean alreadyAdded = false;
					for (IObject2D o : cardPileGroup.getObjects()) {
						if (o instanceof CardPile2D) {
							((CardPile2D) o).setScale(1);
							if (!alreadyAdded && ((CardPile2D) o).contains(nx, ny)) {
								CardPileCard cpc = new CardPileCard();
								cpc.setCard(selectedCardDrag.getCard());
								cpc.setCardPileInstance(((CardPile2D) o).getCardPileInstance());
								((CardPile2D) o).addCard(cpc);
								alreadyAdded = true;
							}
						}
					}
				}
				mapScreen.randomPile.setScale(1);
				selectedCardDrag = null;
				return PointerState.NONE;
			}
			return current;
		} else if (current == PointerState.ON_BOARD) {
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if (event.getPointerCount() > 1) {
					// zoom
					ScreenProperty sp = mapScreen.getView().getScreenProperty();
					int nx2 = (int) (event.getX(1) / sp.screenScale);
					int ny2 = (int) (event.getY(1) / sp.screenScale);
					float newDist = (float) Math.sqrt((nx - nx2) * (nx - nx2) + (ny - ny2) * (ny - ny2));
					float deltaX = (nx + nx2) / 2 - lastPointerX;
					float deltaY = (ny + ny2) / 2 - lastPointerY;

					setX((int) (getX() + deltaX / getGlobalScale()));
					setY((int) (getY() + deltaY / getGlobalScale()));

					lastPointerX = (nx + nx2) / 2;
					lastPointerY = (ny + ny2) / 2;
					float x = lastPointerX / getGlobalScale() - getX();
					float y = lastPointerY / getGlobalScale() - getY();
					setGlobalScale(getGlobalScale() * newDist / lastPointerDist);
					setX((int) (lastPointerX / getGlobalScale() - x));
					setY((int) (lastPointerY / getGlobalScale() - y));
					lastPointerDist = newDist;
				} else {
					// move
					lastPointerDeltaX = nx - lastPointerX;
					lastPointerDeltaY = ny - lastPointerY;
					setX(getX() + (int) (lastPointerDeltaX / getGlobalScale()));
					setY(getY() + (int) (lastPointerDeltaY / getGlobalScale()));
					lastPointerX = nx;
					lastPointerY = ny;
				}
				return current;
			} else if (event.getAction() == MotionEvent.ACTION_POINTER_2_DOWN) {
				ScreenProperty sp = mapScreen.getView().getScreenProperty();
				int nx2 = (int) (event.getX(1) / sp.screenScale);
				int ny2 = (int) (event.getY(1) / sp.screenScale);
				lastPointerX = (nx + nx2) / 2;
				lastPointerY = (ny + ny2) / 2;
				lastPointerDist = (float) Math.sqrt((nx - nx2) * (nx - nx2) + (ny - ny2) * (ny - ny2));
				return current;
			} else if (event.getAction() == MotionEvent.ACTION_POINTER_2_UP) {
				lastPointerDeltaX = 0;
				lastPointerDeltaY = 0;
				lastPointerX = nx;
				lastPointerY = ny;
				return current;
			} else if (event.getAction() == MotionEvent.ACTION_POINTER_1_UP) {
				ScreenProperty sp = mapScreen.getView().getScreenProperty();
				int nx2 = (int) (event.getX(1) / sp.screenScale);
				int ny2 = (int) (event.getY(1) / sp.screenScale);
				lastPointerDeltaX = 0;
				lastPointerDeltaY = 0;
				lastPointerX = nx2;
				lastPointerY = ny2;
				return current;
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				if (System.currentTimeMillis() - lastPointerDownTime < 300) {
					// simple click, try to select of tile
					clearTileIcons();
					selectedSound = null;
					selectedCardPile = null;
					selectedTile = null;
					if (mapScreen.inEdition && mapScreen.randomPile.contains(nx, ny)) {
						// TODO Remove the dump
						// dump();
						editCardInPile(mapScreen.randomPile);
					} else if (mapScreen.inEdition && mapScreen.saveButton.contains(nx, ny)) {
						saveScenario();
					} else {
						for (IObject2D o : soundGroup.getObjects()) {
							if (o instanceof Sound2D && ((Sound2D) o).contains(nx, ny)) {
								selectedSound = (Sound2D) o;
								if (mapScreen.inEdition) {
									defineSoundIcons();
								} else {
									DeviceUtil.playSound(selectedSound.getSound());
								}
								break;
							}
						}
						if (selectedSound == null) {
							for (IObject2D o : cardPileGroup.getObjects()) {
								if (o instanceof CardPile2D && ((CardPile2D) o).contains(nx, ny)) {
									selectedCardPile = (CardPile2D) o;
									if (mapScreen.inEdition) {
										defineCardPileIcons();
									} else {
										editCardInPile(selectedCardPile);
									}
									break;

								}
							}
						}
						if (mapScreen.inEdition && selectedSound == null && selectedCardPile == null) {
							for (IObject2D o : tileGroup.getObjects()) {
								if (o instanceof Tile2D && ((Tile2D) o).contains(nx, ny)) {
									selectedTile = (Tile2D) o;
									defineTileIcons();
									break;
								}
							}
						}
					}
				}
				return PointerState.NONE;
			} else {
				return current;
			}
		} else {
			return current;
		}
	}

	private void anchorTile(Tile2D tileToAnchor) {
		int left = tileToAnchor.x - 150 * tileToAnchor.getTileWidth() / 2 - getX();
		int top = tileToAnchor.y - 150 * tileToAnchor.getTileHeight() / 2 - getY();
		if (left < 0) {
			tileToAnchor.x += (left - 75) / 150 * 150 - left;
		} else {
			tileToAnchor.x += (left + 75) / 150 * 150 - left;
		}
		if (top < 0) {
			tileToAnchor.y += (top - 75) / 150 * 150 - top;
		} else {
			tileToAnchor.y += (top + 75) / 150 * 150 - top;
		}
	}

	private void clearTileIcons() {
		removeObject(mapScreen.getHMIComponent(ComponentKey.MOVE_ICON));
		removeObject(mapScreen.getHMIComponent(ComponentKey.DELETE_ICON));
		removeObject(mapScreen.getHMIComponent(ComponentKey.ROTATE_CLOCK_ICON));
		removeObject(mapScreen.getHMIComponent(ComponentKey.ROTATE_COUNTER_CLOCK_ICON));
		removeObject(mapScreen.getHMIComponent(ComponentKey.PLAY_ICON));
		removeObject(mapScreen.getHMIComponent(ComponentKey.ADD_CARD_PILE_ICON));
		removeObject(mapScreen.getHMIComponent(ComponentKey.EDIT_CARD_PILE_ICON));
	}

	private void defineCardPileIcons() {
		if (selectedCardPile != null) {
			Image2D img = new Image2D("tiles/icons/move.png");
			img.x = selectedCardPile.x;
			img.y = selectedCardPile.y;
			mapScreen.registerHMIComponent(ComponentKey.MOVE_ICON, img);
			addObject(img);

			img = new Image2D("tiles/icons/close.png");
			img.x = selectedCardPile.x + 64 / 2 + 24;
			img.y = selectedCardPile.y + 64 / 2 + 24;
			mapScreen.registerHMIComponent(ComponentKey.DELETE_ICON, img);
			addObject(img);

			if (selectedCardPile.count() > 0) {
				img = new Image2D("tiles/icons/edit_card_pile.png");
				img.x = selectedCardPile.x + 64 / 2 + 24;
				img.y = selectedCardPile.y - 64 / 2 - 24;
				mapScreen.registerHMIComponent(ComponentKey.EDIT_CARD_PILE_ICON, img);
				addObject(img);
			}
		}
	}

	private void defineSoundIcons() {
		if (selectedSound != null) {
			Image2D img = new Image2D("tiles/icons/move.png");
			img.x = selectedSound.x;
			img.y = selectedSound.y;
			mapScreen.registerHMIComponent(ComponentKey.MOVE_ICON, img);
			addObject(img);

			img = new Image2D("tiles/icons/close.png");
			img.x = selectedSound.x + 64 / 2 + 24;
			img.y = selectedSound.y + 64 / 2 + 24;
			mapScreen.registerHMIComponent(ComponentKey.DELETE_ICON, img);
			addObject(img);

			img = new Image2D("tiles/icons/play.png");
			img.x = selectedSound.x + 64 / 2 + 24;
			img.y = selectedSound.y - 64 / 2 - 24;
			mapScreen.registerHMIComponent(ComponentKey.PLAY_ICON, img);
			addObject(img);

		}
	}

	private void defineTileIcons() {
		if (selectedTile != null) {

			Image2D img = new Image2D("tiles/icons/move.png");
			img.x = selectedTile.x;
			img.y = selectedTile.y;
			mapScreen.registerHMIComponent(ComponentKey.MOVE_ICON, img);
			addObject(img);

			img = new Image2D("tiles/icons/close.png");
			img.x = selectedTile.x + selectedTile.getTileWidth() * 150 / 2 - 32;
			img.y = selectedTile.y + selectedTile.getTileHeight() * 150 / 2 - 32;
			mapScreen.registerHMIComponent(ComponentKey.DELETE_ICON, img);
			addObject(img);

			img = new Image2D("tiles/icons/rotate_clock.png");
			img.x = selectedTile.x + selectedTile.getTileWidth() * 150 / 2 - 32;
			img.y = selectedTile.y - selectedTile.getTileHeight() * 150 / 2 + 32;
			mapScreen.registerHMIComponent(ComponentKey.ROTATE_CLOCK_ICON, img);
			addObject(img);

			img = new Image2D("tiles/icons/rotate_counter_clock.png");
			img.x = selectedTile.x - selectedTile.getTileWidth() * 150 / 2 + 32;
			img.y = selectedTile.y - selectedTile.getTileHeight() * 150 / 2 + 32;
			mapScreen.registerHMIComponent(ComponentKey.ROTATE_COUNTER_CLOCK_ICON, img);
			addObject(img);

			img = new Image2D("tiles/icons/add_card_pile.png");
			img.x = selectedTile.x - selectedTile.getTileWidth() * 150 / 2 + 32;
			img.y = selectedTile.y + selectedTile.getTileHeight() * 150 / 2 - 32;
			mapScreen.registerHMIComponent(ComponentKey.ADD_CARD_PILE_ICON, img);
			addObject(img);
		}
	}

	private void editCardInPile(ICardPile cardPile) {
		List<Card> cards = cardPile.getCards();
		if (cards == null || cards.size() == 0) {
			return;
		}
		CharSequence[] _options = new CharSequence[cards.size()];
		int index = 0;
		for (int i = _options.length - 1; i >= 0; i--) {
			Card card = cards.get(i);
			String txt;
			if (card.isEmbedded()) {
				txt = mapScreen.getContext().getString(
						mapScreen.getContext().getResources().getIdentifier(card.getType() + "_" + card.getName(), "string", ApplicationConstants.PACKAGE));
			} else {
				txt = card.getName();
			}
			_options[index] = txt;
			index++;
		}
		boolean[] _selections = new boolean[_options.length];

		Dialog dlg = new AlertDialog.Builder(mapScreen.getContext()).setTitle(mapScreen.getContext().getString(R.string.card_pile_title))
				.setMultiChoiceItems(_options, _selections, new DialogSelectionClickHandler(_selections))
				.setPositiveButton(mapScreen.getContext().getString(R.string.discard_selected), new DialogButtonClickHandler(cardPile, _selections))
				.setNegativeButton(mapScreen.getContext().getString(R.string.cancel), new DialogButtonClickHandler()).create();
		dlg.show();
	}

	private class DialogSelectionClickHandler implements DialogInterface.OnMultiChoiceClickListener {
		private boolean[] _selections;

		/**
		 * 
		 */
		public DialogSelectionClickHandler(boolean[] _selections) {
			this._selections = _selections;
		}

		@Override
		public void onClick(DialogInterface dialog, int which, boolean isChecked) {
			_selections[which] = isChecked;
		}

	}

	private class DialogButtonClickHandler implements DialogInterface.OnClickListener {
		private boolean okButton;
		private ICardPile cardPile;
		private boolean[] _selection;

		private DialogButtonClickHandler() {
			okButton = false;
		}

		private DialogButtonClickHandler(ICardPile cardPile, boolean[] _selection) {
			okButton = true;
			this.cardPile = cardPile;
			this._selection = _selection;
		}

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			if (okButton) {
				cardPile.removeSelection(mapScreen, _selection);
			}
		}

	}

	private void dump() {
		try {
			FileOutputStream fos = new FileOutputStream("/mnt/sdcard/sql.txt");
			for (Object o : tileGroup.getObjects()) {
				Tile2D tile = (Tile2D) o;
				String sql = "insert into TILE_INSTANCE (" + TileInstance.DbField.ID + "," + TileInstance.DbField.SCENARIO_ID + "," + TileInstance.DbField.POS_X + ","
						+ TileInstance.DbField.POS_Y + "," + TileInstance.DbField.TILE_ID + "," + TileInstance.DbField.ROTATION + ") values ('" + tile.getTileInstance().getId()
						+ "','" + mapScreen.scenario.getId() + "'," + (tile.x - getX()) + "," + (tile.y - getY()) + ",'" + tile.getTile().getId() + "'," + tile.getRotation() + ")";
				fos.write("db.execSQL(\"".getBytes());
				fos.write(sql.getBytes());
				fos.write(" \");".getBytes());
				fos.write("\n".getBytes());
			}
			fos.write("\n".getBytes());

			for (Object o : cardPileGroup.getObjects()) {
				CardPile2D pile = (CardPile2D) o;
				String sql = "insert into CARD_PILE_INSTANCE (" + CardPileInstance.DbField.ID + "," + CardPileInstance.DbField.SCENARIO_ID + "," + CardPileInstance.DbField.POS_X
						+ "," + CardPileInstance.DbField.POS_Y + ") values ('" + pile.getCardPileInstance().getId() + "','" + mapScreen.scenario.getId() + "'," + (pile.x - getX())
						+ "," + (pile.y - getY()) + ")";

				fos.write("db.execSQL(\"".getBytes());
				fos.write(sql.getBytes());
				fos.write(" \");".getBytes());
				fos.write("\n".getBytes());
			}
			fos.flush();
			fos.close();
		} catch (Exception e) {
			Log.e(ApplicationConstants.PACKAGE, "", e);
		}
	}

	protected void saveScenario() {
		// TODO move it into a AsyncTask
		try {
			TileInstanceDao.getInstance(mapScreen.getContext()).getDatabase().beginTransaction();

			for (Object o : tileGroup.getObjects()) {
				Tile2D tile = (Tile2D) o;
				TileInstance instance = tile.getTileInstance();
				instance.setScenario(mapScreen.scenario);
				instance.setTile(tile.getTile());
				instance.setPosX((tile.x - getX()));
				instance.setPosY((tile.y - getY()));
				instance.setRotation(tile.getRotation());

				TileInstanceDao.getInstance(mapScreen.getContext()).persist(instance);
			}

			for (Object o : soundGroup.getObjects()) {
				Sound2D sound = (Sound2D) o;
				SoundInstance instance = sound.getSoundInstance();
				instance.setScenario(mapScreen.scenario);
				instance.setSound(sound.getSound());
				instance.setPosX((sound.x - getX()));
				instance.setPosY((sound.y - getY()));

				SoundInstanceDao.getInstance(mapScreen.getContext()).persist(instance);
			}

			for (Object o : cardPileGroup.getObjects()) {
				CardPile2D pile = (CardPile2D) o;
				CardPileInstance instance = pile.getCardPileInstance();
				instance.setScenario(mapScreen.scenario);
				instance.setPosX((pile.x - getX()));
				instance.setPosY((pile.y - getY()));

				CardPileInstanceDao.getInstance(mapScreen.getContext()).persist(instance);

				// save content
				CardPileCardDao.getInstance(mapScreen.getContext()).deleteAll(instance);
				int index = 0;
				for (Card card : pile.getCards()) {
					CardPileCard content = new CardPileCard();
					content.setCard(card);
					content.setCardPileInstance(instance);
					content.setOrder(index++);
					CardPileCardDao.getInstance(mapScreen.getContext()).persist(content);
				}
			}

			RandomCardPileCardDao.getInstance(mapScreen.getContext()).deleteAll(mapScreen.scenario);
			int index = 0;
			for (Card card : mapScreen.randomPile.getCards()) {
				RandomCardPileCard content = new RandomCardPileCard();
				content.setCard(card);
				content.setScenario(mapScreen.scenario);
				content.setOrder(index++);
				RandomCardPileCardDao.getInstance(mapScreen.getContext()).persist(content);
			}

			// DELETION
			for (Entity entity : deletedEntities) {
				entity.setState(DbState.DELETE);
				if (entity instanceof TileInstance) {
					TileInstanceDao.getInstance(mapScreen.getContext()).persist((TileInstance) entity);
				} else if (entity instanceof SoundInstance) {
					SoundInstanceDao.getInstance(mapScreen.getContext()).persist((SoundInstance) entity);
				} else if (entity instanceof CardPileInstance) {
					CardPileInstanceDao.getInstance(mapScreen.getContext()).persist((CardPileInstance) entity);
				}
			}

			TileInstanceDao.getInstance(mapScreen.getContext()).getDatabase().setTransactionSuccessful();
			deletedEntities.clear();
			Toast.makeText(mapScreen.getContext(), "Map Saved", 1000).show();
		} finally {
			TileInstanceDao.getInstance(mapScreen.getContext()).getDatabase().endTransaction();
		}
	}

}
