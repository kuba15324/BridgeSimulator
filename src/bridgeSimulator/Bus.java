package bridgeSimulator;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.io.IOException;
import javax.imageio.ImageIO;
/*
 * Program: Symulacja przejazdu pojazdów przez wąski most
 * Plik:	Bus.java
 * Autor:	Jakub Mroziński
 * Data:	20.12.2020r.
 */
public class Bus extends Thread{
	private static final int drivingToBridgeTime = 2000;
	private static final int drivingThroughBridgeTime = 2800;
	private static final int maxBoardingTime = 2000;
	private static Image iconE = null, iconW =  null;
	private static int iconWidth = 15, iconHeight = 15;
	
	private boolean running;
	private int nr, x, width;
	private Direction direction;
	private BusState state;
	private BridgeApp parent;
	
	public Bus(int nr, Direction direction, BridgeApp parent) {
		this.nr = nr;
		this.direction = direction;
		this.parent = parent;
		state = BusState.BOARDING;
		x = iconWidth / 2;
		width = (parent.getWidth() - 15) / 17;
		start();
	}

	@Override
	public void run() {
		running = true;
		while(BridgeApp.running && running) {
			parent.write(this);
			try{
				switch(state) {
				case BOARDING: 
					Thread.sleep(BridgeApp.random.nextInt(maxBoardingTime - 1000) + 1000); 
					break;
				case DRIVING_TO_BRIDGE:
					for(int i = 0; i < width * 6; i++) {
						x ++;
						Thread.sleep(drivingToBridgeTime/(width * 6));
					}
					break;
				case WAITING_AT_GATE: 
					parent.getBridge().addBus(this); 
					break;
				case DRIVING_THROUGH_BRIDGE:  
					for(int i = 0; i < width * 4; i++) {
						x++;
						Thread.sleep(drivingThroughBridgeTime/(width * 4));
					} 
					break;
				case DRIVING_TO_PARKING: 
					parent.getBridge().removeBus(this);
					for(int i = 0; i < width * 6; i++) {
						x ++;
						Thread.sleep(drivingToBridgeTime/(width * 6));
					} 
					break;
				case UNBOARDING:
					Thread.sleep(300);
					running = false;
					parent.removeBus(this);
					return;
				}
			} catch(InterruptedException e) {};
			state = BusState.next(state);
		}
	}
	
	public void draw(Graphics g, int y, Component parent) {
		if(iconE != null && iconW != null) {
			Image image = (state == BusState.WAITING_AT_GATE ? parent.createImage(new FilteredImageSource((direction == Direction.EAST ? iconE : iconW).getSource(), new RGBImageFilter() {
				@Override
				public int filterRGB(int x, int y, int rgb) {
					if(findSimilarRGB(rgb, new Color(0, 194, 255).getRGB()))
						return Color.YELLOW.getRGB();
				    return rgb;
				}
				private boolean findSimilarRGB(int rgb1, int rgb2) {
					int delta = 130;
					//pobranie odpowiednich składowych z kodu rgb
					int r1 = (rgb1 >> 16) & 0x000000FF,
						g1 = (rgb1 >> 8) & 0x000000FF,
						b1 = (rgb1) & 0x000000FF,
						r2 = (rgb2 >> 16) & 0x000000FF,
						g2 = (rgb2 >> 8) & 0x000000FF,
						b2 = (rgb2) & 0x000000FF;
					if(r1 - r2 < delta && r1 - r2 > -delta && g1 - g2 < delta && g1 - g2 > -delta && b1 - b2 < delta && b1 - b2 > -delta)
						return true;
					return false;
				}
			})) : (direction == Direction.EAST ? iconE : iconW));
			g.drawImage(image, (direction == Direction.EAST ? x - iconWidth / 2 : width * 17 - x - iconWidth / 2), y - iconHeight / 2, null);
		}
		else {
			g.setColor(state == BusState.WAITING_AT_GATE ? Color.yellow : Color.gray);
			g.fillRect((direction == Direction.EAST ? x - iconWidth / 2 : width * 17 - x - iconWidth / 2), y - iconHeight / 2, iconWidth, iconHeight);
			g.setColor(Color.BLACK);
			g.drawRect((direction == Direction.EAST ? x - iconWidth / 2 : width * 17 - x - iconWidth / 2), y - iconHeight / 2, iconWidth, iconHeight);
		}
		g.setColor(Color.BLACK);
		g.drawString(nr + "", (direction == Direction.EAST ? x : width * 17 - x) - 8, y + iconHeight / 5);
	}
	
	public static void loadIcon(BridgeApp parent) throws IOException {
		iconE = ImageIO.read(parent.getClass().getResource("/iconE.png"));
		iconW = ImageIO.read(parent.getClass().getResource("/iconW.png"));
		iconHeight = iconE.getHeight(null);
		iconWidth = iconE.getWidth(null);
	}

	public Integer getNr() {return nr;}
	public Direction getDirection() {return direction;}
	public BusState getBusState() {return state;}
	@Override 
	public String toString() {return "Bus: " + nr + " " + direction.toString() + " -> " + state.toString();}
}

enum Direction{
	WEST('W'),
	EAST('E');
	private char value;
	private Direction(char value){this.value = value;}
	@Override
	public String toString() {return value + "";}
}

enum BusState{
	BOARDING("załadunek"),
	DRIVING_TO_BRIDGE("jedzie do mostu"),
	WAITING_AT_GATE("czeka przed mostem"),
	DRIVING_THROUGH_BRIDGE("JEDZIE PRZEZ MOST"),
	DRIVING_TO_PARKING("jedzie na parking końcowy"),
	UNBOARDING("rozładunek");
	private String message;
	private BusState(String message) {this.message = message;}
	public static BusState next(BusState state) {
		switch(state) {
		case BOARDING: return DRIVING_TO_BRIDGE; 
		case DRIVING_TO_BRIDGE: return WAITING_AT_GATE; 
		case WAITING_AT_GATE: return DRIVING_THROUGH_BRIDGE; 
		case DRIVING_THROUGH_BRIDGE: return DRIVING_TO_PARKING; 
		case DRIVING_TO_PARKING: return UNBOARDING; 
		case UNBOARDING: return BOARDING;
		default: return null;
		}
	}
	@Override
	public String toString() {return message;}
}