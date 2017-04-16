package es.ucm.fdi.tp.practica5.ataxx;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.RectBoardSwingView;

/**
 * Clase que define el tablero de juego para Ataxx
 */
public class AtaxxSwingView extends RectBoardSwingView{

	private static final long serialVersionUID = 1L;
	
	//Atributos
	private int colOrig;
	private int rowOrig;
	private boolean haveOrig;
	private AtaxxSwingPlayer player;
	private boolean activado;
	
	//Constructora
	public AtaxxSwingView(Observable<GameObserver> g, Controller c,
			Piece localPiece, Player randPlayer, Player aiPlayer) {
		super(g, c, localPiece, randPlayer, aiPlayer);
		this.setTitle(getTitle()+"Ataxx");
		if(localPiece != null)
			this.setTitle(getTitle() +"("+ localPiece.getId()+")");
		this.haveOrig=false;
		player = new AtaxxSwingPlayer();
	}

	@Override
	protected void handleMouseClick(int row, int col, int mouseButton) {
		if(activado){
			if(mouseButton==0){
				if(!this.haveOrig){
					this.colOrig=col;
					this.rowOrig=row;
					this.haveOrig=true;
				}else{
					this.haveOrig=false;
					Piece piece=getTurn();
					player.setMove(this.rowOrig, this.colOrig,row,col,piece);
					decideMakeManualMove(this.player);
				}
			}
			else
				this.haveOrig=false;
		}
	}

	@Override
	protected void activateBoard() {
		this.activado=true;
	}

	@Override
	protected void deActivateBoard() {
		this.activado=false;
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
