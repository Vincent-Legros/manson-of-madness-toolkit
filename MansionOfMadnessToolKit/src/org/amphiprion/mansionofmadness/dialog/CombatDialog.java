/*
 * @copyright 2011 Ridha Chelghaf
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
package org.amphiprion.mansionofmadness.dialog;

import org.amphiprion.mansionofmadness.R;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class CombatDialog extends Dialog implements OnClickListener {
	private Context mContext;
	private Spinner mAtkType;
	private Button mDraw;
	private Button mClose;
	private TextView mResult;

	// private RadioGroup mRadioGroup;

	public CombatDialog(Context context, int height) {
		// super(context, R.style.CombatDlgStyle);
		super(context);
		mContext = context;
		boolean smallScreen = height < 600;

		/** It will hide the title */
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (smallScreen) {
			setContentView(R.layout.combat_resolve_phone);
		} else {
			setContentView(R.layout.combat_resolve);
		}
		setTitle(context.getString(R.string.combat_title));

		mAtkType = (Spinner) findViewById(R.id.spinAtkType);
		ArrayAdapter <CharSequence> adapter = new ArrayAdapter <CharSequence> (context, R.layout.my_spinner_textview);
		adapter.add(mContext.getString(R.string.combat_attack_type_noweapon));
		adapter.add(mContext.getString(R.string.combat_attack_type_meleeweapon));
		adapter.add(mContext.getString(R.string.combat_attack_type_bluntmeleeweapon));
		adapter.add(mContext.getString(R.string.combat_attack_type_sharpmeleeweapon));
		adapter.add(mContext.getString(R.string.combat_attack_type_rangedweapon));
		adapter.add(mContext.getString(R.string.combat_attack_type_monsterattack));
		adapter.add(mContext.getString(R.string.combat_attack_type_monstervsbarrier));
		adapter.add(mContext.getString(R.string.combat_attack_type_monstervshiding));				 		
		// adapter.setDropDownViewResource(R.layout.my_spinner_textview);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mAtkType.setAdapter(adapter);

		mDraw = (Button) findViewById(R.id.btnDraw);
		mDraw.setOnClickListener(this);

		mClose = (Button) findViewById(R.id.btnClose);
		mClose.setOnClickListener(this);

		mResult = (TextView) findViewById(R.id.txtResult);
		if (smallScreen) {
			mResult.setTextSize(10);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == mClose) {
			dismiss();
		} else if (v == mDraw) {
			// TODO retrieve card from db
			displayTxt(mContext.getString(R.string.combat_card_01_inv), mContext.getString(R.string.combat_card_01_inv_success),
					mContext.getString(R.string.combat_card_01_inv_failure));
		}
	}

	public void displayTxt(String test, String success, String failure) {
		String formattedTest = "<font color=#ffffff><b><i>" + test + "</i></b></font>";
		String formattedSuccess = "<font color=#00ff00><b>" + mContext.getString(R.string.combat_card_success) + "&nbsp;" + success + "</b></font>";
		String formattedFailure = "<font color=#ff0000><b>" + mContext.getString(R.string.combat_card_failure) + "&nbsp;" + failure + "</b></font>";
		mResult.setText(Html.fromHtml(formattedTest + "<br/><br/>" + formattedSuccess + "<br/><br/>" + formattedFailure));
	}

}