package gui.controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import gui.layouts.CreateCommandPopUpBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utility.AuthoringUtil;
import utility.Language;
import utility.Phrase;
/**
 * Scenario maker controller.
 * @author Jinho Hwang
 *
 */
public class ScenarioMakerController {

    @FXML
    private AnchorPane root;

    @FXML
    private ListView<Phrase> listOfCommands;

    @FXML
    private TextField scenarioNameField;

    @FXML
    private TextField numCellTextField;

    @FXML
    private TextField numButtonTextField;

    @FXML
    private Button saveButton;

    @FXML
    private Button createCommandButton;

    @FXML
    private ToggleGroup CommandPosGroup;

    @FXML
    private Button removeCommandButton;

    @FXML
    private Button moveUpButton;

    @FXML
    private Button moveDownButton;

    @FXML
    private RadioButton replace;

    @FXML
    private RadioButton below;
 	
    @FXML
    private RadioButton above;
    
    @FXML
    private Button exitButton;
    
 	File scenarioFile;
 	
 	List<File> scenarioList;
 	
 	List<Phrase> phraseList;
 	
 	List<String> scenarioNameList;
 	
 	ObservableList<Phrase> phraseListObs;
 	
 	ObservableList<String> numberOfCellAndButton;
 	
 	public ScenarioMakerController(){
 		phraseListObs = FXCollections.observableArrayList();
 		numberOfCellAndButton = FXCollections.observableArrayList();
 		phraseList = new LinkedList<Phrase>();
 	}
 	
 	public void setScenarioList(List<File> scenarioList) {
 		this.scenarioList = scenarioList;
 	}
 	
 	public void setScenarioNameList(List<String> scnearioNameList) {
 		this.scenarioNameList = scnearioNameList;
 	}
 	
 	public void setFile(File file) {
 		// Set this scenario maker with this file.
 		this.scenarioFile = file;
 		
 		// Set the scenario name.
 		scenarioNameField.setText(file.getName());
 		
 		// Bring the list of Phrases.
 		List<Phrase> lawPhraseList = AuthoringUtil.phraseScenario(file);
 		
 		// If scenario load was successful,
 		if( lawPhraseList != null) {
 			
	 		// Gets first two phrase containing num cell and buttons.
	 		Phrase cellPhrase = lawPhraseList.get(0);
	 		Phrase buttonPhrase = lawPhraseList.get(1);
	 		
	 		// Add those cell # and button # to obsList for text fields.
	 		numberOfCellAndButton.add(cellPhrase.getArguments()[0]);
	 		numberOfCellAndButton.add(buttonPhrase.getArguments()[0]);
	 		
	 		// Set cell # and button # on text fields.
	 		numCellTextField.setText(numberOfCellAndButton.get(0));
	 		numButtonTextField.setText(numberOfCellAndButton.get(1));
	 		
	 		if(lawPhraseList.size() > 2) {
	 			// Set phraseList to be rest of phrase list other then first two.
	 			phraseList.addAll(lawPhraseList.subList(2, lawPhraseList.size()));
	 		}
	 		
	 		listUpdate();
	 		
	 		// Set the gui list.
	 		listOfCommands.setItems(phraseListObs);
 		}
 		
 	}
 	
 	public void exit() {
 		Stage window = (Stage)root.getScene().getWindow();
 		window.close();
 	}
 	
    private boolean isItemSelected() {
    	return selectedItemIndex() != -1;
    }
    
    private int selectedItemIndex() {
    	return listOfCommands.getSelectionModel().getSelectedIndex();
    }
    
    /*private Phrase selectedItem() {
    	return listOfCommands.getSelectionModel().getSelectedItem();
    }*/
 	
    private void listUpdate() {
    	this.phraseListObs.clear();
    	
    	for(Phrase phrase : phraseList) {
    		phraseListObs.add(phrase);
    	}
    	
    	listOfCommands.setItems(phraseListObs);
    }
    
