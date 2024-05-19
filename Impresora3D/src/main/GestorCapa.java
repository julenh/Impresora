package main;

import java.util.ArrayList;

public class GestorCapa {

	ArrayList<Capa> lista;
	
	public GestorCapa() {
		lista = new ArrayList<Capa>();
	}
	
	public void add(Capa capa) {
		lista.add(capa);
	}
}
