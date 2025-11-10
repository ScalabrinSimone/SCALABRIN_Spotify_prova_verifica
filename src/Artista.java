import java.util.List; //Per lista di canzoni

public class Artista {
    //Entità per deserializzare
    //Proprietá pubbliche per risparmiare linee di codice
    public int id;
    public String nome;
    public String paese;
    public String genere;
    public Canzone[] canzoni;

    //Costruttori
    public Artista(int id, String nome, String paese, String genere, Canzone[] canzoni)
    {
        this.id = id;
        this.nome = nome;
        this.paese = paese;
        this.genere = genere;
        this.canzoni = canzoni;
    }
    public Artista(String nome, String paese, String genere, Canzone[] canzoni)
    {
        this.nome = nome;
        this.paese = paese;
        this.genere = genere;
        this.canzoni = canzoni;
    }
    public Artista(String nome, String paese, String genere)
    {
        this.nome = nome;
        this.paese = paese;
        this.genere = genere;
    }

    @Override
    public String toString() {
        return id + "\t" + nome + "\t" + paese + "\t" + genere + "\n";
    }
}
