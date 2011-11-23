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
package org.amphiprion.mansionofmadness.activity.scenario;

import java.util.List;

import org.amphiprion.mansionofmadness.ApplicationConstants;
import org.amphiprion.mansionofmadness.MapActivity;
import org.amphiprion.mansionofmadness.R;
import org.amphiprion.mansionofmadness.activity.ILoadTask;
import org.amphiprion.mansionofmadness.activity.IMenuProvider;
import org.amphiprion.mansionofmadness.activity.LoadDataListener;
import org.amphiprion.mansionofmadness.activity.PaginedListActivity;
import org.amphiprion.mansionofmadness.activity.PaginedListContext;
import org.amphiprion.mansionofmadness.dao.CardPileCardDao;
import org.amphiprion.mansionofmadness.dao.CardPileInstanceDao;
import org.amphiprion.mansionofmadness.dao.ScenarioDao;
import org.amphiprion.mansionofmadness.dao.SoundInstanceDao;
import org.amphiprion.mansionofmadness.dao.TileInstanceDao;
import org.amphiprion.mansionofmadness.dto.CardPileCard;
import org.amphiprion.mansionofmadness.dto.CardPileInstance;
import org.amphiprion.mansionofmadness.dto.Entity.DbState;
import org.amphiprion.mansionofmadness.dto.Scenario;
import org.amphiprion.mansionofmadness.dto.SoundInstance;
import org.amphiprion.mansionofmadness.dto.TileInstance;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class ScenarioListActivity extends PaginedListActivity<Scenario> implements IMenuProvider {
	private Scenario currentScenario;

	/**
	 * 
	 */
	public ScenarioListActivity() {
		super(new PaginedListContext<Scenario>(20), R.layout.scenario_list, R.id.scroll_view, R.id.scenario_list, R.string.empty_scenario_list);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.amphiprion.mansionofmadness.activity.PaginedListActivity#loadDatas
	 * (int, int)
	 */
	@Override
	protected List<Scenario> loadDatas(int page, int pageSize) {
		return ScenarioDao.getInstance(this).getScenarios(page, pageSize);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.amphiprion.mansionofmadness.activity.PaginedListActivity#getTask(
	 * org.amphiprion.mansionofmadness.activity.LoadDataListener, int, int)
	 */
	@Override
	protected ILoadTask<Scenario> getTask(LoadDataListener<Scenario> l, int page, int pageSize) {
		return new LoadScenariosTask(l, page, pageSize);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.amphiprion.mansionofmadness.activity.PaginedListActivity#
	 * getDataSummaryView(java.lang.Object)
	 */
	@Override
	protected View getDataSummaryView(final Scenario data) {
		ScenarioSummaryView view = new ScenarioSummaryView(this, data);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean sessionExists = ScenarioDao.getInstance(ScenarioListActivity.this).isGameInProgress(data);
				if (!sessionExists) {
					startScenario(data);
				} else {
					chooseScenarioExecutionMode(data);
				}
			}
		});
		view.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				registerForContextMenu(v);
				openContextMenu(v);
				unregisterForContextMenu(v);
				return true;
			}
		});
		return view;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		menu.clear();

		if (v instanceof ScenarioSummaryView) {
			currentScenario = ((ScenarioSummaryView) v).getScenario();
			if (ScenarioDao.getInstance(this).isGameInProgress(currentScenario)) {
				menu.add(0, ApplicationConstants.MENU_ID_RESUME_SCENARIO, 0, R.string.resume_scenario);
			}
			menu.add(0, ApplicationConstants.MENU_ID_START_SCENARIO, 0, R.string.start_scenario);

			menu.add(1, ApplicationConstants.MENU_ID_EDIT_SCENARIO, 0, R.string.edit_scenario);
			if (!currentScenario.isEmbedded()) {
				menu.add(1, ApplicationConstants.MENU_ID_RENAME_SCENARIO, 1, R.string.rename_scenario);
				menu.add(1, ApplicationConstants.MENU_ID_DELETE_SCENARIO, 2, R.string.delete_scenario);
			}

			menu.add(2, ApplicationConstants.MENU_ID_COPY_SCENARIO, 0, R.string.scenario_copy);
		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == ApplicationConstants.MENU_ID_EDIT_SCENARIO) {
			ScenarioDao.getInstance(this).initScenario(currentScenario);
			Intent i = new Intent(this, MapActivity.class);
			i.putExtra("SCENARIO", currentScenario);
			i.putExtra("IN_EDITION", true);
			startActivity(i);
		} else if (item.getItemId() == ApplicationConstants.MENU_ID_START_SCENARIO) {
			startScenario(currentScenario);
		} else if (item.getItemId() == ApplicationConstants.MENU_ID_RESUME_SCENARIO) {
			resumeScenario(currentScenario);
		} else if (item.getItemId() == ApplicationConstants.MENU_ID_RENAME_SCENARIO) {
			updateScenarioName(currentScenario);
		} else if (item.getItemId() == ApplicationConstants.MENU_ID_COPY_SCENARIO) {
			showCopyScenarioDialog(currentScenario);
		} else if (item.getItemId() == ApplicationConstants.MENU_ID_DELETE_SCENARIO) {
			checkDeleteScenario(currentScenario);
		}

		return true;
	}

	private void checkDeleteScenario(final Scenario scenario) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(this.getString(R.string.delete_scenario_title));
		builder.setMessage(this.getString(R.string.delete_scenario_message));
		builder.setCancelable(true).setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				deleteScenario(scenario);
			}
		});
		builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {

			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void deleteScenario(Scenario scenario) {
		scenario.setState(DbState.DELETE);
		ScenarioDao.getInstance(this).persist(scenario);
		showDataList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		buildOptionMenu(menu);

		return true;
	}/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.amphiprion.mansionofmadness.activity.IMenuProvider#buildOptionMenu
	 * (android.view.Menu)
	 */

	@Override
	public void buildOptionMenu(Menu menu) {
		menu.clear();
		MenuItem mi = menu.add(0, ApplicationConstants.MENU_ID_CREATE_SCENARIO, 0, R.string.create_scenario);
		mi.setIcon(android.R.drawable.ic_menu_add);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == ApplicationConstants.MENU_ID_CREATE_SCENARIO) {
			updateScenarioName(new Scenario());
		}
		return true;
	}

	private void showCopyScenarioDialog(final Scenario source) {
		String[] _options = getResources().getStringArray(R.array.scenario_copy_options);

		boolean[] _selections = new boolean[_options.length];
		for (int i = 0; i < _selections.length; i++) {
			_selections[i] = true;
		}

		Dialog dlg = new AlertDialog.Builder(this).setTitle(this.getString(R.string.scenario_copy))
				.setMultiChoiceItems(_options, _selections, new DialogSelectionClickHandler(_selections))
				.setPositiveButton(this.getString(R.string.ok), new DialogButtonClickHandler(source, _selections))
				.setNegativeButton(this.getString(R.string.cancel), new DialogButtonClickHandler()).create();
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
		private Scenario source;
		private boolean[] _selection;

		private DialogButtonClickHandler() {
			okButton = false;
		}

		private DialogButtonClickHandler(Scenario source, boolean[] _selection) {
			okButton = true;
			this.source = source;
			this._selection = _selection;
		}

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			if (okButton) {
				copyScenario(source, _selection);
			}
		}

	}

	private void copyScenario(Scenario source, boolean[] options) {
		try {
			TileInstanceDao.getInstance(this).getDatabase().beginTransaction();

			Scenario dest = new Scenario();
			dest.setName(source.getDisplayName() + "_copy");
			ScenarioDao.getInstance(this).persist(dest);

			if (options[0]) {
				List<TileInstance> tileInstances = TileInstanceDao.getInstance(this).getTileInstances(source.getId());
				for (TileInstance i : tileInstances) {
					TileInstance di = new TileInstance();
					di.setPosX(i.getPosX());
					di.setPosY(i.getPosY());
					di.setScenario(dest);
					di.setRotation(i.getRotation());
					di.setTile(i.getTile());
					TileInstanceDao.getInstance(this).persist(di);
				}
			}
			if (options[1]) {
				List<SoundInstance> soundInstances = SoundInstanceDao.getInstance(this).getSoundInstances(source.getId());
				for (SoundInstance i : soundInstances) {
					SoundInstance di = new SoundInstance();
					di.setPosX(i.getPosX());
					di.setPosY(i.getPosY());
					di.setScenario(dest);
					di.setSound(i.getSound());
					SoundInstanceDao.getInstance(this).persist(di);
				}
			}
			if (options[2]) {
				List<CardPileInstance> pileInstances = CardPileInstanceDao.getInstance(this).getCardPileInstances(source.getId());
				for (CardPileInstance i : pileInstances) {
					CardPileInstance di = new CardPileInstance();
					di.setPosX(i.getPosX());
					di.setPosY(i.getPosY());
					di.setScenario(dest);
					CardPileInstanceDao.getInstance(this).persist(di);
					if (options[3]) {
						int index = 0;
						List<CardPileCard> contents = CardPileCardDao.getInstance(this).getCardPileCards(i.getId());
						for (CardPileCard c : contents) {
							CardPileCard dc = new CardPileCard();
							dc.setCard(c.getCard());
							dc.setCardPileInstance(di);
							dc.setOrder(index++);
							CardPileCardDao.getInstance(this).persist(dc);
						}
					}
				}
			}

			TileInstanceDao.getInstance(this).getDatabase().setTransactionSuccessful();
			showDataList();
		} finally {
			TileInstanceDao.getInstance(this).getDatabase().endTransaction();
		}
	}

	private void updateScenarioName(final Scenario scenario) {
		final EditText input = new EditText(ScenarioListActivity.this);
		if (scenario.getDisplayName() != null) {
			input.setText(scenario.getDisplayName());
		}
		new AlertDialog.Builder(ScenarioListActivity.this).setTitle(R.string.scenario_edit_name).setView(input)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						String name = input.getText().toString();
						if (name != null && !"".equals(name)) {
							scenario.setName(name);
							scenario.setEmbedded(false);
							ScenarioDao.getInstance(ScenarioListActivity.this).persist(scenario);
							showDataList();
						}
					}
				}).setNegativeButton(getText(R.string.cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						//
					}
				}).show();
	}

	private void startScenario(Scenario scenario) {
		ScenarioDao.getInstance(this).initScenario(scenario);

		Intent i = new Intent(this, MapActivity.class);
		i.putExtra("SCENARIO", scenario);
		i.putExtra("IN_EDITION", false);
		i.putExtra("RESUME_SESSION", false);
		startActivity(i);
	}

	private void resumeScenario(Scenario scenario) {
		Intent i = new Intent(this, MapActivity.class);
		i.putExtra("SCENARIO", scenario);
		i.putExtra("IN_EDITION", false);
		i.putExtra("RESUME_SESSION", true);
		startActivity(i);
	}

	private void chooseScenarioExecutionMode(final Scenario scenario) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(this.getString(R.string.session_exist_title));
		builder.setMessage(this.getString(R.string.session_exist_message));
		builder.setCancelable(true).setPositiveButton(getResources().getString(R.string.bt_resume), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				resumeScenario(scenario);
			}
		});
		builder.setNegativeButton(getResources().getString(R.string.bt_new), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				startScenario(scenario);
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
}