 	public void createCommand() {
 		
 		
 		if(!isItemSelected()) {
 			CreateCommandPopUpBox popup = new CreateCommandPopUpBox(phraseList,"above",0);
 			phraseList = popup.display();
 		}else if(isItemSelected()) {
	 		
	 		String pos = "";
	 		
	 		if(above.isSelected()) {
	 			pos = "above";
	 			
	 		}else if(replace.isSelected()) {
	 			pos = "replace";
	 			
	 		}else if(below.isSelected()) {
	 			pos = "below";
	 			
	 		}
	 		CreateCommandPopUpBox popup = new CreateCommandPopUpBox(phraseList,pos,selectedItemIndex());
	 		phraseList = popup.display();
	 		
 		}
 		
 		listUpdate();
 		
 		
 	}
 	
 	public void removeCommand() {
 		if(isItemSelected()) {
 			phraseList.remove(selectedItemIndex());
 			listUpdate();
 		}
 	}
 	
 	private void swap(int pos1, int pos2) {
 		Phrase temp = phraseListObs.get(pos1);
 		phraseList.set(pos1, phraseListObs.get(pos2));
 		phraseList.set(pos2, temp);
 	}
 	
 	public void moveUp() {
 		if(isItemSelected()) {
 			if(selectedItemIndex() > 0) {
 				int index = selectedItemIndex()-1;
 				swap(selectedItemIndex(), selectedItemIndex()-1);
 				listUpdate();
 				listOfCommands.getSelectionModel().select(index);
 			}
 		}
 	}
 	
 	public void moveDown() {
 		if(isItemSelected()) {
 			if(selectedItemIndex() < phraseListObs.size()-1) {
 				int index = selectedItemIndex()+1;
 				swap(selectedItemIndex(), selectedItemIndex()+1);
 				listUpdate();
 				listOfCommands.getSelectionModel().select(index);
 			}
 		}
 	}
 	
 	public void save() {
 		
 		if(!numCellTextField.getText().isEmpty() &&
 		   !numCellTextField.getText().isEmpty()  &&
 		   !numButtonTextField.getText().isEmpty() ) {
 			
			String scenarioString = "";
			
			scenarioString += "Cell " + numCellTextField.getText() + "\n";
			scenarioString += "Button " + numButtonTextField.getText() + "\n";
			
			for(Phrase phrase : phraseList) {
				scenarioString += phrase + "\n";
			}
			
			
			if(AuthoringUtil.phraseScenario(scenarioString) != null) {
				
				String fileName = scenarioNameField.getText();
				
				if(fileName.length() < 5) {
					if(!fileName.equals(".txt"))
						fileName += ".txt";
				}else {
					if(!fileName.substring(fileName.length()-4, fileName.length()).equals(".txt")) {
						fileName += ".txt";
					}
				}
				
				if(scenarioNameList.contains(fileName)) {
					while(scenarioNameList.contains(fileName)) {
						fileName = fileName.split("\\.")[0] + "_m.txt";
					}
				}
				File file = new File(Language.scenarioPath + fileName);
				
				try {
					Writer fileWriter = new FileWriter(file);
					fileWriter.write(scenarioString);
					fileWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				scenarioList.add(file);
				scenarioNameList.add(file.getName());
				
				exit();
			}else {
				// TODO : Error occurred.
			}
	 		
	 	}
 	}
 	
 	@FXML
 	void keyPressed(KeyEvent event) {
    	if(event.getCode().equals(Language.openKey)) {
    		if(event.getSource().equals(createCommandButton)) {
    			createCommand();
    		}else if(event.getSource().equals(removeCommandButton)) {
    			removeCommand();
    		}else if(event.getSource().equals(moveUpButton)) {
    			moveUp();
    		}else if(event.getSource().equals(moveDownButton)) {
    			moveDown();
    		}else if(event.getSource().equals(saveButton)) {
    			save();
    		}else if(event.getSource().equals(exitButton)) {
    			exit();
    		}
    	}
    }
 	
 	
}