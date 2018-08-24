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
	
	private  ViewMain view;
	private String hashId;
	private String port;
	private int count = 1;
	
	
	public ControllerMain(ViewMain view) {
		this.view = view;
		this.view.addBtnListener(new NovoBtnAction());
		this.hashId = count+"";
		this.port = "1099";
		this.view.getTxtPorta().setText(port);
		this.view.getTxtHash().setText(hashId);
	}
	
	class NovoBtnAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
		Node node = new Node(hashId);
		hashId = ++count + "";
		view.getTxtHash().setText(hashId);
		int portSum = Integer.parseInt(port) + count;
		view.getTxtPorta().setText(portSum+"");
		node.setPort(view.getTxtPorta().getText());
		ViewAlbum viewAlbum = new ViewAlbum();
		new ControllerAlbum(viewAlbum, node);
		
		viewAlbum.setVisible(true);
			
		}
		
	}
}
