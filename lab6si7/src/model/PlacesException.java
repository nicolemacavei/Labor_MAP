package model;

public class PlacesException  extends Throwable{
    public PlacesException(){

    }

    public String getMessage(){
        return "There are not enough places for this course. Please choose another one :)";
    }
}
