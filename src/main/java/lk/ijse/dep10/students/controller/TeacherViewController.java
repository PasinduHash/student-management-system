package lk.ijse.dep10.students.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import lk.ijse.dep10.students.db.DBConnection;
import lk.ijse.dep10.students.model.Student;

import java.sql.*;

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
    public void initialize(){
        txtClass.setText("10-A");
        tblStudents.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblStudents.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("fullName"));
        tblStudents.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("contact"));
        loadAllStudents();




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
    void btnAddMarksOnAction(ActionEvent event) {

    }

    @FXML
    void btnLibraryRecordOnAction(ActionEvent event) {

    }

    @FXML
    void btnLogOutOnAction(ActionEvent event) {

    }

    @FXML
    void btnMoreInfoOnAction(ActionEvent event) {

    }

}
