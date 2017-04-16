package es.ucm.fdi.tp.practica6.response;

import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;


/**
 * El interfaz response
 * que representa la notificacion y el gameserver se encarga de crearlo y lo envia a todos los clientes.
 */
public interface Response extends java.io.Serializable{
	
	/**
	 * Metodo encargado de llamar correspondiente notificacion de observador
	 * @param o observador que va a necistar cuando notificamos 
	 */
	public void run(GameObserver o);
}
