package main;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface MarkEzd extends Library{
	
	MarkEzd INSTANCE = (MarkEzd) Native.load("MarkEzd", MarkEzd.class);
	
	//EzCadPath = the full path where ezcad2.exe exists
	//bTestMode = Whether in test mode or not
	//hOwenWnd = The window that has the focus. It is used to check the user’s 
	int lmc1_Initial(String EzCadPath, boolean bTestMode, long hOwenWnd);
	int lmc1_Close();
	//bFlyMark enable mark on fly
	int lmc1_Mark(boolean bFlyMark);
	//filePath file location (ezd? file)
	//int lmc1_LoadEzdFile(String filePath);
	//add the appointed file into database
	/*
	  pFileName, //file name
	  pEntName, // name of the file object
	  dPosX, // X coordinate of left-bottom point
	  dPosY, // Y coordinate of left-bottom point
	  dPosZ, // Z coordinate of the file object
	  nAlign, // align way 0－8
	  dRatio, // scaling ratio
	  nPenNo, //the pen number of the file object
	  bHatchFile // hatch the file object or not
	 */
	int lmc1_AddFileToLib(String pFileName, String pEntName, double dPosX, double dPosY, double dPosZ, int nAlign, double dRatio, int nPenNo, boolean bHatchFile);
	/*
	 int nPenNo, // Pen’s NO. (0-255)
	int& nMarkLoop, //mark times
	double& dMarkSpeed, //speed of marking mm/s
	double& dPowerRatio, // power ratio of laser (0-100%)
	double& dCurrent, //current of laser (A)
	int& nFreq, // frequency of laser HZ
	int& nQPulseWidth, //width of Q pulse (us)
	int& nStartTC, // Start delay (us)
	int& nLaserOffTC, //delay before laser off (us)
	int& nEndTC, // marking end delay (us)
	int& nPolyTC, //delay for corner (us)
	double& dJumpSpeed, //speed of jump without laser (mm/s)
	int& nJumpPosTC, //delay about jump position (us)
	int& nJumpDistTC, //delay about the jump distance (us)
	double& dEndComp, //compensate for end (mm)
	double& dAccDist, // distance of speed up (mm)
	double& dPointTime, //delay for point mark (ms)
	BOOL& bPulsePointMode, //pulse for point mark mode
	int& nPulseNum, //the number of pulse
	double& dFlySpeed //speed of production line
	 */
	int lmc1_SetPenParam();
	
	
	
	private String obtenerPathCompletoCarpeta() {
		String salida = "";
		String carpeta = "MakEzd/Ezcad2(20240412)-SDK";
		Path resourcePath = Paths.get(carpeta);
		if (resourcePath.toFile().exists()) {
			salida = resourcePath.toAbsolutePath().toString();
		}
		return salida+"/MarkEzd";
	}
}
/* 
 LMC1_ERR_SUCCESS 0 // Success
 LMC1_ERR_EZCADRUN 1 // Find EZCAD running
 LMC1_ERR_NOFINDCFGFILE 2 // Can not find EZCAD.CFG
 LMC1_ERR_FAILEDOPEN 3 // Open LMC1 board failed
 LMC1_ERR_NODEVICE 4 // Can not find valid lmc1 device
 LMC1_ERR_HARDVER 5 // Lmc1’s version is error.
 LMC1_ERR_DEVCFG 6 // Can not find configuration files
 LMC1_ERR_STOPSIGNAL 7 // Alarm signal
 LMC1_ERR_USERSTOP 8 // User stops
 LMC1_ERR_UNKNOW 9 // Unknown error
 LMC1_ERR_OUTTIME 10 // Overtime
 LMC1_ERR_NOINITIAL 11 // Un-initialized
 LMC1_ERR_READFILE 12 // Read file error
 LMC1_ERR_OWENWNDNULL 13 // Window handle is NULL
 LMC1_ERR_NOFINDFONT 14 // Can not find designated font
 LMC1_ERR_PENNO 15 // Wrong pen number
 LMC1_ERR_NOTTEXT 16 // Object is not text
 LMC1_ERR_SAVEFILE 17 // Save file failed
 LMC1_ERR_NOFINDENT 18 // Can not find designated object
 LMC1_ERR_STATUE 19 // Can not run the operation in current state
*/