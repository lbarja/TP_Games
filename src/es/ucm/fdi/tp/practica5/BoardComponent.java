package es.ucm.fdi.tp.practica5;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * Clase que define el panel donde colocar el tablero
 */
public abstract class BoardComponent extends JPanel{

	private static final long serialVersionUID = 1L;
	private Board board;
	private int filas;
	private int cols;
	
	//Constructora
	public BoardComponent(Board b) {
		this.board = b;
		paint();
		this.setBackground(Color.BLACK);
	}
	
	/**
	 * Metodo que pinta el tablero
	 */
	private void paint(){
		if(board!= null){
			this.filas=board.getRows();
			this.cols=board.getCols();
			this.setLayout(new GridLayout(filas,cols,5,5));
			for (int i = 0; i < filas; i++) {
				for (int j = 0; j < cols; j++) {
					JButton boton = new JButton();
					boton.addMouseListener(new MiMouseListener(i,j));
					if(board.getPosition(i, j)!=null){
						if(isPlayerPiece(board.getPosition(i, j))){
							boton.setText(board.getPosition(i, j).getId());
							boton.setBackground(getPieceColor(this.board.getPosition(i, j)));
						}else 
							boton.setBackground(Color.BLACK);
					}else
						boton.setBackground(Color.WHITE);
					this.add(boton);
				}
			}
		}else{
			this.filas = 0;
			this.cols = 0;
		}
	}
	
	/**
	 * Metodo que genera un nuevo tablero
	 * @param b Informacion del tablero
	 */
	public void redraw(Board b){
		this.board=b;
		this.removeAll();
		paint();
	    this.updateUI();
	    this.repaint();
	}

	/**
	 * Clase anomima que lee las acciones del raton
	 */
	public class MiMouseListener extends MouseAdapter{
		private int fil;
		private int colu;
		//Constructora
		public MiMouseListener(int r,int c){
			this.fil = r;
			this.colu = c;
		}
		
		@Override
		public void mouseClicked(MouseEvent e){
			int mouseButton;
			if((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK)
	            mouseButton=0;
			else
	            mouseButton=1;
			try{
				BoardComponent.this.mouseClicked(this.fil,this.colu,mouseButton);
			}catch (Exception ex){
				System.err.println("Error al pulsar con el raton");
			}
		}
	}
	
	/**
	 * Metodo que accede al color de una pieza
	 * @param p La pieza
	 * @return El color de la pieza p
	 */
	protected abstract Color getPieceColor(Piece p);
	
	/**
	 * Metodo que indica si es una pieza del juego
	 * @param p La pieza
	 * @return Un booleano que indica si la pieza esta jugando
	 */
	protected abstract boolean isPlayerPiece(Piece p);
	
	/**
	 * Metodo que lee las acciones del raton
	 * @param row La fila 
	 * @param col la columna
	 * @param mouseButton El boton del raton que se ha pulsado
	 */
	protected abstract void mouseClicked(int row, int col, int mouseButton);
	
}
