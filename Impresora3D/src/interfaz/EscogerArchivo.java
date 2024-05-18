package interfaz;
import javax.swing.*;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.datatransfer.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class EscogerArchivo extends JFrame{
	
	JLabel label;
	JButton button;

	public EscogerArchivo() {
		setTitle("Escoger archivo STL");
		setSize(400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		button = new JButton("Escoger archivo");

		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Auto-generated method stub
				accionBoton();
			}
		});

		setLayout(new FlowLayout(FlowLayout.CENTER, 20, 80));
		add(button, BorderLayout.CENTER);

	}

	private void accionBoton() {
		boolean seleccionValida = false;
		while (!seleccionValida) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("");
			int seleccion = fileChooser.showOpenDialog(this);

			if (seleccion == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if (file.getAbsolutePath().toLowerCase().endsWith(".stl")) {
					try {
						File pieza = new File("Pieza/pieza.stl");
						Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(pieza.getAbsolutePath()));
						seleccionValida = true;
					} catch (Exception e) {
						// handle exception
						JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
					}
				} else {
					JOptionPane.showMessageDialog(this, "Selecciona un formato STL");
				}
			} else {
				seleccionValida = true;
			}
		}

	}
}
