package com.me.assignment4;

import java.util.List;

/**
 * Customers are simulation actors that have two fields: a name, and a list of
 * Food items that constitute the Customer's order. When running, an customer
 * attempts to enter the coffee shop (only successful if the coffee shop has a
 * free table), place its order, and then leave the coffee shop when the order
 * is complete.
 */
//Invariant : Customer will enter the resturant once the table is available and will place the order once the cook is free
public class Customer implements Runnable {
	// JUST ONE SET OF IDEAS ON HOW TO SET THINGS UP...
	private final String name;
	private final List<Food> order;
	private final int orderNum;

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
		// Log for customer entry.
		// Logic for customer to enter the coffee shop.
		// I will wait till there is a a free table in the coffee shop.

		synchronized (Simulation.CUSTOMER_ENTRY_CONTROL) {
			while (!Simulation.CUSTOMER_ENTRY_CONTROL.isAvailable) {
				try {
					Simulation.CUSTOMER_ENTRY_CONTROL.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Simulation.CUSTOMER_ENTRY_CONTROL.freeTables = Simulation.CUSTOMER_ENTRY_CONTROL.freeTables - 1;
				if (Simulation.CUSTOMER_ENTRY_CONTROL.freeTables == 0)
					Simulation.CUSTOMER_ENTRY_CONTROL.isAvailable = false;
			}
			// Log event Customer entered coffee shop.
		}
		
		/*log Simulator.cutomerplaceorder()
		 * call order food method
		 * log Simulator.customerReceiveOrder()
		 */

		synchronized (Simulation.CUSTOMER_ENTRY_CONTROL) {
			Simulation.CUSTOMER_ENTRY_CONTROL.freeTables = Simulation.CUSTOMER_ENTRY_CONTROL.freeTables + 1;
			Simulation.CUSTOMER_ENTRY_CONTROL.isAvailable = true;
			Simulation.CUSTOMER_ENTRY_CONTROL.notifyAll();
			// Log event customer leaving
		}

	}
/**
 * precondition: Cook should be free to place the order
 * postcondition: order will be placed once cook is available
 * @throws InterruptedException
 */
	private synchronized void orderFood() throws InterruptedException {

		// boolean isCookAvailable = false;
		// Cook cook;
		// for(int i=0;i<Simulation.cooks.length;i++)
		// {
		// if(Simulation.cooks[i].getState().equals(Thread.State.NEW))
		// {
		// isCookAvailable = true;
		// cook = Simulation.cooks[i];
		// break;
		// }
		// }
		// while(!isCookAvailable)
		// {
		// wait();
		// }

		/**
		 * Find any cook thread that is free. if none of the cook thread is
		 * free, wait on this object.(Customer object)
		 *
		 * Once a Cook is free, place the order with the cook and pass this
		 * object to the cook.(call take order method of cook)
		 * 
		 * Again wait for the order to be processed till the cook notifies.
		 * 
		 */

	}
}