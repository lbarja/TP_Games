package es.ucm.fdi.tp.practica6;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.control.commands.Command;
import es.ucm.fdi.tp.basecode.bgame.control.commands.PlayCommand;
import es.ucm.fdi.tp.basecode.bgame.control.commands.QuitCommand;
import es.ucm.fdi.tp.basecode.bgame.control.commands.RestartCommand;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica6.response.Response;

/**
 * Clase que hace de controlador entre la vista y el servidor
 */
public class GameClient extends Controller implements Observable<GameObserver>{
   
	//Atributos
	private String host;
	private int port;
	private List<GameObserver> observers;
	private Piece localPiece;
	private GameFactory gameFactory;
	private Connection connectionToServer;
	private boolean gameOver;
    
	//Constructora
    public GameClient(String host, int port){
    	super(null,null);
    	this.host = host;
    	this.port = port;
    	this.observers = new ArrayList<GameObserver>();
    	try {
			connect();
		} catch (Exception e) {
			System.err.println("Error al conectar");
		}
    }
    
	@Override
	public void addObserver(GameObserver o) {
		observers.add(o);
		
	}

	@Override
	public void removeObserver(GameObserver o) {
		observers.remove(o);
	}

	/**
	 * Accesora de gameFactory
	 * @return La factoria de juego
	 */
	public GameFactory getGameFactoty() {
		return gameFactory;
	}

	/**
	 * Accesora de playerPiece
	 * @return la pieza de cliente
	 */
	public Piece getPlayerPiece() {
		return localPiece;
	}
	
	/**
	 * Crea una conexi�n con el servidor e inicializa los datos que recibe
	 * @throws Exception Excepcion general
	 */
	private void connect() throws Exception {
		connectionToServer = new Connection(new Socket(host, port));
		connectionToServer.sendObject("Connect");
		Object response = connectionToServer.getObject();
		if (response instanceof Exception) {
			throw (Exception) response;
		}
		try {
			gameFactory = (GameFactory) connectionToServer.getObject();
			localPiece =  (Piece) connectionToServer.getObject();
		} catch (Exception e) {
			throw new GameError("Unknown server response: "+e.getMessage());
		}
	}
	
	@Override
	public void makeMove(Player p){
		forwardCommand(new PlayCommand(p));
	}
	
	@Override 
	public void stop(){
		forwardCommand(new QuitCommand());
	}
	
	@Override 
	public void restart(){
		forwardCommand(new RestartCommand());
	}

	/**
	 * Envia al servidor el comando que se ejecuta
	 * @param cmd el comando
	 */
	private void forwardCommand(Command cmd){
		if(!gameOver){
			this.connectionToServer.sendObject(cmd);
		}
	}
	
	/**
	 * Ejecuta las peticiones del servidor
	 */
	public void start(){
		this.observers.add(new GameObserver(){
			@Override
			public void onGameStart(Board board, String gameDesc,
					List<Piece> pieces, Piece turn) {
				gameOver = false;
			}

			@Override
			public void onGameOver(Board board, State state, Piece winner) {
				gameOver = true;
				try {
					connectionToServer.stop();
				} catch (IOException e) {
					System.err.println("Error al cerrar la conexion");
				}
			}

			@Override
			public void onMoveStart(Board board, Piece turn) {
				gameOver = false;
			}

			@Override
			public void onMoveEnd(Board board, Piece turn, boolean success) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onChangeTurn(Board board, Piece turn) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onError(String msg) {
				// TODO Auto-generated method stub
			}
		});
		gameOver = false;
		while(!gameOver){
			try{
				Response res = (Response) connectionToServer.getObject();
				for(GameObserver o : observers){
					res.run(o);
				}
			}catch(ClassNotFoundException | IOException e){
				System.err.println("Error al recibir respuesta del servidor");
				this.gameOver=true;
			}
		}
	}
}
