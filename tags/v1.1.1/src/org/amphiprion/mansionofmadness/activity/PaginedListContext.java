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

import java.util.List;

import org.amphiprion.mansionofmadness.view.MyScrollView;

/**
 * This class is the context of the game list view.
 * 
 * @author amphiprion
 * 
 */
public class PaginedListContext<T> {
	private int pageSize;
	private int loadedPage;
	private List<T> datas;
	private MyScrollView scrollView;
	private T current;
	private boolean allLoaded;
	private boolean loading;
	private ILoadTask<T> task;

	public PaginedListContext(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @return the loadedPage
	 */
	public int getLoadedPage() {
		return loadedPage;
	}

	/**
	 * @param loadedPage
	 *            the loadedPage to set
	 */
	public void setLoadedPage(int loadedPage) {
		this.loadedPage = loadedPage;
	}

	/**
	 * @return the datas
	 */
	public List<T> getDatas() {
		return datas;
	}

	/**
	 * @param datas
	 *            the datas to set
	 */
	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

	/**
	 * @return the scrollView
	 */
	public MyScrollView getScrollView() {
		return scrollView;
	}

	/**
	 * @param scrollView
	 *            the scrollView to set
	 */
	public void setScrollView(MyScrollView scrollView) {
		this.scrollView = scrollView;
	}

	/**
	 * @return the current
	 */
	public T getCurrent() {
		return current;
	}

	/**
	 * @param current
	 *            the current to set
	 */
	public void setCurrent(T current) {
		this.current = current;
	}

	/**
	 * @return the allLoaded
	 */
	public boolean isAllLoaded() {
		return allLoaded;
	}

	/**
	 * @param allLoaded
	 *            the allLoaded to set
	 */
	public void setAllLoaded(boolean allLoaded) {
		this.allLoaded = allLoaded;
	}

	/**
	 * @return the loading
	 */
	public boolean isLoading() {
		return loading;
	}

	/**
	 * @param loading
	 *            the loading to set
	 */
	public void setLoading(boolean loading) {
		this.loading = loading;
	}

	/**
	 * @return the task
	 */
	public ILoadTask<T> getTask() {
		return task;
	}

	/**
	 * @param task
	 *            the task to set
	 */
	public void setTask(ILoadTask<T> task) {
		this.task = task;
	}

	public void reset() {
		loadedPage = 0;
		datas = null;
		scrollView = null;
		current = null;
		allLoaded = false;
		loading = false;
		task = null;
	}
}
