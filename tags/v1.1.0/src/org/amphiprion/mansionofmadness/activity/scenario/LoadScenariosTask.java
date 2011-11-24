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

import org.amphiprion.mansionofmadness.activity.ILoadTask;
import org.amphiprion.mansionofmadness.activity.LoadDataListener;
import org.amphiprion.mansionofmadness.dao.ScenarioDao;
import org.amphiprion.mansionofmadness.dto.Scenario;

import android.os.AsyncTask;

/**
 * @author amphiprion
 * 
 */
public class LoadScenariosTask extends AsyncTask<Void, Integer, List<Scenario>> implements ILoadTask<Scenario> {
	private LoadDataListener<Scenario> caller;
	private int pageIndex;
	private int pageSize;

	/**
	 * Default constructor.
	 */
	public LoadScenariosTask(LoadDataListener<Scenario> caller, int pageIndex, int pageSize) {
		this.caller = caller;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;

	}

	@Override
	protected List<Scenario> doInBackground(Void... v) {
		try {
			List<Scenario> scenarios = ScenarioDao.getInstance(caller.getContext()).getScenarios(pageIndex, pageSize);
			return scenarios;

		} catch (Exception e) {
			return null;
		}
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
	}

	@Override
	protected void onPostExecute(List<Scenario> scenarios) {
		caller.importEnded(!isCancelled() && scenarios != null, scenarios);
	}

}
