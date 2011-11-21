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

import org.amphiprion.mansionofmadness.dto.Scenario;
import org.amphiprion.mansionofmadness.view.MyScrollView;

/**
 * This class is the context of the game list view.
 * 
 * @author amphiprion
 * 
 */
public class ScenarioListContext {
	public static final int PAGE_SIZE = 30;

	public int loadedPage;
	public List<Scenario> scenarios;
	public MyScrollView scrollView;
	public Scenario current;
	public boolean allLoaded;
	public boolean loading;
	public LoadScenariosTask task;

}
