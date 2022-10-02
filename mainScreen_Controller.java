package group3.javagui;

import java.io.*;

import javafx.application.HostServices;

import java.net.MalformedURLException;
import java.sql.*;

import java.net.URL;

import java.util.List;
import java.util.Objects;
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

public class mainScreen_Controller implements Initializable
{

    private Stage stage;
    private Scene scene;
    private Parent root;

    private String selectedYear;

    // List All GUI Elements Here
    // Format : @FXML private <Element Type> <Element Name>;
    // Set the element names in SceneBuilder under the code section on the right side of the screen
	@FXML private Pane rootPane;
    @FXML private TextField searchBox;
    @FXML private Button btn_account;
    @FXML private Button btn_foryou;
    @FXML private Button btn_categories;
    @FXML private Button btn_history_prev;
    @FXML private Button btn_history_next;

    @FXML private ChoiceBox choiceBox_SelectYear;

    @FXML private Label label_watchHistory_One;
    @FXML private Label label_watchHistory_Two;
    @FXML private Label label_watchHistory_Three;
    @FXML private Label label_watchHistory_Four;
    @FXML private Label label_watchHistory_Five;

    @FXML private ImageView img_watchHistory_One;
    @FXML private ImageView img_watchHistory_Two;
    @FXML private ImageView img_watchHistory_Three;
    @FXML private ImageView img_watchHistory_Four;
    @FXML private ImageView img_watchHistory_Five;
	
    public static ArrayList< Label > labels_watchHistory = new ArrayList< Label >();
    public static ArrayList< ImageView > imgs_watchHistory = new ArrayList< ImageView >();

    public static ArrayList< String > watchHistory = new ArrayList< String >();
    private int watchHistory_shownStartIndex = 0;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
        watchHistory.clear();
        labels_watchHistory.clear();
        imgs_watchHistory.clear();

        labels_watchHistory.add(label_watchHistory_One);
        labels_watchHistory.add(label_watchHistory_Two);
        labels_watchHistory.add(label_watchHistory_Three);
        labels_watchHistory.add(label_watchHistory_Four);
        labels_watchHistory.add(label_watchHistory_Five);

        imgs_watchHistory.add(img_watchHistory_One);
        imgs_watchHistory.add(img_watchHistory_Two);
        imgs_watchHistory.add(img_watchHistory_Three);
        imgs_watchHistory.add(img_watchHistory_Four);
        imgs_watchHistory.add(img_watchHistory_Five);



        try {
            Statement stmt = start.conn.createStatement();
            
            String sqlStatement = "SELECT * FROM titles WHERE titleid IN (SELECT titleid FROM customer_ratings WHERE customerid='"+ start.currentCustomerID +"' ORDER BY date DESC);";
            
            System.out.println( sqlStatement );

            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {
                watchHistory.add( result.getString("originaltitle") );
                //System.out.println( result.getString("originaltitle") );
            }
        } catch (Exception e){
            //JOptionPane.showMessageDialog(null,"Error accessing Database.");
        }

        updateRecentlyWatchedLabels();
        updateRecentlyWatchedImg();

        for ( int i = 1900; i < 2021; i++ ) {
            choiceBox_SelectYear.getItems().add( i );
        }

        choiceBox_SelectYear.setOnAction((event) -> { changeYear(); });

	}

    @FXML
	private void goToAnalyticalView(ActionEvent event) throws IOException
	{
        Parent root = FXMLLoader.load(getClass().getResource("resources/scenes/analyticalMain.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void historyNext(ActionEvent event) throws IOException
    {
        watchHistory_shownStartIndex += 5;
        for ( int i = watchHistory_shownStartIndex; i < watchHistory_shownStartIndex+Math.min(watchHistory.size()-watchHistory_shownStartIndex, 5); i++ ) {
            labels_watchHistory.get(i-watchHistory_shownStartIndex).setText( watchHistory.get(i) );
            imgs_watchHistory.get(i-watchHistory_shownStartIndex).setImage( getImg(watchHistory.get(i)) );
        }  
        if ( watchHistory_shownStartIndex > 0 ) btn_history_prev.setDisable(false);
        if ( watchHistory_shownStartIndex >= watchHistory.size() - 5 ) btn_history_next.setDisable(true);
    }

    @FXML
    private void historyPrevious(ActionEvent event) throws IOException {
        watchHistory_shownStartIndex -= 5;
        for (int i = watchHistory_shownStartIndex; i < watchHistory_shownStartIndex + Math.min(watchHistory.size() - watchHistory_shownStartIndex, 5); i++) {
            labels_watchHistory.get(i - watchHistory_shownStartIndex).setText(watchHistory.get(i));
            imgs_watchHistory.get(i-watchHistory_shownStartIndex).setImage( getImg(watchHistory.get(i)) );
        }
        if (watchHistory_shownStartIndex == 0) btn_history_prev.setDisable(true);
        if (watchHistory_shownStartIndex < watchHistory.size() - 5) btn_history_next.setDisable(false);
    }
	
    private void changeYear() {
        selectedYear = String.valueOf(choiceBox_SelectYear.getValue());
        
        watchHistory.clear();

        try {
            Statement stmt = start.conn.createStatement();
            
            String sqlStatement = "SELECT * FROM titles WHERE titleid IN (SELECT titleid FROM customer_ratings WHERE customerid='"+ start.currentCustomerID +"' AND date LIKE '"+ selectedYear +"%' LIMIT 10);";
            System.out.println( sqlStatement );

            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {
                watchHistory.add( result.getString("originaltitle") );
            }

            updateRecentlyWatchedLabels();
            updateRecentlyWatchedImg();
        } catch (Exception e){
            //JOptionPane.showMessageDialog(null,"Error accessing Database.");
        }

    }

    private void updateRecentlyWatchedLabels() {
        watchHistory_shownStartIndex = 0;

        for ( int i = 0; i < Math.min(watchHistory.size(), 5); i++ ) {
            labels_watchHistory.get(i).setText( watchHistory.get(i) );
        }  

        btn_history_prev.setDisable(true);
    }

    private void updateRecentlyWatchedImg() {
        watchHistory_shownStartIndex = 0;

        for ( int i = 0; i < Math.min(watchHistory.size(), 5); i++ ) {
//            imgs_watchHistory.get(i).setText( watchHistory.get(i) );
            imgs_watchHistory.get(i).setImage(getImg(watchHistory.get(i)));
        }

        btn_history_prev.setDisable(true);
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

    // search for titles
    @FXML
    private void searchTitle(ActionEvent event) throws IOException {
        String searchStr = searchBox.getText();
        try {
            Statement stmt = start.conn.createStatement();
            String sqlStatement = "SELECT originaltitle FROM titles WHERE UPPER(originaltitle) LIKE UPPER('%" + searchStr + "%');";
            ResultSet result = stmt.executeQuery(sqlStatement);
            start.searchResults = "";
            int count = 0;
            while (result.next()) {
                start.searchResults+=result.getString("originaltitle");
                start.searchResults += ",,,";
                count ++;
                if (count == 10) {
                    break;
                }
            }
            Parent root = FXMLLoader.load(getClass().getResource("resources/scenes/searchResults.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            //JOptionPane.showMessageDialog(null,"Error accessing Database.");
        }
    }

    @FXML
    void userBewareTitles(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("resources/scenes/userBeware.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void viewersChoice(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("resources/scenes/viewersChoice.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
