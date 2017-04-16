package es.ucm.fdi.tp.practica6.response;

import java.util.List;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;


/**
 * Clase que implementa de reponse que representa la notificacion de que se ha iniciado un juego
 */
public class GameStartResponse implements Response {

	private static final long serialVersionUID = 1L;
		
	//Atributos
	private Board board;
	private String gameDesc;
	private List<Piece> pieces;
	private Piece turn;
	
	/**
	 * Constructor con parametro
	 * @param board el tablero que va a necesitar cuando va a necesitar cuando notifica
	 * @param gameDesc el string que describe el juego que va a necesitar cuando notifica
	 * @param pieces lista de piezaas que va a necesitar cuando va notificar 
	 * @param turn la pieza del que le toca jugar  
	 */
	public GameStartResponse(Board board,String gameDesc, List<Piece> pieces,
			Piece turn) {
		this.board= board;
		this.gameDesc= gameDesc;
		this.pieces= pieces;
		this.turn= turn;
	}
	
	@Override
	public void run(GameObserver o) {
		o.onGameStart(board, gameDesc, pieces, turn);
	}

}
