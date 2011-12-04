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

import org.amphiprion.mansionofmadness.ApplicationConstants;
import org.amphiprion.mansionofmadness.R;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import org.amphiprion.mansionofmadness.dto.CombatCard;
import org.amphiprion.mansionofmadness.dao.CombatCardDao;
import java.util.LinkedHashMap;
import java.util.Iterator;

public class CombatDialog extends Dialog implements OnClickListener {
	private Context mContext;
	private RadioButton mRbBeast;
	private RadioButton mRbHumanoid;
	private RadioButton mRbEldritch;
	private Spinner mSpinAtkType;
	private Button mBtnDraw;
	private Button mBtnClose;
	private TextView mTvResult;
	private LinkedHashMap<String, String> mAtkTypeMap;
	
	static final String MONSTER = "monster";

	// private RadioGroup mRadioGroup;

	public CombatDialog(Context context, int height) {
		// super(context, R.style.CombatDlgStyle);
		super(context, android.R.style.Theme_Holo_Dialog);
		//super(context);
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

		mRbBeast = (RadioButton) findViewById(R.id.rb_beast);
		mRbHumanoid = (RadioButton) findViewById(R.id.rb_humanoid);
		mRbEldritch = (RadioButton) findViewById(R.id.rb_eldritch);
		
		mAtkTypeMap = new LinkedHashMap<String, String>();
		mAtkTypeMap.put(mContext.getString(R.string.combat_attack_type_noweapon),"combat_attack_type_noweapon");
		mAtkTypeMap.put(mContext.getString(R.string.combat_attack_type_meleeweapon),"combat_attack_type_meleeweapon");
		mAtkTypeMap.put(mContext.getString(R.string.combat_attack_type_bluntmeleeweapon),"combat_attack_type_bluntmeleeweapon");
		mAtkTypeMap.put(mContext.getString(R.string.combat_attack_type_sharpmeleeweapon),"combat_attack_type_sharpmeleeweapon");
		mAtkTypeMap.put(mContext.getString(R.string.combat_attack_type_rangedweapon),"combat_attack_type_rangedweapon");
		mAtkTypeMap.put(mContext.getString(R.string.combat_attack_type_monsterattack),"combat_attack_type_monsterattack");
		mAtkTypeMap.put(mContext.getString(R.string.combat_attack_type_monstervsbarrier),"combat_attack_type_monstervsbarrier");
		mAtkTypeMap.put(mContext.getString(R.string.combat_attack_type_monstervshiding),"combat_attack_type_monstervshiding");		
		mSpinAtkType = (Spinner) findViewById(R.id.spinAtkType);
		ArrayAdapter <String> adapter = new ArrayAdapter <String> (context, R.layout.my_spinner_textview);
		for (LinkedHashMap.Entry<String, String> entry : mAtkTypeMap.entrySet()) {
		    adapter.add(entry.getKey());
		}
		// adapter.setDropDownViewResource(R.layout.my_spinner_textview);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinAtkType.setAdapter(adapter);

		mBtnDraw = (Button) findViewById(R.id.btnDraw);
		mBtnDraw.setOnClickListener(this);

		mBtnClose = (Button) findViewById(R.id.btnClose);
		mBtnClose.setOnClickListener(this);

		mTvResult = (TextView) findViewById(R.id.txtResult);
		if (smallScreen) {
			mTvResult.setTextSize(10);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == mBtnClose) {
			dismiss();
		} else if (v == mBtnDraw) {			
			// Retrieve the selected button value
			String monsterClass = "";
			if (mRbBeast.isChecked() == true) {
				//monsterClass = mContext.getString(R.string.combat_monster_class_beast);
				monsterClass = "combat_monster_class_beast";
			} else if(mRbHumanoid.isChecked() == true) {
				//monsterClass = mContext.getString(R.string.combat_monster_class_humanoid);
				monsterClass = "combat_monster_class_humanoid";
			} else if(mRbEldritch.isChecked() == true) {
				//monsterClass = mContext.getString(R.string.combat_monster_class_eldritch);
				monsterClass = "combat_monster_class_eldritch";
			}			
			// Retrieve the spinner value
			String atkType = mSpinAtkType.getSelectedItem().toString();
			
			// Draw the right card			
			CombatCard card = CombatCardDao.getInstance(mContext).drawCombatCard(monsterClass, (String)mAtkTypeMap.get(atkType));
			String test = "";
			String success = "";
			String failure = "";
			if (card != null) { 				
				if (atkType.indexOf(MONSTER) != -1){
					test = mContext.getString(mContext.getResources().getIdentifier(card.getTestMon(), "string", ApplicationConstants.PACKAGE));
					success = mContext.getString(mContext.getResources().getIdentifier(card.getSuccessMon(), "string", ApplicationConstants.PACKAGE));
					failure = mContext.getString(mContext.getResources().getIdentifier(card.getFailureMon(), "string", ApplicationConstants.PACKAGE));
				}else{
					test = mContext.getString(mContext.getResources().getIdentifier(card.getTestInv(), "string", ApplicationConstants.PACKAGE));
					success = mContext.getString(mContext.getResources().getIdentifier(card.getSuccessInv(), "string", ApplicationConstants.PACKAGE));
					failure = mContext.getString(mContext.getResources().getIdentifier(card.getFailureInv(), "string", ApplicationConstants.PACKAGE));
				}
			}else{
				test = mContext.getString(R.string.combat_card_notfound);
			}
			displayTxt(test, success,failure);
		}
	}

	public void displayTxt(String test, String success, String failure) {
		String formattedTest = "<font color=#ffffff><b><i>" + test + "</i></b></font>";
		String formattedSuccess = "<font color=#00ff00><b>" + mContext.getString(R.string.combat_card_success) + "&nbsp;" + success + "</b></font>";
		String formattedFailure = "<font color=#ff0000><b>" + mContext.getString(R.string.combat_card_failure) + "&nbsp;" + failure + "</b></font>";
		mTvResult.setText(Html.fromHtml(formattedTest + "<br/><br/>" + formattedSuccess + "<br/><br/>" + formattedFailure));
	}

}