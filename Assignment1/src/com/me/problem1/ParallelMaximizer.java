package com.me.problem1;

import java.util.*;

/**
 * This class runs <code>numThreads</code> instances of
 * <code>ParallelMaximizerWorker</code> in parallel to find the maximum
 * <code>Integer</code> in a <code>LinkedList</code>.
 */
public class ParallelMaximizer {

	static int numThreads;
	ArrayList<ParallelMaximizerWorker> workers; // = new
												// ArrayList<ParallelMaximizerWorker>(numThreads);

	public ParallelMaximizer(int numThreads) {
		this.numThreads = numThreads;
		workers = new ArrayList<ParallelMaximizerWorker>(numThreads);
	}

	public static void main(String[] args) {
		numThreads = 4; // number of threads for the maximizer
		int numElements = 100; // number of integers in the list

		ParallelMaximizer maximizer = new ParallelMaximizer(numThreads);
		LinkedList<Integer> list = new LinkedList<Integer>();
		// populate the list
		// TODO: change this implementation to test accordingly
		for (int i = 0; i < numElements; i++)
			list.add(i);

		// run the maximizer
		try {
			System.out.println(maximizer.max(list));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Finds the maximum by using <code>numThreads</code> instances of
	 * <code>ParallelMaximizerWorker</code> to find partial maximums and then
	 * combining the results.
	 * 
	 * @param list
	 *            <code>LinkedList</code> containing <code>Integers</code>
	 * @return Maximum element in the <code>LinkedList</code>
	 * @throws InterruptedException
	 */
	public int max(LinkedList<Integer> list) throws InterruptedException {
		int max = Integer.MIN_VALUE; // initialize max as lowest value

		// run numThreads instances of ParallelMaximizerWorker


		for (int i = 0; i < numThreads; i++) {

			workers.add(i, new ParallelMaximizerWorker(list, "Thread" + i));
			workers.get(i).start();
		}
		// wait for threads to finish
		for (int i = 0; i < workers.size(); i++)
			workers.get(i).join();

		// take the highest of the partial maximums
		for (ParallelMaximizerWorker worker : workers) {
			
			if (worker.getPartialMax() > max)
				max = worker.getPartialMax();
		}

		return max;
	}

}