package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import application.Song;

public class SongLibController {
	@FXML ListView<Song> listView;
	@FXML Button edit;
	@FXML Button add;
	@FXML Button delete;
	@FXML Text song;
	@FXML Text artist;
	@FXML Text album;
	@FXML Text year;
	@FXML TextField songField;
	@FXML TextField artistField;
	@FXML TextField albumField;
	@FXML TextField yearField;
	
	
	private ObservableList<Song> obsList;

	public void start(Stage mainStage) {
		
		// Open JSON content as string
		try {
			String content = readFile("src/data/songs.json");
			JSONObject song_obj = new JSONObject(content);
			JSONArray song_info = song_obj.getJSONArray("songs");
			
			ArrayList<Song> songs = new ArrayList<Song>();
			
			
			for(int i=0; i<song_info.length(); i++) {
				Song fromstorage =new Song(song_info.getJSONObject(i).getString("name"),song_info.getJSONObject(i).getString("artist"), song_info.getJSONObject(i).getString("album"),song_info.getJSONObject(i).getInt("Year"));
				songs.add(fromstorage);
			}
			
			// create ObservableList from an ArrayList
			obsList = FXCollections.observableArrayList(songs);
			listView.setItems(obsList);
			
			// preselect first one
			listView.getSelectionModel().select(0);
			
			// listen for changes in selection
			listView.getSelectionModel().selectedIndexProperty().addListener(
					(obs, oldVal, newVal) -> showItem(mainStage));
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Pre-set the data to the first selected.
		Song firstSong = listView.getSelectionModel().getSelectedItem();
		song.setText(firstSong.getName());
		artist.setText(firstSong.getArtist());
		if(firstSong.getAlbum().equals("")) {
			album.setText("Not defined");
		}else {
			album.setText(firstSong.getAlbum());
		}
		if(firstSong.getYear()==-1) {
			year.setText("Not defined");
		}else {
			year.setText(Integer.toString(firstSong.getYear()));
		}
		
	}
	
	private String readFile(String path) throws IOException {
		// Open file
		File file = new File(path);
		
		// Build a string from file's contents
		StringBuilder content = new StringBuilder((int)file.length());
		
		try (Scanner scanner = new Scanner(file)) {
			while(scanner.hasNextLine()) {
				content.append(scanner.nextLine() + System.lineSeparator());
			}
			return content.toString();
		} 
	}
	
	
	/* ADD, EDIT, DELETE BUTTON METHODS */
	
	public void editAction(ActionEvent e) {
		System.out.println("Edit Button Working");
		String currentSong = song.getText();
		String currentArtist = artist.getText();
		String currentAlbum = album.getText();
		String currentYear = year.getText();
		
		songField.setText(currentSong);
		artistField.setText(currentArtist);
		albumField.setText(currentAlbum);
		yearField.setText(currentYear);
		//add way to confirm edits. the current edit button pops the current characteristics in, but clicking it again will do the same thing. Maybe Confirm Edit Button.
	}
	
	public void addAction(ActionEvent e) {
		int counter=0;
		if(songField.getText().isEmpty() ||artistField.getText().isEmpty()) {
			System.out.println("Must add SongName and Artist");//add popup
			return;
		}
		while(counter<obsList.size()) {
			if(obsList.get(counter).getName().equals(songField.getText()) &&obsList.get(counter).getArtist().equals(artistField.getText())) {
				System.out.println("Already entered");//add popup
				return;
			}
			counter++;
		}
		if(yearField.getText().isEmpty()) {
			Song newadd=new Song(songField.getText(),artistField.getText(),albumField.getText(),-1);
			obsList.add(newadd);
			listView.getSelectionModel().clearSelection();
			obsList.sort((a,b) -> a.getName().compareToIgnoreCase(b.getName())==0 ? a.getArtist().compareToIgnoreCase(b.getArtist()) : a.getName().compareToIgnoreCase(b.getName()));
			listView.getSelectionModel().select(obsList.indexOf(newadd));
			
			return;
		}
		try{	
			Song newadd=new Song(songField.getText(),artistField.getText(),albumField.getText(),Integer.parseInt(yearField.getText()));
			obsList.add(newadd);
			obsList.sort((a,b) -> a.getName().compareToIgnoreCase(b.getName())==0 ? a.getArtist().compareToIgnoreCase(b.getArtist()) : a.getName().compareToIgnoreCase(b.getName()));
			
			return;
		}catch (NumberFormatException f) {
			System.out.println("Year isn't an Int");//add popup
			return;
		}
	}
	
	public void deleteAction(ActionEvent e) {
		obsList.remove(listView.getSelectionModel().getSelectedItem());
	}
	
	
	/* CHANGING DETAILS PANE */
	
	private void showItem(Stage mainStage) {
		Song item = listView.getSelectionModel().getSelectedItem();
		if(item==null) {//if there are no items left or when sorting prevents error
			return;
		}
		song.setText(item.getName());
		artist.setText(item.getArtist());
		if(item.getAlbum().equals("")) {
			album.setText("Not defined");
		}else {
			album.setText(item.getAlbum());
		}
		if(item.getYear()==-1) {
			year.setText("Not defined");
		}else {
			year.setText(Integer.toString(item.getYear()));
		}
	}
	//When program being closed. Save the Songs to the json
}
