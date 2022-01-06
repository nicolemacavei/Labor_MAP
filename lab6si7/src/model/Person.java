package model;
import java.io.Serializable;

public abstract class Person implements Comparable<Person>, Serializable {
    protected String lastName;
    protected String firstName;
    protected Long id;


    public Person(String l, String f){

        this.firstName=f;
        this.lastName=l;
    }

    public Person() {

    }

    public String get_lastName(){
        return this.lastName;
    }
    public String get_firstName(){
        return  this.firstName;
    }

    public Long getId(){return  this.id;}



}
