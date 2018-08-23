import controller.ControllerAlbum;
import dht.Node;
import gui.ViewAlbum;

/**
 * Classe Principal que executará o programa
 */
public class Main {
	public static void main(String[] args) {
		
		Node node = new Node("hash1");
		ViewAlbum view = new ViewAlbum();
		new ControllerAlbum(view, node);
		
		view.setVisible(true);
	}
}
