package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Slic3rManager {

	String slic3rPath = obtenerPathCompleto("Slic3r", "Slic3r-console.exe");
	String config;
	String piezaPath = obtenerPathCompleto("Pieza", "Little_Ghost.stl");
	String salidaPath = obtenerPathCompletoCarpeta("Pieza");

	public void anadirSoportes() {
		ProcessBuilder processBuilder = new ProcessBuilder(slic3rPath);
	}
	
	public String obtenerCapas() throws IOException {
		String salida = "";
		// Ejecutar comando
		String[] exportarCapas = {slic3rPath, "--export-svg", "--output", salidaPath, "--layer-height", "0.2", piezaPath};
		ProcessBuilder processBuilder = new ProcessBuilder(exportarCapas);
		processBuilder.redirectErrorStream(true);
		Process process = processBuilder.start();
		// Leer salida
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		while (bufferedReader.readLine() != null) {
			salida = salida + "\n" + bufferedReader.readLine();
		}
		return salida;
	}

	public String obtenerInfo() throws IOException {
		String salida = "";
		// Ejecutar comando
		String[] infoCMD = { slic3rPath, "--info", "--layer-height", "0.2", piezaPath };
		ProcessBuilder processBuilder = new ProcessBuilder(infoCMD);
		processBuilder.redirectErrorStream(true);
		Process process = processBuilder.start();
		// Leer salida
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String linea;
		while ((linea = bufferedReader.readLine()) != null) {
			salida = salida + "\n" + linea;
		}
		return salida;
	}

	private String obtenerPathCompleto(String carpeta, String nombre) {
		String salida = "";
		Path resourcePath = Paths.get(carpeta, nombre);
		if (resourcePath.toFile().exists()) {
			salida = resourcePath.toAbsolutePath().toString();
		}
		return salida;
	}

	private String obtenerPathCompletoCarpeta(String carpeta) {
		String salida = "";
		Path resourcePath = Paths.get(carpeta);
		if (resourcePath.toFile().exists()) {
			salida = resourcePath.toAbsolutePath().toString();
		}
		return salida;
	}

	private void parsearSize(String entrada) {
		// String[]
	}
}
