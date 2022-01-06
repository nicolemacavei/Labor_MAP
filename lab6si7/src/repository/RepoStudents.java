package repository;

import connection.DBConnection;
import model.Course;
import model.CreditException;
import model.Student;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RepoStudents implements ICrudRepository<Student> {
    private List<Student> students= new ArrayList<Student>();
    private String normal_txt_file;
    private String serialized_objects;

    public RepoStudents(){

    }

    public RepoStudents(String file_ser, String file_normal) throws IOException, ClassNotFoundException {
        this.normal_txt_file=file_normal;
        this.serialized_objects=file_ser;

        File f_ser= new File(file_ser);
        if(f_ser.length()>1){
            FileInputStream fis = new FileInputStream(file_ser);
            ObjectInputStream ois = new ObjectInputStream(fis);
            this.students = (ArrayList) ois.readObject();
            ois.close();
            fis.close();


        }else{


            FileInputStream fis = new FileInputStream(file_normal);
            Scanner scanner= new Scanner(fis);
            List<Student> studenti= new ArrayList<Student>();
            while(scanner.hasNextLine()){
                String line= scanner.nextLine();
                if(line.length()>0) {
                    String[] splitted = line.split(",");
                    Student s = new Student(Long.valueOf(splitted[0]), splitted[1], splitted[2]);
                    studenti.add(s);
                }else{break;}
            }
            fis.close();
            scanner.close();
            this.students=studenti;
        }

    }

    @Override
    public Student findOne(Long id) throws SQLException {
        Connection con= DBConnection.connect();
        Statement statement= con.createStatement();
        String query= String.format("select * from Student where id=%d", id);
        ResultSet res=statement.executeQuery(query);

        Student student= null;
        while(res.next()){
            student =new Student(Long.valueOf(res.getInt("id")), res.getString("prenume"), res.getString("name"));
            //return course;
        }
        res.close();
        query= String.format("select * from students_courses where id_stud=%d", id);
        Statement statement2= con.createStatement();

        ResultSet res2= statement2.executeQuery(query);
        ArrayList<Course> cursuri= new ArrayList<>();
        while(res2.next()){
            int id_curs=res2.getInt("id_curs");
            String q= String.format("select * from Course where id=%d", id_curs);
            ResultSet curs= statement.executeQuery(q);
            curs.next();
            Course course= new Course(Long.valueOf(curs.getInt("id")), curs.getString("name"), curs.getInt("numberofcredits"));
            cursuri.add(course);
        }
        res2.close();
        statement.close();
        statement2.close();
        con.close();
        student.setCourses(cursuri);

        return student;

    }

    @Override
    public Iterable findAll() throws SQLException {
        ArrayList<Student> allStudents= new ArrayList<>();
        Connection con= DBConnection.connect();
        Statement statement= con.createStatement();
        String query= "select * from Student";
        ResultSet res=statement.executeQuery(query);
        while(res.next()){
            Student student=findOne(Long.valueOf(res.getInt("id")));
            allStudents.add(student);
        }
        if (!allStudents.isEmpty()){
            return allStudents;
        }
        return null;
    }

    @Override
    public Student save(Student entity) throws SQLException {
        Connection con= DBConnection.connect();
        Statement statement= con.createStatement();
        String query= String.format("select * from Student where Student.id=%d and Student.name=%s", Integer.valueOf(Math.toIntExact(entity.getId())), entity.get_lastName());
        ResultSet res=statement.executeQuery(query);
        res.close();
        while(res.next()){
            if (entity.get_lastName().equals(res.getString("lastName")) && Integer.valueOf(Math.toIntExact(entity.getId()))==res.getInt("id")){
                return entity;
            }
        }
        query= String.format("insert into Student (name, numberofCredits) values ('%s', %d)", entity.get_lastName(), 0);
        statement.execute(query);
        return null;
    }


    @Override
    public Student delete(Long id) throws SQLException {
        Connection con= DBConnection.connect();
        Statement statement= con.createStatement();
        String query= String.format("select * from Student where Student.id=%d", Integer.valueOf(Math.toIntExact(id)));
        ResultSet res= statement.executeQuery(query);
        while(res.next()){
            Student student= new Student(Long.valueOf(res.getInt("id")) , res.getString("prenume"), res.getString("name"));
            String query2= String.format("delete from Student where Student.id=%d", Integer.valueOf(Math.toIntExact(id)));
            statement.execute(query2);
            return student;
        }
        return null;
    }

    @Override
    public Student update(Student entity) throws CreditException, SQLException {
        Connection con= DBConnection.connect();

        if (findOne(entity.getId())!=null){
            Student student= findOne(entity.getId());
            ArrayList<Course> cursuri= (ArrayList<Course>) entity.getCourses();
            int credits=0;
            for(Course c: cursuri){
                credits+=c.getNrCredits();
            }
            if (credits<=entity.credits){
               for(Course c: entity.getCourses()){
                   String query= String.format("select * from students_courses where studentID=%d and CourseID=%d",Integer.valueOf(Math.toIntExact(entity.getId())) ,Integer.valueOf(Math.toIntExact(c.getId())) );
                   Statement statement= con.createStatement();
                   ResultSet resultSet= statement.executeQuery(query);

                   if(resultSet.next()){
                       continue;
                   }else{
                       Statement statement1= con.createStatement();
                       query=String.format("insert into students_courses (studentID, courseID)  values (%d, %d)", Integer.valueOf(Math.toIntExact(entity.getId())) ,Integer.valueOf(Math.toIntExact(c.getId())) );
                       statement1.execute(query);
                       statement1.close();
                       return null;
                   }
               }
            }else{
                throw new CreditException();
            }

        }
        return entity;
        }


    public List<Student> inscrisiLaUnSingurCurs() throws SQLException {
        List<Student> studs= new ArrayList<Student>();
        for(Student s: (List<Student>)this.findAll()){
            if(s.getCourses().size()==1){
                studs.add(s);
            }

        }
        return studs;
    }

    public String getSerializedFile(){
        return this.serialized_objects;
    }
    public String getNormalTxtFile(){
        return this.normal_txt_file;
    }
}
