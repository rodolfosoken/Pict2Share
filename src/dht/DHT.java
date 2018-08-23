package dht;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface para a DHT
 */
public interface DHT extends Remote{
	
	/**
	 * Operação utilizada para se conectar à DHT.
	 * <p> Será recebida uma lista contendo os IP's iniciais e depois de verificado,
	 * caso nenhum IP esteja ativo, será criada uma nova DHT.
	 * 
	 * <p> O arquvivo texto inicial deve conter um IP em cada linha com a porta separada por ":".
	 * 
	 * @param path caminho para o arquivo texto contendo os IP's iniciais.
	 * @return node se a operação foi realizada com sucesso.
	 * @throws IOException
	 * @throws AlreadyBoundException 
	 */
	public Node join(String path) throws IOException, RemoteException, AlreadyBoundException,NotBoundException;
	
	/**
	 * Operação que realiza a desconexão da rede.
	 * <p> Realiza a atualização das referências dos nós vizinhos ao sair,
	 * <br>mantendo a consistência do anel da DHT.
	 * <p> O conteúdo será transferido para o nó sucessor.
	 */
	public void leave() throws RemoteException;
	
	/**
	 * Armazena um dado na DHT utilizando a chave.
	 * <p>O dado será armazenado em next(hash(key)).
	 * @param key
	 * @param data
	 * @return
	 */
	public boolean store(String key, String data) ;
	
	/**
	 * Realiza a busca na DHT e devolve (caso presente na DHT) 
	 * o dado armazenado pela operação store 
	 * @param key
	 * @see #store(String, String)
	 */
	public void retrieve(String key) ;
	
	/**
	 * Processar a mensagem recebida;
	 * @param msg mensagem
	 * */
	public void procMessage(Message msg);
}
