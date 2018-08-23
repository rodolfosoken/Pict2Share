package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
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
		
		//Adiciona os listeners da view
		view.addConectaListener(new ConectaListener());	
		view.addDesconectaListener(new DesconectaListener());
		view.addCarregarImgListener(new CarregaImgListener());
	}
	
	/**
	 * Classe que implementa as ações ao pressionar o botão "Conecta".
	 * 
	 */
	class ConectaListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				node.getDht().join(view.getPath());
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(view.getContentPane(), 
						"Erro ao conectar à rede DHT ", // mensagem
						"Error: join DHT", // titulo da janela
						JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			} catch (AlreadyBoundException e1) {
				JOptionPane.showMessageDialog(view.getContentPane(), 
					"Erro ao conectar à rede DHT: Nó já está registrado.", // mensagem
					"Error: join DHT", // titulo da janela
					JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			} catch (NotBoundException e1) {
				JOptionPane.showMessageDialog(view.getContentPane(), 
						"Erro ao conectar à rede DHT ", // mensagem
						"Error: join DHT", // titulo da janela
						JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
			view.setStatus("Conectado!");
			view.setIdNode(node.getId());
			view.setBtnDesconecta(true);
		}
		
	}
	
	/**
	 * Classe que implementa as ações ao pressionar o botão "Desconectar".
	 */
	class DesconectaListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				node.getDht().leave();
			} catch (RemoteException e1) {
				JOptionPane.showMessageDialog(view.getContentPane(), 
						"Erro ao desconectar à rede DHT ", // mensagem
						"Error: leave DHT", // titulo da janela
						JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
			view.setStatus("Desconectado.");
			view.setBtnDesconecta(false);
		}
	}
	
	/**
	 * Carrega uma imagem a ser salva na DHT.
	 * <br> Ação disparada ao pressionar 
	 * 		o botão "Arquivo > Carregar Imagem..." 
	 */
	class CarregaImgListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int status = fileChooser.showOpenDialog(view.getContentPane());
			if(status == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				BufferedImage bufferImg = null;
				try {
					bufferImg = ImageIO.read(file);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(view.getContentPane(), 
							"Erro ao carregar imagem.", // mensagem
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
	
	
	

}
