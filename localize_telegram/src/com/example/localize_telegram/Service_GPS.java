/*
 * Este Projecto es una App de Android que envia msgs y su Localizacion a un grupo de chat en Telegram (lo como Servidor HTTP):
 * Para poder entender bien como se desarrolla todo el Prj, debemos crear la consultas APIs al telegram desde python y una vez funcionando,
 * lo llevamos al entorno de Android. (1º D:\phytonSpace\miBasic\A_pyVSCode\sms_msg_chat)
 * 
 * Se sugiere tener conceptos bien claros de :
 *  I PARTE:
 * - 1º como crear un bot en Telegram : D:\phytonSpace\miBasic\A_pyVSCode\sms_msg_chat\img\
 * - 2º probar antes de desarrollar la APP en Android (q seria el bot) las app en python : D:\phytonSpace\miBasic\A_pyVSCode\sms_msg_chat
 * 
 * II PARTE:
 * - 1º para saber como opera el App 'localize_Telegram', debes revisar toda la doc e imagenes en 'D:\adtEclipseSpace\adtAndroidSpace\localize_Telegram\img' 
 * - 2º Binders (D:\adtEclipseSpace\Docs\rdMe\Android_Tips.pdf  nuscar 'Descripcion de Bound Services'. ciclo de vida.
 * - 3º Leer: D:\adtEclipseSpace\Docs\rdMe\Cómo utilizar Data Binding en Android_2016.pdf
 * - 4º Consultar en este Chat: D:\adtEclipseSpace\Docs\rdMe\TipsTricks FAQ Android_chatGPT\TipsTricks_Android_chatGPT.mhtml  (buscar apartir de : "Can I implement in BroadcastReceiver all service I want?")
 * - 5º revisar proj:
 *		. D:\adtEclipseSpace\adtAndroidSpace\Service_BackGround_00 
 * 	. D:\adtEclipseSpace\adtAndroidSpace\Service_BackGround_01
 * 	. D:\adtEclipseSpace\adtAndroidSpace\Service_BackGround_02
 * 	. D:\adtEclipseSpace\adtAndroidSpace\Service_BackGround_03
 *     . D:\adtEclipseSpace\adtAndroidSpace\json_test_01 y json_test_02
 *     . D:\adtEclipseSpace\adtAndroidSpace\GpsTelegram
 *     
 * 
 * Este Projecto utiliza:
 * - JSON (en Android/Java)
 * - GPS
 * - SMS (start Activity form SMS)
 * - Batt y Signal de Phone
 * - Active from WakeUp Power ON Phone
 * - Probado en Android 5.1 6.0 y 10 con API de pkg 5.1 
 * - Telegram como Recepetor de los envios HTTP de Android.
 * - Service Class as BackGrounds, means : binds, Activity
 *  LAS APIs que se usande Telgrasm: https://core.telegram.org/bots/api#making-requests
 *  Doc de JSON para Android : https://abhiandroid.com/programming/json
 */

// No usamos la funcion Toast() x q el Service class es un clase sin UI, el Service no tiene como handlear el mensage del Toast .
//  En su lugar creamos una funcion  "setMsgPopUp" como un debugger para almacenar  todo los eventos Logs/Toast aqi y cuando el Activity q lea esta funcion los pueda mostrar en su UI 


package com.example.localize_telegram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

import java.net.URL;
import java.nio.charset.StandardCharsets;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


//import com.example.gpstelegram.OvrTime;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.app.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.net.Uri;

import android.os.Binder;

import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
//import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import android.os.CountDownTimer;

public class Service_GPS extends Service   {

	
	public miBinder localBinder = new miBinder();									// prototipo de subClase
	static int contador = 0;
	//private static boolean flgRunning = true;
	String [] lstCfg;
	static boolean rsltConfig=false;
	
	static SetThrdGPS setThrdGPS=null;
	
	
	GPSLocListener gpsLoc=null;

	public static final int  modGpsOff =3;
		
	PhoneBattery_RECEIVER levelBatt;
	
	TelephonyManager tm;
	PhoneState phState; 
	String strLevelBatt = "batt=-1";
	String strRssi = "signal=-1";
	String statePhone="Tefl=colagdo";
	String nroCall ="123456789";
	
	
	private final static String TAG = "Service ";

	
	
	static Handler httpHandler;
	static Handler localEvents;																		//para evento de la misma clase
	int errCnxHTTP_Break = -1, errCnxHTTP_Srvr=-1;

	
	//------------- AL NOTIFICATION ----------------------------------
	// Notification Text Elements
	private final CharSequence tickerText = "This is a Really, Really, Super Long Notification Message!";
	private final CharSequence contentTitle = "Notification";
	private final CharSequence contentText = "You've Been Notified!";
	
	// Notification ID to allow for future updates
	private static final int MY_NOTIFICATION_ID = 1;
		
	// Notification Sound and Vibration on Arrival  
	private Uri soundURI = Uri.parse("android.resource://com.example.service_background/" + R.raw.notificacion_sms);
	private long[] mVibratePattern = { 0, 200, 200, 300 };
	Notification.Builder miNotify=null;
	NotificationManager notiManager = null;
	
	// Notification Action Elements
	private Intent popUpNotiIntent;
	private PendingIntent pendingNotiIntent;
	static String msgPopUp="";
	static String logLatLon="";
		
	
	sendHTTPGet mySend=null;
	static boolean flgFirstTime=false;
	static boolean flgThrdHTTP=false;																									//  True = para indicar q se ha enviado un dato al sendHTTPGet y debe esperarse a recibir una res del Servidor para estar dispo al sgnt envio.
	static boolean flgSendBeforeSTOPme=false;	
	static boolean flgRqstConfig=false;
	static boolean flgRqstLogALL=false;
	static boolean flgRqstLogGPS=false;
	static boolean flgColgadoTelf=false;
	static boolean flgCallTelfNro=false;

	public static final String  KEY_SMS_CMD = "COMANDO SMS";
	public static final String  KEY_SMS_FROM = "SMS DE QUIEN";

