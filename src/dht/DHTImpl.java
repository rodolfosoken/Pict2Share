package dht;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Classe que implementa os métodos da DHT.
 */
public class DHTImpl implements DHT{
	/*
	 * Não é necessária uma lista com todos os nós. 
	 * A lista pode ser armazenada em uma lista estatica como um txt.
	 *
	 */
//private List<Node> nodes;
	
	private Node node;
	private Registry registry;
	
	public DHTImpl(Node node) {
		this.node = node;
	}
	
	@Override
	public Node join(String path) throws IOException, AlreadyBoundException, NotBoundException {		
		boolean isConnected = false;
		try(BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line = br.readLine();
		    String ipPortName[] = null;
		    while (line != null) {
		    	ipPortName = line.split(":");
		    	if(ipPortName.length>1)
		    		System.out.println("IP: " + ipPortName[0] + " Porta: "+ipPortName[1]);
		        line = br.readLine();
		        
		        try {
		        registry = LocateRegistry.getRegistry(ipPortName[0], Integer.parseInt(ipPortName[1]));
		        }catch(RemoteException e){
		        	registry = null;
		        }
		        if (registry!=null) {
		        	DHT dhtStub = (DHT) registry.lookup(ipPortName[2]);
    			    Message msgJoin = new Message(TypeMessage.JOIN);
    			    msgJoin.setSource(this.toString());
    			    dhtStub.procMessage(msgJoin);
    			    isConnected = true;
		        	break;
		        }	        
		    }
		    
		    //Não conseguiu conectar com nenhum nó no arquivo txt
		    //irá criar o nó inicial
		    if(isConnected == false) {
		    	registry.bind(node.initName, this);
		    }

		    
		}
		return node;
	}
	
	@Override
	public void leave() {	
	}
	
	@Override
	public boolean store(String key, String data) {
		return false;
	}
	
	@Override
	public void retrieve(String key) {
		
	}

	@Override
	public void procMessage(Message msg) {
		
	}

}
