package dht;

public class Node {
	
	private String id;
	private String prev;
	private String next;
	private String value;
	
	
	
	
	
	public Node(String id, String value) {
		super();
		this.id = id;
		this.value = value;
	}



	public Node(String id, String prev, String next, String value) {
		super();
		this.id = id;
		this.prev = prev;
		this.next = next;
		this.value = value;
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
	 * @return the value
	 */
	public String getValue() {
		return value;
	}



	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}



	@Override
	public String toString() {
		return "Nó: "+id;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}

}
