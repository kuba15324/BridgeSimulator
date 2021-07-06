package bridgeSimulator;

import java.util.ArrayList;
import java.util.List;
/*
 * Program: Symulacja przejazdu pojazdów przez wąski most
 * Plik:	Bridge.java
 * Autor:	Jakub Mroziński
 * Data:	20.12.2020r.
 */
public class Bridge {
	private int maxBussesNr = 1;
	private boolean twoWay = true;
	private List<Bus> busses = new ArrayList<>();
	private Direction direction; 
	
	public Bridge() {new DirectionChanger(this).start();}
	
	public synchronized void addBus(Bus bus) {
		if(busses.size() == 0) 
			direction = bus.getDirection();
		while(busses.size() >= maxBussesNr || (twoWay ? false : (bus.getDirection() != direction))) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		if(busses.size() == 0) 
			direction = bus.getDirection();
		busses.add(bus);
		notifyAll();
	}
	
	public synchronized void removeBus(Bus bus) {
		busses.remove(bus);
		if(bus.getDirection() == direction || busses.size() == 0) 
			notifyAll();
	}
	
	public synchronized void setTwoWay(boolean value) {
		twoWay = value;
		if(twoWay == false && busses.size() > 0)
			direction = busses.get(0).getDirection();
	}
	public synchronized void setMaxBusses(int value) {maxBussesNr = value;}
	public void changeDirection() {direction = (direction == Direction.EAST ? Direction.WEST : Direction.EAST);}
	public boolean getTwoWay() {return twoWay;}
}

class DirectionChanger extends Thread {
	private Bridge parent;
	
	public DirectionChanger(Bridge parent) {
		this.parent = parent;
	}
	@Override 
	public void run() {
		try {
			Thread.sleep(10000);
			while(BridgeApp.running) {
				if(!parent.getTwoWay())
					parent.changeDirection();
				Thread.sleep(10000);
			}
		} catch (InterruptedException e) {}
	}
}
