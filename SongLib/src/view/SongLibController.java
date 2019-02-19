//made by Brad Mitchell and Arth Patel
package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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
	private boolean	first;

	public void start(Stage mainStage) {
		
		// Open JSON content as string
		try {
			String content = readFile("src/data/songs.json");
			/* ORIGINAL WAY OF DOING IT
			JSONObject song_obj = new JSONObject(content);
			JSONArray song_info = song_obj.getJSONArray("songs");
			
			ArrayList<Song> songs = new ArrayList<Song>();
			
			
			for(int i=0; i<song_info.length(); i++) {
				Song fromstorage =new Song(song_info.getJSONObject(i).getString("name"),song_info.getJSONObject(i).getString("artist"), song_info.getJSONObject(i).getString("album"),song_info.getJSONObject(i).getInt("Year"));
				songs.add(fromstorage);
			} */
			
			JSONArray song_info = new JSONArray(content);
			ArrayList<Song> songs = new ArrayList<Song>();
			
			for(int i=0; i<song_info.length(); i++) {
				Song fromstorage =new Song(song_info.getJSONObject(i).getString("name"),song_info.getJSONObject(i).getString("artist"), song_info.getJSONObject(i).getString("album"),song_info.getJSONObject(i).getInt("year"));
				songs.add(fromstorage);
			}
			
			
			// create ObservableList from an ArrayList
			obsList = FXCollections.observableArrayList(songs);
			listView.setItems(obsList);
			first=true;
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
		Song firstSong = listView.getSelectionModel().getSelectedItem();
		if(firstSong==null) {//no items left
			song.setText("");
			artist.setText("");
			album.setText("");
			year.setText("");
			
		}else {
		// Pre-set the data to the first selected.
			
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
	
	public void editAction(ActionEvent e) throws IOException {
		if(first) {//if its on a new item and edit hasn't been clicked before
			String currentSong = song.getText();
			String currentArtist = artist.getText();
			String currentAlbum = album.getText();
			String currentYear = year.getText();
			songField.setText(currentSong);
			artistField.setText(currentArtist);
			edit.setText("Save");
			add.setDisable(true);
			delete.setDisable(true);
			if(currentAlbum.equals("Not defined")) {
				currentAlbum="";
			}
			if(currentYear.equals("Not defined")) {
				currentYear="";
			}
			albumField.setText(currentAlbum);
			yearField.setText(currentYear);
			first=false;
		}else {
			int counter=0;
			if(songField.getText().isEmpty() ||artistField.getText().isEmpty()) {
				createErrorAlert("Error During Edit", "Must add song name and artist!");
				return;
			}
			while(counter<obsList.size()) {
				if(obsList.get(counter).getName().equalsIgnoreCase(songField.getText()) &&obsList.get(counter).getArtist().equalsIgnoreCase(artistField.getText())&&counter!=obsList.indexOf(listView.getSelectionModel().getSelectedItem())) {
					createErrorAlert("Error During Edit", "Already Entered");
					return;
				}
				counter++;
			}
			if(yearField.getText().isEmpty()) {
				Song newadd=new Song(songField.getText(),artistField.getText(),albumField.getText(),-1);
				obsList.remove(listView.getSelectionModel().getSelectedItem());
				obsList.add(newadd);
				listView.getSelectionModel().clearSelection();
				obsList.sort((a,b) -> a.getName().compareToIgnoreCase(b.getName())==0 ? a.getArtist().compareToIgnoreCase(b.getArtist()) : a.getName().compareToIgnoreCase(b.getName()));
				listView.getSelectionModel().select(obsList.indexOf(newadd));
			}else {
				try{	
					Song newadd=new Song(songField.getText(),artistField.getText(),albumField.getText(),Integer.parseInt(yearField.getText()));
					obsList.remove(listView.getSelectionModel().getSelectedItem());
					obsList.add(newadd);
					obsList.sort((a,b) -> a.getName().compareToIgnoreCase(b.getName())==0 ? a.getArtist().compareToIgnoreCase(b.getArtist()) : a.getName().compareToIgnoreCase(b.getName()));
				
				}catch (NumberFormatException f) {
					createErrorAlert("Error During Edit", "Year must be an integer!");
					return;
				}
			}
			saveSongs(obsList);
			edit.setText("Edit");
			add.setDisable(false);
			delete.setDisable(false);
		}

	}
	
	public void addAction(ActionEvent e) throws IOException {
		int counter=0;
		if(songField.getText().isEmpty() ||artistField.getText().isEmpty()) {
			createErrorAlert("Error During Add", "Must add SongName and Artist");
			return;
		}
		while(counter<obsList.size()) {
			if(obsList.get(counter).getName().equalsIgnoreCase(songField.getText()) &&obsList.get(counter).getArtist().equalsIgnoreCase(artistField.getText())) {
				createErrorAlert("Error During Add", "Song already exists!");
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
			
			saveSongs(obsList);
			return;
			
		}
		try{	
			Song newadd=new Song(songField.getText(),artistField.getText(),albumField.getText(),Integer.parseInt(yearField.getText()));
			obsList.add(newadd);
			obsList.sort((a,b) -> a.getName().compareToIgnoreCase(b.getName())==0 ? a.getArtist().compareToIgnoreCase(b.getArtist()) : a.getName().compareToIgnoreCase(b.getName()));

		}catch (NumberFormatException f) {
			createErrorAlert("Error During Add", "Year must be an integer!");
			return;
		}
		
		saveSongs(obsList);
	}
	
	public void deleteAction(ActionEvent e) throws IOException {
		if(obsList.size() == 0) {
			createErrorAlert("Error", "No songs to delete!");
		} else {
			int temp=obsList.indexOf(listView.getSelectionModel().getSelectedItem());
			obsList.remove(listView.getSelectionModel().getSelectedItem());
			listView.getSelectionModel().select(temp);
			Song item = listView.getSelectionModel().getSelectedItem();
		
			if(item==null) {//no items left
				saveSongs(obsList);
				song.setText("");
				artist.setText("");
				album.setText("");
				year.setText("");
				return;
			}
			first=true;
	
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
			
			
			saveSongs(obsList);
		}
		
	}
	
	
	/* CHANGING DETAILS PANE */
	
	private void showItem(Stage mainStage) {
		Song item = listView.getSelectionModel().getSelectedItem();
		first=true;
		add.setDisable(false);
		delete.setDisable(false);
		edit.setText("Edit");
		if(item==null) {
			return;
		}
		songField.setText("");
		artistField.setText("");
		albumField.setText("");
		yearField.setText("");
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
	
	/* Error Functions */
	public void createErrorAlert(String errorTitle, String errorMessage) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(errorTitle);
		alert.setHeaderText(null);
		alert.setContentText(errorMessage);
		alert.showAndWait();
	}
	
	public void saveSongs(ObservableList<Song> songList) throws IOException {
		JSONArray songs = new JSONArray(obsList);
		try (FileWriter file = new FileWriter("src/data/songs.json")) {
			file.write(songs.toString());
			System.out.println("Successfully Copied JSON Object to File...");
			System.out.println("\nJSON Object: " + songs);
		}
	}
}
