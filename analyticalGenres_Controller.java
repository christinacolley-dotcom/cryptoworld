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

public class analyticalGenres_Controller implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML private Label label_mostAction;
    @FXML private Label label_mostWestern;
    @FXML private Label label_mostFantasy;

    public void switchAnalyticalMain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("resources/scenes/analyticalMain.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Statement stmt;
        String sqlStatement;
        ResultSet result;

        try {
            stmt = start.conn.createStatement();
            sqlStatement = "SELECT originaltitle FROM titles WHERE numvotes=(SELECT MAX(numvotes) FROM titles WHERE (titletype LIKE 'movie') AND (genres LIKE 'Western')) AND titletype LIKE 'movie' AND genres LIKE 'Western';";
            result = stmt.executeQuery(sqlStatement);
            while (result.next()) {
                label_mostWestern.setText( result.getString("originaltitle") );
            }

            sqlStatement = "SELECT originaltitle FROM titles WHERE numvotes=(SELECT MAX(numvotes) FROM titles WHERE (titletype LIKE 'movie') AND (genres LIKE 'Fantasy')) AND titletype LIKE 'movie' AND genres LIKE 'Fantasy';";
            result = stmt.executeQuery(sqlStatement);
            result.next();
            label_mostFantasy.setText( result.getString("originaltitle") );

            sqlStatement = "SELECT * FROM titles WHERE numvotes=(SELECT MAX(numvotes) FROM titles WHERE (titletype LIKE 'movie') AND (genres LIKE 'Action')) AND titletype LIKE 'movie' AND genres LIKE 'Action';";
            result = stmt.executeQuery(sqlStatement);
            result.next();
            label_mostAction.setText( result.getString("originaltitle") );


        } catch (Exception e){
            System.out.println("Error Accessing Database!");
        }

    }
}