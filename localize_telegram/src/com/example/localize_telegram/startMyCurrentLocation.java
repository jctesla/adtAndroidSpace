package com.example.localize_telegram;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class startMyCurrentLocation extends BroadcastReceiver {

	private final static String TAG = "startMyCurrentLocation";
	
	@Override
    public void onReceive(Context context, Intent intent) {

    	//al quitarle el comentario del if funciono enla tablet
        //if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
    		Log.i(TAG," -- BoradCast Receiver --");
            Intent i = new Intent(context, Service_GPS.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        //}
            
    }

}