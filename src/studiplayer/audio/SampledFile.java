package studiplayer.audio;
import java.io.File;
import studiplayer.basic.BasicPlayer;

public abstract class SampledFile extends AudioFile{
	protected long duration;
	
	
	public SampledFile(){	
	}
	
	public SampledFile(String path) throws NotPlayableException {
		super(path);
		try {
			File f = new File(getPathname());
			f.canRead();
			f.exists();
		}
		catch(RuntimeException e) {
            throw new NotPlayableException(getPathname(),"The file is not readable.");
		}
	}
	
	
	
	public void play() throws NotPlayableException {
		try {
			BasicPlayer.play(getPathname());			
		}
		catch(RuntimeException e) {
			throw new NotPlayableException(getPathname(), "Errors occur when playing the song");
		}
	}
	
	public void togglePause() {
		BasicPlayer.togglePause();
	}
	
	public void stop() {
		BasicPlayer.stop();
	}
	
	public String formatDuration() {
		return timeFormatter(getDuration());
		
	}
	
	public String formatPosition() {
		return timeFormatter(BasicPlayer.getPosition());
		
	}
	
	public static String timeFormatter(long timeInMicroSeconds) {
		
		
		if(timeInMicroSeconds < 0 || timeInMicroSeconds >= 6000000000L) {
			throw new RuntimeException("Invalid values are negative values and values that cannot be displayed in the specified \"mm:ss\" format.");
		}
		double seconds = (double) timeInMicroSeconds/1000000L;
		
		long mins = (long) (seconds/60);
			
		long remainingSeconds = (long)seconds-(mins*60);
		
		String formattedString = String.format("%02d:%02d",mins,remainingSeconds);
		return formattedString;
	}
	
	public long getDuration() {
		return duration;
	}
	
	
	
}

	
