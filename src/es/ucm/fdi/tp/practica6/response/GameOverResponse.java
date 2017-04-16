package es.ucm.fdi.tp.practica6.response;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;

/**
 * Clase que implementa de reponse que representa la notificacion de se ha terminado un juego
 */
public class GameOverResponse implements Response {

	private static final long serialVersionUID = 1L;
	
	//Atributos
	private Board board;
	private State state;
	private Piece winner;
	
	/**
	 * Constructora
	 * @param board el tablero que necesita cuando va a notificar
	 * @param state el estado que va a ncesitar cuando va a notificar
	 * @param winner la pieza que represeta el jugador 
	 */
	public GameOverResponse(Board board, State state, Piece winner){
		this.board=board;
		this.state=state;
		this.winner=winner;
	}

	@Override
	public void run(GameObserver o) {
		o.onGameOver(this.board, this.state, this.winner);
	}

}
