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
package org.amphiprion.mansionofmadness;

import org.amphiprion.gameengine3d.GameActivity;
import org.amphiprion.mansionofmadness.dto.Scenario;
import org.amphiprion.mansionofmadness.screen.map.MapScreen;
import org.amphiprion.mansionofmadness.util.DeviceUtil;

import android.content.Intent;
import android.os.Bundle;

public class MapActivity extends GameActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent i = getIntent();
		Scenario scenario = (Scenario) i.getSerializableExtra("SCENARIO");
		boolean inEdition = i.getBooleanExtra("IN_EDITION", true);
		boolean resumeSession = i.getBooleanExtra("RESUME_SESSION", false);
		view.addScreen(new MapScreen(this, scenario, inEdition, resumeSession));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.amphiprion.gameengine3d.GameActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		DeviceUtil.stopMusic();
	}
}