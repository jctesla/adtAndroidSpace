package com.example.localize_telegram;

import java.util.Calendar;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class Funcion {
	
	public Funcion() {}
		
	
	
	public static  String get24Hour() {
		Calendar c = Calendar.getInstance();
		String Hr24= String.valueOf(c.get(Calendar.HOUR_OF_DAY)) + ":";
		Hr24=Hr24 + String.valueOf(c.get(Calendar.MINUTE)) + ":";
		Hr24=Hr24 + String.valueOf(c.get(Calendar.SECOND));
		return(Hr24);
	}
	
	
	public static  String getFechaDDMMYYYY() {
		Calendar c = Calendar.getInstance();
		String fecha = String.valueOf(c.get(Calendar.DAY_OF_MONTH)) + "/";
		fecha =fecha + String.valueOf(c.get(Calendar.MONTH) + 1 ) + "/";
		fecha =fecha + String.valueOf(c.get(Calendar.YEAR));
		return (fecha);
	}
	
		//fecha = dd/mm/2019 22:25:10
	public static  String getFecha() {
		Calendar c = Calendar.getInstance();
		String fecha = String.valueOf(c.get(Calendar.DAY_OF_MONTH)) + "-";
		fecha =fecha + String.valueOf(c.get(Calendar.MONTH) + 1 ) + "-";
		fecha =fecha + String.valueOf(c.get(Calendar.YEAR)) + " ";
		fecha =fecha + String.valueOf(c.get(Calendar.HOUR_OF_DAY)) + ":";
		fecha =fecha + String.valueOf(c.get(Calendar.MINUTE)) + ":";
		fecha =fecha + String.valueOf(c.get(Calendar.SECOND));
		return (fecha);
	}
	
	
	public static int[] getHM() {
		int[] arryHM = {0,0};
		Calendar c = Calendar.getInstance();
	
		arryHM[0]= c.get(Calendar.HOUR_OF_DAY);
		arryHM[1]= c.get(Calendar.MINUTE);	

		return arryHM;
	}
	
		
	
	public static  int [] getDMYHMSFecha() {
		int [] arryDMYHMS= {0,0,2017,0,0,0};
		Calendar c = Calendar.getInstance();
		
		arryDMYHMS[0]= c.get(Calendar.DAY_OF_MONTH);
		arryDMYHMS[1]= c.get(Calendar.MONTH) + 1;
		arryDMYHMS[2]= c.get(Calendar.YEAR);
		arryDMYHMS[3]= c.get(Calendar.HOUR_OF_DAY);
		arryDMYHMS[4]= c.get(Calendar.MINUTE);
		arryDMYHMS[5]= c.get(Calendar.SECOND);

		return arryDMYHMS;
	}
	
	
	
	public  static String numDigitos(String strValor,String strfill, int digits) {

		String rsltValor = strValor; 
		//String rsltValor = strValor.substring(0, digits-1);
		//System.out.println("Valor de strValor :" +strValor);
		//System.out.println("Valor de rsltValor :" + rsltVal);
				
		while ( rsltValor.length() < digits){
			 rsltValor = ""  +  rsltValor + strfill; 
		}
		return(rsltValor);
	}

	
	
	//permite parsear datos de un solo Nivel como sintaxis : HOLA|ADIOS|COME|VOY|  .length= 4, este string me da un arreglo  tamaño 4 ,con cualqmuier delimitador. 
    public static String[] SplitSimple(String strDato,String delimiter){
		        int pntI = -1;
		        Vector<String> nodo = new Vector<String>();
		        
		        // Emit LogCat message
				//Log.i(TAG,"SplitSimple dato a parsear : " + strDato);
		
		        //--------------Separamos Los Campos Delimitados-----------------------------
		        while (((pntI = strDato.indexOf(delimiter)) != -1) || strDato.length() > 0){
		        	
				            if (pntI == -1){
				                nodo.addElement(strDato.substring(0));
				                strDato = "";
				            }else{
				                nodo.addElement(strDato.substring(0,pntI));
				                strDato = strDato.substring(pntI + 1);
				                //Log.i(TAG,"dato : "  + strDato);
				            }
		        }
		        
		        //--------------Creo el Arreglo Fijo ya Spliteado----------------------------
		        String [] arryDato = new String[nodo.size()];
		        if (nodo.size() > 0){
		            for (int ii=0; ii < nodo.size(); ii++){
		                arryDato [ii] = (String)nodo.elementAt(ii);
		                //Log.i(TAG,"dato Parseado : "  + arryDato [ii] );
		            }
		        }
		
		        return arryDato;
        
    }
	
    
    //Convierte de String a Integer.
    public static int StrToInt(String txtNum) {
		    int intNum = 0;
		    try {
		    	intNum = Integer.parseInt(txtNum.trim());
		    } catch(Exception nfe) {
		    			Log.i("Class Funcion", "ERR Convrt: " + nfe.toString());
		    			System.out.println("Could not parse " + nfe);
		    			intNum=0;
		    } 
		    return intNum;
    }
    
    
  //Convierte de String a Long.
    public static long StrToLong(String txtNum) {
		    long lngNum = 0;
		    try {
		    	lngNum = Long.parseLong(txtNum.trim());
		    } catch(NumberFormatException nfe) {
		    			System.out.println("Could not parse " + nfe);
		    			lngNum=0;
		    } 
		    return lngNum;
    } 
    
    
    
    //Convierte de String a Long.
    public static double StrToDouble(String txtNum) {
		    double lngNum = 0;
		    try {
		    	lngNum = Double.parseDouble(txtNum.trim());
		    } catch(NumberFormatException nfe) {
		    			System.out.println("Could not parse " + nfe);
		    			lngNum=9999;
		    } 
		    return lngNum;
    } 
    
    //convierte de Integer a String
    public static String intToStr(int num) {
	    String txtNum = "";
	    try {
	          txtNum = Integer.toString(num);
	    } catch(NumberFormatException nfe) {
	    			System.out.println("Could not parse " + nfe);
	    			txtNum="";
	    } 
	    return txtNum;
    }
    
    
	public static boolean setLogo(Context contex) {
			
		((Activity)contex).getActionBar().setDisplayShowHomeEnabled(true);
		((Activity)contex).getActionBar().setIcon(R.drawable.alert_dark_frame);
		((Activity)contex).getActionBar().setDisplayUseLogoEnabled(true);
		return true;
	}
	
	
	
	public static String convertSecondsToHmSs (long seconds) {
		long s= seconds % 60;
		long m = (seconds/60) % 60;
		long h = (seconds / (60 * 60)) %24;
		return String.format("%d:%02d:%02d", h, m ,s);
	}
	
	
	public static String convertMiliSecToHours(long millis) {
		String rsltHMS = String.format("%02d:%02d:%02d", 
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis) -  
				TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
				TimeUnit.MILLISECONDS.toSeconds(millis) - 
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));   
		return (rsltHMS);
	}

	
}



