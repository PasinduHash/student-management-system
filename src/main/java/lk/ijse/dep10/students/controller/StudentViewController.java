package lk.ijse.dep10.students.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import lk.ijse.dep10.students.db.DBConnection;
import lk.ijse.dep10.students.model.Student;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class StudentViewController {

    @FXML
    private Button btnBrowse;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;
    @FXML
    private Button btnNewStudent;

    @FXML
    private ImageView imgPicture;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<Student> tblStudents;

    @FXML
    private TextArea txtAddress;

    @FXML
    private TextField txtClass;

    @FXML
    private TextField txtContact;

    @FXML
    private DatePicker txtDateOfBirth;

    @FXML
    private TextArea txtFullName;

    @FXML
    private TextField txtGuardianName;

    @FXML
    private TextField txtGuardianOccupation;

    @FXML
    private TextField txtID;

    @FXML
    private TextField txtNameWithInitials;

    @FXML
    private TextField txtSearch;
    public void initialize(){
        tblStudents.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblStudents.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("grade"));
        tblStudents.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("nameWithInitials"));
        tblStudents.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("contact"));

        loadStudents();
        btnNewStudent.fire();

        tblStudents.getSelectionModel().selectedItemProperty().addListener((ov,prev,current)->{
            if (current != null) {
                btnDelete.setDisable(false);
                txtID.setText(current.getId());
                txtFullName.setText(current.getFullName());
                txtNameWithInitials.setText(current.getNameWithInitials());
                txtClass.setText(current.getGrade());
                txtDateOfBirth.setValue(current.getDob());
                txtGuardianName.setText(current.getGuardianName());
                txtGuardianOccupation.setText(current.getGuardianOccupation());
                txtContact.setText(current.getContact());
                txtAddress.setText(current.getAddress());

                if (current.getProfilePicture() != null) {
                    Image picture = current.getProfilePicture();
                    imgPicture.setImage(picture);
                } else {
                    Image image = new Image("/image/empty-photo.png");
                    imgPicture.setImage(image);
                }
            }

        });
    }

    private void loadStudents() {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement stm1 = connection.createStatement();
            ResultSet rst = stm1.executeQuery("SELECT * FROM Student");
            PreparedStatement stm2 = connection.prepareStatement("SELECT * FROM Picture WHERE student_id = ?");
            while (rst.next()) {
                String id = rst.getString("student_id");
                String nameWithInitials = rst.getString("register_name");
                String fullName = rst.getString("full_name");
                LocalDate dob = rst.getDate("dob").toLocalDate();
                String grade = rst.getString("grade");
                String address = rst.getString("address");
                String contact = rst.getString("contact");
                String guardianName = rst.getString("guardian_name");
                String guardianOccupation = rst.getString("guardian_occupation");
                Image picture = null;
                stm2.setString(1,id);
                ResultSet rstPicture = stm2.executeQuery();
                if (rstPicture.next()) {
                    Blob pictureBlob = rstPicture.getBlob("student_picture");
                    picture = new Image(pictureBlob.getBinaryStream());
                } else {
                    picture = new Image("/image/empty-photo.png");
                }
                Student student = new Student(id, nameWithInitials, fullName, dob, grade, address, contact, guardianName, guardianOccupation, picture);
                tblStudents.getItems().add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnBrowseOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Student Photo");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files","*.png","*.jpg","*.jpeg","*.gif","*.dmp"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Pictures"));
        File file = fileChooser.showOpenDialog(btnBrowse.getScene().getWindow());

        if (file != null) {
            try {
                Image image = new Image(String.valueOf(file.toURI().toURL()));
                imgPicture.setImage(image);
                btnClear.setDisable(false);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        imgPicture.setImage(new Image("/image/empty-photo.png"));
        btnClear.setDisable(true);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        Student selectedStudent = tblStudents.getSelectionModel().getSelectedItem();
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement1 = connection.prepareStatement("DELETE FROM Picture WHERE student_id = ?");
            statement1.setString(1, selectedStudent.getId());
            PreparedStatement statement2 = connection.prepareStatement("DELETE FROM Student WHERE student_id = ?");
            statement2.setString(1, selectedStudent.getId());

            statement1.executeUpdate();
            statement2.executeUpdate();

            tblStudents.getItems().remove(tblStudents.getSelectionModel().getSelectedIndex());
//            btnNewStudent.fire();
            connection.commit();

        } catch (Throwable e) {
            try {
                connection.rollback();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        System.out.println("top at save");
        if (!isDataValid()) return;
        System.out.println("datavalid");
        Student newStudent = new Student(txtID.getText(),txtNameWithInitials.getText(),txtFullName.getText(),txtDateOfBirth.getValue(),txtClass.getText(), txtAddress.getText(), txtContact.getText(), txtGuardianName.getText(), txtGuardianOccupation.getText(),imgPicture.getImage());
        tblStudents.getItems().add(newStudent);
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement
                    ("INSERT INTO Student(student_id, register_name, full_name, dob, grade, address, contact, guardian_name, guardian_occupation) VALUES (?,?,?,?,?,?,?,?,?)");
            PreparedStatement preparedStatementPicture = connection.prepareStatement("INSERT INTO Picture(student_id, student_picture) VALUES (?,?)");

            preparedStatement.setString(1,txtID.getText());
            preparedStatement.setString(2,txtNameWithInitials.getText());
            preparedStatement.setString(3,txtFullName.getText());
            preparedStatement.setString(4, String.valueOf(txtDateOfBirth.getValue()));
            preparedStatement.setString(5,txtClass.getText());
            preparedStatement.setString(6,txtAddress.getText());
            preparedStatement.setString(7,txtContact.getText());
            preparedStatement.setString(8,txtGuardianName.getText());
            preparedStatement.setString(9,txtGuardianOccupation.getText());
            preparedStatement.executeUpdate();

            Image image = imgPicture.getImage();
            BufferedImage bi = SwingFXUtils.fromFXImage(image, null);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bi,"png",bos);
            byte[] bytes = bos.toByteArray();
            Blob pictureBlob = new SerialBlob(bytes);
            preparedStatementPicture.setString(1,txtID.getText());
            preparedStatementPicture.setBlob(2,pictureBlob);
            preparedStatementPicture.executeUpdate();
            btnNewStudent.fire();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                e.printStackTrace();
            }
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isDataValid() {
        System.out.println("top at data validation");
        boolean dataValid = true;

        txtFullName.getStyleClass().remove("invalid");
        txtNameWithInitials.getStyleClass().remove("invalid");
        txtDateOfBirth.getStyleClass().remove("invalid");
        txtClass.getStyleClass().remove("invalid");
        txtContact.getStyleClass().remove("invalid");
        txtAddress.getStyleClass().remove("invalid");
        txtGuardianName.getStyleClass().remove("invalid");
        txtGuardianOccupation.getStyleClass().remove("invalid");
        if(!txtContact.getText().matches("\\d{3}-\\d{7}")){
            dataValid = false;
            txtContact.requestFocus();
            txtContact.getStyleClass().add("invalid");
        }



        System.out.println("before date check");
        if(txtDateOfBirth.getValue()==null){
            dataValid = false;
            txtDateOfBirth.requestFocus();
            txtDateOfBirth.getStyleClass().add("invalid");
        }else {
            System.out.println("inside else");
            try {
                System.out.println("before");
                LocalDate dob = txtDateOfBirth.getValue();
                System.out.println("after");
            } catch (Exception e) {
                dataValid = false;
                System.out.println("in");
                txtDateOfBirth.requestFocus();
                txtDateOfBirth.getStyleClass().add("invalid");
            }
        }

        if(txtAddress.getText().length()<3){
            System.out.println(txtAddress.getText());
            dataValid = false;
            txtAddress.requestFocus();
            txtAddress.getStyleClass().add("invalid");
        }
        if(!txtGuardianName.getText().matches("[A-Z a-z]{3,}")){
            dataValid = false;
            txtGuardianName.requestFocus();
            txtGuardianName.getStyleClass().add("invalid");
        }
        if(!txtGuardianOccupation.getText().matches("[A-Z a-z]{3,}")){
            dataValid = false;
            txtGuardianOccupation.requestFocus();
            txtGuardianOccupation.getStyleClass().add("invalid");
        }
        if(!txtClass.getText().matches("([1-9]|1[0-3])-[A-F]")){
            dataValid = false;
            txtClass.requestFocus();
            txtClass.getStyleClass().add("invalid");
        }
        if(!txtNameWithInitials.getText().matches("[A-Z .a-z]{3,}+")){
            dataValid = false;
            txtNameWithInitials.requestFocus();
            txtNameWithInitials.getStyleClass().add("invalid");
        }
        if(!txtFullName.getText().matches("[A-Z a-z]{3,}")){
            dataValid = false;
            txtFullName.requestFocus();
            txtFullName.getStyleClass().add("invalid");
        }

        return dataValid;
    }

    @FXML
    void txtSearchOnKeyReleased(KeyEvent event) {

    }

    public void btnNewStudentOnAction(ActionEvent actionEvent) {
        btnBrowse.setDisable(false);
        btnSave.setDisable(false);
        txtNameWithInitials.setDisable(false);
        txtFullName.setDisable(false);
        txtDateOfBirth.setDisable(false);
        txtClass.setDisable(false);
        txtAddress.setDisable(false);
        txtGuardianName.setDisable(false);
        txtGuardianOccupation.setDisable(false);
        txtContact.setDisable(false);

        String newStudentId = "S001";
        if (tblStudents.getItems().size()!=0) {
            String lastStudentId = (tblStudents.getItems().get(tblStudents.getItems().size() - 1).getId().substring(1));
            newStudentId = String.format("S%03d", Integer.parseInt(lastStudentId) + 1);
        }
        txtID.setText(newStudentId);
        txtFullName.clear();
        txtNameWithInitials.clear();
        txtAddress.clear();
        txtClass.clear();
        txtContact.clear();
        txtGuardianName.clear();
        txtGuardianOccupation.clear();
        txtDateOfBirth.setValue(null);
        imgPicture.setImage(new Image("/image/empty-photo.png"));
        tblStudents.getSelectionModel().clearSelection();
    }
}
