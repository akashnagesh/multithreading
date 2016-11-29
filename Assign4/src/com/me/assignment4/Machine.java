package com.me.assignment4;

/**
 * A Machine is used to make a particular Food. Each Machine makes just one kind
 * of Food. Each machine has a capacity: it can make that many food items in
 * parallel; if the machine is asked to produce a food item beyond its capacity,
 * the requester blocks. Each food item takes at least item.cookTimeMS
 * milliseconds to produce.
 */

//Invariant : Machine will get requests from cook, and depending upon the capacity of the machines, the machine will parallely cook the food.
public class Machine {
	public final String machineName;
	public final Food machineFoodType;

	// YOUR CODE GOES HERE...
	// create an array of threads

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

		// YOUR CODE GOES HERE...

		// This will create as cookAnItem Thread
		/// initialize the array of threads for the capacity given

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

	/**
	 * precondition: The machine should have the capacity to take this order
	 * 
	 * postCondition: the machine will return the cooked item after a constant
	 * amount of time
	 * 
	 * @return
	 * @throws InterruptedException
	 */

	// This method will get the number of items to cook from the cook.
	// It will check if it is well within capacity.
	//It will then cook the noOfItemsToBeCooked
	
	public Object makeFood() throws InterruptedException {
		// YOUR CODE GOES HERE...

		// initialize a variable to keep count of the cooked items.

		// loop over the number of items to be cooked.

		// start the machine that is free to cook.

		// increment the count variable.
		return null;
	}

	// THIS MIGHT BE A USEFUL METHOD TO HAVE AND USE BUT IS JUST ONE IDEA
	private class CookAnItem implements Runnable {
		public void run() {
			try {
				// YOUR CODE GOES HERE...
				// Sleep for the amount of time it takes to cook an item.
				throw new InterruptedException();
			} catch (InterruptedException e) {
			}
		}
	}

	public String toString() {
		return machineName;
	}
}