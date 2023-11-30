package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

public class Slic3rManager {

	String slic3rPath = obtenerPathCompleto("Slic3r", "Slic3r-console.exe");
	String config;
	String piezaPath = obtenerPathCompleto("Pieza", "Little_Ghost.stl");
	String piezaPath2 = obtenerPathCompleto("Pieza", "Little_Ghost.svg");
	String salidaPath = obtenerPathCompletoCarpeta("Pieza");

	public void anadirSoportes() {
		ProcessBuilder processBuilder = new ProcessBuilder(slic3rPath);
	}
	
	public String obtenerCapas() throws IOException {
		String salida = "";
		// Ejecutar comando
		String[] exportarCapas = {slic3rPath, "--export-svg", "--output", salidaPath, "--layer-height", "0.05", piezaPath};
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
	
	public void obtenerNumCapas() {
		int salida;
		String[] numCapas = {slic3rPath, };
		
		//return salida;
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
	public void separarSVG() throws IOException {
		// leer svg exportado de slic3r con todas las capas
		File fileSVG = new File("Pieza/Little_Ghost.svg");
		SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(null);
		SVGDocument svgDoc = factory.createSVGDocument(fileSVG.toURI().toString());
		// obtener longitud y anchura del canvas de las capas
		NamedNodeMap atributos = svgDoc.getDocumentElement().getAttributes();
		String temp = svgDoc.getRootElement().getAttributes().item(0).getNodeName();
		String anchura = svgDoc.getRootElement().getAttribute("width");
		String longitud = svgDoc.getDocumentElement().getAttribute("height");
		// obtener lista de capas 
		NodeList lista = svgDoc.getRootElement().getElementsByTagName("g");
		// crear un svg por cada capa
		for (int i = 0; i < lista.getLength(); i++) {
			Element node = (Element) lista.item(i);
			// crear documento svg
			DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
			String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
			SVGDocument svgSalida = (SVGDocument) impl.createDocument(svgNS, "svg", null);
			// agregar mismos atributos
			for (int j = 0; j < atributos.getLength(); j++) {
				Node atributoTemp = atributos.item(j);
				String temp1 = atributoTemp.getNodeName();
				String temp2 = atributoTemp.getNodeValue();
				svgSalida.getDocumentElement().setAttributeNS(null, atributoTemp.getNodeName(), atributoTemp.getNodeValue());
			}
			String temp3 = svgSalida.getDocumentElement().getAttribute("width");
			// agregar nodo de la capa i al svg de salida i
			Node capaI = lista.item(i);
			//svgSalida.getDocumentElement().appendChild(capaI);
			// guardar archivo
			String nomSalida = "Capas/capa"+i+".svg";
			File fileSalida = new File(nomSalida);
			//OutputStream outputStream = new FileOutputStream(file);
			//Writer writer = new OutputStreamWriter(outputStream, "UTF-8");
			//SVGGraphics2D svgGenerator = new SVGGraphics2D(svgSalida);
			//svgGenerator.stream(new BufferedWriter(writer), true);
			//ImageIO.write(ImageIO.read((File) svgSalida), "svg", file);
			
			escribirSVG(svgSalida, fileSalida);
			
			//OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(new File("Capas/capa"+i+".svg")), "UTF-8");
		}
	}
	
	private void escribirSVG(SVGDocument entrada, File fileSalida) {
		
		
	}
}
