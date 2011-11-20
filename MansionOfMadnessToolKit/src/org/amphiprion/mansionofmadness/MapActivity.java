package org.amphiprion.mansionofmadness;

import org.amphiprion.gameengine3d.GameActivity;
import org.amphiprion.mansionofmadness.screen.map.MapScreen;

import android.os.Bundle;

public class MapActivity extends GameActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		view.addScreen(new MapScreen(this));
	}

}