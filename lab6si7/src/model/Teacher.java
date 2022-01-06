package model;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends Person {
    private List<Course> myCourses;

    public Teacher(){
        super();
    }
    public Teacher( Long id , String f,String l) {
        super(l, f);
        this.id=id;
        this.myCourses=new ArrayList<Course>();
    }
    public Teacher(String l, String f, Long id, List<Course> cursuri) {
        super(l, f);
        this.id=id;
        this.myCourses=cursuri;
    }

    @Override
    public int compareTo(Person o) {
        if(this.firstName==o.firstName && this.lastName==o.lastName){
            return 1;
        }
        return 0;
    }


    public void update(Teacher t){
        this.id=id;
        this.myCourses=t.myCourses;
        this.lastName=t.lastName;
        this.firstName=t.firstName;
    }
    public void setCourses(List<Course>cursuri){
        this.myCourses=cursuri;
    }
    public List<Course> getCourses(){
       return this.myCourses;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Teacher)) {return false;}
        Teacher s= (Teacher) o;
        return this.id.equals(s.getId()) && this.firstName.equals(s.firstName) && this.lastName.equals(s.lastName);
    }

    @Override
    public String toString(){
        return String.valueOf(this.id)+" "+ this.firstName+ "  "+ this.lastName;
    }
}
