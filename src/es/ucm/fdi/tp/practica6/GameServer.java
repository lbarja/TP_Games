package es.ucm.fdi.tp.practica6;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.control.commands.Command;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica6.response.ChangeTurnResponse;
import es.ucm.fdi.tp.practica6.response.ErrorResponse;
import es.ucm.fdi.tp.practica6.response.GameOverResponse;
import es.ucm.fdi.tp.practica6.response.GameStartResponse;
import es.ucm.fdi.tp.practica6.response.MoveEndResponse;
import es.ucm.fdi.tp.practica6.response.MoveStartResponse;
import es.ucm.fdi.tp.practica6.response.Response;

/**
 * El servidor que controla los clientes
 */
public class GameServer extends Controller implements GameObserver{
	
	//Atributos
    private int port;
	private int numPlayers;
	private int numOfConnectedPlayers;
	private GameFactory gameFactory;
	private List<Connection> clients;
	volatile private ServerSocket server;
	volatile private boolean stopped;
	volatile private boolean gameOver;
	private JTextArea infoArea;
	private boolean first;
	
	//Constructora 
	public GameServer(Game game, List<Piece> pieces) {
		super(game, pieces);
	}
	
	//Constructora 
	public GameServer(GameFactory gameFactory,List<Piece> pieces, int serverPort){
		super(new Game(gameFactory.gameRules()), pieces);
		this.gameFactory=gameFactory;
		this.pieces=pieces;
		this.port=serverPort;
		this.numOfConnectedPlayers=0;
		this.first=true;
		this.clients = new ArrayList<Connection>();
		this.numPlayers = pieces.size();
		
		game.addObserver(this);
	}

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces,
			Piece turn) {
		forwardNotification(new GameStartResponse(board, gameDesc, pieces, turn));	
	}
	
	/**
	 * Enviar a cada cliente una notificacion
	 * @param r la notificacion
	 */
	void forwardNotification(Response r) {
		for(int i=0;i<clients.size();i++){
			clients.get(i).sendObject(r);
		}
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		forwardNotification(new GameOverResponse(board,state,winner ));
		// stop the game
		stopGame();
		log("Fin del juego");
	}
	
	/**
	 * Parar el juego (no el servidor)
	 */
	private void stopGame(){
		this.gameOver=true;
		for(int i=0;i<clients.size();i++){
			try {
				clients.get(i).stop();
			} catch (IOException e) {
				System.err.println("Error stop Game");
			}
		}
		this.numOfConnectedPlayers=0;
		clients.clear();
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
		forwardNotification(new MoveStartResponse( board,turn));
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		forwardNotification(new MoveEndResponse( board,turn,success));	
	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {
		forwardNotification(new ChangeTurnResponse( board,turn ));
	}

	@Override
	public void onError(String msg) {
		forwardNotification(new ErrorResponse( msg ));
	}
	
	@Override
	public synchronized void makeMove(Player player) {
		try{ 
			super.makeMove(player);
		}catch (GameError e) {
			System.err.println("Error make move");
		}
	}
	
	@Override
	public synchronized void stop() {
		try{ 
			super.stop(); 
		}catch (GameError e) {
			System.err.println("Error stop");
		}
	}
	
	@Override
	public synchronized void restart(){
		try{ 
			super.restart();
		}catch (GameError e) { 
			System.err.println("Error restart");
		}
	}
	
	/**
	 * Controlador de la vista del server
	 */
	private void controlGUI(){
		try{
			SwingUtilities.invokeAndWait(new Runnable(){
				@Override
				public void run(){ 
					constructGUI(); 
				}
			});
		} catch(InvocationTargetException | InterruptedException e){
			throw new GameError("Something went wrong when constructing the GUI");
		}
	}
	
	/**
	 * Constructor de la vista del server
	 */
	private void constructGUI(){
		JFrame window= new JFrame("Game Server");
		window.getContentPane().setLayout(new BorderLayout());
		// create text area for printing messages
		this.infoArea = new JTextArea();
		this.infoArea.setEditable(false);
		
		// quit button
		JButton quitButton= new JButton("Stop Sever");
		quitButton.addActionListener(new MiActionListener());
		
		window.getContentPane().add(this.infoArea,BorderLayout.CENTER);
		window.getContentPane().add(quitButton,BorderLayout.SOUTH);
		window.setPreferredSize(new Dimension(300,200));
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		window.pack();
		window.setVisible(true);
	}
	
	/**
	 * Clase anonima que realiza las acciones del boton
	 */
	public class MiActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			stopServer();
		}
	}
	
	/**
	 * Parar el servidor
	 */
	private void stopServer(){
		this.stopped=true;
		stopGame();
		try {
			server.close();
		} catch (IOException e) {
			System.err.println("IOException in stopServer");
		}catch(NullPointerException ex){
			System.err.println("Connection with server closed.");
		}finally{
			System.exit(0);
		}
	}
	
	/**
	 * Añadir mensajes a la ventana del servidor
	 * @param msg El mensaje
	 */
	private void log(String msg) {
		// show the message in infoArea
			SwingUtilities.invokeLater (new Runnable(){

				@Override
				public void run(){actualizacionMensaje(msg); }	
			});
	}
	
	/**
	 * Añadir mensaje al texto que ya esta
	 * @param msg El mensaje nuevo
	 */
	private void actualizacionMensaje(String msg){
		this.infoArea.append(msg);
		this.infoArea.append("\n");
	}
	
	/**
	 * Iniciar el servidor
	 */
	private void startServer(){
		try {
			this.server = new ServerSocket(port);
			log("Se ha inicializado el servidor");
			stopped = false;
			while(!stopped){
				try{
					//  1 . accept a conection into a socket s
					Socket s = server.accept();
					//  2 .  log a corresponding mesagge
					log("Se ha recibido una solicitud");
					// 3. call handleRequest(s) to handle the request
					handleRequest(s);
				
				} catch(IOException e){
					if(!stopped)
						log("error while waiting for a connection: "+ e.getMessage());
				}
			}
		} catch (IOException e1){
			System.err.print("You cant initialize two or more servers. ");
			stopped = true;
		}
	}
	
	/**
	 * Enviar datos a los clientes
	 * @param s Union entre el servidor y el cliente
	 */
	private void handleRequest(Socket s) {
		try{
			Connection c = new Connection(s);
			Object clientRequest = c.getObject();
			if(!(clientRequest instanceof String) && 
					!((String) clientRequest).equalsIgnoreCase("Connect")){
				c.sendObject(new GameError("Invalid Request"));
				c.stop();
				return;
			}
			//Si  el  numero  de  clientes  conectados  ya  ha  alcanzado 
			//el maximo, respondemos con un GameError adecuado
			if(this.numOfConnectedPlayers >= numPlayers){
				c.sendObject(new GameError("There are enough people that are connecting"));
				log("- No se permiten mas jugadores");
			}else{
				this.numOfConnectedPlayers++;
				clients.add(c);
				log("- Exito al ejecutar la solicitud");
				c.sendObject("OK");
				c.sendObject(gameFactory);
				c.sendObject(pieces.get(this.numOfConnectedPlayers-1));
		
				//Si hay un numero suficiente de clientes, iniciar 
				// el juego (la primera vez usando start, despues usando restart)
				if(this.numPlayers==this.numOfConnectedPlayers){
					if(first){
						this.first=false;
						super.start();
						log("Empieza el juego");
					}else
						restart();
				}
				startClientListener(c);
			}
		} catch(IOException | ClassNotFoundException _e) {
			System.err.println("Error handle Request");
		}
	}
	
	/**
	 * Recibe comandos del cliente
	 * @param c Una conexion
	 */
	private void startClientListener(Connection c) {
		this.gameOver = false;
		Thread t =  new Thread(new Runnable(){
			@Override
			public void run() {
				while(!stopped&& !gameOver){
					try{
						Command cmd;
						cmd = (Command)c.getObject();
						cmd.execute(GameServer.this);
					}catch(ClassNotFoundException | IOException e){
						if(!stopped&& !gameOver)
							stopGame();
					}
				}
			}
			
		});
		t.start();
	}
	
	@Override
	public void start(){
		controlGUI();
		startServer();
		if(stopped)
			stopServer();
	}
}
