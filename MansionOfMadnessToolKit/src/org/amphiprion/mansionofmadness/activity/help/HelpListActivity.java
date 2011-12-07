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
package org.amphiprion.mansionofmadness.activity.help;

import java.util.ArrayList;
import java.util.List;

import org.amphiprion.mansionofmadness.ApplicationConstants;
import org.amphiprion.mansionofmadness.R;
import org.amphiprion.mansionofmadness.activity.ILoadTask;
import org.amphiprion.mansionofmadness.activity.IMenuProvider;
import org.amphiprion.mansionofmadness.activity.LoadDataListener;
import org.amphiprion.mansionofmadness.activity.PaginedListActivity;
import org.amphiprion.mansionofmadness.activity.PaginedListContext;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class HelpListActivity extends PaginedListActivity<Help> implements IMenuProvider {
	private LinearLayout ln;

	/**
	 * 
	 */
	public HelpListActivity() {
		super(new PaginedListContext<Help>(1000), R.layout.scenario_list, R.id.scroll_view, R.id.scenario_list, R.string.help_title);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ln = (LinearLayout) findViewById(listViewId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.amphiprion.mansionofmadness.activity.PaginedListActivity#loadDatas
	 * (int, int)
	 */
	@Override
	protected List<Help> loadDatas(int page, int pageSize) {
		return getHelps(this);
	}

	public static List<Help> getHelps(Context context) {
		List<Help> helps = new ArrayList<Help>();
		helps.add(new Help(context, R.string.help_replace_tile_image, R.string.help_replace_tile_image_text));
		helps.add(new Help(context, R.string.help_replace_card_back_image, R.string.help_replace_card_back_image_text));
		helps.add(new Help(context, R.string.help_create_scenario, R.string.help_create_scenario_text));
		helps.add(new Help(context, R.string.help_add_element, R.string.help_add_element_text));
		helps.add(new Help(context, R.string.help_update_element, R.string.help_update_element_text));
		return helps;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.amphiprion.mansionofmadness.activity.PaginedListActivity#getTask(
	 * org.amphiprion.mansionofmadness.activity.LoadDataListener, int, int)
	 */
	@Override
	protected ILoadTask<Help> getTask(LoadDataListener<Help> l, int page, int pageSize) {
		return new LoadHelpTask(l, page, pageSize);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.amphiprion.mansionofmadness.activity.PaginedListActivity#
	 * getDataSummaryView(java.lang.Object)
	 */
	@Override
	protected View getDataSummaryView(Help data) {
		final HelpSummaryView view = new HelpSummaryView(this, data);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int i = ln.indexOfChild(view);
				if (!view.isExpanded()) {
					TextView tv = new TextView(HelpListActivity.this);
					LayoutParams tlp = new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					tv.setLayoutParams(tlp);
					tv.setText(view.getHelp().getHelpDetailId());
					ln.addView(tv, i + 1);
					view.setExpanded(true);
					Animation a = new ScaleAnimation(1f, 1, 0f, 1);
					a.setInterpolator(new BounceInterpolator());
					a.setDuration(500);
					tv.startAnimation(a);
				} else {
					ln.removeViewAt(i + 1);
					view.setExpanded(false);
				}
			}
		});
		// view.setOnLongClickListener(new View.OnLongClickListener() {
		// @Override
		// public boolean onLongClick(View v) {
		// registerForContextMenu(v);
		// openContextMenu(v);
		// unregisterForContextMenu(v);
		// return true;
		// }
		// });
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		buildOptionMenu(menu);

		return true;
	}

	@Override
	public void buildOptionMenu(Menu menu) {
		menu.clear();
		MenuItem mi = menu.add(0, ApplicationConstants.MENU_ID_ASK_NEW_HELP, 0, R.string.help_menu_ask_new_help);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == ApplicationConstants.MENU_ID_ASK_NEW_HELP) {
			final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			emailIntent.setType("plain/text");
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { "amphiprions@gmail.com" });

			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));

			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.help_menu_ask_new_help_body));
			startActivity(emailIntent);
			// context.startActivity(Intent.createChooser(intent,
			// "Send mail..."));

		}
		return true;
	}
}