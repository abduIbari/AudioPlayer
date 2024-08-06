package studiplayer.audio;

import java.io.File;
import java.util.Map;

import studiplayer.basic.TagReader;

public class TaggedFile extends SampledFile {
	private String album;

	public TaggedFile() {
	}

	public TaggedFile(String path) throws NotPlayableException {
		super(path);
		try {
			File f = new File(getPathname());
			f.canRead();
			f.exists();
		} catch (RuntimeException e) {
			throw new NotPlayableException(getPathname(), "The file is not readable.");
		}
		readAndStoreTags();
	}

	public String getAlbum() {
		if (this.album == null) {
			return "";
		} else {
			return album.trim();
		}
	}

	public void readAndStoreTags() throws NotPlayableException {
		try {
			Map<String, Object> tagMap = TagReader.readTags(getPathname());

			if (tagMap == null || tagMap.isEmpty()) {
				throw new NotPlayableException(getPathname(), "Tags are not readable or empty.");
			}

			if ((String) tagMap.get("title") == null || (String) tagMap.get("author") == null) {
				super.parseFilename(getFilename());
			} else {
				this.title = (String) tagMap.get("title");
				this.author = (String) tagMap.get("author");
			}
			this.album = (String) tagMap.get("album");
			this.duration = (long) tagMap.get("duration");
		} catch (RuntimeException e) {
			throw new NotPlayableException(getPathname(), "Tags are not readable from this file.");
		}
	}

	public String toString() {
		if (getAlbum().isEmpty()) {
			System.out.println(super.toString() + " - " + formatDuration());
			return super.toString() + " - " + formatDuration();
		} else {
			System.out.println(super.toString() + " - " + getAlbum() + " - " + formatDuration());
			return super.toString() + " - " + getAlbum() + " - " + formatDuration();
		}

	}

	public static void main(String args[]) {

	}
}
