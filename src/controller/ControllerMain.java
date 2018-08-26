package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

import javax.swing.JOptionPane;

import dht.Node;
import dht.SHA1;
import gui.ViewAlbum;
import gui.ViewMain;

/**
 * Classe Principal que executará o programa
 */
public class ControllerMain {

	private ViewMain view;
	private String hashId;
	private String port;
	private String ip = "127.0.0.1";
	private int count = 1;

	public ControllerMain(ViewMain view) {
		this.view = view;
		this.view.addBtnListener(new NovoBtnAction());
		this.hashId = count + "";
		this.port = "1099";
		this.view.getTxtPorta().setText(port);
		this.view.getTxtHash().setText(hashId);
		this.view.getChckbxSha().addActionListener(new Sha1Action());
		
		try {
			ip=(Inet4Address.getLocalHost().getHostAddress());
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.view.getTxtIp().setText(ip);
	}

	
	class Sha1Action implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			doHash();
		}

	}

	private void doHash() {
		if (view.getChckbxSha().isSelected()) {
			String input = view.getTxtHash().getText();
			try {
				input = SHA1.digest(input);
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
				JOptionPane.showMessageDialog(view.getContentPane(), 
						"Erro ao criar hash: " + e.getMessage(), // mensagem
						"Error: hash", // titulo da janela
						JOptionPane.ERROR_MESSAGE);
			}
			hashId = input;
			view.getTxtHash().setText(hashId);
		} else {
			hashId = count + "";
			view.getTxtHash().setText(hashId);
		}
	}

	class NovoBtnAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			hashId = view.getTxtHash().getText();
			Node node = new Node(hashId);
			node.setPort(view.getTxtPorta().getText());
			node.setIp(view.getTxtIp().getText());
			
			ViewAlbum viewAlbum = new ViewAlbum();
			new ControllerAlbum(viewAlbum, node);

			viewAlbum.setVisible(true);

			hashId = ++count + "";
			doHash();
			int portSum = Integer.parseInt(port) + (count - 1);
			view.getTxtHash().setText(hashId);
			view.getTxtPorta().setText(portSum + "");
		}

	}

}
