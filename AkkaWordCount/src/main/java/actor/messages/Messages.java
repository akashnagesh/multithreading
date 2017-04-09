package actor.messages;

import java.util.List;

/**
 * This class has all the immutable messages that you need to pass around
 * actors. You are free to add more classes that you think is necessary
 * 
 * @author akashnagesh
 *
 */
public class Messages {

	// All the messages passed around the actors have to be effectively
	// immutable.
	public static class NumberOfFiles {
		final private int numberOfFiles;

		public NumberOfFiles(int numberOfFiles) {
			this.numberOfFiles = numberOfFiles;
		}

		public int getNumberOfFiles() {
			return numberOfFiles;
		}

	}

	public static class NumberOfWordsInLine {
		final private int numberOfWordsInLine;

		public NumberOfWordsInLine(int numberOfWords) {
			this.numberOfWordsInLine = numberOfWords;
		}

		public int getNumberOfWordsInLine() {
			return numberOfWordsInLine;
		}
	}

	public static class FileAndWords {
		final private int numberOfWordsInFile;
		final private String fileName;

		public FileAndWords(int numberOfWords, String fileName) {
			this.numberOfWordsInFile = numberOfWords;
			this.fileName = fileName;
		}

		public int getNumberOfWordsInFile() {
			return numberOfWordsInFile;
		}

		@Override
		public String toString() {
			return new StringBuilder().append(fileName).append("-").append(numberOfWordsInFile).toString();
		}

	}

	public static class ProcessStringMsg {
		final private String line;

		public ProcessStringMsg(String line) {
			this.line = line;
		}

		public String getLine() {
			return line;
		}
	}

	// Note: This can be done with just the String as well(because String is
	// immutable) or Enum of Strings. I am creating
	// classes just to make sure there is a standard message that is passed
	// around all the actors.
	public static class StartProcessingFolder {

	}

	public static class StartProcessingFile {

	}

	public static class ProcessedFolder {
		private final List<FileAndWords> filesAndWordCount;

		public ProcessedFolder(List<FileAndWords> filesAndWordCount) {
			this.filesAndWordCount = filesAndWordCount;
		}

		public List<FileAndWords> getFilesAndWordCount() {
			return filesAndWordCount;
		}

	}

}