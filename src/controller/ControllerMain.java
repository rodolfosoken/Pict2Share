package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JOptionPane;
import javax.xml.bind.DatatypeConverter;

import dht.Node;
import gui.ViewAlbum;
import gui.ViewMain;

/**
 * Classe Principal que executará o programa
 */
public class ControllerMain {

	private ViewMain view;
	private String hashId;
	private String port;
	private int count = 1;

	public ControllerMain(ViewMain view) {
		this.view = view;
		this.view.addBtnListener(new NovoBtnAction());
		this.hashId = count + "";
		this.port = "1099";
		this.view.getTxtPorta().setText(port);
		this.view.getTxtHash().setText(hashId);
		this.view.getChckbxSha().addActionListener(new Sha1Action());
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
			input = sha1(input);
			hashId = input;
			view.getTxtHash().setText(hashId);
		}else {
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
			node.setIp("127.0.0.1");
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
	
	private String sha1(String input) {
	    String sha1 = null;
	    try {
			MessageDigest msdDigest = MessageDigest.getInstance("SHA-1");
			msdDigest.update(input.getBytes("UTF-8"), 0, input.length());
			sha1 = DatatypeConverter.printHexBinary(msdDigest.digest());
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e1) {
			JOptionPane.showMessageDialog(view.getContentPane(), "Erro ao criar hash: " + e1.getMessage(), // mensagem
					"Error: hash", // titulo da janela
					JOptionPane.ERROR_MESSAGE);
		}
	    return sha1;
	}
}
