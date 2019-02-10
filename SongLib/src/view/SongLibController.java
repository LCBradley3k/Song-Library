package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class SongLibController {
	@FXML
	ListView<String> listView;
	
	private ObservableList<String> obsList;
	
	public void start(Stage mainStage) {
		// create ObservableList from an ArrayList
		obsList = FXCollections.observableArrayList(
			"Thank You, Next",
			"Sunflower",
			"Without Me",
			"Talk",
			"Going Bad",
			"Bohemian Rhapsody",
			"Happier",
			"Chocolate",
			"3 Nights",
			"Harmony Hall"
		);
		listView.setItems(obsList);
		
		listView.getSelectionModel().select(0);
	}
}
