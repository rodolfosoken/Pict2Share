package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.server.ExportException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import dht.Node;
import gui.ViewAlbum;
import model.Picture;

/**
 * Classe que controla a instância view do álbum.
 */
public class ControllerAlbum {

	private ViewAlbum view;
	private Picture picture;
	private Node node;

	public ControllerAlbum(ViewAlbum view, Node node) {
		this.view = view;
		this.node = node;
		this.picture = new Picture();
		view.setIdNode(node.toString());

		// Adiciona os listeners da view
		view.addConectaListener(new ConectaListener());
		view.addDesconectaListener(new DesconectaListener());
		view.addCarregarImgListener(new CarregaImgListener());
		view.addWindowFocusListener(new WindowFocusListenerImpl());
	}
	
	public void updateTextFields() {
		try {
			if (node.getNext() != null)
				view.setTxtNextnode(node.getNext().getNode().toString());
			if (node.getPrev() != null)
				view.setTxtPrevnode(node.getPrev().getNode().toString());
		} catch (RemoteException e) {
		}
	}
	class WindowFocusListenerImpl implements java.awt.event.WindowFocusListener{
		@Override
		public void windowGainedFocus(WindowEvent e) {updateTextFields();}
		@Override
		public void windowLostFocus(WindowEvent e) {}
	}
	/**
	 * Classe que implementa as ações ao pressionar o botão "Conecta".
	 * 
	 */
	class ConectaListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			// atualiza o status da dht enquanto tenta-se conectar
			try {
				node.getDht().setStoped(false);
			} catch (RemoteException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}

			/*
			 * Inicia uma thread para acompanhar a atualização do status da dht
			 */
			new Thread(() -> {
				int count = 0;
				while (count < 120) {
					try {
						if ((node.getDht().isConnected() && node.getDht().isInserted()) || node.getDht().isStoped())
							break;
						view.setStatus(node.getDht().getStatus());
						count++;
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					} catch (RemoteException e1) {
						break;
					}
				}
				try {
					if (node.getNext() != null)
						view.setTxtNextnode(node.getNext().getNode().toString());
					if (node.getPrev() != null)
						view.setTxtPrevnode(node.getPrev().getNode().toString());
				} catch (RemoteException e1) {
				}
			}).start();

			/*
			 * Inicia a operação join em uma thread separada para não travar a interface do
			 * usuário
			 */
			new Thread(() -> {

				try {
					view.setBtnConectar(false);
					view.setBtnDesconecta(true);
					node.getDht().join(view.getPath());
					view.setStatus(node.getDht().getStatus());
					view.setIdNode(node.toString());
					view.setBtnDesconecta(true);
				} catch (ConnectException e1) {
					JOptionPane.showMessageDialog(view.getContentPane(),
							"Erro ao iniciar conexão: " + e1.getMessage() + "\n Tente (re)iniciar o rmiregistry ", // mensagem
							"Error: join DHT", // titulo da janela
							JOptionPane.ERROR_MESSAGE);
					try {
						view.setBtnConectar(true);
						view.setStatus("Erro: não foi possível conectar.");
						node.getDht().setStoped(true);
					} catch (RemoteException e2) {
					}
					e1.printStackTrace();
				} catch (AlreadyBoundException e1) {
					JOptionPane.showMessageDialog(view.getContentPane(),
							"Erro ao conectar à rede DHT: Nó já está registrado.\n" + e1.getMessage(), // mensagem
							"Error: join DHT", // titulo da janela
							JOptionPane.ERROR_MESSAGE);
					try {
						view.setBtnConectar(true);
						view.setStatus("Erro: não foi possível conectar.");
						node.getDht().setStoped(true);
					} catch (RemoteException e2) {
					}
					e1.printStackTrace();
				} catch (ExportException e1) {
					JOptionPane.showMessageDialog(view.getContentPane(),
							"Objeto já está registrado: " + e1.getMessage(), // mensagem
							"Error: join DHT", // titulo da janela
							JOptionPane.ERROR_MESSAGE);
					try {
						view.setBtnConectar(true);
						view.setStatus("Erro: não foi possível conectar.");
						node.getDht().setStoped(true);
					} catch (RemoteException e2) {
					}
					e1.printStackTrace();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(view.getContentPane(),
							"Erro ao ler o arquivo inicial: " + e1.getMessage(), // mensagem
							"Error: join DHT", // titulo da janela
							JOptionPane.ERROR_MESSAGE);
					try {
						view.setBtnConectar(true);
						view.setStatus("Erro: não foi possível conectar.");
						node.getDht().setStoped(true);
					} catch (RemoteException e2) {
					}
					e1.printStackTrace();
				}
			}).start();

		}

	}

	/**
	 * Classe que implementa as ações ao pressionar o botão "Desconectar".
	 */
	class DesconectaListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				node.getDht().leave();
				node.getDht().setStoped(true);
				view.setStatus("Desconectado.");
				view.setBtnDesconecta(false);
				view.setBtnConectar(true);
			} catch (RemoteException e1) {
				JOptionPane.showMessageDialog(view.getContentPane(), "Erro ao desconectar à rede DHT ", // mensagem
						"Error: leave DHT", // titulo da janela
						JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Carrega uma imagem a ser salva na DHT. <br>
	 * Ação disparada ao pressionar o botão "Arquivo > Carregar Imagem..."
	 */
	class CarregaImgListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int status = fileChooser.showOpenDialog(view.getContentPane());
			if (status == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				BufferedImage bufferImg = null;
				try {
					bufferImg = ImageIO.read(file);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(view.getContentPane(), "Erro ao carregar imagem.", // mensagem
							"Imagem não foi carregada", // titulo da janela
							JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}

				picture.setImg(bufferImg);
				picture.setName(file.getName());
				picture.setDate(new Date().toString());

				view.setImg(new ImageIcon(picture.getImg()));
				view.setBtnSalvar(true);

			}
		}
	}

	/**
	 * @return the view
	 */
	public ViewAlbum getView() {
		return view;
	}

	/**
	 * @param view
	 *            the view to set
	 */
	public void setView(ViewAlbum view) {
		this.view = view;
	}

	/**
	 * @return the picture
	 */
	public Picture getPicture() {
		return picture;
	}

	/**
	 * @param picture
	 *            the picture to set
	 */
	public void setPicture(Picture picture) {
		this.picture = picture;
	}

	/**
	 * @return the node
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * @param node
	 *            the node to set
	 */
	public void setNode(Node node) {
		this.node = node;
	}

}
