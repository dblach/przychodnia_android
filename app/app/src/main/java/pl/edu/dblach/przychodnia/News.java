package pl.edu.dblach.przychodnia;

public class News{
    private String nazwa;
    private String tresc;
    private String obraz;
    private String data_dodania;

    News(String nazwa,String tresc,String obraz,String data_dodania){
        this.nazwa=nazwa;
        this.tresc=tresc;
        this.obraz=obraz;
        this.data_dodania=data_dodania;
    }

    public String nazwa(){return this.nazwa;}
    public String tresc(){return this.tresc;}
    public String obraz(){return this.obraz;}
    public String data_dodania(){return this.data_dodania;}
}
