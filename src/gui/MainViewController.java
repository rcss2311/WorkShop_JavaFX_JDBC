package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemSeller;
	@FXML
	private MenuItem menuItemDepartment;
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller ) ->{
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x->{});
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
	}
	
	//metodo necessario para carregar as diversas vbox que serão usadas durante a execução do programa 
	private  synchronized <T> void loadView(String absoluteName, Consumer<T> initializeAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVbox =  loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVbox = (VBox)((ScrollPane)mainScene.getRoot()).getContent();
			
			Node maindMenu =  mainVbox.getChildren().get(0);
			mainVbox.getChildren().clear();
			mainVbox.getChildren().add(maindMenu);
			mainVbox.getChildren().addAll(newVbox.getChildren());
			
			//loadview passa a ser  um metodo com parametrização generica tornando mais flexivel 
			//linhas que inicializam o controlador da expressão lambda envida como argumento
			T controller = loader.getController();
			initializeAction.accept(controller);
	}
		catch (IOException e) {
		Alerts.showAlert("IOexception", "Error load view", e.getMessage(), AlertType.ERROR);
		}
	}
	
}
