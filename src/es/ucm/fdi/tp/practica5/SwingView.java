package es.ucm.fdi.tp.practica5;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.Main.PlayerMode;

/**
 * Clase que muestra la interfaz grafica
 */
public abstract class SwingView  extends JFrame implements GameObserver{

	private static final long serialVersionUID = 1L;
	
	//Atributos
	private Controller ctrl;
	private Piece localPiece;
	private Piece turn;
	private Board board;
	private List<Piece> pieces;
	private Map<Piece, Color> pieceColors;
	private Map<Piece, PlayerMode> playerTypes;
	private Player random, ai;
	private JPanel pnlAux, pnlMsg, playerInfo, piecesColor, playerModes, autoMoves, quitRestartButton;
	private JTextArea message;
	private MiModeloDeTabla modelo;
	private JComboBox<String> cboPlay1,cboPlay2;
	private JButton rand, intel;
	private boolean pulsado,auto,intelli;
	
	//Constructor
	public SwingView(Observable<GameObserver> g, Controller c, Piece localPiece, Player randPlayer, Player aiPlayer) {
		this.setTitle("Board Games: ");
		this.ctrl = c;
		this.localPiece = localPiece;
		this.random = randPlayer;
		this.ai = aiPlayer;
		this.pieces = new ArrayList<>();
		this.pieceColors = new HashMap<>();
		this.playerTypes = new HashMap<>();
		this.pulsado=false;
		this.auto=false;
		this.intelli=false;
		initGUI();
		g.addObserver(this);
	}
	
	/**
	 * Realiza un movimiento manual
	 * @param manualPlayer El jugador
	 */
	final protected void decideMakeManualMove(Player manualPlayer) {
		ctrl.makeMove(manualPlayer);
	}
	
	/**
	 * Realiza un movimiento random o inteligente
	 */
	private void decideMakeAutomaticMove() { 
		if(!this.auto){
			if(playerTypes.get(this.turn).equals(PlayerMode.AI))
				ctrl.makeMove(this.ai);
			else
				ctrl.makeMove(this.random);
		}else{
			if(this.intelli)
				ctrl.makeMove(this.ai);
			else
				ctrl.makeMove(this.random);
		}
		int numPiece = getAtributosNecesarios();
		if(numPiece > 0){
			setAtributosNecesarios();
			String num;
			numPiece=getAtributosNecesarios();
			if(numPiece >= 0){
				if(localPiece != null)
					num = Integer.toString(numPiece);
				else
					num = Integer.toString(numPiece/2);
				setValueAt(num, this.turn, 2);
			}
		}
		
	}
	
