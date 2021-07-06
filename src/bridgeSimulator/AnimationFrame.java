package bridgeSimulator;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
/*
 * Program: Symulacja przejazdu pojazdów przez wąski most
 * Plik:	AnimationFrame.java
 * Autor:	Jakub Mroziński
 * Data:	20.12.2020r.
 */
public class AnimationFrame extends JFrame {
	private static final long serialVersionUID = -1689488364830720621L;
	
	public AnimationFrame(BridgeApp app) {
		super("Animacja");
		setBounds(app.getX() + app.getWidth() + 20, app.getY(), app.getWidth(), app.getHeight());
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLayout(null);
		setVisible(true);
		setContentPane(new AnimationPanel(app));
		addWindowListener(new WindowAdapter() {
			@Override
		    public void windowIconified(WindowEvent e) {
				app.setState(ICONIFIED);
			}
			@Override
		    public void windowDeiconified(WindowEvent e) {
				app.setState(NORMAL);
			}
		});
		new AnimationRepainter(this).start();
	}
}

class AnimationRepainter extends Thread {
	AnimationFrame frame;
	AnimationRepainter(AnimationFrame panel){
		this.frame = panel;
	}
	@Override 
	public void run() {
		while(true) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {}
			frame.repaint();
		}
	}
}
