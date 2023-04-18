package lk.ijse.dep10.students.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import lk.ijse.dep10.students.db.DBConnection;
import lk.ijse.dep10.students.model.Book;
import lk.ijse.dep10.students.model.LibraryRecord;
import lk.ijse.dep10.students.model.Student;

import java.sql.*;
import java.time.LocalDate;

public class LibraryRecordViewController {

    @FXML
    private Label lblIndexNo;

    @FXML
    private Label lblName;
    private String id;

    @FXML
    private TableView<LibraryRecord> tblBookList;
    public void initialize(){
        loadAllBooks();
    }

    private void loadAllBooks() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();

            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM Library");
            ObservableList<LibraryRecord> bookList = tblBookList.getItems();

            while (rst.next()) {
                String studentId = rst.getString("student_id");
                if (!(studentId.equals(id))) continue;
                String bookId = rst.getString("book_id");
                LocalDate dateBorrowed = rst.getDate("date_borrowed").toLocalDate();
                LocalDate dateDue = rst.getDate("date_due").toLocalDate();
                LibraryRecord.LibraryStatus  status= LibraryRecord.LibraryStatus.valueOf(rst.getString("status"));
                int daysOnLoan=rst.getInt("days_on_loan");
                int daysOverdue =rst.getInt("days_overdue");
                bookList.add(new LibraryRecord(bookId,studentId,dateBorrowed,dateDue,status,daysOnLoan,daysOverdue));


            }

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed load student details, try again").showAndWait();
            Platform.exit();
        }
    }


    public void initData(String studentId){
        id=studentId;

    }

}
