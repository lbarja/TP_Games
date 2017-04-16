package es.ucm.fdi.tp.practica5.ataxx;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.practica4.ataxx.AtaxxFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * Clase que extiende los metodos de AtaxxFactory para poder utilizar los entornos graficos
 */
public class AtaxxFactoryExt extends AtaxxFactory{

	private static final long serialVersionUID = 1L;

	//Constructora por defecto
	public AtaxxFactoryExt(int obstacles){
		super(obstacles);
	}
	
	//Constructora con argumentos de tablero rectangular y obstaculos
	public AtaxxFactoryExt(int rows, int cols, int obstacles) {
		super(rows, cols, obstacles);
	}

	@Override
	public void createSwingView(final Observable<GameObserver> game, Controller ctrl,
			Piece viewPiece, Player randPlayer, Player aiPlayer) {
		try{
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run(){
					new AtaxxSwingView(game,ctrl,viewPiece,randPlayer,aiPlayer);
				}
			});
		}catch(InvocationTargetException | InterruptedException e){
			throw new GameError("Error");
		}
	}
}
