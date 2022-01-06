package sample.Views;

import controller.RegistrationSystem;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.scene.control.Button;
import javafx.util.Pair;
import model.Course;
import model.Student;
import model.Teacher;
import repository.RepoCourses;
import repository.RepoStudents;
import repository.RepoTeachers;

public class TeacherView {

    protected static Teacher activeTeacher=null;
    private static TeacherView teacherView;
    //public static List<Teacher> activeTeacher= new ArrayList<>();


    static {
        try {
            teacherView = new TeacherView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Stage primaryStage= new Stage();
   @FXML public static TextField teacherName=null;
   @FXML public static Button logInTeacher=null;
   @FXML public static javafx.scene.control.ScrollPane myCourses=null;
    @FXML public static javafx.scene.control.ScrollPane myStudents=null;

    private RegistrationSystem r=new RegistrationSystem(new RepoCourses(), new RepoStudents(), new RepoTeachers());

    public TeacherView() throws SQLException {
    }


    @FXML
    public String returnTextInput() throws IOException, SQLException {
//       return teacherName.getText();
        System.out.println(teacherName.getText());
        if(r.findTeacher(teacherName.getText())!=null){
            activeTeacher=r.findTeacher(teacherName.getText());


        }
        return teacherName.getText();
    }


    public static TeacherView getTeacherViewInstance(){
        return teacherView;
    }
    public void showRegisterPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TeacherView.fxml"));
        Parent root=loader.load();
        teacherName = (TextField)loader.getNamespace().get("teacherName");
        logInTeacher=(Button) loader.getNamespace().get("logInTeacher");
        AnchorPane mypane=(AnchorPane) loader.getNamespace().get("myPane");
        BackgroundImage myBI= new BackgroundImage(new Image("sample/Views/resources/teacher.jpg",650,650,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        mypane.setBackground(new Background(myBI));
        mypane.setOpacity(1);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 650, 650));
        primaryStage.show();
    }



    public void showTeacherPage() throws IOException, SQLException {
        returnTextInput();
        System.out.println(activeTeacher);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TeacherPage.fxml"));
        Parent root=loader.load();
        myCourses=(javafx.scene.control.ScrollPane) loader.getNamespace().get("myCourses");
        myStudents=(ScrollPane) loader.getNamespace().get("myStudents");
        myCourses.setContent(populateCourses());
        myStudents.setContent(populateStudents());
        primaryStage.setTitle("Hello World");
        primaryStage.getScene().setRoot(root);
        primaryStage.show();
    }

    public ListView populateCourses() throws SQLException {
        ArrayList<Course> cursuri= new ArrayList<>();
        ListView myCoursesList= new ListView();
        if(activeTeacher!=null) {
            System.out.println(activeTeacher == null);
            for (Course c : activeTeacher.getCourses()) {
                myCoursesList.getItems().add(c.getName());
                cursuri.add(c);
            }
        }

        return myCoursesList;
    }

    public ListView populateStudents() throws SQLException {
        ArrayList<String> students_names= new ArrayList<>();
        ListView myStudentsList= new ListView();
        if(activeTeacher!=null) {
            for (Course c : activeTeacher.getCourses()) {
                Course cursComplet = r.getRepoCourses().findOne(c.getId());
                for (Student s : cursComplet.getStudents()) {
                    students_names.add(s.get_lastName());
                }

            }

            Set<String> myStudents = students_names.stream().collect(Collectors.toSet());
            for (String numeDeStudent : myStudents) {
                myStudentsList.getItems().add(numeDeStudent);
            }
        }
            return myStudentsList;
    }
//    public ListView populateStudents() throws SQLException {
//        ListView myStudentsList= new ListView();
//        ArrayList<Pair> myStud= new ArrayList();
//        for(Course c: activeTeacher.getCourses()){
//            c.getStudents().stream().map()
//            myStud.addAll(c.getStudents().forEach(););
//
//        }
//
//
//    }

}
