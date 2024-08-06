package studiplayer.audio;

import java.util.Comparator;

public class DurationComparator implements Comparator<AudioFile>{

	@Override
	public int compare(AudioFile o1, AudioFile o2) {
		Long duration1 = (o1 instanceof SampledFile) ? ((SampledFile) o1).getDuration() : null;
		Long duration2 = (o2 instanceof SampledFile) ? ((SampledFile) o2).getDuration() : null;

		if (duration1 == null || duration1 == 0) {
            if (duration2 == null || duration2 == 0) {
                return -1; 
            }
            else {
             	return -1; 
            }
        }
        if (duration2 == null || duration2 == 0) {
            return 1; 
        }
        else {
        	return duration1.compareTo(duration2);
        }
	}

}
