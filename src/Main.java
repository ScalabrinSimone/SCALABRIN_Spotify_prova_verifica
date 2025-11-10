import javax.xml.crypto.Data;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static Database db;
    private static API api;

    public static void main(String[] args) {

        api = new API(); //Client API
        try
        {
            db = Database.getInstance(); //Singleton DB
        }
        catch (Exception e)
        {
            System.err.println("Errore di connessione al database");
            throw new RuntimeException(e);
        }

        Scanner sc = new Scanner(System.in); //Scanner

        /*//Debug:
        System.out.printf(api.fetchArtisti());
        System.out.printf(api.fetchArtista(1));*/
    }

    public static void stampaHeader()
    {
        System.out.println("==Benvenuto nella tua lista Spotify==");
    }

    public static void stampaMenu()
    {
        System.out.println("1)...");
    }
}