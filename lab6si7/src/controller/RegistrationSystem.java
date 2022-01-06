package controller;

import model.*;
import repository.RepoCourses;
import repository.RepoStudents;
import repository.RepoTeachers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RegistrationSystem  {
    private RepoStudents repoStudents;
    private RepoCourses repoCourses;
    private RepoTeachers repoTeachers;

    public RegistrationSystem(RepoCourses c, RepoStudents s, RepoTeachers t) throws SQLException {
        this.repoCourses=c;
        this.repoTeachers=t;
        this.repoStudents=s;
//        this.mixCoursesTeachers();

    }

    public  Teacher findTeacher(String name) throws SQLException {
        ArrayList<Teacher> myTeachers=(ArrayList) repoTeachers.findAll();
        System.out.println(myTeachers);
        for (Teacher t: myTeachers){
            if (t.get_lastName().equals(name)){
                return t;
            }
        }
        return null;
    }

    public Student findStudent(String name) throws SQLException {
        ArrayList<Student> myStudents=(ArrayList) repoStudents.findAll();
        System.out.println(myStudents);
        for (Student s: myStudents){
            if (s.get_lastName().equals(name)){
                return s;
            }
        }
        return null;
    }

    public List<Course> getAllCourses() throws SQLException {
        return (List<Course>) this.repoCourses.findAll();
    }
    public List<Teacher> getAllTeachers() throws SQLException {
        return (List<Teacher>) this.repoTeachers.findAll();
    }
    public List<Student> getAllStudents() throws SQLException {
        return (List<Student>) this.repoStudents.findAll();
    }

    public List<Student> studentsEnrolledAtaParticularCourse(Course c) throws SQLException {
        List<Student> studenti= (List<Student>) this.repoStudents.findAll();
        List<Student> enrolled= new ArrayList<Student>();
        for(Student s: studenti){
            for(Course curs: s.getCourses()){
                if(curs==c){
                    enrolled.add(s);
                    break;
                }
            }

        }
        return enrolled;
    }

    public boolean register(Course c, Student s) throws CreditException, PlacesException, SQLException {
        Student stud= new Student(s);
        List<Course> cursuri= new ArrayList<Course>(s.getCourses());

        List curs_lista= cursuri.stream().filter(cur->cur.getId()==(c.getId())).collect(Collectors.toList());
        if(!curs_lista.isEmpty()){
            System.out.println(curs_lista.get(0));
            throw new CreditException();
        }
        cursuri.add(c);
        stud.setCourses(cursuri);
        Course curs= new Course(c);
        List<Student> studenti= new ArrayList<Student>(c.getStudents());
        studenti.add(s);
        curs.setStudents(studenti);
        try{
           this.repoStudents.update(stud);
           this.repoCourses.update(curs);

        } catch (CreditException e) {
//            studenti.remove(s);
//            cursuri.remove(c);
//            stud.setCourses(cursuri);
//            curs.setStudents(studenti);
//            this.repoStudents.update(stud);
//            this.repoCourses.update(curs);
            throw e;
        } catch (PlacesException e) {
//            studenti.remove(s);
//            cursuri.remove(c);
//            stud.setCourses(cursuri);
//            curs.setStudents(studenti);
//            this.repoStudents.update(stud);
//            this.repoCourses.update(curs);
//            throw e;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }


    public void modifyNrCredits(Course c, int newCredits) throws PlacesException, SQLException {

                c.setNrCredits(newCredits);
                this.repoCourses.update(c);


    }

    public void deleteCourse(Course c) throws SQLException {
        this.repoCourses.delete(c.getId());

    }

//    private void eraseCourseFromStudents(Course c) throws SQLException {
//        for(Student s: (List<Student>)this.repoStudents.findAll()){
//            for (Course curs: s.getCourses()){
//                if(curs==c){
//                    s.getCourses().remove(curs);
//                    break;
//                }
//            }
//        }
//    }



    private boolean checkIfCoursesNullTeacher() throws SQLException {
        int notNull=0;
        for(Course c:(List<Course>)this.repoCourses.findAll()){
            if(c.getTeacher()!=null){
                notNull+=1;
            }
        }
        return notNull==0;
    }


//    public void writeToFiles(){
//        try
//        {
//            String coursesFile=this.repoCourses.getSerializedFile();
//            FileOutputStream fos1 = new FileOutputStream(coursesFile);
//            ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
//            oos1.writeObject(this.repoCourses.findAll());
//            oos1.close();
//            fos1.close();
//            String studentsFile= this.repoStudents.getSerializedFile();
//            FileOutputStream fos2 = new FileOutputStream(studentsFile);
//            ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
//            oos2.writeObject(this.repoStudents.findAll());
//            oos2.close();
//            fos2.close();
//            String teachersFile= this.repoTeachers.getSerializedFile();
//            FileOutputStream fos3 = new FileOutputStream(teachersFile);
//            ObjectOutputStream oos3 = new ObjectOutputStream(fos3);
//            oos3.writeObject(this.repoTeachers.findAll());
//            oos3.close();
//            fos3.close();
//
//
//        }
//        catch (IOException | SQLException ioe)
//        {
//            ioe.printStackTrace();
//        }
//    }

    public RepoTeachers getRepoTeachers(){
        return this.repoTeachers;
    }
    public RepoCourses getRepoCourses(){
        return this.repoCourses;
    }
    public RepoStudents getRepoStudents(){
        return this.repoStudents;
    }

    public List<Course> getAvailableCourses() throws SQLException {

        ArrayList<Course> cursuri= (ArrayList<Course>) this.repoCourses.findAll();

        List available=cursuri.stream().filter(curs->curs.get_maxEnrollment()>curs.getStudents().size() && curs.getTeacher()!=null).collect(Collectors.toList());
        return available;
    }

}
