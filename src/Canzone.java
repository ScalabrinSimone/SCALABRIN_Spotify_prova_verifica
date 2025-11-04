public class Canzone {
    private int id;
    private String titolo;
    private int durata;
    private int annoPubblicazione;
    private Artista artista;

    @Override
    public String toString() {
        return id + "\t" + titolo + "\t" + durata + "\t" + annoPubblicazione + "\t" + artista.toString();
    }
}
