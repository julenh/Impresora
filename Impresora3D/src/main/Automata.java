package main;
import com.fazecast.jSerialComm.SerialPort;
import java.io.OutputStream;

public class Automata {
	
	SerialPort puerto;
	
	
	private Automata() {
		puerto = encontrarPuerto();
		if (puerto == null) {
			// lanzar excepcion
		}
	}
	
	private SerialPort encontrarPuerto() {
		SerialPort[] puertos = SerialPort.getCommPorts();
		for (SerialPort puerto : puertos) { 
			if (esPuerto(puerto)) {
				return puerto;
			}
		}
		return null;
	}
	
	private boolean esPuerto(SerialPort puerto) {
		try {
			puerto.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
			puerto.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 1000);
			puerto.openPort();
			
			OutputStream output = puerto.getOutputStream();
			output.write('A');
			
			byte[] buffer = new byte[1];
			puerto.getInputStream().read(buffer);
			
			puerto.closePort();
			
			return (buffer[0] == 'A');
		} catch (Exception e) {
			// handle exception
			return false;
		}
	}
	
	public void enviarS() {
		try {
			puerto.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
			puerto.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 1000);
			puerto.openPort();
			
			OutputStream output = puerto.getOutputStream();
			output.write('H');
			
			puerto.closePort();
		} catch (Exception e) {
			// handle exception
		}
		
	}
}
