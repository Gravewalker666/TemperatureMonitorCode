import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.net.*;

public class TemperatureMonitor extends UnicastRemoteObject implements TemperatureListener, Runnable {
	private int count = 0;

	public TemperatureMonitor() throws RemoteException {
	}

	public void temperatureChanged(double temperature) throws java.rmi.RemoteException {
		System.out.println("\nTemperature change event : " + temperature);
		count = 0;
	}

	public void run() {
		while (true) {
			count++;
			// note that this might only work on windows console
			System.out.print("\r" + count);
			try {
				Thread.sleep(100);
			} catch (InterruptedException ie) {
				System.out.println(ie.getMessage());
			}
		}
	}

	public static void main(String[] args) {
		System.setProperty("java.security.policy", "file:allowall.policy");
		System.setProperty("java.rmi.server.hostname", "localhost");
		try {
			Registry registry = LocateRegistry.getRegistry(1888);
			TemperatureSensor sensor = (TemperatureSensor) registry.lookup("TemperatureSensor");
			double reading = sensor.getTemperature();
			System.out.println("Original temp : " + reading);
			TemperatureMonitor monitor = new TemperatureMonitor();
			// TODO: Add method call to register the listener in the server object
			monitor.run();
		} catch (RemoteException | NotBoundException e) {
			System.out.println(e.getMessage());
		}
	}
}