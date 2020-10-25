package Logica;
import java.io.*;
import java.util.*;


/**
 * Determina la lógica utilizada para ejecutar un sudoku.
 */
public class LogicaSudoku {

	Celda [][] tablero;
	int cantFilas;
	boolean solucionValida;
		
	/**
	 * Inicializa el tablero de juego sin elementos y la matriz de control de repetidos.
	 */
	public LogicaSudoku() {
		cantFilas = 9;
		tablero = new Celda [cantFilas][cantFilas];
		solucionValida = true;
	}
	
	/**
	 * Da lugar al estado inicial del juego insertando elementos en el tablero.
	 * @param dir Dirección donde se encuentra el archivo de texto del que se obtendrá el estado inicial.
	 */
	public void iniciarJuego() {
		
		InputStream archivo;
		BufferedReader buffer;
		int cont, fila, columna, aux;
		Random random;
		String linea;
		String [] separador;
		
		try {
			archivo = this.getClass().getResourceAsStream("/Recursos sudoku/Estado inicial sudoku.txt");
			buffer = new BufferedReader(new InputStreamReader(archivo));
			
			cont = 0;
			
			//Se lee línea por línea el archivo de texto y se ingresa aleatoriamente cada elemento en el tablero.
			while((linea = buffer.readLine()) != null) {
				separador = linea.split(" ");		
				if(separador.length == cantFilas) {
					for(int j = 0; j < cantFilas; j ++) {
						
						tablero [cont][j] = new Celda();
						
						if(esNumerico(separador [j])) {
							aux = Integer.parseInt(separador[j]);
							if(aux > 0 && aux < 10) {
								tablero [cont][j].cambiarValor(aux);
							}
						}
					}
				}
				cont++;
			}
			if(cont != cantFilas) {
				solucionValida = false;
			}
			
			buffer.close();
			
			if(chequearTablero()) {
				cont = 0;
				random = new Random();
				
				while(cont < 51) {
					fila = random.nextInt(cantFilas);
					columna = random.nextInt(cantFilas);
					if(tablero[fila][columna].getValor() != null) {
						tablero[fila][columna].cambiarValor(null);
						cont++;
					}
				}
			}
			else {
				solucionValida = false;
			}
		}
		catch(IOException e) {
			System.out.print("Error al inicializar el juego.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Inserta un elemento en una celda del tablero.
	 * @param fila Fila en la cual se inserta.
	 * @param columna Columna en la que se inserta.
	 * @param elem Elemento a insertar.
	 */
	public void insertarElem(int fila, int columna, Integer elem) {
		tablero [fila][columna].cambiarValor(elem);
	}
	
	/**
	 * Retorna la celda correspondiente a la fila y columna solicitadas.
	 * @param fila Fila a la cual pertenece la celda.
	 * @param columna Columna a la cual pertenece la celda.
	 * @return Celda correspondiente a la fila y columna solicitadas.
	 */
	public Celda recuperarCelda(int fila, int columna) {
		return tablero [fila][columna];
	}
	
	/**
	 * Comprueba la correctitud de todos los elementos del tablero.
	 * @return True en caso de que no haya ningún elemento rompiendo las reglas del juego, false en caso contrario.
	 */
	public boolean chequearTablero() {
		Map<Integer, List<Integer>> repetidosFilas, repetidosColumnas, repetidosPaneles;
		int fila, columna, panel, aux;
		boolean correcto = true;
		
		fila = 0;
		columna = 0;
		repetidosFilas = new HashMap<Integer, List<Integer>>();
		repetidosColumnas = new HashMap<Integer, List<Integer>>();
		repetidosPaneles = new HashMap<Integer, List<Integer>>();
		
		while(correcto && fila < cantFilas) {
			
			columna = 0;
			while(correcto && columna < cantFilas) {
				
				if(tablero[fila][columna] != null && tablero[fila][columna].getValor() != null) {
					
					aux = tablero[fila][columna].getValor();
					
					//Chequeo de filas.
					if(!repetidosFilas.containsKey(aux)) {
						repetidosFilas.put(aux, new LinkedList<Integer>());
						repetidosFilas.get(aux).add(fila);
					}
					else {
						
						if(repetidosFilas.get(aux).contains(fila)) {
							correcto = false;
						}
					
						repetidosFilas.get(aux).add(fila);
					}
				    
					//Chequeo de columnas.
					if(!repetidosColumnas.containsKey(aux)) {
						repetidosColumnas.put(aux, new LinkedList<Integer>());
						repetidosColumnas.get(aux).add(columna);
					}
					else {
						if(repetidosColumnas.get(aux).contains(columna)) {
							correcto = false;
						}
						repetidosColumnas.get(aux).add(columna);
					}
				
					//Selección de panel correspondiente al elemento.
					if(fila < 3) {
						panel = 1;
					}
					else {
						panel = (fila < 6)? 4 : 7;
					}
					
					if(columna > 2) {
						panel = (columna < 6)? panel + 1 : panel + 2;
					}
					
					//Chequeo de paneles.
					if(!repetidosPaneles.containsKey(aux)) {
						repetidosPaneles.put(aux, new LinkedList<Integer>());
						repetidosPaneles.get(aux).add(panel);
					}
					else {
						if(repetidosPaneles.get(aux).contains(panel)) {
							correcto = false;
						}
						repetidosPaneles.get(aux).add(panel);
					}
				}
				else {
					correcto = false;
				}
				columna++;
			}
			fila++;
		}
		return correcto;
		
	}
	
	/**
	 * Actualiza el estado de marca de las celdas de la fila recibida por parámetro, las de la columna y tambien las del subpanel correspondiente.
	 * En caso de que un elemento este repetido, se le asigna true a la marca de la celda.
	 * Retorna una lista de String cuyos elementos son de la forma "i j" donde i representa la fila de un elemento y j la columna del mismo.
	 * @param fila Fila donde se comprobarán los repetidos.
	 * @param columna Columna donde se comprobarán los repetidos. 
	 * @return Lista de Strings con los elementos de la fila recibida por parámetro, asi como tambien los de la columna y los del subpanel correspondiente.
	 */
	public List<String> getRepetidos(int fila, int columna){
		List<String> repetidos = new LinkedList<String>();
		int filaPanel, columnaPanel;
		Integer valor;
		boolean repite = false;;
		
		valor = tablero[fila][columna].getValor();
		
		if(valor != null) {
			
			//Control en la fila.
			for(int i = 0; i < cantFilas; i++) {
				if(tablero[fila][i].getValor() == valor && i != columna) {
					tablero[fila][i].setMarcado(true);
					repite = true;
				}
				else {
					if(tablero[fila][i].getMarcado()) {
						if(!chequearRepeticion(fila, i)) {
							tablero[fila][i].setMarcado(false);
						}
					}
				}
				repetidos.add(fila +  " " + i);
			}
			
			//Control en columna.
			for(int i = 0; i < cantFilas; i++) {
				if(tablero[i][columna].getValor() == valor && i != fila) {
					tablero[i][columna].setMarcado(true);
					repite = true;
				}
				else {
					if(tablero[i][columna].getMarcado()) {
						if(!chequearRepeticion(i, columna)) {
							tablero[i][columna].setMarcado(false);
						}
					}
					
				}
				repetidos.add(i + " " + columna);
			}
			
			//Se busca el subpanel correspondiente.
			if(fila < 3) {
				filaPanel = 0;
			}
			else {
				filaPanel = (fila < 6)? 3 : 6;
			}
			
			if(columna < 3) {
				columnaPanel = 0;
			}
			else {
				columnaPanel = (columna < 6)? 3 : 6;
			}
			
			//Control de subpanel.
			for(int i = filaPanel; i < filaPanel + 3; i++) {
				for(int j = columnaPanel; j < columnaPanel + 3; j++) {
					if(tablero[i][j].getValor() == valor && i != fila && j != columna) {
						tablero[i][j].setMarcado(true);
						repite = true;
					}
					else {
						if(tablero[i][j].getMarcado()) {
							if(!chequearRepeticion(i, j)) {
								tablero[i][j].setMarcado(false);
							}
						}
					}
					repetidos.add(i + " " + j);
				}
			}
		}
		
		if(repite) {
			tablero[fila][columna].setMarcado(true);
		}
		repetidos.add(fila + " " + columna);
		
		return repetidos;
	}
	
	/**
	 * Metodo privado que realiza una segunda comprobación de que el elemento de la fila y columna recibidas no está rompiendo las reglas del juego.
	 * @param fila Fila del elemento.
	 * @param columna Columna del elemento.
	 * @return True en caso de que el elemento este rompiendo las reglas del juego, false en caso contrario.
	 */
	private boolean chequearRepeticion(int fila, int columna) {
		int filaAux, colAux, filaPanel, colPanel;
		Integer valor;
		boolean repite = false;
		
		filaAux = 0;
		colAux = 0;
		valor = tablero[fila][columna].getValor();
		
		//Chequeo de fila.
		while(!repite && colAux < cantFilas) {
			repite = (tablero[fila][colAux].getValor() == valor && colAux != columna)? true : false;
			colAux++;
		}
		
		//Chequeo de columna.
		while(!repite && filaAux < cantFilas) {
			repite = (tablero[filaAux][columna].getValor() == valor && filaAux != fila)? true : false;
			filaAux++;
		}
		
		if(!repite) {
			
			//Busqueda de panel.
			if(fila < 3) {
				filaAux = 0;
			}
			else {
				filaAux = (fila < 6)? 3 : 6;
			}
			
			if(columna < 3) {
				colAux = 0;
			}
			else {
				colAux = (columna < 6)? 3 : 6;
			}
			
			filaPanel = filaAux;
			colPanel = colAux;
			
			//Chequeo de panel.
			while(!repite &&  filaPanel < filaAux + 3) {
				while(!repite && colPanel < colAux + 3) {
					repite = (tablero[filaPanel][colPanel].getValor() == valor && filaPanel != fila && colPanel != columna)? true : false;
					colPanel++;
				}
				colPanel = colPanel - 3;
				filaPanel++;
			}
		}
		
		return repite;
	}
	
	/**
	 * Retorna la cantidad de filas del tablero.
	 * @return Cantidad de filas del tablero.
	 */
	public int getCantFilas() {
		return cantFilas;
	}
	
	/**
	 * Retorna la correctitud de los elementos del tablero al obtener el estado inicial.
	 * @return True en caso de que el estado inicial sea válido, false en caso contrario.
	 */
	public boolean getCorrectitudInicial() {
		return solucionValida;
	}
	
	/**
	 * Metodo privado que comprueba si String recibido por parámetro es un número.
	 * @param c
	 * @return
	 */
	private boolean esNumerico(String c){
        boolean es;
        try{
            Integer.parseInt(c);
            es = true;
        }
        catch (NumberFormatException nfe){
            es = false;
        }
        return es;
    }
	
}
