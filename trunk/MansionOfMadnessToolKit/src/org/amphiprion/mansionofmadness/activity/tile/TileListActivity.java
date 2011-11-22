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
package org.amphiprion.mansionofmadness.activity.tile;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.amphiprion.mansionofmadness.ApplicationConstants;
import org.amphiprion.mansionofmadness.R;
import org.amphiprion.mansionofmadness.activity.ILoadTask;
import org.amphiprion.mansionofmadness.activity.IMenuProvider;
import org.amphiprion.mansionofmadness.activity.LoadDataListener;
import org.amphiprion.mansionofmadness.activity.PaginedListActivity;
import org.amphiprion.mansionofmadness.activity.PaginedListContext;
import org.amphiprion.mansionofmadness.dao.TileDao;
import org.amphiprion.mansionofmadness.dto.Tile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TileListActivity extends PaginedListActivity<Tile> implements IMenuProvider {
	private Tile currentTile;
	private Button btPath;

	/**
	 * 
	 */
	public TileListActivity() {
		super(new PaginedListContext<Tile>(20), R.layout.scenario_list, R.id.scroll_view, R.id.scenario_list, R.string.empty_tile_list);
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
	protected List<Tile> loadDatas(int page, int pageSize) {
		return TileDao.getInstance(this).getTiles(page, pageSize, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.amphiprion.mansionofmadness.activity.PaginedListActivity#getTask(
	 * org.amphiprion.mansionofmadness.activity.LoadDataListener, int, int)
	 */
	@Override
	protected ILoadTask<Tile> getTask(LoadDataListener<Tile> l, int page, int pageSize) {
		return new LoadTilesTask(l, page, pageSize);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.amphiprion.mansionofmadness.activity.PaginedListActivity#
	 * getDataSummaryView(java.lang.Object)
	 */
	@Override
	protected View getDataSummaryView(Tile data) {
		TileSummaryView view = new TileSummaryView(this, data);
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

		if (v instanceof TileSummaryView) {
			currentTile = ((TileSummaryView) v).getTile();
			if (!currentTile.isEmbedded()) {
				menu.add(0, ApplicationConstants.MENU_ID_EDIT_TILE, 0, R.string.edit_tile);
			}
		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == ApplicationConstants.MENU_ID_EDIT_TILE) {
			updateTile(currentTile);
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
		MenuItem mi = menu.add(0, ApplicationConstants.MENU_ID_CREATE_TILE, 0, R.string.create_tile);
		mi.setIcon(android.R.drawable.ic_menu_add);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == ApplicationConstants.MENU_ID_CREATE_TILE) {
			updateTile(new Tile());
		}
		return true;
	}

	private void updateTile(final Tile tile) {
		final Dialog dlg = new Dialog(TileListActivity.this);
		dlg.setTitle(R.string.edit_tile_title);
		dlg.setContentView(R.layout.tile_edit);
		final EditText txtName = (EditText) dlg.findViewById(R.id.txtEdit);
		if (tile.getName() != null) {
			txtName.setText(tile.getName());
		}
		btPath = (Button) dlg.findViewById(R.id.btPath);
		btPath.setText(tile.getImageName());
		if (tile.getImageName() != null) {
			btPath.setText(tile.getImageName());
		}

		btPath.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				File file = new File("" + Environment.getExternalStorageDirectory());
				intent.setDataAndType(Uri.parse(file.toString()), "text/*");
				try {
					startActivityForResult(intent, ApplicationConstants.ACTIVITY_RETURN_CHOOSE_TILE_FILE);
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
				String imageName = btPath.getText().toString();
				if (imageName == null || "".equals(imageName)) {
					return;
				}

				Bitmap b = null;
				try {
					if (imageName.startsWith("file://")) {
						b = BitmapFactory.decodeStream(new FileInputStream(imageName.substring(7)));
					} else {
						b = BitmapFactory.decodeStream(new FileInputStream(imageName));
					}
					if (b.getWidth() == 0 || b.getWidth() / 150 * 150 != b.getWidth()) {
						Toast.makeText(TileListActivity.this, R.string.wrong_tile_width, 1000).show();
						return;
					}
					if (b.getHeight() == 0 || b.getHeight() / 150 * 150 != b.getHeight()) {
						Toast.makeText(TileListActivity.this, R.string.wrong_tile_height, 1000).show();
						return;
					}
				} catch (Exception e) {
					Log.e(ApplicationConstants.PACKAGE, "", e);
					btPath.setText("");
					Toast.makeText(TileListActivity.this, R.string.wrong_tile_file, 1000).show();
					return;
				}
				String txt = txtName.getText().toString();
				if (txt == null || "".equals(txt)) {
					return;
				}
				tile.setName(txt);
				tile.setEmbedded(false);
				tile.setImageName(btPath.getText().toString());
				tile.setWidth(b.getWidth() / 150);
				tile.setHeight(b.getHeight() / 150);

				TileDao.getInstance(TileListActivity.this).persist(tile);
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
			if (requestCode == ApplicationConstants.ACTIVITY_RETURN_CHOOSE_TILE_FILE) {
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