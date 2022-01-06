package repository;

import connection.DBConnection;
import model.Course;
import model.Teacher;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RepoTeachers implements ICrudRepository<Teacher> {
    private List<Teacher> teachers= new ArrayList<Teacher>();
    private String normal_txt_file;
    private String serialized_objects;

    public RepoTeachers(){

    }

//    public RepoTeachers(String file_ser, String file_normal) throws IOException, ClassNotFoundException {
//        this.normal_txt_file=file_normal;
//        this.serialized_objects=file_ser;
//
//        File f_ser= new File(file_ser);
//        if(f_ser.length()>1){
//            FileInputStream fis = new FileInputStream(file_ser);
//            ObjectInputStream ois = new ObjectInputStream(fis);
//            this.teachers = (ArrayList) ois.readObject();
//            ois.close();
//            fis.close();
//
//
//        }else{
//
//
//            FileInputStream fis = new FileInputStream(file_normal);
//            Scanner scanner= new Scanner(fis);
//            List<Teacher> teachers1= new ArrayList<Teacher>();
//            while(scanner.hasNextLine()){
//                String line= scanner.nextLine();
//                if(line.length()>0) {
//                    String[] splitted = line.split(",");
//                    Teacher t = new Teacher(Long.valueOf(splitted[0]), splitted[1], splitted[2]);
//                    teachers1.add(t);
//                }else{break;}
//            }
//            fis.close();
//            scanner.close();
//            this.teachers=teachers1;
//        }
//
//    }
//


    @Override
    public Teacher findOne(Long id) throws SQLException {
        Connection con= DBConnection.connect();
        Statement statement= con.createStatement();
        String query= String.format("select * from Teacher where id=%d", id);
        ResultSet res=statement.executeQuery(query);

        Teacher teacher= null;
        while(res.next()){
            teacher =new Teacher(Long.valueOf(res.getInt("id")), res.getString("prenume"), res.getString("name"));
            //return course;
        }
        res.close();
        query= String.format("select * from teachers_courses where teacherID=%d", id);
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
        teacher.setCourses(cursuri);

        return teacher;
    }

    @Override
    public Iterable findAll() throws SQLException {
        ArrayList<Teacher> allTeachers= new ArrayList<>();
        Connection con= DBConnection.connect();
        Statement statement= con.createStatement();
        String query= "select * from Teacher";
        ResultSet res=statement.executeQuery(query);
        while(res.next()){
            Teacher teache=findOne(Long.valueOf(res.getInt("id")));
            allTeachers.add(teache);
        }
        if (!allTeachers.isEmpty()){
            return allTeachers;
        }
        return null;
    }

    @Override
    public Teacher save(Teacher entity) throws SQLException {
        Connection con= DBConnection.connect();
        Statement statement= con.createStatement();
        String query= String.format("select * from Teacher where Teacher.id=%d and Teacher.lastName=%s", Integer.valueOf(Math.toIntExact(entity.getId())), entity.get_lastName());
        ResultSet res=statement.executeQuery(query);
        res.close();
        while(res.next()){
            if (entity.get_lastName().equals(res.getString("lastName")) && Integer.valueOf(Math.toIntExact(entity.getId()))==res.getInt("id")){
                return entity;
            }
        }
        query= String.format("insert into Teacher (lastName, firstName) values ('%s', %d)", entity.get_lastName(), entity.get_firstName());
        statement.execute(query);
        return null;
    }



    @Override
    public Teacher delete(Long id) throws SQLException {
        Connection con= DBConnection.connect();
        Statement statement= con.createStatement();
        String query= String.format("select * from Teacher where Teacher.id=%d", Integer.valueOf(Math.toIntExact(id)));
        ResultSet res= statement.executeQuery(query);
        while(res.next()){
            Teacher teacher= new Teacher(Long.valueOf(res.getInt("id")) , res.getString("firstName"), res.getString("lastName"));
            String query2= String.format("delete from Teacher where Teacher.id=%d", Integer.valueOf(Math.toIntExact(id)));
            statement.execute(query2);
            return teacher;
        }
        return null;

    }

    @Override
    public Teacher update(Teacher entity) throws SQLException {
//        Connection con= DBConnection.connect();
//
//        if (findOne(entity.getId())!=null){
//            Teacher teacher= findOne(entity.getId());
//            ArrayList<Course> cursuri= (ArrayList<Course>) entity.getCourses();
//                for(Course c: entity.getCourses()){
//                    String query= String.format("select * from teachcourses where id_teacher=%d and id_curs=%d",Integer.valueOf(Math.toIntExact(entity.getId())) ,Integer.valueOf(Math.toIntExact(c.getId())) );
//                    Statement statement= con.createStatement();
//                    ResultSet resultSet= statement.executeQuery(query);
//
//                    if(resultSet.next()){
//                        continue;
//                    }else{
//                        Statement statement1= con.createStatement();
//                        query=String.format("insert into teachcourses (id_teacher, id_curs)  values (%d, %d)", Integer.valueOf(Math.toIntExact(entity.getId())) ,Integer.valueOf(Math.toIntExact(c.getId())) );
//                        statement1.execute(query);
//                        statement1.close();
//                        return null;
//                    }
//                }
//            }else{
//                throw new CreditException();
//            }
//
//        }
        return entity;
    }
    public String getSerializedFile(){
        return this.serialized_objects;
    }
    public String getNormalTxtFile(){
        return this.normal_txt_file;
    }

}
