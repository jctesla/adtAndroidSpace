package com.example.localize_telegram;

import java.util.Timer;


import com.example.localize_telegram.R;
import com.example.localize_telegram.Service_GPS;

import com.example.localize_telegram.Service_GPS.miBinder;
import com.example.localize_telegram.OvrTime;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Localize_main extends Activity implements Runnable{

	private final static String TAG = "Localize_Main";
	public static final String  RECEIVE_SMS = "LEE SMS";
	public static final String  SERVICE_CONN = "READ FLAG SERVICE CONNEC";

	private TextView lblService,lblSetData;
	private Button btnTestGetData,btnIniciarService;
	
	static Timer timeWaitCall=null;														//creador de tareas para el envio de Marcacion y espera de OvrTime, es un envio con ACK.
	static Timer timeOffModGPS = null;													//Para detectar MODULO GPS Apagado.
	static OvrTime callPhOvrTime=null,offMdGpsOvrTime=null;				//extiende de la clase TimerTask
		
	Service_GPS serviceGps = null;
	private boolean flgConnected = false;
	boolean rsltConfig=false;
		
    ManageFile mgFile = null;
    static String config="";															//contiene en memoria actual la configuracion en un solo stream: 0.http server|1.udp server|2.ChannelSrv|3.Trk|4.ipLocal |5.timeShtDwn|6.pdrn|7.tlfnsReprts|8.sentido|9.Description|10.Clave| 		Nota: ChannelSrv = 1     timeShtDwn=4:10#23:10#		pdrn/cRWy=301#10#		tlfnsReprts=987970971#993148609#		tlfnsReprts = Tlf1#Tlf2#Tlf3#
    static String [] lstCfg;															//contiene en memoria actual toda la configuracion del equipo parceado individualmente.
    int typeDevice = 3;																	// est variable almacena el tipo de equipo,  en la Descripcion estan los campos: type#nroTlfDevice#DescripcionDev#Version#      /    type : 1= permite el auto-reset modelo LT6144 / 2= permite auto-reset modelo Pr5449 / 3= no permite auto-reset
    int tracking=10;																		//almacena el tracking del GPS UDP leido del file de configuracion.
    
    static String  url_sendHTTP = "";												// antes = "http://" + lstCfg[var.IpServHttp] + "/valiGVCO/operation/mobile/fromSPhone.asp"
    static String  url_getHTTP = "";												// antes = "http://" + lstCfg[var.IpServHttp] + "/valiGVCO/operation/mobile/fromSPhone.asp"
    int errCnxHTTP_Break = -1, errCnxHTTP_Srvr=-1;

    Intent intentService=null;
    static Thread display = null;
    boolean flgServiceRunning = false;
    String cmdSMS="";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		cmdSMS = getIntent().getStringExtra(RECEIVE_SMS);		// if no SMS :. cmdSMS es null, SMS esta informacion proviene del ReceiveSMS.class  originado por SMS. la orden
		if (cmdSMS == null)																
			cmdSMS="";

		Log.i(TAG,"onCreate() Activity INICIANDO Data from SMS= " + cmdSMS);

		// ==null no ha llegadi unSMS
		//if (cmdSMS == null) {
		setContentView(R.layout.localize_main);
				
		lblService = (TextView) findViewById(R.id.lblService);
		lblSetData = (TextView) findViewById(R.id.lblSetData);
		btnTestGetData = (Button) findViewById(R.id.btnTestGetData);
		btnIniciarService = (Button) findViewById(R.id.btnIniciarService);
				
		// EVENTO del Boton de Get Fecha
		btnTestGetData.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String jalaDate = serviceGps.getSysTime();				// lee del SERVICE la fecha
					String contador = Service_GPS.getContador();
					lblService.setText(jalaDate +" / " + contador );
		
				}
		});
			
				
				
		// EVENTO del Boton de Get Fecha
		btnIniciarService.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
								
								/*
								// NO FUNCIONA sin HILO
								Uri phoneUri = Uri.parse("tel:" + "993148609");
								Intent callIntent = new Intent(Intent.ACTION_CALL, phoneUri);
								startActivity(callIntent);
								 */
								
								/*
								 // SI FUNCIONA
								runOnUiThread(new Runnable() {
								    @Override
								    public void run() {
								        Uri phoneUri = Uri.parse("tel:" + "993148609");
								        Intent callIntent = new Intent(Intent.ACTION_CALL, phoneUri);
								        startActivity(callIntent);
								    }
								});
								*/
								
					
								int sdkVersion = Build.VERSION.SDK_INT;
								String androidVersion = Build.VERSION.RELEASE;
								Service_GPS.setMsgPopUp(TAG +  "sdkVersion: "+ sdkVersion);
								Service_GPS.setMsgPopUp(TAG +  "Android Version: "+ androidVersion);
								Log.i(TAG,"Activity SDK Version: " + sdkVersion);														//  Activity SDK Version: 22					Activity SDK Version: 29             					
								Log.i(TAG,"Activity Android Version: " + androidVersion);											//  Activity Android Version: 5.1.1		Activity Android Version: 10		

								
								// serviceGps.setThrdGPS( tracking, lstCfg[var.codeUser], url_sendHTTP,url_getHTTP,  lstCfg[var.descripcion] );								// int contador, int tracking, String codUser, String url_sendHTTP, String descripcion
								Log.i(TAG,  "PRESIONO BOTON Iniciar Service, Pero el Servie no esta Ligado");
							}
				});
		
	}// onCreate()
	
	
		
	
	// Method Permite leer el estado de mi SERVICEs si/no esta corriendo.
	//En este caso Leer si el Service_GPS esta activo/desactivo.
	public boolean isMyServiceRunning(Class<?> serviceClass) {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}

	

	
	
	//--------------------------------------------------------------------------------------
	//					CICLO DE VIDA DEL ACTIVITY
	//--------------------------------------------------------------------------------------
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.i(TAG,"Activity onSTART()" + " " + Funcion.getFecha());

	}


	@Override
	protected void onResume() {
			super.onResume();
			
			Log.i(TAG,"onResume() My Context = " + getBaseContext().getPackageName());
			Toast.makeText(getBaseContext(),"onResume()",Toast.LENGTH_SHORT).show();			// 15/06/2023
			
			ManageFile mgFile = new ManageFile(getApplicationContext());
			boolean rsltConfig = mgFile.readFile(var.Config_File);
			Log.i(TAG,"onResume rsltConfig = " + String.valueOf(rsltConfig));		
			
			if (!rsltConfig){
					Log.i(TAG,"NO HAY Configuracion Config.cfg!, Activity onRESUME() flgConfig");		
					try {
					 	  Intent itnConfig = new Intent(Localize_main.this,LogConfig.class);
						  startActivity( itnConfig);
					}catch(Exception ee) {
						ee.printStackTrace();
					}
					
			}else {
					
					//Toast.makeText(getBaseContext(), "onCreate()", Toast.LENGTH_SHORT).show();
					Log.i(TAG,"Config.cfg OKEY onRESUME()" + " DEPLOYED   " + Funcion.getFecha());
					
					// Creamos el vinculo con un intentService q va unir el Service al Activity.
					// Verifico si el SERVICIO esta Corriendo SI o NO
					if (isMyServiceRunning(Service_GPS.class) == false) {
						
							// declaro el intentService q va unir el Service q vamos a lanzar con el Activity												// con la Opcion : .BIND_IMPORTANT
							Log.i(TAG, "Service_GPS is STOPPED, Will LAUNCH");																					// ols msg = "Service_GPS = STOP, Will shoot Up"
							Toast.makeText(getBaseContext(),"Service is STOPPED, Will LAUNCH",Toast.LENGTH_SHORT).show();		// Service es el Service_GPS.
							serviceGps = new Service_GPS();
							intentService = new Intent(this, Service_GPS.class);
							bindService(intentService, serviceConn, Context.BIND_AUTO_CREATE);															// Crea una Vinculacion este Activity con el Servicio 'MiBoundService'
							startService(intentService);																																	// .BIND_AUTO_CREATE   dura poco y se desvanece
							flgServiceRunning = true;
						
					}else {
							Log.i(TAG, "Service is RUNNING, Will reconnect");
							Toast.makeText(getBaseContext(),"Service is RUNNING, Will reconnect",Toast.LENGTH_SHORT).show();	// ols msg = "Service_GPS= RUNNING, Will reconnect"
	
							intentService = new Intent(this, Service_GPS.class);
							bindService(intentService, serviceConn,0);																										// si quito este bindService, trabaja igual, pero no permite leer los valores de la Instancia Anterior de la Clase, es decir el Contador si va leerlo con el ultimo valor.
							startService(intentService);																																	// .BIND_AUTO_CREATE   dura poco y se desvanece
							flgServiceRunning = false;
					}
					
							
					// launch Thread of DISPLAY DEBUG of Service_GPS()
					display = new Thread ((Runnable) this);																														//this x que le decimos qye el metodo run esta en elmismo contexto de la clase.
					display.start();

			}
			
	}//fin de Resume
	
	
	
	
	// run() de DISPLAY DEBUG of Service_GPS() 
	// permite Displayar los mesnajes del Service_GPS
	public void run() {
			// TODO Auto-generated method stub
			//Service_GPS.delay(1000);
			Log.i(TAG,"INICIANDO DISPLAY de ACTIVITY");
			
			boolean ardilla = true;
			while(ardilla) {
				
					Localize_main.delay(1500);			//1500
					//Nos permite Vizualizar el TextView en el UI, How do we use runOnUiThread in Android?
					//https://stackoverflow.com/questions/11140285/how-do-we-use-runonuithread-in-android
					// este Hilo se usa como una funcion; permite lanzar Objetos q involucra el UI como un Hilo mas.
					// de otra forma no se puede usar los Obj UIs.
					runOnUiThread(new Runnable() {
	
	                    @Override
	                    public void run() {
	                    	lblSetData.setText(serviceGps.getMsgPopUp());
	                    }
	                });
									
					//Log.i(TAG,serviceGps.getMsgPopUp());
				
			}
	}	
	
	
	
	
	
	
	//DELAY DE ESTA CLASE
	public static void delay(int tiempo) {
		try {
	 		Thread.sleep(tiempo);	//1500 mseg
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
			
		
	
	

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG,"Activity onPAUSE()" + " " + Funcion.getFecha());
	}
	
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.i(TAG,"Activity onSTOP()" + " " + Funcion.getFecha());
	}
	

	@Override
	protected void onRestart() {
			super.onRestart();
			Log.i(TAG,"Activity onRESTART()" + " " + Funcion.getFecha());
	}
	
	
	// Ciclo Natural del Activity
	protected void onDestroy() {
		    super.onDestroy();
		    Log.i(TAG,"onDestroy()");
		    
		    if (flgConnected) {
		    	Log.i(TAG,"App shuting down");
		        unbindService(serviceConn);
		        flgConnected = false;
		    }else {
		    	Log.i(TAG,"NO HAY Bind de Service");
		    }
		    
	}
		
	
	
	
	
	
	// Componente q conecta el Binder.
	public ServiceConnection serviceConn = new ServiceConnection() {

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				miBinder binder = (miBinder) service;
				serviceGps = binder.getBoundService();
				Log.i(TAG,"flgConnected Connected = True");
				flgConnected = true;
		
			}
		
			@Override
			public void onServiceDisconnected(ComponentName name) {
				Log.i(TAG,"flgConnected Connected = False");
				flgConnected = false;
			}
	};
	

	
	//-------------------------------------------------------------------------
	//				OPTION MENU
	//-------------------------------------------------------------------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.top_menu, menu);
		return true;
	}
			
			
	// Process clicks on Options Menu items
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
				switch (item.getItemId()) {

					case R.id.configurar:
										try {
											  	
											   Log.i(TAG," Menu Configuar : ");
												//Lanzo Pantalla de Logginn de Configuracion.
											    Intent itnConfig = new Intent(Localize_main.this,LogConfig.class);
											    itnConfig.putExtra(SERVICE_CONN,flgConnected);
											    startActivity( itnConfig);
											    
										}catch(Exception ee) {
											ee.printStackTrace();
										}
										return true;		
										
					case R.id.above:
										//expongo todo las caracteristicas de esta version de Vali-GVCO
										Toast.makeText(getBaseContext(), "Localize Telegran" + " ver02. 17.05.2023", Toast.LENGTH_SHORT).show();
										return true;					//si no colocas TRUE no devuelve el contexto actual view.

					case R.id.debug:
										Toast.makeText(getBaseContext(), "Dont Implemented for this moment", Toast.LENGTH_SHORT).show();
										return true;		
									
									
					default:
										return false;
				}
	}

	
}
