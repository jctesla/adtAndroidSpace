package com.example.localize_telegram;


public class var {
	// state result GPS Location
	public final static int NOLOC = 0;
	public final static int SILOC = 1;
	public final static int ERRLOC = -1;
	
// comando de SMS
	public final static String START = "#start";
	public final static String STOP = "#stop";
	public final static String RQST_CFG = "#rqconfig";
	public final static String RQST_LOGALL = "#rqlog";
	public final static String RQST_LOGGPS = "#rqgps";
	public final static String COLGADO = "#colgar";
	public final static String CALL_ME = "#callme";
	
	//     TELEGRAM
	public static final String  map = "https://www.google.com/maps/search/?api=1&query=";
	// constantes del TELEGRAM
	public static final String  URL_SERV_TELEGRAM ="https://api.telegram.org/bot";
	public static final String  TOKEN_TELEGRAM = "6249959391:AAGko3-WyPRN6O1FYrRiRloM3ABULR3POaE";
	public static final String  CMND_SEND_TELEGRAM = "/sendMessage";
	public static final String  CMND_GETUPDATE_TELEGRAM = "/getUpdates";
	public static final String  RQST_CHATID_TELEGRAM ="?chat_id=";
	public static final String  CHAT_ID_GRP =	"-940305823";  //miLocalizer_GRP										//"-916049626"; //grp= chat name=miLocalizer_grp							//"-944828942";	//grp chat name=miLocalizer					// "-884531886";  // grp chat name=Test_miTelegram
	public static final String  SEND_TxT_TELEGRAM = "&text=";
	public static final String  MSG_TELEGRAM_INI = "Hola este es un Mensaje de INICIO a Telegram";
	// https://api.telegram.org/bot6249959391:AAGko3-WyPRN6O1FYrRiRloM3ABULR3POaE/sendMessage?chat_id=-884531886&text="Este es 1er Mensaje q envio al Telegram”
	public static final String  URL_SEND_TELEGRAM= URL_SERV_TELEGRAM + TOKEN_TELEGRAM + CMND_SEND_TELEGRAM + RQST_CHATID_TELEGRAM + CHAT_ID_GRP + SEND_TxT_TELEGRAM + "";
	
	//https://api.telegram.org/bot6249959391:AAGko3-WyPRN6O1FYrRiRloM3ABULR3POaE/getUpdates
	public static final String  URL_GetUpdate_TELEGRAM= URL_SERV_TELEGRAM + TOKEN_TELEGRAM + CMND_GETUPDATE_TELEGRAM + "";
	
	
	//Llave y Version del App.
	public static final String VERSION = "Test_miTelegram ver=1";	//a la vez q srive de Version Sirve de Llave de sincronizacion entre el ServUDP y la App.
	public static final String MIC_KEY = "MIC";			//a la vez q srive de Version Sirve de Llave de sincronizacion entre el ServUDP y la App.
		
	//Read Body of Datagram Socket
	public static final String DTGRM_DATA = "1";				//Es el cuerpo del Dato que recivimos en la clase ReceiveSendUdp
	public static final String DTGRM_REMIPADDRSS = "2";	//Es la Ip Address del equipo Remoto de la clase ReceiveSendUdp

	
	//variales flg de pantalla canvas.
	public final static int cvInicial=0;									//cuando aun no hay estados.
	public final static int cvIniConfig=1;								//al cargar la app carga la configuracion x 1era vez
	public final static int cvTrhdMainConfig=2;					//cuando ingresas a la configuracion desde la app
	public final static int cvTrhdMainAlone=3;						//cuando la app ha sido interrumpida desde afuera.
		
	
	
	//Tipo Device Root
	public final static int DeviceSiRootedSiRst=1;
	public final static int DeviceNoRootedSiRst=0;
	public final static int DeviceNoRootNoRst=-1;
	
	//Para la rutina de Marcacion.
	public static final int  esCorteRuta = 0;
	public static final int  esPntControl= 1;
	public static final int  esTerminal = 2;
	public static final int headMarc = 1;					//es el tipo de idHead que va contener o contiene el Tbl_datlogger para diferenciar el tipo de frame que se quiere almacenar/leer.
	public static final int headLocation = 2;
	
	//XY de los Botones Canvas de Central y Mecanica.
	public final static int X1_BtnArriba=0;
	public final static int Y1_BtnArriba=1;
	public final static int X2_BtnArriba=2;
	public final static int Y2_BtnArriba=3;
	
