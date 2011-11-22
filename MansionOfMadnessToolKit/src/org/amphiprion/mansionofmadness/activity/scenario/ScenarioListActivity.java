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
import org.amphiprion.mansionofmadness.activity.LoadDataListener;
import org.amphiprion.mansionofmadness.activity.PaginedListActivity;
import org.amphiprion.mansionofmadness.activity.PaginedListContext;
import org.amphiprion.mansionofmadness.dao.ScenarioDao;
import org.amphiprion.mansionofmadness.dto.Scenario;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class ScenarioListActivity extends PaginedListActivity<Scenario> {
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
	protected View getDataSummaryView(Scenario data) {
		ScenarioSummaryView view = new ScenarioSummaryView(this, data);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

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
			menu.add(0, ApplicationConstants.MENU_ID_EDIT_SCENARIO, 0, R.string.edit_scenario);
			if (!currentScenario.isEmbedded()) {
				menu.add(0, ApplicationConstants.MENU_ID_RENAME_SCENARIO, 1, R.string.rename_scenario);
			}
		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == ApplicationConstants.MENU_ID_EDIT_SCENARIO) {
			Intent i = new Intent(this, MapActivity.class);
			i.putExtra("SCENARIO", currentScenario);
			// startActivityForResult(i,
			// ApplicationConstants.ACTIVITY_RETURN_MANAGE_SET);
			startActivity(i);
		} else if (item.getItemId() == ApplicationConstants.MENU_ID_RENAME_SCENARIO) {
			updateScenarioName(currentScenario);
		} // } else if (item.getItemId() ==
			// ApplicationConstants.MENU_ID_MANAGE_DECK) {
			// Intent i = new Intent(this, DeckListActivity.class);
			// i.putExtra("GAME", currentGame);
			// startActivityForResult(i,
			// ApplicationConstants.ACTIVITY_RETURN_MANAGE_DECK);
			// } else if (item.getItemId() ==
			// ApplicationConstants.MENU_ID_MANAGE_TABLE) {
			// Intent i = new Intent(this, TableListActivity.class);
			// i.putExtra("GAME", currentGame);
			// startActivityForResult(i,
			// ApplicationConstants.ACTIVITY_RETURN_MANAGE_TABLE);
			// }

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		MenuItem mi = menu.add(0, ApplicationConstants.MENU_ID_CREATE_SCENARIO, 0, R.string.create_scenario);
		mi.setIcon(android.R.drawable.ic_menu_add);

		return true;
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
}