package group3.javagui;

import java.io.*;

import javafx.application.HostServices;

import java.net.MalformedURLException;
import java.sql.*;

import java.net.URL;

import java.util.ResourceBundle;
import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

public class userSearch_Controller implements Initializable
{
    @FXML private ImageView img1;
    @FXML private ImageView img10;
    @FXML private ImageView img2;
    @FXML private ImageView img3;
    @FXML private ImageView img4;
    @FXML private ImageView img5;
    @FXML private ImageView img6;
    @FXML private ImageView img7;
    @FXML private ImageView img8;
    @FXML private ImageView img9;

    @FXML private Label lbl1;
    @FXML private Label lbl2;
    @FXML private Label lbl3;
    @FXML private Label lbl4;
    @FXML private Label lbl5;
    @FXML private Label lbl6;
    @FXML private Label lbl7;
    @FXML private Label lbl8;
    @FXML private Label lbl9;
    @FXML private Label lbl10;

    private Stage stage;
    private Scene scene;

    String[] splitResults = start.searchResults.split(",,,");
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList< ImageView > imgs_Search = new ArrayList< ImageView >();
        ArrayList< Label > lbls_Search = new ArrayList< Label >();
        imgs_Search.add(img1);
        imgs_Search.add(img2);
        imgs_Search.add(img3);
        imgs_Search.add(img4);
        imgs_Search.add(img5);
        imgs_Search.add(img6);
        imgs_Search.add(img7);
        imgs_Search.add(img8);
        imgs_Search.add(img9);
        imgs_Search.add(img10);
        lbls_Search.add(lbl1);
        lbls_Search.add(lbl2);
        lbls_Search.add(lbl3);
        lbls_Search.add(lbl4);
        lbls_Search.add(lbl5);
        lbls_Search.add(lbl6);
        lbls_Search.add(lbl7);
        lbls_Search.add(lbl8);
        lbls_Search.add(lbl9);
        lbls_Search.add(lbl10);
        for (int i = 0; i < splitResults.length; i++) {
            System.out.print("Result: ");
            System.out.println(splitResults[i]);
            imgs_Search.get(i).setImage(getImg(splitResults[i]));
            lbls_Search.get(i).setText(splitResults[i]);
        }
    }

    // search for titles
    @FXML
    private void backToHome(ActionEvent event) throws IOException {
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
