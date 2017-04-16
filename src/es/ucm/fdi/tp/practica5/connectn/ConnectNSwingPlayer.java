package es.ucm.fdi.tp.practica5.connectn;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.connectn.ConnectNMove;
/**
 * Clase que define un jugador de ConnectN
 */
public class ConnectNSwingPlayer extends Player {

	private static final long serialVersionUID = 1L;
	private int row;
	private int col;
	private Piece p;
	
	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces,GameRules rules) {
		return new ConnectNMove(this.row,this.col,this.p);
	}
	
	/**
	 * Inicializa los atributos de la clase para realizar ese movimiento
	 * @param row Numero de fila
	 * @param col Numero de columna
	 * @param p Pieza que va ha realizar el movimiento
	 */
	public void setMove(int row, int col,Piece p) {
		this.row = row;
		this.col = col;
		this.p = p;
	}

}
