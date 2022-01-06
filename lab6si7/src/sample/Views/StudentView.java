package sample.Views;

import controller.RegistrationSystem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.*;
import repository.RepoCourses;
import repository.RepoStudents;
import repository.RepoTeachers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentView {

    private static StudentView studentView;

    static {
        try {
            studentView = new StudentView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static Stage primaryStage=new Stage();
    protected static Student activeStudent=null;
    @FXML public static TextField studentNameTextField=null;
    @FXML public static Button logInStudent=null;
    @FXML public static javafx.scene.control.ScrollPane myCourses=null;
    @FXML public static javafx.scene.control.ScrollPane coursesWithPlaces= null;
    @FXML public static TextField courseID=null;
    @FXML public static Button registerButton=null;
    private RegistrationSystem r= new RegistrationSystem(new RepoCourses(), new RepoStudents(),  new RepoTeachers());

    public StudentView() throws SQLException {
    }

    public static StudentView getStudentViewInstance(){
        return studentView;
    }

    public Student getActiveStudent(){
        return activeStudent;
    }

    public String returnTextInput() throws IOException, SQLException {
//       return teacherName.getText();
        System.out.println(studentNameTextField.getText());
        if(r.findStudent(studentNameTextField.getText())!=null){
            activeStudent=r.findStudent(studentNameTextField.getText());
        }
        return studentNameTextField.getText();
    }

    public void showRegisterPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentRegister.fxml"));
        Parent root=loader.load();
        studentNameTextField = (TextField) loader.getNamespace().get("studentNameTextField");
        logInStudent=(Button) loader.getNamespace().get("logInStudent");

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 700, 700));
        primaryStage.show();
    }



    public void showStudentPage() throws IOException, SQLException {
        System.out.println("a functionat butonul");
        returnTextInput();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentPage.fxml"));
        Parent root=loader.load();
        myCourses=(javafx.scene.control.ScrollPane) loader.getNamespace().get("myCourses");
        myCourses.setContent(populateCourses());
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 650, 650));
        primaryStage.getScene().setRoot(root);
        primaryStage.show();

    }


    public ListView populateCourses() throws SQLException {
        ArrayList<Course> cursuri= new ArrayList<>();
        ListView myCoursesList= new ListView();

        System.out.println(activeStudent==null);
        if(activeStudent!=null) {
            for (Course c : activeStudent.getCourses()) {
                myCoursesList.getItems().add(c.getName());
                cursuri.add(c);
            }
        }

        return myCoursesList;
    }

    public void showRegisterToCoursePage() throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterToACourse.fxml"));
        Parent root=loader.load();
        coursesWithPlaces=(javafx.scene.control.ScrollPane) loader.getNamespace().get("coursesWithPlaces");
        courseID=(TextField) loader.getNamespace().get("courseID");
        registerButton= (Button) loader.getNamespace().get("registerButton");
        coursesWithPlaces.setContent(populateCoursesWithPlaces());
        primaryStage.getScene().setRoot(root);
        primaryStage.show();
    }

    public ListView populateCoursesWithPlaces() throws SQLException {
        ArrayList<Course> cursuri= (ArrayList<Course>) r.getRepoCourses().findAll();
        ListView myCoursesList= new ListView();
        Label titlu1= new Label();
        Label titlu2= new Label();
        Label label= new Label();
        label.setText("     ");
        titlu1.setText("Course Title");
        titlu2.setText("Numar de locuri disponibile");
        HBox row= new HBox();
        row.getChildren().addAll(titlu1, label,titlu2);
        myCoursesList.getItems().add(row);

        for(Course c: cursuri){
            int numarLocuriDisponibile=c.maxEnrollment-c.getStudents().size();
            String infoCurs= String.valueOf(c.getId())+"  "+c.getName();
            Label info= new Label();
            Label numarLocuri= new Label();
            Label label2= new Label();
            label2.setText("                  ");
            info.setText(infoCurs);
            numarLocuri.setText(String.valueOf(numarLocuriDisponibile));
            HBox hBox= new HBox();
            hBox.getChildren().addAll(info, label2,numarLocuri);
            myCoursesList.getItems().add(hBox);

        }


        return myCoursesList;
    }

    public void registerToACourse() throws SQLException {
        int userInput= Integer.valueOf(courseID.getText());
        Course curs= r.getRepoCourses().findOne(Long.valueOf(userInput));
        try{
            r.register(curs, activeStudent);
            TeacherView.getTeacherViewInstance().showTeacherPage();

        }
        catch (PlacesException e){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("There are zero available places for this course. Sorry, choose another one! :)");
            a.show();
        }
        catch (CreditException e){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("This course exceeds your allowed number of credits! Please choose another one with fewer credits.");
            a.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
