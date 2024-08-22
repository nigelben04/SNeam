package main.com;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.com.view.LoginPage;

public class Main extends Application {

    public static void main(String[] args) { 
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        LoginPage loginPage = new LoginPage();
        Scene scene = new Scene(loginPage, 700, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("SNeam");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
}
