package lk.ijse.dep10.students.model;

import javafx.scene.image.ImageView;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@AllArgsConstructor
@Data
public class Teacher implements Serializable {
    private int id;
    private String name;
    private String address;
    private String grade;
    private ArrayList<String> subjectList = new ArrayList<>();
    private ArrayList<String> contactList = new ArrayList<>();
    private ImageView picture;
    private String userName;
    private String password;
}
