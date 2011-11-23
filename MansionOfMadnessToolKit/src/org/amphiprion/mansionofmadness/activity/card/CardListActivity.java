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

import org.amphiprion.mansionofmadness.ApplicationConstants;
import org.amphiprion.mansionofmadness.R;
import org.amphiprion.mansionofmadness.activity.ILoadTask;
import org.amphiprion.mansionofmadness.activity.IMenuProvider;
import org.amphiprion.mansionofmadness.activity.LoadDataListener;
import org.amphiprion.mansionofmadness.activity.PaginedListActivity;
import org.amphiprion.mansionofmadness.activity.PaginedListContext;
import org.amphiprion.mansionofmadness.dao.CardDao;
import org.amphiprion.mansionofmadness.dto.Card;
import org.amphiprion.mansionofmadness.dto.Entity.DbState;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class CardListActivity extends PaginedListActivity<Card> implements IMenuProvider {
	private Card currentCard;

	/**
	 * 
	 */
	public CardListActivity() {
		super(new PaginedListContext<Card>(20), R.layout.scenario_list, R.id.scroll_view, R.id.scenario_list, R.string.empty_card_list);
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
	protected List<Card> loadDatas(int page, int pageSize) {
		return CardDao.getInstance(this).getCards(page, pageSize, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.amphiprion.mansionofmadness.activity.PaginedListActivity#getTask(
	 * org.amphiprion.mansionofmadness.activity.LoadDataListener, int, int)
	 */
	@Override
	protected ILoadTask<Card> getTask(LoadDataListener<Card> l, int page, int pageSize) {
		return new LoadCardTask(l, page, pageSize);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.amphiprion.mansionofmadness.activity.PaginedListActivity#
	 * getDataSummaryView(java.lang.Object)
	 */
	@Override
	protected View getDataSummaryView(Card data) {
		CardSummaryView view = new CardSummaryView(this, data);
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

		if (v instanceof CardSummaryView) {
			currentCard = ((CardSummaryView) v).getCard();
			if (!currentCard.isEmbedded()) {
				menu.add(0, ApplicationConstants.MENU_ID_EDIT_CARD, 0, R.string.edit_card);
				menu.add(0, ApplicationConstants.MENU_ID_DELETE_CARD, 1, R.string.delete_card);
			}
		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == ApplicationConstants.MENU_ID_EDIT_CARD) {
			updateCard(currentCard);
		} else if (item.getItemId() == ApplicationConstants.MENU_ID_DELETE_CARD) {
			checkDeleteCard(currentCard);
		}
		return true;
	}

	private void checkDeleteCard(final Card card) {
		if (CardDao.getInstance(this).isUsed(card)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(this.getString(R.string.used_item_title));
			builder.setMessage(this.getString(R.string.used_item_message));
			builder.setCancelable(true).setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					deleteCard(card);
				}
			});
			builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {

				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		} else {
			deleteCard(card);
		}
	}

	private void deleteCard(Card card) {
		card.setState(DbState.DELETE);
		CardDao.getInstance(this).persist(card);
		showDataList();
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
		MenuItem mi = menu.add(0, ApplicationConstants.MENU_ID_CREATE_CARD, 0, R.string.create_card);
		mi.setIcon(android.R.drawable.ic_menu_add);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == ApplicationConstants.MENU_ID_CREATE_CARD) {
			updateCard(new Card());
		}
		return true;
	}

	private void updateCard(final Card card) {
		final Dialog dlg = new Dialog(CardListActivity.this);
		dlg.setTitle(R.string.edit_card_title);
		dlg.setContentView(R.layout.card_edit);
		final EditText txtName = (EditText) dlg.findViewById(R.id.txtEdit);
		if (card.getName() != null) {
			txtName.setText(card.getName());
		}
		final Spinner cbType = (Spinner) dlg.findViewById(R.id.cbType);
		ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.card_types, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cbType.setAdapter(adapter);
		if ("exploration".equals(card.getType())) {
			cbType.setSelection(0);
		} else if ("lock".equals(card.getType())) {
			cbType.setSelection(1);
		} else {
			cbType.setSelection(2);
		}
		Button btOk = (Button) dlg.findViewById(R.id.btOk);
		btOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int i = cbType.getSelectedItemPosition();
				if (i < 0) {
					return;
				}
				String txt = txtName.getText().toString();
				if (txt == null || "".equals(txt)) {
					return;
				}
				card.setName(txt);
				card.setEmbedded(false);
				if (i == 0) {
					card.setType("exploration");
				} else if (i == 1) {
					card.setType("lock");
				} else {
					card.setType("obstacle");
				}
				CardDao.getInstance(CardListActivity.this).persist(card);
				showDataList();

				dlg.dismiss();
			}
		});

		Button btCancel = (Button) dlg.findViewById(R.id.btCancel);
		btCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dlg.dismiss();
			}
		});
		dlg.show();
	}

}