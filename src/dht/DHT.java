package dht;

import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.ConnectException;
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
	public String join(String path) throws IOException, RemoteException, ConnectException, AlreadyBoundException;
	
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
	public boolean store(String key, byte[] data)throws RemoteException, NotBoundException ;
	
	/**
	 * Realiza a busca na DHT e devolve (caso presente na DHT) 
	 * o dado armazenado pela operação store 
	 * @param key
	 * @see #store(String, String)
	 */
	public void retrieve(String key) throws RemoteException, NotBoundException;
	
	/**
	 * Processa a mensagem recebida;
	 * @param msg mensagem
	 * */
	public void procMessage(Message msg)throws RemoteException,NotBoundException;
	
	/***
	 * @return retorna o id do nó da dht
	 */
	public String getIdNode()throws RemoteException;
	
	/***
	 * @return retorna o nó da dht
	 */
	public String getNode()throws RemoteException;
	
	/**
	 * 
	 * @return o status da dht
	 * @throws RemoteException
	 */
	public String getStatus() throws RemoteException;
	/**
	 * @return the isConnected
	 */
	public boolean isConnected() throws RemoteException;
	/**
	 * @return the isInserted (Join_ok)
	 */
	public boolean isInserted() throws RemoteException;
	/**
	 * @param isStoped the isStoped to set
	 */
	public void setStoped(boolean isStoped)throws RemoteException ;
	/**
	 * @return the isStoped
	 */
	public boolean isStoped()throws RemoteException;
	/**
	 * 
	 * @return a dht anterior 
	 * @throws RemoteException
	 */
	public DHT getPrev()throws RemoteException;
	/**
	 * 
	 * @return a próxima dht
	 * @throws RemoteException
	 */
	public DHT getNext()throws RemoteException;
	
	/**
	 * Faz o registro de um nó que está em uma máquina remota
	 * 
	 * @param id
	 * @param stub
	 * @throws AccessException
	 * @throws RemoteException
	 * @throws AlreadyBoundException
	 */
	public void bindDHT(String id,DHT stub)throws AccessException, RemoteException, AlreadyBoundException;

	/**
	 *  
	 * @return result resultado da busca
	 * @throws RemoteException
	 */
	public byte[] getResult() throws RemoteException;

	/**
	 * 
	 * @param result
	 * @throws RemoteException
	 */
	public void setResult(byte[] result) throws RemoteException;
	
	/**
	 * 
	 * @return true se a imagem foi encontrada
	 * @throws RemoteException
	 */
	public boolean isFounded()throws RemoteException;

	/**
	 * 
	 * @param isFounded
	 * @throws RemoteException
	 */
	public void setFounded(boolean isFounded) throws RemoteException;
}
