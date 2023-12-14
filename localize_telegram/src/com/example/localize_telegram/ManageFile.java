package com.example.localize_telegram;


/*
DEFECTO =  "miclab.ddnsking.com:9099~127.0.0.1~1~9~10.174.55.23~4:10:#23:10:#~280#10#~987970971#993148609#~SentidoA_B~999999999#Ver210517#~000000~";
01.-CodUser				   :miclab.ddnsking.com:9099
02.-Descripcion			   :127.0.0.1
03.-UrlHttp					   :7 										;NOTA Segundos:  7+1 = 2.-Leo Loc Modulo GPS, 	7+2 =  3.-Verifico si Hay Marcacion GPS,  7+2 = 5.- Envio al SERVIDOR UDP.
04.-Tracking				   :60 seg
05.-ReTry					   :5										;nro de intentos de coneccion en caso de errors
06.-Clave Admin		   :000000								;Es la Clave de Acceso ala Configuracion
 */



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.util.Log;

public class ManageFile {

			private final static String TAG = "MangeFile";
			//NOTA en el String de la Config solo se usan como delimitadores '~'
			private final static String DEFECTO = "280-mic" + "~" + "Loc-Telegram v03 SMS-CALL Sense" + "~" + "http://miclab.com.pe" + "~" + "600" + "~" + "5" + "~" + "000000" + "~" ;
			private String strDato="";			//se accde desde afuera de la clase para leer este dato.


			//para open yWrite file
			private OutputStreamWriter bw =null;
			private Context cntx;
	
			public ManageFile(Context cntx){
				this.cntx=cntx;
	
			}
		
			//Metodo permite leer la configuracion del equipo que son 5 registros de un file a un arreglo string.
			//si no existe el file, la funcion arroja false. :. es true=indica que se puede leer el arreglo.
			public  boolean readFile(String fileName) {
						boolean rslt = false;
	
						try{
	
							Log.i(TAG,"......Beging read File");
	
							BufferedReader br = 	new BufferedReader(	new InputStreamReader(cntx.openFileInput(fileName)));
							Log.i(TAG,"......BufferReader Creado! procedo a leer el file");
	
							int cntr=0;
							String strTmp;
							while ((strTmp=br.readLine()) !=  null) {													  // NOTA: Recorro toso los lines del config.cfg p leer sus parametros.
								strDato =  strTmp;
								cntr++;
								//Log.i(TAG, "Linea config [" + String.valueOf(cntr) + "]" + strTmp );			//nota se asume que el datos guardado en el files es del tipo concanetado: hola~127.0.0.1:99~0000~
							}
							br.close();
							//Log.i(TAG, "Lineas Leidas de config =" + String.valueOf(cntr));
	
							//LA 1ra VEZ q inicio la App, no hay datos grabados en el FileConfig=config.cfg;
							//lo determino x q cntr=0, ahi es donde recien seteo a config.cfg= DEFECTO, las subsigueintes veces
							//si va existir datos en config.cfg.
							if (cntr > 0) {
								rslt=true;
							}else {
								
								if (fileName.equals(var.Config_File))
									strDato = DEFECTO;
								
								rslt=false;
							}
	
						}catch (Exception ex){
							Log.e(TAG, "Error al leer fichero no existe");			// devuelde a traves de su Method getStrData() el valor por DEFECTO.
	
							if (fileName.equals(var.Config_File)) {
								strDato = DEFECTO;								
								Log.i(TAG, "Cargo por Defecto Valores de Configuracion : "  + strDato );
							}
	
							
							rslt=false;
						}
						return (rslt);
	
			}//READ FILE CONFIG




			//open a file if not exist then it create!
			public  boolean openFile(String fileName) {
					try{
						bw =	new OutputStreamWriter(cntx.openFileOutput(fileName, Context.MODE_PRIVATE));	//permite crear el File.
						Log.e(TAG, "File se Creo con exito o se abrio");
						return(true);
	
					}catch (Exception ex){
						Log.e(TAG, "Error al abrir Fichero en memoria interna");
						return (false);
					}
			}



			public boolean closeFile() {
					try{
	
						if (bw != null) {
							bw.close();
							Log.i(TAG, "File Se Cerro.");
							return(true);							//true = si cerro un file.
						}else
							return (false);							//false = no cerro un file.
	
					}catch (Exception ex){
						Log.e(TAG, "Error al Cerrar Fichero en memoria interna");
						return (false);
					}
			}



			//el file config.cfg se crea en: //data/data/com.example.gpstelegram/file/config.cfg
			public  boolean writeFile(String dato) {
	
						try{
							if (bw != null) {
								bw.write(dato);
								Log.i(TAG, "Se Escribio Correctamente en Fichero.");
								return(true);
							}else
								return (false);
	
						}catch (Exception ex){
							Log.e(TAG, "Error al escribir fichero a memoria interna");
							return (false);
						}
			}




			public  boolean deleteFile(String fileName) {
							try{
									cntx.deleteFile(fileName);
									Log.i(TAG, "File Se Borro Exitosamente!.");
									return(true);
	
							}catch (Exception ex){
								Log.e(TAG, "Error al Borrar File, pude ser que no Exista!");
								return (false);
							}
	
			}
	
			
	
			public String getStrData() {
				return(strDato);
			}
		
		
	}// WIRTE CONFIG


