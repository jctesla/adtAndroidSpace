package com.example.localize_telegram;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
//import com.google.android.gms.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

//---------Clase que permite capturar los datos del modulo de GPS:
public class GPSLocListener implements LocationListener{
	
	private final static String TAG = "GPSLocListener";
	//private static final LocationListener GPSLocListener = null;
	
	// ----------- GPS ------------------
	Location locGPS=null;
	LocationManager locManager=null;
	String provider = "GPS/NET";
	private String latGPS="";
	private String lonGPS="";
	private String precisionGPS="";
	private String velocGPS="";
	private String alturaGPS="";
	private String tiempoGPS="";
	private String direccionGPS="";
	private long lngTimeGPS=0;
	static int newLocGPS=var.NOLOC;						// 0= NO Hay Locacizacion   / 1 = Hay Localizacion  / -1 = error de localizacion

	
	//------------ CLASS -------------------

	//estados del Modulo GPS/NET.
	private String state="";

	static boolean OnOffProvider=false;				//de consulta
	private String msgProvider="";
	
	
	public GPSLocListener() {
		Log.i(TAG,"...Instancia de GPSLocListener() "+ "  " + Funcion.getFecha());
		
	}
	
	
	
	public void setLocFrec(int frecLoc, Context context, boolean flgGpsNet ) {
				// TODO Auto-generated method stub
				// Constructor de l Clase
				// 	flgGpsNet = TRUE =GPS  / FALSE = NETWORK
			
			   	//----------------CLASE LOC----------------------------------
				locManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);   									// Context.getSystemService(Context.LOCATION_SERVICE).
				Log.i(TAG,"...Ref LocationManager = OK"+ "  " + Funcion.getFecha());
										
					
				
