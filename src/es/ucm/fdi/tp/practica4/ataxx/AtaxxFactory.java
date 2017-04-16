package es.ucm.fdi.tp.practica4.ataxx;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import es.ucm.fdi.tp.basecode.bgame.control.ConsolePlayer;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.DummyAIPlayer;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.AIAlgorithm;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.bgame.views.GenericConsoleView;

/**
 * Clase que define y crea los parametros de ataxx
 */
public class AtaxxFactory implements GameFactory{

	private static final long serialVersionUID = 1L;
	
	//Atributos
	private int dimFila;
	private int dimCol;
	private int obstaculo;
	
	/**
	 * Constructor por defecto que inicializa uno con dimension 7x7
	 */
	public AtaxxFactory(int obstaculo) {
		this(7,7,obstaculo);
	}
	
	/**
	 * Constructora con parametros y obstaculos
	 * @param dimFila Numero de filas del tablero
	 * @param dimCol Numero de columnas del tablero
	 * @param obstaculo Numero de obstaculos
	 */
	public AtaxxFactory(int dimFila,int dimCol,int obstaculo){
		if (dimFila < 5 || dimCol < 5 ) 
			throw new GameError("Dimension de la fila y columna debe ser al menos  5: " + dimFila + dimCol);
		else if(dimFila%2 == 0 || dimCol%2 == 0)
			throw new GameError("Dimension de la fila y columna debe ser impar: " + dimFila + dimCol);
		else if(obstaculo >= dimFila*dimCol)
			throw new GameError("Demasiado obstaculos"+ obstaculo);
		else{
			this.dimFila = dimFila;
			this.dimCol = dimCol;
			this.obstaculo = obstaculo;
		}	
	}
	
	@Override
	public GameRules gameRules(){
		return new AtaxxRules(dimFila,dimCol,obstaculo);
	}

	@Override
	public Player createConsolePlayer(){
		ArrayList<GameMove> possibleMoves = new ArrayList<GameMove>();
		possibleMoves.add(new AtaxxMove());
		return new ConsolePlayer(new Scanner(System.in), possibleMoves);
	}

	@Override
	public Player createRandomPlayer(){
		return new AtaxxRandomPlayer();
	}

	@Override
	public Player createAIPlayer(AIAlgorithm alg){
		return new DummyAIPlayer(createRandomPlayer(), 1000);
	}

	@Override
	public List<Piece> createDefaultPieces(){
		List<Piece> pieces = new ArrayList<Piece>();
		pieces.add(new Piece("X"));
		pieces.add(new Piece("O"));
		return pieces;
	}

	@Override
	public void createConsoleView(Observable<GameObserver> game, Controller ctrl){
		new GenericConsoleView(game, ctrl);
	}

	@Override
	public void createSwingView(Observable<GameObserver> game, Controller ctrl,
			Piece viewPiece, Player randPlayer, Player aiPlayer){
		throw new UnsupportedOperationException("There is no swing view");
	}

}
