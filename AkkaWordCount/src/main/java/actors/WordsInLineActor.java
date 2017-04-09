package actors;

import static actor.messages.Messages.*;

import akka.actor.UntypedActor;

/**
 * This actor counts number words in a single line
 * 
 * @author akashnagesh
 *
 */
public class WordsInLineActor extends UntypedActor {

	@Override
	public void onReceive(Object msg) throws Throwable {
		if (msg instanceof ProcessStringMsg) {
			int length = ((ProcessStringMsg) msg).getLine().split(" ").length;
			getSender().tell(new NumberOfWordsInLine(length), getSelf());
		} else {
			System.err.println("Cannot identify message" + msg);
		}
	}
}
