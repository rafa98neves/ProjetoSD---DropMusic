import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;

class MulticastConnection extends Thread {
    private String MULTICAST_ADDRESS = "224.3.2.1";
    private int PORT_SEND = 4322;
	private int PORT_RECEIVE = 4321;
	private String protocolo;
	private boolean Waiting = true;
	public MulticastConnection(String protocolo){
		super("Multicast Conection");
		this.protocolo = protocolo;
	}
	public String GetResponse(){
		this.start();
		try{
			this.join();
		}catch(Exception c){
			System.out.println("Join error in Multicaste Thread: " + c);
		}
		return protocolo;
	}
	
    public void run() {
		
        MulticastSocket socket = null;
        long counter = 0;
		byte[] buffer = protocolo.getBytes();				
		try{
			Waiting = true;
			socket = new MulticastSocket(PORT_RECEIVE);
			InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
			socket.joinGroup(group);
			
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT_SEND);
			socket.send(packet);
			
			buffer = new byte[256];
			
			packet = new DatagramPacket(buffer, buffer.length, group, PORT_RECEIVE);
			System.out.println("Bloqueado");
			socket.receive(packet);
			System.out.println("Desbloqueado");
			
			String received = new String(packet.getData(), 0, packet.getLength());
			protocolo = received;
			Waiting = false;
		}catch(Exception c){
			System.out.println("Exception in send/receive : " + c);
		}finally{
			socket.close();
		}
    }
}

public class ServidorRMI extends UnicastRemoteObject implements DropMusic_S_I{
	
	public ServidorRMI() throws RemoteException {
		super();
	}
	
	public void NewUser(String s) throws RemoteException {
		System.out.println("> " + s);
	}
	
	public void CheckNotifications(String username, DropMusic_C_I c) throws RemoteException{
		Map<String, String> protocolo = new HashMap<String, String>();
		protocolo.put("type", new String("notifications"));
		protocolo.put("username", username);
		//Procura na BD por notificacoes pendentes a este user
		try {
			c.Print("\nInformacao: Nenhuma notificacao pendente");
		} catch (Exception re) {
			System.out.println("Exception in Find(): " + re);
		} 
	}
	
	public boolean RegistUser(String username, String password) throws RemoteException{
		String protocolo = new String();
		protocolo = "type | registo ; username | " + username + " ; password | " + password;
        MulticastConnection N = new MulticastConnection(protocolo);
		protocolo = N.GetResponse();
		System.out.println(">>>" + protocolo);
		//Mandar protocolo e ver se o username esta a ser usado
		return false;
	}
	public boolean CheckUser(String name, String password,  DropMusic_C_I c) throws RemoteException{
		Map<String, String> protocolo = new HashMap<String, String>();
		protocolo.put("type", new String("login"));
		protocolo.put("username", name);
		protocolo.put("password", password);
		//Ver se o user esta na BD
		return true;
	}
	public void Find(String name, String tipo, DropMusic_C_I c) throws RemoteException{
		Map<String, String> protocolo = new HashMap<String, String>();
		protocolo.put("type", new String("search"));
		protocolo.put("nome", name);
		protocolo.put("from", tipo);
		//Receber da BD toda a informação relatica ao album, artista ou musica.
		String aux = new String("Temos esse " + tipo + " sim");
		try {
			c.Print(aux);
		} catch (Exception re) {
			System.out.println("Exception in Find(): " + re);
		} 
	}
	
	public void Write(String username, int pont, String critica, String album) throws RemoteException{
		Map<String, String> protocolo = new HashMap<String, String>();
		protocolo.put("type", new String("critic"));
		protocolo.put("username", username);
		protocolo.put("album", album);
		protocolo.put("score", Integer.toString(pont));
		protocolo.put("write", critica);		
		//find album
		//escreve critica
	}
	public void ShareMusic(String NAOSEI) throws RemoteException{
		
	}
	public void TransferMusic(String NAOSEI) throws RemoteException{
		
	}
	
	public void GivePriv(String username, DropMusic_C_I c) throws RemoteException{
		Map<String, String> protocolo = new HashMap<String, String>();
		protocolo.put("privileges", new String("critic"));
		protocolo.put("username", username);
		protocolo.put("editor", new String("true"));	
		String aux;
		//Receber da BD o ID do Client c com este username
		// c.ChangeUserToEditor()
		aux = "Previlegios dados com sucesso!";
		try {
			c.Print(aux);
		} catch (Exception re) {
			System.out.println("Exception in Find(): " + re);
		}
	}

	// ============================MAIN===========================

	public static void main(String args[]) {
		String a;
		
		System.getProperties().put("java.security.policy", "policy.all");
		System.setSecurityManager(new RMISecurityManager());
		
		try {
			
			ServidorRMI s = new ServidorRMI();
			Naming.bind("Drop", s);
			System.out.println("DropMusic RMI Server ready.");
			while (true) {
					
				}
		} catch (Exception re) {
			System.out.println("Exception in DropMusicImpl.main: " + re);
		}
	}
}