import controller.ControllerMain;
import gui.ViewMain;

public class MainRun {
	public static void main(String[] args) {
		ViewMain view = new ViewMain();
		new ControllerMain(view);
		view.setVisible(true);
	}

}
