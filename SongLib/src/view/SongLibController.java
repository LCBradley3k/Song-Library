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

public class SongLibController {
	@FXML ListView<String> listView;
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
	
	
	private ObservableList<String> obsList;
	
	public void start(Stage mainStage) {
		
		// Open JSON content as string
		try {
			String content = readFile("src/data/songs.json");
			JSONObject song_obj = new JSONObject(content);
			JSONArray song_info = song_obj.getJSONArray("songs");
			
			ArrayList<String> song_names = new ArrayList<String>();
			
			for(int i=0; i<song_info.length(); i++) {
				song_names.add(song_info.getJSONObject(i).getString("name"));
			}
			
			// create ObservableList from an ArrayList
			obsList = FXCollections.observableArrayList(song_names);
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
		String firstSong = listView.getSelectionModel().getSelectedItem();
		song.setText(firstSong);
		
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
		/* To Do */
	}
	
	public void addAction(ActionEvent e) {
		System.out.println("Add Button Working");
		/* To Do */
	}
	
	public void deleteAction(ActionEvent e) {
		System.out.println("Delete Button Working");
		/* To Do */
		obsList.remove(listView.getSelectionModel().getSelectedItem());
	}
	
	
	/* CHANGING DETAILS PANE */
	
	private void showItem(Stage mainStage) {
		String item = listView.getSelectionModel().getSelectedItem();
		song.setText(item);
	}
}
