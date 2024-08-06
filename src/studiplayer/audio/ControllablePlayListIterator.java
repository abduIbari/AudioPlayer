package studiplayer.audio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ControllablePlayListIterator implements Iterator<AudioFile> {
	private List<AudioFile> list;
	private int index;
	
	public ControllablePlayListIterator(List<AudioFile> list){
		this.list = list;
		this.index = 0;
	}

	public ControllablePlayListIterator(List<AudioFile> list, String search, SortCriterion sortCriterion) {
		if(search == null || search.isEmpty()) {
			this.list = list;
		}
		else {
			this.list = new ArrayList<>();
			for(AudioFile file : list) {
				if((file instanceof TaggedFile)) {
					
				if(file.getAuthor().contains(search) 
						|| file.getTitle().contains(search) 
						|| ((TaggedFile) file).getAlbum().contains(search)){
					this.list.add(file);
				}
				}else {
					if(file.getAuthor().contains(search) 
							|| file.getTitle().contains(search) 
						){
						this.list.add(file);
					}	
				} 
			}
		}
		if (!sortCriterion.equals(SortCriterion.DEFAULT)) {
		    List<AudioFile> tempList = new ArrayList<>(this.list); 
		    if (sortCriterion.equals(SortCriterion.AUTHOR)) {
		        Collections.sort(tempList, new AuthorComparator());
		    } else if (sortCriterion.equals(SortCriterion.TITLE)) {
		        Collections.sort(tempList, new TitleComparator());
		    } else if (sortCriterion.equals(SortCriterion.ALBUM)) {
		        Collections.sort(tempList, new AlbumComparator());
		    } else if (sortCriterion.equals(SortCriterion.DURATION)) {
		        Collections.sort(tempList, new DurationComparator());
		    }
		    this.list = tempList; 
		}

		
	}
	
	@Override
	public boolean hasNext() {
		if (index < list.size()) {
			return true;
		}
		return false;
	}

	@Override
	public AudioFile next() {
		if (hasNext()) {
			return list.get(index++);
		}
		return null;
	}
	
	public AudioFile jumpToAudioFile(AudioFile file) {
		int fileIndex = list.indexOf(file);
		if(fileIndex != -1 && fileIndex < list.size()) {
			int difference = fileIndex - index;
			if(difference >= 0) {
				for(int i = 0; i < difference; i++) {
					next();
				}
			}
			else if(difference < 0) {
				resetIndex();
				for(int i = 0; i <= fileIndex; i++) {
					next();
				}
			}
		
		System.out.println("this: "+list.get(fileIndex));
		return list.get(fileIndex);
		}
			return null;
	
	}
		
	

	public AudioFile current() {
		if (index >= 0 && index < list.size() && !list.isEmpty()) {
	        return list.get(index);
	    }else if (list.isEmpty()) {
	    	return null;
	    }
		else {
	        return list.get(resetIndex()); 
	    }
	}
	
	public int resetIndex() {
	    return index = 0;
	}
	
	

}
