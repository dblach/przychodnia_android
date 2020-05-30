package pl.edu.dblach.przychodnia;

import java.util.ArrayList;

public class Doctor{
    private String id;
    private String imie;
    private String nazwisko;
    private String zdjecie;
    private String godziny_przyjec;

    Doctor(String id,String imie,String nazwisko,String zdjecie){
        this.id=id;
        this.imie=imie;
        this.nazwisko=nazwisko;
        this.zdjecie=zdjecie;
        godziny_przyjec="";
    }

    void addAdmission(String dzien_tygodnia,String godzina_rozpoczecia,String godzina_zakonczenia,String pomieszczenie){
        godziny_przyjec+=dzien_tygodnia+": "+godzina_rozpoczecia+"-"+godzina_zakonczenia+" ["+pomieszczenie+"]"+System.getProperty("line.separator");
    }

    public String id(){return this.id;}
    public String imie(){return this.imie;}
    public String nazwisko(){return this.nazwisko;}
    public String zdjecie(){return this.zdjecie;}
    public String godziny_przyjec(){return this.godziny_przyjec;}

}
