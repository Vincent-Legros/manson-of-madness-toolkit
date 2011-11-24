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
package org.amphiprion.mansionofmadness.activity;

import java.util.ArrayList;
import java.util.List;

import org.amphiprion.mansionofmadness.R;
import org.amphiprion.mansionofmadness.view.MyScrollView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

public abstract class PaginedListActivity<T> extends Activity {
	private PaginedListContext<T> listContext;
	private int viewLayoutId;
	private int scrollViewId;
	protected int listViewId;
	private int emptyTextId;

	/**
	 * 
	 */
	public PaginedListActivity(PaginedListContext<T> listContext, int viewLayoutId, int scrollViewId, int listViewId, int emptyTextId) {
		this.listContext = listContext;
		this.viewLayoutId = viewLayoutId;
		this.scrollViewId = scrollViewId;
		this.listViewId = listViewId;
		this.emptyTextId = emptyTextId;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(viewLayoutId);
		showDataList();
	}

	public void showDataList() {
		listContext.reset();

		final Rect r = new Rect();
		listContext.setScrollView((MyScrollView) findViewById(scrollViewId));
		listContext.getScrollView().setOnScrollChanged(new OnScrollChangedListener() {
			@Override
			public void onScrollChanged() {
				if (!listContext.isAllLoaded() && !listContext.isLoading()) {
					LinearLayout ln = (LinearLayout) listContext.getScrollView().getChildAt(0);
					if (ln.getChildCount() > 3) {
						boolean b = ln.getChildAt(ln.getChildCount() - 3).getLocalVisibleRect(r);
						if (b) {
							listContext.setLoading(true);
							loadGameNextPage();
						}
					}
				}
			}
		});
		initGameList();

	}

	private void initGameList() {
		listContext.setLoadedPage(0);
		if (listContext.getDatas() == null) {
			listContext.setDatas(new ArrayList<T>());
		} else {
			listContext.getDatas().clear();
		}
		loadGameNextPage();
	}

	protected abstract List<T> loadDatas(int page, int pageSize);

	private void loadGameNextPage() {
		if (listContext.getLoadedPage() == 0) {
			List<T> newDatas = loadDatas(listContext.getLoadedPage(), listContext.getPageSize());
			importDatasEnded(true, newDatas);
		} else {
			LoadDataListener<T> l = new LoadDataListener<T>() {

				@Override
				public void importEnded(boolean succeed, List<T> datas) {
					importDatasEnded(succeed, datas);
				}

				@Override
				public Context getContext() {
					return PaginedListActivity.this;
				}
			};
			listContext.setTask(getTask(l, listContext.getLoadedPage(), listContext.getPageSize()));
			listContext.getTask().execute();
		}
	}

	protected abstract ILoadTask<T> getTask(LoadDataListener<T> l, int page, int pageSize);

	public void importDatasEnded(boolean succeed, List<T> newDatas) {
		if (succeed) {
			listContext.setTask(null);
			if (newDatas != null && newDatas.size() > 0) {
				if (newDatas.size() == listContext.getPageSize() + 1) {
					newDatas.remove(listContext.getPageSize());
					listContext.setAllLoaded(false);
				} else {
					listContext.setAllLoaded(true);
				}
			} else {
				listContext.setAllLoaded(true);
			}
			if (listContext.getLoadedPage() != 0) {
				addElementToList(newDatas);
			} else {
				listContext.setDatas(newDatas);
				buildList();
			}
			listContext.setLoadedPage(listContext.getLoadedPage() + 1);
		}
		listContext.setLoading(false);

	}

	private void buildList() {

		LinearLayout ln = (LinearLayout) findViewById(listViewId);
		ln.removeAllViews();
		if (listContext.getDatas() != null && listContext.getDatas().size() > 0) {
			addElementToList(listContext.getDatas());
		} else {
			TextView tv = new TextView(this);
			tv.setText(emptyTextId);
			ln.addView(tv);
		}
	}

	private void addElementToList(List<T> newDatas) {
		LinearLayout ln = (LinearLayout) findViewById(listViewId);

		if (newDatas != listContext.getDatas()) {
			listContext.getDatas().addAll(newDatas);
			if (ln.getChildCount() > 0) {
				ln.removeViewAt(ln.getChildCount() - 1);
			}
		}
		for (final T data : newDatas) {
			View view = getDataSummaryView(data);
			ln.addView(view);
		}

		if (!listContext.isAllLoaded()) {
			ln.addView(getProgressView());
		}
	}

	protected abstract View getDataSummaryView(T data);

	private View getProgressView() {
		LinearLayout lnExpand = new LinearLayout(this);
		LayoutParams lp = new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lnExpand.setLayoutParams(lp);
		lnExpand.setBackgroundColor(getResources().getColor(R.color.grey));

		ProgressBar im = new ProgressBar(this);
		LayoutParams imglp = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		imglp.gravity = Gravity.CENTER_VERTICAL;
		imglp.rightMargin = 5;
		im.setLayoutParams(imglp);
		lnExpand.addView(im);

		TextView tv = new TextView(this);
		tv.setText(getResources().getText(R.string.loading));
		LayoutParams tlp = new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1);
		tlp.gravity = Gravity.CENTER_VERTICAL;

		tv.setLayoutParams(tlp);
		lnExpand.addView(tv);

		return lnExpand;
	}

}