	String valSMSCmd = "";
	String valSMSFrom = "";
	

	
	//-------------------------------------------------------------------
	//						MAIN()
	// -------------------------------------------------------------------
	public Service_GPS() {
		Log.i(TAG, "...Instancia de Service_GPS Class ACTIVADO");
	}

	
	@Override
	public void onCreate() {
		super.onCreate();

		// This method is called when the service is first created.
		Log.i(TAG, "--------------- onCreate() INICIADO-----------------------");
		//Toast.makeText(getBaseContext(), TAG +"onCreate() INICIADO", Toast.LENGTH_SHORT).show();
	}
	
	
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		
		Log.i(TAG, "--------------- onStartCommand()-----------------------");
		Toast.makeText(getBaseContext(), "onStartCommand()", Toast.LENGTH_SHORT).show();
		
		//-------------- Lectura del SMS---------------
		Bundle recevSMS = null;
		try {
				recevSMS = intent.getExtras();
		} catch (Exception e) {
			Log.i(TAG,"intent.getExtras()= " + e.getMessage());
		}
		
		
		//-------------- evaluamos contenido del SMS---------------
		if (recevSMS != null) {
			    valSMSCmd = recevSMS.getString(KEY_SMS_CMD);
			    valSMSFrom= recevSMS.getString(KEY_SMS_FROM);
			    
				Log.i(TAG,"onStartCommand INICIANDO SMS= " + valSMSCmd + "   From Number=" + valSMSFrom);
			    
			    // COMANDO = START,tiempo
				if (valSMSCmd.indexOf(var.START) ==0) {
						int idx = valSMSCmd.indexOf(",");
						int tiempoTrck = Funcion.StrToInt(valSMSCmd.substring(idx+1))*60;
						Log.i(TAG,"COMANDO START Thread trck = "+ String.valueOf( tiempoTrck) + " seg");
						//Toast.makeText(getBaseContext(), "START COMMAND" + String.valueOf( tiempoTrck) + " seg", Toast.LENGTH_SHORT).show();
						setMsgPopUp(TAG +  "comando START Thread trck = "+ String.valueOf( tiempoTrck) + " seg"  + " From:" + valSMSFrom);
						configLaunch(tiempoTrck);
				}
				
				
			    // COMANDO = STOP
				if (valSMSCmd.indexOf(var.STOP) ==0) {
						Log.i(TAG,"COMANDO STOP  Thread.");
						//Toast.makeText(getBaseContext(), "STOP COMMAND" , Toast.LENGTH_SHORT).show();
						setMsgPopUp(TAG +  "comando STOP Thread."  + " "   + " From:" + valSMSFrom);
						flgSendBeforeSTOPme = true;
				}
				
				
			    // COMANDO = RQCONFIG
				if (valSMSCmd.indexOf(var.RQST_CFG) ==0) {
						Log.i(TAG,"COMANDO RQST CONFIGURATION -- config.cfg --");
						setMsgPopUp(TAG +  "comando RQST CONFIG --config.cfg--"  + " "   + " From:" + valSMSFrom);
						//Toast.makeText(getBaseContext(), "RQST CONFIG COMMAND" , Toast.LENGTH_SHORT).show();
						flgRqstConfig= true;
						configLaunch(0);
				}

				
			    // COMANDO = RQLOG
				if (valSMSCmd.indexOf(var.RQST_LOGALL) ==0) {
						Log.i(TAG,"COMANDO RQST ALL LOG SCREEN");
						setMsgPopUp(TAG +  "comando RQST ALL LOG SCREEN"  + " "  + " From:" + valSMSFrom);
						//Toast.makeText(getBaseContext(), "RQST ALL LOG of SCREEN" , Toast.LENGTH_SHORT).show();
						flgRqstLogALL= true;
						configLaunch(0);
				}


			    // COMANDO = RQLOGGPS
				if (valSMSCmd.indexOf(var.RQST_LOGGPS) ==0) {
						Log.i(TAG,"COMANDO RQST LOG LOCATION of GPS");
						setMsgPopUp(TAG +  "comando RQST LOG LOCATION of GPS"  + " "  + " From:" + valSMSFrom);
						//Toast.makeText(getBaseContext(), "RQST LOG of GPS" , Toast.LENGTH_SHORT).show();
						flgRqstLogGPS= true;
						configLaunch(0);
				}
				
				
				
				// COMANDO = COLGADO
				if (valSMSCmd.indexOf(var.COLGADO) ==0) {
						Log.i(TAG,"COMANDO RQST COLGADO TELF");
						setMsgPopUp(TAG +  "comando RQST COLGADO TELF"  + " "   + " From:" + valSMSFrom);
						//Toast.makeText(getBaseContext(), "RQST LOG of GPS" , Toast.LENGTH_SHORT).show();
						flgColgadoTelf= true;
						configLaunch(0);
				}
				
				
				
				
			    // COMANDO = CALL ME
				if (valSMSCmd.indexOf(var.CALL_ME) ==0) {
					int idx = valSMSCmd.indexOf(",");
					nroCall = valSMSCmd.substring(idx+1);
					Log.i(TAG,"COMANDO RQST CALL TO = "+ nroCall);
					setMsgPopUp(TAG +  "comando RQST CALL TO = "+ nroCall  + " "  + " From:" + valSMSFrom);
					flgCallTelfNro = true;
					configLaunch(0);
				}
				
			
				
		}else {
			
			configLaunch(0);
			
		}

