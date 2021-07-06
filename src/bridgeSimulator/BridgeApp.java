package bridgeSimulator;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JCheckBox;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
/*
 * Program: Symulacja przejazdu pojazdów przez wąski most
 * Plik:	BridgeApp.java
 * Autor:	Jakub Mroziński
 * Data:	20.12.2020r.
 */
public class BridgeApp extends JFrame {
	public static final Font myFont = new Font("Tahoma", Font.PLAIN, 12);
	private static final long serialVersionUID = -4736344269647960765L;
	
	public static Random random;
	public static boolean running = true;
	
	private AnimationFrame animationFrame;
	private BusFactory factory;
	private Bridge bridge;
	private List<Bus> busses = new ArrayList<>();
	private List<String> queueBusses = new ArrayList<>(),
						 bridgeBusses = new ArrayList<>();
	
	private JLabel maxBussesLabel = new JLabel("Maksymalna liczba autobusów na moście:"),
				   trafficLabel = new JLabel("Natężenie ruchu"),
				   wayLabel = new JLabel("Kierunek ruchu"),
				   smallLabel = new JLabel("małe"),
				   bigLabel = new JLabel("duże"),
				   eastLabel = new JLabel("wschód"),
				   westLabel = new JLabel("zachód"),
				   onBridgeLabel = new JLabel("Na moście:"),
				   queueLabel = new JLabel("Kolejka:");
	
