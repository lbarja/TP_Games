package es.ucm.fdi.tp.practica5.ttt;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.ttt.TicTacToeFactory;

/**
 * Clase que extiende los metodos de TicTacToeFactory para poder utilizar los entornos graficos
 */
public class TicTacToeFactoryExt extends TicTacToeFactory{

	private static final long serialVersionUID = 1L;
	
	//Constructora por defecto
	public TicTacToeFactoryExt(){
		super();
	}
	
	@Override
	public void createSwingView(Observable<GameObserver> game, Controller ctrl,
			Piece viewPiece, Player randPlayer, Player aiPlayer) {
		try{
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run(){
					new TicTacToeSwingView(game,ctrl,viewPiece,randPlayer,aiPlayer);
				}
			});
		}catch(InvocationTargetException | InterruptedException e){
			throw new GameError("Error");
		}
	}
}
