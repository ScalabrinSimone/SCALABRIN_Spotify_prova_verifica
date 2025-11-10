import java.util.List;

/**
 * Rappresentazione di un Artista come nell'API.
 * Usato sia per deserializzare (Gson) che per salvare nel DB (tramite DAO).
 */
public class Artista {
    public Integer id;      // id può essere null per nuovi artisti (prima del POST)
    public String nome;
    public String paese;
    public String genere;
    public List<Canzone> canzoni; // list of songs (può essere null quando non presente)

    public Artista() {}

    public Artista(String nome, String paese, String genere) {
        this.nome = nome;
        this.paese = paese;
        this.genere = genere;
    }
    public Artista(Integer id, String nome, String paese, String genere) {
        this.id = id;
        this.nome = nome;
        this.paese = paese;
        this.genere = genere;
    }

    @Override
    public String toString() {
        return "Artista{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", paese='" + paese + '\'' +
                ", genere='" + genere + '\'' +
                ", canzoni=" + (canzoni == null ? "[]" : canzoni.size()) +
                '}';
    }
}
