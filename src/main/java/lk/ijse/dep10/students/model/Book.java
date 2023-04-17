package lk.ijse.dep10.students.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@Data

public class Book implements Serializable {
    private String bookId;
    private String bookName;
    private String author;
    private String category;
    private String status;
    private LocalDate dateBorrowed;
    private LocalDate dateDue;
    private int daysOnLoan;
    private int daysOverdue;
    private double lateReturnFine;
    private String borrowedStudentId;

}
