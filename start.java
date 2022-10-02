package group3.javagui;

import java.sql.*;
import javax.swing.JOptionPane;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;

//import java.sql.DriverManager;
/*
CSCE 315
9-25-2019
 */

 //Cyrus left a comment
public class start extends Application {

  // Connection to Database
  public static Connection conn = null;
  public static String currentCustomerID;
  public static String searchResults = "";

  // List of all FXML Files
  public static String mainScreen_FXML = "resources/scenes/mainScreen.fxml";
  public static String analytical_FXML = "resources/scenes/analyticalMain.fxml";

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation( getClass().getResource( analytical_FXML ) );
    Parent root = loader.load();
    
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }

  @Override
    public void stop(){
        System.out.println("Stage is closing");
        // Closing the connection
        // try {
        //     conn.close();
        //     JOptionPane.showMessageDialog(null,"Connection Closed.");
        // } catch(Exception e) {
        //     JOptionPane.showMessageDialog(null,"Connection NOT Closed.");
        // }
    }

  public static void main(String args[]) {
    // Setup Database Connection
    dbSetup my = new dbSetup();
    try {
      Class.forName("org.postgresql.Driver");
      conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315_910_3db",
          my.user, my.pswd);
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      System.exit(0);
    }

    // Display if connection worked
    JOptionPane.showMessageDialog(null,"Opened database successfully");

    launch(args); // Starts up Scene GUI

    // Example on how to get data from database.
    // String titleid = "";

    // try {
      
    //   Statement stmt = conn.createStatement();
      
    //   String sqlStatement = "SELECT titleid FROM crew";
      
    //   ResultSet result = stmt.executeQuery(sqlStatement);

    //   JOptionPane.showMessageDialog(null,"Crew Title ID from the Database.");
    //   //System.out.println("______________________________________");
    //   while (result.next()) {
    //     System.out.println(result.getString("titleid"));
    //     //titleid += result.getString("titleid")+"\n";
    //   }
    // } catch (Exception e){
    //   JOptionPane.showMessageDialog(null,"Error accessing Database.");
    // }

    
  }
}