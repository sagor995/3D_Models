package com.appsdevsa.threedp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.appsdevsa.threedp.MyGame;

public class AndroidLauncher extends AndroidApplication {
	String value = "";
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			value = extras.getString("modelv","");
		}

		//Log.d("ModeName",value);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MyGame(value), config);

	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(getApplicationContext(),MenuActivity.class);
		startActivity(i);
		finish();
	}
}
