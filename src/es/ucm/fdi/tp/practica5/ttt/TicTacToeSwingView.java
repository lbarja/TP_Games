package es.ucm.fdi.tp.practica5.ttt;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.RectBoardSwingView;

/**
 * Clase que define el tablero de juego para Tic Tac Toe
 */
public class TicTacToeSwingView extends RectBoardSwingView{
	
	private static final long serialVersionUID = 1L;

	protected TicTacToeSwingPlayer player;
	private boolean activado;
	
	//Constructora
	public TicTacToeSwingView(Observable<GameObserver> g, Controller c,
			Piece localPiece, Player randPlayer, Player aiPlayer) {
		super(g, c, localPiece, randPlayer, aiPlayer);
		this.setTitle(getTitle()+"Tic Tac Toe");
		if(localPiece != null)
			this.setTitle(getTitle() +"("+ localPiece.getId()+")");
		player = new TicTacToeSwingPlayer();
	}

	@Override
	protected void handleMouseClick(int row, int col, int mouseButton) {
		if(this.activado){
			Piece piece = getTurn();
			player.setMove(row, col,piece);
			decideMakeManualMove(this.player);
		}
	}

	@Override
	protected void activateBoard() {
		this.activado = true;
		
	}

	@Override
	protected void deActivateBoard() {
		this.activado = false;
		
	}

	@Override
	protected void reiniciar() {
		//De momento solo se utiliza en el juego attt
	}

	@Override
	protected void setAtributosNecesarios() {
		//De momento solo se utiliza en el juego attt
	}

	@Override
	protected int getAtributosNecesarios() {
		//De momento solo se utiliza en el juego attt
		return 0;
	}
}
