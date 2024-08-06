package studiplayer.audio;
import java.io.File;

public class AudioFileFactory {
	
	
	public static AudioFile createAudioFile(String path) throws NotPlayableException {
		File file = new File(path);
        if (!file.canRead()) {
            throw new NotPlayableException(path, "The file is not readable.");
        }
       
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');
        String extension = (lastDot == -1) ? "" : name.substring(lastDot + 1).toLowerCase();
        
		if(extension.equals("wav")) {
			WavFile wFile = new WavFile(path);
			return wFile;
		}
		else if(extension.equals("mp3") || extension.equals("ogg")){
			TaggedFile tFile = new TaggedFile(path);
			return tFile;
		}
     
		else {
        	throw new NotPlayableException(path, "Unknown suffix for AudioFile \"" + path + "\"");        	
        }
			
	}
	
}
