package actors;

import static actor.messages.Messages.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import actor.messages.Messages.FileAndWords;
import actor.messages.Messages.StartProcessingFile;
import actor.messages.Messages.StartProcessingFolder;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * This is the main actor and the only actor that is created directly under the
 * {@code ActorSystem} This actor creates more child actors
 * {@code WordCountInAFileActor} depending upon the number of files in the given
 * directory structure
 * 
 * @author akashnagesh
 *
 */
public class WordCountActor extends UntypedActor {

	final File folder;
	final int totalFiles;
	private int filesProcessed = 0;
	private ActorRef folderSender = null;
	private List<FileAndWords> fileAndNumberOfWordsList = new ArrayList<>();

	public WordCountActor(String filePath) {
		folder = new File(filePath);
		this.totalFiles = folder.listFiles().length - 1;
	}

	@Override
	public void onReceive(Object msg) throws Throwable {
		if (msg instanceof StartProcessingFolder) {
			this.folderSender = getSender();
			Arrays.stream(folder.listFiles()).filter(f -> f.getName().endsWith(".txt"))
					.forEach(file -> getContext()
							.actorOf(Props.create(WordCountInAFileActor.class, file.getAbsolutePath()))
							.tell(new StartProcessingFile(), getSelf()));
		}

		if (msg instanceof FileAndWords) {
			fileAndNumberOfWordsList.add((FileAndWords) msg);
			filesProcessed++;
			if (filesProcessed == totalFiles) {
				System.out.println(folderSender);
				folderSender.tell(new ProcessedFolder(fileAndNumberOfWordsList), getSelf());
			}
		}
	}

}
