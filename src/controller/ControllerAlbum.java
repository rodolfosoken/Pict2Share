package controller;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import dht.DHT;
import dht.Node;
import gui.ViewAlbum;
import model.Picture;

/**
 * Classe que controla a instância view do álbum.
 */
public class ControllerAlbum {
	
	private ViewAlbum view;
	private Picture picture;
	private DHT dht;
	private Node currentNode;
	
	public ControllerAlbum(ViewAlbum view, DHT dht) {
		this.view = view;
		this.dht = dht;
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
				currentNode = dht.join(view.getPath());
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(view.getContentPane(), 
						"Erro ao conectar à rede DHT ", // mensagem
						"Error: join DHT", // titulo da janela
						JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
			view.setStatus("Conectado!");
			view.setIdNode(currentNode.getId());
			view.setBtnDesconecta(true);
		}
		
	}
	
	/**
	 * Classe que implementa as ações ao pressionar o botão "Desconectar".
	 */
	class DesconectaListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			dht.leave();
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
