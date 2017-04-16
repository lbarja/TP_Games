package es.ucm.fdi.tp.practica6;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Clase que simplifica el uso de socket
 */
public class Connection {
	
	//Atributos
	private Socket s;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	//Constructora
	public Connection(Socket s) throws IOException{
		this.s= s;
		this.out= new ObjectOutputStream( s.getOutputStream() );
		this.in= new ObjectInputStream( s.getInputStream() );
	}
	
	/**
	 * Enviar un objeto
	 * @param r El objeto que queremos enviar
	 * @throws IOException Salta si se interrumpe las operaciones de entrada/salida
	 */
	public void sendObject(Object r) {
		try {
			out.writeObject(r);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Error writeobject");
		}
		try {
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Error flush");
		}
		try {
			out.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Error reset");
		}
	}

	/**
	 * Recibir un objeto
	 * @return El objeto leido mediante los sockets
	 * @throws IOException Salta si se interrumpe las operaciones de entrada/salida
	 * @throws ClassNotFoundException Clase no encontrada
	 */
	public Object getObject() throws IOException, ClassNotFoundException{
		return in.readObject();
	}
	
	/**
	 * Cerrar el socket
	 * @throws IOException Salta si se interrumpe las operaciones de entrada/salida
	 */
	public void stop() throws IOException{
		s.close();
	}
}
