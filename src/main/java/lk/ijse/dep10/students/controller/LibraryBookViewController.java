package lk.ijse.dep10.students.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.dep10.students.db.DBConnection;
import lk.ijse.dep10.students.model.Book;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LibraryBookViewController {
    public TextField txtBookID;
    public TextField txtBookName;
    public TextField txtBookAuthor;
    public ComboBox cmbBookCategory;
    public ComboBox cmbBookStatus;
    public DatePicker dtpDateBorrowed;
    public DatePicker dtpDateDue;
    public TextField txtDaysOnLoan;
    public TextField txtDaysOverdue;
    public TextField txtLateReturnFine;
    public ComboBox cmbMemberType;
    public TextField txtMemberIndex;
    public TextField txtMemberName;
    public TextField txtMemberContact;
    public Button btnAdd;
    public Button btnSave;
    public Button btnDelete;
    public TableView<Book> tblLibrary;
    public TextField txtSearch;
    public Button btnSearch;


    public void initialize(){
        tblLibrary.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("bookId"));
        tblLibrary.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("bookName"));
        tblLibrary.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("author"));
        tblLibrary.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("status"));
        tblLibrary.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("dateDue"));

        loadAllBooks();

    }

    private void loadAllBooks() {
        try {
            Connection connection= DBConnection.getInstance().getConnection();

            Statement stm= connection.createStatement();
            ResultSet rst=stm.executeQuery("SELECT *FROM Library");
            ObservableList<Book> bookList= tblLibrary.getItems();

            while (rst.next()){

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void btnAddOnAction(ActionEvent actionEvent) {
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
    }
}
