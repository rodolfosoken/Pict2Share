package dht;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Classe que implementa os métodos da DHT.
 */
public class DHTImpl implements DHT {
	/*
	 * Não é necessária uma lista com todos os nós. A lista pode ser armazenada em
	 * uma lista estatica como um txt.
	 *
	 */
	// private List<Node> nodes;

	private Node node;
	private Registry registryRemote;
	private Registry registryLocal;
	private String status;
	private byte[] result;
	private boolean isConnected;
	private boolean isInserted;
	private boolean isStoped;
	private boolean isRemote;
	private boolean isFounded;
	private boolean isNotFounded;
	private boolean isSuperNode;
	private DHT dhtStub;

	public DHTImpl(Node node) {
		this.node = node;
		status = "Inicializando DHT...";
		isConnected = false;
		isInserted = false;
		isStoped = false;
		isRemote = false;
		result = null;
		isFounded = false;
		isNotFounded = false;
		isSuperNode =false;
	}

	

	@Override
	public String join(String path) throws IOException, ConnectException, AlreadyBoundException {
		isConnected = false;
		isInserted = false;
		isStoped = false;
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line = br.readLine();
			String ipPortName[] = null;
			while (line != null) {
				ipPortName = line.split(":");
				if (ipPortName.length >= 3) {
					// System.out.println("IP: " + ipPortName[0] + " Porta: "+ipPortName[1]+" Nome:
					// "+ipPortName[2]);
					line = br.readLine();
					// tenta se conectar ao serviço de nomes do nó inicial
					// caso o nó não seja encontrado será lançada uma exceção
					try {
						status = "IP:" + ipPortName[0] + " Conectando...";
						System.out.println(status);
						registryRemote = LocateRegistry.getRegistry(ipPortName[0], Integer.parseInt(ipPortName[1]));
						dhtStub = (DHT) registryRemote.lookup(ipPortName[2]);
						status = node.getId() + " : " + dhtStub.getIdNode() + " ATIVO | " + ipPortName[0] + ":"
								+ ipPortName[1];
						System.out.println(status);
						isConnected = true;
						if (!ipPortName[0].equals("127.0.0.1"))
							isRemote = true;
						else
							isRemote = false;

						// Adiciona os atributos no nó
						// e realiza o registro do nó no RMI
						node.setIp(ipPortName[0]);
						DHT stub = (DHT) UnicastRemoteObject.exportObject(this, Integer.parseInt(node.getPort()));
						registryLocal = LocateRegistry.getRegistry();
						registryLocal.bind(node.getId(), stub);
						if (isRemote)
							dhtStub.bindDHT(node.getId(), stub);

						// cria e envia a mensagem de join
						status = "Conectado a: " + ipPortName[0] + " | Enviando mensagem join...";
						isInserted = false;
						System.out.println(status);
						Message msgJoin = new Message(TypeMessage.JOIN);
						msgJoin.setSource(node.toString());
						dhtStub.procMessage(msgJoin);
						break;
					} catch (RemoteException e) {
						if (!(e instanceof ConnectException)) {
							status = e.getLocalizedMessage();
							System.out.println(node.getId() + ": " + e.getLocalizedMessage());
							e.printStackTrace();
						} else
							System.out.println(e.getMessage());
					} catch (NotBoundException e) {

					}
				} else {
					new IOException();
				}
			}

			// Não conseguiu conectar com nenhum nó no arquivo txt
			// irá criar o nó inicial
			if (isConnected == false) {
				registryLocal = LocateRegistry.getRegistry();
				DHT stub = (DHT) UnicastRemoteObject.exportObject(this, 0);
				registryLocal.bind(node.getId(), stub);
				status = "Nova DHT Iniciada: Conectado! | " + node.getIp();
				System.out.println(status);
				isConnected = true;
				isInserted = true;
				isStoped = false;
				isSuperNode = true;
			}

			if (!isRemote)
				registryRemote = registryLocal;

		}
		return node.getIp() + ":" + node.getPort();
	}

	@Override
	public void bindDHT(String id, DHT stub) throws AccessException, RemoteException, AlreadyBoundException {
		registryLocal.bind(id, stub);
	}
	
	@Override
	public void unbindDHT(String id) throws RemoteException, NotBoundException {
		registryLocal.unbind(id);
	}

	@Override
	public void leave() throws RemoteException, NotBoundException {

		Message msgLeave = new Message(TypeMessage.LEAVE);
		msgLeave.setSource(node.toString());

		Message msgGone = new Message(TypeMessage.NODE_GONE);
		msgGone.setSource(node.toString());

		if (getPrev() != null && getNext() != null) {
			msgLeave.setArgs(getPrev().getNode().toString());
			System.out.println(node.getId() + ": Enviando LEAVE para..." + getNext().getIdNode());
			getNext().procMessage(msgLeave);

			for (Entry<String, byte[]> data : node.getData().entrySet()) {
				Message msgTransfer = new Message(TypeMessage.TRANSFER);
				msgTransfer.setSource(node.toString());
				msgTransfer.setArgs(data.getKey());
				msgTransfer.setData(data.getValue());
				System.out.println(node.getId() + ": Enviando TRANSFER para..." + getNext().getIdNode());
				getNext().procMessage(msgTransfer);
			}
			node.setData(new HashMap<>());
			msgGone.setArgs(getNext().getNode().toString());
			System.out.println(node.getId() + ": Enviando NODE_GONE para..." + getPrev().getIdNode());
			getPrev().procMessage(msgGone);

		}
		
		UnicastRemoteObject.unexportObject(this, true);
		if (isRemote) 
			dhtStub.unbindDHT(node.getId());
		registryLocal.unbind(node.getId());
		isConnected = false;
		isStoped = true;
		isSuperNode=false;
	}

	@Override
	public boolean store(String key, byte[] data) throws RemoteException, NotBoundException {
		BigInteger keyStore = new BigInteger(key, 16), thisNode = new BigInteger(node.getId(), 16);
		Message msg = new Message(TypeMessage.STORE);
		msg.setDest(key);
		msg.setSource(node.toString());
		msg.setData(data);
		System.out.println(node.getId() + ": Enviando STORE.");
		if (keyStore.compareTo(thisNode) < 0 && getPrev() != null)
			getPrev().procMessage(msg);
		else if (getNext() != null)
			getNext().procMessage(msg);
		else
			procMessage(msg);
		return true;
	}

	@Override
	public void retrieve(String key) throws RemoteException, NotBoundException {
		isFounded = false;
		isNotFounded = false;
		BigInteger keyStore = new BigInteger(key, 16), thisNode = new BigInteger(node.getId(), 16);
		Message msg = new Message(TypeMessage.RETRIEVE);
		msg.setDest(key);
		msg.setSource(node.toString());
		System.out.println(node.getId() + ": Enviando RETRIEVE.");
		if (keyStore.compareTo(thisNode) < 0 && getPrev() != null)
			getPrev().procMessage(msg);
		else if (getNext() != null)
			getNext().procMessage(msg);
		else
			procMessage(msg);
	}

	@Override
	public void procMessage(Message msg) throws RemoteException, NotBoundException {
		switch (msg.getType()) {
		case JOIN:
			System.out.println(node.getId() + " : JOIN recebido.");
			BigInteger src = new BigInteger(msg.getSource().split(";")[2], 16);
			BigInteger idPrev = new BigInteger("0", 16), thisNode = new BigInteger(node.getId(), 16);
			if (getPrev() != null)
				idPrev = new BigInteger(getPrev().getIdNode(), 16);
			if (idPrev.compareTo(src) < 0 || (thisNode.compareTo(idPrev) < 0)) { // se o anterior for maior que este nó,
				// então este é o sucessor do último,
				// e deve-se proceder com a inserção
				if (src.compareTo(thisNode) <= 0 || getNext() == null
						|| (thisNode.compareTo(idPrev) < 0 && src.compareTo(idPrev) > 0)) {
					System.out.println(node.getId() + " : JOIN Adicionando: " + msg.getSource().split(";")[2] + "...");
					Message resp = new Message(TypeMessage.JOIN_OK);
					resp.setSource(node.toString());
					if (node.getPrev() != null)
						resp.setArgs(getPrev().getNode().toString());
					else
						resp.setArgs(getNode().toString());
					DHT nodeSrc = (DHT) registryRemote.lookup(msg.getSource().split(";")[2]);
					node.setPrev(nodeSrc);
					if (node.getNext() == null)
						node.setNext(nodeSrc);
					nodeSrc.procMessage(resp);
				} else {
					// se o src (nó ingressante) é maior que este
					// nó, então passar para o próximo nó
					System.out.println(node.getId() + " : JOIN repassado ao próximo.");
					this.node.getNext().procMessage(msg);
				}

			} else {
				// se o src (nó ingressante) é menor que o antecessor
				// deste nó, então passar ao anterior
				System.out.println(node.getId() + " : JOIN repassado ao anterior.");
				this.node.getPrev().procMessage(msg);
			}
			break;
		case JOIN_OK:
			System.out.println(node.getId() + ": JOIN_OK recebido.");
			DHT nodeSrc1 = (DHT) registryRemote.lookup(msg.getSource().split(";")[2]);
			node.setNext(nodeSrc1);
			if (msg.getArgs() != null) {
				DHT nodePrev1 = (DHT) registryRemote.lookup(msg.getArgs().split(";")[2]);
				if (nodePrev1 != null) {
					node.setPrev(nodePrev1);
					Message respNewNode = new Message(TypeMessage.NEW_NODE);
					respNewNode.setSource(node.toString());
					status = node.getId() + ": Enviando mensagem NEW_NODE para " + getPrev().getIdNode();
					System.out.println(status);
					node.getPrev().procMessage(respNewNode);
				}
			}
			status = "Conectado, nó inserido na DHT.";
			isConnected = true;
			isInserted = true;
			break;
		case NEW_NODE:
			System.out.println(node.getId() + ": NEW_NODE recebida.");
			DHT nodeSrc2 = (DHT) registryRemote.lookup(msg.getSource().split(";")[2]);
			node.setNext(nodeSrc2);
			System.out.println(node.getId() + ": next : " + node.getNext().getIdNode());
			break;
		case STORE:
			System.out.println(node.getId() + ": STORE recebida.");
			BigInteger keyStore3 = new BigInteger(msg.getDest(), 16), idCurrentNode3 = new BigInteger(node.getId(), 16),
					idPrev3 = new BigInteger("0", 16);
			if (getPrev() != null)
				idPrev3 = new BigInteger(getPrev().getIdNode(), 16);
			if (idPrev3.compareTo(keyStore3) < 0 || (idCurrentNode3.compareTo(idPrev3) < 0)) { // se o anterior for
																								// maior que este nó,
				// então este é o sucessor do último,
				// e deve-se proceder com a inserção
				if (keyStore3.compareTo(idCurrentNode3) <= 0 || getNext() == null
						|| (idCurrentNode3.compareTo(idPrev3) < 0 && keyStore3.compareTo(idPrev3) > 0)) {
					System.out.println(
							node.getId() + " : STORE salvando img de: " + msg.getSource().split(";")[2] + "...");
					node.getData().put(msg.getDest(), msg.getData());
				} else {
					// se o src (nó ingressante) é maior que este
					// nó, então passar para o próximo nó
					System.out.println(node.getId() + " : STORE repassado ao próximo.");
					this.node.getNext().procMessage(msg);
				}

			} else {
				// se o src (nó ingressante) é menor que o antecessor
				// deste nó, então passar ao anterior
				System.out.println(node.getId() + " : STORE repassado ao anterior.");
				this.node.getPrev().procMessage(msg);
			}

			break;
		case LEAVE:
			System.out.println(node.getId() + ": LEAVE recebida.");
			DHT st = (DHT) registryRemote.lookup(msg.getArgs().split(";")[2]);
			node.setPrev(st);
			System.out.println(node.getId() + ": prev.: " + getPrev().getIdNode());

			break;
		case RETRIEVE:
			System.out.println(node.getId() + ": RETRIEVE recebida.");
			BigInteger keyStore5 = new BigInteger(msg.getDest(), 16), idCurrentNode5 = new BigInteger(node.getId(), 16),
					idPrev5 = new BigInteger("0");
			if (getPrev() != null)
				idPrev5 = new BigInteger(getPrev().getIdNode(), 16);
			if (idPrev5.compareTo(keyStore5) < 0 || (idCurrentNode5.compareTo(idPrev5) < 0)) { // se o anterior for
																								// maior que este nó,
				// então este é o sucessor do último,
				// e deve-se proceder com a inserção
				if (keyStore5.compareTo(idCurrentNode5) <= 0 || getNext() == null
						|| (idCurrentNode5.compareTo(idPrev5) < 0 && keyStore5.compareTo(idPrev5) > 0)) {

					// procura a imagem dentro do nó
					byte[] result = node.getData().get(msg.getDest());
					DHT respStub = (DHT) registryRemote.lookup(msg.getSource().split(";")[2]);

					if (result != null) {// se a imagem foi encontrada responder ok
						System.out.println(node.getId() + " : RETRIEVE img encontrada.");
						Message okResp = new Message(TypeMessage.OK);
						okResp.setSource(node.toString());
						okResp.setData(result);
						respStub.procMessage(okResp);
					} else { // se não foi encontrada então Not_found
						System.out.println(node.getId() + " : RETRIEVE: NOT_FOUND.");
						Message not = new Message(TypeMessage.NOT_FOUND);
						respStub.procMessage(not);
					}

				} else {
					// se o src (nó ingressante) é maior que este
					// nó, então passar para o próximo nó
					System.out.println(node.getId() + " : STORE repassado ao próximo.");
					this.node.getNext().procMessage(msg);
				}

			} else {
				// se o src (nó ingressante) é menor que o antecessor
				// deste nó, então passar ao anterior
				System.out.println(node.getId() + " : STORE repassado ao anterior.");
				this.node.getPrev().procMessage(msg);
			}
			break;
		case NODE_GONE:
			System.out.println(node.getId() + ": NODE_GONE recebida.");
			if (getNext() != null) {
				DHT st2 = (DHT) registryRemote.lookup(msg.getArgs().split(";")[2]);
				node.setNext(st2);
				System.out.println(node.getId() + ": next atualizado para: " + getNext().getIdNode());
			}
			break;
		case OK:
			System.out.println(node.getId() + ": OK recebida.");
			status = "Imagem encontrada.";
			result = msg.getData();
			isFounded = true;
			isNotFounded = false;
			break;
		case NOT_FOUND:
			System.out.println(node.getId() + ": NOT_FOUND recebida.");
			isFounded = false;
			isNotFounded = true;
			break;
		case TRANSFER:
			System.out.println(node.getId() + ": TRANSFER recebida.");
			node.getData().put(msg.getArgs(), msg.getData());
			System.out.println(node.getId() + ": " + msg.getArgs() + " armazenada em " + node.getId());
			break;

		default:
			break;
		}

	}

	@Override
	public String getIdNode() throws RemoteException {
		return node.getId();
	}

	/**
	 * @return the node
	 */
	public String getNode() {
		return node.toString();
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the isConnected
	 */
	public boolean isConnected() {
		return isConnected;
	}

	/**
	 * @return the isInserted
	 */
	public boolean isInserted() {
		return isInserted;
	}

	/**
	 * @return the isStoped
	 */
	public boolean isStoped() {
		return isStoped;
	}

	/**
	 * @param isStoped
	 *            the isStoped to set
	 */
	public void setStoped(boolean isStoped) {
		this.isStoped = isStoped;
	}

	@Override
	public DHT getNext() throws RemoteException {
		return node.getNext();
	}

	@Override
	public DHT getPrev() throws RemoteException {
		return node.getPrev();
	}

	@Override
	public byte[] getResult() {
		return result;
	}

	@Override
	public void setResult(byte[] result) {
		this.result = result;
	}

	@Override
	public boolean isFounded() {
		return isFounded;
	}

	@Override
	public void setFounded(boolean isFounded) {
		this.isFounded = isFounded;
	}

	@Override
	public boolean isNotFounded() {
		return isNotFounded;
	}

	@Override
	public void setNotFounded(boolean isNotFounded) {
		this.isNotFounded = isNotFounded;
	}


	@Override
	public boolean isSuperNode() {
		return isSuperNode;
	}


	@Override
	public void setSuperNode(boolean isSuperNode) {
		this.isSuperNode = isSuperNode;
	}
	
	

}
