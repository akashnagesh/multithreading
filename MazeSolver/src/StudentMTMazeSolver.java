
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.management.relation.InvalidRelationServiceException;

/**
 * This file needs to hold your solver to be tested. You can alter the class to
 * extend any class that extends MazeSolver. It must have a constructor that
 * takes in a Maze. It must have a solve() method that returns the datatype
 * List<Direction> which will either be a reference to a list of steps to take
 * or will be null if the maze cannot be solved.
 */
public class StudentMTMazeSolver extends SkippingMazeSolver {

	public StudentMTMazeSolver(Maze maze) {
		super(maze);
	}

	
	volatile boolean isSolved = false;

	public List<Direction> solve() {

		Choice initialChoice = null;
		List<Direction> result = null;
		try {
			initialChoice = firstChoice(maze.getStart());
		} catch (SolutionFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final Choice start = initialChoice; // making another copy because
											// variables inside lambdas have to
											// be final/effectively final
		List<CompletableFuture<List<Direction>>> cfs = IntStream.range(0, start.choices.size()).mapToObj(
				i -> new Choice(start.at, start.from, new LinkedList<Direction>(Arrays.asList(start.choices.pop()))))
				.map(newChoice -> CompletableFuture.supplyAsync(() -> {
					LinkedList<Choice> choiceStack = new LinkedList<Choice>();
					Choice currChoice;

					try {
						choiceStack.push(newChoice);
						while (!choiceStack.isEmpty()) {
							if (isSolved)
								break;
							currChoice = choiceStack.peek();

							if (currChoice.isDeadend()) {
								// backtrack
								choiceStack.pop();
								if (!choiceStack.isEmpty())
									choiceStack.peek().choices.pop();
								continue;
							}
							choiceStack.push(follow(currChoice.at, currChoice.choices.peek()));
						}
						return null;
					} catch (SolutionFound e) {
						isSolved = true;
						Iterator<Choice> iter = choiceStack.iterator();
						LinkedList<Direction> solutionPath = new LinkedList<Direction>();

						while (iter.hasNext()) {
							currChoice = iter.next();
							solutionPath.push(currChoice.choices.peek());
						}
						return pathToFullPath(solutionPath);
					}
				})).collect(Collectors.toList());

		for (CompletableFuture<List<Direction>> f : cfs) {
			try {
				if (f.get() != null) {
					result = f.get();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
}
