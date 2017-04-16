package es.ucm.fdi.tp.practica6.response;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * clase que implementa de reponse que representa la notificacion de qeu se ha empezado un movimiento
 */
public class MoveStartResponse implements Response {

	private static final long serialVersionUID = 1L;
	
	//Atributos
	private Board board;
	private Piece turn;
	
	/**
	 * constructor con parametro
	 * @param board el tablero que va a necesitar cuando va a notificar
	 * @param turn la ficha del turno que va a necesitar cuando va a notificar
	 */
	public MoveStartResponse(Board board, Piece turn){
		this.board=board;
		this.turn=turn;
	}
	
	@Override
	public void run(GameObserver o) {
		o.onMoveStart(this.board, this.turn);
	}

}
