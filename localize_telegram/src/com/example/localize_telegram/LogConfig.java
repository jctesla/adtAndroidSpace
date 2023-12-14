// esta Clase representa el login para entrar a la configuracion del equipo "Configuracion.java"   

package com.example.localize_telegram;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LogConfig extends Activity {

	public static final String LOGCONFIG_RSLT = "LOGCONFIG_RSLT";	//indica que se esta trabajando con los datos de la Clase InspectorNormal.
	public static final String  SERVICE_CONN = "READ FLAG SERVICE CONNEC";
	
	private final static String TAG = "LogConfig - Admin";
	
	TextView lblNomInspector;
	TextView txtClaveAdm;
	Button btnOkConfig;
	Button btnBackConfig;
	String [] strDatos;
	ManageFile mgFile=null;
	//Intent intent = new Intent(this, Service_GPS.class);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Funcion.setLogo(LogConfig.this);
		setContentView(R.layout.log_config);
		
		lblNomInspector = (TextView)findViewById(R.id.lblNomInspector);
		txtClaveAdm = (TextView)findViewById(R.id.txtClaveAdmLog);
		btnOkConfig = (Button) findViewById(R.id.btnOkConfig);
		btnBackConfig= (Button) findViewById(R.id.btnBackConfig);
		
		
		//obtengo el nombre del inspector que esta accediendo a la configuracion
		String getNomInspector = "";
		lblNomInspector.setText(getNomInspector);	
		
		
		//rutina de Boton OK
		btnOkConfig.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {

				String clave=txtClaveAdm.getText().toString();
				Log.i(TAG,"Lei clave Ingresada : " + clave);
				
				if (clave != null && clave.length() > 0) {

						//si no hay spacio vacio y <> null evaluo la clave.
						//ManageFile fc = new ManageFile(LogConfig.this,"config.cfg");
						Intent configuracion = new Intent(LogConfig.this,Configuracion.class);			// del Activity actual, al activity q queiro saltar dspues (preparo el Itent.)
						
						if (mgFile==null)
							mgFile = new ManageFile(getApplicationContext());
						
						if (mgFile.readFile(var.Config_File)==true) {															//si existe el config.cfg y existen los registrs completos o no existen x primera vez.
								
								// 	SI EXISTE EL config,cfg
								strDatos = Funcion.SplitSimple(mgFile.getStrData(), "~");							//http server:port ~ USBChnnl ~ udpSrv ~ Trk ~ ipLocal ~ timeShtDwn ~ pdrn#crwy# ~ tlfnsReprts ~ Sentido ~ NroLinea#VersionSoft# ~ Clave ~
								Log.i(TAG,"Clave : " + strDatos[var.claveCfg]);
								if (strDatos[var.claveCfg].equals(clave)) {													//EVALUO LA CLAVE del CONFIG.
										
									//-------- ACCESO CORRECTO -------------
									 	Service_GPS.setStopThread();
										Log.i(TAG,"Clave Correcta");
										Toast.makeText(getApplicationContext(), "ACCESO OK",Toast.LENGTH_SHORT).show();
										configuracion.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										configuracion.putExtra(LOGCONFIG_RSLT,mgFile.getStrData());
										startActivity(configuracion);

								}else {
										Log.i(TAG, "Ok - Clave : " + "CLAVE INCORRECTA!");
										Toast.makeText(getApplicationContext(), "Clave Incorrecta",Toast.LENGTH_SHORT).show();
									
								}
								
						 }else {
							 	// SINO EXISTE EL config,cfg
							 	// NOTA: Solo 1ra VEZ, crea valores por DEFECTO.
								txtClaveAdm.setText("");
								Log.i(TAG, "...No Existen los datos o No exite el file de Configuracion");
								
								//-------- ACCESO CORRECTO -------------
								if (clave.equals("000000")) {
											Service_GPS.setStopThread();
											//Como NO EXISTE la config se carga x DEFECTO
											String exprt = mgFile.getStrData();															// de la Clase ManageFile--Config.cfg
											configuracion.putExtra(LOGCONFIG_RSLT,exprt);
											configuracion.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivity(configuracion);
								}else {
											Toast.makeText(getApplicationContext(), "Clave Incorrecta",Toast.LENGTH_SHORT).show();
								}
							
						}

				}else {
					
					Log.i(TAG, "Ok - Clave : " + "Ingrese un Valor");
					Toast.makeText(getApplicationContext(), "Ingrese un Valor",Toast.LENGTH_SHORT).show();
					//finish();
				}
					
		     }
	    });


		
		
		
		
		//rutina de Boton Fecha Subida
		btnBackConfig.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						finish();
					}
		});

	}//onCreate

}
