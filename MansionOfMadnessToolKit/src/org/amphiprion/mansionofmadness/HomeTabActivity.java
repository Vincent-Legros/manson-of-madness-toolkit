package org.amphiprion.mansionofmadness;

import org.amphiprion.mansionofmadness.activity.IMenuProvider;
import org.amphiprion.mansionofmadness.activity.scenario.ScenarioListActivity;
import org.amphiprion.mansionofmadness.activity.sound.SoundListActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class HomeTabActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.home_tab);
		TabHost tabHost = getTabHost(); // The activity TabHost
		tabHost.clearAllTabs();
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, ScenarioListActivity.class);
		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("Scenario").setIndicator(getText(R.string.scenario_title)).setContent(intent);
		tabHost.addTab(spec);

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, SoundListActivity.class);
		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("Sound").setIndicator(getText(R.string.sound_title)).setContent(intent);
		tabHost.addTab(spec);

		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 50;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		menu.add(0, 0, 0, "---");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onMenuOpened(int, android.view.Menu)
	 */
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		menu.clear();
		if (getLocalActivityManager().getCurrentActivity() instanceof IMenuProvider) {
			((IMenuProvider) getLocalActivityManager().getCurrentActivity()).buildOptionMenu(menu);
			return true;
		}
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return getLocalActivityManager().getCurrentActivity().onOptionsItemSelected(item);
	}
}
