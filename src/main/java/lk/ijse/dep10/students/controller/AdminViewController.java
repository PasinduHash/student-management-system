package lk.ijse.dep10.students.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class AdminViewController {

    @FXML
    private Button btnStudents;

    @FXML
    private Button btnTeachers;

    @FXML
    void btnStudentsOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();

        URL fxmlFile = getClass().getResource("/view/StudentView.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlFile);
        AnchorPane root = fxmlLoader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(btnStudents.getScene().getWindow());
        stage.setTitle("Manage Students");
        stage.show();
        stage.centerOnScreen();

    }

    @FXML
    void btnTeachersOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();

        URL fxmlFile = getClass().getResource("/view/TeacherView.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlFile);
        AnchorPane root = fxmlLoader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(btnTeachers.getScene().getWindow());
        stage.setTitle("Manage Teachers");
        stage.show();
        stage.centerOnScreen();
    }

}
