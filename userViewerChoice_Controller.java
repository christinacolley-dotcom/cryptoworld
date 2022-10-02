package group3.javagui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.image.AreaAveragingScaleFilter;
import java.sql.SQLException;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;

public class userViewerChoice_Controller implements Initializable {
    private Stage stage;
    private Scene scene;
    @FXML
    private ImageView img1;

    @FXML
    private ImageView img10;

    @FXML
    private ImageView img2;

    @FXML
    private ImageView img3;

    @FXML
    private ImageView img4;

    @FXML
    private ImageView img5;

    @FXML
    private ImageView img6;

    @FXML
    private ImageView img7;

    @FXML
    private ImageView img8;

    @FXML
    private ImageView img9;

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
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList< ImageView > imgs = new ArrayList< ImageView >();
        ArrayList< Label > lbls = new ArrayList< Label >();
        imgs.add(img1);
        imgs.add(img2);
        imgs.add(img3);
        imgs.add(img4);
        imgs.add(img5);
        imgs.add(img6);
        imgs.add(img7);
        imgs.add(img8);
        imgs.add(img9);
        imgs.add(img10);
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
        String userID = start.currentCustomerID; // provide the user ID
        HashMap<String, Double> genre_scores = new HashMap<String, Double>();
        try {
            Statement stmt = start.conn.createStatement();
            String sqlStatement = "SELECT genres FROM titles WHERE titleid IN (SELECT titleid FROM customer_ratings WHERE customerid='" + userID + "');";
            ResultSet result = stmt.executeQuery(sqlStatement);

            while(result.next()) {
                System.out.println(result.getString("genres"));
                if (result.getString("genres") == null) {
                    continue;
                }
                String[] genres = result.getString("genres").split(",");
                for (int i = 0; i < genres.length; i++) {
                    if (!genre_scores.containsKey(genres[i])) {
                        // add key, val to genre_scores
                        genre_scores.put(genres[i], 0.0);
                    }
                    if (i == 0){
                        // key, val+=1
                        genre_scores.put(genres[i], genre_scores.get(genres[i]) + 1);
                    } else {
                        // key, val+=.5
                        genre_scores.put(genres[i], genre_scores.get(genres[i]) + 0.5);
                    }
                }
            }
            System.out.println("done putting key, val into HashMap");
            // get all scores in an array, sort from max->min. top 3 are the wanted scores f or the next for loop
            ArrayList< Double > scoresSort = new ArrayList< Double >();
            for (Map.Entry<String, Double> entry : genre_scores.entrySet()) {
                scoresSort.add(entry.getValue());
            }
            scoresSort.sort(Collections.reverseOrder());
            for (Double aDouble : scoresSort) {
                System.out.println(aDouble);
            }

            // get the first max
            ArrayList< String > maxGenres = new ArrayList< String >();
            genre_scores.forEach((key, value) -> System.out.println(key + " " + value));

            for (int i = 0; i < 3; i++) {
                for (Map.Entry<String, Double> entry : genre_scores.entrySet()) {
                    if (Objects.equals(scoresSort.get(i), entry.getValue())) {
                        maxGenres.add(entry.getKey());
                        genre_scores.remove(entry.getKey());
                        break;
                    }
                }
            }
            ArrayList< String > rec1 = new ArrayList< String >();
            ArrayList< String > rec2 = new ArrayList< String >();
            ArrayList< String > rec3 = new ArrayList< String >();
            String recSQL1 = "SELECT originaltitle,genres FROM (SELECT * FROM titles WHERE (genres LIKE '%" + maxGenres.get(0) + "%')  ORDER BY numvotes DESC LIMIT 100) AS top100 ORDER BY random() LIMIT 10;";
            String recSQL2 = "SELECT originaltitle,genres FROM (SELECT * FROM titles WHERE (genres LIKE '%" + maxGenres.get(1) + "%')  ORDER BY numvotes DESC LIMIT 100) AS top100 ORDER BY random() LIMIT 10;";
            String recSQL3 = "SELECT originaltitle,genres FROM (SELECT * FROM titles WHERE (genres LIKE '%" + maxGenres.get(2) + "%')  ORDER BY numvotes DESC LIMIT 100) AS top100 ORDER BY random() LIMIT 10;";

            Statement recStmt1 = start.conn.createStatement();
            ResultSet recResult1 = recStmt1.executeQuery(recSQL1);
            while (recResult1.next()) {
                rec1.add(recResult1.getString("originaltitle"));
            }

            Statement recStmt2 = start.conn.createStatement();
            ResultSet recResult2 = recStmt2.executeQuery(recSQL2);
            while (recResult2.next()) {
                rec2.add(recResult2.getString("originaltitle"));
            }

            Statement recStmt3 = start.conn.createStatement();
            ResultSet recResult3 = recStmt3.executeQuery(recSQL3);
            while (recResult3.next()) {
                rec3.add(recResult3.getString("originaltitle"));
            }

            double totalPoints = scoresSort.get(0) + scoresSort.get(1) + scoresSort.get(2);
            int calc1 =  (int) Math.round((scoresSort.get(0) / totalPoints) * 10);
            int calc2 = (int) Math.round((scoresSort.get(1) / totalPoints) * 10);

            for (int i = 0; i < calc1; i++) {
                lbls.get(i).setText(rec1.get(i));
                imgs.get(i).setImage(getImg(rec1.get(i)));
            }
            for (int i = calc1; i < calc2+calc1; i++) {
                lbls.get(i).setText(rec2.get(i));
                imgs.get(i).setImage(getImg(rec2.get(i)));
            }
            for (int i = calc2+calc1; i < 10; i++) {
                lbls.get(i).setText(rec3.get(i));
                imgs.get(i).setImage(getImg(rec3.get(i)));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    Image getImg(String titleName) {
        String titleURL = getImageURL(titleName);
        try {
            // Create URL object
            URL url = new URL(titleURL);
            BufferedReader readr =
                    new BufferedReader(new InputStreamReader(url.openStream()));

            // read each line from stream till end
            String line = "";
            boolean srcAvailable = false;
            while ((line = readr.readLine()) != null) {
                if (line.contains("class=\"poster\" src=\"")) {
                    srcAvailable = true;
                    break;
                }
                line = "";
            }

            if (srcAvailable) {
                String imageExtension = line.split("src=\"")[1].split("\"")[0];
                String imageSource = "https://www.themoviedb.org" + imageExtension;
                Image image = new Image(imageSource);
//                img1.setImage(image);
                return image;
            }
            readr.close();
            System.out.println("Successfully Downloaded.");
        }
        // Exceptions
        catch (MalformedURLException mue) {
            System.out.println("Malformed URL Exception raised");
        }
        catch (IOException ie) {
            System.out.println("IOException raised");
        }
        return null;

    }
    String getImageURL (String name){
        String url = "https://www.themoviedb.org/search?query=";
        for (String i : name.split(" ")) {
            url+=i;
            url+= "%20";
        }
        url = url.substring(0, url.length()-3);
        return url;
    }

    @FXML
    void backToHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("resources/scenes/mainScreen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
