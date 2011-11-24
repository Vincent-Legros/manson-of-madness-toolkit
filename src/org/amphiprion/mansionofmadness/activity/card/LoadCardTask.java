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
package org.amphiprion.mansionofmadness.activity.card;

import java.util.List;

import org.amphiprion.mansionofmadness.activity.ILoadTask;
import org.amphiprion.mansionofmadness.activity.LoadDataListener;
import org.amphiprion.mansionofmadness.dao.CardDao;
import org.amphiprion.mansionofmadness.dto.Card;

import android.os.AsyncTask;

/**
 * @author amphiprion
 * 
 */
public class LoadCardTask extends AsyncTask<Void, Integer, List<Card>> implements ILoadTask<Card> {
	private LoadDataListener<Card> caller;
	private int pageIndex;
	private int pageSize;

	/**
	 * Default constructor.
	 */
	public LoadCardTask(LoadDataListener<Card> caller, int pageIndex, int pageSize) {
		this.caller = caller;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;

	}

	@Override
	protected List<Card> doInBackground(Void... v) {
		try {
			List<Card> cards = CardDao.getInstance(caller.getContext()).getCards(pageIndex, pageSize, false);
			return cards;

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
	protected void onPostExecute(List<Card> cards) {
		caller.importEnded(!isCancelled() && cards != null, cards);
	}

}
