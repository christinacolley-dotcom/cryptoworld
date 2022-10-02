package group3.javagui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class analyticalCultClassics_Controller implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label lbl1;

    @FXML
    private Label lbl10;

    @FXML
    private Label lbl2;

    @FXML
    private Label lbl3;

    @FXML
    private Label lbl4;

    @FXML
    private Label lbl5;

    @FXML
    private Label lbl6;

    @FXML
    private Label lbl7;

    @FXML
    private Label lbl8;

    @FXML
    private Label lbl9;


    @Override
	public void initialize(URL location, ResourceBundle resources) 
	{
        ArrayList<String> cultClassics = new ArrayList<String>();
        ArrayList<Label> lbls = new ArrayList<Label>();
        lbls.add(lbl1);
        lbls.add(lbl2);
        lbls.add(lbl3);
        lbls.add(lbl4);
        lbls.add(lbl5);
        lbls.add(lbl6);
        lbls.add(lbl7);
        lbls.add(lbl8);
        lbls.add(lbl9);
        lbls.add(lbl10);

        Statement stmt = null;
        try {
            stmt = start.conn.createStatement();
            String sqlStatement = "SELECT customer_ratings.titleid,titles.originaltitle,COUNT(customer_ratings.titleid) FROM customer_ratings INNER JOIN titles ON customer_ratings.titleid=titles.titleid WHERE rating >= 4 GROUP BY customer_ratings.titleid,titles.originaltitle ORDER BY count DESC LIMIT 10;";
            ResultSet result = stmt.executeQuery(sqlStatement);
            while (result.next()) {
                cultClassics.add(result.getString("originaltitle"));
            }
            for (int i = 0; i < 10; i++) {
                lbls.get(i).setText(cultClassics.get(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

    @FXML
    public void switchAnalyticalMovies(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("resources/scenes/analyticalMovies.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}