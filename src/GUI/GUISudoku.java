package GUI;

import Logica.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.Duration;
import java.util.*;

import javax.swing.*;
import java.util.Timer;

public class GUISudoku extends JFrame {

		private JPanel panelTablero, panelSeleccion, panelTiempo;
		private JToggleButton [][] celdas;
		private JButton [] opciones;
		private JButton iniciar, reiniciar, pausa, comprobar;
		private JLabel imagenPausa;
		private ButtonGroup grupo;
		private SpringLayout spring;
		private Container contenedor;
		
		private LogicaSudoku logica;
		private Cronometro cronometro;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUISudoku frame = new GUISudoku(new LogicaSudoku());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUISudoku(LogicaSudoku l) {
		super("Sudoku");
		logica = l;
		contenedor = getContentPane();
		spring = new SpringLayout();
		contenedor.setLayout(spring);
		setSize(1010, 700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		GuiSudoku();
	}
	
	private void GuiSudoku() {
		int subPanel;
		String color = "#344861";
		String [] imgOpciones;
		JPanel panelAux;
		
		//Arma el panel del tablero.
		panelTablero = new JPanel();
		panelTablero.setLayout(new GridLayout(3, 3));
		panelTablero.setBorder(BorderFactory.createLineBorder(Color.decode(color)));
		panelTablero.setBackground(Color.decode(color));
		
		celdas = new JToggleButton [logica.getCantFilas()][logica.getCantFilas()];
		grupo = new ButtonGroup();
					
		//Crea los paneles del tablero.
		for(int i = 0; i < 9; i++) {
			panelAux = new JPanel();
			panelAux.setLayout(new GridLayout(3, 3));
			panelAux.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.decode(color)), BorderFactory.createLineBorder(Color.decode("#BEC6D4"))));
			panelAux.setBackground(Color.WHITE);
			panelTablero.add(panelAux);
		}
		
		//Crea las celdas del tablero.
		for(int i = 0; i < logica.getCantFilas(); i++) {
			for(int j = 0; j < logica.getCantFilas(); j++) {
				
				celdas[i][j] = new JToggleButton();
				grupo.add(celdas[i][j]);
				
				//Se crea y asigna un oyente de teclado para la celda.
				celdas[i][j].addKeyListener(new OyenteTecla());
				celdas[i][j].setActionCommand(i + " " + j);
				
				celdas[i][j].setBackground(Color.WHITE);
				celdas[i][j].setBorder(BorderFactory.createLineBorder((Color.decode("#BEC6D4"))));
				celdas[i][j].setEnabled(false);
				
				if(i < 3) {
					subPanel = 0;
				}
				else {
					subPanel = (i < 6)? 3 : 6;
				}
				
				if(j > 2) {
					subPanel = (j < 6)? subPanel + 1 : subPanel + 2;
				}
				
				panelAux = (JPanel) panelTablero.getComponent(subPanel);
				panelAux.add(celdas[i][j]);
			}
		}
		
		//Arma el panel donde se encontrarán los objetos para completar el tablero.
		panelSeleccion = new JPanel();
		panelSeleccion.setLayout(new GridLayout(3, 3));
		panelSeleccion.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.decode(color), 2), BorderFactory.createLineBorder(Color.decode("#BEC6D4"))));
		panelSeleccion.setBackground(Color.decode(color));
		
		//Crea las etiquetas con las imagenes de los objetos.
		imgOpciones = new String [9];
		for(int i = 0; i < 9; i++) {
			imgOpciones[i] = "/Recursos sudoku/" + (i + 1) + ".png";
		}
		
		opciones = new JButton [9];
		for(int i = 0; i < 9; i++) {
			opciones[i] = new JButton(new ImageIcon(this.getClass().getResource(imgOpciones[i])));
			panelSeleccion.add(opciones[i]);
			
			opciones[i].addActionListener(new OyenteOpcion());
			opciones[i].addKeyListener(new OyenteTecla());
			
			opciones[i].setActionCommand("" + i);
			
			opciones[i].setBorder(BorderFactory.createLineBorder(Color.decode("#BEC6D4")));
			opciones[i].setBackground(Color.WHITE);
		}
		
		//Crea el boton iniciar.
		iniciar = new JButton("Nueva partida");
		iniciar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.decode(color)), BorderFactory.createLineBorder(Color.decode("#BEC6D4"))));
		iniciar.setBackground(Color.WHITE);
		
		Font font = new Font("Brittanic Bold", Font.BOLD, 20);
		iniciar.setFont(font);
		iniciar.setForeground(Color.decode(color));
		
		iniciar.addActionListener(new OyenteIniciar());
		
		//Crea el boton reiniciar.
		reiniciar = new JButton("Reiniciar");
		reiniciar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.decode(color)), BorderFactory.createLineBorder(Color.decode("#BEC6D4"))));
		reiniciar.setBackground(Color.WHITE);
		
		reiniciar.setFont(font);
		reiniciar.setForeground(Color.decode(color));
		
		reiniciar.addActionListener(new OyenteReinicio());
		reiniciar.setEnabled(false);
		
		
		//Crea el panel del cronometro.
		panelTiempo = new JPanel();
		panelTiempo.setLayout(new GridLayout(1, 8));
		panelTiempo.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.decode(color)), BorderFactory.createLineBorder(Color.decode("#BEC6D4"))));
		panelTiempo.setBackground(Color.WHITE);
		JLabel [] labelDigitos = new JLabel [8];
		
		for(int i = 0; i < 8; i++) {
			labelDigitos [i] = new JLabel();
			panelTiempo.add(labelDigitos [i]);
		}
		
		cronometro = new Cronometro(labelDigitos);
		
		
		//Crea el boton de pausa.
		pausa = new JButton(new ImageIcon(this.getClass().getResource("/Recursos sudoku/pausa.png")));
		pausa.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.decode(color)), BorderFactory.createLineBorder(Color.decode("#BEC6D4"))));
		pausa.setBackground(Color.WHITE);
		
		pausa.addActionListener(new OyentePausa());
		pausa.setEnabled(false);
		imagenPausa = new JLabel(new ImageIcon(this.getClass().getResource("/Recursos sudoku/pausa.gif")));
		
		//Crea el boton comprobar.
		comprobar = new JButton("Comprobar solución");
		comprobar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.decode(color)), BorderFactory.createLineBorder(Color.decode("#BEC6D4"))));
		comprobar.setBackground(Color.WHITE);
		
		comprobar.setFont(font);
		comprobar.setForeground(Color.decode(color));
		
		comprobar.addActionListener(new OyenteComprobar());
		comprobar.setEnabled(false);
		
		//Crea un label con la palabra "sudoku".
		JLabel cartelSudoku = new JLabel(new ImageIcon(this.getClass().getResource("/Recursos sudoku/titulo.png")));
		
		//Ordena el Layout principal.
		contenedor.add(panelTablero);
		contenedor.add(panelSeleccion);
		contenedor.add(iniciar);
		contenedor.add(reiniciar);
		contenedor.add(panelTiempo);
		contenedor.add(pausa);
		contenedor.add(imagenPausa);
		contenedor.add(comprobar);
		contenedor.add(cartelSudoku);
		
		contenedor.setBackground(Color.decode("#FAF4F2"));
		
		spring.putConstraint(SpringLayout.NORTH, panelTablero, 25, SpringLayout.NORTH, contenedor);
		spring.putConstraint(SpringLayout.WEST, panelTablero, 25, SpringLayout.WEST, contenedor);
		
		spring.putConstraint(SpringLayout.NORTH, panelSeleccion, 362, SpringLayout.NORTH, contenedor);
		spring.putConstraint(SpringLayout.EAST, contenedor, 25, SpringLayout.EAST, panelSeleccion);
		
		spring.putConstraint(SpringLayout.WEST, panelSeleccion, 15, SpringLayout.EAST, panelTablero);
		spring.putConstraint(SpringLayout.SOUTH, panelSeleccion, 0, SpringLayout.SOUTH, panelTablero);
		spring.putConstraint(SpringLayout.WEST, iniciar, 15, SpringLayout.EAST, panelTablero);
		
		spring.putConstraint(SpringLayout.NORTH, iniciar, 25, SpringLayout.NORTH, contenedor);
		spring.putConstraint(SpringLayout.EAST, iniciar, -25, SpringLayout.EAST, contenedor);
		
		spring.putConstraint(SpringLayout.NORTH, reiniciar, 10, SpringLayout.SOUTH, iniciar);
		spring.putConstraint(SpringLayout.EAST, reiniciar, -25, SpringLayout.EAST, contenedor);
		spring.putConstraint(SpringLayout.WEST, reiniciar, 15, SpringLayout.EAST, panelTablero);
		
		spring.putConstraint(SpringLayout.NORTH, panelTiempo, 10, SpringLayout.SOUTH, reiniciar);
		spring.putConstraint(SpringLayout.WEST, panelTiempo, 15, SpringLayout.EAST, panelTablero);
		
		spring.putConstraint(SpringLayout.EAST, pausa, -25, SpringLayout.EAST, contenedor);
		spring.putConstraint(SpringLayout.NORTH, pausa, 10, SpringLayout.SOUTH, reiniciar);
		spring.putConstraint(SpringLayout.EAST, panelTiempo, -5, SpringLayout.WEST, pausa);
		spring.putConstraint(SpringLayout.SOUTH, pausa, 0, SpringLayout.SOUTH, panelTiempo);
		
		spring.putConstraint(SpringLayout.NORTH, imagenPausa, 25, SpringLayout.NORTH, contenedor);
		spring.putConstraint(SpringLayout.SOUTH, contenedor, 25, SpringLayout.SOUTH, imagenPausa);
		spring.putConstraint(SpringLayout.WEST, imagenPausa, 25, SpringLayout.WEST, contenedor);
		spring.putConstraint(SpringLayout.EAST, imagenPausa, 0, SpringLayout.EAST, panelTablero);
		spring.putConstraint(SpringLayout.SOUTH, panelTablero, 0, SpringLayout.SOUTH, imagenPausa);
		
		spring.putConstraint(SpringLayout.NORTH, comprobar, 10, SpringLayout.SOUTH, panelTiempo);
		spring.putConstraint(SpringLayout.WEST, comprobar, 15, SpringLayout.EAST, panelTablero);
		spring.putConstraint(SpringLayout.EAST, comprobar, -84, SpringLayout.EAST, contenedor);
		
		spring.putConstraint(SpringLayout.NORTH, cartelSudoku, 20, SpringLayout.SOUTH, comprobar);
		spring.putConstraint(SpringLayout.WEST, cartelSudoku, 15, SpringLayout.EAST, panelTablero);
		spring.putConstraint(SpringLayout.EAST, cartelSudoku, -25, SpringLayout.EAST, contenedor);
		
		
		panelTablero.setSize(550, 550);
		panelTablero.setPreferredSize(panelTablero.getSize());

		panelTiempo.setSize(210, 70);
		panelTiempo.setPreferredSize(panelTiempo.getSize());
		
		imagenPausa.setSize(550, 550);
		imagenPausa.setPreferredSize(imagenPausa.getSize());
		
		comprobar.setSize(210, 44);
		comprobar.setPreferredSize(comprobar.getSize());
		
		
	}
	
	//Declaración de oyentes.
	
	/**
	 * Oyente para los botones del panel donde se encuentran los objetos a insertar en el tablero.
	 */
	private class OyenteOpcion implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			int fila, columna, num;
			boolean repite;
			String [] separador;
			ImageIcon imagen;
			LinkedList<String> lista;
			
			if(grupo.getSelection() != null) {
				
				//Se recuperan la fila y columna de la celda donde se ingresará el elemento.
				num = Integer.parseInt(e.getActionCommand());
				separador = grupo.getSelection().getActionCommand().split(" ");
				fila = Integer.parseInt(separador[0]);
				columna = Integer.parseInt(separador[1]);
				
				//Se inserta el elemento en el tablero y se cambia el icono de la celda.
				logica.insertarElem(fila, columna, num + 1);
				
				imagen = new ImageIcon();
				imagen.setImage(logica.recuperarCelda(fila, columna).getGrafico().getImagenActual().getImage());
				celdas[fila][columna].setIcon(imagen);
				
				//Marcado de los elementos repetidos luego de ingresar un elemento al tablero.
				lista = (LinkedList<String>) logica.getRepetidos(fila, columna);
				while(!lista.isEmpty()) {
					separador = lista.remove().split(" ");
					fila = Integer.parseInt(separador[0]);
					columna = Integer.parseInt(separador[1]);
					repite = logica.recuperarCelda(fila, columna).getMarcado();
					
					if(repite) {
						celdas[fila][columna].setBackground(Color.decode("#FC6148"));
						celdas[fila][columna].setBorder(BorderFactory.createLineBorder(Color.decode("#FF2300")));
					}
					else {
						celdas[fila][columna].setBackground(Color.WHITE);
						celdas[fila][columna].setBorder(BorderFactory.createLineBorder(Color.decode("#BEC6D4")));
					}
				}
			}
		}
	}
		
	/**
	 * Oyente para el boton que da inicio a una partida nueva.
	 */
	private class OyenteIniciar implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			Celda c;
			
			logica.iniciarJuego();
			if(logica.getCorrectitudInicial()) {
				
				grupo.clearSelection();
				cronometro.resetear();
				cronometro.iniciar();
				pausa.setIcon(new ImageIcon(this.getClass().getResource("/Recursos sudoku/pausa.png")));
				
				panelTablero.setVisible(true);
				reiniciar.setEnabled(true);
				pausa.setEnabled(true);
				comprobar.setEnabled(true);
				
				for(int i = 0; i < celdas.length; i++) {
					for(int j = 0; j < celdas[0].length; j++) {
						c = logica.recuperarCelda(i, j);
						celdas[i][j].setBackground(Color.WHITE);
						celdas[i][j].setBorder(BorderFactory.createLineBorder(Color.decode("#BEC6D4")));
						if(c.getValor() != null) {
							celdas[i][j].setIcon(c.getGrafico().getImagenActual());
							celdas[i][j].setDisabledIcon(c.getGrafico().getImagenActual());
							celdas[i][j].setEnabled(false);
						}
						else {
							celdas[i][j].setIcon(null);
							celdas[i][j].setDisabledIcon(c.getGrafico().getImagenActual());
							celdas[i][j].setEnabled(true);
						}
					}
				}
			}
			else {
                JOptionPane dialog = new JOptionPane();
                dialog.showMessageDialog(new JFrame(),"El estado inicial del tablero es inválido.","ERROR",JOptionPane.ERROR_MESSAGE);
                System.exit(0);
			}
		}
		
	}
	
	/**
	 * Oyente para el boton que reinicia el estado inicial de la partida en curso.
	 */
	private class OyenteReinicio implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			for(int i = 0; i < celdas.length; i ++) {
				for(int j = 0; j < celdas[0].length; j++) {
					if(celdas[i][j].isEnabled()) {
						logica.insertarElem(i, j, null);
						celdas[i][j].setIcon(null);
						
					}
					celdas[i][j].setBackground(Color.WHITE);
					celdas[i][j].setBorder(BorderFactory.createLineBorder(Color.decode("#BEC6D4")));
				}
			}
			grupo.clearSelection();
			cronometro.resetear();
			cronometro.iniciar();
			panelTablero.setVisible(true);
			pausa.setIcon(new ImageIcon(this.getClass().getResource("/Recursos sudoku/pausa.png")));
		}
		
	}
	
	/**
	 * Oyente para el boton que pausa/reanuda la ejecución del juego.
	 */
	private class OyentePausa implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			if(panelTablero.isVisible()) {
				panelTablero.setVisible(false);
				comprobar.setEnabled(false);
				cronometro.pausa();
				grupo.clearSelection();
				
				pausa.setIcon(new ImageIcon(this.getClass().getResource("/Recursos sudoku/play.png")));
			}
			else {
				panelTablero.setVisible(true);
				comprobar.setEnabled(true);
				cronometro.iniciar();
				
				pausa.setIcon(new ImageIcon(this.getClass().getResource("/Recursos sudoku/pausa.png")));
			}
		}
	}
	
	/**
	 * Oyente para el boton que comprueba la solución del tablero.
	 */
	private class OyenteComprobar implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {

			JOptionPane dialog = new JOptionPane();
			
			if(logica.chequearTablero()) {
				
				grupo.clearSelection();
				for(int i = 0; i < logica.getCantFilas(); i++) {
					for(int j = 0; j < logica.getCantFilas(); j++) {
						celdas[i][j].setBackground(Color.decode("#2EEB1C"));
						celdas[i][j].setEnabled(false);
					}
				}
				cronometro.pausa();
				pausa.setEnabled(false);
				reiniciar.setEnabled(false);
				comprobar.setEnabled(false);
				ImageIcon imagen = new ImageIcon(this.getClass().getResource("/Recursos sudoku/victoria.gif"));
	            dialog.showOptionDialog(new JFrame(), imagen, "FELICIDADES", JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
			}
			else {
				cronometro.pausa();
				dialog.showMessageDialog(new JFrame(), "La solución actual del tablero es incorrecta o está incompleta.", "SOLUCIÓN INCORRECTA", JOptionPane.ERROR_MESSAGE);
				cronometro.iniciar();
			}
			
		}
		
	}
	
	/**
	 * Oyente de teclado para las celdas del tablero. Permite ingresar los elementos con presionar su número correspondiente y seleccionar las celdas
	 * cercanas a la activa con las flechas.
	 */
	private class OyenteTecla implements KeyListener{

		public void keyTyped(KeyEvent e) {
		}

		public void keyPressed(KeyEvent e) {
			boolean encontre = false;
			int fila, columna, opcion, cont = 0;
			int [] tecla = {KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5, KeyEvent.VK_6, KeyEvent.VK_7, KeyEvent.VK_8,
					KeyEvent.VK_9, KeyEvent.VK_NUMPAD1, KeyEvent.VK_NUMPAD2, KeyEvent.VK_NUMPAD3, KeyEvent.VK_NUMPAD4, KeyEvent.VK_NUMPAD5, 
					KeyEvent.VK_NUMPAD6, KeyEvent.VK_NUMPAD7, KeyEvent.VK_NUMPAD8, KeyEvent.VK_NUMPAD9};
			
			int [] flecha = {KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_KP_LEFT, KeyEvent.VK_KP_RIGHT, 
					KeyEvent.VK_KP_UP, KeyEvent.VK_KP_DOWN};
			
			if(grupo.getSelection() != null) {
				
				while(!encontre && cont < 9) {
					if(e.getKeyCode() == tecla[cont] || e.getKeyCode() == tecla[cont + 9]) {
						opciones[cont].doClick();
						encontre = true;
					}
					cont++;
				}
				
				if(!encontre) {
					
					String [] separador = grupo.getSelection().getActionCommand().split(" ");
					fila = Integer.parseInt(separador[0]);
					columna = Integer.parseInt(separador[1]);
					
					//Procedimiento flecha izquierda.
					if(e.getKeyCode() == flecha[0] || e.getKeyCode() == flecha[4]) {
						opcion = columna - 1;
						while(opcion > 0 && !celdas[fila][opcion].isEnabled()) {
							opcion--;
						}
						if(opcion >= 0) {
							celdas[fila][opcion].doClick();
						}
					}
					
					//Procedimiento flecha derecha.
					if(e.getKeyCode() == flecha[1] || e.getKeyCode() == flecha[5]) {
						opcion = columna + 1;
						while(opcion < celdas[0].length && !celdas[fila][opcion].isEnabled()) {
							opcion++;
						}
						if(opcion <= celdas[0].length - 1) {
							celdas[fila][opcion].doClick();
						}
					}
					
					//Procedimiento flecha arriba.
					if(e.getKeyCode() == flecha[2] || e.getKeyCode() == flecha[6]) {
						opcion = fila - 1;
						while(opcion > 0 && !celdas[opcion][columna].isEnabled()) {
							opcion--;
						}
						if(opcion >= 0) {
							celdas[opcion][columna].doClick();
						}
					}
					
					//Procedimiento flecha abajo.
					if(e.getKeyCode() == flecha[3] || e.getKeyCode() == flecha[7]) {
						opcion = fila + 1;
						while(opcion < celdas.length - 1 && !celdas[opcion][columna].isEnabled()) {
							opcion++;
						}
						if(opcion <= celdas.length - 1) {
							celdas[opcion][columna].doClick();
						}
					}
				}
			}
		}

		public void keyReleased(KeyEvent e) {
		}
	}
	
	/**
	 * Clase encargada de manejar el tiempo del cronometro y cambiar sus digitos según corresponda.
	 */
	private class Cronometro {
		
		private Timer crono;
		private ImageIcon [] img;
		private JLabel [] labels;
		private boolean activo;
		private long pausado, tiempoAnterior;
		
		public Cronometro(JLabel [] l) {
			crono = new Timer();
			activo = false;
			pausado = 0;
			labels = l;
			img = new ImageIcon [10];
			
			for(int i = 0; i < 10; i++) {
				img [i] = new ImageIcon(this.getClass().getResource(("/Recursos sudoku/" + i + ".png")));
			}
			
			for(int i = 0; i < labels.length; i++) {
				if(i != 2 && i != 5) {
					labels [i].setIcon(img[0]);
					reDimensionar(labels [i], img[0]);
				}
				else {
					ImageIcon imagen = new ImageIcon(this.getClass().getResource("/Recursos sudoku/dosPuntos.png"));
					labels [i].setIcon(imagen);
					reDimensionar(labels[i], imagen);
				}
			}
		}
		
		/**
		 * Inicia el cronometro.
		 */
		public void iniciar() {
			
			if(activo) {
				crono.cancel();
				crono = new Timer();
			}
			
			TimerTask tarea = new TimerTask() {	
				
				long start = System.currentTimeMillis();
				
				public void run() {
					int hora, minutos, segundos;
					
					long stop = System.currentTimeMillis();
					Duration transcurrido = Duration.ofMillis((stop + pausado) - start);
					tiempoAnterior = transcurrido.toMillis();
					
					segundos = transcurrido.toSecondsPart();
					minutos = transcurrido.toMinutesPart();
					hora = (int) transcurrido.toHours();
					
					//Cambia labels de segundos.
					if(segundos % 10 == 0) {
						labels[6].setIcon(img[segundos/10]);
						reDimensionar(labels [6], img[segundos/10]);
					}
					
					labels[7].setIcon(img[segundos%10]);
					reDimensionar(labels [7], img[segundos%10]);
					
					//Cambia labels de minutos.
					if(segundos == 0) {
						if(minutos % 10 == 0) {
							labels[3].setIcon(img[minutos/10]);
							reDimensionar(labels [3], img[minutos/10]);
						}
						
						labels[4].setIcon(img[minutos%10]);
						reDimensionar(labels [4], img[minutos%10]);
					}
					
					//Cambia labels de horas.
					if(segundos == 0 && minutos == 0) {
						if(hora % 10 == 0) {
							labels[0].setIcon(img[hora/10]);
							reDimensionar(labels [0], img[hora/10]);
						}
						
						labels[1].setIcon(img[hora%10]);
						reDimensionar(labels [1], img[hora%10]);
					}
					
					if(hora == 99) {
						start = System.currentTimeMillis();
					}
				}
			};
			
			crono.schedule(tarea, 0, 1000);
			activo = true;
		}
		
		/**
		 * Pausa la ejecución del cronometro y guarda el tiempo transcurrido.
		 */
		public void pausa() {
			pausado = tiempoAnterior;
			crono.cancel();
			crono = new Timer();
			activo = false;
		}
		
		/**
		 * Resetea el tiempo transcurrido del cronometro para comenzar su ejecución desde 0.
		 */
		public void resetear() {
			pausado = 0;
		}
	}
	
	private void reDimensionar(JLabel label, ImageIcon grafico) {
		Image image = grafico.getImage();
		if (image != null) {  
			Image newimg = image.getScaledInstance(22, 52,  java.awt.Image.SCALE_SMOOTH);
			grafico.setImage(newimg);
			label.repaint();
		}
	}
}
