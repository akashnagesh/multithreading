package com.me.assignment6;

import java.util.ArrayList;
import java.util.List;

/**
 * Cooks are simulation actors that have at least one field, a name. When
 * running, a cook attempts to retrieve outstanding orders placed by Eaters and
 * process them..
 */
public class Cook implements Runnable {
	private final String name;
	Customer customerToServe;
	public List<Food> foodCooked = new ArrayList<>();

	/**
	 * You can feel free modify this constructor. It must take at least the
	 * name, but may take other parameters if you would find adding them useful.
	 *
	 * @param: the
	 *             name of the cook
	 */
	public Cook(String name) {
		this.name = name;
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
				synchronized (Simulation.customerIn) {
					while (Simulation.customerIn.isEmpty()) {

						Simulation.customerIn.wait();

						// TODO Auto-generated catch block
						// Thread.currentThread().interrupt();

					}
					customerToServe = Simulation.customerIn.get(0);
					Simulation.customerIn.remove(0);
					Simulation.logEvent(SimulationEvent.cookReceivedOrder(this, customerToServe.getOrder(),
							customerToServe.getOrderNum()));
					Simulation.customerIn.notifyAll();
				}

				for (int i = 0; i < customerToServe.getOrder().size(); i++) {

					Food food = customerToServe.getOrder().get(i);

					if (food.equals(FoodType.burger)) {
						synchronized (Simulation.burgerMachine.foodToCook) {
							while (Simulation.burgerMachine.foodToCook.size() > Simulation.burgerMachine.capacityIn) {

								Simulation.burgerMachine.foodToCook.wait();

							}
							Simulation.logEvent(
									SimulationEvent.cookStartedFood(this, food, customerToServe.getOrderNum()));
							Simulation.burgerMachine.makeFood(this);
							Simulation.burgerMachine.foodToCook.notifyAll();
						}
					} else if (food.equals(FoodType.fries)) {
						
						synchronized (Simulation.friesMachine.foodToCook) {
							while (Simulation.friesMachine.foodToCook.size() > Simulation.friesMachine.capacityIn) {

								Simulation.friesMachine.foodToCook.wait();

							}
							Simulation.logEvent(
									SimulationEvent.cookStartedFood(this, food, customerToServe.getOrderNum()));
							Simulation.friesMachine.makeFood(this);
							Simulation.friesMachine.foodToCook.notifyAll();
						}
					} else if (food.equals(FoodType.coffee)) {
						synchronized (Simulation.coffeeMachine.foodToCook) {
							while (Simulation.coffeeMachine.foodToCook.size() > Simulation.coffeeMachine.capacityIn) {

								Simulation.coffeeMachine.foodToCook.wait();

							}
							Simulation.logEvent(
									SimulationEvent.cookStartedFood(this, food, customerToServe.getOrderNum()));
							Simulation.coffeeMachine.makeFood(this);
							Simulation.coffeeMachine.foodToCook.notifyAll();
						}
					}
				}

				synchronized (foodCooked) {
					while (foodCooked.size() != customerToServe.getOrder().size()) {
						foodCooked.wait();
					}
					// foodCooked.notifyAll();
					
					foodCooked = new ArrayList<>();
				}
				synchronized (customerToServe) {
					customerToServe.orderComplete = true;
					customerToServe.notifyAll();
				}

			}
		} catch (InterruptedException e) {
			Simulation.logEvent(SimulationEvent.cookEnding(this));
		}

	}
}