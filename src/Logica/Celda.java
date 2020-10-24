package Logica;

/**
 * Modelo de Celda que encapsula un valor y una entidad gr�fica.
 */
public class Celda {

	private Integer valor;
	private EntidadGrafica entidadG;
	private boolean marcado;
	
	/**
	 * Inicializa una celda sin valor.
	 */
	public Celda() {
		valor = null;
		entidadG = new EntidadGrafica();
		marcado = false;
	}
	
	/**
	 * Cambia el valor contenido por la celda actualizando su entidad gr�fica en el proceso. 
	 * @param v Valor que contrendr� la celda.
	 */
	public void cambiarValor(Integer v) {
		if(v != null && v < this.getCantElem() + 1 && v > 0) {
			valor = v;
			entidadG.actualizar(v - 1);
		}
		else {
			valor = null;
		}
	}
	
	/**
	 * Cambia el marcado de la celda.
	 * @param marca Valor booleano que representar� el estado de marca de la celda.
	 */
	public void setMarcado(boolean marca) {
		marcado = marca;
	}
	
	/**
	 * Retorna el valor contenido por la celda.
	 * @return Valor de la celda.
	 */
	public Integer getValor() {
		return valor;
	}
	
	/**
	 * Retorna la entidad gr�fica de la celda.
	 * @return Entidad gr�fica de la celda. 
	 */
	public EntidadGrafica getGrafico() {
		return entidadG;
	}
	
	/**
	 * Retorna el estado de marcado de la celda.
	 * @return True si est� marcada, false en caso contrario.
	 */
	public boolean getMarcado() {
		return marcado;
	}
	
	/**
	 * Retorna la cantidad de imagenes posibles que puede tomar la entidad gr�fica. 
	 * @return cantidad de imagenes de entidad gr�fica.
	 */
	public int getCantElem() {
		return entidadG.getImagenes().length;
	}
	
}
