package dht;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DHT {
	/*
	 * Não é necessária uma lista com todos os nós. 
	 * A lista pode ser armazenada em uma lista estatica como um txt.
	 *
	 */
//private List<Node> nodes;
	
	/**
	 * Operação utilizada para se conectar à DHT.
	 * <p> Será recebida uma lista contendo os IP's iniciais e depois de verificado,
	 * caso nenhum IP esteja ativo, será criada uma nova DHT.
	 * 
	 * <p> O arquvivo texto inicial deve conter um IP em cada linha com a porta separada por ":".
	 * 
	 * @param path caminho para o arquivo texto contendo os IP's iniciais.
	 * @return boolean verdadeiro se a operação foi realizada com sucesso.
	 * @throws IOException
	 */
	public boolean join(String path, Node node) throws IOException {
		try(BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line = br.readLine();

		    while (line != null) {
		    	String ipPort[] = line.split(":");
		    	if(ipPort.length>1)
		    		System.out.println("IP: " + ipPort[0] + " Porta: "+ipPort[1]);
		        line = br.readLine();
		    }
		}
		return true;
	}
	
	/**
	 * Operação que realiza a desconexão da rede.
	 * <p> Realiza a atualização das referências dos nós vizinhos ao sair,
	 * <br>mantendo a consistência do anel da DHT.
	 * <p> O conteúdo será transferido para o nó sucessor.
	 */
	public void leave() {	
	}
	
	/**
	 * Armazena um dado na DHT utilizando a chave.
	 * <p>O dado será armazenado em next(hash(key)).
	 * @param key
	 * @param data
	 * @return
	 */
	public boolean store(String key, String data) {
		return false;
	}
	
	/**
	 * Realiza a busca na DHT e devolve (caso presente na DHT) 
	 * o dado armazenado pela operação store 
	 * @param key
	 * @see #store(String, String)
	 */
	public void retrieve(String key) {
		
	}

}
