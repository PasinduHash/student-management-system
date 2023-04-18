package lk.ijse.dep10.students.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.dep10.students.db.DBConnection;
import lk.ijse.dep10.students.model.Student;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;

public class TeacherViewController {

    @FXML
    private Button btnAddMarks;

    @FXML
    private Button btnLibraryRecord;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnMoreInfo;

    @FXML
    private ImageView imgStudentVIew;

    @FXML
    private TableView<Student> tblStudents;

    @FXML
    private TextField txtClass;

    @FXML
    private TextField txtIndexNo;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtStudentContactNo;

    @FXML
    private TextField txtStudentName;

    @FXML
    private TextField txtTeacherId;

    @FXML
    private TextField txtTeacherName;
    private String studentId;

    public void initialize(){
        txtClass.setText("10-A");
        tblStudents.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblStudents.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("fullName"));
        tblStudents.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("contact"));
        loadAllStudents();

        txtSearch.textProperty().addListener((ov, previous, current) -> {
            Connection connection = DBConnection.getInstance().getConnection();
            try {
                Statement stm = connection.createStatement();
                String sql = "SELECT * FROM Student " +
                        "WHERE Student.id LIKE  '%1$s'";
                // sql = "SELECT * FROM Student WHERE first_name LIKE '%a%'";
                sql = String.format(sql, "%" + current + "%");
                ResultSet rst = stm.executeQuery(sql);

                ObservableList<Student> studentList = tblStudents.getItems();
                studentList.clear();

                while (rst.next()){
                    String id = rst.getString("id");
                    String fullName = rst.getString("full_name");
                    String contact = rst.getString("contact");

                    studentList.add(new Student(id,fullName,contact));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        tblStudents.getSelectionModel().selectedItemProperty().addListener(((observable, previous, current) -> {
            if (current==null) return;
            studentId=current.getId();
                System.out.println(studentId);
                txtIndexNo.setText(current.getId());
                txtStudentName.setText(current.getFullName());
                txtStudentContactNo.setText(current.getContact());
        }));




    }

    private void loadAllStudents() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();

            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM Student");
            ObservableList<Student> studentList = tblStudents.getItems();
            String teacherClass = txtClass.getText();


            while (rst.next()) {
                String grade=rst.getString("grade");
                if (!(teacherClass.equals(grade))) continue;
                String id = rst.getString("id");
                String fullName = rst.getString("full_name");
                String contact = rst.getString("contact");

                studentList.add(new Student(id,fullName,contact));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed load student details, try again").showAndWait();
            Platform.exit();
        }
    }

    @FXML
    void btnAddMarksOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();

        URL fxmlFile = getClass().getResource("/view/AddMarks.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlFile);

        AnchorPane root = fxmlLoader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(btnAddMarks.getScene().getWindow());
        stage.setTitle("Mark Sheet");
        stage.show();
        stage.centerOnScreen();
    }

    @FXML
    void btnLibraryRecordOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();

        URL fxmlFile = getClass().getResource("/view/LibraryRecordView.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlFile);


        studentId=txtIndexNo.getText();
        AnchorPane root = fxmlLoader.load();
        LibraryRecordViewController controller=fxmlLoader.getController();
        System.out.println(controller);
        controller.initData(studentId);
        System.out.println(controller);




        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(btnAddMarks.getScene().getWindow());
        stage.setTitle("Library Record");
        stage.show();
        stage.centerOnScreen();

    }

    @FXML
    void btnLogOutOnAction(ActionEvent event) {

    }

    @FXML
    void btnMoreInfoOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();

        URL fxmlFile = getClass().getResource("/view/StudentsInfoView.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlFile);
        AnchorPane root = fxmlLoader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(btnAddMarks.getScene().getWindow());
        stage.setTitle("Student Information");
        stage.show();
        stage.centerOnScreen();

    }

}
