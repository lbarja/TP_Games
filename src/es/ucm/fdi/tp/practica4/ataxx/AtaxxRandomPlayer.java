package es.ucm.fdi.tp.practica4.ataxx;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.Utils;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * Clase que define un jugador aleatorio para Ataxx.
 */
public class AtaxxRandomPlayer extends Player{
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces,
			GameRules rules) {
		if (board.isFull())
			throw new GameError("The board is full, cannot make a random move!!");
		List<GameMove> moves = new ArrayList<GameMove>();
		moves = rules.validMoves(board, pieces, p);
		int moveRandom = Utils.randomInt(moves.size());
		return moves.get(moveRandom);
	}
}
