package com.example.localize_telegram;


import java.util.TimerTask;



public class OvrTime extends TimerTask {
	
	
	//Constantes del tipo de OverTime
	
    //Si = 0 aun no termina el OverTime sigue esperando respuesta del servidor
    //Si = 1 Termino Exitosamente (si lengthrepondio el servidor
    //Si = -1 el Servidor nunca respondio y se ha generado un OverTime
    public static final int  initPacketData = 0;
    public static final int  ovrTimeSendFrm = 1;
    public static final int  rstWchtdogMltplxr = 2;
    public static final int  sendCallPh=3;
    public static final int  sendMOdGpsOff=4;

    //Indicadores de Comunicacion
    public static final int  WaitOrNotInit = 0;            		//el flag e Over Time indica q aun no se ha inicado el tiempo de espera
    public static final int  AcknowledgeOk = 1;            	//el flag e Over Time indica q antes de finalizar el tiempo de espera ya hay una respuesta.
    public static final int  TmOvrOccurred  = -1;          	//el flag de Over Time indica q el tiempo de espera termino

    private int typeOfRequest = -1;
    public int flgOvrTime = -1;
//    private Context cntx;
 //   private String frmTosave="";
    private int idHead=0;													//es el idHead del Frame que voy almacenar si ocurre un OvrTime en el envio al Servidor UDP, lo almaceno para luego enviarlo de nuevo.
    
   

	    public OvrTime(int typeOfRequest, int idHead ){
	          this.typeOfRequest =  typeOfRequest;
	          //this.cntx=cntx;
	          this.idHead=idHead;
	          //this.frmTosave=frmTosave;
	    }
	

		@Override
		public void run() {
			
				 switch (typeOfRequest){
				
				         case initPacketData:
				                                              System.out.println("....Time Over for Waiting Initialization PacketData initPacketData..."); 
				                                              setFlgOvrTime(TmOvrOccurred);
				                                              break;
				
				                                              
				         case ovrTimeSendFrm:	  //Ocurrio un OvertTime, si el servidor NO envio una confirmacion.	
				                                              //luego de haber esperado respuesta del servidor este no respondio entonces almaceno la data.
				        	 								  //si el idHead=0 no se almacena, solo es un ACK.
				        	 								  switch(idHead) {
						        	 						
  						        	 								    case var.SrvNxtCmdChngIp:		//Este comando es el que espero del ServUDP, si no hay Respuesta del ServUDP
  						        	 								    												//Se genera esta rutina indicando que ubo un Over time de espera.
  						        	 								    												//CanvasSurf.setDebugSys(" + "ServUDO No Responde!" + " " + Funcion.getFecha());

									        	 																		break;
									        	 						
									        	 																		
									        	 																		
									        	 						case var.NxtSrvIdMarcGPSACK:	//ACK de un envio de Marcacion.
									        	 								 										//saveData();
									        	 								 										break;
				        	 								   }
  			                                                   break;
				
  			                                                   
				          case rstWchtdogMltplxr:
				        	  									//---------------- INICIO GPS DEVICE----------------------------------
				                                            	// MltplxrThrd mltplxrThrd = new MltplxrThrd();
				                                             	//mltplxrThrd.start();
				        	  									//timeCnxUdp.cancel();
				                                              	break;
				                                              	
				          case sendCallPh:	setFlgOvrTime(AcknowledgeOk);

				          									//ValiMain.timeWaitCall=null;
				        	  								break;
				        	  								
				          case sendMOdGpsOff:

																//ValiMain.timeWaitCall=null;
							  								break;
				        	  							
		
				 }
	
		}

		
		
		

		public int getFlgOvrTime() {
			return flgOvrTime;
		}
	
	
		public void setFlgOvrTime(int flgOvrTime) {
			this.flgOvrTime = flgOvrTime;
		}

}//fin de clase
