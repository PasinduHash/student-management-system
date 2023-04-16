package lk.ijse.dep10.students.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lk.ijse.dep10.students.db.DBConnection;
import lk.ijse.dep10.students.model.Teacher;

import java.awt.print.Book;
import java.sql.*;
import java.util.ArrayList;

public class TeachersDetailViewController {

    public Button btnBrowseImage;
    public Button btnDeleteImage;
    @FXML
    private Button btnAddContact;

    @FXML
    private Button btnAddSubject;

    @FXML
    private Button btnChange;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnDeleteContact;

    @FXML
    private Button btnDeleteSubject;

    @FXML
    private Button btnNewTeacher;

    @FXML
    private Button btnSave;

    @FXML
    private ImageView imgPicture;

    @FXML
    private ListView<String> lstContacts;

    @FXML
    private ListView<String> lstSubjects;

    @FXML
    private PasswordField pwdPassword;

    @FXML
    private TableView<Book> tblBooks;

    @FXML
    private TableView<Teacher> tblTeachersDetails;

    @FXML
    private TextField txtClass;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtID;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtSubject;

    @FXML
    private TextField txtUserName;

    public void initialize() {

        tblTeachersDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblTeachersDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblTeachersDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("grade"));
        tblTeachersDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("subjects"));
        tblTeachersDetails.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("contacts"));

        loadAllTeachers();
    }

    private void loadAllTeachers() {

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM Teacher");
            PreparedStatement stm2 = connection.prepareStatement("SELECT * FROM Subjects WHERE teacher_id = ?");
            PreparedStatement stm3 = connection.prepareStatement("SELECT * FROM Contacts WHERE teacher_id = ?");

            while (rst.next()) {
                String id = rst.getString("teacher_id");
                String name = rst.getString("teacher_name");
                String grade = rst.getString("class");
                ArrayList<String> contactList = new ArrayList<>();
                ArrayList<String> subjectList = new ArrayList<>();
                String userName = rst.getString("user_name");
                String password = rst.getString("password");
                Image image = rst
                stm2.setString(1, id);
                stm3.setString(1, id);
                ResultSet rstContacts = stm2.executeQuery();
                ResultSet rstSubjects = stm3.executeQuery();
                while (rstContacts.next()){
                    String contact = rstContacts.getString("contact");
                    contactList.add(contact);
                }
                while (rstSubjects.next()) {
                    String subject = rstSubjects.getString("subject");
                    subjectList.add(subject);
                }
                Teacher teacher = new Teacher(id, name, grade, subjectList, contactList, userName, password);
                tblTeachersDetails.getItems().add(teacher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load teachers' details. Try again!", ButtonType.OK).show();
        }
    }

    @FXML
    void btnAddContactOnAction(ActionEvent event) {

    }

    @FXML
    void btnAddSubjectOnAction(ActionEvent event) {

    }

    @FXML
    void btnChangeOnAction(ActionEvent event) {

    }

    @FXML
    void btnDeleteContact(ActionEvent event) {

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {

    }

    @FXML
    void btnDeleteSubjectOnAction(ActionEvent event) {

    }

    @FXML
    void btnNewTeacherOnAction(ActionEvent event) {

        txtID.setText(autoGenerateId());
        txtName.setDisable(false);
        txtClass.setDisable(false);
        txtSubject.setDisable(false);
        txtContact.setDisable(false);
        btnAddContact.setDisable(false);
        btnAddSubject.setDisable(false);
        btnSave.setDisable(false);
        txtName.requestFocus();

        txtName.clear();
        txtClass.clear();
        txtSubject.clear();
        txtContact.clear();
        lstSubjects.getItems().clear();
        lstContacts.getItems().clear();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {

    }

    public void btnBrowseImageOnAction(ActionEvent actionEvent) {
        
    }

    public void btnDeleteImageOnAction(ActionEvent actionEvent) {
        
    }

    private String autoGenerateId() {
        String newTeacherId="T001";
        if (tblTeachersDetails.getItems().size()!=0){
            String lastTeacherId = tblTeachersDetails.getItems().get(tblTeachersDetails.getItems().size() - 1).getId().substring(1);
            newTeacherId = String.format("T%03d",(Integer.parseInt(lastTeacherId))+1);
        }
        return newTeacherId;
    }
}
