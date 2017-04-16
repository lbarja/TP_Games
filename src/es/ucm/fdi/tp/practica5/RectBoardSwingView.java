package es.ucm.fdi.tp.practica5;

import java.awt.Color;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * Clase que inicializa la parte grafica del tablero
 */
public abstract class RectBoardSwingView extends SwingView{

	private static final long serialVersionUID = 1L;
	//Atributos
	private BoardComponent boardComp;
    private final static Color OBSTACLECOLOR=Color.BLACK;
	
    //Constructora
	public RectBoardSwingView(Observable<GameObserver> g, Controller c,
			Piece localPiece, Player randPlayer, Player aiPlayer) {
		super(g, c, localPiece, randPlayer, aiPlayer);
	}

	/**
	 * Inicializa el tablero de juego
	 */
	protected void initBoardGui(){
		if(boardComp !=null)
			super.remove(boardComp);
		boardComp = new BoardComponent(getBoard()){
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void mouseClicked(int row, int col, int mouseButton){
				// call handleMouseClick to let subclasses handle the event
				handleMouseClick(row,  col,  mouseButton);
			}

			@Override
			protected boolean isPlayerPiece(Piece p){
			    // return true if p is a player piece, false if not (e.g, an obstacle)
				return getPieces().contains(p);
			}
			
			@Override
			protected Color getPieceColor(Piece p){
				// get the color from the colours table, and if not
				// available (e.g., for obstacles) set it to have a color
				Color color;
				if(isPlayerPiece(p))
					color = getPieceColors(p);
				else
					color = OBSTACLECOLOR;
				return color; 
			};
		};
		setBoardArea(boardComp); // install the board in the view
	}
		

      
	@Override
	protected void redrawBoard(){
		this.boardComp.redraw(getBoard());
   }

	
	public void setValueAt(String o, Piece p, int c){
		super.setValueAt(o, p, c);
	}
   
	/**
	 * Metodo que lee las acciones del raton
	 * @param row La fila
	 * @param col La columna
	 * @param mouseButton El boton del raton que se ha pulsado
	 */
	protected abstract void handleMouseClick(int row, int col, int mouseButton);
	
}
