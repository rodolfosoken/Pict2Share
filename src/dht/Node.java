package dht;

/***
 * Classe que representa um nó na rede.
 * <p> A cada novo cliente é criado um novo nó. 
 */
public class Node {
	
	/** O id do nó. */
	private String id;
	
	/** O nó anterior. */
	private String prev;
	
	/** O próximo nó */
	private String next;
	
	/** O dado armazenado no nó */
	private byte[] data;
	
	/** A dht */
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
	}

	/**
	 * Instantiates a new node.
	 *
	 * @param id the id
	 * @param data the data
	 */
	public Node(String id, byte[] data) {
		this.id = id;
		this.data = data;
	}

	/**
	 * Instantiates a new node.
	 *
	 * @param id the id
	 * @param prev the prev
	 * @param next the next
	 * @param data the data
	 */
	public Node(String id, String prev, String next, byte[] data) {
		this.id = id;
		this.prev = prev;
		this.next = next;
		this.data = data;
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
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}



	/**
	 * Gets the prev.
	 *
	 * @return the prev
	 */
	public String getPrev() {
		return prev;
	}



	/**
	 * Sets the prev.
	 *
	 * @param prev the prev to set
	 */
	public void setPrev(String prev) {
		this.prev = prev;
	}



	/**
	 * Gets the next.
	 *
	 * @return the next
	 */
	public String getNext() {
		return next;
	}



	/**
	 * Sets the next.
	 *
	 * @param next the next to set
	 */
	public void setNext(String next) {
		this.next = next;
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Sets the data.
	 *
	 * @param data the new data
	 */
	public void setData(byte[] data) {
		this.data = data;
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
		return "Nó: "+id;
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
