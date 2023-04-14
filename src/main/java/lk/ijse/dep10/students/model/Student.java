package lk.ijse.dep10.students.model;

import javafx.scene.image.Image;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Date;

@AllArgsConstructor
@Data
public class Student implements Serializable {
    private String id;
    private String nameWithInitials;
    private String fullName;
    private Date dob;
    private String grade;
    private String address;
    private String contact;
    private String guardianName;
    private String guardianOccupation;
    private Image profilePicture;
}
