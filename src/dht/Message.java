package dht;

import java.io.Serializable;



public class Message  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TypeMessage type;
	private String source;
	private String dest;
	private String args;
	
	public Message(TypeMessage type, String source, String dest, String args) {
		this.type = type;
		this.source = source;
		this.dest = dest;
		this.args = args;
	}
	
	public Message(TypeMessage type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public TypeMessage getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(TypeMessage type) {
		this.type = type;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the dest
	 */
	public String getDest() {
		return dest;
	}

	/**
	 * @param dest the dest to set
	 */
	public void setDest(String dest) {
		this.dest = dest;
	}

	/**
	 * @return the args
	 */
	public String getArgs() {
		return args;
	}

	/**
	 * @param args the args to set
	 */
	public void setArgs(String args) {
		this.args = args;
	}
		
	
	

}


