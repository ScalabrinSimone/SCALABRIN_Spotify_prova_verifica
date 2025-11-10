/**
 * Rappresentazione di una Canzone, come definita dalla API README.
 */
public class Canzone {
    public Integer id;
    public String titolo;
    public Integer durata; // in secondi
    public Integer annoPubblicazione;
    public Artista artista; // annidato

    public Canzone() {}

    @Override
    public String toString() {
        return "Canzone{" +
                "id=" + id +
                ", titolo='" + titolo + '\'' +
                ", durata=" + durata +
                ", annoPubblicazione=" + annoPubblicazione +
                ", artista=" + (artista != null ? artista.nome : "null") +
                '}';
    }
}