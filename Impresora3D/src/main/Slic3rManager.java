package main;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;
import org.xml.sax.XMLReader;

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
		File fileSVG = new File("Pieza/pieza.svg");
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
		NodeList nodos = svgDoc.getRootElement().getChildNodes();
		for (int j = 0; j < nodos.getLength(); j++) {
			String a = nodos.item(j).getBaseURI();
		}
		for (int i = 0; i < lista.getLength(); i++) {
			Element node = (Element) lista.item(i);
			// crear documento svg
			DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
			String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
			SVGDocument svgSalida = (SVGDocument) impl.createDocument(svgNS, "svg", null);
			
			DocumentType doctype = impl.createDocumentType("svg", "-//W3C//DTD SVG 1.0//EN", "http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd");
            //svgSalida.insertBefore(doctype, svgSalida.getDocumentElement());

	        Element svgRoot = svgSalida.getDocumentElement();
	        svgRoot.setAttributeNS(null, "contentScriptType", "text/ecmascript");
	        svgRoot.setAttributeNS(null, "width", anchura);
	        //svgRoot.setAttributeNS(null, "xmlns:xlink", "http://www.w3.org/1999/xlink");
	        svgRoot.setAttributeNS(null, "zoomAndPan", "magnify");
	        svgRoot.setAttributeNS(null, "contentStyleType", "text/css");
	        svgRoot.setAttributeNS(null, "height", longitud);
	        svgRoot.setAttributeNS(null, "preserveAspectRatio", "xMidYMid meet");
	        svgRoot.setAttributeNS(null, "version", "1.0");

	        Element gElement = svgSalida.createElement("g");

			// agregar mismos atributos
			gElement.setAttribute( "width", anchura);
			gElement.setAttribute( "height", longitud);
			
			Element capaI = (Element) svgSalida.importNode(lista.item(i), true);
			gElement.appendChild(capaI);
			
			svgRoot.appendChild(gElement);
			// guardar archivo
			
			String nomSalida = "Capas/capa"+i+".png";
			escribirPNG(svgSalida, nomSalida);
			Capa capa = new Capa(nomSalida);
			gestorCapa.lista.add(capa);
		}
		return gestorCapa;
	}
	
	private void escribirPNG(SVGDocument entrada, String fileSalida) throws IOException {
		// transformar objeto SVGDocument en texto 
		/*String contenidoSVG = DOMUtilities.getXML(entrada);
		DOMUtilities.getPrefix(contenidoSVG);
		Files.write(Paths.get(fileSalida), contenidoSVG.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		*/
		Transcoder transcoder = new PNGTranscoder();
		TranscoderInput input = new TranscoderInput(entrada);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		FileOutputStream fos;
		try {
			TranscoderOutput output = new TranscoderOutput(outputStream);
            transcoder.transcode(input, output);
            fos = new FileOutputStream(fileSalida);
            fos.write(outputStream.toByteArray());
		} catch (Exception e) {
			// handle exception
			e.printStackTrace();
			
		}
		
	}
	private boolean existeAtributo(SVGDocument svgSalida, String atributo) {
		boolean salida = svgSalida.getDocumentElement().hasAttribute(atributo);
		return salida;
	}
}
