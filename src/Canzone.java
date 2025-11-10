public class Canzone {

    //Proprietá pubbliche per risparmiare linee di codice
    public int id;
    public String titolo;
    public int durata; //In secondi
    public int annoPubblicazione;
    public Artista artista; //Annidato

    public Canzone() {}

    @Override
    public String toString() {
        return id + "\t" + titolo + "\t" + durata + "\t" + annoPubblicazione + "\t" + artista.toString();
    }
}
