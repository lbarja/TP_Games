package es.ucm.fdi.tp.practica6.response;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * clase que implementa de reponse que representa la notificacion de se ha terminado un moviemiento
 */
public class MoveEndResponse implements Response {

	private static final long serialVersionUID = 1L;
	
	//Atributos
	private Board board;
	private Piece turn;
	private boolean success;
	
	/**
	 * constructor con parametro
	 * @param board el tablero que necesita cuando va a notificar 
	 * @param turn la ficha del turno que va necesitar cuando va a notificar 
	 * @param success el booleano qye va a necesitar cuando va  a notificra
	 */
	public MoveEndResponse(Board board, Piece turn, boolean success){
		this.board=board;
		this.turn=turn;
		this.success=success;
	}
	
	@Override
	public void run(GameObserver o) {
		o.onMoveEnd(this.board, this.turn, this.success);
	}

}
