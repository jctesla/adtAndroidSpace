package com.example.localize_telegram;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class callTlf_Activity extends Activity implements Runnable{
		private final static String TAG = "Call TELF ";
		
		
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}
		

		@Override
		protected void onStart() {
			super.onStart();
			Log.i(TAG,"Activity onSTART()" + " " + Funcion.getFecha());
	
		}
	
	
		@Override
		protected void onResume() {
				super.onResume();
				Log.i(TAG,"onResume()" + " " + Funcion.getFecha());
				Service_GPS.setMsgPopUp(TAG +  "onResume()"+  " " + Funcion.getFecha());
				
				try {

					// this Launcher make a CALL TELF. FUNCIONA
					//PEro solo si el Activity esta Presente si sólo esta el ServiceClass NO LLAMA, NO SALE ERROR,
					//simplemente no llama
					Service_GPS.setMsgPopUp(TAG +  "CALLING A NUMBER= " +  "993148609");
					
					/*
    				Uri phoneUri = Uri.parse("tel:" + "993148609");
				    Intent callIntent = new Intent(Intent.ACTION_CALL, phoneUri);
				    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TOP  | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
				    startActivity(callIntent);
				    */

				}catch(Exception ee) {
					Log.i(TAG,"ERR onResume() " + ee.toString() );
					Service_GPS.setMsgPopUp(TAG + "ERR Start Calling = " + ee.toString());
				}
				
		}
		
		
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
		
			
}
