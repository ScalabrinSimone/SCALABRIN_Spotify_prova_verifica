
import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Main: menu a console per interagire con API e DB locale.
 *
 * Ragionamento generale:
 * - L'app è divisa in livelli:
 *    - API -> comunica con API esterna (operazioni REST). Questo layer non tocca il DB.
 *    - Database -> layer locale con pattern Singleton (gestisce la collezione personale).
 *    - Main -> orchestration: mostra menu, invoca API o Database a seconda dell'azione.
 *
 * - CRUD:
 *    - API: create/update/delete Artistai via API -> corrisponde ad un CRUD remoto.
 *    - Locale: salvataggio/lettura/aggiornamento/eliminazione degli Artistai salvati -> CRUD locale su SQLite.
 */

public class Main {
    private static final String BASE_URL = "http://localhost:4567/api"; // come da README
    private static API api;
    private static Database db;

    public static void main(String[] args) {
        api = new API(BASE_URL);
        db = Database.getInstance(); // Singleton DB
        Scanner sc = new Scanner(System.in);

        printHeader();
        while (true) {
            printMenu();
            System.out.print("Scelta> ");
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> listAllCanzones();
                    case "2" -> getCanzoneById(sc);
                    case "3" -> listAllArtistas();
                    case "4" -> viewArtistaDetailsAndOptionToSave(sc);
                    case "5" -> createArtista(sc);
                    case "6" -> updateArtista(sc);
                    case "7" -> deleteArtista(sc);
                    case "8" -> viewLocalCollection();
                    case "9" -> {
                        System.out.println("Esco. Arrivederci!");
                        db.close();
                        System.exit(0);
                    }
                    default -> System.out.println("Scelta non valida.");
                }
            } catch (Exception e) {
                System.err.println("Errore: " + e.getMessage());
                // stampa stack solo per debug in verifiche scolastiche:
                e.printStackTrace();
            }
        }
    }

    private static void printHeader() {
        System.out.println("=== Gestione Musicale - Client per API Locale ===");
        System.out.println("Usa l'API locale su " + BASE_URL);
        System.out.println("Java 21, Gson, SQLite (xerial).");
        System.out.println();
    }

    private static void printMenu() {
        System.out.println("\nMENU:");
        System.out.println("1) Esplora catalogo completo di canzoni (GET /canzoni)");
        System.out.println("2) Cerca canzone tramite ID (GET /canzoni/{id})");
        System.out.println("3) Consulta elenco Artistai (GET /Artistai)");
        System.out.println("4) Visualizza dettagli Artistaa e salva nel DB locale (GET /Artistai/{id})");
        System.out.println("5) Aggiungi nuovo Artistaa al sistema (POST /Artistai)");
        System.out.println("6) Modifica Artistaa esistente (PUT /Artistai/{id})");
        System.out.println("7) Elimina Artistaa dal catalogo (DELETE /Artistai/{id})");
        System.out.println("8) Consulta la collezione locale salvata nel DB");
        System.out.println("9) Esci");
    }

    private static void listAllCanzones() throws IOException, InterruptedException {
        System.out.println("--- Catalogo Canzoni ---");
        List<Canzone> Canzones = api.getAllCanzones();
        for (Canzone s : Canzones) {
            System.out.printf("ID:%d - %s (%d) - Artistaa: %s\n", s.id, s.titolo, s.annoPubblicazione, s.artista != null ? s.artista.nome : "N/A");
        }
        System.out.println("--- fine lista ---");
    }

    private static void getCanzoneById(Scanner sc) throws IOException, InterruptedException {
        System.out.print("Inserisci ID canzone: ");
        String in = sc.nextLine().trim();
        try {
            int id = Integer.parseInt(in);
            Canzone s = api.getCanzoneById(id);
            if (s == null) System.out.println("Canzone non trovata.");
            else System.out.println("Dettaglio: " + s);
        } catch (NumberFormatException ex) {
            System.out.println("ID non numerico.");
        }
    }

    private static void listAllArtistas() throws IOException, InterruptedException {
        System.out.println("--- Elenco Artistai (API) ---");
        List<Artista> Artistas = api.getAllArtistas();
        for (Artista a : Artistas) {
            System.out.printf("ID:%d - %s (%s, %s) - %d canzoni\n",
                    a.id, a.nome, a.paese, a.genere, a.canzoni == null ? 0 : a.canzoni.size());
        }
        System.out.println("--- fine lista ---");
    }

    private static void viewArtistaDetailsAndOptionToSave(Scanner sc) throws IOException, InterruptedException, SQLException {
        System.out.print("Inserisci ID Artistaa: ");
        String in = sc.nextLine().trim();
        int id = Integer.parseInt(in);
        Artista a = api.getArtistaById(id);
        if (a == null) {
            System.out.println("Artistaa non trovato (404).");
            return;
        }
        // Mostro dettagli in console
        System.out.println("Dettaglio Artistaa:\n" + a);
        /*// Apro una finestra Swing per vedere i dettagli (UI)
        SwingUtilities.invokeLater(() -> {
            ArtistaDetailFrame frame = new ArtistaDetailFrame(a);
            frame.setVisible(true);
        });*/

        System.out.print("Vuoi salvare questo Artistaa nella collezione locale? (s/n): ");
        String save = sc.nextLine().trim();
        if (save.equalsIgnoreCase("s")) {
            int localId = db.saveArtista(a);
            System.out.println("Artistaa salvato localmente con id locale: " + localId);
        }
    }

    private static void createArtista(Scanner sc) throws IOException, InterruptedException {
        System.out.println("Creazione nuovo Artistaa (POST /Artistai). Inserisci i campi richiesti.");
        System.out.print("Nome: "); String nome = sc.nextLine().trim();
        System.out.print("Paese: "); String paese = sc.nextLine().trim();
        System.out.print("Genere: "); String genere = sc.nextLine().trim();

        Artista a = new Artista(nome, paese, genere);
        Artista created = api.createArtista(a);
        System.out.println("Creato Artistaa: " + created);
    }

    private static void updateArtista(Scanner sc) throws IOException, InterruptedException {
        System.out.print("Inserisci ID Artistaa da aggiornare: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Nuovo nome: "); String nome = sc.nextLine().trim();
        System.out.print("Nuovo paese: "); String paese = sc.nextLine().trim();
        System.out.print("Nuovo genere: "); String genere = sc.nextLine().trim();

        Artista a = new Artista(nome, paese, genere);
        Artista updated = api.updateArtista(id, a);
        if (updated == null) {
            System.out.println("Artistaa non trovato, update fallito (404).");
        } else {
            System.out.println("Artistaa aggiornato: " + updated);
        }
    }

    private static void deleteArtista(Scanner sc) throws IOException, InterruptedException {
        System.out.print("Inserisci ID Artistaa da eliminare: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        boolean ok = api.deleteArtista(id);
        if (ok) System.out.println("Artistaa eliminato correttamente (204).");
        else System.out.println("Artistaa non trovato (404).");
    }

    private static void viewLocalCollection() throws SQLException {
        System.out.println("--- Collezione locale (DB) ---");
        var list = db.getAllSavedArtistas();
        for (var a : list) {
            System.out.printf("Nome: %s - Paese: %s - Genere: %s\n", a.nome, a.paese, a.genere);
        }
        System.out.println("--- fine collezione ---");
    }
}