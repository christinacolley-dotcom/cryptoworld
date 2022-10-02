package group3.javagui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class analyticalPairs_Controller implements Initializable {
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
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList< Label > lbls = new ArrayList< Label >();
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

        try {
            Statement getRatings = start.conn.createStatement();
            String getRatingsQ = "SELECT titleid,averagerating FROM titles;";
            ResultSet ratingsResult = getRatings.executeQuery(getRatingsQ);
            HashMap<String, Float> titleRatings = new HashMap<String, Float>();
            while (ratingsResult.next()) {
                titleRatings.put(ratingsResult.getString("titleid"), Float.parseFloat(ratingsResult.getString("averageRating")));
            }

            Statement stmt = start.conn.createStatement();
            String sqlStatement = "SELECT * FROM pairs;"; // change this to actual sql query
            ResultSet result = stmt.executeQuery(sqlStatement);
            HashMap<String, String> pairTitles = new HashMap<String, String>(); // check that this is correct
            HashMap<String, Double> pairAverages = new HashMap<String, Double>();
            int count = 0;
            ArrayList< Double > scoresSort = new ArrayList< Double >();
            while (result.next()) {
                System.out.println(count + "/" + "295380");
                String pairs = result.getString("actors");
                String[] titlesForPairs = result.getString("titleid").split(","); // change this to ArrayList
                ArrayList<String> titlesCleaned = new ArrayList<String>();
                for (int i = 0; i < titlesForPairs.length; i++) {
                    if (titlesForPairs[i].length() > 0) {
                        titlesCleaned.add(titlesForPairs[i]);
                    }
                }
                double average = 0.00;
                for (String title : titlesCleaned) {
                    average += titleRatings.get(title);
                }
                average = average/titlesCleaned.size();
                pairAverages.put(pairs, average);
                count ++;
                scoresSort.add(average);
            }
            scoresSort.sort(Collections.reverseOrder());
            ArrayList< String > maxPairs = new ArrayList< String >();

            for (int i = 0; i < 10; i++) {
                for (Map.Entry<String, Double> entry : pairAverages.entrySet()) {
                    if (Objects.equals(scoresSort.get(i), entry.getValue())) {
                        maxPairs.add(entry.getKey());
                        pairAverages.remove(entry.getKey());
                        break;
                    }
                }
            }
            int cnt = 0;
            for(String pairs : maxPairs) {
                System.out.println(Arrays.toString(pairs.split("'")));
                String title1 = pairs.split("'")[1].split(" ")[0];
                String title2 = pairs.split("'")[3].split(" ")[0];

                Statement stmtTitles1 = start.conn.createStatement();
                String sqlTitles1 = "SELECT nconst, primaryname from names where nconst=('" + title1 + "');"; // change this to actual sql query
                ResultSet resultTitles1 = stmtTitles1.executeQuery(sqlTitles1);
                while(resultTitles1.next()) {
                    title1 = resultTitles1.getString("primaryname");
                }

                Statement stmtTitles2 = start.conn.createStatement();
                String sqlTitles2 = "SELECT nconst, primaryname from names where nconst=('" + title2 + "');"; // change this to actual sql query
                ResultSet resultTitles2 = stmtTitles2.executeQuery(sqlTitles2);
                while(resultTitles2.next()) {
                    title2 = resultTitles2.getString("primaryname");
                }


                lbls.get(cnt).setText(title1 + " " + title2);
                cnt++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
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