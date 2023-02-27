package io.github.haappi.bold_client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    public static void main(String[] args) throws IOException {
        //        Client client = new Client();
        //        client.connect("localhost", 2005);
        //        client.sendObject(new Test());
        //        client.close();
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader =
                new FXMLLoader(HelloApplication.class.getResource("connect-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1600, 900);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
