package librarypass.sort;

import java.util.Comparator;

import librarypass.entity.Voce;

public class VoceComparator implements Comparator<Voce> {
	
	@Override
	public int compare(Voce voce1, Voce voce2) {
	    int compareDescrizione = voce1.getDescrizione().compareTo(voce2.getDescrizione());
	    if (compareDescrizione != 0) {
	        return compareDescrizione;
	    } else {
	        return voce1.getUser().compareTo(voce2.getUser());
	    }
	}
}
