package com.me.problem2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class PublicTestSorter {

	private int threadCount = 10; // number of threads to run
	private ParallelSorter sorter = new ParallelSorter(threadCount);

	@Test
	public void compareMax() {
		int size = 1000; // size of list
		List<List<Integer>> listOfLists = new ArrayList<>();
		sorter.getRandomlyGeneratedList(size, listOfLists, threadCount);
		List<Integer> seriallySorted = sortSerially(listOfLists);

		List<Integer> parallelySorted = new LinkedList<>();

		// try to find parallelMax
		try {
			parallelySorted = sorter.parallelSort(parallelySorted, listOfLists);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("The test failed because the max procedure was interrupted unexpectedly.");
		} catch (Exception e) {
			e.printStackTrace();
			fail("The test failed because the max procedure encountered a runtime error: " + e.getMessage());
		}

		assertEquals("The serial max doesn't match the parallel max", seriallySorted, parallelySorted);
	}

	public List<Integer> sortSerially(List<List<Integer>> listOfIntegers) {
		return listOfIntegers.stream().flatMap(l -> l.stream()).sorted().collect(Collectors.toList());

	}

}
