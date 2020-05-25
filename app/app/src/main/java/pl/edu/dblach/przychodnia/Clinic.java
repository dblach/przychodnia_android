package pl.edu.dblach.przychodnia;

public class Clinic{
    private String id;
    private String nazwa;
    private String ikona;

    Clinic(String id,String nazwa,String ikona){
        this.id=id;
        this.nazwa=nazwa;
        this.ikona=ikona;
    }

    public String id(){return this.id;}
    public String nazwa(){return this.nazwa;}
    public String ikona(){return this.ikona;}
}
