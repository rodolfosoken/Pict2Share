package dht;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

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
	private Registry registry;
	private String status;
	private boolean isConnected;
	private boolean isInserted;
	private boolean isStoped;

	public DHTImpl(Node node) {
		this.node = node;
		status = "Inicializando DHT...";
		isConnected = false;
		isInserted = false;
		isStoped = false;
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
						registry = LocateRegistry.getRegistry(ipPortName[0], Integer.parseInt(ipPortName[1]));
						DHT dhtStub = (DHT) registry.lookup(ipPortName[2]);
						status = "No : " + dhtStub.getIdNode() + " ATIVO | " + ipPortName[0] + ":" + ipPortName[1];
						System.out.println(status);
						isConnected = true;

						// Adiciona os atributos no nó
						// e realiza o registro do nó no RMI
						node.setIp(ipPortName[0]);
						DHT stub = (DHT) UnicastRemoteObject.exportObject(this, Integer.parseInt(node.getPort()));
						registry.bind(node.getId(), stub);

						// cria e envia a mensagem de join
						status = "Conectado a: " + ipPortName[0] + " | Enviando mensagem join...";
						isInserted = false;
						System.out.println(status);
						Message msgJoin = new Message(TypeMessage.JOIN);
						msgJoin.setSource(node.toString());
						dhtStub.procMessage(msgJoin);
						break;
					} catch (RemoteException e) {
						status = e.getLocalizedMessage();
						System.out.println(node.getId() + ": " + e.getLocalizedMessage());
						e.printStackTrace();
					} catch (NotBoundException e) {

					}
				} else {
					new IOException();
				}
			}

			// Não conseguiu conectar com nenhum nó no arquivo txt
			// irá criar o nó inicial
			if (isConnected == false) {
				registry = LocateRegistry.getRegistry();
				node.setIp("127.0.0.1");
				node.setPort("1099");
				DHT stub = (DHT) UnicastRemoteObject.exportObject(this, 0);
				registry.bind(node.getId(), stub);
				status = "Nova DHT Iniciada: Conectado! | " + node.getIp();
				System.out.println(status);
				isConnected = true;
				isInserted = true;
				isStoped = false;
			}

		}
		return node.getIp() + ":" + node.getPort();
	}

	@Override
	public void leave() {
		// se este for o nó inicial será preciso desregistrar o nome no RMI
		try {
			registry = LocateRegistry.getRegistry();
			UnicastRemoteObject.unexportObject(this, true);
			registry.unbind(node.getId());
			isConnected = false;
			isStoped = true;
		} catch (RemoteException | NotBoundException e) {
			// se não há objetos registrados,
			// a aplicação será encerrada
		}
	}

	@Override
	public boolean store(String key, byte[] data) {
		node.getData().put(key, data);
		return true;
	}

	@Override
	public void retrieve(String key) {

	}

	@Override
	public void procMessage(Message msg) throws RemoteException, NotBoundException {
		switch (msg.getType()) {
		case JOIN:
			System.out.println(node.getId() + " : JOIN recebido.");
			int src = Integer.parseInt(msg.getSource().split(";")[2]);
			int idPrev = -1, thisNode = Integer.parseInt(node.getId());
			if (node.getPrev() != null)
				idPrev = Integer.parseInt(node.getPrev().getIdNode());
			if (idPrev < src ) {
				if(src <= thisNode || (node.getNext()==null)) {
					Message resp = new Message(TypeMessage.JOIN_OK);
					resp.setSource(node.toString());
					if (node.getPrev() != null)
						resp.setArgs(node.getPrev().toString());
					registry = LocateRegistry.getRegistry(Integer.parseInt(node.getPort()));
					DHT nodeSrc = (DHT) registry.lookup(String.valueOf(src));
					node.setPrev(nodeSrc);
					nodeSrc.procMessage(resp);
				}else {
					// se o src (nó ingressante) é maior que este
					// nó, então passar para o próximo nó
					this.node.getNext().procMessage(msg);
					System.out.println(node.getId() + " : JOIN repassado ao próximo.");		
				}
			} else {
				// se o src (nó ingressante) é menor que o antecessor
				// deste nó, então passar ao anterior
				this.node.getPrev().procMessage(msg);
				System.out.println(node.getId() + " : JOIN repassado ao anterior.");
			}
			break;
		case JOIN_OK:
			System.out.println(node.getId() + ": JOIN_OK");
			status = "Conectado, nó inserido na DHT.";
			isConnected = true;
			isInserted = true;

			break;
		case NEW_NODE:
			System.out.println("new_node");
			break;
		case STORE:
			System.out.println("store");
			break;
		case LEAVE:
			System.out.println("leave");
			break;
		case RETRIEVE:
			System.out.println("retrieve");
			break;
		case NODE_GONE:
			System.out.println("node_gone");
			break;
		case OK:
			System.out.println("ok");
			break;
		case NOT_FOUND:
			System.out.println("not_found");
			break;
		case TRANSFER:
			System.out.println("transfer");
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
	public Node getNode() {
		return node;
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

}
