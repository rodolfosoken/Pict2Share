package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	}

	class NovoBtnAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			hashId=view.getTxtHash().getText();
			Node node = new Node(hashId);
			node.setPort(view.getTxtPorta().getText());
			node.setIp("127.0.0.1");
			ViewAlbum viewAlbum = new ViewAlbum();
			new ControllerAlbum(viewAlbum, node);

			viewAlbum.setVisible(true);

			hashId = ++count + "";
			int portSum = Integer.parseInt(port) + (count-1);
			view.getTxtHash().setText(hashId);
			view.getTxtPorta().setText(portSum + "");
		}

	}
}
