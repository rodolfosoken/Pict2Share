package dht;

public class Node {
	
	private String id;
	private String prev;
	private String next;
	private String data;
	
	
	
	
	
	public Node(String id, String data) {
		super();
		this.id = id;
		this.data = data;
	}



	public Node(String id, String prev, String next, String data) {
		super();
		this.id = id;
		this.prev = prev;
		this.next = next;
		this.data = data;
	}



	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}



	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}



	/**
	 * @return the prev
	 */
	public String getPrev() {
		return prev;
	}



	/**
	 * @param prev the prev to set
	 */
	public void setPrev(String prev) {
		this.prev = prev;
	}



	/**
	 * @return the next
	 */
	public String getNext() {
		return next;
	}



	/**
	 * @param next the next to set
	 */
	public void setNext(String next) {
		this.next = next;
	}



	/**
	 * @return the Data
	 */
	public String getData() {
		return data;
	}



	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
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
