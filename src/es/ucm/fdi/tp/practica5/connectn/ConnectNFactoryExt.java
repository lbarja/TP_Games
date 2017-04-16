package es.ucm.fdi.tp.practica5.connectn;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.connectn.ConnectNFactory;

/**
 * Clase que extiende los metodos de ConnectNFactory para poder utilizar los entornos graficos
 */
public class ConnectNFactoryExt extends ConnectNFactory{

	private static final long serialVersionUID = 1L;
	
	//Constructora por defecto
	public ConnectNFactoryExt(){
		super();
	}
	
	//Constructora con el argumento de la dimension del tablero cuadrado
	public ConnectNFactoryExt(int dim){
		super(dim);
	}
	
	@Override
	public void createSwingView(final Observable<GameObserver> g, final Controller c, final Piece viewPiece,
			Player random, Player ai) {
		try{
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run(){				
					new ConnectNSwingView(g,c,viewPiece,random,ai);
				}
			});
		}catch(InvocationTargetException | InterruptedException e){
			throw new GameError("Error");
		}
	}
}
