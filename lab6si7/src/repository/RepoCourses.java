package repository;

import connection.DBConnection;
import model.Course;
import model.PlacesException;
import model.Student;
import model.Teacher;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RepoCourses  implements ICrudRepository<Course> {
    private List<Course> courses= new ArrayList<Course>();
    private String serialized_objects;
    private String normal_txt_file;

    public RepoCourses(){

    }

    public RepoCourses(String file_ser, String file_normal) throws IOException, ClassNotFoundException , FileNotFoundException {
        this.normal_txt_file=file_normal;
        this.serialized_objects=file_ser;

        File f_ser= new File(file_ser);
        if(f_ser.length()>0){
                FileInputStream fis = new FileInputStream(file_ser);
                ObjectInputStream ois = new ObjectInputStream(fis);
                this.courses = (ArrayList) ois.readObject();
                ois.close();
                fis.close();


        }else{


            FileInputStream fis = new FileInputStream(file_normal);
            Scanner scanner= new Scanner(fis);
            List<Course> cursuri= new ArrayList<Course>();
            while(scanner.hasNextLine()){
                String line= scanner.nextLine();
                String [] splitted= line.split(",");
                Course c= new Course(Long.valueOf(splitted[0]) , splitted[1], Integer.parseInt(splitted[2]));
                cursuri.add(c);
            }
            fis.close();
            scanner.close();
            this.courses=cursuri;
        }


    }




    @Override
    public Course findOne(Long id) throws SQLException {
        Connection con= DBConnection.connect();
        Statement statement= con.createStatement();
        String query= String.format("select * from Course where id=%d", id);
        ResultSet res=statement.executeQuery(query);

        Course course= null;
      while(res.next()){
         course =new Course(Long.valueOf(res.getInt("id")), res.getString("name"), res.getInt("numberofCredits"));
          //return course;

      }

      res.close();
      query=String.format("select * from teachers_courses where courseID=%d", Integer.valueOf(Math.toIntExact(id)));
      Statement statement1= con.createStatement();
      ResultSet resultSet= statement1.executeQuery(query);
      if(resultSet.next()){
          Statement stat1= con.createStatement();
          query=String.format("select * from Teacher where id=%d", resultSet.getInt("teacherID"));
          ResultSet rs= stat1.executeQuery(query);
          rs.next();
          Teacher t= new Teacher(Long.valueOf(rs.getInt("id")), rs.getString("firstName"), rs.getString("lastName"));
          rs.close();
          stat1.close();
          course.setTeacher(t);

      }
      query= String.format("select * from students_courses where courseID=%d", id);
        Statement statement2= con.createStatement();

      ResultSet res2= statement2.executeQuery(query);
        ArrayList<Student> studs= new ArrayList<>();
      while(res2.next()){
          int id_stud=res2.getInt("studentID");
          String q= String.format("select * from Student where id=%d", id_stud);
          ResultSet stud= statement.executeQuery(q);
          stud.next();
          Student s= new Student(Long.valueOf(stud.getInt("id")), stud.getString("firstName"), stud.getString("lastName"));
                studs.add(s);
      }

      res2.close();
      statement.close();
      statement2.close();
      con.close();
        course.setStudents(studs);

      return course;


    }

    @Override
    public Iterable findAll() throws SQLException {
        ArrayList<Course> allCourses= new ArrayList<>();
        Connection con= DBConnection.connect();
        Statement statement= con.createStatement();
        String query= "select * from Course";
        ResultSet res=statement.executeQuery(query);
        while(res.next()){
            Course course=findOne(Long.valueOf(res.getInt("id")));
            allCourses.add(course);
        }
        if (!allCourses.isEmpty()){
            return allCourses;
        }
        return null;
    }

    @Override
    public Course save(Course entity) throws SQLException {
        Connection con= DBConnection.connect();
        Statement statement= con.createStatement();
        String query= String.format("select * from Course where Course.name='%s' and numberofCredits=%d", entity.getName(), entity.getNrCredits());
        ResultSet res=statement.executeQuery(query);
        while(res.next()){
            if (entity.getName().equals(res.getString("name")) && entity.getNrCredits()==res.getInt("numberofCredits")){
                return entity;
            }
        }
        query= String.format("insert into Course (name, numberofCredits) values ('%s', %d)", entity.getName(), entity.getNrCredits());
        statement.execute(query);
        return null;
    }

    @Override
    public Course delete(Long id) throws SQLException {
        Connection con= DBConnection.connect();
        Statement statement= con.createStatement();
        String query= String.format("select * from Course where Course.id=%d", Integer.valueOf(Math.toIntExact(id)));
        ResultSet res= statement.executeQuery(query);
        while(res.next()){
            Course curs= new Course(Long.valueOf(res.getInt("id")) , res.getString("name"), res.getInt("numberofcredits"));
            String query2= String.format("delete from Course where Course.id=%d", Integer.valueOf(Math.toIntExact(id)));
            statement.execute(query2);
            return curs;

        }

        return null;
    }

    @Override
    public Course update(Course entity) throws PlacesException, SQLException {
        Connection con= DBConnection.connect();

        if (findOne(entity.getId())!=null){
            Course c= findOne(entity.getId());
            if (entity.getNrCredits()!=c.getNrCredits()){
                c.setNrCredits(entity.getNrCredits());
                String query= String.format("update Course set numberofCredits=%d where id=%d", entity.getNrCredits(), Integer.valueOf((int) entity.getId()));
                    Statement statement= con.createStatement();
                    statement.execute(query);

            }
            if (entity.getStudents().size()<=entity.get_maxEnrollment()){
                Statement statement= con.createStatement();
                for(Student s: entity.getStudents()){
                    String query=String.format("select * from students_courses where studentID=%d and courseID=%d", Integer.valueOf(Math.toIntExact(s.getId())), Integer.valueOf((int) entity.getId()));
                    ResultSet res= statement.executeQuery(query);
                    if(res.next()){
                        continue;
                    }else{
                        query=String.format("insert into students_courses (studentID, courseID) values (%d, %d)", Integer.valueOf(Math.toIntExact(s.getId())), Integer.valueOf((int) entity.getId())  );
                        statement.execute(query);
                        break;
                    }
                }
            }else{
                throw new PlacesException();
            }
            return entity;


        }
        return entity;
    }

    public String getSerializedFile(){
        return this.serialized_objects;
    }
    public String getNormalTxtFile(){
        return this.normal_txt_file;
    }




}
