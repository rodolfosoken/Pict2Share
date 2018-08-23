package dht;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

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
	public Node join(String path) throws IOException, ConnectException, AlreadyBoundException {		
		boolean isConnected = false;
		String firstLineFile[] = null;
		try(BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line = br.readLine();
		    String ipPortName[] = null;
		    while (line != null) {
		    	ipPortName = line.split(":");
		    	if(ipPortName.length>1)
//		    		System.out.println("IP: " + ipPortName[0] + " Porta: "+ipPortName[1]+" Nome: "+ipPortName[2]);
		        line = br.readLine();
		    	
		    	//armazena a primeira linha do arquivo
		    	//caso seja necessário iniciar uma nova dht
		        if(firstLineFile == null && ipPortName != null)
		        	firstLineFile = ipPortName;
		        //tenta se conectar ao serviço de nomes do nó inicial
		        //caso o nó não seja encontrado será lançada uma exceção
		        try {
		        	registry = LocateRegistry.getRegistry(ipPortName[0], Integer.parseInt(ipPortName[1]));
		        	DHT dhtStub = (DHT) registry.lookup(ipPortName[2]);
	        		System.out.println("Nó : "+dhtStub.getIdNode()+" ATIVO");
	        		Message msgJoin = new Message(TypeMessage.JOIN);
	        		msgJoin.setSource(this.toString());
	        		dhtStub.procMessage(msgJoin);
	        		isConnected = true;
	        		break;
		        }catch(NotBoundException | RemoteException e){
		        }
		    }
		    
		    //Não conseguiu conectar com nenhum nó no arquivo txt
		    //irá criar o nó inicial
		    if(isConnected == false) {
		    	registry = LocateRegistry.getRegistry();
		    	node.setIp(firstLineFile[0]);
		    	node.setPort(firstLineFile[1]);
		    	node.setId(firstLineFile[2]);
		    	DHT stub = (DHT) UnicastRemoteObject.exportObject(this, 0);
		    	registry.bind(node.getId(), stub);
		    	System.out.println("Nova DHT Inciada: Conectado!");
		    }

		    
		}
		return node;
	}
	
	@Override
	public void leave() {
		//se este for o nó inicial será preciso desregistrar o nome no RMI
		try {
			registry = LocateRegistry.getRegistry();
			UnicastRemoteObject.unexportObject(this, true);
			registry.unbind(node.getId());
		} catch (RemoteException e) {
		} catch (NotBoundException e) {
		}
    	
	}
	
	@Override
	public boolean store(String key, byte[] data) {
		node.getData().put(key, data);
		return true;
	}
	
	@Override
	public void retrieve(String key) {
		
	}

	@Override
	public void procMessage(Message msg) {
		switch (msg.getType()) {
		case JOIN:
			System.out.println("join");
			break;
		case JOIN_OK:
			System.out.println("join_ok");
			break;
		case NEW_NODE:
			System.out.println("new_node");
			break;
		case STORE:
			System.out.println("store");
			String args[] = msg.getArgs().split(" ");
			for (int i = 0; i < Integer.parseInt(args[1]); i++) {
				
			}
			
			break;
		case LEAVE:
			System.out.println("leave");
			break;
		case RETRIEVE:
			System.out.println("retrieve");
			break;
		case NODE_GONE:
			System.out.println("node_gone");
			break;
		case OK:
			System.out.println("ok");
			break;
		case NOT_FOUND:
			System.out.println("not_found");
			break;
		case TRANSFER:
			System.out.println("transfer");
			break;

		default:
			break;
		}
		
	}
	
	@Override
	public String getIdNode() throws RemoteException {
		return node.getId();
	}

}
