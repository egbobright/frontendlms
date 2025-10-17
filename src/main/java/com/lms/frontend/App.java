package com.lms.frontend;

import com.lms.frontend.views.BookView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Default.fxml"));
        //Scene scene = new Scene(fxmlLoader.load());
        BookView bookView = new BookView();
        Scene scene = new Scene(bookView.getView(), 900, 600);
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
