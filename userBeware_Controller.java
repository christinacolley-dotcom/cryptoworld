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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class userBeware_Controller implements Initializable {
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
        ArrayList< Label > labels = new ArrayList< Label >();

        try {
            labels.add(lbl1);
            labels.add(lbl2);
            labels.add(lbl3);
            labels.add(lbl4);
            labels.add(lbl5);
            labels.add(lbl6);
            labels.add(lbl7);
            labels.add(lbl8);
            labels.add(lbl9);
            labels.add(lbl10);
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
            Statement stmt = start.conn.createStatement();
            String sqlStatement = "select customerid,string_agg(titleid, ',') as badtitles from customer_ratings where rating < 3 group by customerid;"; // get every user with their watched mo
            String currUser = "select customerid,string_agg(titleid, ',') as badtitles from customer_ratings where rating < 3 and customerid=('" + start.currentCustomerID + "') group by customerid;";
            ResultSet currUserResults = stmt.executeQuery(currUser);
            List currUserBadTitles = null;
            while(currUserResults.next()) {
                currUserBadTitles = List.of(currUserResults.getString("badtitles").split(","));
            }
            ResultSet result = stmt.executeQuery(sqlStatement);
            int max = 0;
            String maxID = "";
            while (result.next()) {
                int currPoints = 0;
                String [] listOfBad =  result.getString("badtitles").split(",");
                if (Objects.equals(result.getString("customerid"), start.currentCustomerID)) {
                    continue;
                }
                for (String s : listOfBad) {
                    if (currUserBadTitles.contains(s)) {
                        currPoints++;
                    }
                }
                if (currPoints > max) {
                    max = currPoints;
                    maxID = result.getString("customerid");
                }
            }
            String userAlike = "select customerid,string_agg(titleid, ',') as badtitles from customer_ratings where rating < 3 and customerid=('" + maxID + "') group by customerid;";
            ResultSet userAlikeResults = stmt.executeQuery(userAlike);
            List secondUserList = null;
            while(userAlikeResults.next()) {
                secondUserList = List.of(userAlikeResults.getString("badtitles").split(","));
            }
            ArrayList<String> recs = new ArrayList<String>();
            for (Object s : secondUserList) {
                String another = (String) s;
                if (!currUserBadTitles.contains(another)) {
                    recs.add(another);
                }
            }

            String finalCommand = "select customer_ratings.titleid,titles.originaltitle from customer_ratings inner join titles on customer_ratings.titleid=titles.titleid  where (";
            for (int i = 0; i < recs.size(); i++) {
                finalCommand += ("customer_ratings.titleid=('" + recs.get(i) + "')");
                if(i == recs.size()-1) {
                    break;
                }
                finalCommand += " or ";
            }
            finalCommand += (") and customerid=('" + maxID + "');");
            ResultSet finalResult = stmt.executeQuery(finalCommand);
            int count = 0;
            while(finalResult.next()) {
                System.out.println(finalResult.getString("originaltitle"));
                labels.get(count).setText(finalResult.getString("originaltitle"));
                imgs.get(count).setImage(getImg(finalResult.getString("originaltitle")));
                count ++;
                if (count >= 10) {
                    break;
                }
            }

            System.out.println(max + " " + maxID);
        } catch (Exception e){
            System.out.println("unsuccessful");
        }
    }
    @FXML
    void backToHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("resources/scenes/mainScreen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
}
