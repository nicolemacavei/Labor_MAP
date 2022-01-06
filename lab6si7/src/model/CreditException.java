package model;

public class CreditException extends Throwable {
    public CreditException(){

    }

    public String getMessage(){
        return "The nr of credits for this course is beyond your limit";
    }
}
