package main;

import javax.swing.SwingUtilities;

import interfaz.EscogerArchivo;

public class Impresora {

	public static void main(String[] args) {
		

		
		
	}
	
	private void cargarPieza() {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// Auto-generated method stub
				new EscogerArchivo().setVisible(true);
			}
		});
	}
	
	private void imprimirPieza() {}

	private void cargarParametros() {}
}
