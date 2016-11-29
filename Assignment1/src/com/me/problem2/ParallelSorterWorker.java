package com.me.problem2;

import java.util.LinkedList;
import java.util.List;

/**
 * Given a <code>LinkedList</code>, this class will sort the list
 */
public class ParallelSorterWorker extends Thread {

	protected List<Integer> resultList;
	protected LinkedList<Integer> threadList = new LinkedList<>();
	protected int partialMax = Integer.MIN_VALUE; // initialize to lowest value

	public ParallelSorterWorker(List<Integer> resultList, List<Integer> threadList, String threadName) {
		super(threadName);
		this.resultList = resultList;
		this.threadList.addAll(threadList);
	}

	/**
	 * Update <code>partialMax</code> until the list is exhausted.
	 */
	public void run() {
		while (!threadList.isEmpty()) {
			// check if list is not empty and removes the head
			// synchronization needed to avoid atomicity violation
			synchronized (resultList) {
				addValue(threadList.pop());
			}
		}
	}

	private void addValue(int val) {

		if (resultList.size() == 0) {
			resultList.add(val);
		} else if (resultList.get(0) > val) {
			resultList.add(0, val);
		} else if (resultList.get(resultList.size() - 1) < val) {
			resultList.add(resultList.size(), val);
		} else {
			int i = 0;
			while (resultList.get(i) < val) {
				i++;
			}
			resultList.add(i, val);
		}

	}
}
