package model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Course implements Comparable<Course> , Serializable {

    private Long id;
    private String name;
    private Person teacher;
    public final int maxEnrollment = 3;
    private int credits;
    private List<Student> studentsEnrolled;

    /**
     *
     *constructor valabil doar pentru citire din fisier
     */
    public Course(Long id, Person teacher, int cred, String name) throws IllegalArgumentException {
        if (teacher instanceof Teacher) {
            this.teacher = teacher;
        } else {
            throw new IllegalArgumentException();
        }
        this.credits = cred;
        this.name = name;
        this.id = id;
        this.studentsEnrolled = new ArrayList<Student>();

    }

    public Course(Long id,  String name, int cred){
        this.teacher= null;
        this.id=id;
        this.name=name;
        this.credits = cred;
        this.studentsEnrolled= new ArrayList<Student>();
    }


    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Course))return false;
        Course otherMyClass = (Course) other;
        return this.id.equals(otherMyClass.id) && this.name.equals(otherMyClass.name) && this.credits == otherMyClass.credits;

    }


    public long getId() {
        return this.id;

    }

    public Teacher getTeacher(){
        return (Teacher) this.teacher;
    }

    public void setTeacher(Teacher t){
        this.teacher=t;
    }


    public Course(Course c) {
        this.credits = c.credits;
        this.studentsEnrolled = c.studentsEnrolled;
        this.teacher = c.teacher;
        this.name = c.name;
        this.id = c.id;

    }

    @Override
    public String toString(){
        return String.valueOf(this.id)+ "  "+ this.name+ "  "+ String.valueOf(this.credits)+ String.valueOf(this.getStudents());
    }

    /**
     * This method updates an object with new features coming from a copied and later madified object
     * @param c -- course object
     * @return true- if the object was updated or false oterwise
     *
     */
    public boolean update(Course c) throws PlacesException {
        if (c.studentsEnrolled.size() > c.maxEnrollment || c.credits < 0) {
            throw new PlacesException();
        } else {
            System.out.println(c);
            this.studentsEnrolled = c.studentsEnrolled;
            this.credits = c.credits;
            this.teacher = c.teacher;
            this.id=id;

        }
        return true;
    }

    public int getNrCredits() {
        return this.credits;
    }


    public int get_maxEnrollment(){
        return this.maxEnrollment;
    }

    public int getNrStudents(){
        return this.studentsEnrolled.size();
    }


    public List<Student> getStudents(){
        return this.studentsEnrolled;
    }

    public void setStudents(List<Student> studs){
        this.studentsEnrolled=studs;
    }
    public void setNrCredits(int newNum){
        this.credits= newNum;
    }


    @Override
    public int compareTo(Course o) {
        if(this.id==o.id && this.teacher==o.teacher && this.name==o.name && this.credits==o.credits && this.maxEnrollment==o.maxEnrollment){
            return 1;
        }
        return 0;
    }

    public String getName(){
        return this.name;
    }
}
