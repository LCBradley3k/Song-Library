package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
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
	@FXML
	ListView<String> listView;
	
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
			
			listView.getSelectionModel().select(0);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
}
