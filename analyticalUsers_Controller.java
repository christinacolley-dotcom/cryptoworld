package group3.javagui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;

public class analyticalUsers_Controller implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML private Label label_numofusers;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Statement stmt;
        String sqlStatement;
        ResultSet result;

        try {
            stmt = start.conn.createStatement();
            sqlStatement = "SELECT COUNT (DISTINCT customerid) as users FROM customer_ratings;";
            result = stmt.executeQuery(sqlStatement);
            while (result.next()) {
                label_numofusers.setText(result.getString("users"));
            }
        } catch (Exception e){
            System.out.println("Error Accessing Database!");
        }
    }

    @FXML
    void switchToFreshTomato(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("resources/scenes/analyticalFreshTomato.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchAnalyticalMain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("resources/scenes/analyticalMain.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

