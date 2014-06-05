package com.goal.mundial.video;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.os.Build;

public class SelectCategory extends Activity {

	private String[] catList;
	private String[] catListIds;
	private Context mContext;
	private Typeface tf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_category);
		try {
			mContext=this;
			tf = Typeface.createFromAsset(getAssets(), "brasilfont.otf");
			if (savedInstanceState == null) {
				catList = getIntent().getExtras().getStringArray("catList");
				catListIds = getIntent().getExtras().getStringArray(
						"catListIds");
			}
			LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayoutCheck);
			for (int i =0; i<catList.length;i++){
				CheckBox checkBox= new CheckBox(mContext);
				checkBox.setText(catList[i]);
				checkBox.setTypeface(tf);
				checkBox.setBackground(getResources().getDrawable(R.drawable.blue));
				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							
							
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

}
