package group3.javagui;

import java.io.IOException;

import java.net.URL;

import java.sql.*;

import java.util.ResourceBundle;
import java.util.ArrayList;

import javafx.scene.text.Font;
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
 
public class analyticalMovies_Controller implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    private String selectedYear;
    
    @FXML private Label topTen_One;
    @FXML private Label topTen_Two;
    @FXML private Label topTen_Three;
    @FXML private Label topTen_Four;
    @FXML private Label topTen_Five;
    @FXML private Label topTen_Six;
    @FXML private Label topTen_Seven;
    @FXML private Label topTen_Eight;
    @FXML private Label topTen_Nine;
    @FXML private Label topTen_Ten;

    @FXML private Label label_topRatedMovie;
    @FXML private Label label_mostPopularHalloween;

    Font font = Font.loadFont("NetflixSansMedium.woff", 4);

    @FXML private ChoiceBox choiceBox_SelectYear;

    ArrayList< Label > labels_MostWatched = new ArrayList< Label >();
    ArrayList<String> mostWatched = new ArrayList<String>();

    @Override
	public void initialize(URL location, ResourceBundle resources) 
	{
        labels_MostWatched.add(topTen_One);
        labels_MostWatched.add(topTen_Two);
        labels_MostWatched.add(topTen_Three);
        labels_MostWatched.add(topTen_Four);
        labels_MostWatched.add(topTen_Five);
        labels_MostWatched.add(topTen_Six);
        labels_MostWatched.add(topTen_Seven);
        labels_MostWatched.add(topTen_Eight);
        labels_MostWatched.add(topTen_Nine);
        labels_MostWatched.add(topTen_Ten);

        Statement stmt;
        String sqlStatement;
        ResultSet result;

        try {
            stmt = start.conn.createStatement();
            sqlStatement = "SELECT * FROM titles WHERE titletype LIKE 'movie' ORDER BY numvotes DESC LIMIT 10;";
            result = stmt.executeQuery(sqlStatement);
            while (result.next()) {
                mostWatched.add( result.getString("originaltitle") );
            }

            sqlStatement = "SELECT * FROM titles WHERE averagerating=(SELECT MAX(averagerating) FROM titles WHERE titletype LIKE 'movie') AND titletype LIKE 'movie';";
            result = stmt.executeQuery( sqlStatement );
            while ( result.next() ) {
                label_topRatedMovie.setText( result.getString("originalTitle") );
            }

            sqlStatement = "SELECT originaltitle from titles WHERE titleId = (SELECT titleId FROM customer_ratings WHERE date LIKE '%-10-31' GROUP BY titleId ORDER BY COUNT(*) DESC LIMIT 1);\n";
            result = stmt.executeQuery( sqlStatement );
            while ( result.next() ) {
                label_mostPopularHalloween.setText( result.getString("originalTitle") );
            }
        } catch (Exception e){
            System.out.println("Error Accessing Database!");
        }

        for ( int i = 1900; i < 2021; i++ ) {
            choiceBox_SelectYear.getItems().add( i );
        }

        choiceBox_SelectYear.setOnAction((event) -> { changeYear(); });

        updateMostWatched();



	}

    @FXML
    public void switchAnalyticalMain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("resources/scenes/analyticalMain.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void updateMostWatched() {
        for ( int i = 0; i < Math.min(10, mostWatched.size()); i++ ) {
            labels_MostWatched.get(i).setText( i+1 + ". " + mostWatched.get(i) );
        }
    }

    private void changeYear() {
        selectedYear = String.valueOf(choiceBox_SelectYear.getValue());
        
        mostWatched.clear();

        try {
            Statement stmt = start.conn.createStatement();
            
            String sqlStatement = "SELECT * FROM titles WHERE startyear="+ selectedYear +" AND titletype LIKE 'movie' ORDER BY numvotes DESC  LIMIT 10;";
            System.out.println( sqlStatement );

            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {
                mostWatched.add( result.getString("originaltitle") );
            }

            updateMostWatched();
        } catch (Exception e){
            //JOptionPane.showMessageDialog(null,"Error accessing Database.");
        }
    }

    @FXML
    void switchCultClassics(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("resources/scenes/analyticalCultClassics.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}