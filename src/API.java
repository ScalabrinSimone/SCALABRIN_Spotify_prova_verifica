import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.PreparedStatement;

public class API {
    private final HttpClient client;
    private final String ENDPOINT_BASE = "http://localhost:4567/api/";
    private final Gson deserializzatore;

    public API()
    {
        this.client = HttpClient.newHttpClient();
        this.deserializzatore = new Gson();
    } //Creazione del client

    // ========== Artista ==========

    // GET /Artista
    public String fetchArtisti() {
        //Build richiesta
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT_BASE + "artisti"))
                .GET()
                .build();

        //Build risposta: il client manda la richiesta
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); //BodyHandelers è come gestitre il corpo.
            //String serve per dire che ritorna una stringa.

            Artista[] artisti = deserializzatore.fromJson(response.body(), Artista[].class);

            StringBuilder result = new StringBuilder();
            for (Artista artista : artisti) { //Foreach in java
                result.append(artista.toString());
            }

            return result.toString();
        }catch (IOException | InterruptedException e) //Carattere pipe |
        {
            System.err.println("Errore in riocheista API: " + e.getMessage());
            return null;
        }

    }

    // GET /Artista/{id}
    public String fetchArtista(int id) {
        //Build richiesta
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT_BASE + "artisti/" + id))
                .GET()
                .build();

        //Build risposta: il client manda la richiesta
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); //BodyHandelers è come gestitre il corpo.
            //String serve per dire che ritorna una stringa.

            Gson deserializzatore = new Gson();
            Artista artista = deserializzatore.fromJson(response.body(), Artista.class);

            return artista.toString();
        }catch (IOException | InterruptedException e) //Carattere pipe |
        {
            System.err.println("Errore in riocheista API: " + e.getMessage());
            return null;
        }

    }

    // GET /Artista/{id}/canzoni

    // POST /Artista  (crea Artista)

    // PUT /Artista/{id} (aggiorna)

    // DELETE /Artista/{id}


    // ========== CANZONI ==========

    // GET /canzoni

    // GET /canzoni/{id}
}
