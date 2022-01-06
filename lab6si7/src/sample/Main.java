package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
  FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
  Parent root= loader.load();
    Pane mypane= (Pane) loader.getNamespace().get("myPane");

        BackgroundImage myBI= new BackgroundImage(new Image("sample/Views/resources/school.jpg",650,650,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        mypane.setBackground(new Background(myBI));
        mypane.setOpacity(1);
//then you set to your node
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 700, 650));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
