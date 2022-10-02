package group3.javagui;

import java.io.IOException;

import java.net.URL;

import java.sql.*;

import java.util.ResourceBundle;

import javafx.stage.Stage;

import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;

import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
 
public class login_Controller implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    @FXML private ChoiceBox dropdown_UserID;
    @FXML private Button btn_Continue;


    @Override
	public void initialize(URL location, ResourceBundle resources) {
        try {
            Statement stmt = start.conn.createStatement();
            
            String sqlStatement = "SELECT DISTINCT customerid FROM customer_ratings;";
            
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {
                dropdown_UserID.getItems().add( result.getString("customerid") );
            }
        } catch (Exception e){
            System.out.println("Error Accessing Database!");
        }

	}

    public void continueLogin(ActionEvent event) throws IOException {
        start.currentCustomerID = (String) dropdown_UserID.getValue();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/scenes/mainScreen.fxml"));
        Parent root = loader.load();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

    }
}