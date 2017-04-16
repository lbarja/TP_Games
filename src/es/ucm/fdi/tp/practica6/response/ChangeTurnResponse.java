package es.ucm.fdi.tp.practica6.response;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * Clase que implementa de reponse que representa la notificacion de cambio de turno
 */
public class ChangeTurnResponse implements Response {

	private static final long serialVersionUID = 1L;
	
	//Atributos
	private Board board;
	private Piece turn;
	
	/**
	 * Constructor con parametro
	 * @param board el tablero que va a necesitar cuando va a notificar
	 * @param turn la ficha del turno que va a necesitar cuando va a notificar
	 */
	public ChangeTurnResponse(Board board, Piece turn){
		this.board=board;
		this.turn=turn;
	}
	
	@Override
	public void run(GameObserver o) {
		o.onChangeTurn(this.board, this.turn);
	}
}