				if (flgGpsNet) {
					
							//--------------- GPS
							try {
							 			locGPS = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
								    	boolean rsltLocGPS = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER); 											//el status de disponibilidad del mod GPS.
																	
								    	// if Mod de GPS : True
								    	if (rsltLocGPS) {
								   				Log.i(TAG,"...1.- GPS Location ENABLE"+ "  " + Funcion.getFecha());
												locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, frecLoc, 0, this);									// para el GPS frecLoc=3000 milisegundos
												Log.i(TAG,"...2.- GPS Set requestLocationUpdates for PROVIDER");
												setOnProvider(true);
																			
								    	}else{
												Log.i(TAG,"...GPS Location DISABLE , CURRENT GPS IS DISABLED PLEASE ACTIVATE IT");
												setLocGPSFlg(var.NOLOC);
												setOnProvider(false);
										}
				
							}catch(Exception io) {
										 Service_GPS.setMsgPopUp(TAG + " ERR - LocationUpdate GPS:" + " " + io.toString());
										 Log.i(TAG,"ERR - LocationUpdate GPS:" + " "+ io.toString() +  "  " + Funcion.getFecha());
							}
							
							
				}else {
				
							//--------------- NETWORK
							try {	
									boolean rsltLocNET = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
				
									// if Modulo de NET : True
									if (rsltLocNET){
											Log.i(TAG,"...1.- NETOWRK Location ENABLE");
											locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);											// para el NETWORK
											Log.i(TAG,"...2.- NETWORK Set requestLocationUpdates for PROVIDER");
									}else {
										Log.i(TAG,"...NETWORK Location DISABLE , CURRENT NET IS DISABLED PLEASE ACTIVATE IT");
									}
									
							}catch(Exception io) {
										Service_GPS.setMsgPopUp(TAG + " ERR - LocationUpdate NETWORK:" + " " + io.toString());
										 Log.i(TAG,"ERR - LocationUpdate NETWORK:" + " "+ io.toString());
							}
				}
			
	}
	
	
	
	
	@Override
	public void onLocationChanged(Location loc) {
		
			try {
					provider = loc.getProvider();									// LocationManager.GPS_PROVIDER ó   LocationManager.NETWORK_PROVIDER
					
					latGPS = String.valueOf(loc.getLatitude());
					if (latGPS.length() > 10)
						latGPS = latGPS.substring(0,10);
												
					lonGPS = String.valueOf(loc.getLongitude());
					if (lonGPS.length() > 10)
						lonGPS = lonGPS.substring(0,10);
										
					precisionGPS = String.valueOf(loc.getAccuracy());
												
					velocGPS = String.valueOf((loc.getSpeed() * 3600)/1000);		//km/hr  falta cortar el string
					velocGPS = velocGPS.substring(0,velocGPS.indexOf(".") + 2);				//entrega un entero o los enteros hasta el punto mas un digito.
													
					alturaGPS = String.valueOf((loc.getAltitude())); 						 //.substring(0,6);
					alturaGPS = alturaGPS.substring(0,alturaGPS.indexOf(".") + 2);				//entrega un entero o los enteros hasta el punto mas un digito.
									
					tiempoGPS = String.valueOf(loc.getTime());
					lngTimeGPS = loc.getTime();
					direccionGPS = String.valueOf(loc.getBearing());
					newLocGPS= var.SILOC;
					
			}catch(Exception ex) {
					newLocGPS= var.ERRLOC;
					//this.setLocFlg(false);
					Service_GPS.setMsgPopUp(TAG + " ERR onLocationChanged()=" + " " + ex.toString());
					Log.i(TAG,"ERR onLocationChanged() GPS=" + " " + ex.toString());
			}
			
	} // fin onLocationChanged

	
	
	
	
	//Called when the provider status changes. This method is called when a provider is unable to fetch(no puede ir a buscar,no puede  traer) a location or if the provider has recently become available after a period of unavailability.
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		Log.i(TAG, "-------- onStatusChanged() :  " + String.valueOf(status));
		state =  String.valueOf(status);
	}

	
	//Called when the provider is enabled by the user.
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		OnOffProvider =true;
		msgProvider = "Enabled by User - Modulo de GPS ON";
		//Toast.makeText(cntx, moduloGPS,	Toast.LENGTH_SHORT).show();
		Log.i(TAG," ..GPS ACTIVADO." +  "  " + Funcion.getFecha());
	}

	
	//Called when the provider is disabled by the user. If requestLocationUpdates is called on an already disabled provider, this method is called immediately.
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		OnOffProvider=false;
		newLocGPS= var.NOLOC;
		msgProvider = "Disable by User - Modulo de GPS OFF";
		//Toast.makeText(cntx, moduloGPS,	Toast.LENGTH_SHORT).show();
		Log.i(TAG,"Err - ..GPS DESACTIVADO." +  "  " + Funcion.getFecha());
	}

	
	
	
	//------------------------------------------------------------------
	//					METHODs GPS
	//------------------------------------------------------------------
	
	String getProvider() {
		return this.provider;
	}
	
	String getLatGPS() {
		return latGPS;
	}

	String getLonGPS() {
		return lonGPS;
	}

	String getPrecisionGPS() {
		return precisionGPS;
	}

	String getVelocGPS() {
		return velocGPS;
	}

	String getAlturaGPS() {
		return alturaGPS;
	}

	String getTiempoGPS() {
		return tiempoGPS;
	}

	String getDireccionGPS() {
		return direccionGPS;
	}

	long getLngTimeGPS() {
		return lngTimeGPS;
	}


	public  int getLocGPSFlg() {
		return newLocGPS;
	}


	public void setLocGPSFlg(int rstFlg) {
		newLocGPS = rstFlg;
	}

	
	
	
	
	//--------------------------------------------------
	//           CLASS
	//--------------------------------------------------
	String getStateModule() {
		return state;
	}

	
	void setOnProvider(boolean onProvider) {
		OnOffProvider = onProvider;
	}
	
	boolean isOnOffProvider() {
		return OnOffProvider;
	}


	String getMsgProvider() {
		return msgProvider;
	}

	public void unLocListener(GPSLocListener gpsLoc) {
		locManager.removeUpdates(gpsLoc);
		
	}



	
}//end MyLocListener
