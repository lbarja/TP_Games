package es.ucm.fdi.tp.practica5.attt;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.RectBoardSwingView;
/**
 * Clase que define el tablero de juego para Advanced Tic Tac Toe
 */
public class AdvancedTTTSwingView extends RectBoardSwingView{

	private static final long serialVersionUID = 1L; 
	private static final int MAXFICHAS = 6;
	private int numberOfPiece;
	private int colOrig;
	private int rowOrig;
	private boolean haveOrig;
	private AdvancedTTTSwingPlayer player;
	private boolean activado;
	private boolean multiVentana;
	
	//Contructora
	public AdvancedTTTSwingView(Observable<GameObserver> g, Controller c,
			Piece localPiece, Player randPlayer, Player aiPlayer) {
		super(g, c, localPiece, randPlayer, aiPlayer);
		this.setTitle(getTitle() + "Advanced Tic Tae Toe ");
		if(localPiece != null){
			this.setTitle(getTitle()+"("+ localPiece.getId()+")");
			this.numberOfPiece = MAXFICHAS/2;
			this.multiVentana=true;
		}
		else{
			this.numberOfPiece = MAXFICHAS;
			this.multiVentana=false;
		}
		
		this.haveOrig=false;
		player = new AdvancedTTTSwingPlayer();
	}
	

	@Override	
	protected void handleMouseClick(int row, int col, int mouseButton) {
		//si el tablero esta activado
		if(activado){
			//si hace click en el boton izquierdo
			if(mouseButton==0){
				//si todavia no ha superado el numero de fichas que puede crear
				if(this.numberOfPiece>0){
					Piece piece = getTurn();
					player.setMove(-1,-1,row,col,piece);
					decideMakeManualMove(this.player);
					this.numberOfPiece--;
					String num;
					if(this.multiVentana)
						num = Integer.toString(this.numberOfPiece);
					else
						num = Integer.toString(this.numberOfPiece/2);
					super.setValueAt(num, getTurn(), 2);
				}
				else{
					if(!this.haveOrig){
						this.colOrig=col;
						this.rowOrig=row;
						this.haveOrig=true;
					}
				    else{
				    	this.haveOrig=false;
						Piece piece=getTurn();
						player.setMove(this.rowOrig, this.colOrig,row,col,piece);
						decideMakeManualMove(this.player);
					}
				}
			}
			//si hace click en el derecho
			else{
				if(row==this.rowOrig && col==this.colOrig){
					this.haveOrig=false;
				}
			}
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
		this.numberOfPiece=MAXFICHAS;
	}


	@Override
	protected void setAtributosNecesarios() {
		this.numberOfPiece--;
	}


	@Override
	protected int getAtributosNecesarios() {
		return this.numberOfPiece;
	}

}
