package dht;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
	
	public DHTImpl(Node node) {
		this.node = node;
	}
	
	@Override
	public Node join(String path) throws IOException {		
		try(BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line = br.readLine();

		    while (line != null) {
		    	String ipPort[] = line.split(":");
		    	if(ipPort.length>1)
		    		System.out.println("IP: " + ipPort[0] + " Porta: "+ipPort[1]);
		        line = br.readLine();
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

}
