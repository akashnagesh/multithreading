package actors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import actor.messages.Messages.FileAndWords;
import actor.messages.Messages.NumberOfWordsInLine;
import actor.messages.Messages.ProcessStringMsg;
import actor.messages.Messages.StartProcessingFile;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * this actor reads the file line by line and sends them to
 * {@code WordsInLineActor} to count the words in line. Upon geting the results,
 * It sends the result to it's parent actor {@code WordCount}
 * 
 * @author akashnagesh
 *
 */
public class WordCountInAFileActor extends UntypedActor {
	private int totalLines = 0;
	private int linesProcessed = 0;
	private int result = 0;
	private ActorRef fileSender = null;
	private String pathToFile;

	public WordCountInAFileActor(String pathToFile) {
		// TODO Auto-generated constructor stub
		this.pathToFile = pathToFile;
	}

	@Override
	public void onReceive(Object msg) throws Throwable {
		if (msg instanceof StartProcessingFile) {
			fileSender = getSender();

			try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
				String line;
				while ((line = br.readLine()) != null) {
					getContext().actorOf(Props.create(WordsInLineActor.class)).tell(new ProcessStringMsg(line),
							getSelf());
					totalLines += 1;
				}
			}
		}

		else if (msg instanceof NumberOfWordsInLine) {
			int numberOfWordsInLine = ((NumberOfWordsInLine) msg).getNumberOfWordsInLine();
			result += numberOfWordsInLine;
			linesProcessed += 1;
			if (linesProcessed == totalLines) {
				fileSender.tell(new FileAndWords(result, new File(pathToFile).getName()), getSelf());
			}
		} else {
			System.err.println("Cannot identify message" + msg);
		}
	}

}
