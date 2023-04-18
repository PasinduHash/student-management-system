package lk.ijse.dep10.students.model;

import javafx.print.Printer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
@AllArgsConstructor@Data
public class LibraryRecord {
    private String bookId;
    private String StudentId;
    private LocalDate dateBorrowed;
    private  LocalDate dateDue;
    private LibraryStatus status;
    private int daysOnLoan;
    private int daysOverdue;



    public enum LibraryStatus {
        DUE,HANDED_OVER
    }
}
