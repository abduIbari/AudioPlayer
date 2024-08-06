package studiplayer.audio;

import java.io.File;

public abstract class AudioFile {
	private String filename;
	private String pathname;
	protected String author;
	protected String title;

	public AudioFile(String path) throws NotPlayableException {
		try {
			File f = new File(path);
			f.canRead();
		}
		catch(RuntimeException e) {
            throw new NotPlayableException(path,"The file is not readable.");
		}
		parsePathname(path);
		parseFilename(filename);
		
	}

	public AudioFile() {
		this.title = "";
		this.author = "";
	}

	String forLinux(String path) {

		String linuxStr = path;
		int counter = 1;
		for (int i = 0; i < linuxStr.length() - 1; i++) {
			int nextIndex = i + 1;
			if ((linuxStr.charAt(i) == '/') || (linuxStr.charAt(i) == '\\')) {
				int tempIndex_i = i;
				while ((linuxStr.charAt(tempIndex_i) == '/' && linuxStr.charAt(nextIndex) == '/')
						|| (linuxStr.charAt(tempIndex_i) == '\\' && linuxStr.charAt(nextIndex) == '\\')
						|| (linuxStr.charAt(tempIndex_i) == '/' && linuxStr.charAt(nextIndex) == '\\')
						|| (linuxStr.charAt(tempIndex_i) == '\\' && linuxStr.charAt(nextIndex) == '/')) {

					counter++;
					tempIndex_i++;
					nextIndex++;
				}
				
				if (linuxStr.contains(":")) {
					linuxStr = linuxStr.replace(":", "");
					linuxStr = new StringBuilder(linuxStr).insert(0, "/").toString();
				}
				
				String slashesReplaced = linuxStr.substring(i, i + counter);
				linuxStr = linuxStr.replace(slashesReplaced, "/");
				linuxStr = linuxStr.replace("\\", "/");
				counter = 1;
			}

		}

		return linuxStr;
	}

	String forWindows(String path) {

		String windowsStr = path;
		int counter = 1;
		for (int i = 0; i < windowsStr.length() - 1; i++) {
			int nextIndex = i + 1;
			if ((windowsStr.charAt(i) == '/') || (windowsStr.charAt(i) == '\\')) {
				int tempIndex_i = i;
				while ((windowsStr.charAt(tempIndex_i) == '/' && windowsStr.charAt(nextIndex) == '/')
						|| (windowsStr.charAt(tempIndex_i) == '\\' && windowsStr.charAt(nextIndex) == '\\')
						|| (windowsStr.charAt(tempIndex_i) == '/' && windowsStr.charAt(nextIndex) == '\\')
						|| (windowsStr.charAt(tempIndex_i) == '\\' && windowsStr.charAt(nextIndex) == '/')) {
					counter++;
					tempIndex_i++;
					nextIndex++;
				}
				
				String slashesReplaced = windowsStr.substring(i, i + counter);
				windowsStr = windowsStr.replace(slashesReplaced, "\\");
				windowsStr = windowsStr.replaceAll("/", "\\\\");
				counter = 1;

			}
		}
		return windowsStr;
	}

	public void parsePathname(String path) {

		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().contains("win")) {
			pathname = forWindows(path);
			String[] splittedPath = pathname.split("\\\\");
			if (pathname.endsWith("\\")) {
				filename = "";
			} else {
				int lastIndex = splittedPath.length - 1;
				filename = splittedPath[lastIndex];
			}
			if (path == " - ") {
				pathname = "-";
				filename = "-";

			}
			if (path == " -  ") {
				pathname = "-";
				filename = "-";

			}
		}

		else {
			pathname = forLinux(path);
			String[] splittedPath = pathname.split("/");
			if (pathname.endsWith("/")) {
				filename = "";
			} else {
				int lastIndex = splittedPath.length - 1;
				filename = splittedPath[lastIndex];
			}
			if (path == " - ") {
				pathname = "-";
				filename = "-";
			}
			if (path == " -  ") {
				pathname = "-";
				filename = "-";

			}
		}

		pathname = pathname.trim();
		filename = filename.trim();
	}

	public void parseFilename(String fileName) {

		String fileNameStr = fileName;

		if (fileNameStr.indexOf(" - ") == -1 && fileNameStr.contains(".")) {
			author = "";
			int lastDot = fileNameStr.lastIndexOf('.');
			title = fileNameStr.substring(0, lastDot).trim();
		}
		if (fileNameStr.length() == 0 || (fileNameStr.indexOf(" - ") == -1 && fileNameStr.indexOf(".") == -1)) {
			author = "";
			title = "";
		}
		if (fileNameStr == "-") {
			author = "";
			title = "-";
		}
		if (fileNameStr == " - ") {
			author = "";
			title = "";
		}

		if (fileNameStr.contains("-") && fileNameStr.contains(".")) {
			for (int i = 0; i < fileNameStr.length(); i++) {
				int nextIndex = i + 1;
				int nextToNextIndex = i + 2;
				if (fileNameStr.charAt(i) == ' ' && fileNameStr.charAt(nextIndex) == '-'
						&& fileNameStr.charAt(nextToNextIndex) == ' ') {
					String tempAuthor = fileNameStr.substring(0, nextIndex - 1);
					String tempTitleString = fileNameStr.substring(nextIndex + 1);
					if (tempAuthor == null) {
						author = "";
					}
					else {
						author = tempAuthor.trim();
					}

					int lastDot = tempTitleString.lastIndexOf('.');
					title = tempTitleString.substring(0, lastDot).trim();
					
				}

			}
		}

	}

	public String getPathname() {
		return pathname;
	}

	public String getFilename() {
		return filename;
	}

	public String getAuthor() {
		if (author == null) {
			return "";
		}
		else {
			return author.trim();			
		}
	}

	public String getTitle() {
		
		return title.trim();
		
	}

	public String toString() {
		if (author.isEmpty()) {
			return getTitle();
		} else {
			return getAuthor() + " - " + getTitle();
		}
	}

	public static void main(String[] args) {
	}

	public abstract void play() throws NotPlayableException;
	
	public abstract void togglePause();
	
	public abstract void stop();
	
	public abstract String formatDuration();
	
	public abstract String formatPosition();
	

}
