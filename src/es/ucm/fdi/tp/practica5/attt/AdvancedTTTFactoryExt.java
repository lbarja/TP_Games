package es.ucm.fdi.tp.practica5.attt;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.attt.AdvancedTTTFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * Clase que extiende los metodos de AdvancedTTTFactory para poder utilizar los entornos graficos
 */
public class AdvancedTTTFactoryExt extends AdvancedTTTFactory{

	private static final long serialVersionUID = 1L;

	//Constructora por defecto
	public AdvancedTTTFactoryExt (){
		super();
	}
	
	@Override
	public void createSwingView(Observable<GameObserver> game, Controller ctrl,
			Piece viewPiece, Player randPlayer, Player aiPlayer) {
		try{
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run(){
					new AdvancedTTTSwingView(game,ctrl,viewPiece,randPlayer,aiPlayer);
				}
			});
		}catch(InvocationTargetException | InterruptedException e){
			throw new GameError("Error");
		}
	}
}
