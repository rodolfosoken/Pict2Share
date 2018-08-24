package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dht.Node;
import gui.ViewAlbum;
import gui.ViewMain;

/**
 * Classe Principal que executará o programa
 */
public class Main {
	
	public static void main(String[] args) {
		ViewMain viewMain = new ViewMain();
		viewMain.addBtnListener(new NovoBtnAction());
		viewMain.setVisible(true);
	}
	
	static class NovoBtnAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			Node node = new Node("1");
		ViewAlbum view = new ViewAlbum();
		new ControllerAlbum(view, node);
		
		view.setVisible(true);
			
		}
		
	}
}
