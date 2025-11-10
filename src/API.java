import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

            Artista artista = deserializzatore.fromJson(response.body(), Artista.class);

            return artista.toString();
        }catch (IOException | InterruptedException e) //Carattere pipe |
        {
            System.err.println("Errore in riocheista API: " + e.getMessage());
            return null;
        }

    }

    // GET /Artista/{id}/canzoni
    public Canzone[] fetchjCanzoniArtista(int id) throws IOException, InterruptedException
    {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT_BASE + "artisti/" + id + "/canzoni"))
                .GET()
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 200 || response.statusCode() == 201)
        {
            return deserializzatore.fromJson(response.body(), Canzone[].class);
        }
        else
            return null;

    }

    // POST /Artista  (crea Artista)
    public boolean createArtista(Artista artista) throws IOException, InterruptedException
    {
        String body = deserializzatore.toJson(artista);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT_BASE + "artisti"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json") //Ritorna uno status code
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.statusCode() == 200 || response.statusCode() == 201;
    }
    // PUT /Artista/{id} (aggiorna)

    // DELETE /Artista/{id}


    // ========== CANZONI ==========

    // GET /canzoni

    // GET /canzoni/{id}
}
