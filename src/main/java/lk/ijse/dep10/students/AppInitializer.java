package lk.ijse.dep10.students;

import javafx.application.Application;
import javafx.stage.Stage;
import lk.ijse.dep10.students.db.DBConnection;

import java.sql.Connection;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Connection connection = DBConnection.getInstance().getConnection();

    }
}
