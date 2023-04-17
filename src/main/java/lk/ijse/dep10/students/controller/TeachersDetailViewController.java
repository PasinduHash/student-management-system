package lk.ijse.dep10.students.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import lk.ijse.dep10.students.db.DBConnection;
import lk.ijse.dep10.students.model.Teacher;

import java.awt.print.Book;
import java.io.File;
import java.net.MalformedURLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        btnDelete.setDisable(true);
        tblTeachersDetails.getSelectionModel().selectedItemProperty().addListener((ov, prev, current) -> {
            btnDelete.setDisable(current == null);
            if (current == null) return;

            txtID.setText(current.getId());
            txtName.setText(current.getName());
            txtClass.setText(current.getGrade());
            txtUserName.setText(current.getUserName());
            pwdPassword.setText(current.getPassword());
            lstSubjects.setItems((ObservableList<String>) current.getSubjectList());
            lstContacts.setItems((ObservableList<String>) current.getContactList());

            if (current.getPicture() != null) {
                Image studentImage = current.getPicture().getImage();
                imgPicture.setImage(studentImage);
                btnDeleteImage.setDisable(false);
            } else {
                btnDeleteImage.fire();
            }
        });

        lstContacts.getSelectionModel().selectedItemProperty().addListener((ov, prev, current) -> {
            btnDeleteContact.setDisable(current == null);
        });
        lstSubjects.getSelectionModel().selectedItemProperty().addListener((ov, prev, current) -> {
            btnDeleteSubject.setDisable(current == null);
        });
    }

    private void loadAllTeachers() {

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM Teacher");
            PreparedStatement stm2 = connection.prepareStatement("SELECT * FROM Subjects WHERE teacher_id = ?");
            PreparedStatement stm3 = connection.prepareStatement("SELECT * FROM Contacts WHERE teacher_id = ?");
            PreparedStatement stm4 = connection.prepareStatement("SELECT * FROM TeacherPicture WHERE teacher_id = ?");

            while (rst.next()) {
                String id = rst.getString("teacher_id");
                String name = rst.getString("teacher_name");
                String grade = rst.getString("class");
                ArrayList<String> contactList = new ArrayList<>();
                ArrayList<String> subjectList = new ArrayList<>();
                String userName = rst.getString("user_name");
                String password = rst.getString("password");
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
                ImageView picture = null;
                stm4.setString(1, id);
                ResultSet rstPicture = stm4.executeQuery();
                if (rstPicture.next()) {
                    picture = new ImageView(new Image((rstPicture.getBlob("picture").getBinaryStream())));
                }
                Teacher teacher = new Teacher(id, name, grade, subjectList, contactList, picture, userName, password);
                tblTeachersDetails.getItems().add(teacher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load teachers' details. Try again!", ButtonType.OK).show();
        }
    }

    @FXML
    void btnAddContactOnAction(ActionEvent event) {

        txtContact.getStyleClass().remove("invalid");

        String contact = txtContact.getText().strip();

        Pattern patternContact = Pattern.compile("\\d{3}-\\d{7}$");
        Matcher matcherContact = patternContact.matcher(contact);

        if (!matcherContact.matches() || lstContacts.getItems().contains(contact)) {
            txtContact.getStyleClass().add("invalid");
            txtContact.selectAll();
            txtContact.requestFocus();
        } else {
            txtContact.getStyleClass().remove("invalid");
            ObservableList<String> contacts = lstContacts.getItems();
            contacts.add(contact);
            txtContact.clear();
            txtContact.requestFocus();
        }
    }

    @FXML
    void btnAddSubjectOnAction(ActionEvent event) {

        txtSubject.getStyleClass().remove("invalid");

        String subject = txtSubject.getText().strip();

        Pattern patternSubject = Pattern.compile("\\w{3,}$");
        Matcher matcherSubject = patternSubject.matcher(subject);

        if (!matcherSubject.matches() || lstSubjects.getItems().contains(subject)) {
            txtSubject.getStyleClass().add("invalid");
            txtSubject.selectAll();
            txtSubject.requestFocus();
        } else {
            txtSubject.getStyleClass().remove("invalid");
            ObservableList<String> subjects = lstSubjects.getItems();
            subjects.add(subject);
            txtSubject.clear();
            txtSubject.requestFocus();
        }
    }

    @FXML
    void btnChangeOnAction(ActionEvent event) {
        txtUserName.setDisable(false);
        pwdPassword.setDisable(false);
    }

    @FXML
    void btnDeleteContact(ActionEvent event) {

        String selectedSubject = lstSubjects.getSelectionModel().getSelectedItem();
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, String.format("Are you sure to delete the subject: %s ?", selectedSubject), ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optButton = confirmAlert.showAndWait();

        if (optButton.isEmpty() || optButton.get() == ButtonType.NO) return;

        lstSubjects.getItems().remove(selectedSubject);
        lstSubjects.getSelectionModel().clearSelection();
        txtSubject.requestFocus();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {

    }

    @FXML
    void btnDeleteSubjectOnAction(ActionEvent event) {

        String selectedSubject = lstSubjects.getSelectionModel().getSelectedItem();
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, String.format("Are you sure to delete the subject: %s ?", selectedSubject), ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optButton = confirmAlert.showAndWait();

        if (optButton.isEmpty() || optButton.get() == ButtonType.NO) return;

        lstSubjects.getItems().remove(selectedSubject);
        lstSubjects.getSelectionModel().clearSelection();
        txtSubject.requestFocus();
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
        txtUserName.clear();
        pwdPassword.clear();

        txtName.getStyleClass().remove("invalid");
        txtClass.getStyleClass().remove("invalid");
        txtSubject.getStyleClass().remove("invalid");
        txtContact.getStyleClass().remove("invalid");
        txtUserName.getStyleClass().remove("invalid");
        pwdPassword.getStyleClass().remove("invalid");
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {

    }

    public void btnBrowseImageOnAction(ActionEvent actionEvent) throws MalformedURLException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a Picture for the Teacher");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp"));
        File file = fileChooser.showOpenDialog(btnBrowseImage.getScene().getWindow());
        if (file != null){
            Image image = new Image(file.toURI().toURL().toString(), 240.0, 200.0, true, true);
            imgPicture.setImage(image);
            btnDeleteImage.setDisable(false);
        }
    }

    public void btnDeleteImageOnAction(ActionEvent actionEvent) {
        Image image = new Image("/image/empty-photo.png");
        imgPicture.setImage(image);
        btnDeleteImage.setDisable(true);
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
