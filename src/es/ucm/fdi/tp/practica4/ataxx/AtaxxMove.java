package es.ucm.fdi.tp.practica4.ataxx;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
/**
 * Clase que describe un movimiento de ataxx
 */
public class AtaxxMove extends GameMove {

	private static final long serialVersionUID = 1L;

	//Atributos
	private int fOrig;
	private int cOrig;
	private int fDest;
	private int cDest;
	
	/**
	 * Constructora por defecto
	 */
	public AtaxxMove() {
	}
	
	/**
	 * Constructor con parametro 
	 * @param fOrigen fila Origen de la ficha
	 * @param cOrigen columna origen de la ficha
	 * @param fDestino fila destino de la ficha
	 * @param cDestino columna destino de la ficha
	 * @param p la pieza que tenemos que crear
	 */
	public AtaxxMove(int fOrigen, int cOrigen, int fDestino, int cDestino, Piece p) {
		super(p);
		this.fDest = fDestino;
		this.cDest = cDestino;
		this.fOrig = fOrigen;
		this.cOrig = cOrigen;
	}

	@Override
	public void execute(Board board, List<Piece> pieces) {
		Piece turn = getPiece();
		//conprueba si ha elegido una ficha correcta no uno de enemigo
		if(!turn.equals(board.getPosition(fOrig,cOrig))){
			if(board.getPosition(fOrig,cOrig)!=null)
				throw new GameError("Esa ficha no es tuyo,elige uno que es tuyo");
			else
				throw new GameError("Esa posicion esta vacia, elige uno que contenga ficha");
		}
		if (Math.abs(fOrig - fDest) <= 2 && Math.abs(cOrig - cDest) <= 2){
			if (board.getPosition(fDest, cDest) == null) {
				//aux es identificador de obstaculo
				String aux="*";
				if (turn.equals(board.getPosition(fOrig,cOrig))) {
					board.setPosition(fDest, cDest, turn);
					// Si la distancia entre origen y destino es dos, se elimina la pieza origen
					if (Math.abs(fOrig - fDest) == 2 || Math.abs(cOrig - cDest) == 2)
						board.setPosition(fOrig, cOrig, null);
					//Recorre las casillas adyacentes y modifica las que son de otros jugadores
					for (int i = fDest - 1; i <= fDest + 1; i++) {
						for (int j = cDest - 1; j <= cDest + 1; j++) {
							if (AtaxxRules.esDentroTablero(board, i, j) 
									&&  !turn.equals(board.getPosition(i, j)) 
									&& board.getPosition(i, j)!=null
									//conmprueba que los adaycentes no sean obstaculos
									&& board.getPosition(i, j).getId()!=aux) {
								board.setPosition(i, j, turn);
							}
						}
					}
				}else
					throw new GameError("Movimiento no valido");
			}else {
				throw new GameError("position (" + fDest + "," + cDest
						+ ") is already occupied:we think is obstacle!");
			}
		}
			
		else
			throw new GameError("Movimiento de (" + fOrig + ","
					+ cOrig + ") a (" + fDest + "," + cDest
					+ ") no valido,solo puede mover a las casillas situadas a una distancia maxima de dos casillas.");

		

	}

	@Override
	public GameMove fromString(Piece p, String str) {
		GameMove move = null;
		int fOrigen, cOrigen, fDestino, cDestino;
		String[] words = str.split(" ");
		if (words.length == 4) {
			try {
				fOrigen = Integer.parseInt(words[0]);
				cOrigen = Integer.parseInt(words[1]);
				fDestino = Integer.parseInt(words[2]);
				cDestino = Integer.parseInt(words[3]);
				if (Math.abs(fOrigen - fDestino) <= 2 && Math.abs(cOrigen - cDestino) <= 2)
					move = new AtaxxMove(fOrigen, cOrigen, fDestino, cDestino,
							p);
				else
					throw new GameError("Movimiento de (" + fOrigen + ","
							+ cOrigen + ") a (" + fDestino + "," + cDestino
							+ ") no valido,solo puede mover a las casillas situadas a una distancia maxima de dos casillas.");
			} catch (NumberFormatException e) {
				move = null;
			}
		}
		return move;
	}

	@Override
	public String help() {
		return " 'filaOrigen columnaOrigen filaDestino columnaDestino'  ";
	}

}
