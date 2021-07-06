package bridgeSimulator;
/*
 * Program: Symulacja przejazdu pojazdów przez wąski most
 * Plik:	BusFactory.java
 * Autor:	Jakub Mroziński
 * Data:	20.12.2020r.
 */
public class BusFactory extends Thread{
	private int counter, traffic, way; 
	private BridgeApp parent;
	
	public BusFactory(BridgeApp parent) {
		this.parent = parent;
		counter = 0;
	}
	
	@Override
	public void run() {
		while(BridgeApp.running) {
			createBus();
			try {
				Thread.sleep(Math.round(traffic * 5 + 500));
			} catch (InterruptedException e) {}
		}
	}
	
	private void createBus() {
		Bus bus = new Bus(++counter, (BridgeApp.random.nextInt(1000) > way ? Direction.WEST : Direction.EAST), parent);
		parent.addBus(bus);
	}
	
	public void setTraffic(int value) {traffic = value;}
	public void setWay(int value) {way = value;}
}
