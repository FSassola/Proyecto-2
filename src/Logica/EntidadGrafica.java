package Logica;

import javax.swing.*;

/**
 * Representa una entidad gr�fica que encapsula la imagen de una celda.
 */
public class EntidadGrafica {

	private ImageIcon imagenActual;
	private String [] imagenes;
	
	/**
	 * Inicializa una entidad gr�fica sin imagen.
	 */
	public EntidadGrafica() {
		imagenActual = new ImageIcon();
		imagenes = new String [9];
		for(int i = 0; i < 9; i++) {
			imagenes[i] = "/Recursos sudoku/" + (i + 1) + ".png";
		}
	}
	
	/**
	 * Actualiza la imagen de la entidad.
	 * @param indice indice de la imagen a cambiar.
	 */
	public void actualizar(int indice) {
		if(indice < imagenes.length) {
			ImageIcon aux = new ImageIcon(this.getClass().getResource(imagenes[indice]));
			imagenActual.setImage(aux.getImage());
		}
	}
	
	/**
	 * Retorna la imagen de la entidad.
	 * @return Imagen contenida en la entidad gr�fica.
	 */
	public ImageIcon getImagenActual() {
		return imagenActual;
	}
	
	/**
	 * Retorna un arreglo de String que representa la ruta de las imagenes entre las que puede cambiar la entidad.
	 * @return Arreglo de String con las imagenes de la entidad gr�fica.
	 */
	public String [] getImagenes() {
		return imagenes;
	}
	
	/**
	 * Cambia las imagenes entre las cuales puede cambiar la entidad.
	 * @param imgs Arreglo de String que representar� las imagenes de la entidad gr�fica.
	 */
	public void setImagenes(String [] imgs) {
		imagenes = imgs;
	}
	
}
