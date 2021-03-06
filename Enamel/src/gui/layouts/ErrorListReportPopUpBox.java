/**
 * 
 */
package gui.layouts;

import java.io.File;
import java.io.IOException;
import java.util.List;

import gui.controllers.ErrorListReportPopUpBoxController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utility.Language;

/**
 * A pop up box with a list of errors to show.
 * @author Jinho Hwang
 *
 */
public class ErrorListReportPopUpBox {
	
	// control.
	ErrorListReportPopUpBoxController control;
	
	// List of pane, scene, stage
	private AnchorPane root;
	private Scene scene;
	private Stage window;
		
	public ErrorListReportPopUpBox(String title, String label, List<File> errorList) {
		display(title, label, errorList);
	}
	
	public void display(String title, String label, List<File> errorList) {
		
		try {
			// Instantiates new window
			window = new Stage();
			
			// Window should not be re-sizeable ( else destroys our layout )
			window.setResizable(false);
			
			// Make this box a pop up and stops any input until this pop up is resolved ( closed )
			window.initModality(Modality.APPLICATION_MODAL);
			
			// Get the FXML loader.
			FXMLLoader loader = new FXMLLoader(getClass().getResource(Language.errorListReportPopUpBoxFxml));
			
			// Loading the format from FXML file
			root = (AnchorPane)loader.load();
			
			// Get controller.
			control = loader.getController();
			
			control.setErrorList(errorList);
			control.setLabel(label);
			
			// Instantiate a new scene
			scene = new Scene(root);
			
			// Set window the scene and title
			window.setScene(scene);
			window.setTitle(title);
			
			// If close button (red X button) is pressed, hide the window instead of destroy
			window.setOnCloseRequest(e -> control.close());
			
			window.show();
			
		} catch (IOException e1) {
			e1.printStackTrace(); // This happens if scenarioEditor.fxml changes its name.
		}
		
		
	}
}
