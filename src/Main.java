import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static Database db;
    private static API api;

    public static void main(String[] args) {

        api = new API(); //Client API //cliente ape bzzz
        try
        {
            db = Database.getInstance(); //Singleton DB
        }
        catch (Exception e)
        {
            System.err.println("Errore di connessione al database");
            throw new RuntimeException(e);
        }

        /*TEST:
        // Recupero artista con ID 1 (esempio)
        Artista a = api.getArtistaCompleto(1);

        if (a != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(a.toString());

            for (Canzone c : a.canzoni) {
                sb.append("\t -> ").append(c.toString()).append("\n");
            }

            System.out.println(sb);
        }*/

        Scanner sc = new Scanner(System.in); //Scanner

        stampaHeader();
        while(true)
        {
            stampaMenu();
            System.out.println("Inserici la tua scelta: ");
            String scelta = sc.nextLine().trim(); //Serve String per evitare problemi di exception con lo scanner

            switch (scelta)
            {
                case "1":
                    break;
                case "2":
                    break;
                case "3":
                    System.out.println("Quante canzoni vuoi inserire?");
                    break;
                case "4":
                    System.out.printf(api.fetchArtisti());
                    break;
                case "5":
                    System.out.println("Inserisci l'id dell'artista da visualizzare: ");
                    //Controllo per numero intero
                    while (!sc.hasNextInt()) // se NON è un numero intero
                    {
                        System.out.println("Errore, inserisci un numero valido!");
                        sc.next(); //Scarta l'input sbagliato
                        System.out.println("Inserisci l'id dell'artista da visualizzare: ");
                    }

                    System.out.printf(api.fetchArtista(sc.nextInt()));
                    sc.nextLine(); //Svuotiamo il buffer
                    break;
                case "6":
                    Artista a = null;

                    System.out.println("Inserisci il nome dell'artista: ");
                    a.nome = sc.nextLine();
                    sc.next(); //Scarta l'input

                    System.out.println("Inserisci il genere dell'artista: ");
                    a.genere = sc.nextLine();
                    sc.next(); //Scarta l'input

                    System.out.println("Inserisci il paese dell'artista");
                    a.paese = sc.nextLine();
                    sc.next();

                    System.out.println("Inserisci l'id dell'artista: ");
                    a.id = sc.nextInt();
                    sc.next();

                    try
                    {
                        if (api.createArtista(a))
                            System.out.println("Artista a creato con successo!\nOra se vuoi vai a inserire qualche canzone ;)");
                        else
                            System.out.println("Artista non creato");

                    }
                    catch (IOException | InterruptedException e)
                    {
                        System.err.println("Errore nella creazione dell'artista: " + e.getMessage());
                    }

                    break;
                case "7":
                    break;
                case "8":
                    break;
                case "9":
                    break;
                case "10":
                    System.out.println("Arrivederci!");
                    db.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Scelta non valida, riprova...");
                    break;
            }
        }
    }

    public static void stampaHeader()
    {
        System.out.println("==Benvenuto nella tua lista Spotify==");
    }

    public static void stampaMenu()
    {
        System.out.println("\nMenu Spotify:");
        System.out.println("1) Esplora il catalogo (GET /canzoni)");
        System.out.println("2) Cerca canzone con id (GET /canzoni/{id})");
        System.out.println("3) Aggiungi una canzone ad un artista con id (POST /artista/{id}/canzoni)");
        System.out.println("4) Esplora gli artisti (GET /artisti)");
        System.out.println("5) Esplora un artista con id (GET /artisti/{id})");
        System.out.println("6) Aggiungi un artista (POST /artisti)");
        System.out.println("7) Modifica un artista con id (PUT /artisti/{id})");
        System.out.println("8) Elimina un artista con id (DELETE /artisti/{id})");
        System.out.println("9) Consulta il database locale");
        System.out.println("10) Esci");
    }
}