package bridgeSimulator;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
/*
 * Program: Symulacja przejazdu pojazdów przez wąski most
 * Plik:	AnimationPanel.java
 * Autor:	Jakub Mroziński
 * Data:	20.12.2020r.
 */
public class AnimationPanel extends JPanel {
	private static final long serialVersionUID = -5592219070431797420L;
	private static final Color parkingColor = Color.GRAY, roadColor = new Color(60, 60, 60), gateColor = new Color(255, 160, 160), bridgeColor = new Color(130, 255, 255);

	private BridgeApp app;
	
	private JLabel parkingLabel1 = new JLabel("PARKING"),
				   parkingLabel2 = new JLabel("PARKING"),
				   roadLabel1 = new JLabel("DROGA"),
				   roadLabel2 = new JLabel("DROGA"),
				   gateLabel1 = new JLabel("BRAMKA"),
				   gateLabel2 = new JLabel("BRAMKA"),
				   bridgeLabel = new JLabel("MOST");
	
	public AnimationPanel(BridgeApp app) {
		setBounds(0, 0, app.getWidth() - 15, app.getHeight() - 35);
		this.app = app;
		setLayout(null);
		setBackground();
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int width = getWidth() / 17;
		int height = getHeight();
		g.setColor(parkingColor);
		g.fillRect(0, 0, width * 2, height);
		g.fillRect(width * 15, 0, width * 2, height);
		g.setColor(roadColor);
		g.fillRect(width * 2, 0, width * 3, height);
		g.fillRect(width * 12, 0, width * 3, height);
		g.setColor(gateColor);
		g.fillRect(width * 5, 0, width * 2, height);
		g.fillRect(width * 10, 0, width * 2, height);
		g.setColor(bridgeColor);
		g.fillRect(width * 7, 0, width * 3, height);
		int i = 40;
		synchronized(app.getBusses()) {
			List<Bus> busses = app.getBusses();
			int offset = (busses.size() > (getHeight() - i) / 20 ? (getHeight() - i) / busses.size() : 20);
			for(int j = 0; j < busses.size(); j++) {
				synchronized (busses.get(j)) {
					busses.get(j).draw(g, i, this);
				}
				i += offset;
			}
		}
	}
	private void setBackground() {
		int width = getWidth() / 17;
		
		parkingLabel1.setFont(BridgeApp.myFont);
		parkingLabel2.setFont(BridgeApp.myFont);
		roadLabel1.setFont(BridgeApp.myFont);
		roadLabel2.setFont(BridgeApp.myFont);
		gateLabel1.setFont(BridgeApp.myFont);
		gateLabel2.setFont(BridgeApp.myFont);
		bridgeLabel.setFont(BridgeApp.myFont);
		
		roadLabel1.setForeground(Color.gray);
		roadLabel2.setForeground(Color.gray);
		
		parkingLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		parkingLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		roadLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		roadLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		gateLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		gateLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		bridgeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		parkingLabel1.setBounds(0, 0, width * 2, 20);
		parkingLabel2.setBounds(width * 15, 0, width * 2 + 10, 20);
		roadLabel1.setBounds(width * 2, 0, width * 3, 20);
		roadLabel2.setBounds(width * 12, 0, width * 3, 20);
		gateLabel1.setBounds(width * 5, 0, width * 2, 20);
		gateLabel2.setBounds(width * 10, 0, width * 2, 20);
		bridgeLabel.setBounds(width * 7, 0, width * 3, 20);
		
		add(parkingLabel1);
		add(parkingLabel2);
		add(roadLabel1);
		add(roadLabel2);
		add(gateLabel1);
		add(gateLabel2);
		add(bridgeLabel);
	}
}