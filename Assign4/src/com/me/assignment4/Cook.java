package com.me.assignment4;

import java.util.List;

/**
 * Cooks are simulation actors that have at least one field, a name. When
 * running, a cook attempts to retrieve outstanding orders placed by Eaters and
 * process them.
 */

//Incariant : cook will take order from customer and place the order once the machine is free
public class Cook implements Runnable {
	private final String name;
	public boolean isFree;

	/**
	 * You can feel free modify this constructor. It must take at least the
	 * name, but may take other parameters if you would find adding them useful.
	 *
	 * @param: the
	 *             name of the cook
	 */
	public Cook(String name, boolean isFree) {
		this.name = name;
		this.isFree = isFree;
	}

	public String toString() {
		return name;
	}

	/**
	 * This method executes as follows. The cook tries to retrieve orders placed
	 * by Customers. For each order, a List<Food>, the cook submits each Food
	 * item in the List to an appropriate Machine, by calling makeFood(). Once
	 * all machines have produced the desired Food, the order is complete, and
	 * the Customer is notified. The cook can then go to process the next order.
	 * If during its execution the cook is interrupted (i.e., some other thread
	 * calls the interrupt() method on it, which could raise
	 * InterruptedException if the cook is blocking), then it terminates.
	 */
	public void run() {

		Simulation.logEvent(SimulationEvent.cookStarting(this));
		try {

			while (true) {
				// YOUR CODE GOES HERE...
				//Simulation.cookstarting.
				
				//Iterate over the number of order items that the customer has ordered.
				
				//count each order type of the food ordered and store in 3 different variables.
				
				/*
				 * If the coffee machine is free, call makeFood() on the coffee machine.
				 * If the burger machine is free, call makeBurger() on the burger machine;
				 * if the fires machine is free, call makeFries() on the fries machine.
				 * 
				 */
				throw new InterruptedException("");

			}
		} catch (InterruptedException e) {
			// This code assumes the provided code in the Simulation class
			// that interrupts each cook thread when all customers are done.
			// You might need to change this if you change how things are
			// done in the Simulation class.
			Simulation.logEvent(SimulationEvent.cookEnding(this));
		}
	}
	/**
	 * 
	 */
	
	public void getOrder(List<Food> listOfFood)
	{
		
	}
}