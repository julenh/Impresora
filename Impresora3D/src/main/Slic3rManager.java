package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.dom.util.DOMUtilities;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

public class Slic3rManager {

	String slic3rPath = obtenerPathCompleto("Slic3r", "Slic3r-console.exe");
	String config;
	String piezaPath = obtenerPathCompleto("Pieza", "pieza.stl");
	String salidaPath = obtenerPathCompletoCarpeta("Pieza");

	public String anadirSoportes() throws IOException {
		String salida = "";
		String[] exportarConSoportes = {slic3rPath, "--support-material", "--export-stl", piezaPath, "--output", salidaPath};
		ProcessBuilder processBuilder = new ProcessBuilder(exportarConSoportes);
		processBuilder.redirectErrorStream(true);
		Process process = processBuilder.start();
		// Leer salida
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		while (bufferedReader.readLine() != null) {
			salida = salida + "\n" + bufferedReader.readLine();
		}
		return salida;
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

	public boolean obtenerInfo() throws IOException {
		boolean salida = true;
		// Ejecutar comando
		String[] infoCMD = { slic3rPath, "--info", "--layer-height", "0.2", piezaPath };
		ProcessBuilder processBuilder = new ProcessBuilder(infoCMD);
		processBuilder.redirectErrorStream(true);
		Process process = processBuilder.start();
		// Leer salida
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String linea;
		while ((linea = bufferedReader.readLine()) != null) {
			if (linea.contains("size_y") || linea.contains("size_x")) {
				if (Double.parseDouble(linea.split(" = ")[1]) > 10) {
					salida = false;
				}
			} else if (linea.contains("size_z")) {
				if (Double.parseDouble(linea.split(" = ")[1]) > 20) {
					salida = false;
				}
			}
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

	public GestorCapa separarSVG() throws IOException {
		GestorCapa gestorCapa = new GestorCapa();
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
			svgSalida.getDocumentElement().setAttribute( "width", atributos.getNamedItem("width").getNodeValue());
			svgSalida.getDocumentElement().setAttribute( "height", atributos.getNamedItem("height").getNodeValue());
			/**svgSalida.getDocumentElement().setAttribute( "xmlns:slic3r", atributos.getNamedItem("xmlns:slic3r").getNodeValue());
			for (int j = 0; j < atributos.getLength(); j++) {
				Node atributoTemp = atributos.item(j);
				String tempNombre = atributoTemp.getNodeName();
				String tempValor = atributoTemp.getNodeValue();
				if(!existeAtributo(svgSalida, tempNombre)) {
					svgSalida.getDocumentElement().setAttribute( atributoTemp.getNodeName(), atributoTemp.getNodeValue());
				}
			}**/
			// agregar nodo de la capa i al svg de salida i
			Element capaI = (Element) svgSalida.importNode(lista.item(i), true);
			svgSalida.getRootElement().appendChild(capaI);
			// guardar archivo
			String nomSalida = "Capas/capa"+i+".svg";
			escribirSVG(svgSalida, nomSalida);
			Capa capa = new Capa(nomSalida);
			gestorCapa.lista.add(capa);
		}
		return gestorCapa;
	}
	
	private void escribirSVG(SVGDocument entrada, String fileSalida) throws IOException {
		// transformar objeto SVGDocument en texto 
		String cabecera = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n"
				+ "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.0//EN\" \"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">\r\n"
				+ "";
		String contenidoSVG = cabecera + DOMUtilities.getXML(entrada);
		// 
		Files.write(Paths.get(fileSalida), contenidoSVG.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
	}
	private boolean existeAtributo(SVGDocument svgSalida, String atributo) {
		boolean salida = svgSalida.getDocumentElement().hasAttribute(atributo);
		return salida;
	}
}
