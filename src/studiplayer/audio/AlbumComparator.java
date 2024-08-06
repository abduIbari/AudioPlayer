package studiplayer.audio;

import java.util.Comparator;

public class AlbumComparator implements Comparator<AudioFile>{
	
	

	@Override
	public int compare(AudioFile o1, AudioFile o2) {
		String album1 = (o1 instanceof TaggedFile) ? ((TaggedFile) o1).getAlbum() : null;
        String album2 = (o2 instanceof TaggedFile) ? ((TaggedFile) o2).getAlbum() : null;

        if(album1 == null && album2 != null) {
        	return -1;
        }
        if(album1 != null && album2 == null) {
        	return 1;
        }
        if(album1 == null && album2 == null) {
        	return 0;
        }
        
        	return album1.compareTo(album2);
        
	}
    }