	public final static int X1_BtnAbajo=4;
	public final static int Y1_BtnAbajo=5;
	public final static int X2_BtnAbajo=6;
	public final static int Y2_BtnAbajo=7;
	
	public final static int X1_BtnRuta=8;
	public final static int Y1_BtnRuta=9;
	public final static int X2_BtnRuta=10;
	public final static int Y2_BtnRuta=11;
		
	public final static int X1_BtnTeclado=12;
	public final static int Y1_BtnTeclado=13;
	public final static int X2_BtnTeclado=14;
	public final static int Y2_BtnTeclado=15;
	
	public final static int X1_BtnCentral=16;
	public final static int Y1_BtnCentral=17;
	public final static int X2_BtnCentral=18;
	public final static int Y2_BtnCentral=19;
	
	public final static int X1_BtnApaga=20;
	public final static int Y1_BtnApaga=21;
	public final static int X2_BtnApaga=22;
	public final static int Y2_BtnApaga=23;
	
	public final static int X1_BtnSi=24;
	public final static int Y1_BtnSi=25;
	public final static int X2_BtnSi=26;
	public final static int Y2_BtnSi=27;
	
	public final static int X1_BtnNo=28;
	public final static int Y1_BtnNo=29;
	public final static int X2_BtnNo=30;
	public final static int Y2_BtnNo=31;
	

	
	///NOTA :  ORIENTACION.- X=Columnas Y=Filas.
	public final static int SIZE_PRGPTN=29;					//tamaño de las letras de la Hoja de Programcion de los Puntos de CONTROL
	public final static int SIZE_TITLE=20;					//antes 25 , tamaño de las letras del subtitulo. MsjPersonal,MsjGeneral,strDatero,InfoRuta,InfoUnidad.
	public final static int BIG_LETTER=90;					//tamaño de la Hora y Velocidad.
	public final static int SIZE_VEL=60;						//tamaño de la Etiqueta "Vel: "
	public final static int SIZE_DEBUG_TITTLE=18;		//tamaño de la letra del sistema de debugger.
	public final static int SIZE_DATA=23;					//tamaño de los mensajes de datos que llegan a cada parametro.(MsjPersonal, MsjGeneral, strDatero, InfoRuta, InfoUnidad)
	public final static int CELL_01Y=64;						//coordenada Y  para iniciar dibujo de la letra de Hora y Velocidad de abajo arriba.
	public final static int SIZE_DEBUG_DATA=14;		//coordenada Y  para iniciar dibujo de la letra de Hora y Velocidad de abajo arriba.
	
	public final static int LF = 26;								//line feed .- sirve para dar un reglon adicional al crear una nueva linea de texto en pantalla.
	public final static int mLF = 26;							//line feed del Modod Programacion de Puntos de Control , p la lista delos puntos de control
	//public final static int LF_TITULO = 24;					//line feed .- sirve para dar un reglon adicional al crear una nueva linea de texto titulo en pantalla.
	//public final static int LF_DATA = 26;						//line feed .- sirve para dar un reglon adicional al crear una nueva linea de texto datos en pantalla.
	public final static int CF =7; 								//column feed  orientacion X=Columnas Y=Filas. cada vez que se haga un return a la linea vertical principal amarilla.
	public final static int BIH=3;								//border inicial horizontal Letras.
	public final static int boxX = 5,boxY=5; 				//inicio Rectangulo de color =  Box.
	
	public final static int LINE_Y = 70; 						//posicion de espacio entre lineas principales amarillas del eje X.
	public final static int offset_Y = 115; 					//se suma al LINE_Y para las subsiguientes lineas principales amarillas despues de la primera.(lineas principales son las 1ers q se dibijanantes de todo)
	
	
	public final static int LINES_DEBUG=9; 				//Nro de lineas de 0 al 9 son 10 lineas que va tener el DebusSys
	
	public final static int LINES_DATERO=4; 				//Nro de lineas que va tener el DebusSys}
	public final static int ADLNT_01=0; 						//Valor del dato del de adelante 01 (el mas cercano)
	public final static int ADLNT_02=1; 						//Valor del dato del de adelante 02 (despues del adelante 01)
	public final static int ADLNT_03=2; 						//Valor del dato del de adelante 03 (despues del adelante 02)
	public final static int ADLNT_04=3; 						//Valor del dato del de adelante 04 (despues del adelante 03)
	
