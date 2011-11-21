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

import java.util.ArrayList;
import java.util.List;

import org.amphiprion.mansionofmadness.ApplicationConstants;
import org.amphiprion.mansionofmadness.MapActivity;
import org.amphiprion.mansionofmadness.R;
import org.amphiprion.mansionofmadness.activity.scenario.LoadScenariosTask.LoadScenarioListener;
import org.amphiprion.mansionofmadness.dao.ScenarioDao;
import org.amphiprion.mansionofmadness.dto.Scenario;
import org.amphiprion.mansionofmadness.view.MyScrollView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ScenarioListActivity extends Activity {
	private Scenario currentScenario;
	private ScenarioListContext scenarioListContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.scenario_list);
		showScenarioList();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		menu.clear();

		if (v instanceof ScenarioSummaryView) {
			currentScenario = ((ScenarioSummaryView) v).getScenario();
			menu.add(0, ApplicationConstants.MENU_ID_EDIT_SCENARIO, 0, R.string.edit_scenario);
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

	public void showScenarioList() {
		scenarioListContext = new ScenarioListContext();

		final Rect r = new Rect();
		scenarioListContext.scrollView = (MyScrollView) findViewById(R.id.scroll_view);
		scenarioListContext.scrollView.setOnScrollChanged(new OnScrollChangedListener() {
			@Override
			public void onScrollChanged() {
				if (!scenarioListContext.allLoaded && !scenarioListContext.loading) {
					LinearLayout ln = (LinearLayout) scenarioListContext.scrollView.getChildAt(0);
					if (ln.getChildCount() > 3) {
						boolean b = ln.getChildAt(ln.getChildCount() - 3).getLocalVisibleRect(r);
						if (b) {
							scenarioListContext.loading = true;
							loadGameNextPage();
						}
					}
				}
			}
		});
		initGameList();

	}

	private void initGameList() {
		scenarioListContext.loadedPage = 0;
		if (scenarioListContext.scenarios == null) {
			scenarioListContext.scenarios = new ArrayList<Scenario>();
		} else {
			scenarioListContext.scenarios.clear();
		}
		loadGameNextPage();
	}

	private void loadGameNextPage() {
		if (scenarioListContext.loadedPage == 0) {
			// int nb =
			// GameDao.getInstance(this).getGameCount(gameListContext.collection,
			// gameListContext.search, gameListContext.query);
			// Toast.makeText(this,
			// getResources().getString(R.string.message_nb_result, nb),
			// Toast.LENGTH_LONG).show();
			List<Scenario> newScenarios = ScenarioDao.getInstance(this).getScenarios(scenarioListContext.loadedPage, ScenarioListContext.PAGE_SIZE);
			importScenarioEnded(true, newScenarios);
		} else {
			LoadScenarioListener l = new LoadScenarioListener() {

				@Override
				public void importEnded(boolean succeed, List<Scenario> scenarios) {
					importScenarioEnded(succeed, scenarios);
				}

				@Override
				public Context getContext() {
					return ScenarioListActivity.this;
				}
			};
			scenarioListContext.task = new LoadScenariosTask(l, scenarioListContext.loadedPage, ScenarioListContext.PAGE_SIZE);
			scenarioListContext.task.execute();
		}
	}

	public void importScenarioEnded(boolean succeed, List<Scenario> newScenarios) {
		if (succeed) {
			scenarioListContext.task = null;
			if (newScenarios != null && newScenarios.size() > 0) {
				if (newScenarios.size() == ScenarioListContext.PAGE_SIZE + 1) {
					newScenarios.remove(ScenarioListContext.PAGE_SIZE);
					scenarioListContext.allLoaded = false;
				} else {
					scenarioListContext.allLoaded = true;
				}
			} else {
				scenarioListContext.allLoaded = true;
			}
			if (scenarioListContext.loadedPage != 0) {
				addScenarioElementToList(newScenarios);
			} else {
				scenarioListContext.scenarios = newScenarios;
				buildScenarioList();
			}
			scenarioListContext.loadedPage++;
		}
		scenarioListContext.loading = false;

	}

	private void buildScenarioList() {

		LinearLayout ln = (LinearLayout) findViewById(R.id.scenario_list);
		ln.removeAllViews();
		if (scenarioListContext.scenarios != null && scenarioListContext.scenarios.size() > 0) {
			addScenarioElementToList(scenarioListContext.scenarios);
		} else {
			TextView tv = new TextView(this);
			tv.setText(R.string.empty_scenario_list);
			ln.addView(tv);
		}
	}

	private void addScenarioElementToList(List<Scenario> newScenarios) {
		LinearLayout ln = (LinearLayout) findViewById(R.id.scenario_list);

		if (newScenarios != scenarioListContext.scenarios) {
			scenarioListContext.scenarios.addAll(newScenarios);
			if (ln.getChildCount() > 0) {
				ln.removeViewAt(ln.getChildCount() - 1);
			}
		}
		for (final Scenario scenario : newScenarios) {
			ScenarioSummaryView view = new ScenarioSummaryView(this, scenario);
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

			ln.addView(view);
		}

		if (!scenarioListContext.allLoaded) {
			ln.addView(getProgressView());
		}
	}

	private View getProgressView() {
		LinearLayout lnExpand = new LinearLayout(this);
		LayoutParams lp = new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lnExpand.setLayoutParams(lp);
		lnExpand.setBackgroundColor(getResources().getColor(R.color.grey));

		ProgressBar im = new ProgressBar(this);
		LayoutParams imglp = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		imglp.gravity = Gravity.CENTER_VERTICAL;
		imglp.rightMargin = 5;
		im.setLayoutParams(imglp);
		lnExpand.addView(im);

		TextView tv = new TextView(this);
		tv.setText(getResources().getText(R.string.loading));
		LayoutParams tlp = new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1);
		tlp.gravity = Gravity.CENTER_VERTICAL;

		tv.setLayoutParams(tlp);
		lnExpand.addView(tv);

		return lnExpand;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();

		// MenuItem search = menu.add(0,
		// ApplicationConstants.MENU_ID_IMPORT_GAME, 1, R.string.import_game);
		// search.setIcon(android.R.drawable.ic_menu_upload);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// if (item.getItemId() == ApplicationConstants.MENU_ID_IMPORT_GAME) {
		// final File importGameDir = new
		// File(Environment.getExternalStorageDirectory() + "/" +
		// ApplicationConstants.DIRECTORY_IMPORT_GAMES);
		// final String[] files = importGameDir.list();
		// if (files == null || files.length == 0) {
		// DialogUtil.showConfirmDialog(this,
		// getResources().getString(R.string.empty_import_game_dir,
		// ApplicationConstants.DIRECTORY_IMPORT_GAMES));
		// } else {
		// AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setTitle(getResources().getString(R.string.import_game));
		// builder.setAdapter(new StringAdapter(ScenarioListActivity.this,
		// files), new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int item) {
		// dialog.dismiss();
		// File file = new File(importGameDir, files[item]);
		// ImportGameDriver task = DriverManager.getImportGameDriver(file);
		// if (task != null) {
		// task.importGame(new ImportGameListener() {
		//
		// @Override
		// public void importEnded(boolean succeed, Game game, Exception
		// exception) {
		// Log.d(ApplicationConstants.PACKAGE, "import ended");
		// if (exception != null) {
		// DialogUtil.showErrorDialog(ScenarioListActivity.this,
		// getResources().getString(R.string.an_error_occurs), exception);
		// } else if (succeed) {
		// Log.d(ApplicationConstants.PACKAGE, "on refresh");
		// initGameList();
		// }
		//
		// }
		//
		// @Override
		// public Context getContext() {
		// return ScenarioListActivity.this;
		// }
		// }, file);
		// // Intent i = new Intent(OperationList.this,
		// // DefineImportParameter.class);
		// // i.putExtra("ACCOUNT", account);
		// // i.putExtra("FILE_DRIVER_INDEX", item);
		// // startActivityForResult(i,
		// // ApplicationConstants.ACTIVITY_RETURN_IMPORT_OPERATION);
		// } else {
		// DialogUtil.showConfirmDialog(ScenarioListActivity.this,
		// getResources().getString(R.string.no_driver, files[item]));
		// }
		// }
		// });
		// AlertDialog alert = builder.create();
		// alert.show();
		// }
		// }

		// if (item.getItemId() ==
		// ApplicationConstants.MENU_ID_CHOOSE_EXISTING_SEARCH) {
		// chooseSearchFilter();
		// } else if (item.getItemId() ==
		// ApplicationConstants.MENU_ID_CLEAR_SEARCH) {
		// gameListContext.query = null;
		// gameListContext.search = null;
		// initGameList();
		// } else if (item.getItemId() == ApplicationConstants.MENU_ID_SEARCH) {
		// onSearchRequested();
		// }

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if (requestCode == ApplicationConstants.ACTIVITY_RETURN_MANAGE_SET) {
		// initGameList();
		// } else if (requestCode ==
		// ApplicationConstants.ACTIVITY_RETURN_MANAGE_DECK) {
		// initGameList();
		// } else if (requestCode ==
		// ApplicationConstants.ACTIVITY_RETURN_MANAGE_TABLE) {
		// initGameList();
		// }

	}

}