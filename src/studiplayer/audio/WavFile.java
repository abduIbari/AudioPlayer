package studiplayer.audio;
import java.io.File;

import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile{
	
	public WavFile(){
		
	}
	
	public WavFile(String path) throws NotPlayableException{
		super(path);
		try {
			File f = new File(getPathname());
			f.canRead();
			f.exists();
		}
		catch(RuntimeException e) {
            throw new NotPlayableException(getPathname(),"The file is not readable.");
		}
		this.readAndSetDurationFromFile();
	}
	
	public void readAndSetDurationFromFile() throws NotPlayableException {
		try {
			WavParamReader.readParams(getPathname());
			float frameRate = WavParamReader.getFrameRate();
			long numberOfFrames = WavParamReader.getNumberOfFrames();
			
			if(frameRate <= 0 || numberOfFrames <= 0) {
				throw new NotPlayableException(getPathname(), "One of the parameters is invalid");
			}
			this.duration = computeDuration(numberOfFrames, frameRate);
		}
		catch(RuntimeException e) {
			throw new NotPlayableException(getPathname(), "One of the parameters is invalid");
		}
	}
	
	public String toString() {
		String myString = super.toString() + " - " + formatDuration();
		return myString;
	}
	
	public static long computeDuration(long numberOfFrames, float frameRate) {
		  float rate = (numberOfFrames/frameRate)*1000000; 
		  return (long) Math.round(rate);
	}
	

	public static void main(String args[]) {
		WavFile file = new WavFile();
		System.out.println(file.toString());
	}
}
