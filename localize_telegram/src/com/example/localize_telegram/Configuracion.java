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

public class Configuracion extends Activity {

	public static final String CONFIG_RSLT = "CONFIG_RSLT";	//indica que se esta trabajando con los datos de la Clase InspectorNormal.
	private final static String TAG = "Configuracion";

	TextView txtCodUser,txtDescripcion,txtUrlHTTP,txtTracking,txtReTry,txtClaveConfig;
	Button btnAceptar,btnBorrar,btnSalir;
	String [] strConfig;
	ManageFile mgFileCfg=null;

	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Funcion.setLogo(Configuracion.this);
		setContentView(R.layout.configuracion);
		
		txtCodUser = (TextView)findViewById(R.id.txtCodUser);
		txtDescripcion =(TextView)findViewById(R.id.txtDescripcion);
		txtUrlHTTP = (TextView)findViewById(R.id.txtUrlHTTP);
		txtTracking = (TextView)findViewById(R.id.txtTracking);
		txtReTry= (TextView)findViewById(R.id.txtReTry);
		txtClaveConfig = (TextView)findViewById(R.id.txtClaveConfig);
				
		btnAceptar = (Button) findViewById(R.id.btnAceptar);
		btnBorrar = (Button) findViewById(R.id.btnBorrar);
		btnSalir = (Button) findViewById(R.id.btnSalir);
		
		
		//Recepciono y obtengo los datos del LogConfig.class la 1ra vez q se config..
		String strDato = getIntent().getStringExtra(LogConfig.LOGCONFIG_RSLT);
		strConfig = Funcion.SplitSimple(strDato,"~");
		
		txtCodUser.setText(strConfig[var.codeUser]);
		txtDescripcion.setText(strConfig[var.descripcion]);
		txtUrlHTTP.setText(strConfig[var.urlHttp]);
		txtTracking.setText(strConfig[var.tracking]);
		txtReTry.setText(strConfig[var.reTry]);
		txtClaveConfig.setText(strConfig[var.claveCfg]);
		
		
		//rutina de Boton "GRABAR"
		btnAceptar.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						String codU=txtCodUser.getText().toString();
						String descrip=txtDescripcion.getText().toString();
						String urlhttp=txtUrlHTTP.getText().toString();
						String track=txtTracking.getText().toString();
						String retry=txtReTry.getText().toString();
						String clave=txtClaveConfig.getText().toString();
						Log.i(TAG,"Config ="  + codU + "~" + descrip + "~" + urlhttp + "~"  + track + "~" + retry + "~"+  clave + "~");
						
						if ((codU.length() > 2) & (urlhttp.length() > 7))
							if (track.length() > 1)
								
									if (clave.length() > 5) {
										
													if (mgFileCfg==null)
															mgFileCfg = new ManageFile(getApplicationContext());
													
													mgFileCfg.deleteFile(var.Config_File); 
													
													if (mgFileCfg.openFile(var.Config_File) != false) {
														mgFileCfg.writeFile(codU + "~" + descrip + "~" + urlhttp + "~"  + track + "~" + retry + "~"+  clave + "~");
			
														if (mgFileCfg.closeFile() == true) {
															Log.i(TAG,"Configuracion se Grabo con exito");
															Toast.makeText(getApplicationContext(), "Configuracion se Grabo con exito", Toast.LENGTH_SHORT).show();
															finishAffinity();							//solo baja el actual Activity y regresa al anterior.
															
														}else {
															Log.i(TAG,"Error al Cerrar Configuracion i/o");
															Toast.makeText(getApplicationContext(), "Error al Cerrar Configuracion i/o", Toast.LENGTH_SHORT).show();
														}
														
													}else {
														Log.i(TAG,"Error al Abrir Configuracion i/o");
														Toast.makeText(getApplicationContext(), "Error al Abrir Configuracion i/o", Toast.LENGTH_SHORT).show();
													}
										
									}else
										Toast.makeText(getApplicationContext(), "Clave debe ser > Caracteres", Toast.LENGTH_SHORT).show();
								
							else
								Toast.makeText(getApplicationContext(), "Tracking debe ser > 1 caracter", Toast.LENGTH_SHORT).show();
						else
							Toast.makeText(getApplicationContext(), "Una de las Ips es Valida, debe > 10 carcteres", Toast.LENGTH_SHORT).show();
							
					}
		});
		
		
		//rutina de Boton "Salir" de Todo
		btnSalir.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getBaseContext()," STOP SERVICE", Toast.LENGTH_SHORT).show();
				finishAffinity();
			}
		});		
		
		
		
		//rutina de Boton "BORRAR"
		btnBorrar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				txtCodUser.setText("");
				txtDescripcion.setText("");
				txtUrlHTTP.setText("");
				txtTracking.setText("30");
				txtReTry.setText("3");
				txtClaveConfig.setText("100000");

			}
		});		
		

		/*
		//rutina de Boton Para Ocultar el Teclado
		btnHideKeyBoard.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//View view = this.getCurrentFocus();
				if (v != null) {  
				    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
		});		
		*/

	}
	
	
	@Override
	protected void onResume() {
			super.onResume();
			//Toast.makeText(getBaseContext(), "onResume()", Toast.LENGTH_SHORT).show();
			//GpsTlgrm_Main.stopAllprocss();				//activo 01/07/2017
			//GpsTlgrm_Main.stopMain();							//activo 01/07/2017

	}//fin de Resume
	
	
}
