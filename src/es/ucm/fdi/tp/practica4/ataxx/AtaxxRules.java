package es.ucm.fdi.tp.practica4.ataxx;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.Utils;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.FiniteRectBoard;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Pair;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;

/**
 * Reglas del juego Ataxx.
 * <ul>
 * <li>El juego se juega en un tablero  NxM (con N,M mayor o igual a 5).</li>
 * <li>El numero de jugadores esta entre 2 y 4.</li>
 * <li>Los jugadores juegan en el orden proporcionado, cada uno moviendo sus ficha 
 * a una distancia de 1 o 2 posiciones. Si mueve a una distancia de uno crea una nueva 
 * ficha en el destino. Si mueve a una distancia de dos se elimina la pieza origen.
 * </li>
 * </ul>
 */
public class AtaxxRules implements GameRules {

	//Atributos
	private int dimFila;
	private int dimCol;
	private int obstacles;
	
	//Constante
	private static final int RANGO = 2;

	//Contructora por defecto
	public AtaxxRules() {
		this(7, 7);
	}

	/**
	 * Constructora con parametros sin obstaculos
	 * @param dimFila Numero de filas del tablero
	 * @param dimCol Numero de columnas del tablero
	 */
	public AtaxxRules(int dimFila, int dimCol) {
		if (dimFila < 5 || dimCol < 5) {
			throw new GameError(
					"Dimension de la fila y columna debe ser al menos  5: "
							+ dimFila + dimCol);
		}else if (dimFila % 2 == 0 || dimCol % 2 == 0) {
			throw new GameError(
					"Dimension de la fila y columna debe ser impar: " 
					+ dimFila + dimCol);
		}else {
			this.dimFila = dimFila;
			this.dimCol = dimCol;
			this.obstacles=0;
		}

	}
	/**
	 * Contructora con parametros y obstaculos
	 * @param dimFila Numero de filas del tablero
	 * @param dimCol Numero de columnas del tablero
	 * @param obstacles Numero de obstaculos
	 */
	public AtaxxRules(int dimFila, int dimCol,int obstacles) {
		if (dimFila < 5 || dimCol < 5){
			throw new GameError(
					"Dimension de la fila y columna debe ser al menos  5: "
							+ dimFila + dimCol);
		}else if (dimFila % 2 == 0 || dimCol % 2 == 0) {
			throw new GameError(
					"Dimension de la fila y columna debe ser impar: " + dimFila
							+ dimCol);
			
		}else if(obstacles>=dimFila*dimCol){
			throw new GameError("Demasiado obstaculos"+ obstacles);
		}else{
			this.dimFila = dimFila;
			this.dimCol = dimCol;
			this.obstacles=obstacles;
		}

	}

	@Override
	public String gameDesc() {
		return "Attax " + dimFila + "x" + dimCol;
	}

	@Override
	public Board createBoard(List<Piece> pieces) {
		Board b = new FiniteRectBoard(dimFila, dimCol);
		Piece p;
		p = pieces.get(0);
		b.setPosition(0, 0, p);
		b.setPosition(dimFila - 1, dimCol - 1, p);
		p = pieces.get(1);
		b.setPosition(dimFila - 1, 0, p);
		b.setPosition(0, dimCol - 1, p);
		if (pieces.size() > 2){
			p = pieces.get(2);
			b.setPosition(((dimFila - 1) / 2), 0, p);
			b.setPosition(((dimFila - 1) / 2), dimCol - 1, p);
		}
		if (pieces.size() > 3){
			p = pieces.get(3);
			b.setPosition(0, ((dimCol - 1) / 2), p);
			b.setPosition(dimFila - 1, ((dimCol - 1) / 2), p);
		}
	
		for(int i=0;i<this.obstacles;i++){
			boolean creado=false;
			//aux es simbolo representante de obstaculo;
			String aux="*";
			Piece obs=new Piece(aux);
			creado=creatObstacle(b,obs);
			while(!creado){
				creado=creatObstacle(b,obs);
			}
		}
		return b;
	}
	
	 /**
	  * Metodo que consiste en obtener dos numero de fila y columa aleatoria 
	  * y crearlo en el tablero y si lo crea  devuelve true,si no false
	  * @param b el tablero donde se crea el obstaculo
	  * @param obs el obstaculo que tenmos que crear
	  * @return un booleano que indica si ha creado o no 
	  */
	private boolean creatObstacle(Board b,Piece obs){
		boolean creado=false;
		int filaObstacle=Utils.randomInt(b.getRows());
		int colObstacle=Utils.randomInt(b.getCols());
	    if(b.getPosition(filaObstacle, colObstacle)==null){
			b.setPosition(filaObstacle, colObstacle, obs);
			creado=true;
		}
	    return creado;
	}

	@Override
	public int minPlayers() {
		return 2;
	}

	@Override
	public int maxPlayers() {
		return 4;
	}

