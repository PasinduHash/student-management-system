package lk.ijse.dep10.students.controller;

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

import java.io.File;
import java.net.MalformedURLException;
import java.sql.*;

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
                Date dob = rst.getDate("dob");
                String grade = rst.getString("grade");
                String address = rst.getString("address");
                String contact = rst.getString("contact");
                String guardianName = rst.getString("guardian_name");
                String guardianOccupation = rst.getString("guardian_occupation");
                System.out.println("midddle");
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

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if(!dataValid()) return;
        System.out.println("correct");




    }

    private boolean dataValid() {
        return true;
    }

    @FXML
    void txtSearchOnKeyReleased(KeyEvent event) {

    }

    public void btnNewStudentOnAction(ActionEvent actionEvent) {
        System.out.println(tblStudents.getItems());
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
