package pl.edu.dblach.przychodnia;

public class Appointment{
    private String godzina_rozpoczecia;
    private String godzina_zakonczenia;
    private String data;
    private String lekarz_imie;
    private String lekarz_nazwisko;
    private String id;

    Appointment(String id,String godzina_rozpoczecia,String godzina_zakonczenia,String data,String lekarz_imie,String lekarz_nazwisko){
        this.godzina_rozpoczecia=godzina_rozpoczecia;
        this.godzina_zakonczenia=godzina_zakonczenia;
        this.data=data;
        this.lekarz_imie=lekarz_imie;
        this.lekarz_nazwisko=lekarz_nazwisko;
        this.id=id;
    }

    public String godzina_rozpoczecia(){return this.godzina_rozpoczecia;}
    public String godzina_zakonczenia(){return this.godzina_zakonczenia;}
    public String data(){return this.data;}
    public String lekarz(){return this.lekarz_imie+" "+this.lekarz_nazwisko;}
    public String id(){return this.id;}
}
