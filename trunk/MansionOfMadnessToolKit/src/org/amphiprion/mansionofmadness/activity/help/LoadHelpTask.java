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

import java.util.List;

import org.amphiprion.mansionofmadness.activity.ILoadTask;
import org.amphiprion.mansionofmadness.activity.LoadDataListener;

import android.os.AsyncTask;

/**
 * @author amphiprion
 * 
 */
public class LoadHelpTask extends AsyncTask<Void, Integer, List<Help>> implements ILoadTask<Help> {
	private LoadDataListener<Help> caller;

	/**
	 * Default constructor.
	 */
	public LoadHelpTask(LoadDataListener<Help> caller, int pageIndex, int pageSize) {
		this.caller = caller;

	}

	@Override
	protected List<Help> doInBackground(Void... v) {
		return HelpListActivity.getHelps(caller.getContext());
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
	}

	@Override
	protected void onPostExecute(List<Help> helps) {
		caller.importEnded(!isCancelled() && helps != null, helps);
	}

}
