package es.ucm.fdi.tp.practica5.ataxx;
import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica4.ataxx.AtaxxMove;

/**
 * Clase que define a un jugador de Ataxx
 */
public class AtaxxSwingPlayer extends Player{

	private static final long serialVersionUID = 1L;

	//Atributos
	private Piece p;
	private int colOrig;
	private int rowOrig;
	private int colDest;
	private int rowDest;
	
	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces,
			GameRules rules) {
		return new AtaxxMove(this.rowOrig,this.colOrig,this.rowDest,this.colDest,this.p);
	}
	
	/**
	 * Inicializa los atributos de la clase para realizar ese movimiento
	 * @param rO Fila de origen
	 * @param cO Columna de origen
	 * @param rD Fila de destino
	 * @param cD Columna de destino
	 * @param p Pieza que va ha realizar el movimiento
	 */
	public void setMove(int rO,int cO,int rD,int cD,Piece p){
		this.rowOrig=rO;
		this.colOrig=cO;
		this.rowDest=rD;
		this.colDest=cD;
		this.p=p;
	}

}
