import javax.swing.UIManager;

import controller.ControllerMain;
import gui.ViewMain;

public class MainRun {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Não foi possível alterar a L&F");
		}
		
		ViewMain view = new ViewMain();
		new ControllerMain(view);
		view.setVisible(true);
	}

}
