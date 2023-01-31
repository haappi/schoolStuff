package io.github.haappi.restaurant_game;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import org.bson.Document;

import java.io.IOException;

public class HelloController {
    public TextField gameCodeInput;
    @FXML private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() throws IOException {
        Document game = DBHandler.getInstance().findDocument(gameCodeInput.getText());
        Game gameInstance;
        if (game == null) {
            gameInstance = new Game(gameCodeInput.getText());
            gameInstance.setTest(5);
        } else {
            gameInstance = HelloApplication.gson.fromJson(game.toJson(), Game.class);
            gameInstance.setTest(120);
        }
        System.out.println(HelloApplication.gson.toJson(gameInstance));
        DBHandler.getInstance()
                .insert(
                        game,
                        DBHandler.getInstance()
                                .getCollection(DBHandler.dbName, DBHandler.collectionName));
        System.out.println(gameInstance.getTest());
        welcomeText.setText("Welcome to JavaFX Application!");
        //        HelloApplication.getInstance().setStageScene("main-menu");
    }
}
