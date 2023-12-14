package com.example.localize_telegram;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

@SuppressWarnings("unused")
public  class PhoneBattery_RECEIVER extends BroadcastReceiver {

		private final static String TAG = "PhoneBattery()";
		public static final String  RECEIVE_CALL = "LEE SMS";

		private int nivelBatt = 0;
		String dataFrm="";
		String strNivelBatt=""; 
		//Message msg;
		//private final Handler handler;		//** 
		private PhoneState phState;
		  
		
		public PhoneBattery_RECEIVER() {
			Log.i(TAG, "...Instanciando la Class PhoneBattery_RECEIVER");
		}
		
		
		
		public void onReceive(Context context, Intent intent) {
				
				//determino que tipo de Filtro es cargando Descargando etc.
				String estado = intent.getAction();
				//msg = Message.obtain();
				//Bundle b = new Bundle();
				//CanvasSurf.setDebugSys("que paso : " + estado);						//android.intent.action.ACTION_POWER_CONNECTED   o    android.intent.action.ACTION_POWER_DISCONNECTED
				//Log.i(TAG,"Iniciando onReceive for PHONE and LEVEL BATTERY Class");

				
				// evento de deteccion de una Llamada por telef, este evento va ocurrir si:
				// es una LLMADA o es una COLGADA o es una TIMBRADA, x q no se evalua el STATE
				if ((estado.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED))) {
						Toast.makeText(context, "PhoneBatt Receiver()", Toast.LENGTH_SHORT).show();
						String strCMD = var.START + "," + "1";
						
						 // if Service Class RUNNING
						if (isServiceRunning(context, Service_GPS.class)) {
								Log.i(TAG,"onReceive() Service_GPS are Running NOW");
								Service_GPS.setMsgPopUp(TAG + "Service_GPS() are Running NOW");
						            	
						 } else {
							 	Log.i(TAG,"onReceive() Service_GPS NOT Running lauching Service_GPS()");
						       //launchMyActivity(context,RECEIVE_CALL, Service_GPS.class, strCMD);
						 }
							            
				}

				
				
				
				//este evento solo se se genera sien la clase principal GpsTlgrm_Main o cualquier otro que se declare
				//se registra "registerReceiver(power, batteryLevelFilter)" no necesita ser declarado en el Manifiest.
				if ((estado.equals(Intent.ACTION_BATTERY_CHANGED))) {
					 context.unregisterReceiver(this);											//desRegistro para que no siga pidiendo el estado continuamente.
					 int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
					 int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
					 int level = -1;
					 if (rawlevel >= 0 && scale > 0) {
					         level = (rawlevel * 100) / scale;
					         setNivelBatt(level);										//falta agregarle el % en String
					 }
					 
					 Log.i(TAG,"PhoneBattery_RECEIVER ACTION_BATTERY_CHANGED Batt=" + String.valueOf(getNivelBatt()));					 
				}
				

				// SOLO CON PROPOSITO DE PRUEBA AL COLOCAR CARGADOR.
				if ((estado.equals(Intent.ACTION_POWER_CONNECTED))) {
					
					//Multimidia.cargadorSound(context);
					//Multimidia.beepLargoSound(context);																	// beeeeep
					
					
					//La formula que determina c/cuanto se envian datos x UDP/HTTp = (trckingUdp + 1) x 2
					//Service_GPS.nivelBatt = "batt=" + String.valueOf(this.getNivelBatt());
					Log.i(TAG,"PhoneBattery_RECEIVER ACTION_POWER_CONNECTED");
					
					
					//String localstrFrm  = (char)34 + GpsTlgrm_Main.lstCfg[var.codeUser] + "|" +   nivelBatt + "|" +  GpsTlgrm_Main.strRssi + "|" +  "vel="+ GpsTlgrm_Main.kvGPS.get("vel")     + "|" + var.map + GpsTlgrm_Main.kvGPS.get("lalo") + (char)34;																					//+  kvGPS +  "|" + Funcion.getFecha()
					//GpsTlgrm_Main.upGetReg = new DwnUpDataGet(context,GpsTlgrm_Main.main_IpServHttp + localstrFrm,GpsTlgrm_Main.httpHandler,var.BUFFER_SIZE_ENVIAR, var.HdFisrttime_Http);
					//GpsTlgrm_Main.upGetReg.execute();

				}

				
				// se quda chupi
				if ((estado.equals(Intent.ACTION_POWER_DISCONNECTED))) {
					Log.i(TAG,"PhoneBattery_RECEIVER ACTION_POWER_DISCONNECTED");														
					//Multimidia.desconectadoSound(context);
					//Multimidia.beepCortoSound(context);																	// beeeeep
				
				}
	
	   }
		
		
		

		public int getNivelBatt() {
			return this.nivelBatt;
		}

		private void setNivelBatt(int nivelBatt) {
			this.nivelBatt = nivelBatt;
		}
		

		
		
		
		
		
		
		
		
		
		

		/*
		
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (state != null) {
        	TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        	phState = new PhoneState(context);
        	
        	if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_NONE);
                Log.d("PhoneState", "Idle");
            } else {
                telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
                Log.d("PhoneState", "Active");
            }
        	
            
        }
        
		*/
		
		

		// test if an Activity Class are running or not.
		private boolean isServiceRunning(Context context, Class<?> serviceClass) {
			        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			        if (activityManager != null) {
			            // Get a list of running services
			            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
			                // Check if the service class matches the provided service class
			                if (serviceClass.getName().equals(service.service.getClassName())) {
			                    return true;
			                }
			            }
			        }
			        return false;
		}
		
		
		// lamza el Service  Class la APP del Service_GPS
		 private void launchMyActivity(final Context context,final String strKey, Class<Service_GPS> destClass,String comando) {
			 	Log.i(TAG, "launchMyActivity");
			 	//Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("com.example.localize_telegram.Service_GPS");
				//if (launchIntent != null) {
					Intent serviceIntent = new Intent(context,destClass);		//com.example.localize_telegram.
					Log.i(TAG, "Launch The APP com.example.localize_telegram.Service_GPS");
					serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP  | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
					serviceIntent.putExtra(strKey,comando);
				    //context.startActivity(launchIntent);
					context.startService(serviceIntent);				// if it is running then it remains running.
				//}
		 }
		
		
		
		
		
		
		
		
		
		
		
		
}
