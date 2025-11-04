import java.util.List;

public class Artista {
    //Entità per deserializzare

    private int id;
    private String nome;
    private String paese;
    private String genere;
    private Canzone[] canzoni;

    @Override
    public String toString() {
        return id + "\t" + nome + "\t" + paese + "\t" + genere + "\n";
    }
}
