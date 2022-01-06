package sample;


import javafx.scene.input.MouseEvent;
import sample.Views.StudentView;
import sample.Views.TeacherView;

import java.io.IOException;

public class Controller {

    public  void openStudentWindow() throws IOException {
        StudentView s= StudentView.getStudentViewInstance();
        s.showRegisterPage();

    }


    public void openTeacherWindow() throws IOException {
        TeacherView t= TeacherView.getTeacherViewInstance();
        t.showRegisterPage();
    }


}
