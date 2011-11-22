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
package org.amphiprion.mansionofmadness.activity.sound;

import java.io.File;
import java.util.List;

import org.amphiprion.mansionofmadness.ApplicationConstants;
import org.amphiprion.mansionofmadness.R;
import org.amphiprion.mansionofmadness.activity.ILoadTask;
import org.amphiprion.mansionofmadness.activity.IMenuProvider;
import org.amphiprion.mansionofmadness.activity.LoadDataListener;
import org.amphiprion.mansionofmadness.activity.PaginedListActivity;
import org.amphiprion.mansionofmadness.activity.PaginedListContext;
import org.amphiprion.mansionofmadness.dao.SoundDao;
import org.amphiprion.mansionofmadness.dto.Sound;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SoundListActivity extends PaginedListActivity<Sound> implements IMenuProvider {
	private Sound currentSound;
	private Button btPath;

	/**
	 * 
	 */
	public SoundListActivity() {
		super(new PaginedListContext<Sound>(20), R.layout.scenario_list, R.id.scroll_view, R.id.scenario_list, R.string.empty_sound_list);
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
	protected List<Sound> loadDatas(int page, int pageSize) {
		return SoundDao.getInstance(this).getSounds(page, pageSize, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.amphiprion.mansionofmadness.activity.PaginedListActivity#getTask(
	 * org.amphiprion.mansionofmadness.activity.LoadDataListener, int, int)
	 */
	@Override
	protected ILoadTask<Sound> getTask(LoadDataListener<Sound> l, int page, int pageSize) {
		return new LoadSoundsTask(l, page, pageSize);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.amphiprion.mansionofmadness.activity.PaginedListActivity#
	 * getDataSummaryView(java.lang.Object)
	 */
	@Override
	protected View getDataSummaryView(Sound data) {
		SoundSummaryView view = new SoundSummaryView(this, data);
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

		if (v instanceof SoundSummaryView) {
			currentSound = ((SoundSummaryView) v).getSound();
			if (!currentSound.isEmbedded()) {
				menu.add(0, ApplicationConstants.MENU_ID_EDIT_SOUND, 0, R.string.edit_sound);
			}
		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == ApplicationConstants.MENU_ID_EDIT_SOUND) {
			updateSound(currentSound);
		}
		return true;
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
		MenuItem mi = menu.add(0, ApplicationConstants.MENU_ID_CREATE_SOUND, 0, R.string.create_sound);
		mi.setIcon(android.R.drawable.ic_menu_add);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == ApplicationConstants.MENU_ID_CREATE_SOUND) {
			updateSound(new Sound());
		}
		return true;
	}

	private void updateSound(final Sound sound) {
		final Dialog dlg = new Dialog(SoundListActivity.this);
		dlg.setTitle(R.string.edit_sound_title);
		dlg.setContentView(R.layout.sound_edit);
		final EditText txtName = (EditText) dlg.findViewById(R.id.txtEdit);
		if (sound.getName() != null) {
			txtName.setText(sound.getName());
		}
		btPath = (Button) dlg.findViewById(R.id.btPath);
		btPath.setText(sound.getSoundName());
		if (sound.getSoundName() != null) {
			btPath.setText(sound.getSoundName());
		}

		btPath.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				File file = new File("" + Environment.getExternalStorageDirectory());
				intent.setDataAndType(Uri.parse(file.toString()), "text/*");
				try {
					startActivityForResult(intent, ApplicationConstants.ACTIVITY_RETURN_CHOOSE_SOUND_FILE);
				} catch (ActivityNotFoundException e) {
					// No activity to handle this mime type.
					// Download for exemple ES File Manager
					askToDownloadFileManager();
				}
			}
		});

		Button btOk = (Button) dlg.findViewById(R.id.btOk);
		btOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String soundName = btPath.getText().toString();
				if (soundName == null || "".equals(soundName)) {
					return;
				}
				String txt = txtName.getText().toString();
				if (txt == null || "".equals(txt)) {
					return;
				}
				sound.setName(txt);
				sound.setEmbedded(false);
				sound.setSoundName(btPath.getText().toString());
				SoundDao.getInstance(SoundListActivity.this).persist(sound);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == ApplicationConstants.ACTIVITY_RETURN_CHOOSE_SOUND_FILE) {
				btPath.setText(data.getDataString());
			}
		}
	}

	private void askToDownloadFileManager() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).setMessage(R.string.install_file_chooser_message).create();
		alertDialog.setTitle(R.string.install_file_chooser_title);
		alertDialog.setIcon(android.R.drawable.ic_menu_info_details);
		alertDialog.setButton(getResources().getText(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=lysesoft.andexplorer"));
				startActivity(marketIntent);
				return;
			}
		});
		alertDialog.setButton2(getResources().getText(R.string.cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// do nothing
			}
		});
		alertDialog.show();
		return;
	}
}