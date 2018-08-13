import controller.ControllerAlbum;
import dht.DHT;
import dht.DHTImpl;
import gui.ViewAlbum;

/**
 * Classe Principal que executará o programa
 */
public class Main {
	public static void main(String[] args) {
		DHT dht = new DHTImpl();
		ViewAlbum view = new ViewAlbum();
		ControllerAlbum controllerAlbum = new ControllerAlbum(view, dht);
		
		view.setVisible(true);
	}
}
