package com.algovisualizer.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main application class for AlgoVisualizer.
 */
public class MainApp extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/MainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        
        primaryStage.setTitle("AlgoVisualizer - Graph Algorithm Visualization Tool");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}