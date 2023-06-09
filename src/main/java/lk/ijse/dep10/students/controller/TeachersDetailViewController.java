package lk.ijse.dep10.students.controller;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import lk.ijse.dep10.students.db.DBConnection;
import lk.ijse.dep10.students.model.Student;
import lk.ijse.dep10.students.model.Teacher;
import lk.ijse.dep10.students.util.PasswordEncoder;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.io.ByteArrayOutputStream;
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
        Teacher selectedTeacher = tblTeachersDetails.getSelectionModel().getSelectedItem();
        Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete teacher with teacher id " + selectedTeacher.getId() + " ?", ButtonType.YES, ButtonType.NO).showAndWait();
        if (buttonType.isPresent() && buttonType.get() == ButtonType.YES) {
            Connection connection = DBConnection.getInstance().getConnection();
            try {
                connection.setAutoCommit(false);
                PreparedStatement stmPicture = connection.prepareStatement("DELETE FROM TeacherPicture WHERE teacher_id = ?");
                stmPicture.setString(1, selectedTeacher.getId());
                PreparedStatement stmContact = connection.prepareStatement("DELETE FROM Contacts WHERE teacher_id = ?");
                ArrayList<String> contactList = selectedTeacher.getContactList();
                for (String contact : contactList) {
                    stmContact.setString(1, selectedTeacher.getId());
                    stmContact.executeUpdate();
                }
                PreparedStatement stmSubject = connection.prepareStatement("DELETE FROM Subjects WHERE teacher_id = ?");
                ArrayList<String> subjectList = selectedTeacher.getSubjectList();
                for (String subject : subjectList) {
                    stmSubject.setString(1, selectedTeacher.getId());
                    stmSubject.executeUpdate();
                }
                PreparedStatement stmTeacher = connection.prepareStatement("DELETE FROM Teacher WHERE id = ?");
                stmTeacher.setString(1, selectedTeacher.getId());
                stmPicture.executeUpdate();
                stmTeacher.executeUpdate();
                connection.commit();

                tblTeachersDetails.getItems().remove(selectedTeacher);
                tblTeachersDetails.getSelectionModel().clearSelection();
                if (!tblTeachersDetails.getItems().isEmpty()) btnNewTeacher.fire();
                connection.commit();
            } catch (Throwable e) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to delete the teacher. Try again!").show();
            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
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

        if (!isDataValid()) return;

        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement stmTeacher = connection.prepareStatement("INSERT INTO Teacher (id, name, class, user_name, password) VALUES (?, ?, ?, ?, ?)");
            PreparedStatement stmTeacherSubjects = connection.prepareStatement("INSERT INTO Subjects (teacher_id, subject) VALUES (?, ?)");
            PreparedStatement stmTeacherContacts = connection.prepareStatement("INSERT INTO Contacts (teacher_id, contact) VALUES (?, ?)");
            PreparedStatement stmTeacherPicture = connection.prepareStatement("INSERT INTO TeacherPicture (teacher_id, picture) VALUES (?, ?)");

            connection.setAutoCommit(false);

            stmTeacher.setString(1, txtID.getText());
            stmTeacher.setString(2, txtName.getText());
            stmTeacher.setString(3, txtClass.getText());
            stmTeacher.setString(4, txtUserName.getText());
            stmTeacher.setString(5, PasswordEncoder.encode(pwdPassword.getText()));
            stmTeacher.executeUpdate();

            ObservableList<String> subjectList = lstSubjects.getItems();
            ObservableList<String> contactList = lstContacts.getItems();

            for (String subject : subjectList) {
                stmTeacherSubjects.setString(1, txtID.getText());
                stmTeacherSubjects.setString(2, subject);
                stmTeacherSubjects.executeUpdate();
            }
            for (String contact : contactList) {
                stmTeacherContacts.setString(1, txtID.getText());
                stmTeacherContacts.setString(2, contact);
                stmTeacherSubjects.executeUpdate();
            }

            Teacher newTeacher = new Teacher(txtID.getText(), txtName.getText(), txtClass.getText(), (ArrayList<String>) subjectList, (ArrayList<String>) contactList, null, txtUserName.getText(), pwdPassword.getText());

            Image image = imgPicture.getImage();
            if (image != null) {
                /*nJavaFX Image -> Blob */
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", baos);
                byte[] bytes = baos.toByteArray();
                Blob teacherPicture = new SerialBlob(bytes);

                newTeacher.setPicture(new ImageView(new Image(teacherPicture.getBinaryStream())));
                stmTeacherPicture.setString(1, txtID.getText());
                stmTeacherPicture.setBlob(2, teacherPicture);
                stmTeacherPicture.executeUpdate();
            }
            connection.commit();
            tblTeachersDetails.getItems().add(newTeacher);
            btnNewTeacher.fire();

        } catch (Throwable e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            new Alert(Alert.AlertType.ERROR, "Failed to saved the Teacher. Try again!").show();
            e.printStackTrace();
        }
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

    private boolean isDataValid() {
        return true;
    }
}
