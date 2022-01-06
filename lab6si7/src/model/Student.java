package model;

import java.util.ArrayList;
import java.util.List;

public class Student extends Person{

    public final int credits=30;
    private List<Course> myCourses= new ArrayList<Course>();

    public Student(){
        super();
    }
    public Student(Long id, String f, String l) {
        super(l, f);
        this.id=id;
        this.myCourses= new ArrayList<Course>();

    }

    public Student(Long id, String l, String f, List<Course> cursuri){
        super(l, f);
        this.id=id;
        this.myCourses=cursuri;
    }


    public Student(Student s) {

        this.firstName=s.firstName;
        this.lastName=s.lastName;
        this.id=s.id;
        this.myCourses=s.myCourses;
    }

    @Override
    public int compareTo(Person o) {
        try{
            Student s= (Student) o;
            if (this.lastName==s.lastName && this.firstName==s.firstName && this.id==s.id){return 1;}
        }
        catch(ClassCastException e){
            return 0;
            }
            return 0;
        }

        public int calculate_nr_credits(){
        int total=0;
        for (Course c: myCourses){
            total+=c.getNrCredits();
        }
        return total;
        }

    /**
     *
     * @param s- Student object
     * @return true- if the object was updated with modified attributes, or false otherwise
     */
    public boolean update(Student s) throws CreditException{
            if(s.calculate_nr_credits()>this.credits){
                throw new CreditException();

            }else{
                this.myCourses=s.myCourses;
                this.lastName=s.lastName;
                this.firstName=s.firstName;

            }
            return true;

        }

        public List<Course> getCourses(){
        return this.myCourses;
        }
        public void setCourses(List<Course> lista){
            this.myCourses= lista;
        }
    @Override
    public boolean equals(Object o){
        if(!(o instanceof Student)) {return false;}
        Student s= (Student) o;
        return this.id.equals(s.getId()) && this.firstName.equals(s.firstName) && this.lastName.equals(s.lastName);

    }

    @Override
    public String toString(){
        return String.valueOf(this.id)+" "+ this.firstName+ "  "+ this.lastName;
    }

}