		return START_STICKY;
    }
	
	
	
	
		
	//---------------------------------------------------------------------------------------------------------------------
	//      					METODOS y FUNC DEL SERVICE
	//---------------------------------------------------------------------------------------------------------------------
	


	//      					 LEO CONFIG Y DETERMINA si LANZO THREAD
	//---------------------------------------------------------------------------------------------------------------------
	private void configLaunch(int tiempoTrck) {
			int tracking = -1;
			
			ManageFile mgFile = new ManageFile(getApplicationContext());
			rsltConfig = mgFile.readFile(var.Config_File);
			Log.i(TAG, "--------------- configLaunch() -----------------------");
			Log.i(TAG,"rsltConfig = " + String.valueOf(rsltConfig));			
			// el file-->"config.cfg" se crea solo una vez para todo el ciclo de vida.
			// si existe y esta bien la configuracin del equipo file-->"config.cfg" = TRUE
			// si tiene ERRs o no existe = FALSE
			if (rsltConfig) {
							
								String config = mgFile.getStrData();																			
								lstCfg = Funcion.SplitSimple(config, "~");														
								//Log.i(TAG,"READ CONFIG.CFG desplegado");																
								mgFile.closeFile();
								
								
								String url_sendHTTP = var.URL_SEND_TELEGRAM;																									// Harcodiamos la URL y no usamos la q tiene el Config.cfg
								//Log.i(TAG,"URL SERVIDOR PRINCIPAL : " + url_sendHTTP);																				// no usamos el var.urlHttp
								String url_getHTTP = var.URL_GetUpdate_TELEGRAM;
								
								
								//NOTA: Iniciamos la clase Lectura del GPS.
								if (tiempoTrck > 0)
									tracking = tiempoTrck;
								else	
									tracking = Funcion.StrToInt(lstCfg[var.tracking]);
								
								//Log.i(TAG,"Configuration of Config.cfg COMPLETED!");
								setMsgPopUp(TAG +  "from Config Tracking = " + String.valueOf(tracking)  + " " + Funcion.getFecha());
								
			
								//-------------- Location Listener -----------------------------------------
								//aseguro que la rutina que lee el GPS esta corriendo bien.
								gpsLoc = new GPSLocListener();
								gpsLoc.setLocFrec(600,getBaseContext(),true);														// true es para Provider Mod GPS
								//Log.i(TAG, "GPS Listener INICIADO");
								
								
								 // Create an instance of the BroadcastReceiver
								levelBatt= new PhoneBattery_RECEIVER();
						        
						        // Register the BroadcastReceiver to receive battery-related events
						        IntentFilter filter = new IntentFilter();
						        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
						        registerReceiver(levelBatt, filter);
						        
						        //Phone State
						        phState= new PhoneState(getBaseContext());
						        phState.tm.listen(phState, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);			// Activa el Listener del Evento de Señal SQE
						        phState.tm.listen(phState, PhoneStateListener.LISTEN_CALL_STATE);							// Activa el Listener del Evento de LLAMADA de Telfono.
								
								
						        Log.i(TAG, "Making the Pending NOTIFY ");
								//-------------- NOTIFICAITON -----------------------------------------
						        /*
								popUpNotiIntent = new Intent(getApplicationContext(), callTlf_Activity.class);			// PopUp_Notification.class
								pendingNotiIntent = PendingIntent.getActivity(getApplicationContext(), 0, popUpNotiIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
						        				
								//Las Notificaciones en Service Class neceitanun Permiso de FOREGROUND_SERVICE p mostrarlo al UI
								miNotify = new Notification.Builder( getApplicationContext())
								.setTicker(tickerText)
								.setSmallIcon(android.R.drawable.btn_dropdown)
								.setAutoCancel(true)
								.setContentTitle(contentTitle)
								.setContentText(contentText + " (" + Service_GPS.contador + ")")
								.setContentIntent(pendingNotiIntent).setSound(soundURI)
								//.setPriority(Notification.PRIORITY_MIN)
								.setVibrate(mVibratePattern);
								
								// 2º Pass the Notification to the NotificationManager:
								notiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
								notiManager.notify( MY_NOTIFICATION_ID, miNotify.build() );
						
							    // Start the foreground service & and pass the notification you created.
							    startForeground(MY_NOTIFICATION_ID, miNotify.build());			
							    Log.i(TAG, "NOTIFY INICIADO");
							    */
							    
							    
							    //Toast.makeText(getBaseContext(), "-- Service INICIADO --", Toast.LENGTH_SHORT).show();
						         
						        if (setThrdGPS==null){
							        	Log.i(TAG,"Creating mew Instance of setThrdGPS()");
							        	setMsgPopUp(TAG +  " First Creating New Instance -setThrdGPS()-  Trcking = " + String.valueOf(tracking));
							        	setThrdGPS = new SetThrdGPS( tracking, lstCfg[var.codeUser], url_sendHTTP,url_getHTTP,  lstCfg[var.descripcion] );								// int contador, int tracking, String codUser, String url_sendHTTP, String descripcion);
							        	setThrdGPS.start();
							        	
						        }else {
						        	
							        	// retmamos la Instancia
							        	// if (setThrdGPS.isRunning()) {
							        	if (setThrdGPS.isAlive()) {
							        			// set nre values
							        			Log.i(TAG,"Re-using Instance of setThrdGPS() existente");
							        			//setThrdGPS.tracking = tracking;
							        			setMsgPopUp(TAG +  " Using the same Instance running -setThrdGPS()-  Trcking =" + String.valueOf(setThrdGPS.getTracking()));
							        			
							        	}else {
							        			Log.i(TAG,"Re-creating new Instance of setThrdGPS() EXIST");
							        			setMsgPopUp(TAG +  "nulling & Re-Creating a New Instance -setThrdGPS()-  Trcking =" + String.valueOf(tracking));
								        	  	setThrdGPS=null;
								        	  	setThrdGPS = new SetThrdGPS( tracking, lstCfg[var.codeUser], url_sendHTTP,url_getHTTP,  lstCfg[var.descripcion] );								// int contador, int tracking, String codUser, String url_sendHTTP, String descripcion);
								        	  	setThrdGPS.start();
							        	}
						        }
								
								
		    
			}else {

								mgFile.closeFile();
								stopSelf();										//This is the same as calling stopService(Intent) for this particular service.
								Log.i(TAG,"DEBE 1º CONFIGURAR -- Config.cfg --");
			
			}
	}
	
	
	
    
	//              SETHILO
	//--------------------------------------------------------------------------------
	// HILO PRINCIPAL DEL GPS
	private  class SetThrdGPS extends Thread{
			int tracking = 60;
			String codUser="";
			String url_sendHTTP="";
			String url_getHTTP="";
			String respFromHttp="";
			String descripcion="Service_GPS-MicLab";
			HashMap<String, String> kvGPS = new HashMap<>();
			
	        int timeLoopRdGPS=0;
	        int offLoopRdGPS = 20;//((int)tracking/2);
	        int loopBattSignal = 30; 
	    	int timeLoopBattSign=0;
	    	int timeLoopRdCmdHTTP=70;								// 20 segundos.
	    	int timeTrckng=0;
	    	String strData = "";
	    	int timeEnvioHttpIni = 0;
	    	int timeToReadCmdHTTP = 0;
			int timeFistMax = 2;												// 15 seg
			int cntr=0;
 		    private boolean  flgRunning = false;
 		    private int tictocTime=0;										// es un contador q se incrementa en 1segundo y sirve como base para otros contadores en el Hilo Principal del Service para tareas.
 		    
 		    //Temporizador countTime = new Temporizador(Long.MAX_VALUE, 1000);									// Duración total indefinida y un intervalo de 1 segundo
 		   
 		  
			
			public  SetThrdGPS( int itracking, String icodUser, String iurl_sendHTTP,String iurl_getHTTP, String idescripcion) {
				this.tracking = itracking;
				this.codUser = icodUser;
				this.url_sendHTTP = iurl_sendHTTP;
				this.url_getHTTP = iurl_getHTTP;
				this.descripcion = idescripcion;
				
				kvGPS.put("gps","0");
				kvGPS.put("vel","0");
				kvGPS.put("alt","0");
				kvGPS.put("lalo","0,0");

				
				//----- RUNNING THREAD -------------------
				Log.i(TAG, "--------------- Running SetThrdGPS() -----------------------");
				Toast.makeText(getBaseContext(), "...Run ThreadGPS", Toast.LENGTH_SHORT).show();
				setMsgPopUp(TAG + "...Running ThreadGPS" + "  " + Funcion.getFecha());
			
			}
			
			
			
			public void run() {
				
					flgRunning = true;
					//countTime.start(); 																												// Inicia el temporizador sin duración total
					
					 Timer timerAudit = new Timer();
			 		 OvrTime sndOvrTime= new OvrTime();								//extiende de la clase TimerTask
			 		timerAudit.schedule(sndOvrTime,0,1000);
					
					while( flgRunning==true ) {
						
		    			try {
			        				//Service_GPS.delay(1000);
		    					
			        				try {
				        				Thread.sleep(100);
				        			} catch (InterruptedException e) {
				        				Log.i(TAG,e.getMessage());
				        			}
			        			
		    				
		    				
			        				
		        					// Log.i(TAG,String.valueOf(cntr)  + " " + Funcion.getFecha());
		        					/*
			        				miNotify.setContentText(contentText + " (" + cntr + ")");
				        			notiManager.notify( MY_NOTIFICATION_ID, miNotify.build() );
				        			*/
				        			
				        			setContador(++cntr);
				        			//setMsgPopUp(TAG +  "cntr ==  " + String.valueOf(cntr));
				        			//Toast.makeText(getBaseContext(), "cntr = " + String.valueOf(cntr), Toast.LENGTH_SHORT).show();
				        			
				        			if (cntr > 86400) {
				        				cntr =0;
				        			}
			        				
				        			
				        			
			        				//--------------------------------------------------------------------------------------------------
									//							4.-LEO LOC MODULO GPS / NETWORK
									//--------------------------------------------------------------------------------------------------
									//NOTA: si tracking=13 y offLoopRdGPS es 3/4tracking , de hay q quitarl 1 p q sea 13 seg.
									if (timeLoopRdGPS >  offLoopRdGPS -1) {
										
													timeLoopRdGPS= 0;

													// 	--------- GPS ------------------------------
													if (gpsLoc.getLocGPSFlg() == var.ERRLOC) {
																gpsLoc.setLocFrec(600,getBaseContext(),true);												// ture es para Provider MOD GPS	
																setMsgPopUp(TAG +  " ERR en la LOCALIZACION del GPS"  + "  " + Funcion.getFecha());
														
													}else {
																
																if (gpsLoc.getLocGPSFlg() == var.SILOC) {
																					gpsLoc.setLocGPSFlg(var.NOLOC);													// reseteo de nuevo flg p leer proximanete.
																					
																					//head
																					// = "gps=1" + "~" + "Velocidad=" + gpsLoc.getVeloc() + "~" + "Altura=" + gpsLoc.getAltura() +  "~"  + "LatLon=" + gpsLoc.getLat() + "," + gpsLoc.getLon();"																	"
																					kvGPS.put("provider",gpsLoc.getProvider());							
																					kvGPS.put("vel", gpsLoc.getVelocGPS());
																					kvGPS.put("alt", gpsLoc.getAlturaGPS());
																					kvGPS.put("lalo", gpsLoc.getLatGPS() + "," + gpsLoc.getLonGPS());
																					
																					// alamcenso en DataLogger las ubicaciones gps
																					logGPS("GPS: " + gpsLoc.getLatGPS() + "," + gpsLoc.getLonGPS() + "  " + Funcion.getFecha());
																					
																					//Log.i(TAG,"..LOCALIZACION...OKEY Latitud/Longitud = "+ gpsLoc.getLat() + "," + gpsLoc.getLon() + Funcion.getFecha());
																					setMsgPopUp(TAG +  "..LOC OKEY Lati/Long GPS= "+ gpsLoc.getLatGPS() + "," + gpsLoc.getLonGPS()  + "  " + Funcion.getFecha());
																					//Toast.makeText(getBaseContext(), "LOCALIZACION OKEY", Toast.LENGTH_SHORT).show();
																					
														
																}else {
																					gpsLoc.setLocFrec(600,getBaseContext(),false);			// true o false ???				// false es para Provider NETWORK, no se usa frec=900
																					// kvGPS= "gps=0" + "~" + "Velocidad=0" + "~" + "Altura=0" +  "~"  + "LatLon=0,0";
																					/*
																					kvGPS.put("gps", "0");															
																					kvGPS.put("vel", "0");
																					kvGPS.put("alt", "0");
																					kvGPS.put("lalo", "0,0");
																					*/
																					//Log.i(TAG,"...No Hay LOCALIZACION..." + " " + Funcion.getFecha());
																					logGPS("NET: " + gpsLoc.getLatGPS() + "," + gpsLoc.getLonGPS() + "  " + Funcion.getFecha());
																					setMsgPopUp(TAG + "...No Hay LOCALIZACION... GPS"  + " " + Funcion.getFecha());
																					//Toast.makeText(getBaseContext(), "NO HAY LOCALIZACION", Toast.LENGTH_SHORT).show();
																}
													}
													

									} // fin this 4.-LEO LOC MODULO GPS / NETWORK
									
		
									

									//--------------------------------------------------------------------------------------------------------------
									//						EXTERNAL COMMANDS SMS
									//---------------------------------------------------------------------------------------------------------------
									if (  flgRqstLogGPS==true) {
											flgRqstLogGPS=false;
											Log.i(TAG,"comando Request LOG of LOCATION GPS" + "  "  + Funcion.getFecha());
											setMsgPopUp(TAG + "comando Request LOG of LOCATION GPS " +   " From:" + valSMSFrom + "  " + Funcion.getFecha());
											strData  =  URLEncoder.encode("GPS LOG cod: " + codUser  + " :"  + (char)10  +  logLatLon, "UTF-8");
											mySend = new sendHTTPGet(url_sendHTTP, strData);
											mySend.sendGetRequest();
									}
									
									
																		
									if (  flgCallTelfNro==true) {
											flgCallTelfNro=false;
											Log.i(TAG,"comando CALL to TELF = " +   nroCall +  " From:" + valSMSFrom + "  " + Funcion.getFecha());
											setMsgPopUp(TAG + "comando CALL to TELF = " +  nroCall +  " From:" + valSMSFrom + "  " + Funcion.getFecha());
											
											//Launch Activity for CALL TEFL
											/*
											Intent launchIntent = getPackageManager().getLaunchIntentForPackage("course.examples.localize_telegram.callTlf_Activity");
											launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						    				startActivity(launchIntent);
						    				*/
											
											/*
						    				 Intent launchIntent = new Intent(Service_GPS.this,callTlf_Activity.class);
						    				 launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											 //itnConfig.putExtra(SERVICE_CONN,flgConnected);
											startActivity( launchIntent);
											*/

											
											// envio reg al Chat
											strData  =  URLEncoder.encode("comando CALL TELF : " + nroCall + "  cod: " + codUser  +  "  From:" + valSMSFrom, "UTF-8");
											mySend = new sendHTTPGet(url_sendHTTP, strData);
											mySend.sendGetRequest();
									}
									

									
									if (  flgColgadoTelf==true) {
											flgColgadoTelf=false;
											Log.i(TAG,"comando COLGADO de TELF = " +   nroCall +  " From:" + valSMSFrom  + " " + Funcion.getFecha());
											setMsgPopUp(TAG + "comando COLGADO de TELF " +  nroCall + "  " + Funcion.getFecha());
	
											phState.colgarTelf();
											
											strData  =  URLEncoder.encode("comando COLGADO de TELF :" + nroCall + "  cod:" + codUser  +  " From:" + valSMSFrom, "UTF-8");
											mySend = new sendHTTPGet(url_sendHTTP, strData);
											mySend.sendGetRequest();
									}
									
									
									
									
									
									
									
		
									//---------------------------------------------------------------------------------------------------
									//							5.-VERIFICO NIVEL DE BATERIA
									//---------------------------------------------------------------------------------------------------
									if (timeLoopBattSign > loopBattSignal ) {
				
												timeLoopBattSign=0;
												
												//-------------Arovecho de Leer algunos Stados--------------------
												if (gpsLoc != null) {
														if (gpsLoc.isOnOffProvider() ==false) {
		
																Log.i(TAG,".....Disponibilidad de Modulo GPS : False ACTIVELO"   + "  " + Funcion.getFecha());
																setMsgPopUp("Modulo GPS = FALSE"  + " " + Funcion.getFecha());
																
														}else {
																Log.i(TAG,".....Disponibilidad de Modulo GPS : True"   + "  " + Funcion.getFecha());
																setMsgPopUp("Modulo GPS = TRUE" + " " + Funcion.getFecha());
														}
		
												}else {
													Log.i(TAG,"GPSLocListener  gpsLoc == null "   + "  " + Funcion.getFecha());
												}
													
													
												//------------Leo el Nivel de Batt: Reg Batt Receiver-------
										        // Register the BroadcastReceiver to receive battery-related events
										        IntentFilter filter = new IntentFilter();
										        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
										        registerReceiver(levelBatt, filter);
												strLevelBatt = "batt= " + String.valueOf(levelBatt.getNivelBatt());
												
												//------------Leo el RSSI-------------------------------------
												strRssi = "signal=" + phState.getRssi();				//en dBm.
												Log.i(TAG,"VERIFICACION BATT y SIGNAL  "   + strLevelBatt  + " / "  + strRssi + Funcion.getFecha());
												
												
												//------------Leo estado del TELF----------------------------
												switch(phState.getTlfState()) {
												
																case TelephonyManager.CALL_STATE_IDLE:
																																		statePhone ="Tefl=Colagdo"; 
																																		Log.i(TAG,"onCallStateChanged() CALL_STATE_IDLE -COLGADO-");  // 0
																																		break;				
																	
																case TelephonyManager.CALL_STATE_OFFHOOK:
																																		statePhone ="Tefl=Levantado";
																																		Log.i(TAG,"onCallStateChanged() CALL_STATE_OFFHOOK -TELF LEVANTADO" );  // 2
																																		break;				
																	
																case TelephonyManager.CALL_STATE_RINGING:
																																		statePhone ="Tefl=Ringing";
																																		Log.i(TAG,"onCallStateChanged() CALL_STATE_-RINGING-");	//1
																																		break;
												
												}	
												
												setMsgPopUp("BATT y SIGNAL = " +   strLevelBatt  + " / "  + strRssi  +  " / " + statePhone +  " From:" + valSMSFrom + " " + Funcion.getFecha());
												
									}
		
									
									
									
									
									//-----------------------------------------------------------------------------------------------------
									//						6.-ENVIO al SERVIDOR HTTP LOCALIZACION
									//-----------------------------------------------------------------------------------------------------					
									if (  (timeTrckng > tracking && flgThrdHTTP==false)  ||  flgSendBeforeSTOPme){
				
											timeTrckng =0;
											//anulo el envio de First Inicio
											timeEnvioHttpIni=0;
											flgThrdHTTP =true;
											
											if (flgSendBeforeSTOPme) {
												setMsgPopUp(TAG + "Envio Localizacion x HTTP- STOPPING" + " " + Funcion.getFecha());
												strData  =  URLEncoder.encode( "STOP:" + codUser + "|" +   strLevelBatt + "|" +  strRssi + "|" + statePhone  + "|" + kvGPS.get("provider") +  "|"  + "vel="+ kvGPS.get("vel")     + "|" + var.map  +  kvGPS.get("lalo") 	, "UTF-8");
												
												mySend = new sendHTTPGet(url_sendHTTP, strData);
												mySend.sendGetRequest();
												
												Log.i(TAG,"Envio Localization HTTP = "   + strData + Funcion.getFecha());
												Log.i(TAG,"STOPPING Service_GPS.");
												
												flgSendBeforeSTOPme = false;
												setStopThread();
			
												
											}else {
											
												Log.i(TAG,"Envio Localizacion (http) "   + "  " + Funcion.getFecha());
												setMsgPopUp(TAG + "Envio Localizacion (http) " + " " + Funcion.getFecha());
												strData  = URLEncoder.encode( codUser + "|" +   strLevelBatt + "|" +  strRssi + "|" + statePhone  + "|"   + kvGPS.get("provider") + "|" +   "vel="+ kvGPS.get("vel")     + "|" + var.map  +  kvGPS.get("lalo")  , "UTF-8");
												mySend = new sendHTTPGet(url_sendHTTP, strData);
												mySend.sendGetRequest();
												
												Log.i(TAG,"Envio Localization HTTP = "     + strData + Funcion.getFecha());
											
											}
									}
				
									
								
									
									//--------------------------------------------------------------------------------------------------------------
									//						7.-FIRST TIME X UNICA VEZ AL INICIAR APP PASADO 10seg CONX HTTP 
									//---------------------------------------------------------------------------------------------------------------			
									if (( timeEnvioHttpIni >  timeFistMax)  &&  flgFirstTime==false) {
											
											//timeFistMax = timeEnvioHttpIni + 40;			//Si Falla este envio, es decir si no llega el ACK "var.HdFisrttime_Http" pata que flgFirstTimeOn = false no llega a setearse entonces hago un INTENTO de 35 segundos.
										flgFirstTime =true;
											Log.i(TAG,"FirstTime Sync / Cnxn ServHTTP, INTENTO= "   + String.valueOf(timeFistMax) + "  " + Funcion.getFecha());
											setMsgPopUp(TAG + "Envio de FirstTime Sync " + " " + Funcion.getFecha());
								            
											// [0]http server ~ [1]envioHttp ~ [2]udp server ~ [3]Chnnl ~ [4]Trk/Udp ~ [5]ipLocal ~ [6]timeShtDwn ~ [7]codeName ~ [8]tlfnsReprts  ~ [9]Sentido  ~ [10]nota ~ [11]Clave ~
											strData  =  URLEncoder.encode("status:Inicio" + "|" +  "cod: " + codUser    + "|"  +  "config:" + descripcion + "|" + "tckng="+ String.valueOf(tracking) + "|" +  Funcion.getFecha(), "UTF-8");
											mySend = new sendHTTPGet(url_sendHTTP, strData);
											mySend.sendGetRequest();
									}
									
									// con este if blieamos la entrada al envio de INICIO de la APP hasta q se vuelva a activar por 1ra vez
									if (timeEnvioHttpIni > 10000) {
										timeEnvioHttpIni=timeFistMax;
									}
									
									
									
									
									//--------------------------------------------------------------------------------------------------------------
									//						8.-REQUEST CONFIG config.cfg 
									//---------------------------------------------------------------------------------------------------------------
									if (  flgRqstConfig==true) {
											flgRqstConfig=false;
											Log.i(TAG,"Request CONFIG config.cfg" + "  "  + Funcion.getFecha());
											setMsgPopUp(TAG + "Request Config Command " + " " + Funcion.getFecha());
											
											// envio estos valores para saber por que no envia un Lcalizacion c/Trackinf seteado
											// if (  (++timeTrckng > tracking && flgThrdHTTP==false)  ||  flgSendBeforeSTOPme){
											
											strData  =  URLEncoder.encode("Configuration= " +  "cod: " + codUser    + "|"  +  "config:" + descripcion + "|" + "tckng="+ String.valueOf(tracking) + "|" + 
											"timeTrckng="+ String.valueOf(timeTrckng) + "|" + "flgThrdHTTP="+ String.valueOf(flgThrdHTTP) + "|" +  "flgSendBeforeSTOPme="+ String.valueOf(flgSendBeforeSTOPme) 
											+ "  " +  Funcion.getFecha(), "UTF-8");
											mySend = new sendHTTPGet(url_sendHTTP, strData);
											mySend.sendGetRequest();
									}
									
									

									//--------------------------------------------------------------------------------------------------------------
									//						9.-REQUEST LOG of SCREEN 
									//---------------------------------------------------------------------------------------------------------------
									if (  flgRqstLogALL==true) {
										flgRqstLogALL=false;
											Log.i(TAG,"Request ALL LOG of SCREEN" + "  "  + Funcion.getFecha());
											setMsgPopUp(TAG + "Request all LOG Command " + " " + Funcion.getFecha());
											strData  =  URLEncoder.encode("LOG cod: " + codUser    + "|"  +  msgPopUp, "UTF-8");
											mySend = new sendHTTPGet(url_sendHTTP, strData);
											mySend.sendGetRequest();
									}
									
									
									
									//-----------------------------------------------------------------------------------------------------
									//						10.-ENVIA LEER ORDEN COMANDO del HTTP c/15seg -ERR
									//-----------------------------------------------------------------------------------------------------	
									if (( timeToReadCmdHTTP >  timeLoopRdCmdHTTP) && flgThrdHTTP==false) {
											timeToReadCmdHTTP=0;
											flgThrdHTTP =true;
											/*
											mySend = new sendHTTPGet(url_getHTTP," ");
											mySend.sendGetRequest();
											setMsgPopUp(TAG + "Envio Comando  --getUpdates-- " + " " + Funcion.getFecha());
											Log.i(TAG,"Envio Rqst comando --getUpdates-- al Servidor HTTP");		// String.valueOf(obj.getBoolean("ok")));
											*/
									}
		
									
									
									
									//-----------------------------------------------------------------------------------------------------
									//						11.- THE RECEIVE for ALL RQST OPTION DID BEFORE to SERV HTTP in JSON format
									//						BEFORE meanas options : 10, 9, 8, 7, 6, 4.1 
									//-----------------------------------------------------------------------------------------------------	
									if (flgThrdHTTP) {
										flgThrdHTTP = false;
										
										Log.i(TAG, "--->Read JSON Data .length()= " + String.valueOf(respFromHttp.length()));
										
										// 13/12/2023  la rutina de aqui buscala en D:\adtEclipseSpace\adtAndroidSpace\localize_Telegram\readDocs
										// file = ruitna LeerJSON_Telegram.txt
										
											Log.i(TAG, "---------------------FIN DE CICLO--------------------------- ");	
										} //fin de flgThrdHTTP
				
		
									

		    			} catch (Exception e) {
		    					flgThrdHTTP=false;
		    					
		    					Log.i(TAG,"ERR  setThrdGPS.run() = " + e.getMessage());
			    				setMsgPopUp(TAG + "ERR  setThrdGPS.run() = " + e.getMessage() + " " + Funcion.getFecha());
			    				
			    				if (mySend !=null) {
			    					mySend = null;
			    				}
			    				
			    				try {
									strData  =  URLEncoder.encode("ERR setThrdGPS.run() = STOPPED" + "| ERR: " +  e.getMessage() + "  " +  "| Debug: "  +  msgPopUp, "UTF-8");
									mySend = new sendHTTPGet(url_sendHTTP, strData);
									mySend.sendGetRequest();
			    				} catch (Exception ee) {}
			    				
			    				flgRunning=false;
		    			}
		    				
		    		}  // fin while()
					

				} // fin run()
			
		
		
			//----- METHODs ------------
			
			
			
			
			
			/*
			public boolean isRunning() {
		        return flgRunning;
		    }
		    */
			
			public void setTicToc() {
				tictocTime++;
				timeLoopRdGPS++;
				timeLoopBattSign++;
				timeTrckng++;
				timeEnvioHttpIni++;
				timeToReadCmdHTTP++;
			}
			
			
			public void setFlgRunning(boolean flg) {
		         flgRunning = flg;
		    }
		
			public int getTracking() {
		         return this.tracking;
		    }
			
			
			
			

	}// class setThrdGPS()
	
	
		

	

		
	

	//              SET
	//--------------------------------------------------------------------------------
		
	
	// Almacena tod las Localizaciones del GPS mas los ultimo 50 puntos.
		public static void logGPS(String latlon) {
			
			int count =0;
			
			for (int i = 0; i < logLatLon.length(); i++) {
			    if (logLatLon.charAt(i) == (char)10) {
			        count++;
			    }
			}
		
			String lastLatLon = "# " + getContador() + "|" + latlon + (char)10;
			if (count > 50) {
				int idx = logLatLon.indexOf((char)10);
				logLatLon = logLatLon.substring(idx+1) + lastLatLon; 
			}else {
				logLatLon += lastLatLon;
			}

		}
	
	
	
	
	
	// display Log nsgs to the screen UI of Localize_main.
	public static void setMsgPopUp(String imsgPopUp) {
		
		int count =0;
		
		for (int i = 0; i < msgPopUp.length(); i++) {
		    if (msgPopUp.charAt(i) == (char)10) {
		        count++;
		    }
		}
		
		if (count > 40) {
			msgPopUp = "# " + getContador() + " " + imsgPopUp + (char)10;
		}else {
			msgPopUp += "# "+   getContador() + " " +imsgPopUp + (char)10;
		}

	}
	
	
	public String getMsgPopUp() {
		return Service_GPS.msgPopUp;
	}
	
	
    public void setContador(int cntr) {
		contador = cntr;
	}	
	
    
 // Metodo del HILO Leer
 	public static String getContador() {
 		//Log.i(TAG, " Method GET DATA" + "   contador=" + String.valueOf(contador));
 		return String.valueOf(contador);
 	}
    
 	
    
    // -------- STOP THREAD ---------
    public static void setStopThread() {
	    	if (setThrdGPS!=null){
	    		setThrdGPS.setFlgRunning(false);
	    		setMsgPopUp(TAG +  ".....SET STOP RUNNING SERVICE THREAD" + " " + Funcion.getFecha());
			    //flgRunning = false;
			    //stopSelf();																//This is the same as calling stopService(Intent) for this particular service.
	    	}
	}	
	
   

    

	//              GET
	//--------------------------------------------------------------------------------
	public String getSysTime() {
			SimpleDateFormat sDate = new SimpleDateFormat("hh:mm:ss dd/mm/yyyy", Locale.US); 
			return String.valueOf(sDate.format(new Date())) + " / " + String.valueOf(this.contador);
	}
	
	// Devuelve si el Config.cfg esta disponible SI/NO
	public boolean flgConfig() {
		return rsltConfig;
	}
	
	public String [] getConfig() {
		return this.lstCfg;
	}
	
		    
	
		
	
	
		
	// 			CONECTORES BINDER
	//------------------------------------------------
	public void onReBind(Intent intent) {
		Log.i(TAG, "onRebind( )");
		configLaunch(0);
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		// This method is called when a client unbinds from the service using unbindService()
		// return The Intent that was used to bind to this service
		Log.i(TAG, "onUnbind( )");
		return super.onUnbind(intent);	
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// This method is called when a client binds to the service
		Log.i(TAG, "onBind( )");
		return localBinder;
	}
		
	
	// ------ sub Clase de Coneccion -------------------
	// devuelve toda la Clase Service
	public class miBinder extends Binder{
		Service_GPS getBoundService() {
			return Service_GPS.this;
		}
	}
	

	
	@Override
    public void onDestroy() {
		//This method is called when the service is being destroyed.
		// It is typically used to release any resources or perform cleanup tasks. 
		// After this method is called, the service is considered terminated and cannot be restarted unless explicitly started again.
        super.onDestroy();
        // detengo el Listener
        phState.tm.listen(phState, PhoneStateListener.LISTEN_NONE);
        
		Log.i(TAG, "onDestroy(), ShutDown Service");
		Toast.makeText(getBaseContext(), "Service_GPS ShutDown and STOPED", Toast.LENGTH_SHORT).show();
        //stopForeground(true);							// remove Notifications
    }

	
	
	
	//				CLASS ENVIO HTTP
	//----------------------------------------------------------------------------
	public class sendHTTPGet  implements Runnable{

	    //private static final String BASE_URL = "https://example.com/api";
		private Thread sendHTTP = null; 
	    private String urlData="", urlServer="";
	    private StringBuilder urlBuilder=null;
	    private HttpURLConnection connection=null;

	    
	    private sendHTTPGet(String urlServer , String urlData) {
	    	this.urlServer = urlServer;
	    	this.urlData = urlData;
	    	//flgThrdHTTP = false;
	    }
	    
	    private void sendGetRequest() {
			sendHTTP = new Thread ((Runnable) this);																										//this x que le decimos que el metodo 'run()' esta en la misma clase 'sendHTTPGet'.
			sendHTTP.start();
	    }

			
		public void run() {
	    	
			        // Create a StringBuilder to construct the request URL
			        urlBuilder = new StringBuilder(this.urlServer);
			        urlBuilder.append(urlData); // Add your request parameters here
		
			        try {
			            // Create the HttpURLConnection object and set the request properties
			            URL url = new URL(urlBuilder.toString());
			            connection = (HttpURLConnection) url.openConnection();
			            connection.setRequestMethod("GET");
		
			            // Get the response code
			            int resp = connection.getResponseCode();
		
			            if (resp == HttpURLConnection.HTTP_OK) {
			                
				            	/*
				            	// Verufy if the Streamming are Type = JSON format.
				            	String contentType = connection.getHeaderField("Content-Type");
				            	if (contentType != null && contentType.contains("application/json")) {
				            			Log.i(TAG, "Receive a Type =" + "JSON format");
				            	}else {
				            			Log.i(TAG, "sendHTTPGet Receive NO JSON format type");
				            	}
				            	*/
				            	
				            	// Read the response from the input stream
				                InputStream inputStream = connection.getInputStream();
				                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				                String line;
				                StringBuilder responseBuilder = new StringBuilder();
			
				                while ((line = reader.readLine()) != null) {
				                    responseBuilder.append(line);																		// Lee de la Web c/line y lo apila en el un StringBuilder 
				                }
			
				                
				                // respFromHttp de este Servidor es del tipo JSON file.
				                String respFromHttp = responseBuilder.toString();
				                if (respFromHttp.length() > 30) {
				                	Log.e(TAG, "---> HTTP response: "+ respFromHttp.substring(0,30));
				                }else {
				                	Log.e(TAG, "---> HTTP response: "+ respFromHttp);
				                }
				                
				                		
				                // Close the input stream and disconnect the connection
				                Log.e(TAG,"---> HTTP Send Completed" + "  " + Funcion.getFecha());
				                setMsgPopUp("---> HTTP Send Completed" + "  " + Funcion.getFecha());
				                inputStream.close();
			                

			            }else {
			            	
				                // Handle error response
				                Log.e(TAG, "---> Error: "+  "HTTP Code Error: " + resp);
				                setMsgPopUp("---> ERR sendHTTPGet(): "+  "ERR HTTP = " + resp + " " + Funcion.getFecha());
			                
			            }
		                connection.disconnect();

			        } catch (IOException e) {
			            //e.printStackTrace();
			            setMsgPopUp("---> ERR sendHTTPGet(): " + e.toString() + " " + Funcion.getFecha());
			            //respFromHttp = "";
			            //sendHTTPGet Error Deconocido: java.net.UnknownHostException: Unable to resolve host "api.telegram.org": No address associated with hostname 2-6-2023 8:40:28
			            
			        }
			        //flgThrdHTTP = false;
		}
		
		
		
		// 				METHODs
		//-------------------------------------------------
		

	}// fin de subClase sendHTTPGet
	
	
	
	
//	CLASS TEMPORIZADOR-TASK
//----------------------------------------------------------------------------
	
	public class OvrTime extends TimerTask {
		 
	    @Override
		public void run() {
	    	if (setThrdGPS!=null)
	    		setThrdGPS.setTicToc();
	    	else
	    		 Log.i(TAG, "El setThrdGPS no esta Instanciaso.");
	    }
	}
	
	
	

	//				CLASS TEMPORIZADOR - INDEFINIDO
	//----------------------------------------------------------------------------
	private static class Temporizador extends CountDownTimer {

	        public Temporizador(long duracion, long intervalo) {
	            super(duracion, intervalo);																							// Ej: super(duracion, intervalo);    seria una Duración total de 60 segundos y un intervalo de 1 segundo.
	        }
	
	
	        @Override
	        public void onTick(long millisUntilFinished) {
	          Log.i(TAG, "Segundos transcurridos: " + (millisUntilFinished / 1000));
	          // Aquí puedes realizar acciones adicionales si es necesario
	        }
	
	        @Override
	        public void onFinish() {
	          // Este método se llama cuando el temporizador llega a su fin (no aplicable en
	          // este caso)
	        }
    }
	
	
	

}// fin de MiBoundService Class
