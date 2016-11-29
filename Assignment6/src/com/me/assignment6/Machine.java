package com.me.assignment6;

import java.util.ArrayList;
import java.util.List;

/**
 * A Machine is used to make a particular Food. Each Machine makes just one kind
 * of Food. Each machine has a capacity: it can make that many food items in
 * parallel; if the machine is asked to produce a food item beyond its capacity,
 * the requester blocks. Each food item takes at least item.cookTimeMS
 * milliseconds to produce.
 */
public class Machine {
	public final String machineName;
	public final Food machineFoodType;
	public final int capacityIn;
	public List<Food> foodToCook;

	// YOUR CODE GOES HERE...

	/**
	 * The constructor takes at least the name of the machine, the Food item it
	 * makes, and its capacity. You may extend it with other arguments, if you
	 * wish. Notice that the constructor currently does nothing with the
	 * capacity; you must add code to make use of this field (and do whatever
	 * initialization etc. you need).
	 */
	public Machine(String nameIn, Food foodIn, int capacityIn) {
		this.machineName = nameIn;
		this.machineFoodType = foodIn;
		this.capacityIn = capacityIn;
		this.foodToCook = new ArrayList<>();
	}

	/**
	 * This method is called by a Cook in order to make the Machine's food item.
	 * You can extend this method however you like, e.g., you can have it take
	 * extra parameters or return something other than Object. It should block
	 * if the machine is currently at full capacity. If not, the method should
	 * return, so the Cook making the call can proceed. You will need to
	 * implement some means to notify the calling Cook when the food item is
	 * finished.
	 */
	public void makeFood(Cook cook) {
		// YOUR CODE GOES HERE...
		foodToCook.add(machineFoodType);
		CookAnItem cookAnItem = new CookAnItem(cook);
		Thread machineCooking = new Thread(cookAnItem);
		machineCooking.start();
	}

	// THIS MIGHT BE A USEFUL METHOD TO HAVE AND USE BUT IS JUST ONE IDEA
	private class CookAnItem implements Runnable {
		Cook currentCook;
		int ordernumber;

		public CookAnItem(Cook cook) {
			// TODO Auto-generated constructor stub
			this.currentCook = cook;
		}

		public void run() {
			try {
				// YOUR CODE GOES HERE...
				Simulation.logEvent(SimulationEvent.machineCookingFood(Machine.this, machineFoodType));
				Thread.sleep(machineFoodType.cookTimeMS);
				Simulation.logEvent(SimulationEvent.machineDoneFood(Machine.this, machineFoodType));
				Simulation.logEvent(SimulationEvent.cookFinishedFood(currentCook,machineFoodType, 1));

				synchronized (foodToCook) {
					foodToCook.remove(0);
					foodToCook.notifyAll();
				}

				synchronized (currentCook.foodCooked) {
					currentCook.foodCooked.add(machineFoodType);
					currentCook.foodCooked.notifyAll();
				}
			} catch (InterruptedException e) {
			}
		}
	}

	public String toString() {
		return machineName;
	}
}