	public final static int ATRAS_01=0; 						//Valor del dato del de ATRAS (el mas cercano)
	public final static int ATRAS_02=1; 						//Valor del dato del de ATRAS 02 (despues del de Atras 01)
	public final static int ATRAS_03=2; 						//Valor del dato del de ATRAS 03 (despues del de Atras 02)
	public final static int ATRAS_04=3; 						//Valor del dato del de ATRAS 04 (despues del de Atras 03)
	
	public static final int BUFFER_SIZE_LOGIN =20;	//buffer que se separa para una descarga de datos.
	public static final int BUFFER_SIZE_ENVIAR = 100;	//buffer que se separa para una descarga de datos.
	
	
	//---------------RESP ERR SERV HTTP-----------
	public static final int  rsptSrvHttp_OK = 1;					//Resultado de una coneccion al servidor respuesta Bien.
	public static final int  rsptSrvHttp_Bad = 0;					//Resultado de una coneccion al servidor respuesta Bien.
	public static final int  rsptSrvCnxn_Err = -1;				//Resultado de una coneccion al servidor respuesta Bien.
	
	public static final int HdFisrttime_Http= 1;					//cuando cambia la IP del equipo o x 1era vez se conecta al Servidor. 
	public static final int HdNxtTime_Http= 2;			 		//HEAD= 02 actualizo mi IpBus y el leo el Ip actual del ServerUdp.
	public static final int HdNxtTime_Datero= 59;			 	//HEAD= 59 actualizo mi IpBus y el leo el Ip actual del ServerUdp.
	public static final String  NxtSrvIdAllMarc = "8" ;               //HEAD= 8 Id Head que indica que se esta enviando al servHttp una trama con todas las marcaciones de los puntos de control del sentido que corresponda.
	public static final String  SrvNxtIdAllMarc = "8" ;             	//Ack del Servidor que recivio bien la Marc Id Head de Marcacion de la Trama del device GPS de SalmaGVCO
    public static final int  NxtSrvIdMarcGPS = 9 ;             //HEAD = 9 Id Head de Marcacion de la Trama del device GPS de valiGVCO sin ACK.
    public static final String SrvNxtIdMarcGPS = "9" ;             //Ack del Servidor que recivio bien la Marc Id Head de Marcacion de la Trama del device GPS de SalmaGVCO
    
	
	
	public static final String CONFIG_RSLT = "INSPECTOR";		//indica que se esta trabajando con los datos de la Clase de Configuracion.
  
	//usados en la Lectura/Escritura del file de la Class FileConfig
	//----CONFIGURACION---> los datos deben garbarse y leerse en ese orden estrictamente del 0 al 11.
	//"http servidor=01" + "~" + "1200" + "~" + "udp servidor=02" + "~" + "channle=1" + "~" + "tracking=13" + "~" + "ip local=1.1.1.1" + "~" + "time1down=0" + "~" + "code/name= Chupi" + "~" + "Tlf_Reporte=999999999" + "~" + "sentido=A" + "~" +  "nota=" + var.VERSION  + "~" + "000000" + "~";
	public static final int nReg =6;						//son 11 registros 0..11 : IpServer1,nEnvioHTTP,Ipserver2,ChannelSrv,TrackingUDP,...
	public static final int codeUser =0;				//1er registro Ipserver del Server HTTP..puede ser una IP o un DOMINIO
	public static final int descripcion =1;			//2do cada cuantos segundos envio los datos de localizacion via HTTP.
	public static final int urlHttp=2;				//3er registro Ip del Server UDP.
	public static final int tracking =3;				//4er Channel, el USB al cual esta conectado al ser UDP
	public static final int reTry =4;			//5er registro Tracking para el GPS.
	public static final int claveCfg =5;					//6to IpLocal del Equipo antes era registro envia pulsos al micro externo p indicar actividad.
	
	
	public static final String FileSentido ="sentido.cfg";						//Nombre del Archvo de Zonasy Punto de referecnia para determinar el Sentido del Vehiculo.
	public static final String Config_File ="config.cfg";							//Nombre del Archvo de Configuracion.
	public static final String FileMAvion ="modoavion.cfg";							//Nombre del Archvo que almacena los eventos del Modo Avion.
	public static final String delimitDat ="|";										//Delimitador para Campos de un Registro.
	public static final String delimitReg ="~";										//Delimitador para Registros.
	
