package studiplayer.audio;

import java.util.Comparator;

public class AuthorComparator implements Comparator<AudioFile> {

	public int compare(AudioFile o1, AudioFile o2) {
		if(o1.getAuthor().compareTo(o2.getAuthor()) > 0)
			return 1;
		else if(o1.getAuthor().compareTo(o2.getAuthor()) < 0) {
			return -1;
		}
		return 0;

	}

}
