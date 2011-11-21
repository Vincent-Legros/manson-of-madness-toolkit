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

import org.amphiprion.mansionofmadness.ApplicationConstants;
import org.amphiprion.mansionofmadness.R;
import org.amphiprion.mansionofmadness.dto.Scenario;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * View used to display a scenario in the scenario list.
 * 
 * @author amphiprion
 * 
 */
public class ScenarioSummaryView extends LinearLayout {
	private Scenario scenario;
	private static Interpolator interpolator = new BounceInterpolator();

	/**
	 * Construct an scenario view.
	 * 
	 * @param context
	 *            the context
	 * @param scenario
	 *            the scenario entity
	 */
	public ScenarioSummaryView(Context context, Scenario scenario) {
		super(context);
		this.scenario = scenario;
		LayoutParams lp = new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		setLayoutParams(lp);
		setBackgroundDrawable(context.getResources().getDrawable(R.drawable.list_item_black_background_states));

		// addView(createIcon());

		addView(createAccountLayout());
		// addView(createSetIcon());
		// addView(createDeckIcon());
	}

	/**
	 * @return the scenario
	 */
	public Scenario getScenario() {
		return scenario;
	}

	/**
	 * Create the account layout view
	 * 
	 * @return the view
	 */
	private View createAccountLayout() {
		LinearLayout accountLayout = new LinearLayout(getContext());
		LayoutParams aclp = new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 3);
		accountLayout.setOrientation(VERTICAL);
		accountLayout.setLayoutParams(aclp);
		TextView t = new TextView(getContext());
		LayoutParams tlp = new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

		t.setLayoutParams(tlp);
		if (scenario.isEmbedded()) {
			t.setText(getContext().getText(getContext().getResources().getIdentifier("scenario_" + scenario.getName(), "string", ApplicationConstants.PACKAGE)));
		} else {
			t.setText(scenario.getName());
		}
		t.setTextSize(16);
		t.setTypeface(Typeface.DEFAULT_BOLD);
		t.setTextColor(getContext().getResources().getColor(R.color.white));
		accountLayout.addView(t);

		// t = new TextView(getContext());
		// tlp = new
		// LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
		// android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		//
		// t.setLayoutParams(tlp);
		// t.setText(getContext().getString(R.string.number_of_sets,
		// game.getGameSetCount()));
		// accountLayout.addView(t);

		return accountLayout;
	}

}