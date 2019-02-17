package application;

public class Song {

	public String name;
	public String artist;
	public String album=null;
	public int year=-1;

	public Song(String newname,String newartist, String newalbum, int newyear) {
		name=newname;
		artist=newartist;
		album=newalbum;
		year=newyear;
	}
	public String toLongString() {
		String full = name+", "+artist;
		if(album!=null) {
			full=full+", "+album;
		}
		if(year!=-1) {
			full=full+", "+year;
		}
		return full;
	}
	public String toString() {
		return name+" by "+artist;
	}
	public String getName() {
		return name;
	}
	public String getArtist() {
		return artist;
	}
	public String getAlbum() {
		return album;
	}
	public int getYear() {
		return year;
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
