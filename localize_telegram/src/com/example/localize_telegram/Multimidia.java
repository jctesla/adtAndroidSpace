package com.example.localize_telegram;

import android.content.Context;
import android.media.MediaPlayer;

public class Multimidia {

	

	//-------------------------------------------------------------------------
	//				Thread SONIDO
	//-------------------------------------------------------------------------

	
	public static void mensajeEnviado(final Context cntx) {
		new Thread(new Runnable() {

			public void run() {
				try {
					//media play definition
					final MediaPlayer beepSound = MediaPlayer.create(cntx, R.raw.mensajeenviado);
					beepSound.start();
					Thread.sleep(1070);
					beepSound.release();
					//Thread.sleep(320);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}).start();
		
	}//fin run
	
	
	
	public static void beepbeepSound(final Context cntx) {
		new Thread(new Runnable() {

			public void run() {
				try {
					//media play definition
					final MediaPlayer beepSound = MediaPlayer.create(cntx, R.raw.beep_beep_01);
					beepSound.start();
					Thread.sleep(320);
					beepSound.release();
					//Thread.sleep(320);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}).start();
		
	}//fin run
	
	
	
	
	public static void cargadorSound(final Context cntx) {
		new Thread(new Runnable() {

			public void run() {
				try {
					//media play definition
					final MediaPlayer beepSound = MediaPlayer.create(cntx, R.raw.cargandowo_01);//sms02
					beepSound.start();
					Thread.sleep(591);
					beepSound.release();
					//Thread.sleep(516);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}).start();
	}
	
	
	
	
	

	
	
}
