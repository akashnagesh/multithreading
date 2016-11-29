package com.me.problem2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ParallelSorter {

	static int numThreads;
	ArrayList<ParallelSorterWorker> workers;

	public ParallelSorter(int numThreads) {
		this.numThreads = numThreads;
		workers = new ArrayList<ParallelSorterWorker>(numThreads);
	}

	// method that generates a list of random lists of given size
	public static void getRandomlyGeneratedList(int size, List<List<Integer>> threadLists, int numberOfThreads) {
		for (int i = 0; i < numberOfThreads; i++) {
			threadLists.add(i, (new Random().ints(size, 0, 1000).boxed().collect(Collectors.toList())));
		}

	}

	public static void main(String[] args) {

		numThreads = 4; // number of threads for the sorter

		ParallelSorter sorter = new ParallelSorter(numThreads);
		LinkedList<Integer> resultList = new LinkedList<Integer>();
		List<List<Integer>> threadLists = new ArrayList<>();
		getRandomlyGeneratedList(10, threadLists, numThreads);

		// run the Sorter
		try {
			sorter.parallelSort(resultList, threadLists);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Finds the maximum by using <code>numThreads</code> instances of
	 * <code>ParallelMaximizerWorker</code> to find partial maximums and then
	 * combining the results.
	 * 
	 * @param resultList
	 *            <code>LinkedList</code> containing <code>Integers</code>
	 * @return Maximum element in the <code>LinkedList</code>
	 * @throws InterruptedException
	 */
	public List<Integer> parallelSort(List<Integer> resultList, List<List<Integer>> threadLists)
			throws InterruptedException {

		// run numThreads instances of ParallelSorterWorker

		for (int i = 0; i < numThreads; i++) {

			workers.add(i, new ParallelSorterWorker(resultList, threadLists.get(i), "Thread" + i));
			workers.get(i).start();
		}
		// wait for threads to finish
		for (int i = 0; i < workers.size(); i++)
			workers.get(i).join();

		for (int a : resultList) {
			System.out.println(a);
		}
		return resultList;
	}

}