	@Override
	public Pair<State, Piece> updateState(Board board,
			List<Piece> playersPieces, Piece lastPlayer) {
		Pair<State, Piece> resultado;
		if (!soloUno(board, playersPieces, lastPlayer)
				&& nextPlayer(board,playersPieces, lastPlayer)!=null )
			resultado = new Pair<State, Piece>(State.InPlay, null);
		else{
			int win = ganador(board, playersPieces);
			if (win == -1)
				resultado = new Pair<State, Piece>(State.Draw, null);
			else
				resultado = new Pair<State, Piece>(State.Won,playersPieces.get(win));
		}
		return resultado;
	}

	@Override
	public Piece initialPlayer(Board board, List<Piece> playersPieces){
		return playersPieces.get(0);
	}

	@Override
	public Piece nextPlayer(Board board, List<Piece> playersPieces,
			Piece lastPlayer) {
		List<Piece> pieces = playersPieces;
		int i = pieces.indexOf(lastPlayer);
		int cont=0;
		Piece next = pieces.get((i + 1) % pieces.size());
		// Comprobar que el siguiente jugador puede realizar algun movimiento
		while (cont < playersPieces.size() && validMoves(board, pieces, next).isEmpty()) {
			next = pieces.get((i + 1) % pieces.size());
			i = pieces.indexOf(next);
			cont++;
		}
		if (cont == playersPieces.size())
			next = null;
		return next;
	}

	@Override
	public double evaluate(Board board, List<Piece> pieces, Piece turn, Piece p) {
		return 0;
	}

	@Override
	// De la pieza turn
	public List<GameMove> validMoves(Board board, List<Piece> playersPieces,
			Piece turn){
		List<GameMove> moves = new ArrayList<GameMove>();
		for (int f = 0; f < board.getRows(); f++) {
			for (int c = 0; c < board.getCols(); c++) {
				if (board.getPosition(f, c) == turn)
					pieceMoves(moves, board, turn, f, c);
			}
		}
		return moves;
	}
	
	/**
	 * Metodo privado que añade los movimientos validos de una pieza a la lista
	 * @param moves Lista con los movimientos validos
	 * @param board El tablero de juego
	 * @param p Pieza para la que se buscan movimienotos
	 * @param fila Posicion de la pieza
	 * @param col Posicion de la pieza
	 */
	private void pieceMoves(List<GameMove> moves, Board board, Piece p,
			int fila, int col) {
		// Recorro las 24 casillas que la rodean
		for (int i = fila - RANGO; i <= fila + RANGO; i++) {
			for (int j = col - RANGO; j <= col + RANGO; j++) {
				if (esDentroTablero(board, i, j) && board.getPosition(i, j) == null)
					moves.add(new AtaxxMove(fila, col, i, j, p));
			}
		}
	}
	
	/**
	 * Metodo privado que indica si solo quedan en el tablero fichas de un jugador
	 * @param board El tablero
	 * @param playersPieces Los jugadores
	 * @param lastPlayer La ultima ficha movida
	 * @return un booleano que indica si puede continuar el juego
	 */
	private boolean soloUno(Board board, List<Piece> playersPieces,
			Piece lastPlayer) {
		Piece p = null;
		boolean uno = true;

		int i =0, j=0;
		while(uno && i<board.getRows()){
			j=0;
			while(uno && j < board.getCols()){
				if(p == null && board.getPosition(i, j)!= null)
					p = board.getPosition(i, j);
				else if(p != null && board.getPosition(i, j)!= null)
					uno = p == board.getPosition(i, j);
				j++;
			}
			i++;
		}
		return uno;
	}

	/**
	 * Metodo privado que indica quien ha ganado el juego.
	 * @param board El tablero de juego.
	 * @param playersPieces Los jugadores.
	 * @return Un entero que indica la posicion del ganador en la lista de jugadores. -1 si no hay un ganador.
	 */
	private int ganador(Board board, List<Piece> playersPieces) {
		int[] jugadores = new int[playersPieces.size()];
		int cont = 0, win = 0, second = 0;
		while (cont < playersPieces.size()){
			for (int f = 0; f < board.getRows(); f++){ 
				for (int c = 0; c < board.getCols(); c++){
					if (board.getPosition(f, c) == playersPieces.get(cont))
						jugadores[cont]++;
				}
			}
			cont++;
		}
		for (int i = 1; i < playersPieces.size(); i++) {
			if (jugadores[win] <= jugadores[i]){
				second = win;
				win = i;
			}
		}
		if(jugadores[second]==jugadores[win] && second != win)
			win = -1;
		return win;
	}

	/**
	 * Metodo que indica si una posicion esta dentro del tablero
	 * @param board El tablero de juego
	 * @param f Numero de la fila
	 * @param c Numero de la columna
	 * @return Un booleano: true dento del tablero, false fuera del tablero
	 */
	public static boolean esDentroTablero(Board board, int f, int c) {
		return (f >= 0 && c >= 0 && f < board.getRows() && c < board.getCols());
	}

}