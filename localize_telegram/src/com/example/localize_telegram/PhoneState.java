package com.example.localize_telegram;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class PhoneState extends  PhoneStateListener{

	private String potencia="";
	private int state=-1;
	private String nameRed = "";
	private final static String TAG = "PHONSTATE";
	public static final String  RECEIVE_CALL = "LEE SMS";

	Context cntx;
	static TelephonyManager tm=null;
	int intTipoRed=-1;
	int [] promSignal= {-1,-1};
	PhoneState rdPhst=null;
	
	//private final Handler handler;
	//String rsltFrom;
	Message msg;
	Bundle b; 
	
	
	
	public PhoneState(Context cntx){
		this.cntx = cntx;
		tm=(TelephonyManager)this.cntx.getSystemService(Context.TELEPHONY_SERVICE);
		Log.i(TAG,"PhoneState Instanciado()");
	}
	
	
	
	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
				super.onCallStateChanged(state, incomingNumber);
				//Toast.makeText(cntx, TAG + " -Calling()-", Toast.LENGTH_SHORT).show();
				Log.i(TAG,"onCallStateChanged incomingNumber = " + incomingNumber + "   state=" +  String.valueOf( state));
				
				// onCallStateChanged incomingNumber = 6505551212   state=1					Hay Una LLamada.
				// onCallStateChanged incomingNumber = 6505551212   state=0					Colgado del OtroLado o Del Mismo.

				
				switch(state) {
				
				case TelephonyManager.CALL_STATE_IDLE:
																						setTlfState(TelephonyManager.CALL_STATE_IDLE);
																						Log.i(TAG,"onCallStateChanged() CALL_STATE_IDLE -COLGADO- Nº:" + String.valueOf( state));  // 0
																						//Toast.makeText(cntx, TAG + "-COLGADO()-", Toast.LENGTH_SHORT).show();
																						break;				
					
				case TelephonyManager.CALL_STATE_OFFHOOK:
																						setTlfState(TelephonyManager.CALL_STATE_OFFHOOK);
																						Log.i(TAG,"onCallStateChanged() CALL_STATE_OFFHOOK -TELF LEVANTADO- Nº:" + String.valueOf( state) );  // 2
																						//Toast.makeText(cntx, TAG + "-CONTESTADO()-", Toast.LENGTH_SHORT).show();
																						break;				
					
				case TelephonyManager.CALL_STATE_RINGING:
																						setTlfState(TelephonyManager.CALL_STATE_RINGING);					
																						Log.i(TAG,"onCallStateChanged() CALL_STATE_-RINGING- Nº" + String.valueOf( state));	//1
																						//Toast.makeText(cntx, TAG + "-RINGING()-", Toast.LENGTH_SHORT).show();
																						break;
				
				}
				
		}

	
	
	@Override
	public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			// TODO Auto-generated method stub
			super.onSignalStrengthsChanged(signalStrength);
			int ss= signalStrength.getGsmSignalStrength();

	        //this.signalRed = -1*(113-(2*ss)) + "dBm";
			setRssi(String.valueOf(-1*(113-(2*ss))) + "dBm");
	        
	        //Log.i(TAG,"onSignalStrengthsChanged = " + potencia);
	        //this.nameRed = "Red TLF";

	}//fin de onsignalChanged
	
	
	
	
	@Override
	public void onServiceStateChanged(ServiceState serviceState) {
		// TODO Auto-generated method stub
		super.onServiceStateChanged(serviceState);
		Log.i(TAG,"onServiceStateChanged OK");
	}
	
	

	
	//Lleno los datos de Signal Strenght tELEFONO
	public void StartSignalTelephone(PhoneState rdPhst) {
		
			//this.tm=(TelephonyManager)this.cntx.getSystemService(Context.TELEPHONY_SERVICE);
			tm.listen(rdPhst, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);											//| PhoneStateListener.LISTEN_CELL_LOCATION
			this.intTipoRed = tm.getNetworkType();
			
			 switch (intTipoRed){
					   case 1: this.nameRed = "GPRS";break;
					   case 3: this.nameRed = ("UMTS");break;
					   case 4: this.nameRed = ("CDMA");break;
					   case 8: this.nameRed = ("HSDPA");break;
					   case 9: this.nameRed = ("HSUPA");break;
					   case 10: this.nameRed = ("HSPA"); break;
					   case 13: this.nameRed = ("LTE");break;
					   case 15: this.nameRed = ("ENTEL");break;
					   default: this.nameRed = String.valueOf(intTipoRed);break; 
			 }
			 Log.i(TAG,"StartSignalTelephone OK = " + nameRed);
	}

	

	public int getTlfState() {
		return state;					//agregarle el simbolo de + " %";
	}

	
	private void setTlfState(int state) {
		this.state = state;
	}

	
	
	
	
	
	public String getRssi() {
		return potencia;					//agregarle el simbolo de + " %";
	}

	private void setRssi(String potencia) {
		this.potencia = potencia;
	}

	
	public String getNameRed() {
		return nameRed;
	}
	

	public void OnStop() {
		tm.listen(rdPhst,PhoneStateListener.LISTEN_NONE);
	}
	
	
	
	// https://www.tabnine.com/code/java/methods/com.android.internal.telephony.ITelephony/endCall
		
	public void colgarTelf() {
		
			 try {
				  	Log.i(TAG, "tm.getCallState() = " + String.valueOf(tm.getCallState()));
				  	//  Activity SDK Version: 22					Activity SDK Version: 29
				  	//  Activity Android Version: 5.1.1		Activity Android Version: 10	
				  	
				  	int sdkVersion = Build.VERSION.SDK_INT;
					String androidVersion = Build.VERSION.RELEASE;
	
					// verify is Tefl Levantado = OFFHOOK
					if (tm.getCallState() == TelephonyManager.CALL_STATE_OFFHOOK) {
							if (sdkVersion < 28) {
							        Class<?> telephonyClass = Class.forName(tm.getClass().getName());
							        Method endCallMethod = telephonyClass.getDeclaredMethod("endCall");
							        endCallMethod.setAccessible(true);
							        endCallMethod.invoke(tm);
							        Log.i(TAG,"CallStatus" +  "Call ended successfully");
							}else {
									TelecomManager tm = (TelecomManager) this.cntx.getSystemService(Context.TELECOM_SERVICE);
				
								  	if (tm != null) {
								  	    boolean success = tm.endCall();
								  	    // success == true if call was terminated.
								  	}
							}
			       	}
			       	
				  	
			 } catch (Exception e) {
			        	Log.i(TAG, "CallStatus" + "Failed to end call: " + e.getMessage());
			        	Service_GPS.setMsgPopUp(TAG + "ERR  colgarTelf() = " +  e.toString());
			}
	}
	
	
	
} //find e CLASS
