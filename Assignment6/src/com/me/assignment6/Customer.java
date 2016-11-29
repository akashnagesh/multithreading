package com.me.assignment6;

import java.util.List;

/**
 * Customers are simulation actors that have two fields: a name, and a list of
 * Food items that constitute the Customer's order. When running, an customer
 * attempts to enter the coffee shop (only successful if the coffee shop has a
 * free table), place its order, and then leave the coffee shop when the order
 * is complete.
 */
public class Customer implements Runnable {
	// JUST ONE SET OF IDEAS ON HOW TO SET THINGS UP...
	private final String name;
	private final List<Food> order;
	private final int orderNum;
	volatile  boolean orderComplete;

	private static int runningCounter = 0;

	/**
	 * You can feel free modify this constructor. It must take at least the name
	 * and order but may take other parameters if you would find adding them
	 * useful.
	 */
	public Customer(String name, List<Food> order) {
		this.name = name;
		this.order = order;
		this.orderNum = ++runningCounter;
		this.orderComplete = false;
	}

	public String toString() {
		return name;
	}

	/**
	 * This method defines what an Customer does: The customer attempts to enter
	 * the coffee shop (only successful when the coffee shop has a free table),
	 * place its order, and then leave the coffee shop when the order is
	 * complete.
	 */
	public void run() {
		// YOUR CODE GOES HERE...
		Simulation.logEvent(SimulationEvent.customerStarting(this));
		synchronized (Simulation.customerWaiting) {
			while (Simulation.customerWaiting.size() >= Simulation.numTable) {
				try {
			
					Simulation.customerWaiting.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			Simulation.customerWaiting.add(this);
			Simulation.logEvent(SimulationEvent.customerEnteredCoffeeShop(this));
			Simulation.customerWaiting.notifyAll();
		}

		synchronized (Simulation.customerIn) {
			Simulation.customerIn.add(this);
			Simulation.logEvent(SimulationEvent.customerPlacedOrder(this, order, orderNum));
			Simulation.customerIn.notifyAll();
		}
		
		synchronized(this){
			while(!orderComplete){
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
			Simulation.logEvent(SimulationEvent.customerReceivedOrder(this, this.order, this.orderNum));
			this.notifyAll();
		}

		synchronized (Simulation.customerWaiting) {
			Simulation.logEvent(SimulationEvent.customerLeavingCoffeeShop(this));
			Simulation.customerWaiting.remove(this);
			Simulation.customerWaiting.notifyAll();
		}

	}

	public List<Food> getOrder() {
		return order;
	}

	public int getOrderNum() {
		return orderNum;
	}

}