	public static final String dbName ="DBValiGVCO";						//Nombre de Base de datos.
	public static final String tblName ="Tbl_Location";
	public static final int numFiledTbl_PntControl = 9;						//Nro de Campos de la Tbl_PntControl son 10 campos menos 1 de idItem(autogenerado) y empieza en 0...9
	
	public static final int RESULT_SQLQUERY = 0;							//Solicitud a un Query.
	
	
	public static final int USBSERIAL_BUFF = 30;								//Solicitud a un Query.
	
	
	public static final int  chngSignalStregth= 1;								//Cuando hay un cambio en la Signal Strenght este flag indica eso
	public static final String keySs= "keySs";										//llave usada para el tipo de datos del handler del SignalStregth
	
	
    //-------------------Constante de Tiempo--------------------
    public static final int  timetoRstHndSet = 45000;   						 //15 minutos de auto Reset antes de que el equipo se apague
    public static final int  timeAuditDawas = 6000;      						//determina cada cuanto tiempo el HandSet lee la informacion almacenada del DAWAS, el tiempo no debe exceder del calculo sgnte : (35/NoBuses) x 1seg x 256, es decir si Leeo 35 Buses :. no debo exceder de 4,26 minutos por que si no el contador de nLinked se vuelve al 0. 
    public static final int  maxTimeWaitForSendFrame = 5000;       //tiempo de espera antes de un overTime de envio de datos al Servidor
    public static final int  numTryToSendToServer = 1;   					//Es el numero de reintentos que el Equipo va intentar trasnmitir la misma informacion antes de almacenarla en la Ram y luego en la Flash 
    
	
	
	//-----------ENCABEZADOS DE TRAMA-----------------
    //Identificador de este proyecto
	public static final int  SrvNxtControl1 =252 ;        //valor qiue va antes de cualquier grupo de trama ,identifica una Trama del Proyecto vALI g.vco
	public static final int  SrvNxtControl2 =  253 ; 
	public static byte [] headSinc = {(byte) (252 & 255), (byte)(253 & 255)};
	public static byte [] preambuloSinc = {(byte) (252 & 255), (byte)(250 & 255)};
	
	//Sincronizacion de datos
    public static final int  sincSalma = 170;                   //primer byte de sincronismo de SalmaGVCO con el Nextel
    public static final String endFrm = "" + (char)124;  //| este digito solo se usa en GPS  por que en la Trama sabemos con seguridad que no hay caracteres que no sean ascii (texto), es decor Numero y Letras, no hay numeros binarios o fuera del rango de un Numero y letra Ascii
    public static final String splt = "" + (char)126;        //~
	
  //--------------Orden de algunos elementos de la  Tbl_PntControl en memoria---------------------
    //idStation[0],latLocation[1],lonLocation[2],areaLat[3],areaLon[4]
    public static final int  IdStation =0;     				//Identificar de Trama de Senalizacion del Nextel al Servidor
    public static final int  nameStation =1;     		//Identificar de Trama de Senalizacion del Nextel al Servidor
    public static final int  LatLocation =2;
    public static final int  LonLocation =3;
    public static final int  AreaLat =4;
    public static final int  AreaLon =5;
    public static final int  altura =6;
    public static final int  typeOfPoint =7;
    public static final int  arriveTime =8;
    public static final int  marcTime =9;
    public static final int  volada =10;
    
  //--------------Orden de algunos elementos de la  Tbl_Datalogger en memoria---------------------
  //buffIdaVuelta[0..4] =  idOrden[0] , idHead[1] , detalleA/B[2] , frame[3] , fecha[4]
    public static final int  idOrden =0;     				//Identificar de Trama de Senalizacion del Nextel al Servidor
    public static final int  idHead =1;     		//Identificar de Trama de Senalizacion del Nextel al Servidor
    public static final int  sentidoAB =2;
    public static final int  frmMarc =3;
    public static final int  fecMarc =4;
    
    
    
 //--------------Orden de la Tbl_Sentido en memoria buffer---------------------
      //idZrPr[0],zrLatLoc[1],zrLonLoc[2],zrAreaLat[3],zrAreaLon[4],prLatLoc[5],prLonLoc[6] 
      public static final int  idZrPr =0;     				//Identificar de Trama de Senalizacion del Nextel al Servidor
      public static final int  zrLatLocation =1;     		//Identificar de Trama de Senalizacion del Nextel al Servidor
      public static final int  zrLonLocation =2;
      public static final int  zrAreaLat =3;
      public static final int  zrAreaLon =4;
      public static final int  prLatLocation =5;
      public static final int  prLonLocation =6;
      public static final int  distMinimaZrPr =7;
    