	private JSpinner maxBusses = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
	private JCheckBox twoWay = new JCheckBox("Ruch dwustronny");
	private JSlider trafficSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 500),
					waySlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 500);
	private JTextField onBridgeField = new JTextField(10),
					   queueField = new JTextField(10);
	private JTextArea textArea = new JTextArea();
	private JScrollPane scrollPane = new JScrollPane();
	private JMenuBar menu = new JMenuBar();
	private JMenu animationMenu = new JMenu("Animacja"),
				  authorMenu = new JMenu("O programie");
	private JMenuItem showItem = new JMenuItem("Pokaż animację"),
					  hideItem = new JMenuItem("Ukryj animację"),
					  informationItem = new JMenuItem("Pokaż informacje o programie");
	
	public BridgeApp() {
		super("Symulacja");
		random = new Random();
		setFrameComponents();
		setMenuBar();
		try{
			Bus.loadIcon(this);
		}
		catch(IOException | NullPointerException e) {
			JOptionPane.showMessageDialog(this, "Nie wczytano ikon", "Błąd", JOptionPane.ERROR_MESSAGE);
		}
		running = true;
		animationFrame = new AnimationFrame(this);
		factory = new BusFactory(this);
		factory.setTraffic(trafficSlider.getMaximum() - trafficSlider.getValue());
		factory.setWay(waySlider.getValue());
		factory.start();
		bridge = new Bridge();
		bridge.setTwoWay(twoWay.isSelected());
	}

	public static void main(String [] args) {
		EventQueue.invokeLater(() -> new BridgeApp().setVisible(true));
	}
	
	private void setMenuBar() {
		setJMenuBar(menu);
		menu.add(animationMenu);
		menu.add(authorMenu);
		animationMenu.add(showItem);
		animationMenu.add(hideItem);
		authorMenu.add(informationItem);
		showItem.setEnabled(false);
		
		showItem.addActionListener((event) -> {
			animationFrame.setVisible(true);
			showItem.setEnabled(false);
			hideItem.setEnabled(true);
		});
		hideItem.addActionListener((event) -> {
			animationFrame.setVisible(false);
			showItem.setEnabled(true);
			hideItem.setEnabled(false);
		});
		informationItem.addActionListener((event) -> {
			JOptionPane.showMessageDialog(this, "Program: Symulacja przejazdu pojazdów przez wąski most\nAutor:	Jakub Mroziński\nData:	20.12.2020r.", "O programie", JOptionPane.INFORMATION_MESSAGE);
		});
	}
	
	private void setFrameComponents() {
		setBounds(20, 20, 510, 500);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				running = false;
			}
			@Override
		    public void windowIconified(WindowEvent e) {
				animationFrame.setState(ICONIFIED);
			}
			@Override
		    public void windowDeiconified(WindowEvent e) {
				animationFrame.setState(NORMAL);
			}
		});
		
		maxBussesLabel.setFont(myFont);
		trafficLabel.setFont(myFont);
		wayLabel.setFont(myFont);
		onBridgeField.setFont(myFont);
		onBridgeLabel.setFont(myFont);
		queueField.setFont(myFont);
		queueLabel.setFont(myFont);
		twoWay.setFont(myFont);
		
		maxBussesLabel.setBounds(10, 10, 220, 25);
		trafficLabel.setBounds(10, 50, 90, 20);
		wayLabel.setBounds(10, 80, 90, 20);
		smallLabel.setBounds(100, 68, 30, 13);
		bigLabel.setBounds(350, 68, 30, 13);
		eastLabel.setBounds(345, 98, 60, 13);
		westLabel.setBounds(95, 98, 60, 13);
		maxBusses.setBounds(230, 10, 40, 26);
		twoWay.setBounds(280, 10, 120, 25);
		trafficSlider.setBounds(100, 50, 280, 20);
		waySlider.setBounds(100, 80, 280, 20);
		onBridgeField.setBounds(80, 120, getWidth() - 100, 19);
		onBridgeLabel.setBounds(10, 118, 70, 20);
		queueField.setBounds(80, 149, getWidth() - 100, 19);
		queueLabel.setBounds(10, 149, 70, 20);
		scrollPane.setBounds(10, 179, getWidth() - 30, getHeight() - 240);
		
		onBridgeField.setEditable(false);
		queueField.setEditable(false);
		textArea.setEditable(false);
		
		scrollPane.setViewportView(textArea);
		
		maxBusses.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				bridge.setMaxBusses((int) maxBusses.getValue());
			}
		});
		twoWay.setSelected(true);
		twoWay.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				bridge.setTwoWay(twoWay.isSelected());
			}
		});
		trafficSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				factory.setTraffic(trafficSlider.getMaximum() - trafficSlider.getValue());
			}
		});
		waySlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				factory.setWay(waySlider.getValue());
			}
		});
		add(maxBussesLabel);
		add(trafficLabel);
		add(wayLabel);
		add(smallLabel);
		add(bigLabel);
		add(eastLabel);
		add(westLabel);
		add(maxBusses);
		add(twoWay);
		add(trafficSlider);
		add(waySlider);
		add(onBridgeField);
		add(onBridgeLabel);
		add(queueField);
		add(queueLabel);
		add(scrollPane);
	}

	public synchronized void write(Bus bus) {
		textArea.setText(bus.toString() + "\n" + textArea.getText());
		switch(bus.getBusState()) {
		case WAITING_AT_GATE: 
			queueBusses.add(bus.getNr() + bus.getDirection().toString()); 
			break;
		case DRIVING_THROUGH_BRIDGE: 
			queueBusses.remove(bus.getNr() + bus.getDirection().toString()); 
			bridgeBusses.add(bus.getNr() + bus.getDirection().toString()); 
			break;
		case DRIVING_TO_PARKING: 
			bridgeBusses.remove(bus.getNr() + bus.getDirection().toString()); 
			break;
		default: 
			break;
		}
		queueField.setText(queueBusses.toString().replace("[", "").replace("]", ""));
		onBridgeField.setText(bridgeBusses.toString().replace("[", "").replace("]", ""));
	}

	public void removeBus(Bus bus) {
		while(busses.contains(bus))
			busses.remove(bus);
	}
	
	public void addBus(Bus bus) {busses.add(bus);}
	public Bridge getBridge() {return bridge;}
	public List<Bus> getBusses(){return busses;}
}
