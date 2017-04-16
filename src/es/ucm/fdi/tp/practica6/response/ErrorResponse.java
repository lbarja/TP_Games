package es.ucm.fdi.tp.practica6.response;

import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;

/**
 * Clase que implementa de reponse que representa la notificacion de que se ha fallado algo
 */
public class ErrorResponse implements Response {

	private static final long serialVersionUID = 1L;
	
	//Atributo
	private String msg;
	
	/**
	 * Constructor con parametro
	 * @param msg el mensaje que necesita cuando notifica
	 */
	public ErrorResponse(String msg){
		this.msg=msg;
	}
	
	@Override
	public void run(GameObserver o) {
		o.onError(this.msg);

	}

}