    //-----------------Puntos de Control-------------------------------------------
    public static final int  nroPntsCntrl = 16 ;							//la 10E tiene 16 incluidos Terminales. 14 + 2// de subida y Bajda.
    
	//------------------------------------------------------------ COMANDOS ------------------------------------------------------------------------------------------------------------------------

    public static final int  NxtSrvIdHeadGPS = 10 ;             //Id Head de Location la Trama del device GPS de SalmaGVCO
    public static final int  SrvNxtOnOfScrn = 13 ;             //Activa desactiva la Pantalla de Debugger de letras Verdes para que no se True=activo.
    public static final int  NxtSvrOnOfScrn = 13 ;             //
    
    public static final int SrvNxtPwrConnect = 14;             //Power Connect
    public static final int NxtSrvPwrConnect = 14;             //Power Connect
    
    public static final int SrvNxtPwrDisConnect = 15;            //Id head Power Disconnect
    public static final int NxtSrvPwrDisConnect = 15;            //Id head Power Disconnect
    
    public static final int  SrvNxtDateroAdlnt = 16;            //Byte de Head del Servidor al HandSet, Indica que los comandos que lleguen del Servidor al HandSet se dirigen directos al Mltplxr.
    public static final int  NxtSrvDateroAdlnt = 16;            //rspta
    
    public static final int  NxtSrvDateroAtras = 17;            //Byte de Head del Servidor al HandSet, Indica que los comandos que lleguen del Servidor al HandSet se dirigen directos al Mltplxr.       
    public static final int  SrvNxtDateroAtras = 17;            //rspta
    
