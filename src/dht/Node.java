package dht;

import java.util.HashMap;
import java.util.Map;

// TODO: Auto-generated Javadoc
/***
 * Classe que representa um nó na rede.
 * <p> A cada novo cliente é criado um novo nó. 
 */
public class Node {
	
	/** O id do nó. */
	private String id;
	
	/** O nó anterior. */
	private DHT prev;
	
	/**  O próximo nó. */
	private DHT next;
	
	/**  O dado armazenado no nó. */
	private Map<String,byte[]> data;
	
	/** Ip do no**. */
	private String ip;
	
	/** Porta*. */
	private String port;
	
	/**  A dht. */
	//implementação da dht
	private DHT dht;
	

	/**
	 * Instantiates a new node.
	 *
	 * @param id the id
	 */
	public Node(String id) {
		this.id = id;
		this.dht = new DHTImpl(this);
		data = new HashMap<>();
	}


	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}



	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}



	/**
	 * Gets the prev.
	 *
	 * @return the prev
	 */
	public DHT getPrev() {
		return prev;
	}



	/**
	 * Sets the prev.
	 *
	 * @param prev the new prev
	 */
	public void setPrev(DHT prev) {
		this.prev = prev;
	}



	/**
	 * Gets the next.
	 *
	 * @return the next
	 */
	public DHT getNext() {
		return next;
	}



	/**
	 * Sets the next.
	 *
	 * @param next the new next
	 */
	public void setNext(DHT next) {
		this.next = next;
	}



	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public Map<String, byte[]> getData() {
		return data;
	}



	/**
	 * Sets the data.
	 *
	 * @param data the data
	 */
	public void setData(Map<String, byte[]> data) {
		this.data = data;
	}



	/**
	 * Gets the ip.
	 *
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}



	/**
	 * Sets the ip.
	 *
	 * @param ip the new ip
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}



	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public String getPort() {
		return port;
	}



	/**
	 * Sets the port.
	 *
	 * @param port the new port
	 */
	public void setPort(String port) {
		this.port = port;
	}



	/**
	 * Gets the dht.
	 *
	 * @return the dht
	 */
	public DHT getDht() {
		return dht;
	}



	/**
	 * Sets the dht.
	 *
	 * @param dht the new dht
	 */
	public void setDht(DHT dht) {
		this.dht = dht;
	}




	@Override
	public String toString() {
		return this.ip+";"+this.port+";"+this.id;
	}

	@Override
	public boolean equals(Object other){
		// se ambos possuem o mesmo endereço, então são iguais
		if(this == other) return true;
		// se não pertencem a mesma classe então são diferentes
		if(!(other instanceof Node)) return false; 
		//agora podemos converter para a classe
		Node otherObj = (Node)other; 
		// se possuem o mesmo código e valor, então são iguais
		return this.getId().equals(otherObj.getId()) && 
				this.getData().equals(otherObj.getData());
	}
	


	@Override
	public int hashCode() {
		return id.hashCode();
	}
	

}
