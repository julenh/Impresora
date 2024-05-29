package test;

import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import main.Slic3rManager;

class Slic3rManagerTest {

	@Test
	void testObtenerInfo() {
		// test
		Slic3rManager test = new Slic3rManager();
		// comprobar salida
		boolean salida;
		try {
			salida = test.obtenerInfo();
			System.out.println(salida);
		} catch (Exception e) {
			// handle exception
			e.printStackTrace();
			fail("Not yet implemented");
		}
	}
	
	@Test
	void testObtenerCapas() {
		// test
		Slic3rManager test = new Slic3rManager();
		// comprobar salida
		String salida;
		try {
			salida = test.obtenerCapas();
			System.out.println(salida);
		} catch (Exception e) {
			// handle exception
			e.printStackTrace();
			fail("Not yet implemented");
		}
	}
	
	@Test
	void testSepararSVG() {
		// test
		Slic3rManager test = new Slic3rManager();
		String salida = "";
		String carpeta = "MakEzd/Ezcad2(20240412)-SDK";
		Path resourcePath = Paths.get(carpeta);
		if (resourcePath.toFile().exists()) {
			salida = resourcePath.toAbsolutePath().toString();
		}
		// comprobar salida
		//String salida;
		try {
			test.separarSVG();
		} catch (Exception e) {
			// handle exception
			e.printStackTrace();
			fail("Not yet implemented");
		}
	}
}