    public static final int SrvNxtModoAvion = 18 ;             //Id head Modo Avion.
    public static final int NxtSrvModoAvion = 18 ;             //respta
    public static final int NxtSrvIdHeadTrafico = 19 ;             //Id head respuesta de trafico.
   public static final int  SrvNxtIdHeadTrafico = 19 ;             //Id head indica el trafico que le envia el servidor cuando recibe una marcacion. 
   public static final int  NxtSrvIdHeadSignal = 20;     		//Identificar de Trama de Senalizacion del Nextel al Servidor
   public static final int  SrvNxtCmdErsrDtlggr = 21;          //Borra el Datalogger del HandSet
   public static final int  NxtSrvAckErsrDtlggr = 21;
   public static final int  SrvNxtCmdPshDwn = 22;     			//Comando que Obliga al HandSet a Bajar la App.
   public static final int  NxtSrvAckPshDwn = 22;
   public static final int  SrvNxtCmdChngIp = 23;               //Comando para cambiar el Numero de IP que esta en el HandSert hacia el Servidor que piensa Atenderlo.
   public static final int  NxtSrvCmdChngIp = 23;
   public static final int  SrvNxtCmdAppWkUp = 24;       	//Comando que Obliga al App del HandSet a Activarse o Levantarse.
   public static final int  NxtSrvAckdAppWkUp = 24;
   public static final int  SrvNxtCmdRqstSgnlQ = 25;
   public static final int  NxtSrvAckSgnlQ = 25;     				//Comando para preguntar al HandSet(Modem) su Nivel de Senal de Antena.
   public static final int  SrvNxtLevelBat = 26;          	//Comando para preguntar al HandSet(Modem) su Nivel de Bateria.
   public static final int  NxtSrvLevelBat = 26;
   public static final int SrvNxtCmdRqstAllDtloggr = 27;
   public static final int NxtSrvCmdRqstAllDtloggr = 27;
   public static final int SrvNxtCmdAck = 28;
   public static final int NxtSrvAckCmd = 28;
   public static final int  SrvNxtMsjVali = 29;             			//Byte de Head del Servidor al HandSet, Indica que los comandos que lleguen del Servidor al HandSet se dirigen directos al Mltplxr.
   public static final int  NxtSrvMsjVali = 29;             			//Byte de Head del Servidor al HandSet, Indica que los comandos que lleguen del Servidor al HandSet se dirigen directos al Mltplxr.
   
   
   public static final int  SrvNxtReset = 30;            				//ByteOrden para reseteaer todo el sistema
   public static final int  NxtSrvReset = 30;            				//confirmacion de reset.
   public static final int  SrvNxtMsjGrpVali = 31;            		//Byte de Head del Servidor al HandSet, Indica que los comandos que lleguen del Servidor al HandSet se dirigen directos al Mltplxr.
   public static final int  NxtSrvMsjGrpValii = 31;           		//Byte de Head del Servidor al HandSet, Indica que los comandos que lleguen del Servidor al HandSet se dirigen directos al Mltplxr.   
   public static final int  SrvNxtUpLoadWeb = 32;          		//Roden del Servidor para que el equipo Mavege a la Pagina web por nuevos datos.
   public static final int  NxtSvrUpLoadWeb = 32;          		//Respuesta de confirmacion
   public static final int  SrvNxtMarcVali = 33;             			//Comando de Marcacion del Punto de control
   public static final int  NxtSrvMarcVali = 33;             			//Respuesta de comando de Marcacion del Pnto de Control.
   public static final int  SrvNxtDespacho = 34;             		//Permite enviar los 2 carros atras / 2 carros adelante de despacho.
   public static final int  NxtSrvDespacho = 34;             		//Respuesta de comando de Marcacion del Pnto de Control.
   public static final int  SrvNxtRdConfig = 35;             			//Permite leer la configuracion actual del equipo.
   public static final int  NxtSrvRdConfig = 35;             			//Respuesta de comando de leer la configuracion actual del equipo
   public static final int  SrvNxtWrConfig = 36;             			//Permite Escribir una nueva configuracion al equipo.
   public static final int  NxtSrvWrConfig = 36;             			//Respuesta de comando de Escribir una nueva configuracion al equipo.
   //public static final int  SrvNxtSetScrnTimeOut = 37;     		//permite colocar la pantalla en OFF en el tiempo que se le especifique en Milisegundos.
   //public static final int  NxtSrvSetScrnTimeOut = 37;    			//Respuesta de confirmacion de comando Set Screen time Off.
   public static final int  SrvNxtSetProxPnt = 38;     				//permite setear el Nombre y Hora:Minuto del proximo Punto de Control.
   public static final int  NxtSrvSetProxPnt = 38;    				//Respuesta de setear el Nombre y Hora:Minuto del proximo Punto de Control.
   public static final int  SvrNxtNewPntCntrl = 39;    			//Inserta un nuevo registro de punto de control en la Tbl_PntControl. de DBVali.db local.
   public static final int  NxtSvrNewPntCntrl = 39;    			//Respuesta de confirmacion de comando Set Screen time Off.
   public static final int  SvrNxtRdPntCntrl = 40;    				//Inserta un nuevo registro de punto de control en la Tbl_PntControl. de DBVali.db local.
   public static final int  NxtSvrRdPntCntrl = 40;    				//Respuesta de confirmacion de comando Set Screen time Off.
   public static final int  SvrNxtModificaPntCntrl = 41;    		//Inserta un nuevo registro de punto de control en la Tbl_PntControl. de DBVali.db local.
   public static final int  NxtSvrModificaPntCntrl = 41;    		//Respuesta de confirmacion de comando Set Screen time Off.
   public static final int  SvrNxtEraserPntCntrl = 42;    		//Inserta un nuevo registro de punto de control en la Tbl_PntControl. de DBVali.db local.
   public static final int  NxtSvrEraserPntCntrl = 42;    		//Respuesta de confirmacion de comando Set Screen time Off.
   public static final int  SvrNxtRdDatalogger = 43;    			//Read All datalogger de la Tbl_Datalogger de DBVali.db local.
   public static final int  NxtSvrRdDatalogger = 43;    			//
   public static final int  SvrNxtEraseDatalogger = 44;    		//Borra todo el  Tbl_Datalogger. de DBVali.db local.
   public static final int  NxtSvrEraseDatalogger = 44;    		//
   public static final int  SvrNxtSizeDatalogger = 45;    		//Rquest Size of Datalogger
   public static final int  NxtSvrSizeDatalogger = 45;    		//
   public static final int  SvrNxtAppDown = 46;    					//Orden para bajar la App
   public static final int  NxtSvrAppDown = 46;    					//confirmacion de orden
   
