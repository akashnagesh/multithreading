package actors;

import java.util.concurrent.TimeUnit;
import static actor.messages.Messages.*;

import actor.messages.Messages.StartProcessingFolder;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

/**
 * Main class for your wordcount actor system.
 * 
 * @author akashnagesh
 *
 */
public class WordCount {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ActorSystem system = ActorSystem.create("wordcounter");
		ActorRef actor = system.actorOf(Props.create(WordCountActor.class, args[0]));

		final Timeout timeout = new Timeout(Duration.create(30, TimeUnit.SECONDS));
		// ask method
		final Future<Object> future = Patterns.ask(actor, new StartProcessingFolder(), timeout);

		ProcessedFolder result = (ProcessedFolder) Await.result(future, timeout.duration());

		result.getFilesAndWordCount().stream().forEach(System.out::println);

		System.out.println("Total number of words is  "
				+ result.getFilesAndWordCount().stream().mapToInt(m -> m.getNumberOfWordsInFile()).sum());

		system.shutdown();

	}

}
