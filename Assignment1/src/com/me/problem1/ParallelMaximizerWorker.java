package com.me.problem1;

import java.util.LinkedList;
import java.util.List;

/**
 * Given a <code>LinkedList</code>, this class will find the maximum over a
 * subset of its <code>Integers</code>.
 */
public class ParallelMaximizerWorker extends Thread {

	protected LinkedList<Integer> list;
	protected int partialMax = Integer.MIN_VALUE; // initialize to lowest value
	protected int count = 0;
	
	public ParallelMaximizerWorker(LinkedList<Integer> list,String threadName) {
		super(threadName);
		this.list = list;
	}
	

	
	/**
	 * Update <code>partialMax</code> until the list is exhausted.
	 */
	public void run() {
		while (true) {
			int number;
			// check if list is not empty and removes the head
			// synchronization needed to avoid atomicity violation
			synchronized(list) {
				if (list.isEmpty())
					return; // list is empty
				number = list.remove();
				count++;
				System.out.println("I am thread "+this.getName()+" doing the work");
			}
			
			// update partialMax according to new value
			// TODO: IMPLEMENT CODE HERE
			if(partialMax < number)
				partialMax = number;
		}
	}
	
	public int getPartialMax() {
		return partialMax;
	}
	public int getCount()
	{
		return count;
	}

}
