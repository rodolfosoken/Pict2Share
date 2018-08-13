package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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
		
		//Adiciona os listeners da view
		view.addConectaListener(new ConectaListener());	
		view.addDesconectaListener(new DesconectaListener());
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
	
	
	

}
