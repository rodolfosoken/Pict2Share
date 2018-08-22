package model;

import java.awt.Image;
import java.io.Serializable;

/**
 * Classe modelo de uma imagem.
 */
public class Picture implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String id;
	private String date;
	private Image img;
	
	/**
	 * 
	 * @return nome do arquivo de imagem.
	 */
	public String getName() {
		return name;
	}
	/**
	 * 
	 * @param name o nome do arquivo
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 
	 * @return id da imagem
	 */
	public String getId() {
		return id;
	}
	/**
	 * 
	 * @param id da imagem
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 *  Data em que a imagem foi salva
	 *  
	 * @return date data da imagem
	 */
	public String getDate() {
		return date;
	}
	
	/**
	 * Data em que a imagem foi salva
	 * @param date data da imagem
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * Imagem
	 * @return img
	 */
	public Image getImg() {
		return img;
	}
	/**
	 * 
	 * @param img
	 */
	public void setImg(Image img) {
		this.img = img;
	}	
	
	
	@Override
	public String toString() {
		return "Imagem: "+name+ " Id: "+ id;
	}
	
	@Override
	public boolean equals(Object other){
		// se ambos possuem o mesmo endereço, então são iguais
		if(this == other) return true;
		// se não pertencem a mesma classe então são diferentes
		if(!(other instanceof Picture)) return false; 
		//agora podemos converter para a classe
		Picture otherObj = (Picture)other; 
		// se possuem o mesmo código e valor, então são iguais
		return this.getId().equals(otherObj.getId()) && 
				this.getImg().equals(otherObj.getImg());
	}
	
	@Override
	public int hashCode() {
		return id.hashCode()^img.hashCode();
	}

}
