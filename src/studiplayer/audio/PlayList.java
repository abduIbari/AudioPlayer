package studiplayer.audio;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;


public class PlayList implements Iterable<AudioFile>{

	private String search;
	private SortCriterion sortCriterion = SortCriterion.DEFAULT;
	
	
	public String toString() {
		return getList().toString();
	}

	private ControllablePlayListIterator iterator;

	
	
	public PlayList(String m3uPathname)  {
		try {
			loadFromM3U(m3uPathname);
		} catch (Exception e) {
			throw new RuntimeException("Hello");
		}
		

	}

	public PlayList() {	
	}
	
	public String getSearch() {
		return search;
	}
	
	public void setSearch(String search) {
		this.search = search;
		resetIterator();

	}
	
	public SortCriterion getSortCriterion() {
		return sortCriterion;
	}
	
	public void setSortCriterion(SortCriterion sortCriterion) {
		this.sortCriterion = sortCriterion;
		resetIterator();
		
	}

	LinkedList<AudioFile> list1 = new LinkedList<AudioFile>();

	public LinkedList<AudioFile> getList() {
		return list1;
	}

	public void add(AudioFile file) {
		list1.add(file);
		resetIterator();
	}

	public void remove(AudioFile file) {
		list1.remove(file);
		resetIterator();
	}

	public int size() {
		return list1.size();
	}

	public AudioFile currentAudioFile() {
        if (iterator == null) {
        	resetIterator();
        	}
        return iterator.current();
    }

    public void nextSong() {
        if (iterator == null || !iterator.hasNext()) {
        	resetIterator();
        	}
        if (iterator.hasNext()) {
            iterator.next();
        } 
        else {
        	resetIterator();
        	}
        }
    


	public void saveAsM3U(String pathname) {
		FileWriter writer = null;
		String sep = System.getProperty("line.separator");

		try {
			writer = new FileWriter(pathname);
			for (AudioFile audioFile : getList()) {
				writer.write(audioFile.getPathname() + sep);
			}
		} catch (IOException e) {
			throw new RuntimeException("Unable to write file " + pathname + "!");
		} finally {
			try {
				System.out.println("File " + pathname + " written!");
				writer.close();
			} catch (Exception e) {
			}

		}
	}

	public void loadFromM3U(String pathname) throws NotPlayableException {
		getList().clear();
		Scanner scanner = null;

		try {
			scanner = new Scanner(new File(pathname));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if (line.startsWith("#") || line.isEmpty()) {
					continue;
				}

				try {
					AudioFile file = AudioFileFactory.createAudioFile(line);
					add(file);
				} catch (NotPlayableException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to read file " + pathname);
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}

	@Override
	public Iterator<AudioFile> iterator() {
		return new ControllablePlayListIterator(getList(), search, sortCriterion);	
	}
	
	private void resetIterator() {
        iterator = new ControllablePlayListIterator(getList(), search, sortCriterion);
        iterator.resetIndex();
	}

	public AudioFile jumpToAudioFile(AudioFile file) {
		return iterator.jumpToAudioFile(file);
	}

	
	public static void main(String[] arg) {
		System.out.println(Thread.activeCount());
	}

}