	/**
	 * Inicializa la interfaz grafica
	 */
	private void initGUI(){
		this.setSize(700, 450);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout());
		pnlAux = new JPanel();
		pnlAux.setLayout(new BoxLayout(pnlAux,BoxLayout.Y_AXIS));
		StatusMenssages();
		playerInfo();
		piecesColors();
		playerModes();
		autoMoves();
		quitRestartButton();
		pnlAux.add(pnlMsg);
		pnlAux.add(playerInfo);
		pnlAux.add(piecesColor);
		pnlAux.add(playerModes);
		pnlAux.add(autoMoves);
		pnlAux.add(quitRestartButton);
		initBoardGui();
		this.setLayout(new BorderLayout());
		this.add(pnlAux, BorderLayout.EAST);
		this.setVisible(true);
	}
	
	/**
	 * Escribe por pantalla informacion relativa a la partida
	 */
	private void StatusMenssages(){
		pnlMsg = new JPanel();
		pnlMsg.setLayout(new BorderLayout());
		pnlMsg.setBorder(BorderFactory.createTitledBorder("Mensajes de estado"));
		message = new JTextArea();
		message.setEditable(false);
		pnlMsg.add(message);
		pnlMsg.setPreferredSize(new Dimension(250, 150));
		JScrollPane scr = new JScrollPane(message);
		pnlMsg.add(scr);
	}
	
	/**
	 * Tabla con la informacion de los jugadores
	 */
	private void playerInfo (){
		playerInfo = new JPanel();
		playerInfo.setLayout(new BorderLayout());//Los elementos ocupan todo el espacio disponible
		playerInfo.setPreferredSize(new Dimension(250,95));
		modelo = new MiModeloDeTabla();
		JTable tbl = new JTable(modelo){
			private static final long serialVersionUID = 1L;
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
				Component comp = super.prepareRenderer(renderer, row, col);
				if(SwingView.this.localPiece==null)
					comp.setBackground(pieceColors.get(pieces.get(row)));
				else
					comp.setBackground(pieceColors.get(SwingView.this.localPiece));
				return comp;
			}
		};
		tbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tbl.setCellSelectionEnabled(false);
		tbl.getTableHeader().setReorderingAllowed(false);
		playerInfo.setBorder(BorderFactory.createTitledBorder("Informacion de jugadores"));
		playerInfo.add(tbl);
		JScrollPane scr = new JScrollPane(tbl);
		playerInfo.add(scr);
	}
	
	/**
	 * Clase anidada que representa una tabla
	 */
	protected class MiModeloDeTabla extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private String[][] data;
		private final String[] columnNames;
		
		//Constructora
		public MiModeloDeTabla() {
			this.columnNames = new String[]{"Jugador", "Modo", "#Piezas"};
			if(!pieces.isEmpty())
				inicializarTabla();
		}
		
		/**
		 * Inicializa la tabla
		 */
		private void inicializarTabla(){
			int num=pieces.size();
			if(SwingView.this.localPiece!=null)
				num = 1;
			data = new String[num][3];
			for (int i = 0;i < num; i++){
				Piece p = pieces.get(i);
				if(SwingView.this.localPiece!=null)
					p = SwingView.this.localPiece ;
				data[i][0] = p.getId();
				data[i][1] = playerTypes.get(p).getDesc();
				if (board.getPieceCount(p) != null)
					data[i][2] = board.getPieceCount(p).toString();
			}
		}
		
		@Override
		public int getColumnCount(){
			return columnNames.length;
		}
		
		@Override
		public String getColumnName(int col){
			return columnNames[col];
		}

		@Override
		public int getRowCount(){
			return data != null ? data.length : 0;
		}

		@Override
		public Object getValueAt(int f, int c){
			return data[f][c];
		}
	
		/**
		 * Modifica el valor de el parametro de la posicion (f,c)
		 * @param o Nuevo valor de la casilla
		 * @param f fila a modificar
		 * @param c columna a modificar
		 */
		public void setValueAt(String o, int f, int c){
			data[f][c] = o;
		}
		
		/**
		 * Devuelve la fila en a que se encuentar la pieza p
		 * @param p Una pieza
		 * @return la fila de la pieza p
		 */
		public int getRow(Piece p){
			int fila = 0;
			while (!getValueAt(fila,0).equals(p.getId()) && fila<pieces.size())
				fila++;
			return fila;
		}
		
		@Override 
		public boolean isCellEditable(int row, int col){
			 return false; 
		}
		
		/**
		 * Carga la nueva informacion de la tabla
		 */
		public void refresh() {
			fireTableDataChanged();
		}
	}
	
	/**
	 * Modifica el valor de la celda (p,c)
	 * @param o Nuevo dato
	 * @param p Pieza que cambia 
	 * @param c Columna donde se modifica
	 */
	public void setValueAt(String o, Piece p, int c){
		if(this.localPiece!=null)
			modelo.setValueAt(o,0,c);
		else
			modelo.setValueAt(o,pieces.indexOf(p),c);
		repaint();
	}
	
	/**
	 * Metodo que crea la selleccion de colores
	 */
	private void piecesColors(){
		piecesColor = new JPanel();
		piecesColor.setBorder(BorderFactory.createTitledBorder("Color de fichas"));
		cboPlay1 = new JComboBox<String>();
		JButton color = new JButton("Elegir color");
		color.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Piece p = new Piece(cboPlay1.getSelectedItem().toString());
					ColorChooser c = new ColorChooser(new JFrame(), "Choose Line Color", pieceColors.get(p));
					if (c.getColor() != null) {
						setPieceColor(p,c.getColor());
						repaint();
						modelo.refresh();
						redrawBoard();
					}
				} catch (Exception _e) {
					System.err.println("Error al cambiar el color");
				}
			}
		});
		piecesColor.add(cboPlay1);
		piecesColor.add(color);
	}
	
	/**
	 * Selecciona el modo de juego de cada jugador
	 */
	private void playerModes(){
		PlayerMode[] modos = {PlayerMode.MANUAL, PlayerMode.RANDOM, PlayerMode.AI};
		playerModes = new JPanel();
		playerModes.setBorder(BorderFactory.createTitledBorder("Modos de jugadores"));
		cboPlay2 = null;
		cboPlay2 = new JComboBox<String>();
		JComboBox<String> mode = new JComboBox<String>();
		for (int i = 0; i<modos.length; i++){
			mode.addItem(modos[i].getDesc());
		}
		JButton set = new JButton("Aceptar");
		set.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				Piece p = new Piece(cboPlay2.getSelectedItem().toString());
				PlayerMode m = modos[mode.getSelectedIndex()];
				playerTypes.put(p, m);
				repaint();
				modelo.setValueAt(m.getDesc(), modelo.getRow(p), 1);
				if(p.equals(turn)){
					SwingView.this.auto=false;
					decideMakeAutomaticMove();
				}
			}
		});
		playerModes.add(cboPlay2);
		playerModes.add(mode);
		playerModes.add(set);
	}
	
	/**
	 * Botones para realizar movimientos automaticos
	 */
	private void autoMoves(){
		autoMoves = new JPanel();
		autoMoves.setBorder(BorderFactory.createTitledBorder("Modos automaticos"));
		rand = new JButton("Aleatorio");
		intel = new JButton("Inteligente");
		if(!playerTypes.isEmpty() && !this.turn.equals(this.localPiece) && playerTypes.get(turn) != PlayerMode.MANUAL){
			rand.setEnabled(false);
			intel.setEnabled(false);
		}else{
			if(!pulsado){
				rand.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e){
						rand.setEnabled(false);
						intel.setEnabled(false);
						SwingView.this.auto=true;
						SwingView.this.intelli=false;
						decideMakeAutomaticMove();
					}
				});
				intel.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e){
						rand.setEnabled(false);
						intel.setEnabled(false);
						SwingView.this.auto=true;
						SwingView.this.intelli=true;
						decideMakeAutomaticMove();
					}
				});
				this.pulsado=true;
			}
		}
		autoMoves.add(rand);
		autoMoves.add(intel);
		this.pulsado=false;
	}
	
	/**
	 * Botones para cerrar y empezar de nuevo
	 */
	private void quitRestartButton(){
		
		quitRestartButton = new JPanel();
		JButton quit = new JButton("Cerrar");
		if(!playerTypes.isEmpty() && playerTypes.get(turn)!= PlayerMode.MANUAL)
			quit.setEnabled(false);
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int res = JOptionPane.showConfirmDialog(
						SwingView.this, 
						"Quieres cerrar el juego?", 
						"Cerrar",  
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.QUESTION_MESSAGE);
				if(res == 0){
					ctrl.stop();
					SwingView.this.dispose();
				}
			}
		});
		quitRestartButton.add(quit);
		
		JButton restart = new JButton("Reiniciar");
		if(localPiece == null){
			if(!playerTypes.isEmpty() && !playerTypes.get(turn).equals(PlayerMode.MANUAL) )
				restart.setEnabled(false);
			restart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
			        reiniciar();
					ctrl.restart();
				}
			});
			quitRestartButton.add(restart);
		}
	}
	
	final protected Controller getCtrl(){
		return this.ctrl;
	}
	
	final protected Piece getTurn(){
		return turn;
	}
	
	final protected Board getBoard(){
		return board;
	}
	
	final protected List<Piece> getPieces(){
		return pieces;
	}
	
	final protected Color getPieceColors(Piece p){
		return pieceColors.get(p);
	}
	
	final protected Color setPieceColor(Piece p,Color c){
		return pieceColors.put(p, c);
	}
	
	protected void setBoardArea(BoardComponent boardComp) {
		this.add(boardComp, BorderLayout.CENTER);
	}
	
	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){handleGameStart(board, gameDesc, pieces, turn);}
		});
		
	}
	private void handleGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		Color[] colores = {Color.ORANGE, Color.GREEN, Color.LIGHT_GRAY, Color.PINK};
		this.board = board;
		this.pieces = pieces;
		this.turn = turn;
		cboPlay1.removeAllItems();
		cboPlay2.removeAllItems();
		if(this.localPiece==null){
			for (int i = 0; i<pieces.size(); i++){
				this.cboPlay1.addItem(pieces.get(i).getId());
				this.cboPlay2.addItem(pieces.get(i).getId());
			}
		}
		else{
			for (int i = 0; i<pieces.size(); i++){
				this.cboPlay1.addItem(pieces.get(i).getId());
			}
			this.cboPlay2.addItem(this.localPiece.getId());
		}
		for (int i=0; i < pieces.size(); i++){
			this.playerTypes.put(pieces.get(i), PlayerMode.MANUAL);
			this.pieceColors.put(this.pieces.get(i), colores[i]);
		}
		this.modelo.inicializarTabla();
		this.modelo.refresh();
		message.append("Bienvenido a "+ gameDesc);
		message.append("\n");
		message.append("Empieza el jugador "+ turn.getId());
		message.append("\n");
		initBoardGui();
		if(this.turn.equals(this.localPiece) || this.localPiece == null){
			activateBoard();
			rand.setEnabled(true);
			intel.setEnabled(true);
		}else{
			deActivateBoard();
			rand.setEnabled(false);
			intel.setEnabled(false);
		}
	}
	
	@Override
	public void onGameOver(Board board, Game.State state, Piece winner){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){ handleGameOver(board, state, winner);}
		});
	}
	
	private void handleGameOver(Board board, Game.State state, Piece winner) {
		this.board = board;
		message.append("Fin del Juego!");
		message.append("\n");
		message.append("Estado: ");
		if(state .equals(State.Won) ){
			message.append("Ganado ("+state+")");
			message.append("\n");
			message.append("El ganador es " + winner.getId());
		}else if(state .equals(State.Draw))
			message.append("Empate ("+state+")");
		else if(state.equals(State.Stopped))
			message.append("Parado ("+state+")");
		message.append("\n");
	}
		
	@Override
	public void onMoveStart(Board board, Piece turn){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){ handleMoveStart(board, turn); }
		});
	}
	
	private void handleMoveStart(Board board, Piece turn) {
		this.board = board;
		this.turn = turn;
	}
	
	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){ handleMoveEnd(board, turn, success);}
		});
		
	}
	private void handleMoveEnd(Board board, Piece turn, boolean success) {
		this.board = board;
		this.turn = turn;
		if(success)
			message.append("Movimiento realizado");
		else
			message.append("Error al ejecutar el movimiento!");
		message.append("\n");
		redrawBoard();
	}
	
	@Override
	public void onChangeTurn(Board board, Piece turn){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){handleChangeTurn(board, turn);}
		});
	}

	private void handleChangeTurn(Board board, Piece turn) {
		this.board = board;
		this.turn = turn;
		message.append("Turno de "+ turn.getId());
		if(  turn.equals(localPiece)){
			message.append("(You!)");
		}
		message.append("\n");
		if(playerTypes.get(turn)!= PlayerMode.MANUAL)
			decideMakeAutomaticMove();
		if(this.localPiece == null || turn.equals(localPiece)){
			activateBoard();
			if(playerTypes.get(turn) == PlayerMode.MANUAL){
				rand.setEnabled(true);
				intel.setEnabled(true);
			}
		}
		else{
			deActivateBoard();
			rand.setEnabled(false);
			intel.setEnabled(false);
		}
			
	}
	
	@Override
	public void onError(String msg){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){handleError(msg); }	
		});
	}
	
	private void handleError(String msg) {
		if(this.localPiece==null || this.turn.equals(this.localPiece)){
			JOptionPane.showConfirmDialog(
					SwingView.this, 
					msg, 
					"Error",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.ERROR_MESSAGE);
		}
		
	}
			
	protected abstract void initBoardGui();
	protected abstract void activateBoard();
	protected abstract void deActivateBoard();
	protected abstract void redrawBoard();
	/**
	 * Reiniciar algunos atributos necesarios del juego 
	 */
	protected abstract void reiniciar();
	/**
	 * Modificar algunos atributos necesarios
	 */
	protected abstract void setAtributosNecesarios();
	/**
	 * Obtener algunos atributos necesarios
	 */
	protected abstract int getAtributosNecesarios();
}
