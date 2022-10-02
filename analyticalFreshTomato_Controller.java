package group3.javagui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class analyticalFreshTomato_Controller implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;


    /*
        FOR TESTING START
     */
    String titleidStart = "tt0025929";
    String titleidEnd = "tt0304678";

    /*
        FOR TESTING END
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            System.out.println(Runtime.getRuntime().maxMemory());
            System.out.println(Runtime.getRuntime().totalMemory());
            int numMovies = 0;
            // HASH TABLE IN FORM OF MOVIES->USERS
            Statement stmt = start.conn.createStatement();
            String sqlStatement = "select titleid,string_agg(customerid, ',') as userids from customer_ratings group by titleid;"; // gets every movie with their users that have viewed it
            ResultSet result = stmt.executeQuery(sqlStatement);
            HashMap<String, Set<String>> movieViewHash = new HashMap<String, Set<String>>();
            HashMap<String, Integer> movieIndex = new HashMap<String, Integer>();
            while (result.next()) {
                String currMovieID = result.getString("titleid"); // take off tt for bfs edge
                Set<String> currMovieUsers = new HashSet<>(Arrays.asList(result.getString("userids").split(",")));
                movieViewHash.put(currMovieID, currMovieUsers);
                movieIndex.put(currMovieID, numMovies);
                numMovies++;
            }
            System.out.println(numMovies);

            // HASH TABLE IN FORM OF USER->MOVIES
            Statement stmtUserHash = start.conn.createStatement();
            String sqlStatementUserHash = "select customerid,string_agg(titleid, ',') as titleids from customer_ratings group by customerid;"; // gets every user with their watched titleids
            ResultSet resultUserHash = stmtUserHash.executeQuery(sqlStatementUserHash);
            HashMap<String, Set<String>> userViewHash = new HashMap<String, Set<String>>();
            while(resultUserHash.next()) {
                String currUserID = resultUserHash.getString("customerid");
                Set<String> currUserMovies = new HashSet<>(Arrays.asList(resultUserHash.getString("titleids").split(",")));

                Iterator<String> it = currUserMovies.iterator();
                while(it.hasNext()){
                    currUserMovies.add(it.next());
                }
                userViewHash.put(currUserID, currUserMovies);
            }

            // PRINT THE HASH TABLE
            //movieViewHash.forEach((key, value) -> System.out.println(key + " " + value));
            //userViewHash.forEach((key, value) -> System.out.println(key + " " + value));

            ArrayList<ArrayList<Integer>> movieMap = new ArrayList<ArrayList<Integer>>(numMovies);
            for (Map.Entry<String, Set<String>> entry : movieViewHash.entrySet()) {
                movieMap.add(new ArrayList<Integer>());
            }
            System.out.println(movieIndex.get(titleidStart));
            System.out.println(movieIndex.get(titleidEnd));

            // add all edges    movie -> movie
            int count1 = 0;
            for (Map.Entry<String, Set<String>> entry : movieViewHash.entrySet()) {
                System.out.println(count1 + "/" + "2943");
                String key = entry.getKey();
                Set<String> value = entry.getValue();
                if (Objects.equals(key, titleidStart)) {
                    System.out.println("COUNT: " + count1);
                    System.out.println("key inside: " + key);
                    System.out.println("key index: " + movieIndex.get(key));
                }

                Iterator<String> it = value.iterator();
                if (Objects.equals(key, titleidStart)) {
                    System.out.print("START ->");
                }
                while(it.hasNext()) {
                    Set<String> moviesInSpecificUser = userViewHash.get(it.next());
                    Iterator<String> movieID = moviesInSpecificUser.iterator();
                    while(movieID.hasNext()) {
                        try {
                            if (Objects.equals(key, titleidStart)) {
                                if(Objects.equals(movieID.next(), titleidEnd)) {
                                    System.out.print(" FOUND: " + movieID.next() + " IN USER:");
                                    System.out.println("USER: " + it.next());
                                }
                            }
                            addEdge(movieMap, movieIndex.get(key), movieIndex.get(movieID.next()));
                        } catch(Exception k) {
                            continue;
                        }
                    }
                }
                count1++;
            }
            int src = movieIndex.get(titleidStart), end = movieIndex.get(titleidEnd);
            printShortestDistance(movieMap, src, end, numMovies);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addEdge(ArrayList<ArrayList<Integer>> adj, int i, int j) {
        adj.get(i).add(j);
        adj.get(j).add(i);
    }
    private static void printShortestDistance(ArrayList<ArrayList<Integer>> adj, int s, int dest, int v) {
        int pred[] = new int[v];
        int dist[] = new int[v];

        if (!BFS(adj, s, dest, v, pred, dist)) {
            System.out.println("Given source and destination" +
                    "are not connected");
            return;
        }
        LinkedList<Integer> path = new LinkedList<Integer>();
        int crawl = dest;
        path.add(crawl);
        while (pred[crawl] != -1) {
            path.add(pred[crawl]);
            crawl = pred[crawl];
        }

        // Print distance
        System.out.println("Shortest path length is: " + dist[dest]);
        System.out.println("Path is :");
        for (int i = path.size() - 1; i >= 0; i--) {
            System.out.print(path.get(i) + " ");
        }
    }

    private static boolean BFS(ArrayList<ArrayList<Integer>> adj, int src, int dest, int v, int pred[], int dist[]) {
        LinkedList<Integer> queue = new LinkedList<Integer>();
        boolean visited[] = new boolean[v];
        for (int i = 0; i < v; i++) {
            visited[i] = false;
            dist[i] = Integer.MAX_VALUE;
            pred[i] = -1; //38654742
        }
        visited[src] = true;
        dist[src] = 0;
        queue.add(src);

        while (!queue.isEmpty()) {
            int u = queue.remove();
            for (int i = 0; i < adj.get(u).size(); i++) {
                if (!visited[adj.get(u).get(i)]) {
                    visited[adj.get(u).get(i)] = true;
                    dist[adj.get(u).get(i)] = dist[u] + 1;
                    pred[adj.get(u).get(i)] = u;
                    queue.add(adj.get(u).get(i));

                    if (adj.get(u).get(i) == dest)
                        return true;
                }
            }
        }
        return false;
    }

    public void switchAnalyticalUsers(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("resources/scenes/analyticalUsers.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}