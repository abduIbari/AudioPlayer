package studiplayer.audio;

import java.util.Comparator;

public class TitleComparator implements Comparator<AudioFile> {

	@Override
	public int compare(AudioFile o1, AudioFile o2) {
			if(o1.getTitle().compareTo(o2.getTitle()) > 0)
				return 1;
			else if(o1.getTitle().compareTo(o2.getTitle()) < 0) {
				return -1;
			}
			return 0;

		}		
	}