   public static final int SvrNxtRqstTcldo = 48;    				//Orden Solicitud de llamda a Teclado
   public static final int NxtSvrRqstTcldo= 48;    					//confirmacion de orden
   public static final int SvrNxtRqstCntrl = 47;    					//Orden Solicitud de llamda a Central
   public static final int NxtSvrRqstCntrl = 47;    					//confirmacion de orden
   public static final int SvrNxtRqstBtnPwr = 49;    				//Señal que el ususario toco el boton de encendido/apagado del equipo, (boton plateado).
   public static final int NxtSvrRqstBtnPwr = 49;    				//confirmacion de orden
   
      
   
   //---------- HEADs con ACK -------------------------------
   public static final int  NxtSrvIdMarcGPSACK = 49 ;             //Id Head de Marcacion de la Trama con ACK.
   public static final int  SrvNxtIdMarcGPSACK = 49 ;             //Id Head de Marcacion de la Trama con ACK.
   public static final int  SrvNxtModFieldPntCntrl= 50 ;     		//Permite modificar la Hoja de Programacion de ruta de los puntos de control, Tbl_pntControl, como la Programacion de llegada de los PntsCntrl
   public static final int  NxtSrvModFieldPntCntrl= 50 ;
   
   public static final int  SrvNxtPrgRuta = 51 ;							//igual q el comando 50 pero es solo para programacion de ruta.
   public static final int  NxtSrvPrgRuta = 51 ;
   
   public static final int  SrvNxtApagaScr = 52 ;						//Señal de Orden de Apagar Pantalla = true pero si =false cancela orden de apagar pantalla
   public static final int  NxtSrvApagaScr = 52 ;
   public static final int  SrvNxtBtnRsptSi = 53 ;						//Señal que se presiono una Respuesta SI
   public static final int  NxtSrvBtnRsptSi = 53 ;
   public static final int  SrvNxtBtnRsptNo = 54 ;						//Señal que se presiono una Respuesta NO
   public static final int  NxtSrvBtnRsptNo = 54 ;
   
   public static final int  SrvNxtHightBrilloScr = 55 ;					//Orden de Señal para encender la pantalla
   public static final int  NxtSrvHightBrilloScr = 55 ;  
   public static final int  SrvNxtLowBrilloScr = 56 ;					//Orden de Señal para bajar/apagar el brillo/pantalla del equipo de consumo de energia
   public static final int  NxtSrvLowBrilloScr = 56 ; 
   public static final int  SrvNxSMSReceive = 57 ;					//Orden de Señal para copiar el sms que haya llegado al smart
   public static final int  NxtSrvSMSReceive = 57 ; 
   
   public static final int  SvrNxtModZonaRefSentido = 58;				//permite cambiar las zonas de refeecnia que permiten cambiar los sentidos automaticos del GPS.
   public static final int  NxtSvrModZonaRefSentido = 58;
   

   public static final int  SvrNxtDateroFrm = 59;
   public static final int  NxtSvrDateroFrm = 59;
   public static final int  SrvNxtMsjDelVeh = 60 ;							//esun idSignal no idHead
   public static final int  NxtSrvMsjDelVeh = 60 ;
   
   public static final int  SrvNxtCallTlf = 61 ;							//esun idSignal no idHead
   public static final int  NxtSrvCallTlf = 61 ;								//codigo de que ha llamado un nro de telfo.

   
   public static final int  SvrNxtCambioSentido = 62;				//cambia el Sentidoi en la Unidad de A a B o B a A.
   public static final int  NxtSvrCambioSentido = 62;
   
   public static final int  SvrNxtCambioClave = 63;				//cambia de clave de acceso en la config
   public static final int  NxtSvrCambioClave = 63;

   public static final int  SrvNxtRdPrgRuta = 64 ;							//Leer la Hoja de Ruta Actual del Sentido A o B
   public static final int  NxtSrvRdPrgRuta = 64 ;
   
   public static final int  SrvNxtModGpsOff = 65 ;							//Identifica cuando desactivan el Modulo de GPS.
   public static final int  NxtSrvModGpsOff  = 65 ;						
   
  public static final String HeadVali  =  String.valueOf((byte)SrvNxtControl1 & 255) +  String.valueOf((byte)SrvNxtControl2 & 255);
  //public static final String HeadVali  =  "" + SrvNxtControl1 +  SrvNxtControl2;
   //public static final String HeadVali  = (char)SrvNxtControl1 + (char)SrvNxtControl2; 
   //public static final String HeadVali  = new String(headSinc,"utf-16");
  public static final int VOICE_RECOGNITION_REQUEST_CODE=1;

   
   
   
}
