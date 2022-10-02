package group3.javagui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class analyticalShows_Controller implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML private Label label_highestRatedShow;
    @FXML private Label label_oldestShow;
    @FXML private Label label_mostWatched;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Statement stmt;
        String sqlStatement;
        ResultSet result;

        try {
            stmt = start.conn.createStatement();
            sqlStatement = "SELECT * FROM titles WHERE averagerating=(SELECT MAX(averagerating) FROM titles WHERE titletype LIKE 'tvSeries') AND titletype LIKE 'tvSeries';";
            result = stmt.executeQuery(sqlStatement);
            while (result.next()) {
                label_mostWatched.setText( result.getString("originaltitle") );
            }

            sqlStatement = "SELECT * FROM titles WHERE averagerating=(SELECT MAX(averagerating) FROM titles WHERE titletype LIKE 'tvEpisode') AND titletype LIKE 'tvEpisode' ORDER BY numvotes DESC;";
            result = stmt.executeQuery(sqlStatement);
            result.next();
            label_highestRatedShow.setText( result.getString("originaltitle") );

            sqlStatement = "SELECT * FROM titles WHERE startyear=(SELECT MIN(startyear) FROM titles WHERE titletype LIKE 'tvSeries') AND titletype LIKE 'tvSeries';";
            result = stmt.executeQuery(sqlStatement);
            result.next();
            label_oldestShow.setText( result.getString("originaltitle") );


        } catch (Exception e){
            System.out.println("Error Accessing Database!");
        }
    }


    public void switchAnalyticalMain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("resources/scenes/analyticalMain.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}