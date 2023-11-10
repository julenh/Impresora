package test;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import main.Slic3rManager;

class Slic3rManagerTest {

	@Test
	void testObtenerPathCompleto() {
		// test
		Slic3rManager test = new Slic3rManager();
		// comprobar salida
		String salida;
		try {
			salida = test.obtenerInfo();
			System.out.println(salida);
		} catch (Exception e) {
			// handle exception
			e.printStackTrace();
			fail("Not yet implemented");
		}
	}
	
}
