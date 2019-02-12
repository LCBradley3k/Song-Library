package application;

public class Song {

	public String name;
	public String artist;
	public String album=null;
	public int year=-1;
	public Song(String newname,String newartist) {
		name=newname;
		artist=newartist;
	}
	public Song(String newname,String newartist, String newalbum) {
		name=newname;
		artist=newartist;
		album=newalbum;
	}
	public Song(String newname,String newartist, String newalbum, int newyear) {
		name=newname;
		artist=newartist;
		album=newalbum;
		year=newyear;
	}
	public Song(String newname,String newartist, int newyear) {
		name=newname;
		artist=newartist;
		year=newyear;
	}
	public String toString() {
		String full = name+", "+artist;
		if(album!=null) {
			full=full+", "+album;
		}
		if(year!=-1) {
			full=full+", "+year;
		}
		return full;
	}
	public String getName() {
		return name;
	}
	public String getArtist() {
		return artist;
	}
	public String getAlbum() {
		if(album==null) {
			return "not available";
		}
		return album;
	}
	public String getYear() {
		if(year==-1) {
			return "not available";
		}
		return Integer.toString(year);
	}
	public void changeName(String newName) {
		name=newName;
	}
	public void changeArtist(String newArtist) {
		artist=newArtist;
	}
	public void changeAlbum(String newAlbum) {
		album=newAlbum;
	}
	public void changeYear(int newYear) {
		year=newYear;
	}
	public int compareTo(Song A, Song B) {
	    if(A.getName().compareTo(B.getName())!=0) {
	        return A.getName().compareTo(B.getName());
	    } else 
	    	return A.getArtist().compareTo(B.getArtist());
	}
}
