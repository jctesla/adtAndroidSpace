package com.example.localize_telegram;

import android.app.Activity;
import android.os.Bundle;

public class PopUp_Notification extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup_notification);						// es un XML tipo Activity q contiene el mensage en un textView
	}
}
